import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList. An Iterator with
 * working remove() method is implemented, but ListIterator is unsupported.
 * 
 * @author Chloe Johnson
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private LinearNode<T> head, tail;
	private int size;
	private int modCount;

	/** Creates an empty list */
	public IUSingleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		LinearNode<T> current = null;
		if (isEmpty()) {
			head = new LinearNode<T>(element);
			tail = head;
		} else if (size == 1) {
			head = new LinearNode<T>(element);
			head.setNext(tail);
		} else {
			current = head;
			head = new LinearNode<T>(element);
			head.setNext(current);
		}
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		LinearNode<T> current = null;
		if (isEmpty()) {
			head = new LinearNode<T>(element);
			tail = head;
		} else if (size == 1) {
			tail = new LinearNode<T>(element);
			head.setNext(tail);
		} else {
			current = tail;
			tail = new LinearNode<T>(element);
			current.setNext(tail);
		}
		size++;
		modCount++;
	}

	@Override
	public void add(T element) {
		LinearNode<T> current = null;
		if (isEmpty()) {
			head = new LinearNode<T>(element);
			tail = head;
		} else if (size == 1) {
			tail = new LinearNode<T>(element);
			head.setNext(tail);
		} else {
			current = tail;
			tail = new LinearNode<T>(element);
			current.setNext(tail);
		}
		size++;
		modCount++;
	}

	@Override
	public void addAfter(T element, T target) {
		LinearNode<T> current = head;
		LinearNode<T> newNode = new LinearNode<T>(element);
		boolean isFound = false;
		while (current != null) {
			if (current.getElement().equals(target)) {
				newNode.setNext(current.getNext());
				current.setNext(newNode);
				isFound = true;
			}
			current = current.getNext();
		}
		if (!isFound) {
			throw new NoSuchElementException();
		}
		if (newNode.getNext() == null) {
			tail = newNode;
		}
		size++;
		modCount++;
	}

	@Override
	public void add(int index, T element) {
		LinearNode<T> current = head;
		LinearNode<T> newNode = new LinearNode<T>(element);
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index > size) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			if (size == 0) {
				head = newNode;
				tail = newNode;
			} else {
				newNode.setNext(head);
				head = newNode;
			}
		} else {
			for (int i = 0; i < index - 1; i++) {
				current = current.getNext();
			}
			if (current.equals(tail)) {
				current.setNext(newNode);
				tail = newNode;
			} else {
				newNode.setNext(current.getNext());
				current.setNext(newNode);
			}
		}
		size++;
		modCount++;
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = head.getElement();
		head = head.getNext();
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T removeLast() {
		LinearNode<T> current = head;
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = tail.getElement();
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
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		boolean found = false;
		LinearNode<T> previous = null;
		LinearNode<T> current = head;

		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				previous = current;
				current = current.getNext();
			}
		}

		if (!found) {
			throw new NoSuchElementException();
		}

		if (size() == 1) { // only node
			head = tail = null;
		} else if (current == head) { // first node
			head = current.getNext();
		} else if (current == tail) { // last node
			tail = previous;
			tail.setNext(null);
		} else { // somewhere in the middle
			previous.setNext(current.getNext());
		}

		size--;
		modCount++;

		return current.getElement();
	}

	@Override
	public T remove(int index) {
		LinearNode<T> previous = null;
		LinearNode<T> current = head;
		T retVal = null;
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
		if (size == 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			retVal = head.getElement();
			head = current.getNext();
		} else {
			for (int i = 0; i < index - 1; i++) {
				current = current.getNext();
			}
			if (current.getNext() == tail) {
				previous = current;
				retVal = tail.getElement();
				previous.setNext(null);
				tail = previous;
			} else {
				previous = current;
				current = current.getNext();
				retVal = current.getElement();
				previous.setNext(current.getNext());
			}
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		LinearNode<T> current = head;
		if (index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			head.setElement(element);
		} else {
			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}
			current.setElement(element);
		}
		modCount++;
	}

	@Override
	public T get(int index) {
		LinearNode<T> current = head;
		T retVal = null;
		if (index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			retVal = head.getElement();
		} else {
			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}
			retVal = current.getElement();
		}
		return retVal;
	}

	@Override
	public int indexOf(T element) {
		int index = -1;
		if (!isEmpty()) {
			LinearNode<T> current = head;
			while (current != null) {
				index++;
				if (current.getElement().equals(element)) {
					return index;
				} else {
					current = current.getNext();
				}
			}
			if (current == null) {
				index = -1;
			}
		}
		return index;
	}

	@Override
	public T first() {
		T retVal = null;
		if (isEmpty()) {
			throw new NoSuchElementException();
		} else {
			retVal = head.getElement();
		}
		return retVal;
	}

	@Override
	public T last() {
		T retVal = null;
		if (isEmpty()) {
			throw new NoSuchElementException();
		} else {
			retVal = tail.getElement();
		}
		return retVal;
	}

	@Override
	public boolean contains(T target) {
		return indexOf(target) != -1;
	}

	@Override
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * Converts the list to a readable string
	 * 
	 * @return String
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		LinearNode<T> current = head;
		T currentElement = null;
		str.append("[");
		while (current != null) {
			currentElement = current.getElement();
			str.append(currentElement.toString());
			str.append(" , ");
			current = current.getNext();
		}
		if (!isEmpty()) {
			str.delete(str.length() - 2, str.length());
		}
		str.append("]");
		return str.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private LinearNode<T> nextNode;
		private int iterModCount;
		private boolean canRemove;

		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != null);
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
			nextNode = nextNode.getNext();
			canRemove = true;
			return retVal;
		}

		@Override
		public void remove() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (!canRemove) {
				throw new IllegalStateException();
			}
			if (head == tail) {
				head = tail = null;
			} else if (head.getNext() == nextNode) {
				head = head.getNext();

			} else {
				LinearNode<T> current = head;
				while (current.getNext().getNext() != nextNode) {
					current = current.getNext();
				}
				current.setNext(nextNode);
				if (nextNode == null) {
					tail = current;
				}
			}

			canRemove = false;
			size--;
			modCount++;
			iterModCount++;
		}
	}
}
