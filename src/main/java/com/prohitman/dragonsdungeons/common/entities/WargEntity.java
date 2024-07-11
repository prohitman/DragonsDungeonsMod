package com.prohitman.dragonsdungeons.common.entities;

import com.prohitman.dragonsdungeons.common.entities.goals.AnimatedMeleeAttackGoal;
import com.prohitman.dragonsdungeons.common.entities.goals.FollowLeaderGoal;
import com.prohitman.dragonsdungeons.common.entities.goals.LeaderFightGoal;
import com.prohitman.dragonsdungeons.common.entities.variant.WargVariant;
import com.prohitman.dragonsdungeons.core.datagen.server.ModBiomeTags;
import com.prohitman.dragonsdungeons.core.init.ModEntities;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class WargEntity extends TamableAnimal implements GeoEntity, PlayerRideable, IAttacking, PlayerRideableJumping {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.warg.walk");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("animation.warg.run");
    protected static final RawAnimation SIT_ANIM = RawAnimation.begin().thenLoop("animation.warg.sit");
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.warg.idle");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenLoop("animation.warg.attack");

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(WargEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_RUNNING = SynchedEntityData.defineId(WargEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(WargEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_LEADER = SynchedEntityData.defineId(WargEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DASHING = SynchedEntityData.defineId(WargEntity.class, EntityDataSerializers.BOOLEAN);

    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_289448_) -> {
        EntityType<?> entitytype = p_289448_.getType();
        return entitytype != ModEntities.WARG.get();
    };
    public static final Predicate<LivingEntity> LEADER_SELECTOR = (mob) -> {
        EntityType<?> entitytype = mob.getType();
        if(entitytype == ModEntities.WARG.get()){
            return ((WargEntity)mob).isLeader();
        }
        return false;
    };
    public static final Predicate<WargEntity> LEADER_WARG_PREDICATE =
            mob -> mob != null && mob.isLeader();

    public int attackAnimationTimeout = 0;
    public boolean shouldStartAnim = false;
    private int dashCooldown = 0;
    private boolean isJumping;
    private float playerJumpPendingScale;

    public WargEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(1f);
    }

    public boolean isRunning() {
        return this.entityData.get(IS_RUNNING);
    }
    public void setRunning(boolean is_running) {
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

    public boolean isLeader() {
        return this.entityData.get(IS_LEADER);
    }
    public void setLeader(boolean is_leader) {
        this.entityData.set(IS_LEADER, is_leader);
    }
    public boolean isDashing() {
        return this.entityData.get(IS_DASHING);
    }
    public void setDashing(boolean is_dashing) {
        this.entityData.set(IS_DASHING, is_dashing);
    }
    public boolean isJumping() {
        return this.isJumping;
    }
    public void setIsJumping(boolean pJumping) {
        this.isJumping = pJumping;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes().add(Attributes.MAX_HEALTH, 35D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 5f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f)
                .add(Attributes.JUMP_STRENGTH, 0.42F);
    }
    public void setTame(boolean pTamed) {
        super.setTame(pTamed);
        if (pTamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50D);
            this.setHealth(50F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(35.0);
        }

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(7.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AnimatedMeleeAttackGoal<>(this, 2.4D, true, 5, 10, 0.25));
        this.goalSelector.addGoal(1, new FollowLeaderGoal(this, 2D, 8.0F, 30f));

        this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.75D, 10.0F, 2.0F, false));

        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(Items.ROTTEN_FLESH), true));


        this.goalSelector.addGoal(1, new FollowParentGoal(this, 1.0d));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 4f));

        this.targetSelector.addGoal(3, new NonTameRandomTargetGoal<>(this, Player.class, true, null));
        this.targetSelector.addGoal(3, new NonTameRandomTargetGoal<>(this, AbstractVillager.class, true, null));
        this.targetSelector.addGoal(3, new NonTameRandomTargetGoal<>(this, IronGolem.class, true, null));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, WargEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
        //this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
        this.targetSelector.addGoal(8, new LeaderFightGoal(this, WargEntity.class, true, LEADER_SELECTOR));
    }

    public static boolean checkWargSpawnRules(EntityType<? extends WargEntity> pWolf, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getBlockState(pPos.below()).is(BlockTags.FOXES_SPAWNABLE_ON) && isBrightEnoughToSpawn(pLevel, pPos);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob otherParent) {
        WargEntity warg = ModEntities.WARG.get().create(pLevel);
        if (warg != null && otherParent instanceof WargEntity otherWarg) {
            UUID uuid = this.getOwnerUUID();
            if (uuid != null) {
                warg.setOwnerUUID(uuid);
                warg.setTame(true);
            }
            if(pLevel.random.nextBoolean()){
                warg.setVariant(this.getVariant());
            } else {
                warg.setVariant(otherWarg.getVariant());
            }
        }

        return warg;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllers.add(new AnimationController<>(this, "attackcontroller", 2, this::attackPredicate));
    }

    private PlayState attackPredicate(AnimationState state) {
        if(shouldStartAnim) {
            return state.setAndContinue(ATTACK_ANIM);
        }

        return PlayState.STOP;
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if(this.isInSittingPose()){
            return state.setAndContinue(SIT_ANIM);
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
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_RUNNING, false);
        this.entityData.define(IS_ATTACKING, false);
        this.entityData.define(IS_LEADER, false);
        this.entityData.define(IS_DASHING, false);
    }

    /* Attacking */

    @Override
    public void tick() {
        super.tick();

        if (this.isDashing() && this.dashCooldown < 50 && (this.onGround() || this.isInWater() || this.isPassenger())) {
            this.setDashing(false);
        }

        if (this.dashCooldown > 0) {
            --this.dashCooldown;
            if (this.dashCooldown == 0) {
                this.level().playSound((Player)null, this.blockPosition(), SoundEvents.CAMEL_DASH_READY, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
        }

        if(!this.level().isClientSide){
            if(!this.isVehicle()){
                this.setRunning(this.moveControl.getSpeedModifier() >= 2);
            }
            if(this.isRunning()){
                System.out.println("I am running!");
            } else {
                System.out.println("I am not");
            }
        } else {
            this.setupAttackAnimation();
        }

        List<WargEntity> list = this.level().getEntitiesOfClass(WargEntity.class, this.getBoundingBox().inflate(35.0D), LEADER_WARG_PREDICATE);

        if(list.isEmpty() && !this.isTame() && !this.isBaby()){
            this.setLeader(true);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(7f);
        }
        if(this.isTame() && this.isLeader()){
            this.setLeader(false);
        }
        if(this.isLeader()){
            this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 3));
        }

        //Handles Attack Targets
        if(this.getTarget() != null){
            LivingEntity target = this.getTarget();
            if(target instanceof WargEntity warg){
                if(!this.isLeader() && warg.isLeader()){
                    this.setTarget(null);
                    this.setAggressive(false);
                } else if(this.isTame() && warg.isTame()){
                    if(this.getOwnerUUID() == warg.getOwnerUUID()){
                        this.setTarget(null);
                        this.setAggressive(false);
                    }
                }
            }
            if(target instanceof OwnableEntity tamed && this.isTame()){
                if(((OwnableEntity) target).getOwner() != null && this.isTame()){
                    if(tamed.getOwnerUUID() == this.getOwnerUUID()){
                        this.setTarget(null);
                        this.setAggressive(false);
                    }
                }
            }
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

    //Also Handles Attack Targets
    @Override
    public boolean canAttack(LivingEntity pTarget) {
        if(pTarget instanceof WargEntity warg){
            if(!this.isLeader() && warg.isLeader()){
                return false;
            } else if(this.isTame() && warg.isTame()){
                if(this.getOwnerUUID() == warg.getOwnerUUID()){
                    return false;
                }
            } else {
                return super.canAttack(pTarget);
            }
        }
        if(pTarget instanceof TamableAnimal tamed && this.isTame()){
            if(tamed.isTame() && this.isTame()){
                if(tamed.getOwnerUUID() == this.getOwnerUUID()){
                    return false;
                }
            }
        }
        return super.canAttack(pTarget);
    }

    public boolean canMate(Animal pOtherAnimal) {
        if (pOtherAnimal == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (!(pOtherAnimal instanceof Wolf)) {
            return false;
        } else {
            Wolf wolf = (Wolf)pOtherAnimal;
            if (!wolf.isTame()) {
                return false;
            } else if (wolf.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && wolf.isInLove();
            }
        }
    }

    public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
        if (!(pTarget instanceof Creeper) && !(pTarget instanceof Ghast)) {
            if (pTarget instanceof Wolf) {
                Wolf wolf = (Wolf)pTarget;
                return !wolf.isTame() || wolf.getOwner() != pOwner;
            } else if (pTarget instanceof Player && pOwner instanceof Player && !((Player)pOwner).canHarmPlayer((Player)pTarget)) {
                return false;
            } else if (pTarget instanceof AbstractHorse && ((AbstractHorse)pTarget).isTamed()) {
                return false;
            } else {
                return !(pTarget instanceof TamableAnimal) || !((TamableAnimal)pTarget).isTame();
            }
        } else {
            return false;
        }
    }

    /* VARIANT */

    public WargVariant getVariant() {
        return WargVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(WargVariant variant){
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if(pReason != MobSpawnType.SPAWN_EGG){
            WargVariant variant = getVariantFromBiome(pLevel, this.blockPosition());
            this.setVariant(variant);
        } else {
            WargVariant variant = Util.getRandom(WargVariant.values(), this.random);
            this.setVariant(variant);
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(DATA_ID_TYPE_VARIANT, pCompound.getInt("Variant"));
        pCompound.putBoolean("is_leader", this.entityData.get(IS_LEADER));

    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", this.getTypeVariant());
        this.entityData.set(IS_LEADER, pCompound.getBoolean("is_leader"));
    }

    /* SOUNDS */

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getTarget() != null) {
            return SoundEvents.WOLF_GROWL;
        } else if (this.random.nextInt(3) == 0) {
            return this.isTame() && this.getHealth() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
        } else {
            return SoundEvents.WOLF_AMBIENT;
        }
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

    /* TAMEABLE */

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();

        Item itemForTaming = Items.BONE;

        if(item == itemForTaming && !isTame() && pPlayer.getAbilities().instabuild) {
            if(this.level().isClientSide()) {
                return InteractionResult.CONSUME;
            } else {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                    super.tame(pPlayer);
                    this.navigation.recomputePath();
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte)7);
                    setOrderedToSit(true);
                    this.setInSittingPose(true);
                }

                return InteractionResult.SUCCESS;
            }
        }

        if(isTame()){
            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                this.heal((float)itemstack.getFoodProperties(this).getNutrition());
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                this.level().broadcastEntityEvent(this, (byte)6);
                this.gameEvent(GameEvent.EAT, this);
                return InteractionResult.SUCCESS;
            }
        }

        if(isTame() && pHand == InteractionHand.MAIN_HAND && !isFood(itemstack)) {
            if(!pPlayer.isCrouching()) {
                setRiding(pPlayer);
            } else {
                setOrderedToSit(!isOrderedToSit());
                setInSittingPose(!isOrderedToSit());
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(pPlayer, pHand);
    }

    /* DASHING */

    @Override
    public boolean canCollideWith(Entity pEntity) {
        if(!this.isPassengerOfSameVehicle(pEntity) && this.isDashing()){
            pEntity.hurt(this.damageSources().mobAttack(this), 4);
        }
        return super.canCollideWith(pEntity);
    }

    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);
        if (this.isControlledByLocalInstance()) {
            /*if (pTravelVector.z <= 0.0D) {
                this.gallopSoundCounter = 0;
            }*/

            if (this.onGround()) {
                this.setIsJumping(false);
                if (this.playerJumpPendingScale > 0.0F && !this.isJumping()) {
                    this.executeRidersJump(this.playerJumpPendingScale, pTravelVector);
                }

                this.playerJumpPendingScale = 0.0F;
            }
        }
    }

    protected Vec3 getRiddenInput(Player pPlayer, Vec3 pTravelVector) {
        if (this.onGround() && this.playerJumpPendingScale == 0.0) {
            return Vec3.ZERO;
        } else {
            float f = pPlayer.xxa * 0.5F;
            float f1 = pPlayer.zza;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }

            return new Vec3(f, 0.0D, f1);
        }
    }

    @Override
    public boolean canJump() {
        return true;
    }

    public void onPlayerJump(int pJumpPower) {
        if (this.dashCooldown <= 0 && this.onGround()) {
            if (pJumpPower < 0) {
                pJumpPower = 0;
            }/* else {
                this.allowStandSliding = true;
                this.standIfPossible();
            }*/

            if (pJumpPower >= 90) {
                this.playerJumpPendingScale = 1.0F;
            } else {
                this.playerJumpPendingScale = 0.4F + 0.4F * (float)pJumpPower / 90.0F;
            }
        }
    }

    public boolean canSprint() {
        return true;
    }

    protected void executeRidersJump(float pPlayerJumpPendingScale, Vec3 pTravelVector) {
        double d0 = this.getAttributeValue(Attributes.JUMP_STRENGTH) * (double)this.getBlockJumpFactor() + (double)this.getJumpBoostPower();
        this.addDeltaMovement(this.getLookAngle().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)(22.2222F * pPlayerJumpPendingScale) * this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (double)this.getBlockSpeedFactor()).add(0.0D, (double)(1.4285F * pPlayerJumpPendingScale) * d0, 0.0D));
        this.dashCooldown = 55;
        this.setDashing(true);
        this.hasImpulse = true;
    }

    public void handleStartJump(int pJumpPower) {
        this.playSound(SoundEvents.WOLF_GROWL, 1.0F, 1.0F);
        this.setDashing(true);
    }

    public void handleStopJump() {
    }

    public int getJumpCooldown() {
        return this.dashCooldown;
    }

    //protected boolean canPerformRearing() {
    //    return false;
    //}

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (!this.firstTick && IS_DASHING.equals(pKey)) {
            this.dashCooldown = this.dashCooldown == 0 ? 55 : this.dashCooldown;
        }

        super.onSyncedDataUpdated(pKey);
    }


    /* RIDEABLE */

    private void setRiding(Player pPlayer) {
        this.setInSittingPose(false);

        pPlayer.setYRot(this.getYRot());
        pPlayer.setXRot(this.getXRot());
        pPlayer.startRiding(this);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return ((LivingEntity) this.getFirstPassenger());
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if(this.isVehicle() && getControllingPassenger() instanceof Player) {
            LivingEntity livingentity = this.getControllingPassenger();
            this.setYRot(livingentity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(livingentity.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;
            float f = livingentity.xxa * 0.5F;
            float f1 = livingentity.zza;

            // Inside this if statement, we are on the client!
            if (this.isControlledByLocalInstance()) {
                float speedBonus = this.getJumpCooldown() == 0 ? 0.1f : 0;
                float newSpeed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) + speedBonus;
                this.setRunning(false);
                // increasing speed by 100% if the spring key is held down (number for testing purposes)
                if(Minecraft.getInstance().options.keySprint.isDown()) {
                    newSpeed *= 1.7f;
                }

                if(newSpeed > (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) + speedBonus && !this.level().isClientSide){
                    this.setRunning(true);
                }

                this.setSpeed(newSpeed);
                super.travel(new Vec3(f, pTravelVector.y, f1));
            }
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity pLivingEntity) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis() != Direction.Axis.Y) {
            int[][] offsets = DismountHelper.offsetsForDirection(direction);
            BlockPos blockpos = this.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (Pose pose : pLivingEntity.getDismountPoses()) {
                AABB aabb = pLivingEntity.getLocalBoundsForPose(pose);

                for (int[] offset : offsets) {
                    blockpos$mutableblockpos.set(blockpos.getX() + offset[0], blockpos.getY(), blockpos.getZ() + offset[1]);
                    double d0 = this.level().getBlockFloorHeight(blockpos$mutableblockpos);
                    if (DismountHelper.isBlockFloorValid(d0)) {
                        Vec3 vec3 = Vec3.upFromBottomCenterOf(blockpos$mutableblockpos, d0);
                        if (DismountHelper.canDismountTo(this.level(), pLivingEntity, aabb.move(vec3))) {
                            pLivingEntity.setPose(pose);
                            return vec3;
                        }
                    }
                }
            }
        }

        return super.getDismountLocationForPassenger(pLivingEntity);
    }

    public boolean isFood(ItemStack pStack) {
        Item item = pStack.getItem();
        return item.isEdible() && pStack.getFoodProperties(this).isMeat();
    }

    public static WargVariant getVariantFromBiome(LevelReader levelIn, BlockPos pos){
        if(levelIn.getBiome(pos).is(ModBiomeTags.ASHEN_WARG_SPAWNS)){
            return WargVariant.ASHEN;
        } else if(levelIn.getBiome(pos).is(ModBiomeTags.BLACK_WARG_SPAWNS)){
            return WargVariant.BLACK;
        }else if(levelIn.getBiome(pos).is(ModBiomeTags.CHESTNUT_WARG_SPAWNS)){
            return WargVariant.CHESTNUT;
        }else if(levelIn.getBiome(pos).is(ModBiomeTags.RUSTY_WARG_SPAWNS)){
            return WargVariant.RUSTY;
        }else if(levelIn.getBiome(pos).is(ModBiomeTags.SNOWY_WARG_SPAWNS)){
            return WargVariant.SNOWY;
        }else if(levelIn.getBiome(pos).is(ModBiomeTags.SPOTTED_WARG_SPAWNS)){
            return WargVariant.SPOTTED;
        }else if(levelIn.getBiome(pos).is(ModBiomeTags.STRIPED_WARG_SPAWNS)){
            return WargVariant.STRIPED;
        }else if(levelIn.getBiome(pos).is(ModBiomeTags.WOOD_WARG_SPAWNS)){
            return WargVariant.WOOD;
        } else{
            return WargVariant.PALE;
        }
    }
}
