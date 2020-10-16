package de.castcrafter.travel_anchors.network;

import de.castcrafter.travel_anchors.TravelAnchorList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AnchorListUpdateHandler {

    public static void handle(AnchorListUpdateSerializer.AnchorListUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().world != null) {
                TravelAnchorList.get(Minecraft.getInstance().world).read(msg.nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
