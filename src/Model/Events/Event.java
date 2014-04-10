package Model.Events;

import Model.Configuration;
import Model.HBaseElements.Store;

/**
 * abstract class Event. Objects of this class are events in GlobalQueue
 * @author ibra
 *
 */
abstract class Event implements Comparable<Event> {

  // time left for this event to read from HDFS or write to HDFS
  private long timeLeftToReadAndWrite;

  // current time of this event
  private long time;

  // storeID of store where this event happened
  private int storeID;

  abstract EventType getType();

  Event(int storeID, long time, long timeLeftToReadAndWrite) {
    this.time = time;
    this.storeID = storeID;
    this.timeLeftToReadAndWrite = timeLeftToReadAndWrite;
  }

  long getTime() {
    return this.time;
  }

  int getStoreID() {
    return this.storeID;
  }

  boolean isFinished() {
    return this.timeLeftToReadAndWrite == 0;
  }

  /**
   * this method reads/writes to HDFS for HdfsTimePacket time
   * @param store - store where this event is happening
   */
  void doReadWrite(Store store) {
    // time interval given to this event to read/write
    long dtime = Math.min(Configuration.INSTANCE.getHdfsTimePacket(), this.timeLeftToReadAndWrite);

    this.timeLeftToReadAndWrite -= dtime;
    store.doReadWrite(this.time, dtime);
    this.time = store.getHDFSfreeTime();
  }

  @Override
  public int compareTo(Event other) {
    return Long.compare(this.time, other.time);
  }

  @Override
  public String toString() {
    return this.getType() + " " + this.time + " " + this.timeLeftToReadAndWrite;
  }
}
