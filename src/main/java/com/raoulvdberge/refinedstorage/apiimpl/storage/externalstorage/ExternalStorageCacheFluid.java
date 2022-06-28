package com.raoulvdberge.refinedstorage.apiimpl.storage.externalstorage;

import com.raoulvdberge.refinedstorage.api.network.INetwork;
import com.raoulvdberge.refinedstorage.api.util.IComparer;
import com.raoulvdberge.refinedstorage.api.util.StackListEntry;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ExternalStorageCacheFluid {
    private List<StackListEntry<FluidStack>> cache;

    public void update(INetwork network, @Nullable IFluidHandler handler, List<StackListEntry<FluidStack>> entries) {
        if (handler == null) {
            return;
        }

        if (cache == null) {
            cache = new ArrayList<>();

            for (StackListEntry<FluidStack> entry : entries) {
                FluidStack stack = entry.getStack();
                if (stack != null) stack = stack.copy();
                cache.add(new StackListEntry<>(stack, entry.getCount()));
            }

            return;
        }

        for (int i = 0; i < entries.size(); i++) {
            StackListEntry<FluidStack> actual = entries.get(i);

            if (i >= cache.size()) { // ENLARGED
                if (actual != null) {
                    FluidStack actualStack = actual.getStack();
                    network.getFluidStorageCache().add(actualStack, actual.getCount(), true);

                    cache.add(new StackListEntry<>(actualStack.copy(), actual.getCount()));
                }

                continue;
            }

            StackListEntry<FluidStack> cached = cache.get(i);
            if (actual == null && cached == null) { // NONE
                continue;
            }

            if (actual == null) { // REMOVED
                network.getFluidStorageCache().remove(cached.getStack(), cached.getCount(), true);

                cache.set(i, null);
            } else if (cached == null) { // ADDED
                FluidStack actualStack = actual.getStack();
                network.getFluidStorageCache().add(actualStack, actual.getCount(), true);

                cache.set(i, new StackListEntry<>(actualStack.copy(), actual.getCount()));
            } else if (!API.instance().getComparer().isEqual(actual.getStack(), cached.getStack(), IComparer.COMPARE_NBT)) { // CHANGED
                FluidStack actualStack = actual.getStack();
                network.getFluidStorageCache().remove(cached.getStack(), cached.getCount(), true);
                network.getFluidStorageCache().add(actualStack, actual.getCount(), true);

                cache.set(i, new StackListEntry<>(actualStack.copy(), actual.getCount()));
            } else if (actual.getCount() > cached.getCount()) { // COUNT_CHANGED
                network.getFluidStorageCache().add(actual.getStack(), actual.getCount() - cached.getCount(), true);

                cached.setCount(actual.getCount());
            } else if (actual.getCount() < cached.getCount()) { // COUNT_CHANGED
                network.getFluidStorageCache().remove(actual.getStack(), cached.getCount() - actual.getCount(), true);

                cached.setCount(actual.getCount());
            }
        }

        if (cache.size() > entries.size()) { // SHRUNK
            for (int i = cache.size() - 1; i >= handler.getTankProperties().length; --i) { // Reverse order for the remove call.
                StackListEntry<FluidStack> cached = cache.get(i);

                if (cached != null) {
                    network.getFluidStorageCache().remove(cached.getStack(), cached.getCount(), true);
                }

                cache.remove(i);
            }
        }

        network.getFluidStorageCache().flush();
    }
}
