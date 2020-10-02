package de.castcrafter.travel_anchors;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class WorldData extends WorldSavedData implements Supplier {

    public CompoundNBT data = new CompoundNBT();

    public WorldData()
    {
        super(TravelAnchors.MODID);
    }

    public WorldData(String name)
    {
        super(name);
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        data = nbt.getCompound("DatenNamen");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        nbt.put("DatenNamen", data);
        return nbt;
    }

    public static WorldData get(ServerWorld world) {
        DimensionSavedDataManager storage = world.getSavedData();
        Supplier<WorldData> sup = new WorldData();
        WorldData saver = (WorldData) storage.getOrCreate(sup, TravelAnchors.MODID);

        if (saver == null)
        {
            saver = new WorldData();
            storage.set(saver);
        }
        return saver;
    }

    @Override
    public Object get()
    {
        return this;
    }
}

