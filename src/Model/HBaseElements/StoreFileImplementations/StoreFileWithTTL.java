package Model.HBaseElements.StoreFileImplementations;

import java.util.ArrayList;
import java.util.List;

import Model.Configuration;
import Model.Events.GlobalQueue;
import Model.HBaseElements.StoreFile;
import Tools.Generator;

/**
 * KeyValueData - emulates collection of KeyValues
 * @author ibra
 */
public class StoreFileWithTTL extends StoreFile {

  /**
   * for each KeyValuePack we store its create time to know when its TLL expires
   */
  private List<Long> keyValuePacksCreateTime;

  /**
   * for each KeyValuePack we store its size
   */
  private List<Long> keyValuePackByteSizes;

  /**
   * @return empty StoreFile - empty StoreFile has no KeyValues
   */
  public static StoreFileWithTTL getEmpty() {
    StoreFileWithTTL res = new StoreFileWithTTL();
    res.keyValuePacksCreateTime = new ArrayList<Long>();
    res.keyValuePackByteSizes = new ArrayList<Long>();
    res.byteSize = 0;
    return res;
  }

  /**
   * @return flushed StoreFile - flushed StoreFile is a StoreFile that is created when memStore is full and it is flushed
   */
  public static StoreFileWithTTL getFull() {
    StoreFileWithTTL res = new StoreFileWithTTL();
    res.keyValuePacksCreateTime = new ArrayList<Long>();
    res.keyValuePackByteSizes = new ArrayList<Long>();
    res.keyValuePacksCreateTime.add(GlobalQueue.queue.getCurrentTime());
    res.keyValuePackByteSizes.add(Configuration.INSTANCE.getFlushedFileByteSize());
    res.byteSize = res.keyValuePackByteSizes.get(0);
    return res;
  }

  private StoreFileWithTTL() {
  }

  /**
   * merges this KeyValueData with other. KeyValues that have expired TTL are to be deleted during merge
   * @param toMerge - KeyValueData this to be merged with
   * @return amount of bytes that was merged with this from other
   */
  @Override
  public long mergeWith(final StoreFile toMerge) {
    StoreFileWithTTL other = (StoreFileWithTTL) toMerge;
    long mergedBytes = 0;
    final long curTime = GlobalQueue.queue.getCurrentTime();
    for (int i = 0; i < other.keyValuePacksCreateTime.size(); i++) {
      final long createdTime = other.keyValuePacksCreateTime.get(i);
      final long byteSize = other.keyValuePackByteSizes.get(i);

      // if TTL is expired we don't add this keyValuePack to us
      if (Configuration.INSTANCE.isKeyValuesTTLEnabled()
          && curTime - createdTime >= Generator.getGenerator().getKeyValueTTL()) {
        continue;
      }

      mergedBytes += byteSize;
      this.keyValuePackByteSizes.add(byteSize);
      this.keyValuePacksCreateTime.add(createdTime);
      this.byteSize += byteSize;
    }
    return mergedBytes;
  }
}
