package com.jamiedev.mod.mixin;

import com.jamiedev.mod.common.entities.MoobooEntity;
import com.jamiedev.mod.fabric.init.JamiesModDimension;
import com.jamiedev.mod.fabric.init.JamiesModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CowEntity.class)
public abstract class CowEntityMixin extends AnimalEntity {

    protected int timeInBygone;
    protected CowEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt("TimeInOverworld", this.timeInBygone);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.timeInBygone = nbt.getInt("TimeInBygone");
    }

    protected void zombify(ServerWorld world) {
        MoobooEntity MoobooEntity = (MoobooEntity) this.convertTo(JamiesModEntityTypes.MOOBOO, true);
        if (MoobooEntity != null) {
            MoobooEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
        }
    }

    protected void mobTick() {
        super.mobTick();
        if (this.shouldZombify()) {
            ++this.timeInBygone;
        } else {
            this.timeInBygone = 0;
        }

        if (this.timeInBygone > 300) {
            this.zombify((ServerWorld)this.getWorld());
        }

    }

    @Unique
    public boolean shouldZombify() {
        return (this.getWorld().getRegistryKey() == JamiesModDimension.BYGONE_LEVEL_KEY) && !this.isAiDisabled();
    }
}
