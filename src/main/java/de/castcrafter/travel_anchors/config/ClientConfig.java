package de.castcrafter.travel_anchors.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import de.castcrafter.travel_anchors.TravelAnchors;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    static {
        init(CLIENT_BUILDER);
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static ForgeConfigSpec.BooleanValue DISABLE_ELEVATE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("travel-anchor-client");
        DISABLE_ELEVATE = builder.comment(
                "When this is set, wou won't be able to use the elevation feature of travel anchors",
                "but you'll teleport to the anchor you're looking at when jumping on another travel anchor",
                "This is a client option so each player can adjust it as they prefer."
        ).define("noElevation", false);
    }
}
