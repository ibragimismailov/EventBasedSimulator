package Model.Compactors;

import Model.Configuration;
import Model.Compactors.CompactionConfigurations.HBaseCompactionConfigurations.HBaseCompactionConfiguration;
import Model.Events.GlobalQueue;
import Model.Events.MajorCompactionEvent;
import Model.HBaseElements.StoreFileCollection;
import Tools.Generator;

/**
 * native compaction algorithm used in HBase
 * @author ibra
 */
class HBaseCompactor extends AbstractCompactor {

  /**
   * configuration of this compaction algorithm
   */
  private final HBaseCompactionConfiguration compactionConfiguration;

  @Override
  public HBaseCompactionConfiguration getCompactionConfiguration() {
    return this.compactionConfiguration;
  }

  @Override
  public void compactorInitQueue(int storeID) {
    super.compactorInitQueue(storeID);

    /**
     * invokes major compactions
     */
    for (long j = 0; j < Configuration.INSTANCE.getSimulationTimeDays() * Configuration.MS_PER_DAY; j += Generator
        .getGenerator().getMajorCompactionGap(
          HBaseCompactor.this.compactionConfiguration.getMajorCompactionsGap(),
          HBaseCompactor.this.compactionConfiguration.getMajorCompactionsJitter())) {
      GlobalQueue.queue.add(new MajorCompactionEvent(storeID, j));
    }
    return;
  }

  /**
   * creates and initializes object
   * @param compactionConfiguration - this compactor configuration
   */
  public HBaseCompactor(final HBaseCompactionConfiguration compactionConfiguration) {
    this.compactionConfiguration = compactionConfiguration;
  }

  @Override
  protected StoreFileCollection selectFilesToCompact(final StoreFileCollection storeFiles) {
    StoreFileCollection toCompact = new StoreFileCollection(storeFiles);
    toCompact = this.skipLargeFiles(toCompact);
    toCompact = this.applyCompactionPolicy(toCompact);
    toCompact = this.checkMinFilesCriteria(toCompact);
    toCompact = this.removeExcessFiles(toCompact);

    return toCompact;
  }

  /**
   * method removes too large storeFiles
   * @param storeFiles - collection of storeFiles
   * @return collection of storeFiles, without too large storeFiles
   */
  private StoreFileCollection skipLargeFiles(final StoreFileCollection storeFiles) {
    int pos = 0;
    while (pos < storeFiles.size()
        && storeFiles.get(pos).getBytesSize() > this.compactionConfiguration
            .getCompactionMaxBytes()) {
      pos++;
    }

    if (pos > 0) {
      return storeFiles.subList(pos, storeFiles.size());
    }

    return storeFiles;
  }

  private static boolean isBigger(double a, double b) {
    return a > b + 1e-6;
  }

  /**
   * methods chooses storeFiles for compaction due to configuration
   * @param storeFiles - collection of storeFiles
   * @return collection of storeFiles to compact
   */
  private StoreFileCollection applyCompactionPolicy(StoreFileCollection storeFiles) {
    if (storeFiles.isEmpty()) {
      return storeFiles;
    }

    final double r = this.compactionConfiguration.getCompactionRatio();

    // get store file sizes for incremental compacting selection.
    final int countOfFiles = storeFiles.size();
    final long[] fileSizes = new long[countOfFiles];
    final long[] sumSize = new long[countOfFiles];
    for (int i = countOfFiles - 1; i >= 0; --i) {
      fileSizes[i] = storeFiles.get(i).getBytesSize();
      // calculate the sum of fileSizes[i,i+maxFilesToCompact-1) for algo
      final int tooFar = (int) (i + this.compactionConfiguration.getCompactionMaxFiles() - 1);
      sumSize[i] = fileSizes[i] + ((i + 1 < countOfFiles) ? sumSize[i + 1] : 0)
          - ((tooFar < countOfFiles) ? fileSizes[tooFar] : 0);
    }

    int start = 0;
    while (countOfFiles - start >= this.compactionConfiguration.getCompactionMinFiles()
        && isBigger(fileSizes[start], Math.max(
          this.compactionConfiguration.getCompactionMinBytes(), (long) (sumSize[start + 1] * r)))) {
      ++start;
    }

    storeFiles = storeFiles.subList(start, countOfFiles);

    return storeFiles;
  }

  /**
   * method checks if storeFiles contains enough elements
   * @param storeFiles - collection of storeFiles
   * @return empty collection if storeFiles contains to few elements,
   *   else return collection from argument list
   */
  private StoreFileCollection checkMinFilesCriteria(final StoreFileCollection storeFiles) {
    // if storeFiles contains not enough files, then we return
    // empty StoreFilesCollection - that means that we won't do compaction
    if (storeFiles.size() < this.compactionConfiguration.getCompactionMinFiles()) {
      storeFiles.clear();
    }
    return storeFiles;
  }

  /**
   * if storeFiles collection contains too many elements, method removes excess storeFiles
   * @param storeFiles - collection of StoreFiles
   * @return collection with valid size
   */
  private StoreFileCollection removeExcessFiles(StoreFileCollection storeFiles) {
    final long excess = storeFiles.size() - this.compactionConfiguration.getCompactionMaxFiles();
    if (excess > 0) {
      storeFiles = storeFiles
          .subList(0, (int) this.compactionConfiguration.getCompactionMaxFiles());
    }
    return storeFiles;
  }
}
