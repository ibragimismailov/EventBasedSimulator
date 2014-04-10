package Model.Compactors;

import Model.Compactors.CompactionConfigurations.IbraCompactionConfiguration;
import Model.HBaseElements.StoreFileCollection;

/**
 * My compaction algorithm
 * It tries to compact as many almost-equal files as possible.
 * Algorithm tries to select the biggest set of files that fits this condition:
 * maxFile/minFile < this.compactionConfiguration.getFilesSimilarityRatio()
 * @author ibra
 */
class IbraCompactor extends AbstractCompactor {

  /**
   * configuration of this compaction algorithm
   */
  private final IbraCompactionConfiguration compactionConfiguration;

  @Override
  public IbraCompactionConfiguration getCompactionConfiguration() {
    return this.compactionConfiguration;
  }

  /**
   * creates and initializes object
   * @param compactionConfiguration - this compactor configuration
   */
  public IbraCompactor(final IbraCompactionConfiguration compactionConfiguration) {
    this.compactionConfiguration = compactionConfiguration;
  }

  @Override
  protected StoreFileCollection selectFilesToCompact(final StoreFileCollection storeFiles) {
    /**
     * we select the longest sequence of files that differ in size not too much
     * if storeFiles[start] doestn't differ from storeFiles[i] in size, we 
     * sequence [start, i] can be taken (StoreFilesCollection elements are sorted 
     * by decreasing elements' bytesSize)
     */
    int bestStart = 0;
    int bestCount = 0;
    for (int start = 0; start < storeFiles.size(); start++) {
      int count = 1;
      for (int i = start + 1; i < storeFiles.size(); i++) {
        if (this.isok(storeFiles, start, i)) {
          count++;
        } else {
          break;
        }
      }
      if (count > bestCount) {
        bestCount = count;
        bestStart = start;
      }
    }

    final StoreFileCollection toCompact = new StoreFileCollection();
    for (int i = bestStart; i < bestStart + bestCount; i++) {
      toCompact.add(storeFiles.get(i));
    }

    if (toCompact.size() < this.compactionConfiguration.getMinFilesToCompact()) {
      return new StoreFileCollection();
    }

    return toCompact;
  }

  /**
   * method checks that files in interval [start, i] from storeFiles list have similar size
   * @param storeFiles - all storeFiles of Store
   * @param start - start index of range
   * @param i - end index of range
   * @return if files [start, i] have similar size
   */
  private boolean isok(final StoreFileCollection storeFiles, final int start, final int i) {
    final long a = storeFiles.get(start).getBytesSize();
    final long b = storeFiles.get(i).getBytesSize();
    return b * this.compactionConfiguration.getFilesSimilarityRatio() > a;
  }

}
