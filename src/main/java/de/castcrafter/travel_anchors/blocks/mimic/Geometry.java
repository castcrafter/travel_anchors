package de.castcrafter.travel_anchors.blocks.mimic;

import javafx.util.Pair;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class Geometry implements IModelGeometry<Geometry> {

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        return new BakedModel();
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> missingTextureErrors) {
        return Collections.singletonList(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, BakedModel.TEXTURE));
    }
}
