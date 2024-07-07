package com.prohitman.dragonsdungeons.client.screen;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.screen.menu.FoundryBEMenu;
import com.prohitman.dragonsdungeons.client.screen.menu.FoundryMenu;
import com.prohitman.dragonsdungeons.client.screen.menu.TreasureChestMenu;
import com.prohitman.dragonsdungeons.client.screen.menu.UrnMenu;
import com.prohitman.dragonsdungeons.common.blocks.entity.FoundryBE;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, DragonsDungeons.MODID);

    public static final RegistryObject<MenuType<TreasureChestMenu>> TREASURE_CHEST_MENU =
            registerMenuType("treasure_chest_menu", TreasureChestMenu::new);
    public static final RegistryObject<MenuType<UrnMenu>> URN_MENU =
            registerMenuType("urn_menu", UrnMenu::new);

/*    public static final RegistryObject<MenuType<FoundryMenu>> FOUNDRY_MENU =
            registerMenuType("foundry_menu", FoundryMenu::new);*/

    public static final RegistryObject<MenuType<FoundryBEMenu>> FOUNDRY_MENU =
            registerMenuType("foundry_menu", FoundryBEMenu::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
}
