package com.prohitman.dragonsdungeons.common.blocks.obj;

import com.prohitman.dragonsdungeons.common.entities.projectiles.FlaskOfFireProjectile;
import com.prohitman.dragonsdungeons.core.datagen.server.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class BrazierBlock extends Block implements SimpleWaterloggedBlock{
    public static BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final boolean isSoulFire;
    public BrazierBlock(Properties pProperties, boolean isSoulFire) {
        super(pProperties);
        this.isSoulFire = isSoulFire;
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(WATERLOGGED, false));
    }
    //TODO:sound handling for dowsing cases, fire flask turning unlit blocks lit
   public VoxelShape makeShape(){
       VoxelShape shape = Shapes.empty();
       shape = Shapes.join(shape, Shapes.box(0.0625, 0.0625, 0.0625, 0.9375, 0.1875, 0.9375), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.03125, 0, 0.03125, 0.03125, 0.125, 0.96875), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.0625, 0.9375, 0.875, 0.0625), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.09375, 0.21875, 0.03125, 0.90625, 0.84375, 0.03125), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.96875, 0.21875, 0.09375, 0.96875, 0.84375, 0.90625), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.03125, 0.21875, 0.09375, 0.03125, 0.84375, 0.90625), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0, 0.1875, 0.5, 1, 1.1875, 0.5), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0, 0.1875, 0.5, 1, 1.1875, 0.5), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.0625, 0.875, 0.0625, 0.9375, 0.875, 0.9375), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.0625, 0.0625, 0.875, 0.9375), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.9375, 0.9375, 0.875, 0.9375), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.9375, 0.1875, 0.0625, 0.9375, 0.875, 0.9375), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.03125, 0, 0.03125, 0.96875, 0.125, 0.03125), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.03125, 0, 0.96875, 0.96875, 0.125, 0.96875), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.96875, 0, 0.03125, 0.96875, 0.125, 0.96875), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.03125, 0.125, 0.0625, 0.1875, 0.875, 0.9375), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.8125, 0.125, 0.0625, 0.96875, 0.875, 0.9375), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.0625, 0.125, 0.03125, 0.9375, 0.875, 0.1875), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.0625, 0.125, 0.8125, 0.9375, 0.875, 0.96875), BooleanOp.OR);
       shape = Shapes.join(shape, Shapes.box(0.09375, 0.21875, 0.96875, 0.90625, 0.84375, 0.96875), BooleanOp.OR);

       return shape;
   }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return makeShape();
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT)) {
            if (pRandom.nextInt(10) == 0) {
                pLevel.playLocalSound((double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.6F, false);
            }

            if(pRandom.nextInt(5) == 0){
                for(int i = 0; i < pRandom.nextIntBetweenInclusive(2, 7); ++i) {
                    pLevel.addParticle(ParticleTypes.SMOKE, (double)pPos.getX() + 0.5D + pRandom.nextDouble() / 4.0D * (double)(pRandom.nextBoolean() ? 1 : -1), (double)pPos.getY() + 0.6D + Mth.nextDouble(pRandom, 0, 0.3d), (double)pPos.getZ() + 0.5D + pRandom.nextDouble() / 4.0D * (double)(pRandom.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
                }
            }

            if (!this.isSoulFire && pRandom.nextInt(8) == 0) {
                for(int i = 0; i < pRandom.nextInt(1) + 1; ++i) {
                    pLevel.addParticle(ParticleTypes.LAVA, (double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, (double)(pRandom.nextFloat() / 2.0F), 5.0E-5D, (double)(pRandom.nextFloat() / 2.0F));
                }
            }

        }
    }

    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        BlockPos blockpos = pHit.getBlockPos();
        if (!pLevel.isClientSide && (pProjectile.isOnFire() || pProjectile instanceof FlaskOfFireProjectile) && pProjectile.mayInteract(pLevel, blockpos) && !pState.getValue(LIT) && !pState.getValue(WATERLOGGED)) {
            pLevel.setBlock(blockpos, pState.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
        }

        if(!pLevel.isClientSide && pProjectile instanceof ThrownPotion thrownPotion && pState.getValue(LIT)){
            ItemStack itemstack = thrownPotion.getItem();
            Potion potion = PotionUtils.getPotion(itemstack);
            List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
            boolean flag = potion == Potions.WATER && list.isEmpty();
            if(flag){
                dowse(null, pLevel, blockpos);
                pLevel.setBlock(blockpos, pState.setValue(BlockStateProperties.LIT, false), 11);
            }
        }

    }

    /*public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pState.getValue(LIT) && pEntity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)pEntity)) {
            pEntity.hurt(pLevel.damageSources().inFire(), 1);
        }

        super.entityInside(pState, pLevel, pPos, pEntity);
    }*/

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (pState.getValue(LIT) && pEntity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)pEntity)) {
            pEntity.hurt(pLevel.damageSources().inFire(), isSoulFire ? 2 : 1);
        }

       super.stepOn(pLevel, pPos, pState, pEntity);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);

        if(!pState.getValue(LIT)){
            if(itemStack.is(ModItemTags.LIGHTABLES)){
                BlockState state = pState.setValue(LIT, true);
                pLevel.setBlock(pPos, state, 2);
                pLevel.playSound(null, pPos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1, 1);

                return InteractionResult.SUCCESS;
            }
        }
        if(pState.getValue(LIT)){
            if(itemStack.is(ItemTags.SHOVELS)){
                BlockState state = pState.setValue(LIT, false);
                pLevel.setBlock(pPos, state, 2);
                dowse(null, pLevel, pPos);
                pLevel.playSound(null, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 1);

                return InteractionResult.SUCCESS;
            }
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
        if (!pState.getValue(BlockStateProperties.WATERLOGGED) && pFluidState.getType() == Fluids.WATER) {
            boolean flag = pState.getValue(LIT);
            if (flag) {
                if (!pLevel.isClientSide()) {
                    pLevel.playSound((Player)null, pPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                dowse((Entity)null, pLevel, pPos);
            }

            pLevel.setBlock(pPos, pState.setValue(WATERLOGGED, Boolean.valueOf(true)).setValue(LIT, Boolean.valueOf(false)), 3);
            pLevel.scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay(pLevel));
            return true;
        } else {
            return false;
        }
    }

    public static void dowse(@Nullable Entity pEntity, LevelAccessor pLevel, BlockPos pPos) {
        if (pLevel.isClientSide()) {
            for(int i = 0; i < 20; ++i) {
                makeParticles((Level)pLevel, pPos, true);
            }
        }

        pLevel.gameEvent(pEntity, GameEvent.BLOCK_CHANGE, pPos);
    }

    public static void makeParticles(Level pLevel, BlockPos pPos, boolean pSpawnExtraSmoke) {
        RandomSource randomsource = pLevel.getRandom();
        //SimpleParticleType simpleparticletype = ParticleTypes.CAMPFIRE_COSY_SMOKE;
        //pLevel.addAlwaysVisibleParticle(simpleparticletype, true, (double)pPos.getX() + 0.5D + randomsource.nextDouble() / 3.0D * (double)(randomsource.nextBoolean() ? 1 : -1), (double)pPos.getY() + randomsource.nextDouble() + randomsource.nextDouble(), (double)pPos.getZ() + 0.5D + randomsource.nextDouble() / 3.0D * (double)(randomsource.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
        if (pSpawnExtraSmoke) {
            pLevel.addParticle(ParticleTypes.SMOKE, (double)pPos.getX() + 0.5D + randomsource.nextDouble() / 4.0D * (double)(randomsource.nextBoolean() ? 1 : -1), (double)pPos.getY() + 0.4D, (double)pPos.getZ() + 0.5D + randomsource.nextDouble() / 4.0D * (double)(randomsource.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
        }
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        LevelAccessor levelaccessor = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        boolean flag = levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, flag).setValue(LIT, !flag);
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT, WATERLOGGED);
    }
}
