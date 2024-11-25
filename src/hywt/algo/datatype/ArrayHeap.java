package hywt.algo.datatype;

import java.util.Collection;
import java.util.Iterator;

public class ArrayHeap<E extends Comparable<E>> implements Collection<E> {

    private StatsArrayList<E> list;

    public ArrayHeap() {
        list = new StatsArrayList<E>();
    }

    public boolean add(E e) {
        list.add(e);
        int i = list.size() - 1;
        while (i > 0 &&
                list.get(i).compareTo(list.get(parent(i))) >= 0) {
            swap(i, parent(i));
            i = parent(i);
        }
        return true;
    }

    public E remove() {
        if (list.size() > 0) {
            if (list.size() > 1) {
                E e = list.statsGet(0);
                list.statsSet(0, list.removeLast());
                heapify(0);
                return e;
            } else
                return list.removeLast();
        }
        return null;
    }

    protected void heapify(int index) {
        int largest = index;
        int left = left(index);
        int right = right(index);

        if (left < list.size() && list.get(left).compareTo(list.get(largest)) > 0) {
            largest = left;
        }
        if (right < list.size() && list.get(right).compareTo(list.get(largest)) > 0) {
            largest = right;
        }

        if (largest != index) {
            swap(index, largest);
            heapify(largest);
        }
    }

    protected void swap(int i, int j) {
        E e = list.statsGet(i);
        list.statsSet(i, list.statsGet(j));
        list.statsSet(j, e);
    }

    public String prettyPrint() {
        StringBuffer buffer = new StringBuffer();
        buildTree(buffer, 0);
        return buffer.toString();
    }

    private void buildTree(StringBuffer buffer, int i) {
        int depth = depth(i);
        for (int index = 0; index < depth; index++) {
            buffer.append("    ");
        }

        buffer.append(list.get(i)).append("\n");

        int left = left(i);
        if (left < list.size())
            buildTree(buffer, left);
        int right = right(i);
        if (right < list.size())
            buildTree(buffer, right);
    }

    @Override
    public String toString() {
        return list.toString();
    }

    private static int left(int i) {
        return 2 * i + 1;
    }

    private static int right(int i) {
        return 2 * i + 2;
    }

    private static int parent(int i) {
        return (i - 1) / 2;
    }

    private static int depth(int i) {
        int depth = 0;
        while (i > 0) {
            i = parent(i);
            depth++;
        }
        return depth;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAll'");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        return list.toArray(arg0);
    }
}
