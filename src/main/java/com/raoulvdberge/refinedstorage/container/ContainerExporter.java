package com.raoulvdberge.refinedstorage.container;

import com.raoulvdberge.refinedstorage.container.slot.filter.SlotFilter;
import com.raoulvdberge.refinedstorage.container.slot.filter.SlotFilterFluid;
import com.raoulvdberge.refinedstorage.inventory.item.ItemHandlerUpgrade;
import com.raoulvdberge.refinedstorage.item.ItemUpgrade;
import com.raoulvdberge.refinedstorage.tile.TileExporter;
import com.raoulvdberge.refinedstorage.tile.config.IType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerExporter extends ContainerBase {

    private boolean hasRegulatorUpgrade;

    private final TileExporter exporter;

    public ContainerExporter(TileExporter exporter, EntityPlayer player) {
        super(exporter, player);
        this.exporter = exporter;

        //the client node of the exporter does not always know about the upgrades it has, that's why those other methods
        // are overriden
        this.hasRegulatorUpgrade = hasRegulatorUpgrade();

        initSlots();
    }

    private boolean hasRegulatorUpgrade() {
        return exporter.getNode().getUpgrades().hasUpgrade(ItemUpgrade.TYPE_REGULATOR);
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        //this is called when the actual slots are sent over
        detectRegulatorUpgradeChange();

        super.putStackInSlot(slotID, stack);
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int id, int dragType, @Nonnull ClickType clickType, @Nonnull EntityPlayer player) {
        //called when putting anything in the filters
        detectRegulatorUpgradeChange();

        return super.slotClick(id, dragType, clickType, player);
    }

    private void detectRegulatorUpgradeChange() {
        boolean newState = hasRegulatorUpgrade();
        if (this.hasRegulatorUpgrade != newState) {
            this.initSlots();
            this.hasRegulatorUpgrade = newState;
        }
    }

    public void initSlots() {
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();

        this.transferManager.clearTransfers();

        TileExporter exporter = (TileExporter) getTile();
        ItemHandlerUpgrade upgrades = exporter.getNode().getUpgrades();

        for (int i = 0; i < 4; ++i) {
            addSlotToContainer(new SlotItemHandler(upgrades, i, 187, 6 + (i * 18)));
        }

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new SlotFilter(
                    exporter.getNode().getItemFilters(),
                    i,
                    8 + (18 * i),
                    20,
                    upgrades.hasUpgrade(ItemUpgrade.TYPE_REGULATOR) ? SlotFilter.FILTER_ALLOW_SIZE : 0
            ).setEnableHandler(() -> exporter.getNode().getType() == IType.ITEMS));
        }

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new SlotFilterFluid(
                    exporter.getNode().getFluidFilters(),
                    i,
                    8 + (18 * i),
                    20,
                    upgrades.hasUpgrade(ItemUpgrade.TYPE_REGULATOR) ? SlotFilterFluid.FILTER_ALLOW_SIZE : 0
            ).setEnableHandler(() -> exporter.getNode().getType() == IType.FLUIDS));
        }

        addPlayerInventory(8, 55);

        transferManager.addBiTransfer(getPlayer().inventory, upgrades);
        transferManager.addFilterTransfer(getPlayer().inventory, exporter.getNode().getItemFilters(),
                exporter.getNode().getFluidFilters(), exporter.getNode()::getType);
    }
}
