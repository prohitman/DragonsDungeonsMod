package com.prohitman.dragonsdungeons.core.datagen.server;

import com.prohitman.dragonsdungeons.core.datagen.server.builder.AlloyingRecipeBuilder;
import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import com.prohitman.dragonsdungeons.core.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class ModRecipes extends RecipeProvider {

    public ModRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        this.alloying(ModItems.RAW_ADAMANTITE.get(), ModItems.STEEL_INGOT.get(), ModItems.DWARVEN_STEEL_INGOT.get(), RecipeCategory.MISC, consumer);
        this.alloying(ModBlocks.MITHRIL_CRYSTAL.get().asItem(), ModItems.STEEL_INGOT.get(), ModItems.MITHRIL_INGOT.get(), RecipeCategory.MISC, consumer);
        this.alloying(Items.IRON_INGOT, Items.COAL, ModItems.STEEL_INGOT.get(), RecipeCategory.MISC, consumer);

        createFullBlock(ModItems.STEEL_INGOT.get(), ModBlocks.STEEL_BLOCK.get(), consumer);
        createFullBlock(ModItems.DWARVEN_STEEL_INGOT.get(), ModBlocks.DWARVEN_STEEL_BLOCK.get(), consumer);
        createFullBlock(ModItems.MITHRIL_INGOT.get(), ModBlocks.MITHRIL_BLOCK.get(), consumer);
        createFullBlock(ModItems.ELVEN_BRASS_INGOT.get(), ModBlocks.ELVEN_BRASS_BLOCK.get(), consumer);
        createFullBlock(ModItems.RAW_ADAMANTITE.get(), ModBlocks.RAW_ADAMANTITE_BLOCK.get(), consumer);

        createBlockRecipes(ModBlocks.LAVAROCK.get(), true, consumer);

        createToolSet(ModItems.STEEL_INGOT.get(), consumer);
        createToolSet(ModItems.DWARVEN_STEEL_INGOT.get(), consumer);
        createToolSet(ModItems.MITHRIL_INGOT.get(), consumer);
        createToolSet(ModItems.ELVEN_BRASS_INGOT.get(), consumer);
        createToolSet(ModItems.DRAGON_BONE.get(), consumer);
    }

    public void createBlockRecipes(Block block, boolean withWalls, Consumer<FinishedRecipe> consumer){
        stairBuilder(ModBlocks.getStairFromBlock(block).asItem(), Ingredient.of(block))
                .unlockedBy(getHasName(block), has(block))
                .save(consumer);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.getSlabFromBlock(block).asItem(), Ingredient.of(block))
                .unlockedBy(getHasName(block), has(block))
                .save(consumer);
        if(withWalls){
            wallBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.getWallFromBlock(block).asItem(), Ingredient.of(block))
                    .unlockedBy(getHasName(block), has(block))
                    .save(consumer);
        }
    }

    public void alloying(Item first_ingredient, Item second_ingredient, Item result, RecipeCategory category, Consumer<FinishedRecipe> consumer){
        AlloyingRecipeBuilder.alloying(
                        Ingredient.of(first_ingredient),
                        Ingredient.of(second_ingredient), category,
                        result)
                .unlocks("has_" + getItemName(first_ingredient), has(first_ingredient))
                .save(consumer, getItemName(result) + "_alloying");
    }

    public void createFullBlock(Item material, Block block, Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, block).define('m', material)
                .pattern("mmm")
                .pattern("mmm")
                .pattern("mmm")
                .unlockedBy("has_" + material.getDescriptionId(), has(material))
                .save(consumer);
    }

    public void createArmorSet(){

    }

    public void createToolSet(Item material, Consumer<FinishedRecipe> consumer){
        createToolSet(material,
                ModItems.getSwordFromMaterial(material),
                ModItems.getShovelFromMaterial(material),
                ModItems.getAxeFromMaterial(material),
                ModItems.getHoeFromMaterial(material),
                consumer);
    }

    public void createToolSet(Item material, Item sword, Item shovel, Item axe, Item hoe, Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, sword).define('m', material).define('s', Items.STICK)
                .pattern("m")
                .pattern("m")
                .pattern("s")
                .unlockedBy("has_" + material.getDescriptionId(), has(material))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel).define('m', material).define('s', Items.STICK)
                .pattern("m")
                .pattern("s")
                .pattern("s")
                .unlockedBy("has_" + material.getDescriptionId(), has(material))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe).define('m', material).define('s', Items.STICK)
                .pattern("mm")
                .pattern("sm")
                .pattern("s ")
                .unlockedBy("has_" + material.getDescriptionId(), has(material))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe).define('m', material).define('s', Items.STICK)
                .pattern("mm")
                .pattern("s ")
                .pattern("s ")
                .unlockedBy("has_" + material.getDescriptionId(), has(material))
                .save(consumer);
    }

}
