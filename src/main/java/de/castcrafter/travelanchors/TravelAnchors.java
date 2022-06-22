package de.castcrafter.travelanchors;

import de.castcrafter.travelanchors.network.Networking;
import de.castcrafter.travelanchors.render.TravelAnchorRenderer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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

    public TravelAnchors() {
        super(new CreativeModeTab("travelanchors") {
            @Nonnull
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(ModItems.travelStaff);
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
        builder.enableRegistryTracking();
    }

    @Override
    protected void setup(final FMLCommonSetupEvent event) {
        logger.info("Loading TravelAnchors");
    }

    @Override
    protected void clientSetup(final FMLClientSetupEvent event) {
        
    }
}
