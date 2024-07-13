package com.prohitman.dragonsdungeons.common.blocks.obj;

import com.prohitman.dragonsdungeons.common.Utils;
import com.prohitman.dragonsdungeons.common.blocks.ModBlockStateProperties;
import com.prohitman.dragonsdungeons.common.blocks.shaped.MithrilCrystal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PalisadeBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty IS_TOP = ModBlockStateProperties.IS_TOP;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PalisadeBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(IS_TOP, Boolean.valueOf(true)).setValue(WATERLOGGED, false));
    }

    public VoxelShape makeTopShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.3125, 0.5, 0.4375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.1875, 1, 0.5, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0.5, 0.4375, 1, 1, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0.5, 0.1875, 0.75, 1, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.4375, 0.3125, 0.25, 0.9375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.4375, 0.5625, 0.5, 0.9375, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.4375, 0.375, 0.4375, 0.875, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5625, 0.4375, 0.25, 0.9375, 0.875, 0.625), BooleanOp.OR);

        return shape;
    }

    public VoxelShape makeBottomShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.3125, 0.5, 1, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.1875, 1, 1, 0.6875), BooleanOp.OR);

        return shape;
    }

    public VoxelShape getShapeFromState(BlockState state){
        return state.getValue(IS_TOP) ? makeTopShape() : makeBottomShape();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        VoxelShape voxelshape;
        switch (direction) {
            case EAST:
                voxelshape = Utils.rotateXtoZ(getShapeFromState(pState));
                break;
            case WEST:
                voxelshape = Utils.rotateXtoZ(getShapeFromState(pState), 3);
                break;
            case SOUTH:
                voxelshape = Utils.rotateXtoZ(getShapeFromState(pState), 2);
                break;
            case NORTH:
                voxelshape = Utils.rotateXtoZ(getShapeFromState(pState), 4);
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        return voxelshape;
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        BlockPos top = pos.above(1);
        boolean isTop = !(pContext.getLevel().getBlockState(top).getBlock() instanceof PalisadeBlock);

        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());

        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(IS_TOP, isTop).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        BlockPos top = pPos.above(1);
        boolean isTop = !(pLevel.getBlockState(top).getBlock() instanceof PalisadeBlock);

        BlockState newState = pState.setValue(IS_TOP, isTop);

        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(newState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        if (pState.getValue(IS_TOP)) {
            pEntity.causeFallDamage(pFallDistance + 2.0F, 2.0F, pLevel.damageSources().stalagmite());
        } else {
            super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
        }

    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if(pState.getValue(IS_TOP)){
            pEntity.hurt(pEntity.damageSources().stalagmite(), 1);
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#rotate} whenever
     * possible. Implementing/overriding is fine.
     */
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#mirror} whenever
     * possible. Implementing/overriding is fine.
     */
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, IS_TOP, WATERLOGGED);
    }
}
