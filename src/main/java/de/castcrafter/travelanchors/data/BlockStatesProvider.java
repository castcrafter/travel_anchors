package de.castcrafter.travelanchors.data;

import de.castcrafter.travelanchors.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.BlockStateProviderBase;
import org.moddingx.libx.mod.ModX;

@Datagen
public class BlockStatesProvider extends BlockStateProviderBase {

    public BlockStatesProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    protected void setup() {
        this.cubeAll(ModBlocks.travelAnchor);
    }
}
