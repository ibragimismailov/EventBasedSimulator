package Model.Events;

/**
 * Event in GlobalQueue.
 * This event that happens when compaction starts
 * @author ibra
 *
 */
class CompactionEvent extends Event {

  CompactionEvent(int storeID, long time) {
    super(storeID, time, 0);
  }

  @Override
  public EventType getType() {
    return EventType.COMPACTION;
  }
}
