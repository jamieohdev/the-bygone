package com.jamiedev.mod.mixin.client;

import com.jamiedev.mod.common.items.VerdigrisBladeItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jamiedev.mod.common.client.JamiesModClient;

@Mixin(HumanoidModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> {
    @Shadow
    protected abstract void positionRightArm(T entity);

    @Shadow protected abstract void positionLeftArm(T entity);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;animateArms(Lnet/minecraft/entity/LivingEntity;F)V", shift = At.Shift.BEFORE), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void swordblocking$setBlockingAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!JamiesModClient.isWeaponBlocking(livingEntity))
            return;
        if (livingEntity.getOffhandItem().getItem() instanceof VerdigrisBladeItem)
            this.positionRightArm(livingEntity);
        else
            this.positionLeftArm(livingEntity);
    }
}