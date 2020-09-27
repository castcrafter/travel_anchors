package de.castcrafter.travel_anchors.setup;

import de.castcrafter.travel_anchors.TravelAnchors;
import de.castcrafter.travel_anchors.items.TravelStaff;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sun.security.util.DerEncoder;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TravelAnchors.MOD_ID);

    public static final RegistryObject<Item> TRAVEL_STAFF = ModItems.ITEMS.register("travel_staff", () ->
            new TravelStaff(new Item.Properties().group(ItemGroup.TOOLS)));
}
