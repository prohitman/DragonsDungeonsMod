package com.prohitman.dragonsdungeons.common.items.weapons;

import com.prohitman.dragonsdungeons.common.items.IExtendedReach;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class ReachSword extends SwordItem implements IExtendedReach {
    private float reach;
    public ReachSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, float reach, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.reach = reach;
    }

    @Override
    public float getReach() {
        return this.reach;
    }
}
