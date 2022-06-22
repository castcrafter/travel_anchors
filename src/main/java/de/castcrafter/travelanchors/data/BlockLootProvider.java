package de.castcrafter.travelanchors.data;

import net.minecraft.data.DataGenerator;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.loot.BlockLootProviderBase;
import org.moddingx.libx.mod.ModX;

@Datagen
public class BlockLootProvider extends BlockLootProviderBase {

    public BlockLootProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    protected void setup() {
        // NO-OP
    }
}
