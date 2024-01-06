package com.prohitman.dragonsdungeons.core.init;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedList;
import java.util.List;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DragonsDungeons.MODID);

    public static RegistryObject<CreativeModeTab> DD_TAB = CREATIVE_MODE_TABS.register("dd_tab", () ->
            CreativeModeTab.builder().icon(() -> ModBlocks.MITHRIL_CRYSTAL.get().asItem().getDefaultInstance())
                    .title(Component.translatable("itemGroup.dragonsdungeons"))
                    .displayItems((featureFlags, output) -> {
                        output.acceptAll(getTabItems());
                    }).build());

    private static List<ItemStack> getTabItems(){
        List<ItemStack> list = new LinkedList<>();
        list.addAll(ModItems.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter((item) -> (!(item instanceof BlockItem)
                        || item instanceof SignItem))
                .map(Item::getDefaultInstance).toList());

        list.addAll(ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)
                .filter((block) -> !(block instanceof StandingSignBlock)
                        && !(block instanceof WallSignBlock)
                        && !(block instanceof WallHangingSignBlock)
                        && !(block instanceof CeilingHangingSignBlock))
                .map(Block::asItem).map(Item::getDefaultInstance).toList());

        return list;
    }
}
