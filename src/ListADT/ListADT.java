/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ListADT;

/**
 *
 * @author zhenyu
 * @author katie
 * @author mahir
 * @author jonathan
 */



public class ListADT<T> implements ListADTInterface<T>{
    
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    public boolean isEmpty() { //mahir
        
        // the node is empty if and only if the dummy ← dummy
        return dummy.getNextNode() == dummy;
    }

    @Override
    public void clear() { //mahir
        dummy.setNextNode(dummy);
    }

    @Override
    public void display() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String listWithSort(String exp){ //mahir
        // check empty or not
        if(isEmpty()){
            return "";
        }
        // sort it first
        // we define 2 classes for both string and numeric
        
        
        // list it out
        
        return exp;
    }
    
    //circular singly linkedlist with dummy node
    private String AlphaSort(String exp){
    
         return """
                """;
    }
    
    private int NumSort(int exp){
    
         return 0;
    }
}