package com.raoulvdberge.refinedstorage.gui.grid.sorting;

import com.raoulvdberge.refinedstorage.api.network.grid.IGrid;
import com.raoulvdberge.refinedstorage.gui.grid.stack.IGridStack;

public class GridSorterQuantity implements IGridSorter {
    @Override
    public boolean isApplicable(IGrid grid) {
        return grid.getSortingType() == IGrid.SORTING_TYPE_QUANTITY;
    }

    @Override
    public int compare(IGridStack left, IGridStack right, GridSorterDirection sortingDirection) {
        long leftSize = left.getQuantity();
        long rightSize = right.getQuantity();

        if (leftSize != rightSize) {
            if (sortingDirection == GridSorterDirection.ASCENDING) {
                return (leftSize > rightSize) ? 1 : -1;
            } else if (sortingDirection == GridSorterDirection.DESCENDING) {
                return (rightSize > leftSize) ? 1 : -1;
            }
        }

        return 0;
    }
}
