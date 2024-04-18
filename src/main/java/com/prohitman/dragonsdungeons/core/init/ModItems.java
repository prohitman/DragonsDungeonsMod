package com.prohitman.dragonsdungeons.core.init;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.items.ModItemTiers;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DragonsDungeons.MODID);


    public static final RegistryObject<Item> DWARVEN_STEEL_INGOT = createRegistryWithToolSet("dwarven_steel_ingot", () -> new Item(new Item.Properties()), ModItemTiers.DWARVEN_STEEL);
    public static final RegistryObject<Item> ELVEN_BRASS_INGOT = createRegistryWithToolSet("elven_brass_ingot", () -> new Item(new Item.Properties()), ModItemTiers.ELVEN_BRASS);
    public static final RegistryObject<Item> MITHRIL_INGOT = createRegistryWithToolSet("mithril_ingot", () -> new Item(new Item.Properties()), ModItemTiers.MITHRIL);
    public static final RegistryObject<Item> OLDFORGED_INGOT = ITEMS.register("oldforged_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STEEL_INGOT = createRegistryWithToolSet("steel_ingot", () -> new Item(new Item.Properties()), ModItemTiers.STEEL);
    public static final RegistryObject<Item> DRAGON_BONE = createRegistryWithToolSet("dragon_bone", () -> new Item(new Item.Properties()), ModItemTiers.DRAGON_BONE);
    public static final RegistryObject<Item> RAW_ADAMANTITE = ITEMS.register("raw_adamantite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_COINS = ITEMS.register("gold_coins", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLANK_OBELISK = ITEMS.register("blank_obelisk", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WYVERN_STINGER = ITEMS.register("wyvern_stinger", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WARG_SPAWN_EGG = ITEMS.register("warg_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.WARG, 0x9F9A96, 0xB0AAA7, new Item.Properties()));

    public static <T extends Item> RegistryObject<Item> createRegistryWithToolSet(String name, Supplier<T> item, Tier tier) {
        RegistryObject<Item> object = ITEMS.register(name, item);
        String material = name.replace("_ingot", "");

        RegistryObject<Item> object_sword = ITEMS.register(material+"_sword", () -> new SwordItem(tier, 3, -2.4f, new Item.Properties()));
        RegistryObject<Item> object_axe = ITEMS.register(material+"_axe", () -> new AxeItem(tier, 6, 3, new Item.Properties()));
        RegistryObject<Item> object_pickaxe = ITEMS.register(material+"_pickaxe", () -> new PickaxeItem(tier, 1, -2.8f, new Item.Properties()));
        RegistryObject<Item> object_shovel = ITEMS.register(material+"_shovel", () -> new ShovelItem(tier, 1.5f, -3, new Item.Properties()));
        RegistryObject<Item> object_hoe = ITEMS.register(material+"_hoe", () -> new HoeItem(tier, -2, -1, new Item.Properties()));

        return object;
    }

    /*public static float getAttackDamageModifiers(int index){
        switch (index){
            case 0://Sword
                return 3;
            case 1://Axe
                return 6;
            case 2://Pickaxe
                return 1;
            case 3://Shovel
                return 1.5f;
            case 4://Hoe
            default:
                return -2;
        }
    }

    public static float getAttackSpeedModifiers(int index){
        switch (index){
            case 0://Sword
                return -2.4f;
            case 1://Axe
                return 3;
            case 2://Pickaxe
                return -2.8f;
            case 3://Shovel
                return -3f;
            case 4://Hoe
            default:
                return -1;
        }
    }*/
}
