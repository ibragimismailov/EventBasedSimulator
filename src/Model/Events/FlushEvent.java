package Model.Events;

/**
 * Event in GlobalQueue.
 * This event that happens when a flush starts
 * @author ibra
 *
 */
public class FlushEvent extends Event {

  public FlushEvent(int storeID, long time) {
    super(storeID, time, 0);
  }

  @Override
  public EventType getType() {
    return EventType.FLUSH;
  }
}
