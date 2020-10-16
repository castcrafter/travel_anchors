package de.castcrafter.travel_anchors.network;

import de.castcrafter.travel_anchors.TravelAnchorList;
import de.castcrafter.travel_anchors.TravelAnchors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Networking {

    private static final String PROTOCOL_VERSION = "2";
    private static int discriminator = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TravelAnchors.MODID, "netchannel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        register(new TeUpdateSerializer(), () -> TeUpdateHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
        register(new TeRequestSerializer(), () -> TeRequestHandler::handle, NetworkDirection.PLAY_TO_SERVER);
        register(new AnchorNameChangeSerializer(), () -> AnchorNameChangeHandler::handle, NetworkDirection.PLAY_TO_SERVER);
        register(new AnchorListUpdateSerializer(), () -> AnchorListUpdateHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
    }

    private static <T> void register(PacketSerializer<T> serializer, Supplier<BiConsumer<T, Supplier<NetworkEvent.Context>>> handler, NetworkDirection direction) {
        Objects.requireNonNull(direction);
        BiConsumer<T, Supplier<NetworkEvent.Context>> realHandler;
        if (direction == NetworkDirection.PLAY_TO_CLIENT || direction == NetworkDirection.LOGIN_TO_CLIENT) {
            realHandler = DistExecutor.unsafeRunForDist(() -> handler, () -> () -> (msg, ctx) -> {});
        } else {
            realHandler = handler.get();
        }
        INSTANCE.registerMessage(discriminator++, serializer.messageClass(), serializer::encode, serializer::decode, realHandler, Optional.of(direction));

    }

    public static void sendNameChange(World world, BlockPos pos, String name) {
        if (world.isRemote) {
            INSTANCE.sendToServer(new AnchorNameChangeSerializer.AnchorNameChangeMessage(pos, name));
        }
    }

    public static void updateTE(World world, BlockPos pos) {
        if (!world.isRemote) {
            updateTE(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), world, pos);
        }
    }

    static void updateTE(PacketDistributor.PacketTarget target, World world, BlockPos pos) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te == null)
                return;
            CompoundNBT nbt = te.getUpdateTag();
            //noinspection ConstantConditions
            if (nbt == null)
                return;
            ResourceLocation id = te.getType().getRegistryName();
            if (id == null)
                return;
            INSTANCE.send(target, new TeUpdateSerializer.TeUpdateMessage(pos, id, nbt));
        }
    }

    public static void requestTE(World world, BlockPos pos) {
        if (world.isRemote) {
            INSTANCE.sendToServer(new TeRequestSerializer.TeRequestMessage(pos));
        }
    }

    public static void updateTravelAnchorList(World world, @Nullable TravelAnchorList list) {
        if (!world.isRemote) {
            if (list == null) {
                list = TravelAnchorList.get(world);
            }
            INSTANCE.send(PacketDistributor.DIMENSION.with(world::getDimensionKey), new AnchorListUpdateSerializer.AnchorListUpdateMessage(list.write(new CompoundNBT())));
        }
    }

    public static void updateTravelAnchorList(PlayerEntity player) {
        if (!player.getEntityWorld().isRemote && player instanceof ServerPlayerEntity) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new AnchorListUpdateSerializer.AnchorListUpdateMessage(TravelAnchorList.get(player.getEntityWorld()).write(new CompoundNBT())));
        }
    }
}
