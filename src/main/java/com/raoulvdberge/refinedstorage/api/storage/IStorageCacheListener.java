package com.raoulvdberge.refinedstorage.api.storage;

import com.raoulvdberge.refinedstorage.api.util.StackListResult;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Listens for storage cache changes.
 *
 * @param <T> the type
 */
public interface IStorageCacheListener<T> {
    /**
     * Called when this storage cache listener is attached to a storage cache.
     */
    void onAttached();

    /**
     * Called when the cache invalidates.
     */
    void onInvalidated();

    /**
     * Called when the storage cache changes.
     *
     * @param delta the delta
     */
    void onChanged(@Nonnull StackListResult<T> delta);

    /**
     * Called when the storage cache changes.
     *
     * @param deltas a list of deltas
     */
    void onChangedBulk(@Nonnull List<StackListResult<T>> deltas);
}
