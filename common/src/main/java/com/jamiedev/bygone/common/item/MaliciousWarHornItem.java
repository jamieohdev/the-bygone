package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.registry.BGDataComponents;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MaliciousWarHornItem extends Item {
    
    private static final int MAX_VEXES = 5;
    private static final int RECHARGE_TIME_SECONDS = 60;
    private static final int VEX_LIFETIME_SECONDS = 40;
    private static final int DEATH_PENALTY_SECONDS = 20;
    private static final int USE_DURATION = 10;
    
    public MaliciousWarHornItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            WarHornData data = itemStack.getOrDefault(BGDataComponents.WAR_HORN_DATA.value(), WarHornData.EMPTY);
            
            if (data.cooldownSeconds() <= 0) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(itemStack);
            } else {
                player.displayClientMessage(
                    Component.translatable("item.bygone.malicious_war_horn.cooldown", data.cooldownSeconds())
                        .withStyle(ChatFormatting.RED), 
                    true
                );
                return InteractionResultHolder.fail(itemStack);
            }
        }
        
        return InteractionResultHolder.consume(itemStack);
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide && user instanceof Player player) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                BGSoundEvents.WAR_HORN_USE.get(), SoundSource.RECORDS, 1.5F, 1.0F);
            level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
            
            spawnHornParticles(level, player);
            
            WarHornData data = releaseVexes(stack, level, player);
            stack.set(BGDataComponents.WAR_HORN_DATA.value(), data);
            
            player.awardStat(Stats.ITEM_USED.get(this));
            player.getCooldowns().addCooldown(this, 20);
        }
        
        return stack;
    }
    
    private WarHornData releaseVexes(ItemStack stack, Level level, Player player) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return stack.getOrDefault(BGDataComponents.WAR_HORN_DATA.value(), WarHornData.EMPTY);
        }
        
        Set<UUID> vexIds = new HashSet<>();
        
        for (int i = 0; i < MAX_VEXES; i++) {
            double angle = (2 * Math.PI / MAX_VEXES) * i;
            double x = player.getX() + Math.cos(angle) * 2;
            double z = player.getZ() + Math.sin(angle) * 2;
            double y = player.getY() + 1;
            
            Vex vex = EntityType.VEX.create(serverLevel);
            if (vex != null) {
                vex.moveTo(x, y, z, player.getYRot(), 0.0F);
                vex.setBoundOrigin(player.blockPosition());
                vex.setLimitedLife(VEX_LIFETIME_SECONDS * 20);
                vex.setPersistenceRequired();
                
                serverLevel.addFreshEntity(vex);
                vexIds.add(vex.getUUID());
            }
        }
        
        return new WarHornData(new ArrayList<>(vexIds), RECHARGE_TIME_SECONDS, VEX_LIFETIME_SECONDS);
    }
    
    private void spawnHornParticles(Level level, Player player) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        
        Vec3 lookDirection = player.getLookAngle();
        double hornX = player.getX() + lookDirection.x * 0.5;
        double hornY = player.getY() + player.getEyeHeight() - 0.2;
        double hornZ = player.getZ() + lookDirection.z * 0.5;
        
        for (int i = 0; i < 100; i++) {
            double angle = 2 * Math.PI * level.random.nextDouble();
            double radius = level.random.nextDouble() * 3.0;
            double height = (level.random.nextDouble() - 0.5) * 2.0;
            
            double offsetX = Math.cos(angle) * radius * 0.3;
            double offsetY = height * 0.3;
            double offsetZ = Math.sin(angle) * radius * 0.3;
            
            serverLevel.sendParticles(ParticleTypes.SOUL,
                hornX, hornY, hornZ,
                1, offsetX, offsetY, offsetZ, 0.1);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player && level.getGameTime() % 20 == 0) {
            WarHornData data = stack.getOrDefault(BGDataComponents.WAR_HORN_DATA.value(), WarHornData.EMPTY);
            
            boolean needsUpdate = false;
            int newCooldown = Math.max(0, data.cooldownSeconds() - 1);
            int newVexTime = Math.max(0, data.vexTimeLeft() - 1);
            
            if (newCooldown != data.cooldownSeconds() || newVexTime != data.vexTimeLeft()) {
                needsUpdate = true;
            }
            
            List<UUID> activeVexes = data.activeVexes();
            
            if (newVexTime <= 0 && !activeVexes.isEmpty()) {
                for (UUID vexId : activeVexes) {
                    Entity vexEntity = ((ServerLevel) level).getEntity(vexId);
                    if (vexEntity instanceof Vex vex) {
                        vex.discard();
                    }
                }
                activeVexes = new ArrayList<>();
                newVexTime = 0;
                needsUpdate = true;
            }
            
            if (needsUpdate) {
                WarHornData newData = new WarHornData(activeVexes, newCooldown, newVexTime);
                stack.set(BGDataComponents.WAR_HORN_DATA.value(), newData);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        
        tooltipComponents.add(Component.translatable("item.bygone.malicious_war_horn.desc1")
            .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item.bygone.malicious_war_horn.desc2")
            .withStyle(ChatFormatting.GRAY));
        
        WarHornData data = stack.getOrDefault(BGDataComponents.WAR_HORN_DATA.value(), WarHornData.EMPTY);
        
        if (!data.activeVexes().isEmpty()) {
            tooltipComponents.add(Component.translatable("item.bygone.malicious_war_horn.vexes_active", data.activeVexes().size())
                .withStyle(ChatFormatting.AQUA));
        }
        
        if (data.cooldownSeconds() > 0) {
            tooltipComponents.add(Component.translatable("item.bygone.malicious_war_horn.cooldown_remaining", data.cooldownSeconds())
                .withStyle(ChatFormatting.RED));
        } else {
            tooltipComponents.add(Component.translatable("item.bygone.malicious_war_horn.ready")
                .withStyle(ChatFormatting.GREEN));
        }
    }
    
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION;
    }
    
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }
    
    @Override
    public boolean isFoil(ItemStack stack) {
        WarHornData data = stack.getOrDefault(BGDataComponents.WAR_HORN_DATA.value(), WarHornData.EMPTY);
        return !data.activeVexes().isEmpty();
    }
    
    public static void onVexDeath(Vex vex, ItemStack hornStack) {
        if (hornStack.getItem() instanceof MaliciousWarHornItem) {
            WarHornData data = hornStack.getOrDefault(BGDataComponents.WAR_HORN_DATA.value(), WarHornData.EMPTY);
            
            if (data.activeVexes().contains(vex.getUUID())) {
                List<UUID> newVexes = new ArrayList<>(data.activeVexes());
                newVexes.remove(vex.getUUID());
                
                WarHornData newData = new WarHornData(
                    newVexes,
                    data.cooldownSeconds() + DEATH_PENALTY_SECONDS,
                    data.vexTimeLeft()
                );
                
                hornStack.set(BGDataComponents.WAR_HORN_DATA.value(), newData);
            }
        }
    }
    
    public record WarHornData(List<UUID> activeVexes, int cooldownSeconds, int vexTimeLeft) {
        public static final WarHornData EMPTY = new WarHornData(new ArrayList<>(), 0, 0);
    }
}