package Model.Compactors;

import Model.HBaseElements.StoreFile;

/**
 * CompactionResult class is a class that stores information about result of compaction.
 * @author ibra
 *
 */
public class CompactionResult {

  static final CompactionResult NOT_PERFORMED = new CompactionResult(false, 0, 0, null);

  /**
   * amount of bytes read/written during compaction
   */
  public long readBytes, writtenBytes;

  /**
   * a compacted file
   */
  public StoreFile compacted;

  /**
   * flag that indicates if compaction was performed or not
   */
  public boolean isPerformed;

  CompactionResult(boolean isPerformed, long readBytes, long writtenBytes, StoreFile compacted) {
    this.isPerformed = isPerformed;
    this.readBytes = readBytes;
    this.writtenBytes = writtenBytes;
    this.compacted = compacted;
  }
}
