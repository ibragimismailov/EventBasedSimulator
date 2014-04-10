package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GUI.Charts.AdvancedTimeChart;
import GUI.Charts.SimpleTimeChart;
import Model.Compactors.AbstractCompactor;
import Model.Compactors.PredictorCompactor;
import Model.Events.GlobalQueue;
import Model.HBaseElements.Store;

/**
 * Simulator - several HBase Stores work.
 * It has List of Stores and Compactors for them (compactor is a compaction algorithm and it's configuration).
 * This class updates WAF and RAF charts.
 * @author ibra
 */
public class Simulator {

  /**
   * stores - each Store has it's own compaction algorithm and compaction configuration
   * this list uses storeID as index to Store (since each Store has unique storeID)
   */
  public final List<Store> stores;

  /**
   * compactors for each store
   */
  public List<AbstractCompactor> compactors;

  /**
   * Write amplification graph (WAF) - a significant factor in write performance WAF = bytes written
   * to disk during compactions / bytes written to disk during flushes
   */
  private SimpleTimeChart writeAmplificationsTimeChart;

  /**
   * Read amplification graph (RAF) - a significant factor in read performance WAF = amount of
   * storeFiles RAF is number of files you need to check to read some KeyValue (worst case is just
   * amount of StoreFiles)
   */
  private AdvancedTimeChart readAmplificationTimeChart;

  /**
   * initializes and starts simulator
   * @param compactors - compactors list (for each store)
   */
  public Simulator(final List<AbstractCompactor> compactors) {
    this.compactors = compactors;

    this.stores = new ArrayList<Store>();
    for (int i = 0; i < Configuration.INSTANCE.getCompactionAlgosCount(); i++) {
      this.stores.add(new Store(i, compactors.get(i)));
    }

    List<String> storeTitles = new ArrayList<String>();
    for (int i = 0; i < this.compactors.size(); i++) {
      storeTitles.add(i + "(" + this.compactors.get(i).getCompactionConfiguration().getTitle()
          + ")");
    }
    this.readAmplificationTimeChart = AdvancedTimeChart.go("Stores read amplification", "Time",
      "Stores read amplification", storeTitles);
    this.writeAmplificationsTimeChart = SimpleTimeChart.go("Stores write amplification", "Time",
      "Write amplification", storeTitles);
  }

  /**
   * start simulation process
   */
  public void go() {
    GlobalQueue.queue.go(this);
  }

  /**
   * update WAF and RAF charts with current values
   */
  public void updateCharts() {
    Simulator.this.writeAmplificationsTimeChart.update(GlobalQueue.queue.getCurrentTime(),
      this.getWriteAmplification());
    Simulator.this.readAmplificationTimeChart.update(GlobalQueue.queue.getCurrentTime(),
      this.getReadAmplification());
  }

  /**
   * @return Map<Integer, Double> - map of pairs storeID - this store's read amplification
   */
  private Map<Integer, Double> getReadAmplification() {
    final Map<Integer, Double> res = new HashMap<Integer, Double>();
    for (int i = 0; i < this.compactors.size(); i++) {
      if (!(this.compactors.get(i) instanceof PredictorCompactor)) {
        res.put(i, this.stores.get(i).getReadAmplification());
      }
    }
    return res;
  }

  /**
   * @return Map<Integer, Double> - map of pairs storeID - this store's write amplification
   */
  private Map<Integer, Double> getWriteAmplification() {
    final Map<Integer, Double> res = new HashMap<Integer, Double>();
    for (int i = 0; i < this.compactors.size(); i++) {
      if (this.compactors.get(i) instanceof PredictorCompactor) {
        ((PredictorCompactor) (this.compactors.get(i))).predictTo(
          Simulator.this.writeAmplificationsTimeChart, i, GlobalQueue.queue.getCurrentTime());
      } else {
        res.put(i, this.stores.get(i).getWriteAmplification());
      }
    }
    return res;
  }
}