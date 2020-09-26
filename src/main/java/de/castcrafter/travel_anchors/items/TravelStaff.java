package de.castcrafter.travel_anchors.items;

import de.castcrafter.travel_anchors.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;


public class TravelStaff extends Item{

    public static final RegistryObject<Item> TRAVEL_STAFF = Registration.ITEMS.register("travel_staff", () ->
            new TravelStaff(new Item.Properties().group(ItemGroup.TOOLS)));

    public TravelStaff(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack equipped = playerIn.getHeldItem(handIn);
        System.out.println("rechtsklick1");
        if (playerIn.isSneaking()){
            System.out.println("rechtsklick2");
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public static void register(){}
}
