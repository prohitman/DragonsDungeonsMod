package com.prohitman.dragonsdungeons.common.blocks.obj;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HangingRootsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HangingBarrowMoss extends Block {
    private static final VoxelShape SHAPE = Block.box(2.0D, 10.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    public HangingBarrowMoss(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(2.0D, 10.0D, 2.0D, 14.0D, 16.0D, 14.0D), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D), BooleanOp.OR);

        return shape;
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = level.getBlockState(blockpos);
        return blockstate.isFaceSturdy(level, blockpos, Direction.DOWN);
    }

    public BlockState updateShape(BlockState pState, Direction direction, BlockState neighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos neighborPos) {
        if (direction == Direction.UP && !this.canSurvive(pState, pLevel, pPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(pState, direction, neighborState, pLevel, pPos, neighborPos);
        }
    }
}
