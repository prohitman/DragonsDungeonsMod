package com.prohitman.dragonsdungeons.core.datagen.server.builder;

import com.google.gson.JsonObject;
import com.prohitman.dragonsdungeons.core.init.ModRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class AlloyingRecipeBuilder {
    private final Ingredient first_ingredient;
    private final Ingredient second_ingredient;
    private final RecipeCategory category;
    private final Item result;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private final RecipeSerializer<?> type;

    public AlloyingRecipeBuilder(RecipeSerializer<?> pType, Ingredient first_ingredient, Ingredient second_ingredient, RecipeCategory pCategory, Item pResult) {
        this.category = pCategory;
        this.type = pType;
        this.first_ingredient = first_ingredient;
        this.second_ingredient = second_ingredient;
        this.result = pResult;
    }

    public static AlloyingRecipeBuilder alloying(Ingredient first_ingredient, Ingredient second_ingredient, RecipeCategory pCategory, Item pResult) {
        return new AlloyingRecipeBuilder(ModRecipeSerializers.ALLOYING_SERIALIZER.get(), first_ingredient, second_ingredient, pCategory, pResult);
    }

    public AlloyingRecipeBuilder unlocks(String pKey, CriterionTriggerInstance pCriterion) {
        this.advancement.addCriterion(pKey, pCriterion);
        return this;
    }

    public void save(Consumer<FinishedRecipe> pRecipeConsumer, String pLocation) {
        this.save(pRecipeConsumer, new ResourceLocation(pLocation));
    }

    public void save(Consumer<FinishedRecipe> pRecipeConsumer, ResourceLocation pLocation) {
        this.ensureValid(pLocation);
        this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pLocation)).rewards(AdvancementRewards.Builder.recipe(pLocation)).requirements(RequirementsStrategy.OR);
        pRecipeConsumer.accept(new AlloyingRecipeBuilder.Result(pLocation, this.type, this.first_ingredient, this.second_ingredient, this.result, this.advancement, pLocation.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation pLocation) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pLocation);
        }
    }

    public static record Result(ResourceLocation id, RecipeSerializer<?> type, Ingredient first_ingredient, Ingredient second_ingredient, Item result, Advancement.Builder advancement, ResourceLocation advancementId) implements FinishedRecipe {
        public void serializeRecipeData(JsonObject p_266713_) {
            p_266713_.add("first_ingredient", this.first_ingredient.toJson());
            p_266713_.add("second_ingredient", this.second_ingredient.toJson());
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            p_266713_.add("result", jsonobject);
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return this.type;
        }

        /**
         * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
         */
        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
