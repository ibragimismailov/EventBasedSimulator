package Model.Compactors.CompactionConfigurations;

import java.util.Map;
import java.util.TreeMap;

import GUI.ConfigurationFrames.AbstractConfigurationFrame;
import GUI.ConfigurationFrames.CompactionConfigurationFrame;
import Model.AbstractConfiguration;

/**
 * Each compaction algorithm has its own compaction configuration class
 * AbstractCompactionConfiguration is a abstract parent class for other CompactionConfigurations
 * @author ibra
 */
public abstract class AbstractCompactionConfiguration extends AbstractConfiguration {
  /**
   * Title of this configuration - name for it 
   */
  private String title = this.getClass().getSimpleName();

  @Override
  protected AbstractConfigurationFrame getConfigurationFrame(final String frameTitle,
      final Map<String, GetMethod> getFields, final Map<String, SetMethod> setFields) {
    return new CompactionConfigurationFrame("Store '" + frameTitle + "' CompactionConfiguration",
        getFields, setFields);
  }

  /**
   * fill getFields map
   */
  @Override
  protected Map<String, GetMethod> fillGetFields() {// @formatter:off
    Map<String, GetMethod> res = new TreeMap<String, GetMethod> ();
    res.put("Title", new GetMethod() { @Override public String get() {return AbstractCompactionConfiguration.this.getTitle();} });
    return res;
  }// @formatter:on

  /**
   * fill setFields map
   */
  @Override
  protected Map<String, SetMethod> fillSetFields() {// @formatter:off
    Map<String, SetMethod> res = new TreeMap<String, SetMethod> ();
    res.put("Title", new SetMethod() { @Override public void set(final String value) {AbstractCompactionConfiguration.this.setTitle(value);} });
    return res;
  }// @formatter:on

  // ===================================================================
  // =============================<GETTERS>=============================
  // ===================================================================
  public String getTitle() {
    return this.title;
  }

  // ===================================================================
  // ============================</GETTERS>=============================
  // ===================================================================

  // ===================================================================
  // =============================<SETTERS>=============================
  // ===================================================================
  protected void setTitle(String title) {
    this.title = title;
  }
  // ===================================================================
  // ============================</SETTERS>=============================
  // ===================================================================
}