package de.castcrafter.travel_anchors.setup;

import de.castcrafter.travel_anchors.TravelAnchors;
import de.castcrafter.travel_anchors.network.Networking;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = TravelAnchors.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {
    public static final ItemGroup ITEM_GROUP = new ItemGroup("travel_anchor") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registration.TRAVEL_ANCHOR_BLOCK.get());
        }
    };
    public static void init(final FMLCommonSetupEvent event) {
        Networking.registerMessages();
    }
}
