

/**
 * The Cache class implements a single linked list categorized as level 1 or
 * level 2. Categorization of the levels is necessary for the search function.
 * The search function distinguishes the Cache class from a single linked list
 * through the use of operations that check the Cache for a data item.
 * 
 * 
 * @author chloejohnson
 *
 * @param <E>
 */
public class Cache<E> {

	private int cnt;
	private int size;
	private IUSingleLinkedList<E> list;
	

	/*
	 * Creates a new Cache of a specified size and level.
	 */
	public Cache(int size) {
		
		cnt = 0;
		this.size = size;
		list = new IUSingleLinkedList<E>();
	}

	/**
	 * Adds a data item to the front of the Cache.
	 * 
	 * @param e
	 */
	public void addObject(E e) {
		list.addToFront(e);
		if (size == cnt) {
			removeLast();
		}
		cnt++;
	}

	/**
	 * Removes the item after the current item. This method is not designed to
	 * remove the head.
	 * 
	 * @return E
	 */
	public E removeObject(E e) {
		list.remove(e);
		cnt--;
		return e;
	}
	
	public E removeLast() {
		E e = list.removeLast();
		cnt--;
		return e;
	}

	/**
	 * Removes the data item and adds it to the front of the list
	 */
	public void moveToFront(E e) {
		E it = removeObject(e);
		addObject(it);
	}

	/**
	 * Removes all data items from the cache.
	 */
	public void clearCache() {
		list = new IUSingleLinkedList<E>();
	}

	/**
	 * Searches the cache for the specified word. If the word isn't found, it's
	 * added to the front of the cache.
	 * 
	 * @param word
	 * @return
	 */
	public int search(E element) {
		int hits = 0;
		if (list.contains(element)) {
			moveToFront(element);
			hits++;
		}
		else {
			addObject(element);
		}
		return hits;
	}
	
	public String toString() {
		return list.toString();
	}
}
