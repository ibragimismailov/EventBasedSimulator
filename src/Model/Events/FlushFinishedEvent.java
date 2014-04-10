package Model.Events;

import Model.HBaseElements.StoreFile;

/**
 * Event in GlobalQueue.
 * This event that happens when flush finishes. It contains StoreFile as a result of flush
 * @author ibra
 *
 */
class FlushFinishedEvent extends Event {

  /**
   * result of flush
   */
  StoreFile flushedStoreFile;

  FlushFinishedEvent(int storeID, StoreFile flushedStoreFile, long time, long timeLeftToReadAndWrite) {
    super(storeID, time, timeLeftToReadAndWrite);
    this.flushedStoreFile = flushedStoreFile;
  }

  @Override
  public EventType getType() {
    return EventType.FLUSH_FINISHED;
  }
}
