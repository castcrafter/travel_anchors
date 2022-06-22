package de.castcrafter.travelanchors.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.ItemModelProviderBase;
import org.moddingx.libx.mod.ModX;

@Datagen
public class ItemModelsProvider extends ItemModelProviderBase {

    public ItemModelsProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    protected void setup() {
        // NO-OP
    }
}
