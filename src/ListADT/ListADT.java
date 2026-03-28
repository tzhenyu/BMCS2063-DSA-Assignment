/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ListADT;

import java.util.Iterator;
import java.util.function.Consumer;


/**
 *
 * @author zhenyu
 * @author katie
 * @author mahir
 * @author jonathan
 */



public class ListADT<T extends Comparable<T>> implements ListADTInterface<T>{
    
    // We develop "circular" linked list with "dummy" node
    // What is dummy node? check here https://www.youtube.com/watch?v=aloIxnZ4EvY
    
    private Node<T> dummy; // We declare a dummy node first.
    
    // constructor where we define the linked list with dummy node first.
    // it looks like this:
    // dummy <- dummy
    public ListADT() {
        dummy = new Node<>(null); //defines dummy node with no data.
        dummy.setNextNode(dummy); //point itself, circles back.
    }
    
    @Override
    public void insert(T element) {
        
        Node<T> newNode = new Node<>(element); // defines new node
        Node<T> last = dummy; //defines dummy as last node
        
        // say we have: dummmy <- firstnode <- dummy
        
        // finds the last, real node
        // dummy <- firstnode <- dummy
        //             ^^
        //          this one
        while (last.getNextNode() != dummy) {
            last = last.getNextNode();
        }
        
        // insert new node if next node is dummy, 
        // dummy <- firstnode <- secondnode
        last.setNextNode(newNode);  
        
        // bind new node to dummy for circulation
        // dummy <- firstnode <- secondnode <- dummy
        newNode.setNextNode(dummy); 
        
    }

    @Override
    public boolean remove(T element) {
         Node<T> previous = dummy;
         Node<T> current = dummy.getNextNode();
         
         //Loop until we circle back to the dummy node
         while (current != dummy){
             //check if the current node holds the data we want to remove
             if(current.getData().equals(element)){
                 //Reroute the previous node's pointer to skip the current node
                 previous.setNextNode(current.getNextNode());
                 return true;
             }
             //Move pointers forward
             previous = current;
             current = current.getNextNode();
         }
         return false;
    }

    @Override
    public T search(T element) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isEmpty() {
        // the mode is empty if and only if the dummy <- dummy
        return dummy.getNextNode() == dummy;
    }

    @Override
    public void clear() {
        dummy.setNextNode(dummy);
    }

    @Override
    public String display() {
        //If the dummy points to itself,the list is empty
        if(dummy.getNextNode() == dummy){
            return "The list is empty.\n";
        }
        StringBuilder output = new StringBuilder();
        Node<T> current = dummy.getNextNode();
        
        while(current != dummy){
            output.append(current.getData().toString());
            current = current.getNextNode();
        }
        return output.toString();
    }
    
    @Override
    public void sort() {
        // referenced from: https://www.youtube.com/shorts/USyuCNeCxKU
 
        // Step 1: break the circular chain
        breakCircular();

        // get head node so the dummy node wont be included for the merge sort
        Node<T> head = dummy.getNextNode();
        
        // Step 2: find the middle of linked list
        // Step 3: sort and merge the linked list
        head = mergeSort(head);
        
        // Step 4: reconnect circular chain
        mergeCircular(head);
    }
    
    private Node<T> mergeSort(Node<T> element) {
        // if linked list is empty
        if (element == null || element.getNextNode() == null)
            return element;
        
        // find the middle of linked list
        Node<T> middle = getMiddle(element);
        Node<T> nextOfMiddle = middle.getNextNode();
        
        // split linked list to 2 part
        middle.setNextNode(null);
        // sort each part
        Node<T> left = mergeSort(element);
        Node<T> right = mergeSort(nextOfMiddle);
        
        // after sorting, merge it
        return merge(left, right);
    }
    
    private Node<T> getMiddle(Node<T> element) {
        // got slow pointer and fast pointer
        // say we got A-B-C-D-E-F
        // slow: A-B-C
        // fast: A-C-E
        // last one from fast: E-F correct, but E-G is null, break
        

        Node<T> slow = element;
        Node<T> fast = element;
    
        while (fast.getNextNode() != null && fast.getNextNode().getNextNode() != null) {
            slow = slow.getNextNode();
            fast = fast.getNextNode().getNextNode();
        }   
        
        // returns the slow one because when fast reached at the end of the linked list,
        // the slow one is usually at middle one
        return slow;
    }

    private Node<T> merge(Node<T> a, Node<T> b) {
        // sort and merge
        
        //both is for when after split, only got 1 linked list
        if (a == null) { 
            return b;
        }
        
        if (b == null) { 
            return a;
        }

        Node<T> result;
        
        // compare and sort then merge
        if (a.getData().compareTo(b.getData()) <= 0) {
            result = a;
            result.setNextNode(merge(a.getNextNode(), b));
        } else {
            result = b;
            result.setNextNode(merge(a, b.getNextNode()));
        }

        return result;
    }
    
    private void breakCircular() {
        // this is to help doing merge sort later
        
        Node<T> last = dummy; //defines dummy as last node
        while (last.getNextNode() != dummy) {
            last = last.getNextNode();
        }
        
        // sets the last node to null to break circular
        last.setNextNode(null); 
        
        
    }
    
    private void mergeCircular(Node<T> element) {
        // sets the last node back to dummy to make linked list circular again
        
        // if its empty linked list
        if (element == null) {
            dummy.setNextNode(dummy);
            return;
        }
        
        // else,
        // point dummy to head node
        dummy.setNextNode(element);
        
        Node<T> last = element;
        
        // find the last node
        while (last.getNextNode() != null) {
            last = last.getNextNode();
        }
        // point last node to dummy
        last.setNextNode(dummy);  
    }
    
    @Override
    public Iterator<T> iterator() {
        return new LLIterator();
    }
    
    private class LLIterator implements Iterator<T> {
        // referenced from https://drive.google.com/drive/u/1/folders/1OO0bQtvYea_y3t0NaHI_sDGSrnQnvQvC
        
        private Node<T> current = dummy.getNextNode();
        
        @Override
        public boolean hasNext() {
            // as long no dummy then its not last then can be next
            return current != dummy;
        }

        @Override
        public T next() {
            // throw error if no next 
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            T data = current.getData();
            current = current.getNextNode(); // move to next node
            
            return data;
        }

        @Override
        public void remove() {
            Iterator.super.remove(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            Iterator.super.forEachRemaining(action); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }
        
    }
    
    
}