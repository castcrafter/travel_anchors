package de.castcrafter.travel_anchors.network;

import de.castcrafter.travel_anchors.TravelAnchorList;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.network.NetworkX;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class Networking extends NetworkX {

    public Networking(ModX mod) {
        super(mod);
    }

    @Override
    protected String getProtocolVersion() {
        return "4";
    }

    protected void registerPackets() {
        this.register(new AnchorNameChangeSerializer(), () -> AnchorNameChangeHandler::handle, NetworkDirection.PLAY_TO_SERVER);
        this.register(new AnchorListUpdateSerializer(), () -> AnchorListUpdateHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
        this.register(new ClientEventSerializer(), () -> ClientEventHandler::handle, NetworkDirection.PLAY_TO_SERVER);
    }

    public void sendNameChange(World world, BlockPos pos, String name) {
        if (world.isRemote) {
            this.instance.sendToServer(new AnchorNameChangeSerializer.AnchorNameChangeMessage(pos, name));
        }
    }

    public void updateTravelAnchorList(World world, @Nullable TravelAnchorList list) {
        if (!world.isRemote) {
            if (list == null) {
                list = TravelAnchorList.get(world);
            }
            this.instance.send(PacketDistributor.DIMENSION.with(world::getDimensionKey), new AnchorListUpdateSerializer.AnchorListUpdateMessage(list.write(new CompoundNBT())));
        }
    }

    public void updateTravelAnchorList(PlayerEntity player) {
        if (!player.getEntityWorld().isRemote && player instanceof ServerPlayerEntity) {
            this.instance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new AnchorListUpdateSerializer.AnchorListUpdateMessage(TravelAnchorList.get(player.getEntityWorld()).write(new CompoundNBT())));
        }
    }

    public void sendClientEventToServer(World world, ClientEventSerializer.ClientEvent event) {
        if (world.isRemote) {
            this.instance.sendToServer(event);
        }
    }
}
