/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ListADT;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;
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
    private int numberOfNodes; 
    
    private Node<T> dummy; // We declare a dummy node first.
    private Node<T> tail; // Tail node for o(1) tail insertion
    
    //global numberOfNodes for o(1) 
    private int numberOfNodes; 

    // constructor where we define the linked list with dummy node first.
    // it looks like this:
    // dummy <- dummy
    public ListADT() {
        dummy = new Node<>(null); //defines dummy node with no data.
        dummy.setNextNode(dummy); //point itself, circles back.
        numberOfNodes = 0; //new list always empty
    }
    
    @Override
    public void insert(T element) {
        // Previous implementation adds new node in the tail with no tail node
        // which causes o(n) 
        // Current implementation added tail pointer to insert in o(1)
        Node<T> newNode = new Node<>(element);
        
        if (isEmpty()) {
            dummy.setNextNode(newNode);
            newNode.setNextNode(dummy);
            tail = newNode;
        } else {
            newNode.setNextNode(dummy); // points new node to dummy
            tail.setNextNode(newNode);
            tail = newNode;
        }
        
        // insert new node if next node is dummy, 
        // dummy <- firstnode <- secondnode
        last.setNextNode(newNode); 
        
        
        // bind new node to dummy for circulation
        // dummy <- firstnode <- secondnode <- dummy
        newNode.setNextNode(dummy); 
        numberOfNodes++;
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
                 numberOfNodes--;
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
        // To use search, have to create a object with the element 
        // eg. target = new Doctor("A002");  Doctor.search(target);
        // Start from the first actual data node
        Node<T> current = dummy.getNextNode();

        // Traverse until we loop back to the dummy placeholder
        while (current != dummy) {
            // If the data matches, return the ACTUAL object from the list
            if (current.getData().equals(element)) {
                return current.getData();
            }
            // Move to the next node
            current = current.getNextNode();
        }

        // If we looped through everything and found nothing
        return null;
    }
    
    public T earlyExitSearch(T element) {
        // USE ONLY ON SORTED
        Node<T> current = dummy.getNextNode();
        // Traverse until we loop back to the dummy placeholder
        while (current != dummy) {
            // If the data matches, return the ACTUAL object from the list
            if (current.getData().equals(element)) {
                return current.getData();
            }
            
            // Early Exit condition
            if ((current.getData()).compareTo(element) > 0) {
                return null; 
            }
            
            // Move to the next node
            current = current.getNextNode();
        }

        // If we looped through everything and found nothing
        return null;
    }
    
    public T binarySearch(T element) {
        // USE ONLY ON SORTED
        //Check if list empty -> dummy start, dummy last, both null
        //if list not empty 
        if (!this.isEmpty()) { 
            //initalize boundaries based on dummy
            Node<T> start = dummy.getNextNode();
            Node<T> end = dummy;
            
            // pass to start recursion
            return binarySearch(start, end, element);
        }
        return null;
    }
    
    public T binarySearch(Node<T> start, Node<T> end, T element) {
        //Check if list empty -> dummy start, dummy last, both null 
        if (start == null || start == end) {
            return null;
        }
        
        //set first node as middle of list
        Node<T> middle = getMiddle(start, end);
            
        //compare == , < , > 
        if (middle.getData().compareTo(element) == 0) { // element same then return itself
            return middle.getData();
        } else if (middle.getData().compareTo(element) > 0) { // element less
            return binarySearch(start, middle, element);
        } else { //element greater
            return binarySearch(middle.getNextNode(), end, element);
        }
    }
    
    @Override
    public int size() {
        return numberOfNodes;
    }

    @Override
    public boolean isEmpty() {
        // the mode is empty if and only if the dummy <- dummy
        // For better integration and future maintenance this code checks for numberOfNodes
        // dummy <- dummy is same as numberOfNodes == 0 
        return (numberOfNodes == 0);
    }

    @Override
    public void clear() {
        dummy.setNextNode(dummy);
        numberOfNodes = 0; //reset
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
        // Step 0: do nothing if list empty
        if (dummy.getNextNode() == dummy || dummy.getNextNode().getNextNode() == dummy)  {
            return;
        }
        
        
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

    public void sort(Comparator<T> column) {
        // referenced from: https://www.youtube.com/shorts/USyuCNeCxKU
        // Step 0: do nothing if list empty
        if (dummy.getNextNode() == dummy || dummy.getNextNode().getNextNode() == dummy)  {
            return;
        }
        
        
        // Step 1: break the circular chain
        breakCircular();

        // get head node so the dummy node wont be included for the merge sort
        Node<T> head = dummy.getNextNode();
        
        // Step 2: find the middle of linked list
        // Step 3: sort and merge the linked list
        head = mergeSort(head, column);
        
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
    
    private Node<T> mergeSort(Node<T> element, Comparator<T> comparator) {
        // if linked list is empty
        if (element == null || element.getNextNode() == null)
            return element;
        
        // find the middle of linked list
        Node<T> middle = getMiddle(element);
        Node<T> nextOfMiddle = middle.getNextNode();
        
        // split linked list to 2 part
        middle.setNextNode(null);
        
        // sort each part
        Node<T> left = mergeSort(element, comparator);
        Node<T> right = mergeSort(nextOfMiddle, comparator);
        
        // after sorting, merge it
        return merge(left, right, comparator);
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
    
    private Node<T> getMiddle(Node<T> start, Node<T> end) {
    // Safety check
    if (start == null || start == end) {
        return start;
    }

    Node<T> slow = start;
    Node<T> fast = start;

    // The fast pointer stops if it reaches the 'end' boundary
    while (fast != end && fast.getNextNode() != end) {
        slow = slow.getNextNode();
        fast = fast.getNextNode().getNextNode();
    }   
    
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
    
    private Node<T> merge(Node<T> a, Node<T> b, Comparator<T> comparator) {
        // sort and merge
            
        //both is for when after split, only got 1 linked list
        if (a == null) { 
            return b;
        }
        
        if (b == null) { 
            return a;
        }

        Node<T> result;
        
        // referenced from https://www.w3schools.com/java/java_advanced_sorting.asp
        // compare and sort then merge
        if (comparator.compare(a.getData(), b.getData()) <= 0) {
            result = a;
            result.setNextNode(merge(a.getNextNode(), b, comparator));
        } else {
            result = b;
            result.setNextNode(merge(a, b.getNextNode(), comparator));
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
    
    public T getEntry(int index) {
        //TAN: FILLER FROM AI
        // 1. Boundary check
        if (index < 0 || index >= numberOfNodes) {
            return null;
        }

        // 2. Start at the beginning (the head node)
        Node<T> currentNode = dummy.getNextNode();

        // 3. Traverse the chain to the specific index
        for (int counter = 0; counter < index; counter++) {
            currentNode = currentNode.getNextNode();
        }

        // 4. Return the data stored in that node
        return currentNode.getData();
    };
    
    @Override
    public Iterator<T> iterator() {
        return new LLIterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        ListADTInterface.super.forEach(action); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Spliterator<T> spliterator() {
        return ListADTInterface.super.spliterator(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
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
    
    @Override
    public T getEntry(int index) {
        if (index < 0 || index >= numberOfNodes) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        Node <T> current = dummy.getNextNode();

        for (int i = 0; i < index; i++) {
            current = current.getNextNode();
        }

        return current.getData();
    }
    
    
}