/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ListADT;

/**
 *
 * @author zhenyu
 * Linked list is consist of nodes, and chain it together. For that we need node class to help us define the nodes.
 * referenced from https://www.codecademy.com/learn/getting-started-with-data-structures-java/modules/nodes-java/cheatsheet
 */

// Accepts generic data type (String, Integer, etc.) instead of string only from the reference
// For example, Node<String>, Node<Medicine>
public class Node<T> { 
    
    private T data;
    private Node<T> next; //After node, there's another Node
    
    // instantize the node with data
    public Node(T data) {
        this.data = data;
        this.next = null;
    }
    
    // takes the Node object to bind to another node
    public void setNextNode(Node<T> node) {
        this.next = node;
    }
    
    // get Node's next node.
    public Node<T> getNextNode() {
        return this.next;
    }
    
    //get Node's data
    public T getData() {
        return data;
    }
    
    
}