package cn.jc.util;

/**
 * Created by Administrator on 2016/6/1.
 */
public interface Collection<E> extends Iterable<E>{
    int size();

    boolean isEmpty();

    boolean contains(Object o);

    Iterator<E> iterator();

    Object[] toArray();

    <T> T[] toArray(T[] a);

    boolean add(E e);

    /**
     * 删除包含 (o==null ? e==null : o.equals(e))的元素e
     * @param o
     * @return
     */
    boolean remove(Object o);

    boolean containsAll(Collection<?> c);

    boolean addAll(Collection<? extends E> c);

    boolean removeAll(Collection<?> c);

    boolean retainAll(Collection<?> c);

    void clear();

    boolean equals(Object o);

    int hashCode();
}
