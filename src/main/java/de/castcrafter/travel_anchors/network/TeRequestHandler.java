package de.castcrafter.travel_anchors.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class TeRequestHandler {

    public static void handle(TeRequestSerializer.TeRequestMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            if (sender == null)
                return;
            ServerWorld world = sender.getServerWorld();
            //noinspection deprecation
            if (world.isBlockLoaded(msg.pos)) {
                Networking.updateTE(PacketDistributor.PLAYER.with(() -> sender), world, msg.pos);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
