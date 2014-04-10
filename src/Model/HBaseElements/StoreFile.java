package Model.HBaseElements;

import Model.Configuration;
import Model.HBaseElements.StoreFileImplementations.StoreFileWithTTL;
import Model.HBaseElements.StoreFileImplementations.StoreFileWithoutTTL;

/**
 * StoreFile is a collection of KeyValues
 * @author ibra
 */
public abstract class StoreFile {

  /**
   * @return empty StoreFile - empty StoreFile has no KeyValues
   */
  public static StoreFile getEmpty() {
    if (Configuration.INSTANCE.isKeyValuesTTLEnabled()) {
      return StoreFileWithTTL.getEmpty();
    } else {
      return StoreFileWithoutTTL.getEmpty();
    }
  }

  /**
   * @return flushed StoreFile - flushed StoreFile is a StoreFile that is created when memStore is full and it is flushed
   */
  public static StoreFile getFlushed() {
    if (Configuration.INSTANCE.isKeyValuesTTLEnabled()) {
      return StoreFileWithTTL.getFull();
    } else {
      return StoreFileWithoutTTL.getFull();
    }
  }

  /**
   * merges two StoreFiles
   * @param other - storeFile to merge with
   */
  public abstract long mergeWith(final StoreFile other);

  /**
   * size of StoreFile
   */
  protected long byteSize;

  /**
   * @return size of this storeFile in bytes
   */
  public long getBytesSize() {
    return this.byteSize;
  }
}