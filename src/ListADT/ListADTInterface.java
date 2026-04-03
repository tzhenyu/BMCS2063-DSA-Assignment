/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ListADT;

/**
 *
 * @author zhenyu
 */
public interface ListADTInterface<T> extends Iterable<T>{

    /** 
     * Inserts a new element in the list in sorted order
     * PRE: element != null
     * POST: element is inserted and list remains sorted
     */
    void insert(T element);

    /**
     * Removes an element by its key or object
     * PRE: element != null
     * POST: element is removed if found
     * @return true if removal is successful, false otherwise
     */
    boolean remove(T element);

    /**
     * Searches for an element by key or object
     * @param element element to search for
     * @return the element if found, null otherwise
     */
    T search(T element);

    /**
     * Returns the number of elements in the list
     */
    int size();

    /**
     * Checks whether the list is empty
     * @return true if list has no elements, false otherwise
     */
    boolean isEmpty();

    /**
     * Removes all elements from the list
     */
    void clear();

    /**
     * Traverses the list and displays elements in order
     */
    String display();
    
    void sort();

    public T getEntry(int i);
    
}