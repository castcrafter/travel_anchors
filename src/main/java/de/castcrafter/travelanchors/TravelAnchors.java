package de.castcrafter.travelanchors;

import de.castcrafter.travelanchors.data.BlockLootProvider;
import de.castcrafter.travelanchors.data.BlockStatesProvider;
import de.castcrafter.travelanchors.data.ItemModelsProvider;
import de.castcrafter.travelanchors.data.RecipesProvider;
import de.castcrafter.travelanchors.network.Networking;
import de.castcrafter.travelanchors.render.TravelAnchorRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.moddingx.libx.datagen.DatagenSystem;
import org.moddingx.libx.mod.ModXRegistration;
import org.moddingx.libx.registration.RegistrationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

@Mod("travelanchors")
public final class TravelAnchors extends ModXRegistration {

    public static final Logger logger = LoggerFactory.getLogger(TravelAnchors.class);
    
    private static TravelAnchors instance;
    private static Networking network;
    private static Tab tab;

    public TravelAnchors() {
        super();

        instance = this;
        network = new Networking(this);
        tab = new Tab(this);
        
        MinecraftForge.EVENT_BUS.register(new EventListener());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(TravelAnchorRenderer::renderAnchors));

        DatagenSystem.create(this, system -> {
            system.addDataProvider(BlockStatesProvider::new);
            system.addDataProvider(ItemModelsProvider::new);
            system.addDataProvider(BlockLootProvider::new);
            system.addDataProvider(RecipesProvider::new);
        });
    }

    @Nonnull
    public static TravelAnchors getInstance() {
        return instance;
    }

    @Nonnull
    public static Networking getNetwork() {
        return network;
    }

    @Nonnull
    public static Tab getTab() {
        return tab;
    }

    @Override
    protected void initRegistration(RegistrationBuilder builder) {
        //
    }

    @Override
    protected void setup(final FMLCommonSetupEvent event) {
        logger.info("Loading TravelAnchors");
    }

    @Override
    protected void clientSetup(final FMLClientSetupEvent event) {
        
    }
}
