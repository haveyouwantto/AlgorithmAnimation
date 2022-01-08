package hywt.algo.datatype;

import java.util.*;

public class OrderedPriorityQueue<E> implements Queue<E> {
    private LinkedList<E> list;
    private boolean comparable = false;
    private Comparator<? super E> comparator;

    public <T extends Comparable<? super T>> OrderedPriorityQueue() {
        list = new LinkedList<>();
        comparable = true;
    }

    public <T> OrderedPriorityQueue(Comparator<? super T> comparator) {
        list = new LinkedList<>();
        this.comparator = (Comparator<? super E>) comparator;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(E e) {
        int index = 0;
        for (E element : list) {
            if (compareElement(e, element) < 0) {
                list.add(index, e);
                return true;
            }
            index++;
        }
        return list.add(e);
    }

    private int compareElement(E e1, E e2) {
        if (comparable) {
            return ((Comparable<E>) e1).compareTo(e2);
        }
        return comparator.compare(e1, e2);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return list.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        return list.remove();
    }

    @Override
    public E poll() {
        return list.poll();
    }

    @Override
    public E element() {
        return list.element();
    }

    @Override
    public E peek() {
        return list.peek();
    }

    @Override
    public String toString() {
        return list.toString();
    }

    public void fetch(List<E> target) {
        E element = list.poll();
        target.add(element);
        while (list.size() > 0 && compareElement(element, list.peek()) >= 0) {
            target.add(list.poll());
        }
    }
}
