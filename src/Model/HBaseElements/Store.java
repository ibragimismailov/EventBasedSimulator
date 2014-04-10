package Model.HBaseElements;

import Model.Compactors.AbstractCompactor;
import Model.Compactors.CompactionResult;

/**
 * Store - consists of Collection of StoreFiles and MemStore.
 * Each Store is uniquely identified by its storeID.
 * Each Store represents different compaction algorithms with different configurations
 * @author ibra
 */
public class Store {

  /**
   * compaction algorithm and compaction configuration for this Store
   */
  private final AbstractCompactor compactor;

  /**
   * ID of this Store
   */
  private final int storeID;

  /**
   * Time when HDFS will be free to be used for reading and writing
   */
  private long HDFSfreeTime;

  /**
   * collection of store files of this Store
   */
  private final StoreFileCollection storeFiles;

  /**
   * sum of bytes, that were read/written to disk during flushes for this Store
   */
  private long flushIO;

  /**
   * sum of bytes, that were read/written to disk during compactions for this Store
   */
  private long compactionIO;

  /**
   * creates and initializes Store
   * @param storeID - id of this store
   * @param compactor - compaction algorithm and compaction configuration for this Store
   */
  public Store(final int storeID, final AbstractCompactor compactor) {
    this.storeID = storeID;
    this.HDFSfreeTime = 0;
    this.storeFiles = new StoreFileCollection();
    this.compactor = compactor;
    this.flushIO = 0;
    this.compactionIO = 0;
  }

  /**
   * method invokes compaction with compactor object
   * @return
   */
  public CompactionResult doCompaction() {
    return this.compactor.doCompaction(this.storeFiles);
  }

  /**
   * method invokes force major compaction with compactor object
   * @return
   */
  public CompactionResult forceMajorCompaction() {
    return this.compactor.forceMajorCompaction(this.storeFiles);
  }

  public int getStoreID() {
    return this.storeID;
  }

  public long getHDFSfreeTime() {
    return this.HDFSfreeTime;
  }

  /**
   * this method reads/writes to HDFS
   * @param since - time when read/write starts
   * @param dtime - time hoe much read/write lasts
   */
  public void doReadWrite(long since, long dtime) {
    this.HDFSfreeTime = Math.max(this.HDFSfreeTime, since) + dtime;
  }

  public void flushOccured(StoreFile flushedStoreFile) {
    this.storeFiles.add(flushedStoreFile);
    this.flushIO += flushedStoreFile.getBytesSize();
  }

  public void compactionOccured(CompactionResult compactionResult) {
    this.storeFiles.add(compactionResult.compacted);
    this.compactionIO += compactionResult.readBytes + compactionResult.writtenBytes;
  }

  /**
   * @return ReadAmplification in this Store
   */
  public double getReadAmplification() {
    return this.storeFiles.size();
  }

  /**
   * @return WriteAmplification in this Store
   */
  public double getWriteAmplification() {
    return (double) this.compactionIO / (double) this.flushIO;
  }
}