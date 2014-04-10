package Model.Compactors;

import Model.Compactors.CompactionConfigurations.IbraCompactionConfiguration;
import Model.Compactors.CompactionConfigurations.LevelBasedCompactionConfiguration;
import Model.Compactors.CompactionConfigurations.RandomCompactionConfiguration;
import Model.Compactors.CompactionConfigurations.HBaseCompactionConfigurations.DefaultHBaseCompactionConfiguration;
import Model.Compactors.CompactionConfigurations.HBaseCompactionConfigurations.HBaseCompactionRandomConfiguration;
import Model.Compactors.CompactionConfigurations.HBaseCompactionConfigurations.PredictCompactionConfiguration;
import Model.Compactors.CompactionConfigurations.HBaseCompactionConfigurations.TestHBaseCompactionConfiguration;

/**
 * all compaction algorithms
 * @author ibra
 */
public enum CompactionAlgorithm {
  TestHBaseCompactorConfiguration {
    @Override
    public AbstractCompactor createCompactor() {
      return new HBaseCompactor(new TestHBaseCompactionConfiguration());
    }
  },
  HBaseCompactorWithDefaultConfiguration {
    @Override
    public AbstractCompactor createCompactor() {
      return new HBaseCompactor(new DefaultHBaseCompactionConfiguration());
    }
  },
  HBaseCompactorWithRandomConfiguration {
    @Override
    public AbstractCompactor createCompactor() {
      return new HBaseCompactor(new HBaseCompactionRandomConfiguration());
    }
  },
  LevelBasedCompactor {
    @Override
    public AbstractCompactor createCompactor() {
      return new LevelBasedCompactor(new LevelBasedCompactionConfiguration());
    }
  },
  IbraCompactor {
    @Override
    public AbstractCompactor createCompactor() {
      return new IbraCompactor(new IbraCompactionConfiguration());
    }
  },
  RandomCompactor {
    @Override
    public AbstractCompactor createCompactor() {
      return new RandomCompactor(new RandomCompactionConfiguration());
    }
  },
  Predictor {
    @Override
    public AbstractCompactor createCompactor() {
      return new PredictorCompactor(new PredictCompactionConfiguration());
    }
  };

  public abstract AbstractCompactor createCompactor();
}
