package Model.Events;

/**
 * Event in GlobalQueue.
 * This event that happens when we need to update RAF and WAF charts
 * @author ibra
 *
 */
class UpdateChartEvent extends Event {

  UpdateChartEvent(long time) {
    super(-1, time, 0);
  }

  @Override
  public EventType getType() {
    return EventType.UPDATE_CHART;
  }
}
