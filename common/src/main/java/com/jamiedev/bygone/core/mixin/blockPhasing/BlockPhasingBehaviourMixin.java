package com.jamiedev.bygone.core.mixin.blockPhasing;

import com.jamiedev.bygone.common.entity.BlockPhasingEntity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockPhasingBehaviourMixin {

	@Shadow public abstract boolean isSuffocating(BlockGetter level, BlockPos pos);

	@WrapMethod(
		method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;"
	)
	private VoxelShape walkThroughSides(BlockGetter level, BlockPos pos, CollisionContext context, Operation<VoxelShape> original) {
		VoxelShape result = original.call(level, pos, context);
		if (result.isEmpty()) return result;
		if (!(context instanceof EntityCollisionContext entityCollision)) return result;

		Entity entity = entityCollision.getEntity();
		if (!(entity instanceof BlockPhasingEntity phasing)) return result;
		if (!phasing.canStartPhasing()) return result;

		return this.getPhasingShape(level, pos, context, result);
	}

	@Unique
	private VoxelShape getPhasingShape(BlockGetter level, BlockPos pos, CollisionContext context, VoxelShape original) {
		if (context.isAbove(original, pos, true)) {
			VoxelShape aboveShape = level.getBlockState(pos.above()).getCollisionShape(level, pos.above());
			if (!Block.isFaceFull(aboveShape, Direction.DOWN)) {
				return original;
			}
		}

		return Shapes.empty();
	}

	@WrapMethod(method = "entityInside")
	private void slowWhilePhasing(Level level, BlockPos pos, Entity entity, Operation<Void> original) {
		original.call(level, pos, entity);
		if (this.isSuffocating(level, pos) && entity instanceof BlockPhasingEntity phasing) {
			phasing.setInsideBlock(true);
		}
	}

}
