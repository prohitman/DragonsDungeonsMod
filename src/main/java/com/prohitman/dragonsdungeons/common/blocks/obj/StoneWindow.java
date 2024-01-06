package com.prohitman.dragonsdungeons.common.blocks.obj;

import com.prohitman.dragonsdungeons.common.blocks.ModBlockStateProperties;
import com.prohitman.dragonsdungeons.common.blocks.enums.ConnectedState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.TwistingVinesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import org.jetbrains.annotations.Nullable;

public class StoneWindow extends GlassBlock {
    public static final EnumProperty<ConnectedState> CONNECTED_STATE = ModBlockStateProperties.CONNECTED_STATE;
    public StoneWindow(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONNECTED_STATE, ConnectedState.NONE));
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        BlockState belowState = pLevel.getBlockState(pPos.below());
        BlockState aboveState = pLevel.getBlockState(pPos.above());
        ConnectedState state = pState.getValue(CONNECTED_STATE);

        if(belowState.getBlock() instanceof StoneWindow){
            if(aboveState.getBlock() instanceof StoneWindow){
                state = ConnectedState.BOTH;
            }else{
                state = ConnectedState.TOP;
            }
        } else if(aboveState.getBlock() instanceof StoneWindow){
            state = ConnectedState.BOTTOM;
        } else {
            state = ConnectedState.NONE;
        }
        BlockState newState = pState.setValue(CONNECTED_STATE, state);
        return super.updateShape(newState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

/*
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        BlockState belowState = pLevel.getBlockState(pPos.below());
        BlockState aboveState = pLevel.getBlockState(pPos.above());
        ConnectedState state = pState.getValue(CONNECTED_STATE);

        if(belowState.getBlock() instanceof StoneWindow){
            if(aboveState.getBlock() instanceof StoneWindow){
                state = ConnectedState.BOTH;
            }else{
                state = ConnectedState.TOP;
            }
        } else if(aboveState.getBlock() instanceof StoneWindow){
            state = ConnectedState.BOTTOM;
        } else {
            state = ConnectedState.NONE;
        }
        BlockState newState = pState.setValue(CONNECTED_STATE, state);
        pLevel.setBlock(pPos, newState, 3);
        System.out.println(newState);
    }*/

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(CONNECTED_STATE);
    }
}
