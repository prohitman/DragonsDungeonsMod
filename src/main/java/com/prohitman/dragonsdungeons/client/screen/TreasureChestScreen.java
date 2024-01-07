package com.prohitman.dragonsdungeons.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.screen.menu.TreasureChestMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TreasureChestScreen extends AbstractContainerScreen<TreasureChestMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(DragonsDungeons.MODID,
            "textures/gui/treasure_chest_gui.png");
    public TreasureChestScreen(TreasureChestMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {

        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;

        this.imageHeight = 219;
        this.imageWidth = 250;
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, 250, 219);
        //guiGraphics.blit(TEXTURE, x, y, 0,  189, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
