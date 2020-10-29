package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.block.ScreenTravelAnchor;
import de.castcrafter.travel_anchors.config.ServerConfig;
import de.castcrafter.travel_anchors.network.Networking;
import de.castcrafter.travel_anchors.block.RenderTravelAnchor;
import io.github.noeppi_noeppi.libx.mod.registration.ModXRegistration;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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

import javax.annotation.Nonnull;

@Mod("travel_anchors")
public class TravelAnchors extends ModXRegistration {

    private static TravelAnchors instance;
    private static Networking network;

    public TravelAnchors() {
        super("travel_anchors", new ItemGroup("travel_anchors") {
            @Nonnull
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModComponents.travelStaff);
            }
        });

        instance = this;
        network = new Networking(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ServerConfig.loadConfig(ServerConfig.SERVER_CONFIG, FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(this.modid + "-server.toml"));

        this.addRegistrationHandler(ModComponents::register);
        this.addRegistrationHandler(ModEnchantments::register);

        DistExecutor.unsafeRunForDist(() -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(RenderTravelAnchor::registerModels);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(RenderTravelAnchor::bakeModels);
            return null;
        }, () -> () -> null);

        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    @Nonnull
    public static TravelAnchors getInstance() {
        return instance;
    }

    @Nonnull
    public static Networking getNetwork() {
        return network;
    }

    @Override
    protected void setup(final FMLCommonSetupEvent event) {
        this.logger.info("Loading TravelAnchors");
    }

    @Override
    protected void clientSetup(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ModComponents.travelAnchor, RenderType.getCutoutMipped());
        ScreenManager.registerFactory(ModComponents.travelAnchor.container, ScreenTravelAnchor::new);
        ClientRegistry.bindTileEntityRenderer(ModComponents.travelAnchor.getTileType(), RenderTravelAnchor::new);
    }
}
