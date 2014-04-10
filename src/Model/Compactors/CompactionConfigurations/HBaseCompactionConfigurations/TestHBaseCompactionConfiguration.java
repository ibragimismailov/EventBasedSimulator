package Model.Compactors.CompactionConfigurations.HBaseCompactionConfigurations;

import Model.Configuration;

/**
 * HBase compaction algorithm with some test configurations
 * @author ibra
 */
public class TestHBaseCompactionConfiguration extends HBaseCompactionConfiguration {

  /**
   * creates and initializes TestHBaseCompactionConfiguration object
   */
  public TestHBaseCompactionConfiguration() {
    this.setMajorCompactionsGap(Long.toString(1000L * 28L * Configuration.MS_PER_DAY));
    this.setMajorCompactionsJitter(Double.toString(0.33));

    this.setCompactionMinFiles(Long.toString(3L));
    this.setCompactionMaxFiles(Long.toString(12L));
    this.setCompactionMinBytes(Long.toString(0L));
    this.setCompactionMaxBytes(Long.toString(Long.MAX_VALUE));

    this.setCompactionRatio(Double.toString(1.0));
    this.setThrottle(Long.toString(1000000000L));
  }
}
