package de.castcrafter.travel_anchors.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import de.castcrafter.travel_anchors.TravelAnchors;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ServerConfig {
    public static final ForgeConfigSpec SERVER_CONFIG;
    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

    static {
        init(SERVER_BUILDER);
        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    public static ForgeConfigSpec.DoubleValue MAX_ANGLE;
    public static ForgeConfigSpec.DoubleValue MAX_DISTANCE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("travel-anchor-settings");
        MAX_ANGLE = builder.comment("The maximum angle you can look at the Travel Anchor to teleport.", "Be sure to write a .0 after a whole number because forges config system can not handle whole numbers without it.")
                .defineInRange("maxAngle", 30, 1, Double.MAX_VALUE);
        MAX_DISTANCE = builder.comment("The maximum distance you are allowed to teleport.", "Be sure to write a .0 after a whole number because forges config system can not handle whole numbers without it.")
                .defineInRange("maxDistance", 64, 1, Double.MAX_VALUE);
        builder.pop();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        TravelAnchors.LOGGER.debug("Loading config file {}", path);
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }
}
