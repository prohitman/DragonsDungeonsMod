package com.prohitman.dragonsdungeons.common.items.weapons;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.Vec3;

public class BeardedAxe extends AxeItem {
    public BeardedAxe(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player attacker, Entity target) {
        Vec3 vector3d = (new Vec3(attacker.getX() - target.getX(), attacker.getY() - target.getY(), attacker.getZ() - target.getZ())).scale(0.7D);
        target.setDeltaMovement(target.getDeltaMovement().add(vector3d));
        return super.onLeftClickEntity(stack, attacker, target);
    }
}
