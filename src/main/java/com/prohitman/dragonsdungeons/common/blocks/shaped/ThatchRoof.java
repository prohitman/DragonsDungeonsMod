package com.prohitman.dragonsdungeons.common.blocks.shaped;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThatchRoof extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public ThatchRoof(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
/*
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return makeShape();
    }

    public VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.09375, 0.25, 0.0625, 1.53125, 0.375, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.09375, 0.25, 0.8125, 1.53125, 0.375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.19887378220871654, 0.33145630368119405, 0, 1.2386262177912835, 0.45645630368119405, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.3983538477468634, 0.3604412403844745, 0, 0.2266461522531366, 0.3604412403844745, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.044800457153589594, 0.7139946309777483, 0, 0.5801995428464103, 0.7139946309777483, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.30875293343968396, 1.0675480215710222, 0, 0.9337529334396842, 1.0675480215710222, 1), BooleanOp.OR);

        return shape;
    }*/

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
}
