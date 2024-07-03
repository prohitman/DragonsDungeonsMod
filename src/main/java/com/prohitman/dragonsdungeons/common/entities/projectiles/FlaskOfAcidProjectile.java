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
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class FlaskOfAcidProjectile extends ThrowableItemProjectile {

    public FlaskOfAcidProjectile(EntityType<? extends FlaskOfAcidProjectile> typeIn, Level levelIn) {
        super(typeIn, levelIn);
    }

    public FlaskOfAcidProjectile(Level levelIn, LivingEntity livingEntityIn) {
        super(ModEntities.FLASK_OF_ACID_PROJECTILE.get(), livingEntityIn, levelIn);
    }

    public FlaskOfAcidProjectile(Level levelIn, double x, double y, double z) {
        super(ModEntities.FLASK_OF_ACID_PROJECTILE.get(), x, y, z, levelIn);
    }

    protected Item getDefaultItem() {
        return ModItems.FLASK_OF_ACID.get();
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    public void onClientRemoval() {
        this.animateTick(this.level(), this.blockPosition(), this.random);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void animateTick(Level levelIn, BlockPos pos, RandomSource rand) {
        float radius = 1.5F;
        for (float x = -radius; x < radius; x += 0.25f) {
            for (float z = -radius; z < radius; z += 0.25f) {
                if (x * x + z * z < radius * radius) {
                    double d0 = (double) pos.getX() + x + (rand.nextInt(9) / 5);
                    double d1 = pos.getY();
                    double d2 = (double) pos.getZ() + z + (rand.nextInt(9) / 5);
                    levelIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.01D, 0.0D);
                    levelIn.addParticle(ParticleTypes.ASH, d0, d1, d2, 0.0D, 0.01D, 0.0D);

                }
            }
        }

    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(HitResult result) {
        super.onHit(result);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1, 1);
        if (!this.level().isClientSide) {
            Potion potion = Potions.POISON;
            List<MobEffectInstance> list = Lists.newArrayList();
            list.add(new MobEffectInstance(MobEffects.POISON, 1000, 2));
            list.add(new MobEffectInstance(MobEffects.WEAKNESS, 1000, 0));
            list.add(new MobEffectInstance(MobEffects.WITHER, 1000, 1));

            if (!list.isEmpty()) {
                this.applySplash(list, result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)result).getEntity() : null);

            }
            int i = potion.hasInstantEffects() ? 2007 : 2002;
            this.level().levelEvent(i, this.blockPosition(), PotionUtils.getColor(potion));
            this.discard();
        }
    }

    private void applySplash(List<MobEffectInstance> p_37548_, @Nullable Entity p_37549_) {
        AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
        if (!list.isEmpty()) {
            Entity entity = this.getEffectSource();

            for(LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double d0 = this.distanceToSqr(livingentity);
                    if (d0 < 16.0D) {
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                        if (livingentity == p_37549_) {
                            d1 = 1.0D;
                        }

                        for(MobEffectInstance mobeffectinstance : p_37548_) {
                            MobEffect mobeffect = mobeffectinstance.getEffect();
                            if (mobeffect.isInstantenous()) {
                                mobeffect.applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectinstance.getAmplifier(), d1);
                            } else {
                                int i = (int)(d1 * (double)mobeffectinstance.getDuration() + 0.5D);
                                if (i > 20) {
                                    livingentity.addEffect(new MobEffectInstance(mobeffect, i, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), entity);
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
