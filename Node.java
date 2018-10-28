
public class Node {
	int  height; 
    Node left, right; 
    customer key;
    customer getKey() {return key;}
    Node Right() {return right;}
    Node Left() {return left;}
    Node(customer d) { 
    	key = d; 
        height = 1; 
    } 
}
