package com.prohitman.dragonsdungeons.common.items.misc;

import com.prohitman.dragonsdungeons.common.entities.projectiles.FlaskOfFireProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlaskOfFire extends Item {
    public FlaskOfFire(Properties properties) {
        super(properties);
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * {@link #use}.
     */
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.getCooldowns().addCooldown(this, 10);
        worldIn.playSound((Player)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (worldIn.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isClientSide) {
            FlaskOfFireProjectile fireBombEntity = new FlaskOfFireProjectile(worldIn, playerIn);
            fireBombEntity.setItem(itemstack);
            fireBombEntity.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -20.0F, 0.5F, 1.0F);
            worldIn.addFreshEntity(fireBombEntity);
        }

        playerIn.awardStat(Stats.ITEM_USED.get(this));
        if (!playerIn.isCreative()) {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
    }
}
