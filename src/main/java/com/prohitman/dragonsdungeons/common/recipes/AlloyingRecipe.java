package com.prohitman.dragonsdungeons.common.recipes;

import com.google.gson.JsonObject;
import com.prohitman.dragonsdungeons.DragonsDungeons;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AlloyingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;

    public AlloyingRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation id) {
        this.id = id;
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        if(level.isClientSide){
            return false;
        }
        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            inputs.add(inv.getItem(i));
        }
        for (Ingredient ingredient : ingredients) {
            boolean matched = false;
            for (ItemStack input : inputs) {
                if (ingredient.test(input)) {
                    inputs.remove(input);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }
        return inputs.isEmpty();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<AlloyingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "alloying";
    }

    public static class Serializer implements RecipeSerializer<AlloyingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DragonsDungeons.MODID, "alloying");

        @Override
        public AlloyingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);
            ingredients.set(0, Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "first_ingredient")));
            ingredients.set(1, Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "second_ingredient")));

            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            return new AlloyingRecipe(ingredients, result, recipeId);
        }

        @Override
        public @Nullable AlloyingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);

            ingredients.set(0, Ingredient.fromNetwork(buffer));
            ingredients.set(1, Ingredient.fromNetwork(buffer));

            ItemStack result = buffer.readItem();
            return new AlloyingRecipe(ingredients, result, recipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AlloyingRecipe recipe) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItemStack(recipe.getResultItem(null), false);
        }
    }

}
