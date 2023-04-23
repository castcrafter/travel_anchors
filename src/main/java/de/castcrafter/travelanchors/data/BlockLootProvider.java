package de.castcrafter.travelanchors.data;

import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.loot.BlockLootProviderBase;

public class BlockLootProvider extends BlockLootProviderBase {
    
    public BlockLootProvider(DatagenContext ctx) {
        super(ctx);
    }

    @Override
    protected void setup() {
        // NO-OP
    }
}
