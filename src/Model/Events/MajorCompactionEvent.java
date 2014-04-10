package Model.Events;

/**
 * Event in GlobalQueue.
 * This event that happens when a major compaction starts
 * @author ibra
 *
 */
public class MajorCompactionEvent extends Event {

  public MajorCompactionEvent(int storeID, long time) {
    super(storeID, time, 0);
  }

  @Override
  public EventType getType() {
    return EventType.MAJOR_COMPACTION;
  }
}
