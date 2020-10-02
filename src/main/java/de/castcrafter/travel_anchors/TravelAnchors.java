package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.blocks.TravelAnchorScreen;
import de.castcrafter.travel_anchors.network.Networking;
import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onWorldLoaded);
        MinecraftForge.EVENT_BUS.addListener(this::onWorldSaved);

        Registration.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Loading TravelAnchors");
        Networking.registerPackets();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Registration.TRAVEL_ANCHOR_CONTAINER.get(), TravelAnchorScreen::new);
    }
    public void onWorldLoaded(WorldEvent.Load event)
    {
        if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld)
        {
            WorldData saver = WorldData.get((ServerWorld) event.getWorld());

            if(saver.data.contains("MyData"))
            {
                LOGGER.debug("Found my data: " + saver.data.get("MyData"));
                //hier data zeug
            }
        }
    }

    public void onWorldSaved(WorldEvent.Save event)
    {
        if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld)
        {
            WorldData saver = WorldData.get((ServerWorld) event.getWorld());
            CompoundNBT myData = new CompoundNBT();
            myData.putInt("MyData", 0); //hier wieder data zeug
            saver.data = myData;
            saver.markDirty();
            LOGGER.debug("Put my data in!");
        }
    }
}
