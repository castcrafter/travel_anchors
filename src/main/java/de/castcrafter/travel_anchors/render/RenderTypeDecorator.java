package de.castcrafter.travel_anchors.render;

import java.util.Optional;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.VertexFormat;

/**
 * This class is part of the ObserverLib Mod
 * The complete source code for this mod can be found on github.
 * Class: RenderTypeDecorator
 * Created by HellFirePvP
 * Date: 06.06.2020 / 20:23
 */
public class RenderTypeDecorator extends RenderType {

    private final RenderType decorated;
    private final Runnable afterSetup;
    private final Runnable beforeClean;

    private RenderTypeDecorator(RenderType type, Runnable afterSetup, Runnable beforeClean) {
        super(type.toString(), type.getVertexFormat(), type.getDrawMode(), type.getBufferSize(), type.isUseDelegate(), false, () -> {}, () -> {});
        this.decorated = type;
        this.afterSetup = afterSetup;
        this.beforeClean = beforeClean;
    }

    public static RenderTypeDecorator decorate(RenderType type) {
        return new RenderTypeDecorator(type, () -> {}, () -> {});
    }

    public static RenderTypeDecorator wrapSetup(RenderType type, Runnable setup, Runnable clean) {
        return new RenderTypeDecorator(type, setup, clean);
    }

    @Override
    public void setupRenderState() {
        this.decorated.setupRenderState();
        this.afterSetup.run();
    }

    @Override
    public void clearRenderState() {
        this.beforeClean.run();
        this.decorated.clearRenderState();
    }

    //The ints are currently unused/always 0
    @Override
    public void finish(BufferBuilder buf, int sortOffsetX, int sortOffsetY, int sortOffsetZ) {
        super.finish(buf, sortOffsetX, sortOffsetY, sortOffsetZ);
    }

    @Override
    public String toString() {
        return this.decorated.toString();
    }

    @Override
    public int getBufferSize() {
        return this.decorated.getBufferSize();
    }

    @Override
    public VertexFormat getVertexFormat() {
        return this.decorated.getVertexFormat();
    }

    @Override
    public int getDrawMode() {
        return this.decorated.getDrawMode();
    }

    @Override
    public Optional<RenderType> getOutline() {
        return this.decorated.getOutline();
    }

    @Override
    public boolean isColoredOutlineBuffer() {
        return this.decorated.isColoredOutlineBuffer();
    }

    @Override
    public boolean isUseDelegate() {
        return this.decorated.isUseDelegate();
    }

    @Override
    public Optional<RenderType> getRenderType() {
        return Optional.of(this);
    }
}