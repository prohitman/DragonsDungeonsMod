package com.prohitman.dragonsdungeons.common.entities;

import com.prohitman.dragonsdungeons.common.entities.goals.emblemscute.EmblemscuteFollowMonsterGoal;
import com.prohitman.dragonsdungeons.common.entities.goals.emblemscute.EmblemscuteHidingGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Predicate;

public class EmblemscuteEntity extends PathfinderMob implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private final LookAtPlayerGoal look_player_goal = new LookAtPlayerGoal(this, Player.class, 8.0F) {
        @Override
        public boolean canUse() {
            if(this.mob instanceof EmblemscuteEntity){
                if(((EmblemscuteEntity)this.mob).getHiding()){
                    return false;
                }
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if(this.mob instanceof EmblemscuteEntity){
                if(((EmblemscuteEntity)this.mob).getHiding()){
                    return false;
                }
            }
            return super.canContinueToUse();
        }
    };

    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("Walk");
    protected static final RawAnimation HIDING_ANIM = RawAnimation.begin().thenLoop("Hiding");
    protected static final RawAnimation HIDE_ANIM = RawAnimation.begin().thenLoop("Hide");
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("Idle");

    public static final byte ANIMATION_IDLE = 0;
    public static final byte ANIMATION_HIDE = 1;
    public static final byte ANIMATION_WALK = 2;

    protected static final EntityDataAccessor<Byte> ANIMATION = SynchedEntityData.defineId(EmblemscuteEntity.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Boolean> HIDING = SynchedEntityData.defineId(EmblemscuteEntity.class, EntityDataSerializers.BOOLEAN);

    private static final Predicate<Entity> AVOID_PLAYERS = (entity) -> !entity.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity);
    public int hidinganimationTimer;

    public EmblemscuteEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
        this.setMaxUpStep(1);
        this.goalSelector.addGoal(8, this.look_player_goal);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new EmblemscuteWanderGoal(this, 0.5D, 5));
        //this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        //this.goalSelector.addGoal(8, this.look_player_goal);
        this.goalSelector.addGoal(3, new EmblemscuteFollowMonsterGoal(this, 1.5, 5, 15));
        this.goalSelector.addGoal(8, new EmblemscuteHidingGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 45.0D)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10)
                .add(Attributes.ARMOR_TOUGHNESS, 10);
    }

    public static boolean checkEmblemscuteSpawnRules(EntityType<? extends EmblemscuteEntity> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getDifficulty() != Difficulty.PEACEFUL && pPos.getY() < pLevel.getSeaLevel() - 5 && Monster.isDarkEnoughToSpawn(pLevel, pPos, pRandom) && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
    }

    public byte getAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public void setAnimation(byte pVariant) {
        this.entityData.set(ANIMATION, pVariant);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, ANIMATION_IDLE);
        this.entityData.define(HIDING, false);
    }

    public boolean getHiding(){
        return this.entityData.get(HIDING);
    }

    public void setHiding(boolean hiding){
        this.entityData.set(HIDING, hiding);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setHiding(pCompound.getBoolean("IsHiding"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("IsHiding", this.getHiding());
    }

    /*@Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new GroundPathNavigation(this, pLevel);
    }*/

    @Override
    public void tick() {
        super.tick();

        /*if(!this.moveControl.hasWanted() && this.getAnimation() == ANIMATION_IDLE){
            this.setYBodyRot(this.getYRot());
        }*/
        if(this.getHiding()){
            this.getNavigation().stop();
            //this.setYBodyRot(this.getYRot());
            //this.setYHeadRot(this.getYRot());
            //this.setDeltaMovement(Vec3.ZERO);
            this.setAnimation(ANIMATION_HIDE);
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, 2));
            //this.setBoundingBox(this.getBoundingBox().);
        }else{
            if (hidinganimationTimer > 0) {
                hidinganimationTimer--;
                if (hidinganimationTimer == 0) {
                    setAnimation(ANIMATION_IDLE);
                }
            }
        }

    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        if(this.getAnimation() != ANIMATION_IDLE){
            return pSize.height * 0.2F;
        }
        return pSize.height * 0.25F;
    }

    @Override
    public int getExperienceReward() {
        return 30;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.TURTLE_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.TURTLE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.TURTLE_DEATH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.TURTLE_SHAMBLE, 0.15F, 1.0F);
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    /*protected class EmbemscutePathNavigator extends GroundPathNavigation{
        private EmblemscuteEntity entity;

        public EmbemscutePathNavigator(EmblemscuteEntity entity, Level level) {
            super(entity, level);
            this.entity = entity;
        }


    }*/
    protected BodyRotationControl createBodyControl() {
        return new EmblemscuteEntity.EmblemscuteBodyRotationControl(this);
    }

    class EmblemscuteBodyRotationControl extends BodyRotationControl {
        public EmblemscuteBodyRotationControl(EmblemscuteEntity emblemscuteEntity) {
            super(emblemscuteEntity);
        }

        /**
         * Update the Head and Body rendering angles
         */
        public void clientTick() {
            if (!EmblemscuteEntity.this.getHiding()) {
                super.clientTick();
            }

        }
    }


    protected class EmblemscuteWanderGoal extends WaterAvoidingRandomStrollGoal {
        private EmblemscuteEntity entity;

        public EmblemscuteWanderGoal(EmblemscuteEntity entity, double speedmodifier, float probability) {
            super(entity, speedmodifier, probability);
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            if(this.entity.getHiding()){
                return false;
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if(this.entity.getHiding()){
                return false;
            }
            return super.canContinueToUse();
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller", 10, this::walkAnimController));

    }

    private PlayState walkAnimController(AnimationState<EmblemscuteEntity> state) {
        float limbSwingAmount = state.getLimbSwingAmount();

        boolean isMoving = !(limbSwingAmount > -0.05F && limbSwingAmount < 0.05F);
        AnimationController controller = state.getController();

        byte currentAnimation = this.getAnimation();

        if (currentAnimation == ANIMATION_HIDE) {
            controller.setAnimation(HIDING_ANIM);
            //this.animateParticles(this.level, this.blockPosition(), this.getRandom());
        }
        //return PlayState.CONTINUE;
        else {//controller.markNeedsReload();
            if (isMoving) {
                controller.setAnimation(WALK_ANIM);
                this.setAnimation(ANIMATION_WALK);
            } else {
                controller.setAnimation(IDLE_ANIM);
                this.setAnimation(ANIMATION_IDLE);
            }
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
