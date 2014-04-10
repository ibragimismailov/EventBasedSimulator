package Model;

import java.util.Map;
import java.util.TreeMap;

import GUI.ConfigurationFrames.AbstractConfigurationFrame;
import GUI.ConfigurationFrames.ConfigurationFrame;

/**
 * Class Configuration describes general configuration of HBase (shown in gui in ConfigurationFrame on left side)
 * This class is Singleton
 * configuration values are changed by ConfigurationFrame
 * 
 * To add new configuration value: 
 * 1) add private field with public getter and setter 
 * 2) add it to fillGetFields and fillSetFields
 * @author ibra
 */
public class Configuration extends AbstractConfiguration {

  /**
   * instance of Singleton
   */
  public static final Configuration INSTANCE = new Configuration();

  /**
   * milliseconds per day
   */
  public static final long MS_PER_DAY = 24L * 3600L * 1000L;

  /**
   * maximal amount of compaction algorithms to simulate
   */
  public final int MAX_COMPACTION_ALGOS_COUNT = 10;

  /**
   * compression ratio - when memstore flush occurs we take memstore, compress it and write to
   * storeFile, so this storeFile size = memstore size / compression ratio
   */
  private final long COMPRESSION_RATIO = 10;

  /**
   * Gap between flushes(ms) - time gap between two consecutive flushes in one store
   */
  private long flushGap = 40000;

  /**
   * Memstore flush size(bytes) - size of MemStore
   */
  private long memstoreBytesSize = 135266304; // hbase.hregion.memstore.flush.size

  /**
   * Compaction algorithms count - amount of compaction algorithms you want to test
   */
  private long compactionAlgosCount = 1;

  /**
   * KeyValue TTL(ms) - average TTL for KeyValues
   */
  private long keyValueTTL = -1;

  /**
   * KeyValue TTL jitter - jitter is used for getting KeyValue TTL.
   * Watch RandomGenerator.getJitteredValue for clarifications
   */
  private double keyValueTTLJitter = 0.3;

  /**
   * Read bytes per second to HDFS - read rate from HDFS
   */
  private long HDFSReadBytesPerSecond = 10L * 1024L * 1024L;

  /**
   * Write bytes per second from HDFS - write rate to HDFS
   */
  private long HDFSWriteBytesPerSecond = 30L * 1024L * 1024L;

  /**
   * overall duration of simulation
   */
  private long simulationTimeDays = 365;

  private Configuration() {
  }

  public long getFlushedFileByteSize() {
    return this.getMemstoreBytesSize() / this.COMPRESSION_RATIO;
  }

  /**
   * is TTLs for KeyValues enabled (for example for some cluster TTLs are 3 days, and for another cluster there might be no TTLs)
   * We need this configuration, because storing TTL information about all keyValues without deleting them when their's TTL is expired is too expensive 
   */
  public boolean isKeyValuesTTLEnabled() {
    return this.getKeyValueTTL() != -1;
  }

  /**
   * time packet to HDFS read/write
   */
  public long getHdfsTimePacket() {
    return 10000;
  }

  /**
   * @return time gap between chart updates
   */
  public long getChartUpdateGap() {
    return this.getSimulationTimeDays() * Configuration.MS_PER_DAY / 1000;
  }

  /**
   * @param byteSize size of File to write
   * @return time needed to write a file with byteSize size 
   */
  public long getTimeToWrite(long byteSize) {
    return byteSize * 1000 / this.getHDFSWriteBytesPerSecond();
  }

  /**
   * @param byteSize size of File to read
   * @return time needed to read a file with byteSize size 
   */
  public long getTimeToRead(long byteSize) {
    return byteSize * 1000 / this.getHDFSReadBytesPerSecond();
  }

  @Override
  protected AbstractConfigurationFrame getConfigurationFrame(final String title,
      final Map<String, GetMethod> getFields, final Map<String, SetMethod> setFields) {
    return new ConfigurationFrame(title, getFields, setFields);
  }

  @Override
  protected Map<String, GetMethod> fillGetFields() {// @formatter:off
    final Map<String, GetMethod> res = new TreeMap<String, GetMethod>();
    res.put("Simulation time(days)",           new GetMethod() { @Override public String get() {return Long  .toString(Configuration.this.getSimulationTimeDays     ());} });
    res.put("Gap between flushes(ms)",         new GetMethod() { @Override public String get() {return Long  .toString(Configuration.this.getFlushGap               ());} });
    res.put("Memstore flush size(bytes)",      new GetMethod() { @Override public String get() {return Long  .toString(Configuration.this.getMemstoreBytesSize      ());} });
    res.put("Compaction algos count",          new GetMethod() { @Override public String get() {return Long  .toString(Configuration.this.getCompactionAlgosCount   ());} });
    res.put("KeyValue TTL(ms)",                new GetMethod() { @Override public String get() {return Long  .toString(Configuration.this.getKeyValueTTL            ());} });
    res.put("KeyValue TTL jitter",             new GetMethod() { @Override public String get() {return Double.toString(Configuration.this.getKeyValueTTLJitter      ());} });
    res.put("Read bytes per second from HDFS", new GetMethod() { @Override public String get() {return Long  .toString(Configuration.this.getHDFSReadBytesPerSecond ());} });
    res.put("Write bytes per second to HDFS",  new GetMethod() { @Override public String get() {return Long  .toString(Configuration.this.getHDFSWriteBytesPerSecond());} });
    return res;
  }// @formatter:on

  @Override
  protected Map<String, SetMethod> fillSetFields() {// @formatter:off
    final Map<String, SetMethod> res = new TreeMap<String, SetMethod>();
    res.put("Simulation time(days)",           new SetMethod() { @Override public void set(String value) {Configuration.this.setSimulationTimeDays     (value);} });
    res.put("Gap between flushes(ms)",         new SetMethod() { @Override public void set(String value) {Configuration.this.setFlushGap               (value);} });
    res.put("Memstore flush size(bytes)",      new SetMethod() { @Override public void set(String value) {Configuration.this.setMemstoreBytesSize      (value);} });
    res.put("Compaction algos count",          new SetMethod() { @Override public void set(String value) {Configuration.this.setCompactionAlgosCount   (value);} });
    res.put("KeyValue TTL(ms)",                new SetMethod() { @Override public void set(String value) {Configuration.this.setKeyValueTTL            (value);} });
    res.put("KeyValue TTL jitter",             new SetMethod() { @Override public void set(String value) {Configuration.this.setKeyValueTTLJitter      (value);} });
    res.put("Read bytes per second from HDFS", new SetMethod() { @Override public void set(String value) {Configuration.this.setHDFSReadBytesPerSecond (value);} });
    res.put("Write bytes per second to HDFS",  new SetMethod() { @Override public void set(String value) {Configuration.this.setHDFSWriteBytesPerSecond(value);} });
    return res;
  }// @formatter:on

  // ===================================================================
  // =============================<GETTERS>=============================
  // ===================================================================
  public long getFlushGap() {
    return this.flushGap;
  }

  public long getMemstoreBytesSize() {
    return this.memstoreBytesSize;
  }

  public long getCompactionAlgosCount() {
    return this.compactionAlgosCount;
  }

  public long getKeyValueTTL() {
    return this.keyValueTTL;
  }

  public double getKeyValueTTLJitter() {
    return this.keyValueTTLJitter;
  }

  public long getHDFSReadBytesPerSecond() {
    return this.HDFSReadBytesPerSecond;
  }

  public long getHDFSWriteBytesPerSecond() {
    return this.HDFSWriteBytesPerSecond;
  }

  public long getSimulationTimeDays() {
    return this.simulationTimeDays;
  }

  // ===================================================================
  // ============================</GETTERS>=============================
  // ===================================================================

  // ===================================================================
  // =============================<SETTERS>=============================
  // ===================================================================
  public void setFlushGap(String flushGap) {
    this.flushGap = Long.parseLong(flushGap);
  }

  public void setMemstoreBytesSize(String memstoreBytesSize) {
    this.memstoreBytesSize = Long.parseLong(memstoreBytesSize);
  }

  public void setCompactionAlgosCount(String compactionAlgosCount) {
    long cnt = Long.parseLong(compactionAlgosCount);
    if (cnt < 1 || cnt > this.MAX_COMPACTION_ALGOS_COUNT) {
      throw new NumberFormatException();
    }
    this.compactionAlgosCount = cnt;
  }

  public void setKeyValueTTL(String keyValueTTL) {
    this.keyValueTTL = Long.parseLong(keyValueTTL);
  }

  public void setKeyValueTTLJitter(String keyValueTTLJitter) {
    this.keyValueTTLJitter = Double.parseDouble(keyValueTTLJitter);
  }

  public void setHDFSReadBytesPerSecond(String HDFSReadBytesPerSecond) {
    this.HDFSReadBytesPerSecond = Long.parseLong(HDFSReadBytesPerSecond);
  }

  public void setHDFSWriteBytesPerSecond(String HDFSWriteBytesPerSecond) {
    this.HDFSWriteBytesPerSecond = Long.parseLong(HDFSWriteBytesPerSecond);
  }

  public void setSimulationTimeDays(String simulationTimeDays) {
    this.simulationTimeDays = Long.parseLong(simulationTimeDays);
  }
  // ===================================================================
  // ============================</SETTERS>=============================
  // ===================================================================
}