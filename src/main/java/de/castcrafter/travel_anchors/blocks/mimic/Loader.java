package de.castcrafter.travel_anchors.blocks.mimic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class Loader implements IModelLoader<Geometry> {

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public Geometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new Geometry();
    }
}

