package com.raoulvdberge.refinedstorage.container.slot.grid;

import com.raoulvdberge.refinedstorage.api.network.grid.IGrid;
import com.raoulvdberge.refinedstorage.container.ContainerGrid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotGridCraftingResult extends SlotCrafting {
    private ContainerGrid container;
    private IGrid grid;

    public SlotGridCraftingResult(ContainerGrid container, PlayerEntity player, IGrid grid, int inventoryIndex, int x, int y) {
        super(player, grid.getCraftingMatrix(), grid.getCraftingResult(), inventoryIndex, x, y);

        this.container = container;
        this.grid = grid;
    }

    @Override
    @Nonnull
    public ItemStack onTake(PlayerEntity player, @Nonnull ItemStack stack) {
        onCrafting(stack);

        if (!player.getEntityWorld().isRemote) {
            grid.onCrafted(player);
        }

        return ItemStack.EMPTY;
    }
}
