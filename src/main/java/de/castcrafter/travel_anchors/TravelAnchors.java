package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.blocks.TravelAnchorScreen;
import de.castcrafter.travel_anchors.config.ServerConfig;
import de.castcrafter.travel_anchors.network.Networking;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TravelAnchors.MODID)
public class TravelAnchors {

    public static final String MODID = "travel_anchors";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup ITEM_GROUP = new ItemGroup("travel_anchors") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registration.TRAVEL_STAFF.get());
        }
    };

    public TravelAnchors() {
        ServerConfig.loadConfig(ServerConfig.SERVER_CONFIG, FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(MODID + "-server.toml"));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        Registration.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Loading TravelAnchors");
        Networking.registerPackets();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Registration.TRAVEL_ANCHOR_CONTAINER.get(), TravelAnchorScreen::new);
    }
}
