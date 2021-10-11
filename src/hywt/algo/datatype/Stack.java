package hywt.algo.datatype;

import java.util.ArrayList;
import java.util.Iterator;

public class Stack<E> implements Iterable<E> {
    private final ArrayList<E> list;
    private int ptr;

    public Stack() {
        list = new ArrayList<>();
        ptr = -1;
    }

    public void push(E element) {
        list.add(element);
        ptr++;
    }

    public E peek() {
        return peek(0);
    }

    public E peek(int depth) {
        return list.get(ptr - depth);
    }

    public E pop() {
        return list.remove(ptr--);
    }

    public boolean isEmpty() {
        return ptr < 0;
    }

    public int size() {
        return list.size();
    }

    public E get(int index) {
        return list.get(index);
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Iterator<E> iterator() {
        return list.iterator();
    }
}
