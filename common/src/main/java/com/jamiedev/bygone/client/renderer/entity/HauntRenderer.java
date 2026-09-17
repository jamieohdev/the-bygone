package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.HauntModel;
import com.jamiedev.bygone.common.entity.HauntEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class HauntRenderer extends HauntingsMobRenderer<HauntEntity, HauntModel<HauntEntity>> {

	private static final ResourceLocation HAUNT_LOCATION = Bygone.id("textures/entity/haunt.png");
	private static final ResourceLocation HAUNT_BEAM_LOCATION = Bygone.id("textures/entity/haunt_beam.png");
	private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(HAUNT_BEAM_LOCATION);

	private static final double TARGET_HEIGHT_MULTIPLIER = 0.5D;

	private static final float BEAM_HEIGHT = 0.5F;
	private static final float BEAM_TEXTURE_SCROLL_SPEED = 0.5F;
	private static final float BEAM_ROTATION_SPEED = 0.05F;
	private static final float BEAM_ROTATION_MULTIPLIER = -1.5F;

	private static final float TEXTURE_U_OFFSET = 0.5F;
	private static final float TEXTURE_U_ZERO = 0F;
	private static final float TEXTURE_V_BASE = -1F;

	public HauntRenderer(EntityRendererProvider.Context context) {
		super(context, new HauntModel<>(context.bakeLayer(JamiesModModelLayers.HAUNT)), 0.7F);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull HauntEntity entity) {
		return HAUNT_LOCATION;
	}

	@Override
	protected int getBlockLightLevel(@NotNull HauntEntity entity, @NotNull BlockPos pos) {
		return 15;
	}

	@Override
	public void render(@NotNull HauntEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
		LivingEntity target = entity.getActiveAttackTarget();
		if (target != null) this.renderBeam(entity, target, partialTicks, poseStack, buffer, packedLight);
	}

	public void renderBeam(@NotNull HauntEntity entity, LivingEntity target, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packetLight) {
		float timeInTicks = entity.getClientSideAttackTime() + partialTicks;
		float texVOff = timeInTicks * BEAM_TEXTURE_SCROLL_SPEED % 1F;
		poseStack.pushPose();
		poseStack.translate(0, entity.getEyeHeight(), 0);

		Vec3 targetPosition = this.getPosition(target, (double)target.getBbHeight() * TARGET_HEIGHT_MULTIPLIER, partialTicks);
		Vec3 eyePosition = this.getPosition(entity, entity.getEyeHeight(), partialTicks);
		Vec3 beamVector = targetPosition.subtract(eyePosition);

		float length = (float) beamVector.length();
		beamVector = beamVector.normalize();

		double xRot = Math.acos(beamVector.y);
		double yRot = (Math.PI / 2) - Math.atan2(beamVector.z, beamVector.x);

		poseStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(yRot)));
		poseStack.mulPose(Axis.XP.rotationDegrees((float) Math.toDegrees(xRot)));

		float rot = timeInTicks * BEAM_ROTATION_SPEED * BEAM_ROTATION_MULTIPLIER;
		float cosRot = Mth.cos(rot) * BEAM_HEIGHT / 2;
		float sinRot = Mth.sin(rot) * BEAM_HEIGHT / 2;

		float minV = TEXTURE_V_BASE + texVOff;
		float maxV = length + minV;

		VertexConsumer consumer = buffer.getBuffer(BEAM_RENDER_TYPE);
		PoseStack.Pose pose = poseStack.last();

		renderVortex(consumer, pose, -cosRot, length, -sinRot, TEXTURE_U_OFFSET, maxV, packetLight);
		renderVortex(consumer, pose, -cosRot, 0, -sinRot, TEXTURE_U_OFFSET, minV, packetLight);
		renderVortex(consumer, pose, cosRot, 0, sinRot, TEXTURE_U_ZERO, minV, packetLight);
		renderVortex(consumer, pose, cosRot, length, sinRot, TEXTURE_U_ZERO, maxV, packetLight);
		renderVortex(consumer, pose, -sinRot, length, cosRot, TEXTURE_U_OFFSET, maxV, packetLight);
		renderVortex(consumer, pose, -sinRot, 0, cosRot, TEXTURE_U_OFFSET, minV, packetLight);
		renderVortex(consumer, pose, sinRot, 0, -cosRot, TEXTURE_U_ZERO, minV, packetLight);
		renderVortex(consumer, pose, sinRot, length, -cosRot, TEXTURE_U_ZERO, maxV, packetLight);

		poseStack.popPose();
	}

	protected Vec3 getPosition(LivingEntity livingEntity, double yOffset, float partialTick) {
		double x = Mth.lerp(partialTick, livingEntity.xOld, livingEntity.getX());
		double y = Mth.lerp(partialTick, livingEntity.yOld, livingEntity.getY()) + yOffset;
		double z = Mth.lerp(partialTick, livingEntity.zOld, livingEntity.getZ());
		return new Vec3(x, y, z);
	}

	protected static void renderVortex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int packedLight) {
		consumer.addVertex(pose, x, y, z)
				.setColor(-1)
				.setUv(u, v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(packedLight)
				.setNormal(pose, 0, 1, 0);
	}

}