package cn.jc.util;

/**
 * Created by Administrator on 2016/6/1.
 */
public interface Iterator<E> {
    boolean hasNext();

    E next();

    void remove();
}
