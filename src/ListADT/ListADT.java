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
            output.append(current.getData().toString()).append("\n");
            current = current.getNextNode();
        }
        return output.toString();
    }

}