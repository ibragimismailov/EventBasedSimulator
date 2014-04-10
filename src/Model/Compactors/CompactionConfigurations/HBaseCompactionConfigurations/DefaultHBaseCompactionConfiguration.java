package Model.Compactors.CompactionConfigurations.HBaseCompactionConfigurations;

import Model.Configuration;

/**
 * Default HBase compaction algorithm configurations
 * @author ibra
 */
public final class DefaultHBaseCompactionConfiguration extends HBaseCompactionConfiguration {

  /**
   * creates and initializes DefaultHBaseCompactionConfiguration object
   */
  public DefaultHBaseCompactionConfiguration() {
    this.setMajorCompactionsGap(Long.toString(86400000L));
    this.setMajorCompactionsJitter(Double.toString(0.2));

    this.setCompactionMinFiles(Long.toString(3));
    this.setCompactionMaxFiles(Long.toString(10));
    this.setCompactionMinBytes(Long.toString(0));
    this.setCompactionMaxBytes(Long.toString(Long.MAX_VALUE));

    this.setCompactionRatio(Double.toString(1.2));
    this.setThrottle(Long.toString(2 * this.getCompactionMaxFiles()
        * Configuration.INSTANCE.getMemstoreBytesSize()));

    this.setTitle("DefaultHBaseCompactionConfiguration");
  }
}
