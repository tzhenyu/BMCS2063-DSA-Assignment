/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ListADT;

import java.util.Comparator;

/**
 *
 * @author zhenyu
 */
public interface List<T> extends Iterable<T>{

    void insert(T element);

    boolean remove(T element);

    T search(T element);

    int size();

    boolean isEmpty();

    void clear();

    String display();
    
    void sort();
    
    void sort(Comparator<T> column);

    T earlyExitSearch(T element);
    
    T binarySearch(T element);
    
    T getEntry(int index);
}