package com.prohitman.dragonsdungeons.common.items;

import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import com.prohitman.dragonsdungeons.core.init.ModItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModItemTiers implements Tier {
    STEEL(2, 800, 6.0F, 2.5F, 12, () -> {
        return Ingredient.of(ModItems.STEEL_INGOT.get());
    }),
    MITHRIL(4, 1800, 9.0F, 3F, 12, () -> {
        return Ingredient.of(ModItems.MITHRIL_INGOT.get());
    }),
    ELVEN_BRASS(3, 910, 8.0F, 4.0F, 14, () -> {
        return Ingredient.of(ModItems.ELVEN_BRASS_INGOT.get());
    }),
    DRAGON_BONE(6, 3000, 10.0F, 6.0F, 16, () -> {
        return Ingredient.of(ModItems.DRAGON_BONE.get());
    }),
    DWARVEN_STEEL(3, 1400, 8.0F, 7F, 15, () -> {
        return Ingredient.of(ModItems.DWARVEN_STEEL_INGOT.get());
    }),
    GILDED_IRON(2, 275, 6.0F, 2.0F, 20, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    });


    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairMaterial;

    ModItemTiers(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = repairMaterialIn;
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}
