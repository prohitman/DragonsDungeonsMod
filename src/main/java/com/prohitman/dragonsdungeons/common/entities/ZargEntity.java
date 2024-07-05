package com.prohitman.dragonsdungeons.common.entities;

import com.prohitman.dragonsdungeons.common.entities.goals.AnimatedMeleeAttackGoal;
import com.prohitman.dragonsdungeons.common.entities.goals.FollowLeaderGoal;
import com.prohitman.dragonsdungeons.common.entities.goals.LeaderFightGoal;
import com.prohitman.dragonsdungeons.core.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class ZargEntity extends Animal implements GeoEntity, IAttacking, Enemy {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.warg.walk");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("animation.warg.run");
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.warg.idle");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenLoop("animation.warg.attack");

    private static final EntityDataAccessor<Boolean> IS_RUNNING = SynchedEntityData.defineId(ZargEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(ZargEntity.class, EntityDataSerializers.BOOLEAN);
    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_289448_) -> {
        EntityType<?> entitytype = p_289448_.getType();
        return entitytype != ModEntities.ZARG.get();
    };

    public int attackAnimationTimeout = 0;
    public boolean shouldStartAnim = false;
    public ZargEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(1);
    }

    public boolean isRunning() {
        return this.entityData.get(IS_RUNNING);
    }
    public void setIsRunning(boolean is_running) {
        this.entityData.set(IS_RUNNING, is_running);
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

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes().add(Attributes.MAX_HEALTH, 35D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 5f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AnimatedMeleeAttackGoal<>(this, 2.4D, true, 5, 10, 0.25));

        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(Items.ROTTEN_FLESH), true));


        this.goalSelector.addGoal(1, new FollowParentGoal(this, 1.0d));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 4f));

        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, true, (livingEntity -> {
            return !livingEntity.isSpectator() && !((Player)livingEntity).isCreative();
        })));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true, null));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true, null));

        this.targetSelector.addGoal(4, (new HurtByTargetGoal(this, ZargEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        //this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob otherParent) {
        return ModEntities.ZARG.get().create(pLevel);
    }


    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
        //controllers.add(new AnimationController<>(this, "attackcontroller", 2, this::attackPredicate));
    }

    /*private PlayState attackPredicate(AnimationState state) {
        if(shouldStartAnim) {
            //state.getController().forceAnimationReset();
            state.getController().setAnimation(ATTACK_ANIM);
        }

        return PlayState.STOP;
    }*/

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if(shouldStartAnim){
            return state.setAndContinue(ATTACK_ANIM);
        }
        else if(this.isRunning() && state.isMoving()){
            return state.setAndContinue(RUN_ANIM);
        }
        else if (state.isMoving()) {
            return state.setAndContinue(WALK_ANIM);
        }

        return state.setAndContinue(IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_RUNNING, false);
        this.entityData.define(IS_ATTACKING, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    /* SOUNDS */

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WOLF_GROWL;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    /* Attacking */

    @Override
    public void tick() {
        super.tick();

        if(!this.level().isClientSide){
            this.setIsRunning(this.moveControl.getSpeedModifier() >= 2);
        } else {
            this.setupAttackAnimation();
        }
    }

    private void setupAttackAnimation() {
        if(this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 10; // Duration of the attack animation in Ticks.
            shouldStartAnim = true;
        } else {
            --this.attackAnimationTimeout;
        }

        if(!this.isAttacking()) {
            shouldStartAnim = false;
        }
    }
}
