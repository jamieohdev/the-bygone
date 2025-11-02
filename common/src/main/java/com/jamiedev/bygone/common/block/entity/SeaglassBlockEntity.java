package com.jamiedev.bygone.common.block.entity;

import com.jamiedev.bygone.core.registry.BGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeaglassBlockEntity extends BlockEntity {

    protected static final VoxelShape SHAPE_DOWN = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.01D, 16.0D);
    protected static final VoxelShape SHAPE_UP = Block.box(0.0D, 15.99D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 0.01D);
    protected static final VoxelShape SHAPE_EAST = Block.box(15.99D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_SOUTH = Block.box(0.0D, 0.0D, 15.99D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_WEST = Block.box(0.0D, 0.0D, 0.0D, 0.01D, 16.0D, 16.0D);
    private static final Map<Direction, VoxelShape> occlusionShapes = new HashMap<Direction, VoxelShape>() {{
        put(Direction.DOWN, SHAPE_DOWN);
        put(Direction.UP, SHAPE_UP);
        put(Direction.NORTH, SHAPE_NORTH);
        put(Direction.EAST, SHAPE_EAST);
        put(Direction.SOUTH, SHAPE_SOUTH);
        put(Direction.WEST, SHAPE_WEST);
    }};
    private final List<Direction> occlusionDirs = new ArrayList<>();
    protected VoxelShape occlusionShape = Shapes.empty();

    public SeaglassBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BGBlockEntities.SEAGLASS.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        ListTag occlusionDirStrings = new ListTag();
        for (Direction direction : occlusionDirs) {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("dir", direction.toString());

            occlusionDirStrings.add(compoundTag);
        }
        pTag.put("occlusiondirs", occlusionDirStrings);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        ListTag occlusionDirTag = pTag.getList("occlusiondirs", Tag.TAG_COMPOUND);
        for (int i = 0; i < occlusionDirTag.size(); i++) {
            CompoundTag dirCompoundTag = occlusionDirTag.getCompound(i);
            String dirString = dirCompoundTag.getString("dir");
            Direction direction = Direction.byName(dirString);
            if (direction != null) {
                occlusionDirs.add(direction);
            }
        }
        VoxelShape shape = Shapes.empty();

        for (Direction direction : occlusionDirs) {
            shape = Shapes.or(shape, occlusionShapes.get(direction));
        }

        setOcclusionShape(shape);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag compoundTag = new CompoundTag();
        ListTag occlusionDirStrings = new ListTag();
        for (Direction direction : occlusionDirs) {
            CompoundTag dirTag = new CompoundTag();
            dirTag.putString("dir", direction.toString());

            occlusionDirStrings.add(dirTag);
        }
        compoundTag.put("occlusiondirs", occlusionDirStrings);
        return compoundTag;
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public VoxelShape getOcclusionShape() {
        return occlusionShape;
    }

    public void setOcclusionShape(VoxelShape shape) {
        this.occlusionShape = shape;
    }

    public void addDirection(Direction direction) {
        occlusionDirs.add(direction);
    }

    public List<Direction> getOcclusionDirs() {
        return occlusionDirs;
    }
}