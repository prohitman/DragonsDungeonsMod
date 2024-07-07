package com.prohitman.dragonsdungeons.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.screen.menu.FoundryBEMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FoundryBEScreen extends AbstractContainerScreen<FoundryBEMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DragonsDungeons.MODID, "textures/gui/foundry_ui.png");

    public FoundryBEScreen(FoundryBEMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;

        this.imageHeight = 188;
        this.imageWidth = 175;

        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, 175, 188);

        renderProgressArrow(guiGraphics, x, y);
        renderFuelBar(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 88, y + 26, 238, 46, menu.getScaledProgress(), 17);
        }
    }

    private void renderFuelBar(GuiGraphics guiGraphics, int x, int y) {
        int fuelLeftScaled = menu.getFuelLeftScaled();
        if (fuelLeftScaled > 0) {
            guiGraphics.blit(TEXTURE, x + 80, y + 45 + 20 - fuelLeftScaled, 240, 11+20 - fuelLeftScaled, 15, fuelLeftScaled);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
