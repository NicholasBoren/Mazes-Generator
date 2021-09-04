package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    private int size;
    private HashMap<T, Integer> record;

    public ArrayHeapMinPQ() {
        record = new HashMap<>();
        items = new ArrayList<>();
        size = 0;
        items.add(null);
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
        record.put(temp.getItem(), b);
        record.put(items.get(a).getItem(), a);
    }

    @Override
    public void add(T item, double priority) {  //Wu: we need to consider resize
        if (size == 0) {
            size++;
            items.add(START_INDEX, new PriorityNode(item, priority)); // I think we can remove the start_index
            record.put(item, START_INDEX);
        } else {
            if (contains(item)) {
                throw new IllegalArgumentException();
            }
            size++;
            items.add(size, new PriorityNode<>(item, priority));
            record.put(item, size);
            percolateUp(size);
        }


    }

    private void percolateUp(int currIndex) {
        if (currIndex != START_INDEX) {
            PriorityNode<T> currNode = items.get(currIndex);
            PriorityNode<T> parentNode = items.get(currIndex / 2);
            record.put(currNode.getItem(), currIndex);
            record.put(parentNode.getItem(), currIndex / 2);
            if (currNode.getPriority() < parentNode.getPriority()) {
                swap(currIndex, currIndex / 2);
                percolateUp(currIndex / 2);
            }
        }
    }

    @Override
    public boolean contains(T item) {
        return record.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException("PQ is empty");
        }

        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException("PQ is empty");
        }

        if (size == 1) {
            size--;
            record.remove(items.get(START_INDEX).getItem());
            return items.remove(START_INDEX).getItem();
        } else {

            PriorityNode<T> temp = items.get(START_INDEX);
            swap(START_INDEX, size);
            record.remove(temp.getItem());
            // items.set(START_INDEX, items.get(size));
            items.remove(size);
            size--;
            // record.put(items.get(START_INDEX).getItem(), START_INDEX);
            percolateDown(START_INDEX);


            return temp.getItem();
        }
    }

    private void percolateDown(int currIndex) {
        if (currIndex * 2 <= size) {
            PriorityNode<T> parent = items.get(currIndex);
            PriorityNode<T> leftChild = items.get(2 * currIndex);
            PriorityNode<T> rightChild = new PriorityNode<>(null, Integer.MAX_VALUE);
            if (2 * currIndex + 1 <= size) {
                rightChild = items.get(2 * currIndex + 1);
            }

            if (parent.getPriority() > leftChild.getPriority() || parent.getPriority() > rightChild.getPriority()) {
                if (rightChild.getPriority() > leftChild.getPriority()) {
                    swap(currIndex, 2 * currIndex);
                    percolateDown(2 * currIndex);
                } else if (leftChild.getPriority() > rightChild.getPriority()) {
                    swap(currIndex, 2 * currIndex + 1);
                    percolateDown(2 * currIndex + 1);
                }
            }
        }
    }

    @Override
    public void changePriority(T item, double priority) { // we still need to work on this a bit
        if (!record.containsKey(item)) {
            throw new NoSuchElementException();
        }

        int index = record.get(item);
        PriorityNode<T> current = items.get(index);
        double temp = current.getPriority();

        current.setPriority(priority);
        if (temp < priority)
        {
            percolateDown(index);
        }
        else if (temp > priority)
        {
            percolateUp(index);
        }
    }

    @Override
    public int size() {
        return size;
    }

    //Questions for the TA:
    // should we put in an empty node at the beggining of the list if we are setting the start index as 1?
    // should I use Objects.equals(leftChild, null)?
}
