package com.prohitman.dragonsdungeons.common.blocks.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.prohitman.dragonsdungeons.client.screen.menu.FoundryMenu;
import com.prohitman.dragonsdungeons.common.blocks.obj.FoundryBlock;
import com.prohitman.dragonsdungeons.core.init.ModBlockEntities;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.SmithingTableBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class FoundryBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
    //protected static final int SLOT_INPUT = 0;
    //protected static final int SLOT_FUEL = 1;
    //protected static final int SLOT_RESULT = 2;
    public static final int DATA_LIT_TIME = 0;
    private static final int[] SLOTS_FOR_UP = new int[]{0, 1, 2};
    private static final int[] SLOTS_FOR_DOWN = new int[]{3, 4};
    private static final int[] SLOTS_FOR_SIDES = new int[]{4};
    public static final int DATA_LIT_DURATION = 1;
    public static final int DATA_COOKING_PROGRESS = 2;
    public static final int DATA_COOKING_TOTAL_TIME = 3;
    public static final int NUM_DATA_VALUES = 4;
    public static final int BURN_TIME_STANDARD = 200;
    public static final int BURN_COOL_SPEED = 2;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    protected NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);

    int burningInputIndex;
    int litTime;
    int litDuration;
    int cookingProgress;
    int cookingTotalTime;
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return FoundryBlockEntity.this.litTime;
                case 1:
                    return FoundryBlockEntity.this.litDuration;
                case 2:
                    return FoundryBlockEntity.this.cookingProgress;
                case 3:
                    return FoundryBlockEntity.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    FoundryBlockEntity.this.litTime = value;
                    break;
                case 1:
                    FoundryBlockEntity.this.litDuration = value;
                    break;
                case 2:
                    FoundryBlockEntity.this.cookingProgress = value;
                    break;
                case 3:
                    FoundryBlockEntity.this.cookingTotalTime = value;
            }

        }

        public int getCount() {
            return 4;
        }
    };
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

    public FoundryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FOUNDRY_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.recipeType = RecipeType.BLASTING;
        this.quickCheck = RecipeManager.createCheck((RecipeType)this.recipeType);

    }
    protected Component getDefaultName() {
        return Component.translatable("container.furnace");
    }

    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new FoundryMenu(pId, pPlayer, this, this.dataAccess);
    }


    private static boolean isNeverAFurnaceFuel(Item pItem) {
        return pItem.builtInRegistryHolder().is(ItemTags.NON_FLAMMABLE_WOOD);
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items);
        this.litTime = pTag.getInt("BurnTime");
        this.cookingProgress = pTag.getInt("CookTime");
        this.cookingTotalTime = pTag.getInt("CookTimeTotal");
        this.litDuration = this.getBurnDuration(this.items.get(4));
        this.burningInputIndex = pTag.getInt("BurningInputIndex");
        CompoundTag compoundtag = pTag.getCompound("RecipesUsed");

        for(String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
        }

    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("BurnTime", this.litTime);
        pTag.putInt("CookTime", this.cookingProgress);
        pTag.putInt("CookTimeTotal", this.cookingTotalTime);
        pTag.putInt("BurningInputIndex", this.burningInputIndex);
        ContainerHelper.saveAllItems(pTag, this.items);
        CompoundTag compoundtag = new CompoundTag();
        this.recipesUsed.forEach((p_187449_, p_187450_) -> {
            compoundtag.putInt(p_187449_.toString(), p_187450_);
        });
        pTag.put("RecipesUsed", compoundtag);
    }
//itemstack -> input
//flag2
//protected static final int SLOT_INPUT = 0;
//protected static final int SLOT_FUEL = 1;
//protected static final int SLOT_RESULT = 2;
//input = 0,1,2
//Output = 3
//Fuel = 4
    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, FoundryBlockEntity pBlockEntity) {
        boolean flag = pBlockEntity.isLit();
        boolean flag1 = false;
        if (pBlockEntity.isLit()) {
            --pBlockEntity.litTime;
        }
        int input;
        for(input=0; input<3; input++){
            ItemStack itemstack = pBlockEntity.items.get(4);
            boolean flag2 = !pBlockEntity.items.get(input).isEmpty();
            boolean flag3 = !itemstack.isEmpty();
            if (pBlockEntity.isLit() || flag3 && flag2) {
                Recipe<?> recipe;
                if (flag2) {
                    recipe = pBlockEntity.quickCheck.getRecipeFor(pBlockEntity, pLevel).orElse(null);
                } else {
                    recipe = null;
                }

                int i = pBlockEntity.getMaxStackSize();
                if (!pBlockEntity.isLit() && pBlockEntity.canBurnInSlot(pLevel.registryAccess(), recipe, pBlockEntity.items, input, i)) {
                    pBlockEntity.litTime = pBlockEntity.getBurnDuration(itemstack);
                    pBlockEntity.litDuration = pBlockEntity.litTime;
                    pBlockEntity.burningInputIndex = input;
                    if (pBlockEntity.isLit()) {
                        flag1 = true;
                        if (itemstack.hasCraftingRemainingItem())
                            pBlockEntity.items.set(4, itemstack.getCraftingRemainingItem());
                        else if (flag3) {
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                pBlockEntity.items.set(4, itemstack.getCraftingRemainingItem());
                            }
                        }
                    }
                }

                if(pBlockEntity.burningInputIndex == input){
                    if (pBlockEntity.isLit() && pBlockEntity.canBurnInSlot(pLevel.registryAccess(), recipe, pBlockEntity.items, input, i)) {
                        ++pBlockEntity.cookingProgress;
                        if (pBlockEntity.cookingProgress == pBlockEntity.cookingTotalTime) {
                            pBlockEntity.cookingProgress = 0;
                            pBlockEntity.cookingTotalTime = getTotalCookTime(pLevel, pBlockEntity);
                            if (pBlockEntity.burnInSlot(pLevel.registryAccess(), recipe, pBlockEntity.items, input, i)) {
                                pBlockEntity.setRecipeUsed(recipe);
                            }

                            flag1 = true;
                        }
                    } else {
                        pBlockEntity.cookingProgress = 0;
                    }
                }

            } else if (!pBlockEntity.isLit() && pBlockEntity.cookingProgress > 0) {
                pBlockEntity.cookingProgress = Mth.clamp(pBlockEntity.cookingProgress - 2, 0, pBlockEntity.cookingTotalTime);
            }

            if (flag != pBlockEntity.isLit()) {
                flag1 = true;
                pState = pState.setValue(FoundryBlock.LIT, Boolean.valueOf(pBlockEntity.isLit()));
                pLevel.setBlock(pPos, pState, 3);
            }

            if (flag1 && pBlockEntity.burningInputIndex == input) {
                setChanged(pLevel, pPos, pState);
            }
        }
    }

    private boolean canBurnInSlot(RegistryAccess pRegistryAccess, @Nullable Recipe<?> pRecipe, NonNullList<ItemStack> pInventory, int slot, int pMaxStackSize) {
        if (!pInventory.get(slot).isEmpty() && pRecipe != null) {
            ItemStack itemstack = ((Recipe<WorldlyContainer>) pRecipe).assemble(this, pRegistryAccess);
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = pInventory.get(3);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(itemstack1, itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= pMaxStackSize && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    private boolean burnInSlot(RegistryAccess pRegistryAccess, @Nullable Recipe<?> pRecipe, NonNullList<ItemStack> pInventory, int slot, int pMaxStackSize) {
        if (pRecipe != null && this.canBurn(pRegistryAccess, pRecipe, pInventory, pMaxStackSize)) {
            ItemStack itemstack = pInventory.get(slot);
            ItemStack itemstack1 = ((Recipe<WorldlyContainer>) pRecipe).assemble(this, pRegistryAccess);
            ItemStack itemstack2 = pInventory.get(3);
            if (itemstack2.isEmpty()) {
                pInventory.set(3, itemstack1.copy());
            } else if (itemstack2.is(itemstack1.getItem())) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (itemstack.is(Blocks.WET_SPONGE.asItem()) && !pInventory.get(4).isEmpty() && pInventory.get(4).is(Items.BUCKET)) {
                pInventory.set(4, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    private boolean canBurn(RegistryAccess pRegistryAccess, @Nullable Recipe<?> pRecipe, NonNullList<ItemStack> pInventory, int pMaxStackSize) {
        if (!pInventory.get(0).isEmpty() && pRecipe != null) {
            ItemStack itemstack = ((Recipe<WorldlyContainer>) pRecipe).assemble(this, pRegistryAccess);
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = pInventory.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(itemstack1, itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= pMaxStackSize && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    private boolean burn(RegistryAccess pRegistryAccess, @Nullable Recipe<?> pRecipe, NonNullList<ItemStack> pInventory, int pMaxStackSize) {
        if (pRecipe != null && this.canBurn(pRegistryAccess, pRecipe, pInventory, pMaxStackSize)) {
            ItemStack itemstack = pInventory.get(0);
            ItemStack itemstack1 = ((Recipe<WorldlyContainer>) pRecipe).assemble(this, pRegistryAccess);
            ItemStack itemstack2 = pInventory.get(2);
            if (itemstack2.isEmpty()) {
                pInventory.set(2, itemstack1.copy());
            } else if (itemstack2.is(itemstack1.getItem())) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (itemstack.is(Blocks.WET_SPONGE.asItem()) && !pInventory.get(1).isEmpty() && pInventory.get(1).is(Items.BUCKET)) {
                pInventory.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    protected int getBurnDuration(ItemStack pFuel) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            Item item = pFuel.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(pFuel, this.recipeType);
        }
    }

    private static int getTotalCookTime(Level pLevel, FoundryBlockEntity pBlockEntity) {
        return pBlockEntity.quickCheck.getRecipeFor(pBlockEntity, pLevel).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

    public static boolean isFuel(ItemStack pStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(pStack, null) > 0;
    }

    public int[] getSlotsForFace(Direction pSide) {
        if (pSide == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return pSide == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    /**
     * Returns {@code true} if automation can insert the given item in the given slot from the given side.
     */
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return this.canPlaceItem(pIndex, pItemStack);
    }

    /**
     * Returns {@code true} if automation can extract the given item in the given slot from the given side.
     */
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        if (pDirection == Direction.DOWN && pIndex == 4) {
            return pStack.is(Items.WATER_BUCKET) || pStack.is(Items.BUCKET);
        } else {
            return true;
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getContainerSize() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getItem(int pIndex) {
        return this.items.get(pIndex);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack removeItem(int pIndex, int pCount) {
        return ContainerHelper.removeItem(this.items, pIndex, pCount);
    }

    /**
     * Removes a stack from the given slot and returns it.
    */
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(this.items, pIndex);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
            */
    public void setItem(int pIndex, ItemStack pStack) {
        ItemStack itemstack = this.items.get(pIndex);
        boolean flag = !pStack.isEmpty() && ItemStack.isSameItemSameTags(itemstack, pStack);
        this.items.set(pIndex, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }

        if ((pIndex == 0 || pIndex == 1 || pIndex == 2) && !flag) {
            this.cookingTotalTime = getTotalCookTime(this.level, this);
            this.cookingProgress = 0;
            this.setChanged();
        }
    }

    /**
      Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    /**
     * Returns {@code true} if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     * For guis use Slot.isItemValid
     */
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        if (pIndex == 3) {
            return false;
        } else if (pIndex != 4) {
            return true;
        } else {
            ItemStack itemstack = this.items.get(4);
            return net.minecraftforge.common.ForgeHooks.getBurnTime(pStack, this.recipeType) > 0 || pStack.is(Items.BUCKET) && !itemstack.is(Items.BUCKET);
        }
    }

    public void clearContent() {
        this.items.clear();
    }

    public void setRecipeUsed(@Nullable Recipe<?> pRecipe) {
        if (pRecipe != null) {
            ResourceLocation resourcelocation = pRecipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }

    }

    @Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipes(Player pPlayer, List<ItemStack> pItems) {
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer pPlayer) {
        List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(pPlayer.serverLevel(), pPlayer.position());
        pPlayer.awardRecipes(list);

        for(Recipe<?> recipe : list) {
            if (recipe != null) {
                pPlayer.triggerRecipeCrafted(recipe, this.items);
            }
        }

        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pPopVec) {
        List<Recipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            pLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((p_155023_) -> {
                list.add(p_155023_);
                createExperience(pLevel, pPopVec, entry.getIntValue(), ((AbstractCookingRecipe)p_155023_).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(ServerLevel pLevel, Vec3 pPopVec, int pRecipeIndex, float pExperience) {
        int i = Mth.floor((float)pRecipeIndex * pExperience);
        float f = Mth.frac((float)pRecipeIndex * pExperience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        ExperienceOrb.award(pLevel, pPopVec, i);
    }

    public void fillStackedContents(StackedContents pHelper) {
        for(ItemStack itemstack : this.items) {
            pHelper.accountStack(itemstack);
        }

    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.handlers = net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    }
}
