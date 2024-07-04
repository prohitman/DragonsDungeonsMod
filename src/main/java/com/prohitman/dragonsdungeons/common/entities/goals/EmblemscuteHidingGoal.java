package com.prohitman.dragonsdungeons.common.entities.goals;

import com.google.common.collect.Lists;
import com.prohitman.dragonsdungeons.common.entities.EmblemscuteEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class EmblemscuteHidingGoal extends Goal {
    private final EmblemscuteEntity entity;
    private Player player;

    public EmblemscuteHidingGoal(EmblemscuteEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        List<LivingEntity> mobs = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(7, 2, 7));
        for(LivingEntity entityIn : mobs){
            if(entityIn instanceof Player){
                double d0 = this.entity.distanceToSqr(entityIn.getX(), entityIn.getY(),
                        entityIn.getZ());
                if((!entityIn.isDiscrete() || (d0 <= 4 && entityIn.isDiscrete())) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entityIn)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        float radius2 = 2.0f;
        double speed = 0.25;
        /*for (float a = 0; a < Math.PI * 2; a += Math.PI / 180) {
            float x = (float) (Math.cos(a) * radius2);
            float z = (float) (Math.sin(a) * radius2);

            double particleXSpeed = Math.cos(a) * speed;
            double particleZSpeed = Math.sin(a) * speed;

            double d0 = (double) entity.blockPosition().getX() + x;
            double d1 = entity.blockPosition().getY();
            double d2 = (double) entity.blockPosition().getZ() + z;
            ((ServerLevel)entity.level).sendParticles(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 1, particleXSpeed, 0.0D, particleZSpeed, 0.5);
        }*/
        float radius = 2.0F;
        for (float x = -radius; x < radius; x += 0.4f) {//0.25
            for (float y = -radius; y < radius; y += 0.4f) {
                for (float z = -radius; z < radius; z += 0.4f) {//0.25
                    if (x * x + z * z + y * y < radius * radius) {
                        double d0 = (double) this.entity.getX() + x + (float) (this.entity.getRandom().nextInt(9) / 5);
                        double d1 = (double) this.entity.getY() + y + (float) (this.entity.getRandom().nextInt(9) / 5);//this.entity.getY();
                        double d2 = (double) this.entity.getZ() + z + (float) (this.entity.getRandom().nextInt(9) / 5);
                        //this.entity.level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                        //levelIn.addParticle(ParticleTypes.ASH, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                        //this.entity.level.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                        ((ServerLevel)entity.level()).sendParticles(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.5);

                    }
                }
            }
        }
        entity.setHiding(true);
        entity.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, 5.0f, 1.0f);
    }

    @Override
    public void stop() {
        entity.setHiding(false);
    }

    @Override
    public void tick() {
        super.tick();

        entity.hidinganimationTimer = 1;

        List<MobEffectInstance> effects = Lists.newArrayList();
        List<LivingEntity> mobs = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(15, 5, 15));

        effects.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 500, 1));
        effects.add(new MobEffectInstance(MobEffects.ABSORPTION, 500, 1));
        effects.add(new MobEffectInstance(MobEffects.GLOWING, 1000, 1));
        effects.add(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1000, 2));

        for(LivingEntity entityIn : mobs){
            if(entityIn instanceof Player){
                entityIn = player;
            }
            if(entityIn instanceof Mob){
                if((entityIn instanceof Enemy || ((Mob) entityIn).isAggressive()) && entity.getHiding()){
                    if (entityIn.isAffectedByPotions()) {
                        for(MobEffectInstance effect : effects){
                            MobEffect mobeffect = effect.getEffect();
                            if (mobeffect.isInstantenous()) {
                                mobeffect.applyInstantenousEffect(entity, null, entityIn, effect.getAmplifier(), 1);
                            } else {
                                int i = (int)((double)effect.getDuration() + 0.5D);
                                if (i > 20) {
                                    entityIn.addEffect(new MobEffectInstance(mobeffect, i, effect.getAmplifier(), effect.isAmbient(), effect.isVisible()), entityIn);
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
