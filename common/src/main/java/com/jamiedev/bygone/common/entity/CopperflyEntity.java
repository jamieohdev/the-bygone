package com.jamiedev.bygone.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.UUID;

public class CopperflyEntity extends Animal implements NeutralMob, FlyingAnimal {
    public static final float FLAP_DEGREES_PER_TICK = 120.32113F;
    public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
    Tadpole ref;
    Bee ref2;

    protected CopperflyEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {

    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }

    @Override
    public boolean isFlying() {
        return false;
    }


    class CopperflyEntityWanderGoal extends Goal {
        private static final int WANDER_THRESHOLD = 22;

        CopperflyEntityWanderGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return CopperflyEntity.this.navigation.isDone() && CopperflyEntity.this.random.nextInt(10) == 0;
        }

        public boolean canContinueToUse() {
            return CopperflyEntity.this.navigation.isInProgress();
        }

        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                CopperflyEntity.this.navigation.moveTo(CopperflyEntity.this.navigation.createPath(BlockPos.containing(vec3), 1), 1.0F);
            }

        }

        @javax.annotation.Nullable
        private Vec3 findPos() {
            Vec3 vec3;

            vec3 = CopperflyEntity.this.getViewVector(0.0F);

            int i = 8;
            Vec3 vec32 = HoverRandomPos.getPos(CopperflyEntity.this, 8, 7, vec3.x, vec3.z, ((float) Math.PI / 2F), 3, 1);
            return vec32 != null ? vec32 : AirAndWaterRandomPos.getPos(CopperflyEntity.this, 8, 4, -2, vec3.x, vec3.z, (float) Math.PI / 2F);
        }
    }
}
