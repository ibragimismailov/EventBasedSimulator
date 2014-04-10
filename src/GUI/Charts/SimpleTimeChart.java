package GUI.Charts;

import java.util.List;
import java.util.Map;

import org.jfree.ui.RefineryUtilities;

/**
 * Simple TimeChart class.
 * Everything it does - just plots graphs and updates them with values given to update method
 * 
 * This class is used for plotting Write Amplification Factor graphs 
 * @author ibra
 */
public class SimpleTimeChart extends TimeChart {
  /**
   * creates SimpleTimeChart object
   * @param mainTitle - main title of chart
   * @param xTitle - x-axis title
   * @param yTitle - y-axis title
   * @param titles - titles of each graph
   */
  private SimpleTimeChart(final String mainTitle, final String xTitle, final String yTitle,
      final String... titles) {
    super(mainTitle, xTitle, yTitle, titles);
  }

  /**
   * Updates graphs with new values (adding new point to each graph)
   * @param time - Time (x-axis value) of current update
   * @param values - map of new values of graphs (graphID -> new value)
   */
  @Override
  public void update(final long time, final Map<Integer, Double> values) {
    for (Integer key : values.keySet()) {
      this.addPointToSeries(key, time, values.get(key));
    }
  }

  /**
   * create, make visible, configure and return new instance of SimpleTimeChart
   * @param mainTitle - main title of chart
   * @param xTitle - title of x-axis
   * @param yTitle - title of y-axis
   * @param titles - titles of graphs
   * @return instance of SimpleTimeChart
   */
  public static SimpleTimeChart go(final String mainTitle, final String xTitle,
      final String yTitle, final List<String> titles) {
    final String[] ts = new String[titles.size()];
    for (int i = 0; i < titles.size(); i++) {
      ts[i] = titles.get(i);
    }

    final SimpleTimeChart chart = new SimpleTimeChart(mainTitle, xTitle, yTitle, ts);
    chart.pack();
    RefineryUtilities.centerFrameOnScreen(chart);
    chart.setVisible(true);

    return chart;
  }
}
