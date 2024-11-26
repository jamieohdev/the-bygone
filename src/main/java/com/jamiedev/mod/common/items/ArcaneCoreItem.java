package com.jamiedev.mod.common.items;

import com.jamiedev.mod.common.blocks.BygonePortalFrameBlock;
import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ArcaneCoreItem extends BlockItem
{
    public ArcaneCoreItem(Block block, Settings settings) {
        super(block, settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        ActionResult actionResult = this.place(new ItemPlacementContext(context));
        if (blockState.isOf(JamiesModBlocks.BYGONE_PORTAL_FRAME) && !(Boolean)blockState.get(BygonePortalFrameBlock.EYE)) {
            if (world.isClient) {
               // return ActionResult.SUCCESS;
            } else {
                BlockState blockState2 = (BlockState)blockState.with(BygonePortalFrameBlock.EYE, true);
                Block.pushEntitiesUpBeforeBlockChange(blockState, blockState2, world, blockPos);
                world.setBlockState(blockPos, blockState2, 2);
                world.updateComparators(blockPos, JamiesModBlocks.BYGONE_PORTAL_FRAME);
                context.getStack().decrement(1);
                world.syncWorldEvent(2503, blockPos, 0);
                BlockPattern.Result result = BygonePortalFrameBlock.getCompletedFramePattern().searchAround(world, blockPos);
                if (result != null) {
                    BlockPos blockPos2 = result.getFrontTopLeft().add(-3, 0, -3);

                    for(int i = 0; i < 3; ++i) {
                        for(int j = 0; j < 3; ++j) {
                            world.setBlockState(blockPos2.add(i, 0, j), JamiesModBlocks.BYGONE_PORTAL.getDefaultState(), 2);
                        }
                    }

                    world.syncGlobalEvent(1038, blockPos2.add(1, 0, 1), 0);
                }

                return ActionResult.CONSUME;
            }
        }
        else if (!blockState.isOf(JamiesModBlocks.BYGONE_PORTAL_FRAME))
        {
            return actionResult;
        }
        else
        {
            return ActionResult.FAIL;
        }
        return ActionResult.FAIL;
    }

    @Override
    protected boolean canPlace(ItemPlacementContext context, BlockState state) {
        PlayerEntity playerEntity = context.getPlayer();
        ShapeContext shapeContext = playerEntity == null ? ShapeContext.absent() : ShapeContext.of(playerEntity);
        return (!this.checkStatePlacement() || state.canPlaceAt(context.getWorld(), context.getBlockPos())) && context.getWorld().canPlace(state, context.getBlockPos(), shapeContext) && !state.isOf(JamiesModBlocks.BYGONE_PORTAL_FRAME);
    }

}
