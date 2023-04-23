package de.castcrafter.travelanchors.data;

import de.castcrafter.travelanchors.ModBlocks;
import de.castcrafter.travelanchors.ModItems;
import net.minecraftforge.common.Tags;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.recipe.RecipeProviderBase;
import org.moddingx.libx.datagen.provider.recipe.crafting.CraftingExtension;

public class RecipesProvider extends RecipeProviderBase implements CraftingExtension {
    
    public RecipesProvider(DatagenContext ctx) {
        super(ctx);
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
