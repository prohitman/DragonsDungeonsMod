package com.prohitman.dragonsdungeons.common.entities.goals;

import com.prohitman.dragonsdungeons.common.entities.WargEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class LeaderFightGoal extends NearestAttackableTargetGoal<WargEntity> {
    private final WargEntity tamableMob;

    public LeaderFightGoal(WargEntity pTamableMob, Class<WargEntity> pTargetType, boolean pMustSee, @Nullable Predicate<LivingEntity> pTargetPredicate) {
        super(pTamableMob, pTargetType, 10, pMustSee, false, pTargetPredicate);
        this.tamableMob = pTamableMob;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        return !this.tamableMob.isTame() && this.tamableMob.isLeader() && super.canUse();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.targetConditions != null ? this.targetConditions.test(this.mob, this.target) && this.tamableMob.isLeader() : super.canContinueToUse() && this.tamableMob.isLeader();
    }
}
