import java.util.NoSuchElementException;

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

	private Node<E> head;
	private Node<E> tail;
	protected Node<E> curr;
	int cnt;
	int size;
	int level;

	/*
	 * Creates a new Cache of a specified size and level.
	 */
	public Cache(int size, int level) throws NoSuchLevelFoundException {
		if ((level < 1) || (level > 2)) {
			throw new NoSuchLevelFoundException("Level must be 1 or 2");
		}
		curr = tail = head = new Node<E>(null);
		cnt = 0;
		this.level = level;
		this.size = size;
	}

	/**
	 * Adds a data item to the front of the Cache.
	 * 
	 * @param e
	 */
	public void addObject(E e) {
		if (cnt == 0) {
			head.setElement(e);
			tail = curr = head;
		} else if (cnt == 1) {
			head = new Node<E>(e, tail);
		} else {
			curr = head;
			head = new Node<E>(e, curr);
			if (cnt == size) {
				removeLastObject();
			}

		}
		cnt++;
	}

	/**
	 * Returns the current data item.
	 * 
	 * @return E
	 */
	public E getObject() {
		E it = curr.getElement();
		return it;
	}

	/**
	 * Removes the item after the current item. This method is not designed to
	 * remove the head.
	 * 
	 * @return E
	 */
	public E removeObject() {
		if (head != null) {
			E it = curr.getElement();
			if (cnt == 1) {
				curr = head = tail = null;
			} else if (curr.getNext() == tail) {
				it = curr.getNext().getElement();
				curr.setNext(curr.getNext().getNext());
				tail = curr;
			} else if (curr == tail) {
				throw new NoSuchElementException();
			} else {
				it = curr.getNext().getElement();
				curr.setNext(curr.getNext().getNext());
			}
			cnt--;
			return it;
		} else {
			throw new NoSuchElementException();
		}
	}
	
	public E removeLastObject() {
		Node<E> current = head;
		if (cnt == 0) {
			throw new NoSuchElementException();
		}
		E it = tail.getElement();
		if (size == 1) {
			head = null;
			tail = null;
		} else {
			while (current.getNext() != tail) {
				current = current.getNext();
			}
			current.setNext(null);
			tail = current;
		}
		cnt--;
		return it;
	}

	/**
	 * Sets the value of the current node to the value before the node where the element is found.
	 * 
	 * @param element
	 * @throws Exception 
	 */
	public void setCurr(E element) throws Exception {
		curr = head;
		while (!curr.getNext().getElement().equals(element)) {
			curr = curr.getNext();
			if (curr.getNext() == null) {
				throw new Exception("No element found");
			}
		}
	}

	/**
	 * Returns the cache level.
	 * 
	 * @return int
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Removes the data item and adds it to the front of the list
	 */
	public void moveToFront() {
		E it = removeObject();
		Node<E> element = new Node<E>(it, head);
		head = element;
		cnt++;
	}

	/**
	 * Returns the next node.
	 * 
	 * @return Node<E>
	 */
	public Node<E> getNext() {
		return curr.getNext();
	}

	/**
	 * Removes all data items from the cache.
	 */
	public void clearCache() {
		curr = head = tail = null;
	}

	/**
	 * Searches the cache for the specified word. If the word isn't found, it's
	 * added to the front of the cache.
	 * 
	 * @param word
	 * @return
	 */
	public int search(E word) {
		int hits = 0;
		curr = head;
		while (curr != tail) {
			if (curr.getNext().getElement().equals(word)) {
				hits++;
				moveToFront();
			}
			if (curr != tail) {
				curr = curr.getNext();
			}
		}
		if (hits == 0) {
			addObject(word);
		}
		return hits;
	}

	/**
	 * Searches the cache for the specified word. If the cache is level 1, a level 2
	 * cache must be specified. If the cache is level 2, a level 1 cache must be
	 * specified. When the cache is level 1, it is searched for the word. If it
	 * can't be found, a value of 0 is returned. If it can be found, the data item
	 * is moved to the front of both caches and 1 is returned. When the cache is
	 * level 1 and the data item can't be found, it's added to both caches and and 0
	 * is returned. If the data item is found, it's moved to the front of the level
	 * 2 cache and it's added to the level 1 cache. Then 1 is returned.
	 * 
	 * @param element
	 * @param cache
	 * @return
	 * @throws Exception
	 */
	public int search(E element, Cache<E> cache) throws Exception {
		int hits = 0;
		curr = head;
		if (level == 1) {
			if (cache.getLevel() != 2) {
				throw new Exception("Level for the second cache must equal 2");
			}

			while (curr != tail) {
				if (curr.getNext().getElement().equals(element)) {
					hits++;
					moveToFront();
					cache.setCurr(element);
					cache.moveToFront();
					return hits;
				}
				if (curr != tail) {
					curr = curr.getNext();
				}
			}
		} else {
			if (cache.getLevel() != 1) {
				throw new Exception("Level for the first cache must equal 1");
			}

			while (curr != tail) {
				if (curr.getNext().getElement().equals(element)) {
					hits++;
					moveToFront();
					cache.addObject(element);
					return hits;
				}
				if (curr != tail) {
					curr = curr.getNext();
				}
			}
			if (hits == 0) {
				addObject(element);
				cache.addObject(element);
			}
		}
		return hits;
	}

	/**
	 * Returns the cache as a printable string object.
	 * 
	 * @return String
	 */
	public String toString() {
		curr = head;
		String retVal = "";
		while (curr != null) {
			retVal += curr.getElement() + " ";
			curr = curr.getNext();
		}
		return retVal;
	}
}