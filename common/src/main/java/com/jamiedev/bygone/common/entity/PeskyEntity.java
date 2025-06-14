package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.block.PlagaCropBlock;
import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.common.entity.ai.EatCropGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class PeskyEntity extends Monster
{
    Spider ref;

    public PeskyEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setSpeedModifier((double)0.1F);
    }

    public void setSpeedModifier(double speedModifier) {
        this.getNavigation().setSpeedModifier(speedModifier);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), speedModifier);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, (double)6.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)1.0F).add(Attributes.ATTACK_DAMAGE, (double)5.0F).add(Attributes.STEP_HEIGHT, (double)3.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, (double)1.0F, true));
        this.goalSelector.addGoal(3, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(4, new AvoidBlockGoal(this, 16, 1.4, 2.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(JamiesModTag.PEST_REPELLENTS);
        }));
        this.goalSelector.addGoal(4, new AvoidBlockGoal(this, 8, 1.2, 2.1, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            if (state.is(BGBlocks.PLAGA_CROP.get())){
                return state.getValue(PlagaCropBlock.AGE) > 5;
            } else return false;
        }));
        this.goalSelector.addGoal(4, new AvoidBlockGoal(this, 4, 1.2, 2.1, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            if (state.is(BGBlocks.PLAGA_CROP.get())){
                return state.getValue(PlagaCropBlock.AGE) < 5;
            } else return false;
        }));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, NectaurEntity.class, 6.0F, (double)1.0F, 1.2));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0, 10));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }

    }

}
