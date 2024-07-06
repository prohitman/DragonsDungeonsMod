package com.prohitman.dragonsdungeons.common.entities.goals.automaton;

import com.prohitman.dragonsdungeons.common.entities.AutomatonEntity;
import com.prohitman.dragonsdungeons.common.entities.goals.AnimatedMeleeAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public class AutomatonMeleeGoal extends AnimatedMeleeAttackGoal<AutomatonEntity> {
    public AutomatonMeleeGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, int attackDelay, int attackDuration, double attackReach) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen, attackDelay, attackDuration, attackReach);
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.mob.getTarget();
        return super.canUse() && this.mob.distanceToSqr(livingEntity) < 7;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingEntity = this.mob.getTarget();
        return super.canContinueToUse() && this.mob.distanceToSqr(livingEntity) < 7;
    }


    @Override
    public void stop() {
        super.stop();
        this.mob.setAggressive(true);
    }
}
