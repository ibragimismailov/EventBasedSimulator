package GUI.Charts;

import java.awt.Color;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import Model.Configuration;

/**
 * This class is abstract parent class for SimpleTimeChart and AdvancedTimeChart
 * 
 * Here you can see methods just for initializing TimeChart object and method addPoint to add point to some graph
 * 
 * TimeChart is a frame with a chart, that contains several graphs
 * @author ibra
 */
public abstract class TimeChart extends JFrame {
  /**
   * list of graphs to be shown in this chart
   */
  private final List<TimeSeries> timeSeries;

  public abstract void update(final long time, final Map<Integer, Double> values);

  /**
   * creates TimeChart object
   * @param mainTitle - main title of chart
   * @param xTitle - x-axis title
   * @param yTitle - y-axis title
   * @param titles - titles of each graph
   */
  protected TimeChart(final String mainTitle, final String xTitle, final String yTitle,
      final String... titles) {
    super(mainTitle);

    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    this.timeSeries = new ArrayList<TimeSeries>();

    for (final String title : titles) {
      this.timeSeries.add(new TimeSeries(title));
    }

    final JFreeChart chart = createChart(mainTitle, xTitle, yTitle, this.timeSeries);
    final ChartPanel chartPanel = new ChartPanel(chart, false);
    chartPanel.setPreferredSize(new java.awt.Dimension(1000, 625));
    chartPanel.setMouseZoomable(true, false);
    this.setContentPane(chartPanel);

    for (final TimeSeries s : this.timeSeries) {
      s.add(new Millisecond(new Date(1 + 0 * Configuration.MS_PER_DAY)), 0.0);
    }
  }

  /**
   * creates chart
   * @param mainTitle - main title of chart
   * @param xTitle - x-axis title
   * @param yTitle - y-axis title
   * @param graphs - graphs of chart
   * @return
   */
  private static JFreeChart createChart(final String mainTitle, final String xTitle,
      final String yTitle, final List<TimeSeries> graphs) {
    final JFreeChart chart = ChartFactory.createTimeSeriesChart(mainTitle, // title
      xTitle, // x-axis label
      yTitle, // y-axis label
      createDataset(graphs), // data
      true, // create legend?
      true, // generate tooltips?
      false // generate URLs?
        );

    chart.setBackgroundPaint(Color.white);

    final XYPlot plot = (XYPlot) chart.getPlot();
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);
    plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
    plot.setDomainCrosshairVisible(true);
    plot.setRangeCrosshairVisible(true);

    final XYItemRenderer r = plot.getRenderer();
    final XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
    renderer.setBaseShapesVisible(true);
    renderer.setBaseShapesFilled(true);
    renderer.setDrawSeriesLineAsPath(true);

    // make time x-axis be counted in year-day
    final DateAxis axis = (DateAxis) plot.getDomainAxis();
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd");
    axis.setDateFormatOverride(new SimpleDateFormat("yyyy/MMM/dd")
    // for nice time view on x-axis
    {
      @Override
      public StringBuffer format(final Date arg0, final StringBuffer arg1, final FieldPosition arg2) {
        final StringBuffer buff = formatter.format(arg0, arg1, arg2);
        final Integer year = Integer.parseInt(buff.substring(0, 4)) - 1970;
        final String month = buff.substring(5, 8);
        final Integer day = Integer.parseInt(buff.substring(9, 11)) - 1;
        return new StringBuffer(year + "/" + month + "/" + day);
      }
    });

    return chart;
  }

  /**
   * creates xydataset
   * @param series - series - graphs to be shown on chart
   */
  private static XYDataset createDataset(final List<TimeSeries> series) {
    final TimeSeriesCollection dataset = new TimeSeriesCollection();
    for (final TimeSeries s : series) {
      dataset.addSeries(s);
    }
    return dataset;
  }

  /**
   * add point to graph
   * @param graphId - id of graph we are adding point to
   * @param time - x-axis value
   * @param value - y-axis value
   */
  protected void addPointToSeries(final int graphId, final long time, final double value) {
    this.timeSeries.get(graphId).add(new Millisecond(new Date(time)), value);
  }
}
