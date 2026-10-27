package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.ReaverAnimations;
import com.jamiedev.bygone.client.renderer.SpectralRenderUtil;
import com.jamiedev.bygone.common.entity.ReaverEntity;
import com.jamiedev.bygone.core.registry.BGRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class ReaverModel<T extends Entity> extends HauntingsMobModel<T> {
	private final ModelPart Body;
	private final ModelPart LowerJaw;
	private final ModelPart eye;
	private final ModelPart UpperJaw;
	private final ModelPart LeftArm;
	private final ModelPart LowerLeftArm;
	private final ModelPart LeftClaw;
	private final ModelPart RightArm;
	private final ModelPart LowerRightArm;
	private final ModelPart RightClaw;
	private final List<ModelPart> poseParts;
	private final Map<ReaverEntity, PoseState> poseStates = new WeakHashMap<>();
	private PoseState renderPoseState;
	private float spectralAlpha = 1.0F;
	private boolean depthOnly;
	private MultiBufferSource bodyBuffer;
	private ResourceLocation bodyTexture;

	public ReaverModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.LowerJaw = this.Body.getChild("LowerJaw");
		this.eye = this.Body.getChild("eye");
		this.UpperJaw = this.Body.getChild("UpperJaw");
		this.LeftArm = this.Body.getChild("LeftArm");
		this.LowerLeftArm = this.LeftArm.getChild("LowerLeftArm");
		this.LeftClaw = this.LowerLeftArm.getChild("Hand_r1");
		this.RightArm = this.Body.getChild("RightArm");
		this.LowerRightArm = this.RightArm.getChild("LowerRightArm");
		this.RightClaw = this.LowerRightArm.getChild("Hand_r2");
		this.poseParts = this.Body.getAllParts().toList();
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 21.6F, 0.0F));

		PartDefinition LowerJaw = Body.addOrReplaceChild("LowerJaw", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, 0.0F, -18.0F, 18.0F, 11.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 88).addBox(-8.5F, -0.5F, -17.5F, 17.0F, 11.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 9.0F));

		PartDefinition eye = Body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(80, 12).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 2.0F));

		PartDefinition UpperJaw = Body.addOrReplaceChild("UpperJaw", CubeListBuilder.create().texOffs(0, 29).addBox(-9.0F, -3.0F, -18.0F, 18.0F, 7.0F, 18.0F, new CubeDeformation(0.02F))
		.texOffs(56, 82).addBox(-8.5F, -2.5F, -17.5F, 17.0F, 7.0F, 17.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, -11.0F, 9.0F, -0.6545F, 0.0F, 0.0F));

		PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 54).addBox(-8.0F, -4.0F, -12.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -6.0F, 4.0F, 1.5708F, 0.0436F, 0.0F));

		PartDefinition LowerLeftArm = LeftArm.addOrReplaceChild("LowerLeftArm", CubeListBuilder.create().texOffs(0, 78).addBox(-2.0F, -2.0F, -6.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, -12.0F));

		PartDefinition Hand_r1 = LowerLeftArm.addOrReplaceChild("Hand_r1", CubeListBuilder.create().texOffs(72, 14).addBox(0.0F, -3.0F, -14.0F, 0.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, -0.12F, -1.5708F));

		PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(48, 54).addBox(0.0F, -4.0F, -12.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -6.0F, 4.0F, 1.5708F, -0.0436F, 0.0F));

		PartDefinition LowerRightArm = RightArm.addOrReplaceChild("LowerRightArm", CubeListBuilder.create().texOffs(20, 78).addBox(-2.0F, -2.0F, -6.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -12.0F));

		PartDefinition Hand_r2 = LowerRightArm.addOrReplaceChild("Hand_r2", CubeListBuilder.create().texOffs(72, 23).addBox(0.0F, -3.0F, -14.0F, 0.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 0.12F, 1.5708F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public @NotNull ModelPart root() {
		return this.Body;
	}

	@Override
	public Optional<ModelPart> getAnyDescendantWithName(String name) {
		return "Body".equals(name) ? Optional.of(this.Body) : super.getAnyDescendantWithName(name);
	}

	public void setDepthOnly(boolean depthOnly) {
		this.depthOnly = depthOnly;
	}

	public void setBodyRenderBuffer(@Nullable MultiBufferSource buffer, @Nullable ResourceLocation texture) {
		this.bodyBuffer = buffer;
		this.bodyTexture = texture;
	}

	@Override
	public void setupAnim(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.spectralAlpha = SpectralRenderUtil.healthAlpha(entity);
		this.renderPoseState = null;

		if (entity instanceof ReaverEntity reaverEntity) {
			ReaverEntity.AttackPhase phase = reaverEntity.getAttackPhase();
			if (phase == ReaverEntity.AttackPhase.IDLE) {
				this.animate(reaverEntity.idleAnimationState, ReaverAnimations.IDLE, ageInTicks, 1.0F);
			}
			this.animate(reaverEntity.idleAnimationState, ReaverAnimations.BREATHING, ageInTicks, 1.0F);
			this.animate(reaverEntity.floatAnimationState, ReaverAnimations.FLOAT, ageInTicks, 1.0F);
			this.animate(reaverEntity.meleeAnimationState, ReaverAnimations.ATTACK, ageInTicks, 1.0F);
			float elapsed = reaverEntity.getAttackPhaseTicks(ageInTicks - reaverEntity.tickCount);
			if (phase == ReaverEntity.AttackPhase.RECOVERY && reaverEntity.isTantrumRecovery()) {
				float grounded = smooth((elapsed - ReaverEntity.RECOVERY_DROP_START)
						/ (ReaverEntity.RECOVERY_LAND_TICK - ReaverEntity.RECOVERY_DROP_START))
						* (1 - smooth((elapsed - ReaverEntity.RECOVERY_SPIN_START) / 4.0F));
				this.Body.y = Mth.lerp(grounded, this.Body.y, this.Body.getInitialPose().y);
			}
			this.applyAttackPose(phase, elapsed,
					reaverEntity.getClawPattern(), reaverEntity.getRecoverySource());
			int key = phase.ordinal() * 8
					+ (reaverEntity.idleAnimationState.isStarted() ? 1 : 0)
					+ (reaverEntity.floatAnimationState.isStarted() ? 2 : 0)
					+ (reaverEntity.meleeAnimationState.isStarted() ? 4 : 0);
			float duration = switch (phase) {
				case IDLE -> 4;
				case FLURRY -> 0.6F;
				case LUNGE, CHOMP -> 2;
				default -> 3;
			};
			this.renderPoseState = this.poseStates.computeIfAbsent(reaverEntity, ignored -> new PoseState());
			this.loadPose(updatePose(this.renderPoseState, key, ageInTicks, duration, this.capturePose()));
		}
	}

	private void applyAttackPose(ReaverEntity.AttackPhase phase, float elapsed,
			int clawPattern, ReaverEntity.AttackPhase recoverySource) {
		switch (phase) {
			case WINDUP -> {
				float progress = Mth.clamp(elapsed / ReaverEntity.WINDUP_TICKS, 0, 1);
				this.Body.xRot -= progress * 0.2F;
				this.Body.z += progress * 2.0F;
				this.UpperJaw.xRot -= progress * 0.45F;
				this.LowerJaw.xRot += progress * 0.5F;
				this.applyLungePose(progress);
			}
			case LUNGE, FLURRY -> {
				this.Body.xRot += 0.2F;
				this.UpperJaw.xRot -= 0.3F;
				this.LowerJaw.xRot += 0.35F;
				if (phase == ReaverEntity.AttackPhase.LUNGE) {
					this.applyLungePose();
				} else {
					this.applyFlurryBodyPose(elapsed, clawPattern, 1.0F);
					this.applyClawPose(elapsed, clawPattern);
				}
			}
			case RECOVERY -> this.applyRecoveryPose(elapsed, clawPattern, recoverySource);
			default -> { }
		}
	}

	private void applyLungePose() {
		this.applyLungePose(1.0F);
	}

	private void applyLungePose(float progress) {
		float blend = smooth(progress);
		this.applyLungeArm(this.LeftArm, this.LowerLeftArm, this.LeftClaw, 1, blend);
		this.applyLungeArm(this.RightArm, this.LowerRightArm, this.RightClaw, -1, blend);
	}

	private void applyLungeArm(ModelPart arm, ModelPart forearm, ModelPart claw, int side, float blend) {
		PartPose rest = arm.getInitialPose();
		arm.xRot = Mth.lerp(blend, rest.xRot, -1.45F);
		arm.yRot = Mth.lerp(blend, rest.yRot, side * 0.25F);
		arm.zRot = Mth.lerp(blend, rest.zRot, side * -0.5F);
		forearm.xRot = forearm.getInitialPose().xRot;
		claw.xRot = Mth.lerp(blend, claw.getInitialPose().xRot, 0.95F);
	}

	private void applyClawPose(float elapsed, int pattern) {
		float time = Mth.clamp(elapsed, 0, ReaverEntity.FLURRY_TICKS);
		int strikes = ReaverEntity.FLURRY_TICKS / ReaverEntity.CLAW_HIT_INTERVAL;
		int strike = Math.min((int) (time / ReaverEntity.CLAW_HIT_INTERVAL), strikes - 1);
		float progress = (time - strike * ReaverEntity.CLAW_HIT_INTERVAL) / ReaverEntity.CLAW_HIT_INTERVAL;
		this.applyClawStroke(this.LeftArm, this.LowerLeftArm, 1, pattern % 3, strike, progress);
		this.applyClawStroke(this.RightArm, this.LowerRightArm, -1, pattern / 3, strike, progress);
		if (time < 0.6F) {
			float blend = smooth(time / 0.6F);
			this.blendFromLunge(this.LeftArm, 1, blend);
			this.blendFromLunge(this.RightArm, -1, blend);
		}
		this.relaxClaws(smooth(time / 0.6F));
	}

	private void relaxClaws(float blend) {
		this.LeftClaw.xRot = Mth.lerp(blend, 0.95F, this.LeftClaw.getInitialPose().xRot);
		this.RightClaw.xRot = Mth.lerp(blend, 0.95F, this.RightClaw.getInitialPose().xRot);
	}

	private void blendFromLunge(ModelPart arm, int side, float blend) {
		arm.xRot = Mth.lerp(blend, -1.45F, arm.xRot);
		arm.yRot = Mth.lerp(blend, side * 0.25F, arm.yRot);
		arm.zRot = Mth.lerp(blend, side * -0.5F, arm.zRot);
	}

	private void applyClawStroke(ModelPart arm, ModelPart forearm, int side, int offset, int strike, float progress) {
		this.applyClawSweep(arm, side, (offset + strike) % 3,
				clawSweep(progress, clawDelay(side, strike)), strike % 2 == 0, 0);
		this.straightenForearm(forearm);
	}

	private void applyClawSweep(ModelPart arm, int side, int path, float sweep, boolean outward, float tilt) {
		arm.xRot = clawPitch(path, sweep);
		arm.yRot = side * Mth.lerp(sweep, outward ? 1.25F : -0.95F, outward ? -0.95F : 1.25F);
		arm.zRot = side * (Mth.lerp(sweep, outward ? -0.2F : 0.2F, outward ? 0.2F : -0.2F)
				+ (outward ? tilt : -tilt));
	}

	private void straightenForearm(ModelPart forearm) {
		forearm.xRot = 0;
		forearm.yRot = 0;
		forearm.zRot = 0;
	}

	private static float clawDelay(int side, int strike) {
		if (side == (strike % 2 == 0 ? 1 : -1)) {
			return 0;
		}
		return switch (strike % 3) {
			case 0 -> 0.28F;
			case 1 -> 0.2F;
			default -> 0.32F;
		};
	}

	private static float clawSweep(float progress, float delay) {
		float time = progress - delay;
		if (time < 0.2F) {
			return -0.2F * smooth(time / 0.2F);
		}
		if (time < 0.42F) {
			return Mth.lerp(smooth((time - 0.2F) / 0.22F), -0.2F, 0.92F);
		}
		return Mth.lerp(smooth((time - 0.42F) / (0.58F - delay)), 0.92F, 1.0F);
	}

	private static float clawFlash(float elapsed, int side) {
		if (elapsed < 0 || elapsed >= ReaverEntity.FLURRY_TICKS) {
			return 0;
		}
		int strike = (int) (elapsed / ReaverEntity.CLAW_HIT_INTERVAL);
		float progress = (elapsed - strike * ReaverEntity.CLAW_HIT_INTERVAL) / ReaverEntity.CLAW_HIT_INTERVAL;
		float time = progress - clawDelay(side, strike);
		return smooth((time - 0.15F) / 0.05F) * (1 - smooth((time - 0.42F) / 0.26F));
	}

	private static float spectralSlashStart(int slash) {
		return slash * 0.58F + (slash % 3 == 1 ? 0.1F : slash % 3 == 2 ? -0.04F : 0);
	}

	private static int spectralSlashSide(int slash) {
		return switch (slash % 8) {
			case 0, 3, 5, 6 -> 1;
			default -> -1;
		};
	}

	private static float spectralClawFlash(float time) {
		return smooth(time / 0.12F) * (1 - smooth((time - 0.75F) / 1.1F));
	}

	private void applySpectralClawPose(float time, int pattern, int slash) {
		int side = spectralSlashSide(slash);
		int path = ((side == 1 ? pattern % 3 : pattern / 3) + slash / 2) % 3;
		ModelPart arm = side == 1 ? this.LeftArm : this.RightArm;
		ModelPart forearm = side == 1 ? this.LowerLeftArm : this.LowerRightArm;
		ModelPart claw = side == 1 ? this.LeftClaw : this.RightClaw;
		float sweep = time < 0.2F ? -0.15F * smooth(time / 0.2F)
				: Mth.lerp(smooth((time - 0.2F) / 0.55F), -0.15F, 1);
		boolean outward = slash / 3 % 2 == 0;
		float tilt = path == 1 ? 0.25F : path == 2 ? -0.25F : 0;
		this.applyClawSweep(arm, side, path, sweep, outward, tilt);
		this.straightenForearm(forearm);
		PartPose origin = arm.getInitialPose();
		arm.x = origin.x - side * (2.5F + slash % 3 * 1.25F);
		arm.y = origin.y + (path == 1 ? -3.0F : path == 2 ? 3.0F : 0);
		arm.z = origin.z - 2.0F - slash % 2;
		PartPose clawRest = claw.getInitialPose();
		claw.xRot = clawRest.xRot;
		claw.yRot = clawRest.yRot;
		claw.zRot = clawRest.zRot;
	}

	private static float clawPitch(int path, float sweep) {
		return Mth.lerp(sweep, path == 2 ? 0.8F : -0.5F, path == 1 ? 0.8F : -0.5F);
	}

	private void applyFlurryBodyPose(float elapsed, int pattern, float weight) {
		float time = Mth.clamp(elapsed, 0, ReaverEntity.FLURRY_TICKS);
		int strikes = ReaverEntity.FLURRY_TICKS / ReaverEntity.CLAW_HIT_INTERVAL;
		int strike = Math.min((int) (time / ReaverEntity.CLAW_HIT_INTERVAL), strikes - 1);
		float progress = (time - strike * ReaverEntity.CLAW_HIT_INTERVAL) / ReaverEntity.CLAW_HIT_INTERVAL;
		float leftSweep = Mth.clamp(clawSweep(progress, clawDelay(1, strike)), 0, 1);
		float rightSweep = Mth.clamp(clawSweep(progress, clawDelay(-1, strike)), 0, 1);
		float swing = (strike % 2 == 0 ? 1 : -1) * (leftSweep + rightSweep - 1);
		float leftPitch = clawPitch((pattern % 3 + strike) % 3, leftSweep);
		float rightPitch = clawPitch((pattern / 3 + strike) % 3, rightSweep);
		float blend = weight * smooth(time / 0.6F);
		this.Body.xRot += (leftPitch + rightPitch - 0.3F) / 1.3F * 0.12F * blend;
		this.Body.yRot += swing * 0.18F * blend;
		this.Body.zRot += swing * 0.08F * blend;
	}

	private void applyRecoveryPose(float elapsed, int pattern, ReaverEntity.AttackPhase source) {
		if (source == ReaverEntity.AttackPhase.FLURRY) {
			this.applyClawPose(ReaverEntity.FLURRY_TICKS, pattern);
			float blend = smooth(elapsed / ReaverEntity.HIT_RECOVERY_TICKS);
			this.applyFlurryBodyPose(ReaverEntity.FLURRY_TICKS, pattern, 1 - blend);
			this.Body.xRot += (1 - blend) * 0.2F;
			this.UpperJaw.xRot -= (1 - blend) * 0.3F;
			this.LowerJaw.xRot += (1 - blend) * 0.35F;
		} else if (source == ReaverEntity.AttackPhase.LUNGE || source == ReaverEntity.AttackPhase.WINDUP) {
			this.applyLungePose();
		}
		if (source != ReaverEntity.AttackPhase.LUNGE) {
			float blend = smooth(elapsed / ReaverEntity.HIT_RECOVERY_TICKS);
			if (source == ReaverEntity.AttackPhase.WINDUP) {
				this.relaxClaws(blend);
			}
			this.blendRecoveryArm(this.LeftArm, this.LowerLeftArm, blend);
			this.blendRecoveryArm(this.RightArm, this.LowerRightArm, blend);
			return;
		}
		float blend = smooth(elapsed / ReaverEntity.RECOVERY_DROP_START);
		float shake = Mth.sin(elapsed * 3.4F) * Mth.sin(blend * Mth.PI) * 0.08F;
		float raise = smooth((elapsed - ReaverEntity.RECOVERY_DROP_START)
				/ (ReaverEntity.RECOVERY_LAND_TICK - ReaverEntity.RECOVERY_DROP_START));
		float slam = smooth((elapsed - ReaverEntity.RECOVERY_LAND_TICK)
				/ (ReaverEntity.RECOVERY_SPIN_START - ReaverEntity.RECOVERY_LAND_TICK));
		float rotor = smooth((elapsed - ReaverEntity.RECOVERY_SPIN_START) / 4.0F);
		float spin = smooth((elapsed - ReaverEntity.RECOVERY_SPIN_START)
				/ (ReaverEntity.RECOVERY_SPIN_END - ReaverEntity.RECOVERY_SPIN_START));
		float settle = smooth((elapsed - ReaverEntity.RECOVERY_SPIN_END)
				/ (ReaverEntity.RECOVERY_TICKS - ReaverEntity.RECOVERY_SPIN_END));
		this.Body.xRot += (1 - blend) * 0.2F + shake - raise * (1 - slam) * 0.08F;
		this.Body.yRot += spin * Mth.TWO_PI * 2 + shake * 0.6F;
		this.Body.zRot += shake * 0.7F;
		this.UpperJaw.xRot -= (1 - blend) * 0.3F;
		this.LowerJaw.xRot += (1 - blend) * 0.35F;
		this.applyMissRecoveryArm(this.LeftArm, this.LowerLeftArm, this.LeftClaw, 1, blend, raise, slam, rotor, settle);
		this.applyMissRecoveryArm(this.RightArm, this.LowerRightArm, this.RightClaw, -1, blend, raise, slam, rotor, settle);
	}

	private void applyMissRecoveryArm(ModelPart arm, ModelPart forearm, ModelPart claw, int side,
			float blend, float raise, float slam, float rotor, float settle) {
		arm.xRot = Mth.lerp(blend, arm.xRot, Mth.lerp(raise, -0.1F, -1.45F));
		arm.yRot = Mth.lerp(blend, arm.yRot, side * Mth.lerp(raise, 0.35F, 0.25F));
		arm.zRot = Mth.lerp(blend, arm.zRot, side * Mth.lerp(raise, -0.15F, -0.5F));
		claw.xRot = Mth.lerp(blend, claw.xRot, 0);
		arm.xRot = Mth.lerp(slam, arm.xRot, 0.29F);
		arm.yRot = Mth.lerp(slam, arm.yRot, side * 0.35F);
		arm.zRot = Mth.lerp(slam, arm.zRot, side * -0.15F);
		arm.xRot = Mth.lerp(rotor, arm.xRot, 0);
		arm.yRot = Mth.lerp(rotor, arm.yRot, side * Mth.HALF_PI);
		arm.zRot = Mth.lerp(rotor, arm.zRot, 0);
		claw.yRot = Mth.lerp(rotor * (1 - settle), claw.getInitialPose().yRot, 0);
		forearm.xRot = 0;
		this.blendRecoveryArm(arm, forearm, settle);
	}

	private void blendRecoveryArm(ModelPart arm, ModelPart forearm, float blend) {
		PartPose rest = arm.getInitialPose();
		arm.xRot = Mth.lerp(blend, arm.xRot, rest.xRot);
		arm.yRot = Mth.lerp(blend, arm.yRot, rest.yRot);
		arm.zRot = Mth.lerp(blend, arm.zRot, rest.zRot);
		forearm.xRot = Mth.lerp(blend, forearm.xRot, forearm.getInitialPose().xRot);
	}

	private static float smooth(float value) {
		return (float) Mth.smoothstep(Mth.clamp(value, 0, 1));
	}

	public void renderClawAfterimages(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
			float elapsed, int pattern, float opacity) {
		this.renderArmAfterimages(poseStack, buffer, packedLight, packedOverlay, elapsed, pattern, opacity, false);
		this.renderSpectralClaws(poseStack, buffer, packedLight, packedOverlay, elapsed, pattern, opacity);
	}

	public void renderLungeAfterimages(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float opacity) {
		this.renderArmAfterimages(poseStack, buffer, packedLight, packedOverlay, 0, 0, opacity, true);
	}

	private void renderArmAfterimages(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
			float elapsed, int pattern, float opacity, boolean lunging) {
		PartPose[] original = this.capturePose();
		try {
			for (int trail = 3; trail >= 1; trail--) {
				float time = elapsed - trail * 0.3F;
				this.loadPose(original);
				if (lunging) {
					if (this.renderPoseState != null) {
						this.loadPose(previousPose(this.renderPoseState, trail * 0.6F));
					} else {
						this.applyLungePose();
					}
					this.LeftArm.z += trail * 1.5F;
					this.RightArm.z += trail * 1.5F;
				} else {
					if (this.renderPoseState != null) {
						this.loadPose(previousPose(this.renderPoseState, trail * 0.3F));
					} else {
						this.applyClawPose(time, pattern);
					}
				}
				float alpha = opacity * (0.26F - trail * 0.055F);
				this.renderAfterimageArm(poseStack, buffer, packedLight, packedOverlay, this.LeftArm, alpha * (lunging ? 1 : clawFlash(time, 1)));
				this.renderAfterimageArm(poseStack, buffer, packedLight, packedOverlay, this.RightArm, alpha * (lunging ? 1 : clawFlash(time, -1)));
			}
		} finally {
			this.loadPose(original);
		}
	}

	private void renderSpectralClaws(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
			float elapsed, int pattern, float opacity) {
		PartPose[] original = this.capturePose();
		try {
			for (int slash = 0; spectralSlashStart(slash) < ReaverEntity.FLURRY_TICKS; slash++) {
				ModelPart arm = spectralSlashSide(slash) == 1 ? this.LeftArm : this.RightArm;
				for (int trail = 2; trail >= 0; trail--) {
					float time = elapsed - spectralSlashStart(slash) - trail * 0.14F;
					float alpha = opacity * (0.32F - trail * 0.075F) * spectralClawFlash(time);
					if (alpha <= 0) {
						continue;
					}
					this.loadPose(original);
					this.applySpectralClawPose(time, pattern, slash);
					this.renderAfterimageArm(poseStack, buffer, packedLight, packedOverlay, arm, alpha);
				}
			}
		} finally {
			this.loadPose(original);
		}
	}

	private void renderAfterimageArm(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
			ModelPart arm, float alpha) {
		poseStack.pushPose();
		try {
			this.Body.translateAndRotate(poseStack);
			arm.render(poseStack, buffer, packedLight, packedOverlay,
					SpectralRenderUtil.applyAlpha(this.modifyColor(0xFFFFFFFF), this.spectralAlpha * alpha));
		} finally {
			poseStack.popPose();
		}
	}

	private PartPose[] capturePose() {
		PartPose[] poses = new PartPose[this.poseParts.size()];
		for (int i = 0; i < poses.length; i++) {
			poses[i] = this.poseParts.get(i).storePose();
		}
		return poses;
	}

	private void loadPose(PartPose[] poses) {
		for (int i = 0; i < poses.length; i++) {
			this.poseParts.get(i).loadPose(poses[i]);
		}
	}

	private static PartPose[] updatePose(PoseState state, int key, float time, float duration, PartPose[] target) {
		if (state.current == null || time < state.lastTime || time - state.lastTime > 8) {
			state.history.clear();
			state.current = target;
			state.transitionOffset = null;
			state.poseKey = key;
			state.transitionStart = time;
			state.transitionDuration = 0;
		} else if (key != state.poseKey) {
			state.transitionOffset = poseDifference(state.current, target);
			state.poseKey = key;
			state.transitionStart = time;
			state.transitionDuration = duration;
		} else if (time == state.lastTime) {
			return state.current;
		}
		float progress = state.transitionDuration > 0
				? Mth.clamp((time - state.transitionStart) / state.transitionDuration, 0, 1) : 1;
		state.current = progress < 1
				? offsetPose(target, state.transitionOffset, 1 - (float) Mth.smoothstep(progress)) : target;
		state.lastTime = time;
		if (!state.history.isEmpty() && state.history.getLast().time == time) {
			state.history.removeLast();
		}
		state.history.addLast(new PoseSample(time, state.current));
		while (state.history.size() > 64 || (state.history.size() > 1 && state.history.getFirst().time < time - 4)) {
			state.history.removeFirst();
		}
		return state.current;
	}

	private static PartPose[] previousPose(PoseState state, float ticksAgo) {
		float time = state.lastTime - ticksAgo;
		PoseSample before = state.history.getFirst();
		for (PoseSample after : state.history) {
			if (after.time >= time) {
				float interval = after.time - before.time;
				return interval > 0 ? blendPoses(before.pose, after.pose,
						Mth.clamp((time - before.time) / interval, 0, 1)) : before.pose;
			}
			before = after;
		}
		return state.current;
	}

	private static PartPose[] blendPoses(PartPose[] from, PartPose[] to, float weight) {
		PartPose[] result = new PartPose[to.length];
		for (int i = 0; i < result.length; i++) {
			PartPose start = from[i];
			PartPose end = to[i];
			result[i] = PartPose.offsetAndRotation(
					Mth.lerp(weight, start.x, end.x), Mth.lerp(weight, start.y, end.y),
					Mth.lerp(weight, start.z, end.z), interpolatePoseAngle(weight, start.xRot, end.xRot),
					interpolatePoseAngle(weight, start.yRot, end.yRot), interpolatePoseAngle(weight, start.zRot, end.zRot));
		}
		return result;
	}

	private static PartPose[] poseDifference(PartPose[] from, PartPose[] to) {
		PartPose[] result = new PartPose[to.length];
		for (int i = 0; i < result.length; i++) {
			PartPose start = from[i];
			PartPose end = to[i];
			result[i] = PartPose.offsetAndRotation(start.x - end.x, start.y - end.y, start.z - end.z,
					interpolatePoseAngle(1, end.xRot, start.xRot) - end.xRot,
					interpolatePoseAngle(1, end.yRot, start.yRot) - end.yRot,
					interpolatePoseAngle(1, end.zRot, start.zRot) - end.zRot);
		}
		return result;
	}

	private static PartPose[] offsetPose(PartPose[] target, PartPose[] offset, float weight) {
		PartPose[] result = new PartPose[target.length];
		for (int i = 0; i < result.length; i++) {
			PartPose pose = target[i];
			PartPose delta = offset[i];
			result[i] = PartPose.offsetAndRotation(pose.x + delta.x * weight, pose.y + delta.y * weight,
					pose.z + delta.z * weight, pose.xRot + delta.xRot * weight,
					pose.yRot + delta.yRot * weight, pose.zRot + delta.zRot * weight);
		}
		return result;
	}

	private static float interpolatePoseAngle(float weight, float from, float to) {
		return from + weight * Mth.wrapDegrees((to - from) * Mth.RAD_TO_DEG) * Mth.DEG_TO_RAD;
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		if (this.depthOnly) {
			if ((this.modifyColor(color) >>> 24) != 0) {
				this.Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color | 0xFF000000);
			}
			if (this.bodyBuffer == null) {
				return;
			}
			if (this.bodyBuffer instanceof MultiBufferSource.BufferSource bufferSource) {
				bufferSource.endBatch(BGRenderTypes.reaverBodyDepth(this.bodyTexture));
			}
			this.depthOnly = false;
			vertexConsumer = this.bodyBuffer.getBuffer(RenderType.entityTranslucent(this.bodyTexture));
		}
		this.renderColorToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	public void renderColorToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		this.Body.render(poseStack, vertexConsumer, packedLight, packedOverlay,
				SpectralRenderUtil.applyAlpha(this.modifyColor(color), this.spectralAlpha));
	}

	private static final class PoseState {
		private final Deque<PoseSample> history = new ArrayDeque<>();
		private PartPose[] current;
		private PartPose[] transitionOffset;
		private int poseKey;
		private float lastTime;
		private float transitionStart;
		private float transitionDuration;
	}

	private record PoseSample(float time, PartPose[] pose) { }
}
