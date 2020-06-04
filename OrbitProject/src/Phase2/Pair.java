package Phase2;

public class Pair <E, V> {
    private E first;
    private V second;
    public Pair(E e, V v) {
        first = e;
        second = v;
    }

    public E getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public void setFirst(E e) {
        first = e;
    }

    public void setSecond(V v) {
        second = v;
    }
}
