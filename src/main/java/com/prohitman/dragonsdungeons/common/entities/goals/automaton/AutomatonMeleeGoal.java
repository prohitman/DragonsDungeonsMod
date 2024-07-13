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
        if(this.mob.getTarget() != null){
            LivingEntity livingEntity = this.mob.getTarget();

            if(this.mob.distanceToSqr(livingEntity) >= 8){
                return false;
            }
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if(this.mob.getTarget() != null){
            LivingEntity livingEntity = this.mob.getTarget();

            if(this.mob.distanceToSqr(livingEntity) >= 8){
                return false;
            }
        }
        return super.canContinueToUse();
    }
}
