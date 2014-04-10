package Model.HBaseElements.StoreFileImplementations;

import Model.Configuration;
import Model.HBaseElements.StoreFile;

/**
 * KeyValueData - emulates collection of KeyValues
 * @author ibra
 */
public class StoreFileWithoutTTL extends StoreFile {

  /**
   * @return empty StoreFile - empty StoreFile has no KeyValues
   */
  public static StoreFileWithoutTTL getEmpty() {
    StoreFileWithoutTTL res = new StoreFileWithoutTTL();
    res.byteSize = 0;
    return res;
  }

  /**
   * @return flushed StoreFile - flushed StoreFile is a StoreFile that is created when memStore is full and it is flushed
   */
  public static StoreFileWithoutTTL getFull() {
    StoreFileWithoutTTL res = new StoreFileWithoutTTL();
    res.byteSize = Configuration.INSTANCE.getFlushedFileByteSize();
    return res;
  }

  private StoreFileWithoutTTL() {
  }

  /**
   * merges this KeyValueData with other
   * @param toMerge - KeyValueData this to be merged with
   * @return amount of bytes that was merged with this from other
   */
  @Override
  public long mergeWith(final StoreFile toMerge) {
    StoreFileWithoutTTL other = (StoreFileWithoutTTL) toMerge;
    this.byteSize += other.byteSize;
    return other.byteSize;
  }
}
