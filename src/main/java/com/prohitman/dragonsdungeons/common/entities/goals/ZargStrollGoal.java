package com.prohitman.dragonsdungeons.common.entities.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class ZargStrollGoal extends WaterAvoidingRandomStrollGoal {
    public ZargStrollGoal(PathfinderMob pMob, double pSpeedModifier) {
        super(pMob, pSpeedModifier);
    }

    @Override
    public boolean canUse() {
        if(this.mob.isVehicle()){
            return true;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }
}
