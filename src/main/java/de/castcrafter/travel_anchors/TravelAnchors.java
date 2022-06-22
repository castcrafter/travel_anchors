package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.network.Networking;
import de.castcrafter.travel_anchors.render.TravelAnchorRenderer;
import io.github.noeppi_noeppi.libx.mod.registration.ModXRegistration;
import io.github.noeppi_noeppi.libx.mod.registration.RegistrationBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

@Mod("travel_anchors")
public final class TravelAnchors extends ModXRegistration {

    public static final Logger logger = LoggerFactory.getLogger("travel_anchors");
    
    private static TravelAnchors instance;
    private static Networking network;

    public TravelAnchors() {
        super(new CreativeModeTab("travel_anchors") {
            @Nonnull
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(ModComponents.travelStaff);
            }
        });

        instance = this;
        network = new Networking(this);
        
        MinecraftForge.EVENT_BUS.register(new EventListener());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(TravelAnchorRenderer::renderAnchors));
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
    protected void initRegistration(RegistrationBuilder builder) {
        builder.setVersion(1);
    }

    @Override
    protected void setup(final FMLCommonSetupEvent event) {
        logger.info("Loading TravelAnchors");
    }

    @Override
    protected void clientSetup(final FMLClientSetupEvent event) {
        
    }
}
