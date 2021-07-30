package de.castcrafter.travel_anchors.block;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.castcrafter.travel_anchors.TravelAnchors;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ScreenTravelAnchor extends AbstractContainerScreen<MenuTravelAnchor> {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(TravelAnchors.getInstance().modid, "textures/gui/travel_anchor.png");
    private EditBox textFieldWidget;

    public ScreenTravelAnchor(MenuTravelAnchor screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }

    @Override
    public void init() {
        super.init();
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        this.textFieldWidget = new EditBox(this.font, this.width / 2 - 50, this.height / 2 - 63, 100, 15, new TranslatableComponent("screen.travel_anchors.search"));
        this.textFieldWidget.setMaxLength(32767);
        this.textFieldWidget.changeFocus(true);
        this.textFieldWidget.setValue(this.menu.getBlockEntity().getName());
    }

    @Override
    protected void renderBg(@Nonnull PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderHelper.resetColor();
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        this.blit(poseStack, (this.width - this.imageWidth) / 2, (this.height - this.imageHeight) / 2, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(@Nonnull PoseStack poseStack, int mouseX, int mouseY) {
        String title = this.title.getString();
        this.font.draw(poseStack, title, (float) (this.imageWidth / 2 - this.font.width(title) / 2), 6.0F, 0x404040);
        boolean empty = this.textFieldWidget.getValue().trim().isEmpty();
        this.font.draw(poseStack, empty ? I18n.get("screen.travel_anchors.nameless") : this.playerInventoryTitle.getString(), 8, (float) (this.imageHeight - 126), empty ? 0xB31616 : 0x404040);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        this.textFieldWidget.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    @Nullable
    public GuiEventListener getFocused() {
        return this.textFieldWidget;
    }

    @Override
    public void removed() {
        super.removed();
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(false);
        if (Minecraft.getInstance().level != null) {
            TravelAnchors.getNetwork().sendNameChange(this.menu.getLevel(), this.menu.getPos(), this.textFieldWidget.getValue().trim());
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputConstants.Key mapping = InputConstants.getKey(keyCode, scanCode);
        //noinspection ConstantConditions
        if (keyCode != 256 && (this.minecraft.options.keyInventory.isActiveAndMatches(mapping)
                || this.minecraft.options.keyDrop.isActiveAndMatches(mapping))) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void containerTick() {
        this.textFieldWidget.tick();
    }
}
