package cn.jc.util;

/**
 * Created by Administrator on 2016/6/1.
 */
public interface ListIterator<E> extends Iterator<E> {

    boolean hasNext();

    E next();

    void remove();

    /*******************************
     * new method for this interface
     *******************************/

    boolean hasPrevious();

    E previous();

    int nextIndex();

    int previousIndex();

    void set(E e);

    void add(E e);
}
