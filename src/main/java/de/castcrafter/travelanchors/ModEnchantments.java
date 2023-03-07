package de.castcrafter.travelanchors;

import de.castcrafter.travelanchors.enchantments.RangeEnchantment;
import de.castcrafter.travelanchors.enchantments.TeleportationEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.moddingx.libx.annotation.registration.Reg.Exclude;
import org.moddingx.libx.annotation.registration.RegisterClass;

@RegisterClass(registry = "ENCHANTMENT")
public class ModEnchantments {

    @Exclude
    public static final EnchantmentCategory TELEPORTABLE_NO_STAFF = EnchantmentCategory.create(TravelAnchors.getInstance().modid + "_teleportable_no_staff",
            item -> EnchantmentCategory.WEAPON.canEnchant(item) || EnchantmentCategory.DIGGER.canEnchant(item));

    @Exclude
    public static final EnchantmentCategory TELEPORTABLE = EnchantmentCategory.create(TravelAnchors.getInstance().modid + "_teleportable",
            item -> item == ModItems.travelStaff || TELEPORTABLE_NO_STAFF.canEnchant(item));

    public static final Enchantment teleportation = new TeleportationEnchantment();
    public static final Enchantment range = new RangeEnchantment();
}
