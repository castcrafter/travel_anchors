package de.castcrafter.travel_anchors.setup;

import de.castcrafter.travel_anchors.TravelAnchors;
import de.castcrafter.travel_anchors.blocks.TravelAnchorBlock;
import de.castcrafter.travel_anchors.tileentity.TravelAnchorTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TravelAnchors.MOD_ID);

    public static final RegistryObject<TileEntityType<TravelAnchorTileEntity>> TRAVEL_ANCHOR = TILE_ENTITY_TYPES.register("travel_anchor", () ->
            TileEntityType.Builder.create(TravelAnchorTileEntity::new, ModBlocks.TRAVEL_ANCHOR.get())
                    .build(null)
    );
}
