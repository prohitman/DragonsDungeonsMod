package com.prohitman.dragonsdungeons.common.blocks.shaped;

import com.prohitman.dragonsdungeons.common.Utils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class MithrilCrystal extends FaceAttachedHorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    //public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    /*public static final VoxelShape SHAPES = Stream.of(
            Block.box(0.40625, 0, 0.40625, 0.59375, 0.75, 0.59375),
            Block.box(0.28125, 0, 0.21875, 0.46875, 0.4375, 0.40625),
            Block.box(0.53125, 0, 0.59375, 0.71875, 0.4375, 0.78125),
            Block.box(0.3125, 0, 0.59375, 0.4375, 0.3125, 0.71875),
            Block.box(0.5625, 0, 0.28125, 0.6875, 0.3125, 0.40625)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
*/
    public MithrilCrystal(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        switch ((AttachFace)pState.getValue(FACE)) {
            case FLOOR:
                if (direction.getAxis() == Direction.Axis.X) {
                    return makeShape();
                }

                return Utils.rotateXtoZ(makeShape());
            case WALL:
                VoxelShape voxelshape;
                switch (direction) {
                    case EAST:
                        voxelshape = Utils.rotateXtoEast(Utils.rotateXtoZ(makeShape()));
                        break;
                    case WEST://Done
                        voxelshape = Utils.rotateXtoWest(Utils.rotateXtoZ(makeShape()));
                        break;
                    case SOUTH://Done
                        voxelshape = Utils.rotateXtoSouth(Utils.rotateXtoZ(makeShape()));
                        break;
                    case NORTH:
                    case UP:
                    case DOWN://Done
                        voxelshape = Utils.rotateXtoNorth(Utils.rotateXtoZ(makeShape()));
                        break;
                    default:
                        throw new IncompatibleClassChangeError();
                }

                return voxelshape;
            case CEILING:
            default:
                if (direction.getAxis() == Direction.Axis.X) {
                    return Utils.floorToCeiling(makeShape());
                } else {
                    return Utils.floorToCeiling(Utils.rotateXtoZ(makeShape()));
                }
        }
    }

    public VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.40625, 0, 0.40625, 0.59375, 0.75, 0.59375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.28125, 0, 0.21875, 0.46875, 0.4375, 0.40625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.53125, 0, 0.59375, 0.71875, 0.4375, 0.78125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.59375, 0.4375, 0.3125, 0.71875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5625, 0, 0.28125, 0.6875, 0.3125, 0.40625), BooleanOp.OR);

        return shape;
    }

   /* public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }*/

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#rotate} whenever
     * possible. Implementing/overriding is fine.
     */
    /*public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }*/

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#mirror} whenever
     * possible. Implementing/overriding is fine.
     */
    /*public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }*/

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, FACE);
    }

    public void onProjectileHit(Level p_152001_, BlockState p_152002_, BlockHitResult p_152003_, Projectile p_152004_) {
        if (!p_152001_.isClientSide) {
            BlockPos blockpos = p_152003_.getBlockPos();
            p_152001_.playSound((Player)null, blockpos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1.0F, 0.5F + p_152001_.random.nextFloat() * 1.2F);
            p_152001_.playSound((Player)null, blockpos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 0.5F + p_152001_.random.nextFloat() * 1.2F);
        }

    }

}
