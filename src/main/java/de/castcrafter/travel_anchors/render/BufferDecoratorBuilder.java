package de.castcrafter.travel_anchors.render;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.IVertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * This class is part of the ObserverLib Mod
 * The complete source code for this mod can be found on github.
 * Class: BufferDecoratorBuilder
 * Created by HellFirePvP
 * Date: 11.02.2020 / 18:48
 */
public class BufferDecoratorBuilder {

    private PositionDecorator positionDecorator;
    private ColorDecorator colorDecorator;
    private UVDecorator uvDecorator;
    private IntMapDecorator overlayDecorator;
    private IntMapDecorator lightmapDecorator;
    private NormalDecorator normalDecorator;

    ///////////////////////////////////////////////////////////////////////////
    //      Decoration options
    ///////////////////////////////////////////////////////////////////////////

    public static BufferDecoratorBuilder withPosition(PositionDecorator positionDecorator) {
        return new BufferDecoratorBuilder().setPositionDecorator(positionDecorator);
    }

    public BufferDecoratorBuilder setPositionDecorator(PositionDecorator positionDecorator) {
        this.positionDecorator = positionDecorator;
        return this;
    }

    public static BufferDecoratorBuilder withColor(ColorDecorator colorDecorator) {
        return new BufferDecoratorBuilder().setColorDecorator(colorDecorator);
    }

    public BufferDecoratorBuilder setColorDecorator(ColorDecorator colorDecorator) {
        this.colorDecorator = colorDecorator;
        return this;
    }

    public static BufferDecoratorBuilder withUV(UVDecorator uvDecorator) {
        return new BufferDecoratorBuilder().setUvDecorator(uvDecorator);
    }

    public BufferDecoratorBuilder setUvDecorator(UVDecorator uvDecorator) {
        this.uvDecorator = uvDecorator;
        return this;
    }

    public static BufferDecoratorBuilder withOverly(IntMapDecorator overlayDecorator) {
        return new BufferDecoratorBuilder().setOverlayDecorator(overlayDecorator);
    }

    public BufferDecoratorBuilder setOverlayDecorator(IntMapDecorator overlayDecorator) {
        this.overlayDecorator = overlayDecorator;
        return this;
    }

    public static BufferDecoratorBuilder withLightmap(IntMapDecorator lightmapDecorator) {
        return new BufferDecoratorBuilder().setLightmapDecorator(lightmapDecorator);
    }

    public BufferDecoratorBuilder setLightmapDecorator(IntMapDecorator lightmapDecorator) {
        this.lightmapDecorator = lightmapDecorator;
        return this;
    }

    public static BufferDecoratorBuilder withNormal(NormalDecorator normalDecorator) {
        return new BufferDecoratorBuilder().setNormalDecorator(normalDecorator);
    }

    public BufferDecoratorBuilder setNormalDecorator(NormalDecorator normalDecorator) {
        this.normalDecorator = normalDecorator;
        return this;
    }

    public void decorate(IVertexBuilder builder, Consumer<IVertexBuilder> runDecorated) {
        runDecorated.accept(new DecoratedBuilder(builder, this));
    }

    public void decorate(IVertexConsumer consumer, Consumer<IVertexConsumer> runDecorated) {
        runDecorated.accept(new DecoratedConsumer(consumer, this));
    }

    public void decorate(BufferBuilder buf, Consumer<BufferBuilder> runDecorated) {
        runDecorated.accept(new DecoratedBufferBuilder(buf, this));
    }

    public IVertexBuilder decorate(IVertexBuilder builder) {
        return new DecoratedBuilder(builder, this);
    }

    public IVertexConsumer decorate(IVertexConsumer builder) {
        return new DecoratedConsumer(builder, this);
    }

    public BufferBuilder decorate(BufferBuilder builder) {
        return new DecoratedBufferBuilder(builder, this);
    }

    private static class DecoratedBuilder implements IVertexBuilder {

        final IVertexBuilder vertexBuilder;
        final BufferDecoratorBuilder decorator;

        private DecoratedBuilder(IVertexBuilder vertexBuilder, BufferDecoratorBuilder decorator) {
            this.vertexBuilder = vertexBuilder;
            this.decorator = decorator;
        }

        ///////////////////////////////////////////////////////////////////////////
        //      Methods with decorated changes
        ///////////////////////////////////////////////////////////////////////////

        @Override
        public IVertexBuilder pos(double x, double y, double z) {
            if (this.decorator.positionDecorator != null) {
                double[] newPosition = this.decorator.positionDecorator.decorate(x, y, z);
                this.vertexBuilder.pos(newPosition[0], newPosition[1], newPosition[2]);
                return this;
            }
            this.vertexBuilder.pos(x, y, z);
            return this;
        }

        @Override
        public IVertexBuilder color(int red, int green, int blue, int alpha) {
            if (this.decorator.colorDecorator != null) {
                int[] newColor = this.decorator.colorDecorator.decorate(red, green, blue, alpha);
                this.vertexBuilder.color(newColor[0], newColor[1], newColor[2], newColor[3]);
                return this;
            }
            this.vertexBuilder.color(red, green, blue, alpha);
            return this;
        }

        @Override
        public IVertexBuilder tex(float u, float v) {
            if (this.decorator.uvDecorator != null) {
                float[] newUV = this.decorator.uvDecorator.decorate(u, v);
                this.vertexBuilder.tex(newUV[0], newUV[1]);
                return this;
            }
            this.vertexBuilder.tex(u, v);
            return this;
        }

        @Override
        public IVertexBuilder overlay(int u, int v) {
            if (this.decorator.overlayDecorator != null) {
                int[] newUV = this.decorator.overlayDecorator.decorate(u, v);
                this.vertexBuilder.overlay(newUV[0], newUV[1]);
                return this;
            }
            this.vertexBuilder.overlay(u, v);
            return this;
        }

        @Override
        public IVertexBuilder lightmap(int u, int v) {
            if (this.decorator.lightmapDecorator != null) {
                int[] newUV = this.decorator.lightmapDecorator.decorate(u, v);
                this.vertexBuilder.lightmap(newUV[0], newUV[1]);
                return this;
            }
            this.vertexBuilder.lightmap(u, v);
            return this;
        }

        @Override
        public IVertexBuilder normal(float x, float y, float z) {
            if (this.decorator.normalDecorator != null) {
                float[] newNormals = this.decorator.normalDecorator.decorate(x, y, z);
                this.vertexBuilder.normal(newNormals[0], newNormals[1], newNormals[2]);
                return this;
            }
            this.vertexBuilder.normal(x, y, z);
            return this;
        }

        @Override
        public void endVertex() {
            this.vertexBuilder.endVertex();
        }
    }

    private static class DecoratedConsumer extends DecoratedBuilder implements IVertexConsumer {

        final IVertexConsumer vertexConsumer;

        private DecoratedConsumer(IVertexConsumer vertexConsumer, BufferDecoratorBuilder decorator) {
            super(vertexConsumer, decorator);
            this.vertexConsumer = vertexConsumer;
        }

        ///////////////////////////////////////////////////////////////////////////
        //      Delegate decorations
        //      At this time we applied decorations ideally
        ///////////////////////////////////////////////////////////////////////////

        @Override
        public VertexFormatElement getCurrentElement() {
            return this.vertexConsumer.getCurrentElement();
        }

        @Override
        public void nextVertexFormatIndex() {
            this.vertexConsumer.nextVertexFormatIndex();
        }

        @Override
        public void putByte(int i, byte b) {
            this.vertexConsumer.putByte(i, b);
        }

        @Override
        public void putShort(int i, short s) {
            this.vertexConsumer.putShort(i, s);
        }

        @Override
        public void putFloat(int i, float f) {
            this.vertexConsumer.putFloat(i, f);
        }

    }

    //Well, gotta do it all over again for this one..
    private static class DecoratedBufferBuilder extends BufferBuilder {

        private BufferBuilder decorated;
        private BufferDecoratorBuilder decorator;
        private DecoratedConsumer decoratedDelegate;

        public DecoratedBufferBuilder(BufferBuilder decorated, BufferDecoratorBuilder decorator) {
            super(0);
            this.decorated = decorated;
            this.decoratedDelegate = new DecoratedConsumer(this.decorated, decorator);
            this.decorator = decorator;
        }

        @Override
        public void sortVertexData(float cameraX, float cameraY, float cameraZ) {
            this.decorated.sortVertexData(cameraX, cameraY, cameraZ);
        }

        @Override
        public State getVertexState() {
            return this.decorated.getVertexState();
        }

        @Override
        public void begin(int glMode, VertexFormat format) {
            this.decorated.begin(glMode, format);
        }

        @Override
        public void finishDrawing() {
            this.decorated.finishDrawing();
        }

        @Override
        public boolean isDrawing() {
            return this.decorated.isDrawing();
        }

        @Override
        public Pair<DrawState, ByteBuffer> getNextBuffer() {
            return this.decorated.getNextBuffer();
        }

        @Override
        public void reset() {
            this.decorated.reset();
        }

        @Override
        public void discard() {
            this.decorated.discard();
        }

        @Override
        public void setVertexState(State state) {
            this.decorated.setVertexState(state);
        }

        @Override
        public void endVertex() {
            this.decorated.endVertex();
        }

        //TODO re-check on this. we might need to unpack & repack data here
        @Override
        public void putBulkData(ByteBuffer buffer) {
            this.decorated.putBulkData(buffer);
        }

        @Override
        public VertexFormat getVertexFormat() {
            return super.getVertexFormat();
        }

        @Override
        public void addVertex(float x, float y, float z,
                              float red, float green, float blue, float alpha,
                              float texU, float texV,
                              int overlayUV, int lightmapUV,
                              float normalX, float normalY, float normalZ) {
            if (this.decorator.positionDecorator != null) {
                double[] newPosition = this.decorator.positionDecorator.decorate(x, y, z);
                x = (float) newPosition[0];
                y = (float) newPosition[1];
                z = (float) newPosition[2];
            }
            if (this.decorator.colorDecorator != null) {
                int[] newColors = this.decorator.colorDecorator.decorate((int) red * 255, (int) green * 255, (int) blue * 255, (int) alpha * 255);
                red   = newColors[0] / 255F;
                green = newColors[1] / 255F;
                blue  = newColors[2] / 255F;
                alpha = newColors[3] / 255F;
            }
            if (this.decorator.uvDecorator != null) {
                float[] newUV = this.decorator.uvDecorator.decorate(texU, texV);
                texU = newUV[0];
                texV = newUV[1];
            }
            if (this.decorator.overlayDecorator != null) {
                int[] newOverlayCoords = this.decorator.overlayDecorator.decorate(overlayUV & 0xFFFF, (overlayUV >> 16) & 0xFFFF);
                overlayUV = newOverlayCoords[0] | (newOverlayCoords[1] << 16);
            }
            if (this.decorator.lightmapDecorator != null) {
                int[] newLightMapCoords = this.decorator.lightmapDecorator.decorate(lightmapUV & 0xFFFF, (lightmapUV >> 16) & 0xFFFF);
                lightmapUV = newLightMapCoords[0] | (newLightMapCoords[1] << 16);
            }
            if (this.decorator.normalDecorator != null) {
                float[] newNormals = this.decorator.normalDecorator.decorate(normalX, normalY, normalZ);
                normalX = newNormals[0];
                normalY = newNormals[1];
                normalZ = newNormals[2];
            }
            super.addVertex(x, y, z, red, green, blue, alpha, texU, texV, overlayUV, lightmapUV, normalX, normalY, normalZ);
        }

        @Override
        public VertexFormatElement getCurrentElement() {
            return this.decoratedDelegate.getCurrentElement();
        }

        @Override
        public void nextVertexFormatIndex() {
            this.decoratedDelegate.nextVertexFormatIndex();
        }

        @Override
        public void putByte(int i, byte b) {
            this.decoratedDelegate.putByte(i, b);
        }

        @Override
        public void putFloat(int i, float f) {
            this.decoratedDelegate.putFloat(i, f);
        }

        @Override
        public void putShort(int i, short s) {
            this.decoratedDelegate.putShort(i, s);
        }

        @Override
        public void setDefaultColor(int red, int green, int blue, int alpha) {
            this.decorated.setDefaultColor(red, green, blue, alpha);
        }

        @Override
        public IVertexBuilder pos(double x, double y, double z) {
            return this.decoratedDelegate.pos(x, y, z);
        }

        @Override
        public IVertexBuilder color(int red, int green, int blue, int alpha) {
            return this.decoratedDelegate.color(red, green, blue, alpha);
        }

        @Override
        public IVertexBuilder tex(float u, float v) {
            return this.decoratedDelegate.tex(u, v);
        }

        @Override
        public IVertexBuilder overlay(int u, int v) {
            return this.decoratedDelegate.overlay(u, v);
        }

        @Override
        public IVertexBuilder lightmap(int u, int v) {
            return this.decoratedDelegate.lightmap(u, v);
        }

        @Override
        public IVertexBuilder normal(float x, float y, float z) {
            return this.decoratedDelegate.normal(x, y, z);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    //      Decorator interfaces
    ///////////////////////////////////////////////////////////////////////////

    public static interface PositionDecorator {

        public double[] decorate(double x, double y, double z);

    }

    public static interface NormalDecorator {

        public float[] decorate(float x, float y, float z);

    }

    public static interface IntMapDecorator {

        //0-15 each, return same
        public int[] decorate(int x, int z);

    }

    public static interface UVDecorator {

        public float[] decorate(float u, float v);

    }

    public static interface ColorDecorator {

        public int[] decorate(int r, int g, int b, int a);

    }

}