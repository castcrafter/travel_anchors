package de.castcrafter.travel_anchors.config;

import io.github.noeppi_noeppi.libx.annotation.config.RegisterConfig;
import io.github.noeppi_noeppi.libx.config.Config;

@RegisterConfig(value = "client", client = true)
public class ClientConfig {
    
    @Config({
            "When this is set, wou won't be able to use the elevation feature of travel anchors",
            "but you'll teleport to the anchor you're looking at when jumping on another travel anchor",
            "This is a client option so each player can adjust it as they prefer."
    })
    public static boolean disable_elevation = false;
}
