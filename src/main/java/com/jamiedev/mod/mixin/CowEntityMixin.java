package com.jamiedev.mod.mixin;

import com.jamiedev.mod.common.entities.MoobooEntity;
import com.jamiedev.mod.fabric.init.JamiesModDimension;
import com.jamiedev.mod.fabric.init.JamiesModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Cow.class)
public abstract class CowEntityMixin extends Animal {

    protected int timeInBygone;
    protected CowEntityMixin(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putInt("TimeInOverworld", this.timeInBygone);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.timeInBygone = nbt.getInt("TimeInBygone");
    }

    protected void zombify(ServerLevel world) {
        MoobooEntity MoobooEntity = this.convertTo(JamiesModEntityTypes.MOOBOO, true);
        if (MoobooEntity != null) {
            MoobooEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.shouldZombify()) {
            ++this.timeInBygone;
        } else {
            this.timeInBygone = 0;
        }

        if (this.timeInBygone > 300) {
            this.zombify((ServerLevel)this.level());
        }

    }

    @Unique
    public boolean shouldZombify() {
        return (this.level().dimension() == JamiesModDimension.BYGONE_LEVEL_KEY) && !this.isNoAi();
    }
}
