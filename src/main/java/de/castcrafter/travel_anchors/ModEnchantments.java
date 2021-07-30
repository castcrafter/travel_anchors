package de.castcrafter.travel_anchors;

import de.castcrafter.travel_anchors.enchantments.RangeEnchantment;
import de.castcrafter.travel_anchors.enchantments.TeleportationEnchantment;
import io.github.noeppi_noeppi.libx.annotation.registration.NoReg;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

@RegisterClass
public class ModEnchantments {

    @NoReg public static final EnchantmentCategory TELEPORTABLE_NO_STAFF = EnchantmentCategory.create(TravelAnchors.getInstance().modid + "_teleportable_no_staff",
            item -> EnchantmentCategory.WEAPON.canEnchant(item) || EnchantmentCategory.DIGGER.canEnchant(item));
    
    @NoReg public static final EnchantmentCategory TELEPORTABLE = EnchantmentCategory.create(TravelAnchors.getInstance().modid + "_teleportable",
            item -> item == ModComponents.travelStaff || TELEPORTABLE_NO_STAFF.canEnchant(item));

    public static final Enchantment teleportation = new TeleportationEnchantment();
    public static final Enchantment range = new RangeEnchantment();
}
