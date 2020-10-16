package de.castcrafter.travel_anchors.network;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TeUpdateHandler {

    public static void handle(TeUpdateSerializer.TeUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            if (world == null)
                return;
            TileEntity te = world.getTileEntity(msg.pos);
            if (te != null && msg.id.equals(te.getType().getRegistryName())) {
                te.handleUpdateTag(world.getBlockState(msg.pos), msg.nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
