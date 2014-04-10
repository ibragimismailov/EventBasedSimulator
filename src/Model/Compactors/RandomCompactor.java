package Model.Compactors;

import Model.Compactors.CompactionConfigurations.RandomCompactionConfiguration;
import Model.HBaseElements.StoreFileCollection;
import Tools.RandomGenerator;

/**
 * very simple random compaction algorithm
 * @author ibra
 */
class RandomCompactor extends AbstractCompactor {

  /**
   * configuration of this compaction algorithm
   */
  private final RandomCompactionConfiguration compactionConfiguration;

  @Override
  public RandomCompactionConfiguration getCompactionConfiguration() {
    return this.compactionConfiguration;
  }

  /**
   * creates and initializes object
   * @param compactionConfiguration - this compactor configuration
   */
  RandomCompactor(final RandomCompactionConfiguration compactionConfiguration) {
    this.compactionConfiguration = compactionConfiguration;
  }

  @Override
  protected StoreFileCollection selectFilesToCompact(final StoreFileCollection storeFiles) {
    if (storeFiles.size() <= this.compactionConfiguration.getMaxFiles()) {
      return new StoreFileCollection();
    }

    final StoreFileCollection toCompact = new StoreFileCollection();
    while (toCompact.size() < this.compactionConfiguration.getFilesToCompact()) {
      int id = RandomGenerator.getRandomInt(storeFiles.size() - toCompact.size());
      for (int i = 0; i < storeFiles.size(); i++) {
        if (!toCompact.contains(storeFiles.get(i))) {
          id--;
        }
        if (id == -1) {
          toCompact.add(storeFiles.get(i));
          break;
        }
      }
    }
    return toCompact;
  }
}