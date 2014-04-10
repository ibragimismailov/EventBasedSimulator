package Model.Compactors.CompactionConfigurations;

import java.util.Map;

/**
 * Level-based compaction algorithm configuration
 * See LevelBasedCompactor for clarifications
 */
public final class LevelBasedCompactionConfiguration extends AbstractCompactionConfiguration {

  /**
   * max amount of files on one level
   */
  private double filesPerLevel = 5;

  /**
   * this configuration defines how much different files can be on the same level
   */
  private long filesSimilarityRatio = 2;

  @Override
  protected Map<String, SetMethod> fillSetFields() {// @formatter:off
    final Map<String, SetMethod> res = super.fillSetFields();
    res.put("Files per level",                new SetMethod() { @Override public void set(final String value) {LevelBasedCompactionConfiguration.this.setFilesPerLevel       (value);} });
    res.put("Files similarity ratio",         new SetMethod() { @Override public void set(final String value) {LevelBasedCompactionConfiguration.this.setFilesSimilarityRatio(value);} });
    return res;
  }// @formatter:on

  @Override
  protected Map<String, GetMethod> fillGetFields() {// @formatter:off
    final Map<String, GetMethod> res = super.fillGetFields();
    res.put("Files per level",                new GetMethod() { @Override public String get() {return Double.toString(LevelBasedCompactionConfiguration.this.getFilesPerLevel       ());} });
    res.put("Files similarity ratio",         new GetMethod() { @Override public String get() {return Long  .toString(LevelBasedCompactionConfiguration.this.getFilesSimilarityRatio());} });
    return res;
  };// @formatter:on

  // ===================================================================
  // =============================<GETTERS>=============================
  // ===================================================================
  public double getFilesPerLevel() {
    return this.filesPerLevel;
  }

  public long getFilesSimilarityRatio() {
    return this.filesSimilarityRatio;
  }

  // ===================================================================
  // ============================</GETTERS>=============================
  // ===================================================================

  // ===================================================================
  // =============================<SETTERS>=============================
  // ===================================================================
  private void setFilesPerLevel(final String filesPerLevel) {
    this.filesPerLevel = Double.parseDouble(filesPerLevel);
  }

  private void setFilesSimilarityRatio(final String filesSimilarityRatio) {
    this.filesSimilarityRatio = Long.parseLong(filesSimilarityRatio);
  }
  // ===================================================================
  // ============================</SETTERS>=============================
  // ===================================================================
}