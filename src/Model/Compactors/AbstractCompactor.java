package Model.Compactors;

import Model.Compactors.CompactionConfigurations.AbstractCompactionConfiguration;
import Model.Events.FlushEvent;
import Model.Events.GlobalQueue;
import Model.HBaseElements.StoreFile;
import Model.HBaseElements.StoreFileCollection;

/**
 * AbstractCompactor is an abstract parent class other compactors classes. It does minor and major compactions.
 * You should inherit from this class if you want create some new compaction algorithm. 
 * AbstractCompactor contains compaction configuration object.
 * @author ibra
 */
public abstract class AbstractCompactor {

  /**
   * methods selects what storeFiles should be compacted during minor compaction
   * @param storeFiles - all storeFiles of store
   * @return collection of StoreFiles to compact
   */
  protected abstract StoreFileCollection selectFilesToCompact(StoreFileCollection storeFiles);

  /**
   * @return configuration of this compaction algorithm
   */
  public abstract AbstractCompactionConfiguration getCompactionConfiguration();

  /**
   * initialize GlobalQueue with initial events of this compactor
   * @param storeID
   */
  public void compactorInitQueue(int storeID) {
    GlobalQueue.queue.add(new FlushEvent(storeID, 0));
    return;
  }

  /**
   * method does minor compaction
   * @param storeFiles - collection of storeFiles of store
   * @return CompactionResult object - description and result of compaction
   */
  public final CompactionResult doCompaction(final StoreFileCollection storeFiles) {
    final StoreFileCollection toCompact = this.selectFilesToCompact(storeFiles);
    return this.compact(storeFiles, toCompact);
  }

  /**
   * method does major compaction
   * @param storeFiles - collection of storeFiles of store
   * @return CompactionResult object - description and result of compaction
   */
  public final CompactionResult forceMajorCompaction(final StoreFileCollection storeFiles) {
    return this.compact(storeFiles, new StoreFileCollection(storeFiles));
  }

  /**
   * this method is performing a compaction
   * @param storeFiles - collection of storeFiles of store
   * @param toCompact - collection of storeFiles that are to be compacted
   */
  private CompactionResult compact(final StoreFileCollection storeFiles,
      final StoreFileCollection toCompact) {
    if (toCompact.isEmpty()) {
      return CompactionResult.NOT_PERFORMED;
    }

    long compactionSize = 0;
    for (final StoreFile storeFile : toCompact) {
      compactionSize += storeFile.getBytesSize();
    }

    for (StoreFile storeFile : toCompact) {
      storeFiles.remove(storeFile);
    }

    final StoreFile compacted = StoreFile.getEmpty();
    for (StoreFile storeFile : toCompact) {
      compacted.mergeWith(storeFile);
    }

    return new CompactionResult(true, compactionSize, compacted.getBytesSize(), compacted);
  }
}