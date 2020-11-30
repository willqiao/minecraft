package com.kaupenjoe.mccourse.tileentity;

import com.kaupenjoe.mccourse.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElectrifierTile extends TileEntity implements ITickableTileEntity
{
    private final ItemStackHandler itemHandler = createHandler();

    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private int tick = 0;
    private int energyLevel = 0;

    public ElectrifierTile(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public ElectrifierTile()
    {
        this(ModTileEntities.ELECTRIFIER_TILE_ENTITY.get());
    }


    @Override
    public void tick()
    {
        tick++;
        if(tick > 10) // should be configurable
        {
            if(this.itemHandler.getStackInSlot(0).getItem() == Items.DIAMOND && this.energyLevel < 64)
            {
                itemHandler.extractItem(0, 1, false); // "deletes" the item
                energyLevel++;
            }

            if(this.itemHandler.getStackInSlot(1).getItem() == ModItems.COPPER_WIRE.get()
                && this.energyLevel > 0 && this.itemHandler.getStackInSlot(2).getCount() < 64)
            {
                itemHandler.extractItem(1, 1, false);
                itemHandler.insertItem(2, new ItemStack(Items.EMERALD, 1), false);
                energyLevel--;
            }

            tick = 0;
        }
    }

    public int getEnergyLevel()
    {
        return energyLevel;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag)
    {
        itemHandler.deserializeNBT(tag.getCompound("inv"));

        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        tag.put("inv", itemHandler.serializeNBT());

        return super.write(tag);
    }

    private ItemStackHandler createHandler()
    {
        return new ItemStackHandler(3)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                switch (slot)
                {
                    case 0: return stack.getItem() == Items.DIAMOND;
                    case 1: return stack.getItem() == ModItems.COPPER_WIRE.get();
                    case 2: return stack.getItem() == Items.EMERALD;
                    default: return false;
                }
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
            {
                if(!isItemValid(slot, stack))
                {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return handler.cast();
        }

        return super.getCapability(capability, side);
    }
}
