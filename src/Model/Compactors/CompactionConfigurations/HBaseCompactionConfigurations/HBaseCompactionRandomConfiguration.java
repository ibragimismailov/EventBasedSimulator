package Model.Compactors.CompactionConfigurations.HBaseCompactionConfigurations;

import Model.Configuration;
import Tools.RandomGenerator;

/**
 * HBase compaction algorithm random configurations
 * @author ibra
 */
public final class HBaseCompactionRandomConfiguration extends HBaseCompactionConfiguration {

  /**
   * creates and initializes HBaseCompactionRandomConfiguration object
   */
  public HBaseCompactionRandomConfiguration() {

    this.setMajorCompactionsGap(Long.toString(1000L * Configuration.MS_PER_DAY));
    this.setMajorCompactionsJitter(Double.toString(0.2));

    this.setCompactionMinFiles(Long.toString(RandomGenerator.getRandomLong(3, 12)));
    this.setCompactionMaxFiles(Long.toString(RandomGenerator.getRandomLong(5, 20)));
    this.setCompactionMinBytes(Long.toString(0));
    this.setCompactionMaxBytes(Long.toString(Long.MAX_VALUE));

    this.setCompactionRatio(Double.toString(RandomGenerator.getRandomDouble(0.2, 3.0)));
    this.setThrottle(Long.toString(2 * this.getCompactionMaxFiles()
        * Configuration.INSTANCE.getMemstoreBytesSize()));

    this.setTitle("RandomHBaseCompactionConfiguration " + "( minFiles "
        + this.getCompactionMinFiles() + ")" + "( maxFiles " + this.getCompactionMaxFiles() + ")"
        + "( ratio " + this.getCompactionRatio() + ")");
  }
}
