package com.prohitman.dragonsdungeons.common.entities.goals.automaton;

import com.prohitman.dragonsdungeons.common.entities.AutomatonEntity;
import com.prohitman.dragonsdungeons.common.entities.goals.AnimatedRangedAttackGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class AutomatonRangedGoal extends AnimatedRangedAttackGoal<AutomatonEntity> {

    public AutomatonRangedGoal(AutomatonEntity pMob, double pSpeedModifier, int pAttackIntervalMin, float pAttackRadius, int attackDelay, int attackDuration) {
        super(pMob, pSpeedModifier, pAttackIntervalMin, pAttackRadius, attackDelay, attackDuration);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingEntity = this.entity.getTarget();
        return super.canContinueToUse() && this.entity.distanceToSqr(livingEntity) >= 7;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.entity.getTarget();
        return super.canUse() && this.entity.distanceToSqr(livingEntity) >= 7;
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAggressive(true);
        this.entity.setZza(0);
        this.entity.getNavigation().stop();
        this.entity.setDeltaMovement(Vec3.ZERO);
    }

    /*@Override
    public void tick() {
        LivingEntity livingentity = entity.getTarget();
        if (livingentity != null) {
            double d0 = entity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean flag = entity.getSensing().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                this.entity.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.entity.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)entity.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)entity.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > (double)(this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double)(this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }

                entity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                Entity entity = this.entity.getControlledVehicle();
                if (entity instanceof Mob) {
                    Mob mob = (Mob)entity;
                    mob.lookAt(livingentity, 30.0F, 30.0F);
                }

                this.entity.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.entity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (isEnemyWithinAttackDistance(d0)*//* && this.seeTime >= -60*//*) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    entity.setShooting(true);
                }

                if(isTimeToAttack()) {
                    this.entity.getLookControl().setLookAt(entity.getTarget().getX(), entity.getTarget().getEyeY(), entity.getTarget().getZ());
                    performAttack();
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setShooting(false);
                entity.setShootAnimationTimeOut(0);
            }
        }
        if(shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }*/

}
