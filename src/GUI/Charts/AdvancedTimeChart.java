package GUI.Charts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.jfree.ui.RefineryUtilities;

import Model.Configuration;

/**
 * AdvancedTimeChart - TimeChart, that plots average graphs.
 * Current value of average graph is average value of last avgCount values given to update method
 * 
 * This class is used for plotting Read Amplification Factor(RAF) graphs.
 * We use this class for plotting RAF because RAF value changes very frequently and 
 * it is hard to compare just two RAF graphs without seeing its averages.
 * @author ibra
 */
public class AdvancedTimeChart extends TimeChart {

  /**
   * stat contains last avgCount values of each original graph so Queue of doubles is last avgCount values
   */
  private final List<Queue<Double>> stat;

  /**
   * avgCount - amount of values we store for each graph to calculate average graph
   * configured in such way, that chart show average value for some period
   */
  private long getAvgCount() {
    // 10 days
    return 10L * Configuration.MS_PER_DAY / (Configuration.INSTANCE.getChartUpdateGap());
  }

  /**
   * creates AdvancedTimeChart object
   * @param mainTitle main title of chart
   * @param xTitle x-axis title
   * @param yTitle y-axis title
   * @param titles titles of each graph
   */
  private AdvancedTimeChart(final String mainTitle, final String xTitle, final String yTitle,
      final String... titles) {
    super(mainTitle, xTitle, yTitle, titles);

    this.stat = new ArrayList<Queue<Double>>();
    for (String title2 : titles) {
      this.stat.add(new LinkedList<Double>());
    }
  }

  /**
   * Updates graphs with new values (adding new point to each graph)
   * @param time - Time (x-axis value) of current update
   * @param values - map of new values of graphs (graphID -> new value)
   */
  @Override
  public void update(final long time, final Map<Integer, Double> values) {

    for (Integer key : values.keySet()) {
      // if we already have at least avgCount of them
      // we remove old value from stat
      while (this.stat.get(key).size() >= this.getAvgCount()) {
        this.stat.get(key).poll();
      }

      // add new value to stat
      this.stat.get(key).add(values.get(key));

      // calculate average value
      double avgValue = 0;
      for (Double v : this.stat.get(key)) {
        avgValue += v;
      }
      avgValue /= this.stat.get(key).size();

      // add point to average graph
      this.addPointToSeries(key, time, avgValue);
    }
  }

  /**
   * create, make visible, configure and return new instance of AdvancedTimeChart
   * @param mainTitle main title of chart
   * @param xTitle title of x-axis
   * @param yTitle title of y-axis
   * @param titles titles of graphs
   * @return instance of AdvancedTimeChart
   */
  public static AdvancedTimeChart go(final String mainTitle, final String xTitle,
      final String yTitle, final List<String> titles) {
    final String[] ts = new String[titles.size()];
    for (int i = 0; i < titles.size(); i++) {
      ts[i] = "avg-" + titles.get(i);
    }

    final AdvancedTimeChart chart = new AdvancedTimeChart(mainTitle, xTitle, yTitle, ts);
    chart.pack();
    RefineryUtilities.centerFrameOnScreen(chart);
    chart.setVisible(true);

    return chart;
  }
}
