package Model.HBaseElements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * collection of storeFiles sorted by decreasing byte size
 * @author ibra
 */
public class StoreFileCollection implements Iterable<StoreFile> {

  /**
   * StoreFileCollection is a wrap over this list
   */
  private final List<StoreFile> storeFiles;

  /**
   * creates empty collection
   */
  public StoreFileCollection() {
    this.storeFiles = new ArrayList<StoreFile>();
  }

  /**
   * creates collection as clone of storeFileCollection
   */
  public StoreFileCollection(final StoreFileCollection storeFileCollection) {
    this.storeFiles = new ArrayList<StoreFile>(storeFileCollection.storeFiles);
  }

  /**
   * creates collection as collection of StoreFiles from storeFileList
   */
  private StoreFileCollection(final List<StoreFile> storeFileList) {
    this.storeFiles = new ArrayList<StoreFile>(storeFileList);
  }

  /**
   * adds storeFile to collection
   * @param storeFile - storeFile to be added
   */
  public void add(final StoreFile storeFile) {
    // if collection is empty or size of storeFile is bigger than any other in this collection, then
    // we insert it the beginning
    if (this.storeFiles.isEmpty()
        || storeFile.getBytesSize() > this.storeFiles.get(0).getBytesSize()) {
      this.storeFiles.add(0, storeFile);
    } else {
      // else find place to insert so that after insert collection to stay sorted
      for (int i = this.storeFiles.size() - 1; i >= 0; i--) {
        if (this.storeFiles.get(i).getBytesSize() >= storeFile.getBytesSize()) {
          this.storeFiles.add(i + 1, storeFile);
          break;
        }
      }
    }
  }

  /**
   * @param index index of storeFile to be returned
   * @return storeFile in [index] in this collection
   */
  public StoreFile get(final int index) {
    return this.storeFiles.get(index);
  }

  /**
   * removes specified element from the collection
   * @param storeFile - element to be removed from this collection
   */
  public void remove(final StoreFile storeFile) {
    this.storeFiles.remove(storeFile);
  }

  /**
   * removes all elements from this collection
   */
  public void clear() {
    this.storeFiles.clear();
  }

  /**
   * @param storeFile - storeFile to search
   * @return if collection contains this element
   */
  public boolean contains(final StoreFile storeFile) {
    return this.storeFiles.contains(storeFile);
  }

  /**
   * @return amount of elements in this collection
   */
  public int size() {
    return this.storeFiles.size();
  }

  /**
   * @return if collection is empty
   */
  public boolean isEmpty() {
    return this.storeFiles.isEmpty();
  }

  @Override
  public Iterator<StoreFile> iterator() {
    return this.storeFiles.iterator();
  }

  /**
   * @param fromIndex - begin index of sublist to be returned
   * @param toIndex - index after last index of sublist to be returned
   * @return sublist of this collection of range [fromIndex, toIndex)
   */
  public StoreFileCollection subList(final int fromIndex, final int toIndex) {
    return new StoreFileCollection(this.storeFiles.subList(fromIndex, toIndex));
  }
}
