package com.jamiedev.bygone.client.models.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.util.Mth;

public class WhiskbillAnimations {
    public static final AnimationDefinition WHISKBILL_WALK = createWalkAnimation();

    public static final AnimationDefinition WHISKBILL_IDLE = AnimationDefinition.Builder.withLength(5.0F).looping()
            .addAnimation("Tail", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.5394F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.5394F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.5394F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.75F, KeyframeAnimations.degreeVec(0.5394F, -3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.5394F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(9.5394F, 0.0F, -1.2196F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    private static AnimationDefinition createWalkAnimation() {
        AnimationDefinition.Builder builder = AnimationDefinition.Builder.withLength(3.3F).looping();
        Keyframe[] bodyPosition = new Keyframe[81];
        Keyframe[] bodyRotation = new Keyframe[81];
        Keyframe[] headRotation = new Keyframe[81];
        Keyframe[] tailRotation = new Keyframe[81];

        for (int i = 0; i <= 80; i++) {
            float phase = i / 80.0F;
            float time = phase * 3.3F;
            float sway = Mth.sin(phase * Mth.TWO_PI);
            float weightShift = Mth.sin(phase * Mth.TWO_PI * 2.0F);
            float headNod = Mth.sin((phase - 0.05F) * Mth.TWO_PI * 2.0F);
            float headSway = Mth.sin((phase - 0.08F) * Mth.TWO_PI);
            float tailSway = Mth.sin((phase - 0.08F) * Mth.TWO_PI);
            float tailBalance = Mth.sin((phase - 0.09F) * Mth.TWO_PI * 2.0F);
            float tailSettle = Mth.sin((phase - 0.065F) * Mth.TWO_PI * 4.0F);
            bodyPosition[i] = new Keyframe(time, KeyframeAnimations.posVec(0.0F, -getBodyDip(phase), 0.0F), AnimationChannel.Interpolations.CATMULLROM);
            bodyRotation[i] = new Keyframe(time, KeyframeAnimations.degreeVec(weightShift * 0.25F, 0.0F, sway * 0.35F), AnimationChannel.Interpolations.CATMULLROM);
            headRotation[i] = new Keyframe(time, KeyframeAnimations.degreeVec(6.0F - headNod * 2.0F,
                    headSway * 2.2F, -headSway * 0.35F), AnimationChannel.Interpolations.CATMULLROM);
            tailRotation[i] = new Keyframe(time, KeyframeAnimations.degreeVec(-1.5F - tailBalance * 1.1F - tailSettle * 0.2F,
                    tailSway * 6.0F, -tailSway * 0.12F), AnimationChannel.Interpolations.CATMULLROM);
        }

        builder.addAnimation("Body", new AnimationChannel(AnimationChannel.Targets.POSITION, bodyPosition));
        builder.addAnimation("Body", new AnimationChannel(AnimationChannel.Targets.ROTATION, bodyRotation));
        builder.addAnimation("Head", new AnimationChannel(AnimationChannel.Targets.ROTATION, headRotation));
        builder.addAnimation("Tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, tailRotation));
        addLegAnimation(builder, "Leg_BL", 0.0F, 48.0F, 13.0F, 9.0F);
        addLegAnimation(builder, "Leg_FL", 0.75F, 42.0F, 15.0F, 6.0F);
        addLegAnimation(builder, "leg_BR", 0.5F, 48.0F, 13.0F, 9.0F);
        addLegAnimation(builder, "Leg_FR", 0.25F, 42.0F, 15.0F, 6.0F);
        return builder.build();
    }

    private static void addLegAnimation(AnimationDefinition.Builder builder, String part, float offset, float maxAngle, float height, float toeLength) {
        Keyframe[] rotation = new Keyframe[81];
        Keyframe[] position = new Keyframe[81];
        float reach = Mth.sin(maxAngle * Mth.DEG_TO_RAD);

        for (int i = 0; i <= 80; i++) {
            float phase = i / 80.0F;
            float legPhase = (phase + offset) % 1.0F;
            float lift = 0.0F;
            float angle;
            if (legPhase < 0.8F) {
                angle = (float) Math.asin(Mth.lerp(legPhase / 0.8F, -reach, reach));
            } else {
                float swing = (legPhase - 0.8F) / 0.2F;
                float progress = swing * swing * (3.0F - 2.0F * swing);
                angle = (float) Math.asin(Mth.lerp(progress, reach, -reach));
                float liftProgress = Mth.sin(swing * Mth.PI);
                lift = liftProgress * liftProgress * 2.0F;
            }
            float footHeight = height * Mth.cos(angle) + Math.abs(Mth.sin(angle)) * (angle < 0.0F ? toeLength : 4.0F);
            float time = phase * 3.3F;
            rotation[i] = new Keyframe(time, KeyframeAnimations.degreeVec(angle * Mth.RAD_TO_DEG, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM);
            position[i] = new Keyframe(time, KeyframeAnimations.posVec(0.0F, footHeight - height + lift + getBodyDip(phase), 0.0F), AnimationChannel.Interpolations.CATMULLROM);
        }

        builder.addAnimation(part, new AnimationChannel(AnimationChannel.Targets.ROTATION, rotation));
        builder.addAnimation(part, new AnimationChannel(AnimationChannel.Targets.POSITION, position));
    }

    private static float getBodyDip(float phase) {
        float settle = Mth.sin((phase * 4.0F % 1.0F) * Mth.PI);
        return settle * settle * 0.8F;
    }
}
