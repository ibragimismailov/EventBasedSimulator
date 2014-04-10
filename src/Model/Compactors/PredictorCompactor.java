package Model.Compactors;

import GUI.Charts.TimeChart;
import Model.Compactors.CompactionConfigurations.HBaseCompactionConfigurations.PredictCompactionConfiguration;
import Model.HBaseElements.StoreFileCollection;

/**
 * this class is not actually a compactor class. It imitates compactor work using Predictor class.
 * Predictor class is a class that predicts WAF by HBase native compaction algorithm configuration
 * @author ibra
 *
 */
public class PredictorCompactor extends HBaseCompactor {

  private Predictor predictor;

  /**
   * creates and initializes object
   * @param compactionConfiguration - this compactor configuration
   */
  PredictorCompactor(final PredictCompactionConfiguration compactionConfiguration) {
    super(compactionConfiguration);
    this.predictor = new Predictor(compactionConfiguration);
  }

  public void predictTo(TimeChart chart, int graphId, long endTime) {
    this.predictor.predictTo(chart, graphId, endTime);
  }

  @Override
  public void compactorInitQueue(int storeID) {
    return;
  }

  @Override
  protected StoreFileCollection selectFilesToCompact(StoreFileCollection storeFiles) {
    return null;
  }
}
