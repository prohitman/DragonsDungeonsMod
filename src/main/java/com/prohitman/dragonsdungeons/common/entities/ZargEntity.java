package com.prohitman.dragonsdungeons.common.entities;

import com.prohitman.dragonsdungeons.common.entities.goals.AnimatedMeleeAttackGoal;
import com.prohitman.dragonsdungeons.common.entities.goals.FollowLeaderGoal;
import com.prohitman.dragonsdungeons.common.entities.goals.LeaderFightGoal;
import com.prohitman.dragonsdungeons.core.init.ModEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
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
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
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
    private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZargEntity.class, EntityDataSerializers.BOOLEAN);

    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_289448_) -> {
        EntityType<?> entitytype = p_289448_.getType();
        return entitytype != ModEntities.ZARG.get();
    };

    public int attackAnimationTimeout = 0;
    public boolean shouldStartAnim = false;
    private int villagerConversionTime;
    @javax.annotation.Nullable
    private UUID conversionStarter;

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

        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 2, false, true, (livingEntity -> {
            return !livingEntity.isSpectator() && !((Player)livingEntity).isCreative();
        })));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true, null));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true, null));

        this.targetSelector.addGoal(4, (new HurtByTargetGoal(this, ZargEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        //this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public static boolean checkZargSpawnRules(EntityType<? extends ZargEntity> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getBlockState(pPos.below()).is(BlockTags.FOXES_SPAWNABLE_ON) && pLevel.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(pLevel, pPos, pRandom) && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = pLevel.getRandom();
        if(randomsource.nextInt(100) == 0){
            this.setBaby(true);
        }
        if (randomsource.nextFloat() < 0.4 && !this.isBaby()) {
            spawnRidingZombie(pLevel, pDifficulty);
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public void spawnRidingZombie(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty){
        Zombie zombie = EntityType.ZOMBIE.create(this.level());
        if (zombie != null) {
            zombie.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            boolean zombieHasSword = false;

            for(EquipmentSlot equipmentslot : EquipmentSlot.values()) {

                if (equipmentslot.getType() == EquipmentSlot.Type.ARMOR && pLevel.getRandom().nextBoolean()) {
                    ItemStack itemstack = zombie.getItemBySlot(equipmentslot);

                    if (itemstack.isEmpty()) {
                        int i = pLevel.getRandom().nextInt(2) + 2;

                        Item item = getEquipmentForSlot(equipmentslot, i);
                        if (item != null) {
                            zombie.setItemSlot(equipmentslot, new ItemStack(item));

                            if(pLevel.getRandom().nextInt(3) == 0){
                                zombie.setItemSlot(equipmentslot, EnchantmentHelper.enchantItem(pLevel.getRandom(), new ItemStack(item), (int)(5.0F + 1 * (float)pLevel.getRandom().nextInt(18)), false));
                            }
                        }
                    }

                } else if(equipmentslot == EquipmentSlot.MAINHAND && pLevel.getRandom().nextInt(3) == 0 && !zombieHasSword){
                    ItemStack itemStack = zombie.getItemBySlot(equipmentslot);
                    if(itemStack.isEmpty()){
                        zombie.setItemSlot(equipmentslot, new ItemStack(Items.IRON_SWORD));
                        zombieHasSword = true;
                    }
                }
            }

            zombie.finalizeSpawn(pLevel, pDifficulty, MobSpawnType.JOCKEY, (SpawnGroupData)null, (CompoundTag)null);
            zombie.startRiding(this);
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob otherParent) {
        return ModEntities.ZARG.get().create(pLevel);
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
        if(this.isRunning() && state.isMoving()){
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

    /* Conversion */

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(Items.GOLDEN_APPLE)) {
            if (!pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            if (!this.level().isClientSide) {
                this.startConverting(pPlayer.getUUID(), this.random.nextInt(600) + 1000);
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    /**
     * Returns whether this zombie is in the process of converting to a villager
     */
    public boolean isConverting() {
        return this.getEntityData().get(DATA_CONVERTING_ID);
    }

    /**
     * Starts conversion of this zombie villager to a villager
     */
    private void startConverting(@javax.annotation.Nullable UUID pConversionStarter, int pVillagerConversionTime) {
        this.conversionStarter = pConversionStarter;
        this.villagerConversionTime = pVillagerConversionTime;
        this.getEntityData().set(DATA_CONVERTING_ID, true);
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, pVillagerConversionTime, Math.min(this.level().getDifficulty().getId() - 1, 0)));
        this.level().broadcastEntityEvent(this, (byte)16);
    }

    /**
     * Handles an entity event received from a {@link net.minecraft.network.protocol.game.ClientboundEntityEventPacket}.
     */
    public void handleEntityEvent(byte pId) {
        if (pId == 16) {
            if (!this.isSilent()) {
                this.level().playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
            }

        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Nullable
    public WargEntity convertZargToWarg(EntityType<WargEntity> pEntityType) {
        if (this.isRemoved()) {
            return null;
        } else {
            WargEntity t = pEntityType.create(this.level());
            if (t == null) {
                return (WargEntity)null;
            } else {
                t.copyPosition(this);
                t.setBaby(this.isBaby());
                t.setNoAi(this.isNoAi());
                if (this.hasCustomName()) {
                    t.setCustomName(this.getCustomName());
                    t.setCustomNameVisible(this.isCustomNameVisible());
                }

                if (this.isPersistenceRequired()) {
                    t.setPersistenceRequired();
                }

                t.setInvulnerable(this.isInvulnerable());

                t.setTame(true);
                t.setOwnerUUID(this.conversionStarter);

                this.level().addFreshEntity(t);
                if (this.isPassenger()) {
                    Entity entity = this.getVehicle();
                    this.stopRiding();
                    t.startRiding(entity, true);
                }

                this.discard();
                return t;
            }
        }
    }

    private void finishConversion(ServerLevel pServerLevel) {
        WargEntity warg = this.convertZargToWarg(ModEntities.WARG.get());

        warg.finalizeSpawn(pServerLevel, pServerLevel.getCurrentDifficultyAt(warg.blockPosition()), MobSpawnType.CONVERSION, (SpawnGroupData)null, (CompoundTag)null);

        if (!this.isSilent()) {
            pServerLevel.levelEvent((Player)null, 1027, this.blockPosition(), 0);
        }
        net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, warg);
    }

    private int getConversionProgress() {
        int i = 1;
        if (this.random.nextFloat() < 0.01F) {
            int j = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(int k = (int)this.getX() - 4; k < (int)this.getX() + 4 && j < 14; ++k) {
                for(int l = (int)this.getY() - 4; l < (int)this.getY() + 4 && j < 14; ++l) {
                    for(int i1 = (int)this.getZ() - 4; i1 < (int)this.getZ() + 4 && j < 14; ++i1) {
                        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos.set(k, l, i1));
                        if (blockstate.is(Blocks.IRON_BARS) || blockstate.getBlock() instanceof BedBlock) {
                            if (this.random.nextFloat() < 0.3F) {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_RUNNING, false);
        this.entityData.define(IS_ATTACKING, false);
        this.entityData.define(DATA_CONVERTING_ID, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("ConversionTime", 99) && pCompound.getInt("ConversionTime") > -1) {
            this.startConverting(pCompound.hasUUID("ConversionPlayer") ? pCompound.getUUID("ConversionPlayer") : null, pCompound.getInt("ConversionTime"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("ConversionTime", this.isConverting() ? this.villagerConversionTime : -1);
        if (this.conversionStarter != null) {
            pCompound.putUUID("ConversionPlayer", this.conversionStarter);
        }
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

        if (!this.level().isClientSide && this.isAlive() && this.isConverting()) {
            int i = this.getConversionProgress();
            this.villagerConversionTime -= i;
            if (this.villagerConversionTime <= 0 && ForgeEventFactory.canLivingConvert(this, EntityType.VILLAGER, (timer) -> this.villagerConversionTime = timer)) {
                this.finishConversion((ServerLevel)this.level());
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
}
