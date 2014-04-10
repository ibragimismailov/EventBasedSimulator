package Tools;

public abstract class Generator {

  private static boolean isRand = false;

  private static DeterminantGenerator det = new DeterminantGenerator();
  private static RandomGenerator rand = new RandomGenerator();

  public static Generator getGenerator() {
    if (isRand) {
      return rand;
    } else {
      return det;
    }
  }

  /**
   * @return keyValue TTL
   */
  public abstract long getKeyValueTTL();

  /**
   * @param majorCompactionGap base value of major compaction gap
   * @param majorCompactionJitter jitter value of major compaction gap
   * @return majorCompactionGap - random value in range [majorCompactionGap-majorCompactionGap*jitter, 
   *         majorCompactionGap+majorCompactionGap*jitter]
   */
  public abstract long getMajorCompactionGap(final long majorCompactionGap,
      final double majorCompactionJitter);
}
