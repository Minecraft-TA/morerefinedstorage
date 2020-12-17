package com.raoulvdberge.refinedstorage.container;

import com.raoulvdberge.refinedstorage.container.slot.filter.SlotFilter;
import com.raoulvdberge.refinedstorage.tile.TileStorage;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerStorage extends ContainerBase {
    public ContainerStorage(TileStorage storage, EntityPlayer player) {
        super(storage, player);

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new SlotFilter(storage.getNode().getConfig().getItemFilters(), i, 8 + (18 * i), 20));
        }

        addPlayerInventory(8, 141);

        transferManager.addItemFilterTransfer(player.inventory, storage.getNode().getConfig().getItemFilters());
    }
}
