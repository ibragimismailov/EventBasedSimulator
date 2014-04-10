package Model.Events;

import Model.Compactors.CompactionResult;

/**
 * Event in GlobalQueue.
 * This event that happens when compaction finishes. It contains CompactionResult as a result of compaction
 * @author ibra
 *
 */
class CompactionFinishedEvent extends Event {

  /**
   * result of compaction
   */
  final CompactionResult compactionResult;

  CompactionFinishedEvent(int storeID, CompactionResult compactionResult, long time,
      long timeLeftToReadAndWrite) {
    super(storeID, time, timeLeftToReadAndWrite);
    this.compactionResult = compactionResult;
  }

  @Override
  public EventType getType() {
    return EventType.COMPACTION_FINISHED;
  }
}
