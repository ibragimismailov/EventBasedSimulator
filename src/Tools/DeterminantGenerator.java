package Tools;

import Model.Configuration;

/**
 * DeterminantGenerator - class, used for getting determinied values
 * @author ibra
 */
class DeterminantGenerator extends Generator {

  DeterminantGenerator() {
  }

  /**
   * @return keyValue TTL - random value in range [TTL-TTL*jitter, TTL+TTL*jitter]
   */
  @Override
  public long getKeyValueTTL() {
    return Configuration.INSTANCE.getKeyValueTTL();
  }

  /**
   * @param majorCompactionGap base value of major compaction gap
   * @param majorCompactionJitter jitter value of major compaction gap
   * @return majorCompactionGap - random value in range [majorCompactionGap-majorCompactionGap*jitter, 
   *         majorCompactionGap+majorCompactionGap*jitter]
   */
  @Override
  public long getMajorCompactionGap(final long majorCompactionGap,
      final double majorCompactionJitter) {
    return majorCompactionGap;
  }
}
