package cn.jc.util;

import java.util.ListIterator;

/**
 * Created by Administrator on 2016/6/1.
 */
public interface List<E> extends Collection<E>{
    int size();

    boolean isEmpty();

    boolean contains(Object e);

    Iterator<E> iterator();

    Object[] toArray();

    <T> T[] toArray(T[] a);

    boolean add(E e);

    boolean remove(Object o);

    boolean containAll(Collection<?> c);

    boolean addAll(Collection<? extends E> c);

    /**
     * a new method for list interface.
     * @param index
     * @param c
     * @return
     */
    boolean addAll(int index, Collection<? extends E> c);

    boolean removeAll(Collection<?> c);

    boolean retain(Collection<?> c);

    void clear();

    boolean equals(Object o);

    int hashCode();

    /*****************************************
     * new methods for list interface
     *****************************************/

    E get(int index);

    E set(int index, E element);

    void add(int index, E element);

    E remove(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    ListIterator<E> listIterator();

    ListIterator<E> listIterator(int index);

    List<E> subList(int fromIndex, int toIndex);

}
