package de.castcrafter.travel_anchors.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.castcrafter.travel_anchors.TravelAnchors;
import de.castcrafter.travel_anchors.network.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class TravelAnchorScreen extends ContainerScreen<TravelAnchorContainer> {

    private static final ResourceLocation GUI = new ResourceLocation(TravelAnchors.MODID, "textures/gui/travel_anchor.png");
    private TextFieldWidget textFieldWidget;
    private final TravelAnchorTile tile;

    public TravelAnchorScreen(TravelAnchorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.tile = screenContainer.tileEntity;
    }

    @Override
    public void init() {
        super.init();
        this.getMinecraft().keyboardListener.enableRepeatEvents(true);
        this.textFieldWidget = new TextFieldWidget(this.font, this.width / 2 - 50, this.height /2 -63, 100, 15, new TranslationTextComponent("screen.travelanchor.search"));
        this.textFieldWidget.setMaxStringLength(32767);
        this.textFieldWidget.changeFocus(true);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        String s = this.title.getString();
        this.font.drawString(matrixStack, s, (float) (this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 0x404040);
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
        if(Minecraft.getInstance().world != null){
            Networking.sendNameChange(Minecraft.getInstance().world, this.tile.getPos(), this.textFieldWidget.getText());
        }

    }

    @Override
    public void tick() {
        textFieldWidget.tick();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
