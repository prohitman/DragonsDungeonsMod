package com.prohitman.dragonsdungeons.common.entities.goals;

import com.prohitman.dragonsdungeons.common.entities.IAttacking;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.nio.file.Path;

public class AnimatedMeleeAttackGoal<T extends PathfinderMob & IAttacking> extends MeleeAttackGoal {
    private final T entity;
    private int attackDelay;
    private int attackDuration;
    private int ticksUntilNextAttack;
    private double attackReach;
    private boolean shouldCountTillNextAttack = false;

    public AnimatedMeleeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, int attackDelay, int attackDuration, double attackReach) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((T) pMob);
        this.attackDuration = attackDuration;
        this.attackDelay = attackDelay;
        this.ticksUntilNextAttack = attackDuration - attackDelay;
        this.attackReach = attackReach;
    }

    @Override
    public void start() {
        super.start();
        ticksUntilNextAttack = attackDuration - attackDelay;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                entity.setAttacking(true);
            }

            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttacking(false);
            entity.setAttackAnimationTimeOut(0);
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= (this.getAttackReachSqr(pEnemy) + attackReach);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDuration);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= attackDelay;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }


    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
    }

    @Override
    public void tick() {
        super.tick();
        if(shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        super.stop();
    }

/*    @Override
    protected double getAttackReachSqr(LivingEntity pAttackTarget) {
        return super.getAttackReachSqr(pAttackTarget) + attackReach;
    }*/
}
