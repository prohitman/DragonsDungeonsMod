package com.prohitman.dragonsdungeons.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import java.util.List;
import java.util.Objects;

public class LootBag extends Item {
    private boolean isMageLoot;
    public LootBag(Properties pProperties, boolean isMageLoot) {
        super(pProperties);
        this.isMageLoot = isMageLoot;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.NEUTRAL, 1.0F, 1.0F);
        if(!worldIn.isClientSide()){
            ItemStack itemstackIn = playerIn.getItemInHand(handIn);

            List<ItemStack> list = this.generateLoot((ServerLevel) worldIn, playerIn.blockPosition(), playerIn);

            for(ItemStack itemstack : list) {
                ItemEntity itementity = new ItemEntity(playerIn.level(), playerIn.getX(), playerIn.getY(), playerIn.getZ(), itemstack);
                worldIn.addFreshEntity(itementity);
                playerIn.level().addFreshEntity(new ExperienceOrb(playerIn.level(), playerIn.getX(), playerIn.getY() + 0.5D, playerIn.getZ() + 0.5D, playerIn.level().random.nextInt(6) + 1));

            }
            playerIn.awardStat(Stats.ITEM_USED.get(this));
            if (!playerIn.isCreative()) {
                itemstackIn.shrink(1);
            }
            return super.use(worldIn, playerIn, handIn);
        }
        return super.use(worldIn, playerIn, handIn);
    }

    private List<ItemStack> generateLoot(ServerLevel level, BlockPos pos, Entity entity){
        ResourceLocation lootTableLocation = new ResourceLocation("minecraft", "chests/ancient_city");
        LootTable lootTable = level.getServer().getLootData().getLootTable(lootTableLocation);

        LootParams lootparams = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).create(LootContextParamSets.GIFT);

        return lootTable.getRandomItems(lootparams);
    }
}
