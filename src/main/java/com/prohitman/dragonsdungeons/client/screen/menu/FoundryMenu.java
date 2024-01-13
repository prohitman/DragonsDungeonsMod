package com.prohitman.dragonsdungeons.client.screen.menu;

import com.prohitman.dragonsdungeons.client.screen.ModMenuTypes;
import com.prohitman.dragonsdungeons.client.screen.slots.FoundryFuelSlot;
import com.prohitman.dragonsdungeons.client.screen.slots.FoundryResultSlot;
import com.prohitman.dragonsdungeons.common.blocks.entity.FoundryBlockEntity;
import com.prohitman.dragonsdungeons.common.blocks.entity.TreasureChestBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class FoundryMenu extends RecipeBookMenu<Container> {
    public static final int INGREDIENT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    public static final int SLOT_COUNT = 3;
    public static final int DATA_COUNT = 4;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;
    private final Container container;
    private final ContainerData data;
    protected final Level level;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookType recipeBookType;
    public final FoundryBlockEntity blockEntity;

    public FoundryMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory, (Container) pPlayerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public FoundryMenu(int pContainerId, Inventory pPlayerInventory, Container pContainer, ContainerData pData) {
        super(ModMenuTypes.FOUNDRY_MENU.get(), pContainerId);
        this.recipeType = RecipeType.BLASTING;
        this.recipeBookType = RecipeBookType.BLAST_FURNACE;
        checkContainerSize(pContainer, 5);
        checkContainerDataCount(pData, 4);
        this.container = pContainer;
        this.data = pData;
        this.level = pPlayerInventory.player.level();
        blockEntity = ((FoundryBlockEntity) pContainer);

        addPlayerInventory(pPlayerInventory);
        addPlayerHotbar(pPlayerInventory);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            for (int i = 0; i < 3; ++i) {
                this.addSlot(new SlotItemHandler(iItemHandler, i, 44+i * 18, 27));
            }

            this.addSlot(new FoundryFuelSlot(this, pContainer, 4, 80, 75));
            this.addSlot(new FoundryResultSlot(pPlayerInventory.player, pContainer, 3, 116, 27));

        });

        this.addDataSlots(pData);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 107 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 165));
        }
    }

    public void fillCraftSlotsStackedContents(StackedContents pItemHelper) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.container).fillStackedContents(pItemHelper);
        }

    }

    public void clearCraftingContent() {
        this.getSlot(0).set(ItemStack.EMPTY);
        this.getSlot(1).set(ItemStack.EMPTY);
        this.getSlot(2).set(ItemStack.EMPTY);

        this.getSlot(4).set(ItemStack.EMPTY);
    }

    public boolean recipeMatches(Recipe<? super Container> pRecipe) {
        return pRecipe.matches(this.container, this.level);
    }

    public int getResultSlotIndex() {
        return 3;
    }

    public int getGridWidth() {
        return 3;
    }

    public int getGridHeight() {
        return 1;
    }

    public int getSize() {
        return 5;
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(Player pPlayer) {
        return this.container.stillValid(pPlayer);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex == 3) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (pIndex != 1 && pIndex != 0) {
                if (this.canSmelt(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 3 && pIndex < 30) {
                    if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 30 && pIndex < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
    }

    protected boolean canSmelt(ItemStack pStack) {
        return this.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>)this.recipeType, new SimpleContainer(pStack), this.level).isPresent();
    }

    public boolean isFuel(ItemStack pStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(pStack, this.recipeType) > 0;
    }

    public int getBurnProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.data.get(0) * 21 / i;
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }

    public boolean shouldMoveToInventory(int pSlotIndex) {
        return pSlotIndex != 4;
    }
}
