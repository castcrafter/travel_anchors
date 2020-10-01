package de.castcrafter.travel_anchors.network;

import de.castcrafter.travel_anchors.TravelAnchors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class Networking {

    private static final String PROTOCOL_VERSION = "1";
    private static int discriminator = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TravelAnchors.MODID, "netchannel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        register(new AnchorNameChangeHandler(), NetworkDirection.PLAY_TO_SERVER);
    }

    private static <T> void register(PacketHandler<T> handler, @SuppressWarnings("SameParameterValue") NetworkDirection direction) {
        INSTANCE.registerMessage(discriminator++, handler.messageClass(), handler::encode, handler::decode, handler::handle, Optional.of(direction));
    }

    public static void sendNameChange(World world, BlockPos pos, String name) {
        if (world.isRemote) {
            INSTANCE.sendToServer(new AnchorNameChangeHandler.AnchorNameChangeMessage(pos, name));
        }
    }
}
