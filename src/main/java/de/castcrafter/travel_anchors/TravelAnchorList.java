package de.castcrafter.travel_anchors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TravelAnchorList extends WorldSavedData {

    public static TravelAnchorList get(World world) {
        if (!world.isRemote) {
            DimensionSavedDataManager storage = ((ServerWorld) world).getSavedData();
            return storage.getOrCreate(TravelAnchorList::new, TravelAnchors.MODID);
        } else {
            return new TravelAnchorList();
        }
    }

    public TravelAnchorList() {
        super(TravelAnchors.MODID);
    }

    public TravelAnchorList(String name) {
        super(name);
    }

    private final HashMap<BlockPos, String> anchors = new HashMap<>();

    @Override
    public void read(@Nonnull CompoundNBT nbt) {
        anchors.clear();
        if (nbt.contains("anchors", Constants.NBT.TAG_LIST)) {
            ListNBT list = nbt.getList("anchors", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundNBT entryNBT = new CompoundNBT();
                BlockPos pos = new BlockPos(entryNBT.getInt("x"), entryNBT.getInt("y"), entryNBT.getInt("z")).toImmutable();
                anchors.put(pos, entryNBT.getString("name"));
            }
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        for (Map.Entry<BlockPos, String> entry : anchors.entrySet()) {
            CompoundNBT entryNBT = new CompoundNBT();
            entryNBT.putInt("x", entry.getKey().getX());
            entryNBT.putInt("y", entry.getKey().getY());
            entryNBT.putInt("z", entry.getKey().getZ());
            entryNBT.putString("name", entry.getValue());
            list.add(entryNBT);
        }
        nbt.put("anchors", list);
        return nbt;
    }

    public void setAnchor(BlockPos pos, @Nullable String name) {
        if (name == null || name.trim().isEmpty()) {
            anchors.remove(pos.toImmutable());
        } else {
            anchors.put(pos.toImmutable(), name);
        }
    }

    public String getAnchor(BlockPos pos) {
        return anchors.getOrDefault(pos.toImmutable(), null);
    }

    public Stream<Pair<BlockPos, String>> getAnchorsAround(Vector3d pos, double maxDistanceSq) {
        return anchors.entrySet().stream()
                .filter(entry -> entry.getKey().distanceSq(pos.x, pos.y, pos.z, true) < maxDistanceSq)
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()));
    }
}

