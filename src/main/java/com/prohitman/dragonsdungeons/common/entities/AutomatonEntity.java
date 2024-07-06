package com.prohitman.dragonsdungeons.common.entities;

import com.prohitman.dragonsdungeons.common.entities.goals.automaton.AutomatonMeleeGoal;
import com.prohitman.dragonsdungeons.common.entities.goals.automaton.AutomatonRangedGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class AutomatonEntity extends PathfinderMob implements GeoEntity, IAttacking, RangedAttackMob, IShooting, Enemy {

    //public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(AutomatonEntity.class,
    //        EntityDataSerializers.INT);
    //public static final EntityDataAccessor<Boolean> ATTACKABLE = SynchedEntityData.defineId(AutomatonEntity.class,
    //        EntityDataSerializers.BOOLEAN);
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("run");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenLoop("chop");
    protected static final RawAnimation SHOOT_ANIM = RawAnimation.begin().thenLoop("shooting");
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

    private static final EntityDataAccessor<Boolean> IS_RUNNING = SynchedEntityData.defineId(AutomatonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(AutomatonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SHOOTING = SynchedEntityData.defineId(AutomatonEntity.class, EntityDataSerializers.BOOLEAN);


    //protected static final EntityDataAccessor<Byte> ANIMATION = SynchedEntityData.defineId(AutomatonEntity.class, EntityDataSerializers.BYTE);

    //public int animationTick = 0;
    public int attackAnimationTimeout = 0;
    public boolean shouldStartAttackAnim = false;
    public int shootAnimationTimeout = 0;
    public boolean shouldStartShootAnim = false;

    public AutomatonEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        //this.goalSelector.addGoal(5, new AutomatonRangedMeleeAttack<>(this, 1.0D, 20, 20.0F));
        //this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, false));
        //this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(0, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(1, new AutomatonMeleeGoal(this, 1.25D, true, 10, 22, 2));
        this.goalSelector.addGoal(1, new AutomatonRangedGoal(this, 1.15D, 0, 15F, 12, 35));

        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.targetSelector.addGoal(8, (new HurtByTargetGoal(this, this.getClass())).setAlertOthers());
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Player.class,  false, true));
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Camel.class, 0, false, false, null));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.FOLLOW_RANGE, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 5.0D);
    }

    public boolean isRunning() {
        return this.entityData.get(IS_RUNNING);
    }
    public void setRunning(boolean is_running) {
        this.entityData.set(IS_RUNNING, is_running);
    }
    public boolean isShooting() {
        return this.entityData.get(IS_SHOOTING);
    }
    public void setShooting(boolean is_shooting) {
        this.entityData.set(IS_SHOOTING, is_shooting);
    }
    public boolean isAttacking() {
        return this.entityData.get(IS_ATTACKING);
    }
    public void setAttacking(boolean attacking) {
        this.entityData.set(IS_ATTACKING, attacking);
    }
    @Override
    public void setAttackAnimationTimeOut(int attackAnimTimeOut) {
        this.attackAnimationTimeout = attackAnimTimeOut;
    }
    @Override
    public void setShootAnimationTimeOut(int shootAnimTimeOut) {
        this.shootAnimationTimeout = shootAnimTimeOut;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_RUNNING, false);
        this.entityData.define(IS_ATTACKING, false);
        this.entityData.define(IS_SHOOTING, false);
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height * 0.85f;
    }

    @Override
    public int getExperienceReward() {
        return 10;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.level().isClientSide){
            this.setRunning(this.moveControl.getSpeedModifier() > 1);
        } else {
            this.setupAttackAnimation();
            this.setupShootAnimation();
        }
    }

    private void setupAttackAnimation() {
        if(this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 22; // Duration of the attack animation in Ticks.
            shouldStartAttackAnim = true;
        } else {
            --this.attackAnimationTimeout;
        }

        if(!this.isAttacking()) {
            shouldStartAttackAnim = false;
        }
    }

    private void setupShootAnimation() {
        if(this.isShooting() && shootAnimationTimeout <= 0) {
            shootAnimationTimeout = 35; // Duration of the attack animation in Ticks.
            shouldStartShootAnim = true;
        } else {
            --this.shootAnimationTimeout;
        }

        if(!this.isShooting()) {
            shouldStartShootAnim = false;
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
        controllers.add(new AnimationController<>(this, "attackcontroller", 2, this::attackPredicate));
    }

   private PlayState attackPredicate(AnimationState state) {
        if(shouldStartAttackAnim) {
            return state.setAndContinue(ATTACK_ANIM);
        } else if(shouldStartShootAnim){
            return state.setAndContinue(SHOOT_ANIM);
        }

        return PlayState.STOP;
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        float limbSwingAmount = state.getLimbSwingAmount();

        boolean isMoving = !(limbSwingAmount > -0.05F && limbSwingAmount < 0.05F);

        if(this.isRunning() && state.isMoving()){
            return state.setAndContinue(RUN_ANIM);
        }
        else if (isMoving) {
            return state.setAndContinue(WALK_ANIM);
        }

        return state.setAndContinue(IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {

        ArrowItem arrowitem = (ArrowItem)(Items.ARROW);
        ItemStack arrowstack = new ItemStack(arrowitem);
        Vec3 vec3 = this.getViewVector(1.0F);
        AbstractArrow abstractarrow = arrowitem.createArrow(this.level(), arrowstack, this);
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        //abstractarrow.setPos(this.getX() + 0.5 /*+ vec3.x * 4*/, this.getEyeY() - (double)0.1F, this.getZ() - 0.5 /*+ vec3.z * 1*/);
        abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.8F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }
}
