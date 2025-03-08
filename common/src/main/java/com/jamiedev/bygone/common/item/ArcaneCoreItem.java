package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.common.block.BygonePortalFrameBlock;
import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.phys.shapes.CollisionContext;

public class ArcaneCoreItem extends BlockItem
{
    public ArcaneCoreItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        InteractionResult actionResult = this.place(new BlockPlaceContext(context));
        if (blockState.is(BGBlocks.BYGONE_PORTAL_FRAME) && !(Boolean)blockState.getValue(BygonePortalFrameBlock.EYE)) {
            if (world.isClientSide) {
               // return ActionResult.SUCCESS;
            } else {
                BlockState blockState2 = blockState.setValue(BygonePortalFrameBlock.EYE, true);
                Block.pushEntitiesUp(blockState, blockState2, world, blockPos);
                world.setBlock(blockPos, blockState2, 2);
                world.updateNeighbourForOutputSignal(blockPos, BGBlocks.BYGONE_PORTAL_FRAME);
                context.getItemInHand().shrink(1);
                world.levelEvent(2503, blockPos, 0);
                BlockPattern.BlockPatternMatch result = BygonePortalFrameBlock.getCompletedFramePattern().find(world, blockPos);
                if (result != null) {
                    BlockPos blockPos2 = result.getFrontTopLeft().offset(-3, 0, -3);

                    for(int i = 0; i < 3; ++i) {
                        for(int j = 0; j < 3; ++j) {
                            world.setBlock(blockPos2.offset(i, 0, j), BGBlocks.BYGONE_PORTAL.defaultBlockState(), 2);
                        }
                    }

                    world.globalLevelEvent(1038, blockPos2.offset(1, 0, 1), 0);
                }

                return InteractionResult.CONSUME;
            }
        }
        else if (!blockState.is(BGBlocks.BYGONE_PORTAL_FRAME))
        {
            return actionResult;
        }
        else
        {
            return InteractionResult.FAIL;
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        Player playerEntity = context.getPlayer();
        CollisionContext shapeContext = playerEntity == null ? CollisionContext.empty() : CollisionContext.of(playerEntity);
        return (!this.mustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos())) && context.getLevel().isUnobstructed(state, context.getClickedPos(), shapeContext) && !state.is(BGBlocks.BYGONE_PORTAL_FRAME);
    }

}
