package de.castcrafter.travelanchors.data;

import de.castcrafter.travelanchors.ModBlocks;
import de.castcrafter.travelanchors.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.recipe.RecipeProviderBase;
import org.moddingx.libx.datagen.provider.recipe.crafting.CraftingExtension;
import org.moddingx.libx.mod.ModX;

@Datagen
public class RecipesProvider extends RecipeProviderBase implements CraftingExtension {

    public RecipesProvider(ModX mod, PackOutput output) {
        super(mod, output);
    }

    @Override
    protected void setup() {
        this.shaped(ModItems.travelStaff,
                "  i",
                " e ",
                "e  ",
                'i', Tags.Items.INGOTS_IRON,
                'e', Tags.Items.ENDER_PEARLS);
        this.shaped(ModBlocks.travelAnchor,
                "bib",
                "iei",
                "bib",
                'b', Tags.Items.STORAGE_BLOCKS_IRON,
                'i', Tags.Items.INGOTS_IRON,
                'e', Tags.Items.ENDER_PEARLS);
    }
}
