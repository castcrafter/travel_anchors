package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.block.TravelAnchorScreen;
import de.castcrafter.travel_anchors.config.ServerConfig;
import de.castcrafter.travel_anchors.network.Networking;
import de.castcrafter.travel_anchors.render.SpecialRender;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ServerConfig.loadConfig(ServerConfig.SERVER_CONFIG, FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(MODID + "-server.toml"));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        DistExecutor.unsafeRunForDist(() -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(SpecialRender::bakeModels);
            return null;
        }, () -> () -> null);

        MinecraftForge.EVENT_BUS.register(new EventListener());

        Registration.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Loading TravelAnchors");
        Networking.registerPackets();
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(Registration.TRAVEL_ANCHOR_BLOCK.get(), (RenderType) -> true);
        RenderTypeLookup.setRenderLayer(Registration.TRAVEL_ANCHOR_BLOCK.get(), RenderType.getCutoutMipped());

        ScreenManager.registerFactory(Registration.TRAVEL_ANCHOR_CONTAINER.get(), TravelAnchorScreen::new);

        ModelLoader.addSpecialModel(SpecialRender.ANCHOR_MODEL);

        ClientRegistry.bindTileEntityRenderer(Registration.TRAVEL_ANCHOR_TILE.get(), SpecialRender::new);
    }
}
