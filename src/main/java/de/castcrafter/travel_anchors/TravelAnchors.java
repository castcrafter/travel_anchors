package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.setup.ModBlocks;
import de.castcrafter.travel_anchors.setup.ModContainerTypes;
import de.castcrafter.travel_anchors.setup.ModItems;
import de.castcrafter.travel_anchors.setup.ModTileEntityTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TravelAnchors.MOD_ID)
public class TravelAnchors
{
    public static final String MOD_ID = "travel_anchors";
    private static final Logger LOGGER = LogManager.getLogger();

    public TravelAnchors() {
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
    }
}
