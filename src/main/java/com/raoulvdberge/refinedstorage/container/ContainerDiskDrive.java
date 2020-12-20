package com.raoulvdberge.refinedstorage.container;

import com.raoulvdberge.refinedstorage.container.slot.filter.SlotFilter;
import com.raoulvdberge.refinedstorage.container.slot.filter.SlotFilterFluid;
import com.raoulvdberge.refinedstorage.tile.TileDiskDrive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerDiskDrive extends ContainerBase {
    public ContainerDiskDrive(TileDiskDrive diskDrive, EntityPlayer player) {
        super(diskDrive, player);

        int x = 80;
        int y = 54;

        for (int i = 0; i < 8; ++i) {
            addSlotToContainer(new SlotItemHandler(diskDrive.getNode().getDisks(), i, x + ((i % 2) * 18), y + Math.floorDiv(i, 2) * 18));
        }

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new SlotFilter(diskDrive.getNode().getConfig().getItemFilters(), i, 8 + (18 * i), 20).setEnableHandler(() -> diskDrive.getNode().getConfig().isFilterTypeItem()));
        }

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new SlotFilterFluid(diskDrive.getNode().getConfig().getFluidFilters(), i, 8 + (18 * i), 20).setEnableHandler(() -> diskDrive.getNode().getConfig().isFilterTypeFluid()));
        }

        addPlayerInventory(8, 141);

        transferManager.addBiTransfer(player.inventory, diskDrive.getNode().getDisks());
        transferManager.addFilterTransfer(player.inventory, diskDrive.getNode().getConfig().getItemFilters(), diskDrive.getNode().getConfig().getFluidFilters(), diskDrive.getNode().getConfig()::getFilterType);
    }
}
