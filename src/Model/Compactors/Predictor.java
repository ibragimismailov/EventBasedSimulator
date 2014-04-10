package Model.Compactors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GUI.Charts.TimeChart;
import Model.Configuration;
import Model.Compactors.CompactionConfigurations.HBaseCompactionConfigurations.PredictCompactionConfiguration;

//@formatter:off
/**
 * Class Predictor generates WAF chart with compactionConfiguration of HBase native compaction algorithm.
 * See article about Predictor class for clarifications about how this class works and what formula it uses
 * 
 * so WAF after Q flushes is:
 * WAF(Q) =(2*∑(i=[1,Q])〖aRi〗_i )/Q , where
 * 
 * Q - flush id
 *〖aR〗_i - radix of the smallest not-null position of pR numeral system presentation of number i.
 *
 * pR numeral system:
 *  〖pR〗_0=1
 *  〖pR〗_1=Max(MN,R)
 *             
 *  〖pR〗_i = 
 *      if i≥start AND (i-start) MOD period==0: ceil(〖pR〗_(i-1)*R)+add,&i≥start AND (i-start)MOD period=0
 *      else:                                   ceil(〖pR〗_(i-1)*R),&else
 *
 * MN is compactionConfiguration.getCompactionMinFiles
 * MX is compactionConfiguration.getCompactionMaxFiles
 * R  is ceil((1.0 + 1.0 / this.compactionConfiguration.getCompactionRatio()))
 * start, add and period are calculated as it is in methods below
 * 
 * @author ibra
 *
 */
//@formatter:on
class Predictor {

  /**
   * compaction configuration object contains configurations for compaction algorithm
   */
  private final PredictCompactionConfiguration compactionConfiguration;

  /**
   * pR numeral system
   */
  private final List<Long> pR;

  /**
   * amount of flushes that are already happened
   */
  private int flushId;

  /**
   * amount of bytes written/read during compactions
   */
  private double compactionsIO;

  /**
   * latest time when chart update happened
   */
  private long lastUpdateChartTime;

  Predictor(final PredictCompactionConfiguration compactionConfiguration) {
    this.compactionConfiguration = compactionConfiguration;
    this.pR = this.generatePws();
    this.flushId = 1;
    this.compactionsIO = 0;
    this.lastUpdateChartTime = 0;
  }

  private static long ceil(double d) {
    return Math.round(Math.ceil(d));
  }

  /**
   * method adds points to chart until simulation time of this algorithm comes to endTime
   * @param chart - chart points to be added to
   * @param graphId - id of graph in chart
   * @param endTime - end time of simulation
   */
  void predictTo(TimeChart chart, int graphId, long endTime) {

    for (;; this.flushId++) {
      long time = this.flushId * Configuration.INSTANCE.getFlushGap();
      if (time > endTime) {
        break;
      }

      long p = this.pR.get(this.getARindex(this.flushId));
      if (p > 1) {
        this.compactionsIO += 2.0 * p * Configuration.INSTANCE.getFlushedFileByteSize();
      }

      double waf = 0.0;
      if (this.flushId > 0) {
        waf = this.compactionsIO / (this.flushId * Configuration.INSTANCE.getFlushedFileByteSize());
      }
      if (time - this.lastUpdateChartTime >= Configuration.INSTANCE.getChartUpdateGap()) {
        this.lastUpdateChartTime += Configuration.INSTANCE.getChartUpdateGap();
        Map<Integer, Double> update = new HashMap<Integer, Double>();
        update.put(graphId, waf);
        chart.update(time, update);
      }
    }
  }

  private List<Long> generatePws() {
    double r = (1.0 + 1.0 / this.compactionConfiguration.getCompactionRatio());

    int mx = (int) this.compactionConfiguration.getCompactionMaxFiles();
    int mn = (int) this.compactionConfiguration.getCompactionMinFiles();
    int rr = (int) ceil(r);

    if (mx < mn || mx < rr) {
      return new ArrayList<Long>();
    }

    List<Long> res = new ArrayList<Long>();
    res.add(1L);
    res.add((long) Math.max(rr, mn));
    int a = this.getStart();
    int b = this.getAdd();
    int d = this.getPeriod();
    if (d == 0) {
      return new ArrayList<Long>();
    }
    for (int i = 2; res.get(res.size() - 1) < 1e9; i++) {
      long next = ceil(res.get(res.size() - 1) * r);
      if (i >= a && (i - a) % d == 0) {
        next += b;
      }
      res.add(next);
    }

    return res;
  }

  private int getARindex(long n) {
    for (int i = this.pR.size() - 1; i >= 0; i--) {
      if (this.pR.get(i).compareTo(n) == -1) {
        n = (n - 1) % this.pR.get(i) + 1;
      }
      if (this.pR.get(i).compareTo(n) == 0) {
        return i;
      }
    }
    return 1;
  }

  private int getStart() {
    double r = (1.0 + 1.0 / this.compactionConfiguration.getCompactionRatio());

    int mx = (int) this.compactionConfiguration.getCompactionMaxFiles();
    int mn = (int) this.compactionConfiguration.getCompactionMinFiles();
    int rr = (int) ceil(r);
    int p = rr - 1;

    return (mx - Math.max(mn, rr)) / p + 2;
  }

  private int getAdd() {
    double r = (1.0 + 1.0 / this.compactionConfiguration.getCompactionRatio());

    int mn = (int) this.compactionConfiguration.getCompactionMinFiles();
    int rr = (int) ceil(r);

    return Math.max(1, mn - rr);
  }

  private int getPeriod() {
    double r = (1.0 + 1.0 / this.compactionConfiguration.getCompactionRatio());

    int mx = (int) this.compactionConfiguration.getCompactionMaxFiles();
    int mn = (int) this.compactionConfiguration.getCompactionMinFiles();
    int rr = (int) ceil(r);

    if (mn > rr) {
      return (mx - mn) / (rr - 1) + 1;
    } else {
      return (mx - (rr + 1)) / (rr - 1) + 1;
    }
  }
}
