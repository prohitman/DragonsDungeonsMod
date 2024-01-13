package com.prohitman.dragonsdungeons.client.screen;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.screen.menu.FoundryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.BlastingRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FoundryScreen extends AbstractContainerScreen<FoundryMenu> implements RecipeUpdateListener {
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    public final AbstractFurnaceRecipeBookComponent recipeBookComponent;
    private boolean widthTooNarrow;
    private static final ResourceLocation TEXTURE = new ResourceLocation(DragonsDungeons.MODID,
            "textures/gui/foundry_gui.png");
    public FoundryScreen(FoundryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
            this.recipeBookComponent = new BlastingRecipeBookComponent();
    }

    public void init() {
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;

        this.imageHeight = 188;
        this.imageWidth = 175;

        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.addRenderableWidget(new ImageButton(this.leftPos + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (p_289628_) -> {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            p_289628_.setPosition(this.leftPos + 20, this.height / 2 - 49);
        }));
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    /**
     * Renders the graphical user interface (GUI) element.
     * @param pGuiGraphics the GuiGraphics object used for rendering.
     * @param pMouseX the x-coordinate of the mouse cursor.
     * @param pMouseY the y-coordinate of the mouse cursor.
     * @param pPartialTick the partial tick time.
     */
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
            this.recipeBookComponent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        } else {
            this.recipeBookComponent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            this.recipeBookComponent.renderGhostRecipe(pGuiGraphics, this.leftPos, this.topPos, true, pPartialTick);
        }

        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        this.recipeBookComponent.renderTooltip(pGuiGraphics, this.leftPos, this.topPos, pMouseX, pMouseY);
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        pGuiGraphics.blit(TEXTURE, i, j, 0, 0, 175, 188);
        if (this.menu.isLit()) {
            int k = this.menu.getLitProgress();
            pGuiGraphics.blit(TEXTURE, i + 80, j + 45 + 20 - k, 240, 11+20 - k, 15, k + 1);
        }

        int l = this.menu.getBurnProgress();
        pGuiGraphics.blit(TEXTURE, i + 97, j + 25, 238, 46, l + 1, 17);
    }

    /**
     * Called when a mouse button is clicked within the GUI element.
     * <p>
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     * @param pMouseX the X coordinate of the mouse.
     * @param pMouseY the Y coordinate of the mouse.
     * @param pButton the button that was clicked.
     */
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.recipeBookComponent.mouseClicked(pMouseX, pMouseY, pButton)) {
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() ? true : super.mouseClicked(pMouseX, pMouseY, pButton);
        }
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void slotClicked(Slot pSlot, int pSlotId, int pMouseButton, ClickType pType) {
        super.slotClicked(pSlot, pSlotId, pMouseButton, pType);
        this.recipeBookComponent.slotClicked(pSlot);
    }

    /**
     * Called when a keyboard key is pressed within the GUI element.
     * <p>
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     * @param pKeyCode the key code of the pressed key.
     * @param pScanCode the scan code of the pressed key.
     * @param pModifiers the keyboard modifiers.
     */
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return this.recipeBookComponent.keyPressed(pKeyCode, pScanCode, pModifiers) ? false : super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    protected boolean hasClickedOutside(double pMouseX, double pMouseY, int pGuiLeft, int pGuiTop, int pMouseButton) {
        boolean flag = pMouseX < (double)pGuiLeft || pMouseY < (double)pGuiTop || pMouseX >= (double)(pGuiLeft + this.imageWidth) || pMouseY >= (double)(pGuiTop + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(pMouseX, pMouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, pMouseButton) && flag;
    }

    /**
     * Called when a character is typed within the GUI element.
     * <p>
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     * @param pCodePoint the code point of the typed character.
     * @param pModifiers the keyboard modifiers.
     */
    public boolean charTyped(char pCodePoint, int pModifiers) {
        return this.recipeBookComponent.charTyped(pCodePoint, pModifiers) ? true : super.charTyped(pCodePoint, pModifiers);
    }

    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
