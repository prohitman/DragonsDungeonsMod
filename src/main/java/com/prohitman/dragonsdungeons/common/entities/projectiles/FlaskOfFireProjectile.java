package com.prohitman.dragonsdungeons.common.entities.projectiles;

import com.google.common.collect.Lists;
import com.prohitman.dragonsdungeons.core.init.ModEntities;
import com.prohitman.dragonsdungeons.core.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class FlaskOfFireProjectile extends ThrowableItemProjectile {
    public FlaskOfFireProjectile(EntityType<? extends FlaskOfFireProjectile> typeIn, Level levelIn) {
        super(typeIn, levelIn);
    }

    public FlaskOfFireProjectile(Level levelIn, LivingEntity livingEntityIn) {
        super(ModEntities.FLASK_OF_FIRE_PROJECTILE.get(), livingEntityIn, levelIn);
    }

    public FlaskOfFireProjectile(Level levelIn, double x, double y, double z) {
        super(ModEntities.FLASK_OF_FIRE_PROJECTILE.get(), x, y, z, levelIn);
    }

    protected Item getDefaultItem() {
        return ModItems.FLASK_OF_FIRE.get();
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onClientRemoval() {
        this.animateParticles(this.level(), this.blockPosition(), this.random);
    }

    public void animateParticles(Level levelIn, BlockPos pos, RandomSource rand) {
        float radius = 1.5F;
        for (float x = -radius; x < radius; x += 0.25f) {//0.25
            for (float z = -radius; z < radius; z += 0.25f) {//0.25
                if (x * x + z * z < radius * radius) {
                    double d0 = (double) pos.getX() + x + (float)(rand.nextInt(9) / 5);
                    double d1 = pos.getY();
                    double d2 = (double) pos.getZ() + z + (float)(rand.nextInt(9) / 5);
                    levelIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                    //levelIn.addParticle(ParticleTypes.ASH, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                    levelIn.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0D, 0.0D, 0.0D);

                }
            }
        }
        //cool flying fiery circle
        /*int radius2 = 3;
        for(float a = 0; a < Math.PI * 2; a += Math.PI / 180){
            float x = (float) (Math.cos(a) * radius2);
            float z = (float) (Math.sin(a) * radius2);
            double d0 = (double) pos.getX() + x;
            double d1 = pos.getY();
            double d2 = (double) pos.getZ() + z;
            levelIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, 2.0D, 0.0D, 2.0D);
            levelIn.addParticle(ParticleTypes.LANDING_LAVA, d0, d1, d2, 2.0D, 0.0D, 2.0D);

        }*/

        float radius2 = 1.25f;
        double speed = 0.5;
        for (float a = 0; a < Math.PI * 2; a += Math.PI / 180) {
            float x = (float) (Math.cos(a) * radius2);
            float z = (float) (Math.sin(a) * radius2);

            double particleXSpeed = Math.cos(a) * speed;
            double particleZSpeed = Math.sin(a) * speed;

            double d0 = (double) pos.getX() + x;
            double d1 = pos.getY();
            double d2 = (double) pos.getZ() + z;
            levelIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, particleXSpeed, 0.0D, particleZSpeed);
            //levelIn.addParticle(ParticleTypes.LAVA, d0, d1, d2, particleXSpeed, 0.0D, particleZSpeed);
        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(HitResult result) {
        super.onHit(result);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 1, 1);
        if (!this.level().isClientSide) {
            Potion potion = Potions.FIRE_RESISTANCE;
            List<MobEffectInstance> list = Lists.newArrayList();
            list.add(new MobEffectInstance(MobEffects.HARM, 1, 1));

            if (!list.isEmpty()) {
                this.getAndEffectNearbyEntities(list, result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) result).getEntity() : null);
            }
            if(result instanceof BlockHitResult){
                spawnFire(this.random.nextInt(10));
                level().gameEvent(this.getOwner(), GameEvent.BLOCK_PLACE, this.blockPosition());
            }
            int i = potion.hasInstantEffects() ? 2007 : 2002;
            this.level().levelEvent(i, this.blockPosition(), PotionUtils.getColor(potion));
            this.discard();
        }
    }

    private void spawnFire(int pExtraIgnitions) {
        if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            BlockPos blockpos = this.blockPosition();
            BlockState blockstate = BaseFireBlock.getState(this.level(), blockpos);
            if (this.level().getBlockState(blockpos).isAir() && blockstate.canSurvive(this.level(), blockpos)) {
                this.level().setBlockAndUpdate(blockpos, blockstate);
            }

            for(int i = 0; i < pExtraIgnitions; ++i) {
                BlockPos blockpos1 = blockpos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
                blockstate = BaseFireBlock.getState(this.level(), blockpos1);
                if (this.level().getBlockState(blockpos1).isAir() && blockstate.canSurvive(this.level(), blockpos1)) {
                    this.level().setBlockAndUpdate(blockpos1, blockstate);
                }
            }

        }
    }

    private void getAndEffectNearbyEntities(List<MobEffectInstance> mobeffectinstance, @Nullable Entity entity) {
        AABB axisalignedbb = this.getBoundingBox().inflate(10.0D, 3.0D, 10.0D);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb);
        if (!list.isEmpty()) {
            for (LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double d0 = this.distanceToSqr(livingentity);
                    if (d0 < 16.0D) {//was 16
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;//was 4
                        if (livingentity == entity) {
                            d1 = 1.0D;
                        }
                        livingentity.setSecondsOnFire(1000);
                        for (MobEffectInstance effectinstance : mobeffectinstance) {
                            MobEffect effect = effectinstance.getEffect();
                            if (effect.isInstantenous()) {
                                effect.applyInstantenousEffect(this, this.getOwner(), livingentity, effectinstance.getAmplifier(), d1);

                            } else {
                                int i = (int) (d1 * (double) effectinstance.getDuration() + 0.5D);
                                if (i > 20) {
                                    livingentity.addEffect(new MobEffectInstance(effect, i, effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.isVisible()));
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
