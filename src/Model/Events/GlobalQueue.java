package Model.Events;

import java.util.PriorityQueue;

import Model.Configuration;
import Model.Simulator;
import Model.Compactors.CompactionResult;
import Model.HBaseElements.StoreFile;

/**
 * GlobalQueue is a Singleton class.
 * It simulates compaction algorithms' work with event-based queue
 * @author ibra
 *
 */
public class GlobalQueue {
  private GlobalQueue() {
  }

  /**
   * instance of Singleton
   */
  public final static GlobalQueue queue = new GlobalQueue();

  /**
   * queue of events - events are sorted by current time of event
   */
  private PriorityQueue<Event> q = new PriorityQueue<Event>();

  /**
   * current time - time of current event from queue
   */
  private long currentTime;

  /**
   * add event to queue
   * @param event
   */
  public void add(Event event) {
    this.q.add(event);
  }

  /**
   * current time - time of current event from queue
   */
  public long getCurrentTime() {
    return this.currentTime;
  }

  /**
   * start simulation
   * @param simulator
   */
  public void go(final Simulator simulator) {
    GlobalQueue.queue.add(new UpdateChartEvent(0));

    for (int i = 0; i < simulator.compactors.size(); i++) {
      simulator.compactors.get(i).compactorInitQueue(i);
    }

    while (!this.q.isEmpty()) {
      Event event = this.q.poll();
      this.currentTime = event.getTime();
      if (this.currentTime > Configuration.INSTANCE.getSimulationTimeDays()
          * Configuration.MS_PER_DAY) {
        continue;
      }

      // if event is not finished IO interaction with HDFS, we give some time packet for this store
      // to read/write to HDFS
      if (!event.isFinished()) {
        event.doReadWrite(simulator.stores.get(event.getStoreID()));
        this.add(event);
        continue;
      }

      int storeID;
      long dtime;
      CompactionResult compactionResult;
      switch (event.getType()) {
      // it is time to update charts
      case UPDATE_CHART:
        simulator.updateCharts();
        this.add(new UpdateChartEvent(this.currentTime + Configuration.INSTANCE.getChartUpdateGap()));
        break;
      // it is time to do a flush in storeID Store
      case FLUSH:
        storeID = event.getStoreID();
        StoreFile flushingStoreFile = StoreFile.getFlushed();
        dtime = Configuration.INSTANCE.getTimeToWrite(flushingStoreFile.getBytesSize());

        this.add(new FlushFinishedEvent(storeID, flushingStoreFile, this.currentTime, dtime));

        this.add(new FlushEvent(storeID, this.currentTime + Configuration.INSTANCE.getFlushGap()));
        break;
      // flush is finished in storeID Store
      case FLUSH_FINISHED:
        storeID = event.getStoreID();
        StoreFile flushedStoreFile = ((FlushFinishedEvent) event).flushedStoreFile;

        simulator.stores.get(storeID).flushOccured(flushedStoreFile);

        this.add(new CompactionEvent(storeID, this.currentTime));

        break;
      // it is time to do a compaction in storeID Store
      case COMPACTION:
        storeID = event.getStoreID();
        compactionResult = simulator.stores.get(storeID).doCompaction();
        if (compactionResult.isPerformed) {
          long readTime = Configuration.INSTANCE.getTimeToRead(compactionResult.readBytes);
          long writeTime = Configuration.INSTANCE.getTimeToWrite(compactionResult.writtenBytes);
          dtime = readTime + writeTime;

          this.add(new CompactionFinishedEvent(storeID, compactionResult, this.currentTime, dtime));
        }
        break;
      // it is time to do a major compaction in storeID Store
      case MAJOR_COMPACTION:
        storeID = event.getStoreID();
        compactionResult = simulator.stores.get(storeID).forceMajorCompaction();
        if (compactionResult.isPerformed) {
          long readTime = Configuration.INSTANCE.getTimeToRead(compactionResult.readBytes);
          long writeTime = Configuration.INSTANCE.getTimeToWrite(compactionResult.writtenBytes);
          dtime = readTime + writeTime;

          this.add(new CompactionFinishedEvent(storeID, compactionResult, this.currentTime, dtime));
        }
        break;
      // compaction is finished in storeID Store
      case COMPACTION_FINISHED:
        storeID = event.getStoreID();
        compactionResult = ((CompactionFinishedEvent) event).compactionResult;
        simulator.stores.get(storeID).compactionOccured(compactionResult);

        break;
      }
    }
  }
}