package com.prohitman.dragonsdungeons.common.blocks.entity;

import com.prohitman.dragonsdungeons.client.screen.menu.FoundryMenu;
import com.prohitman.dragonsdungeons.common.blocks.obj.FoundryBlock;
import com.prohitman.dragonsdungeons.common.recipes.AlloyingRecipe;
import com.prohitman.dragonsdungeons.core.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class FoundryBE extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4);

    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int FUEL_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public FoundryBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FOUNDRY_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> FoundryBE.this.progress;
                    case 1 -> FoundryBE.this.maxProgress;
                    case 2 -> FoundryBE.this.fuelTime;
                    case 3 -> FoundryBE.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> FoundryBE.this.progress = pValue;
                    case 1 -> FoundryBE.this.maxProgress = pValue;
                    case 2 -> FoundryBE.this.fuelTime = pValue;
                    case 3 -> FoundryBE.this.maxFuelTime = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.dragonsdungeons.foundry");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FoundryMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("foundry.progress", progress);
        pTag.putInt("foundry.fuelTime", fuelTime);
        pTag.putInt("foundry.maxFuelTime", maxFuelTime);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("foundry.progress");
        fuelTime = pTag.getInt("foundry.fuelTime");
        maxFuelTime = pTag.getInt("foundry.maxFuelTime");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (isBurning()) {
            if(!pLevel.isClientSide){
                pState = pState.setValue(FoundryBlock.LIT, true);
                pLevel.setBlock(pPos, pState, 3);
            }

            fuelTime--;
        }
        else {
            if(!pLevel.isClientSide){
                pState = pState.setValue(FoundryBlock.LIT, false);
                pLevel.setBlock(pPos, pState, 3);
            }
        }

        if(hasFuelInFuelSlot() && hasRecipe() && !isBurning()){
            startBurning();
        }

        if (hasRecipe() && isBurning()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private boolean hasFuelInFuelSlot() {
        return !this.itemHandler.getStackInSlot(FUEL_SLOT).isEmpty();
    }

    private boolean isBurning() {
        return this.fuelTime > 0;
    }

    private void startBurning() {
        ItemStack fuelStack = this.itemHandler.getStackInSlot(FUEL_SLOT);
        this.maxFuelTime = ForgeHooks.getBurnTime(fuelStack, RecipeType.SMELTING);
        this.fuelTime = this.maxFuelTime;

        if (fuelStack.hasCraftingRemainingItem()) {
            this.itemHandler.setStackInSlot(FUEL_SLOT, fuelStack.getCraftingRemainingItem());
        } else {
            fuelStack.shrink(1);
            if (fuelStack.isEmpty()) {
                this.itemHandler.setStackInSlot(FUEL_SLOT, ItemStack.EMPTY);
            }
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        Optional<AlloyingRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);

        this.itemHandler.extractItem(INPUT_SLOT_1, 1, false);
        this.itemHandler.extractItem(INPUT_SLOT_2, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasRecipe() {
        Optional<AlloyingRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());

        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<AlloyingRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(AlloyingRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }
}
