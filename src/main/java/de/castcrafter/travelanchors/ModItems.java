package de.castcrafter.travelanchors;

import de.castcrafter.travelanchors.item.ItemTravelStaff;
import net.minecraft.world.item.Item;
import org.moddingx.libx.annotation.registration.RegisterClass;
import org.moddingx.libx.base.ItemBase;

@RegisterClass(registry = "ITEM_REGISTRY")
public class ModItems {

    public static final ItemBase travelStaff = new ItemTravelStaff(TravelAnchors.getInstance(), new Item.Properties().stacksTo(1));
}
