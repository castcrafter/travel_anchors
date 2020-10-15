package de.castcrafter.travel_anchors.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.castcrafter.travel_anchors.TravelAnchors;
import de.castcrafter.travel_anchors.network.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class TravelAnchorScreen extends ContainerScreen<TravelAnchorContainer> {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(TravelAnchors.MODID, "textures/gui/travel_anchor.png");
    private TextFieldWidget textFieldWidget;

    public TravelAnchorScreen(TravelAnchorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void init() {
        super.init();
        this.getMinecraft().keyboardListener.enableRepeatEvents(true);
        this.textFieldWidget = new TextFieldWidget(this.font, this.width / 2 - 50, this.height / 2 - 63, 100, 15, new TranslationTextComponent("screen.travel_anchors.search"));
        this.textFieldWidget.setMaxStringLength(32767);
        this.textFieldWidget.changeFocus(true);
        this.textFieldWidget.setText(this.container.tile.getName());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        //noinspection deprecation
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        //noinspection ConstantConditions
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        this.blit(matrixStack, (this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {
        String title = this.title.getString();
        this.font.drawString(matrixStack, title, (float) (this.xSize / 2 - this.font.getStringWidth(title) / 2), 6.0F, 0x404040);
        this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 8, (float) (this.ySize - 126), 0x404040);
    }

    @Override
    public void render(@Nonnull MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(ms, mouseX, mouseY);
        this.textFieldWidget.render(ms, mouseX, mouseY, partialTicks);
    }

    @Override
    @Nullable
    public IGuiEventListener getListener() {
        return this.textFieldWidget;
    }

    @Override
    public void onClose() {
        super.onClose();
        this.getMinecraft().keyboardListener.enableRepeatEvents(false);
        if (Minecraft.getInstance().world != null) {
            Networking.sendNameChange(this.container.getWorld(), this.container.getPos(), this.textFieldWidget.getText());
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputMappings.Input mapping = InputMappings.getInputByCode(keyCode, scanCode);
        //noinspection ConstantConditions
        if (keyCode != 256 && (this.minecraft.gameSettings.keyBindInventory.isActiveAndMatches(mapping)
                || this.minecraft.gameSettings.keyBindDrop.isActiveAndMatches(mapping))) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void tick() {
        this.textFieldWidget.tick();
    }
}
