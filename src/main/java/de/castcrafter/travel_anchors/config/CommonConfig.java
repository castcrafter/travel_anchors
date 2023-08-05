package de.castcrafter.travel_anchors.config;

import io.github.noeppi_noeppi.libx.annotation.config.RegisterConfig;
import io.github.noeppi_noeppi.libx.config.Config;
import io.github.noeppi_noeppi.libx.config.validator.DoubleRange;
import io.github.noeppi_noeppi.libx.config.validator.IntRange;

@RegisterConfig("common")
public class CommonConfig {
    
    @Config("The maximum angle you can look at the Travel Anchor to teleport.")
    @DoubleRange(min = 1)
    public static double max_angle = 30;
    
    @Config("The maximum distance you are allowed to teleport.")
    @DoubleRange(min = 1)
    public static double max_distance = 64;

    @Config("The cooldown in ticks between teleports.")
    @IntRange(min = 0)
    public static int cooldown = 30;
}
