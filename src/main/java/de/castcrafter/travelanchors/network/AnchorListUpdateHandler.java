package de.castcrafter.travelanchors.network;

import de.castcrafter.travelanchors.TravelAnchorList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AnchorListUpdateHandler {

    public static void handle(AnchorListUpdateSerializer.AnchorListUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                TravelAnchorList.get(Minecraft.getInstance().level).load(msg.nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
