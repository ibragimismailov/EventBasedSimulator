package Model;

import java.util.Map;

import GUI.ConfigurationFrames.AbstractConfigurationFrame;

/**
 * abstract parent class for Configuration and CompactionConfiguration classes
 * @author ibra
 *
 */
public abstract class AbstractConfiguration {

  /**
   * All methods to get compaction configuration fields. They are organized to Map to
   * add/remove some configurations: to add a new configuration all you need to do is create
   * private field, getter, setter and add it to this map in overloaded fillGetFields method
   */
  private final Map<String, GetMethod> getFields = this.fillGetFields();

  /**
   * All methods to set compaction configuration fields. They are organized to Map it to be easy to
   * add/remove some configurations: to add a new configuration all you need to do is create
   * private field, getter, setter and add it to this map in overloaded fillSetFields method
   */
  private final Map<String, SetMethod> setFields = this.fillSetFields();

  /**
   * method that fills getFields map
   */
  protected abstract Map<String, GetMethod> fillGetFields();

  /**
   * method that fills setFields map
   */
  protected abstract Map<String, SetMethod> fillSetFields();

  /**
   * This methods show ConfigurationFrame for this configuration object.
   * User can enter/edit values for this object using GUI
   * @param title - title for ConfigurationSetterFrame
   */
  public final void showFrame(final String title) {
    this.getConfigurationFrame(title, this.getFields, this.setFields).setVisible(true);
  }

  /**
   * abstract method to get ConfigurationFrame for changing this configuration
   * @param title - main title for ConfigurationFrame
   * @param getFields - get fields map of compaction configuration
   * @param setFields - get fields map of compaction configuration
   */
  protected abstract AbstractConfigurationFrame getConfigurationFrame(final String title,
      final Map<String, GetMethod> getFields, final Map<String, SetMethod> setFields);

  /**
   * interface to get compaction configuration fields
   */
  public interface GetMethod {
    public String get();
  }

  /**
   * interface to set compaction configuration fields
   */
  public interface SetMethod {
    public void set(String value) throws NumberFormatException;
  }
}
