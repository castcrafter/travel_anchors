package de.castcrafter.travel_anchors.network;

import de.castcrafter.travel_anchors.blocks.TravelAnchorTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AnchorNameChangeHandler implements PacketHandler<AnchorNameChangeHandler.AnchorNameChangeMessage> {

    @Override
    public Class<AnchorNameChangeMessage> messageClass() {
        return AnchorNameChangeMessage.class;
    }

    @Override
    public void encode(AnchorNameChangeMessage msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeString(msg.name);
    }

    @Override
    public AnchorNameChangeMessage decode(PacketBuffer buffer) {
        return new AnchorNameChangeMessage(buffer.readBlockPos(), buffer.readString());
    }

    @Override
    public void handle(AnchorNameChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                //noinspection ConstantConditions
                ServerWorld world = ctx.get().getSender().getServerWorld();
                //noinspection deprecation
                if (world.isBlockLoaded(msg.pos)) {
                    TileEntity te = world.getTileEntity(msg.pos);
                    if (te instanceof TravelAnchorTile) {
                        ((TravelAnchorTile) te).setName(msg.name);
                        te.markDirty();
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static class AnchorNameChangeMessage {


        public BlockPos pos;
        public String name;

        public AnchorNameChangeMessage() {

        }

        public AnchorNameChangeMessage(BlockPos pos, String name) {
            this.pos = pos;
            this.name = name;
        }
    }
}
