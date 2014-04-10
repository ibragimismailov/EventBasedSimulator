package Model.Compactors;

import Model.Compactors.CompactionConfigurations.LevelBasedCompactionConfiguration;
import Model.HBaseElements.StoreFileCollection;

/**
 * Level-based compaction algorithm:
 * During memStore flushes StoreFiles are flushed into Level0
 * then, when amount of files in i-th Level exceeds limit, we take all files in 
 * i-th level, compact them and put compacted file into (i+1)-th Level 
 * @author ibra
 */
class LevelBasedCompactor extends AbstractCompactor {

  /**
   * configuration of this compaction algorithm
   */
  private final LevelBasedCompactionConfiguration compactionConfiguration;

  @Override
  public LevelBasedCompactionConfiguration getCompactionConfiguration() {
    return this.compactionConfiguration;
  }

  /**
   * creates and initializes object
   * @param compactionConfiguration - this compactor configuration
   */
  LevelBasedCompactor(final LevelBasedCompactionConfiguration compactionConfiguration) {
    this.compactionConfiguration = compactionConfiguration;
  }

  @Override
  protected StoreFileCollection selectFilesToCompact(final StoreFileCollection storeFiles) {
    for (int i = 0; i + this.compactionConfiguration.getFilesPerLevel() <= storeFiles.size(); i++) {
      int start = i;
      int end = (int) (i + this.compactionConfiguration.getFilesPerLevel() - 1);
      if (this.isAboutTheSame(storeFiles.get(start).getBytesSize(), storeFiles.get(end)
          .getBytesSize())) {
        return storeFiles.subList(start, end + 1);
      }
    }
    return new StoreFileCollection();
  }

  /**
   * @param fileSize1 - size of file1
   * @param fileSize2 - size of file2
   * @return if two files have about the same size
   */
  private boolean isAboutTheSame(final long fileSize1, final long fileSize2) {
    return Math.max(fileSize1, fileSize2) / Math.min(fileSize1, fileSize2) < this.compactionConfiguration
        .getFilesSimilarityRatio();
  }

}
