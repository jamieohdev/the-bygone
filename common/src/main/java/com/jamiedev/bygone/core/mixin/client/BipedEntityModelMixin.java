package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.client.BygoneClient;
import com.jamiedev.bygone.common.item.VerdigrisBladeItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> {
    @Shadow
    protected abstract void poseRightArm(T entity);

    @Shadow protected abstract void poseLeftArm(T entity);

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel;setupAttackAnimation(Lnet/minecraft/world/entity/LivingEntity;F)V", shift = At.Shift.BEFORE))
    private void bygone$setBlockingAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!BygoneClient.isWeaponBlocking(livingEntity))
            return;
        if (livingEntity.getOffhandItem().getItem() instanceof VerdigrisBladeItem)
            this.poseRightArm(livingEntity);
        else
            this.poseLeftArm(livingEntity);
    }
}