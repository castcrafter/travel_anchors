package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.setup.ClientSetup;
import de.castcrafter.travel_anchors.setup.ModSetup;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TravelAnchors.MODID)
public class TravelAnchors
{
    public static final String MODID = "travel_anchors";
    private static final Logger LOGGER = LogManager.getLogger();

    public TravelAnchors() {
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CONFIG_CLIENT);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CLIENT);

        Registration.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }
}
