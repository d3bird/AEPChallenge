


public class AVLTree {

	
	
    Node root; 
  
    // A utility function to get the height of the tree 
   private int height(Node N) { 
        if (N == null) 
            return 0; 
  
        return N.height; 
    } 
  
    // A utility function to get maximum of two integers 
   private int max(int a, int b) { 
        return (a > b) ? a : b; 
    } 
  
    // A utility function to right rotate subtree rooted with y 
    // See the diagram given above. 
  private  Node rightRotate(Node y) { 
        Node x = y.left; 
        Node T2 = x.right; 
  
        // Perform rotation 
        x.right = y; 
        y.left = T2; 
  
        // Update heights 
        y.height = max(height(y.left), height(y.right)) + 1; 
        x.height = max(height(x.left), height(x.right)) + 1; 
  
        // Return new root 
        return x; 
    } 
  
    // A utility function to left rotate subtree rooted with x 
    // See the diagram given above. 
   private Node leftRotate(Node x) { 
        Node y = x.right; 
        Node T2 = y.left; 
  
        // Perform rotation 
        y.left = x; 
        x.right = T2; 
  
        //  Update heights 
        x.height = max(height(x.left), height(x.right)) + 1; 
        y.height = max(height(y.left), height(y.right)) + 1; 
  
        // Return new root 
        return y; 
    } 
  
    // Get Balance factor of node N 
   private int getBalance(Node N) { 
        if (N == null) 
            return 0; 
  
        return height(N.left) - height(N.right); 
    } 
  
  public Node insert(Node node, customer key) { 
  
        /* 1.  Perform the normal BST insertion */
        if (node == null) 
            return (new Node(key)); 
  
        if (key.isLessThan(node.key)){ 
            node.left = insert(node.left, key); 
        }
        else if (node.key.isLessThan(key)) {
            node.right = insert(node.right, key); 
        }
        else { // Duplicate keys not allowed 
            return node; 
        }
        /* 2. Update height of this ancestor node */
        node.height = 1 + max(height(node.left), 
                              height(node.right)); 
  
        /* 3. Get the balance factor of this ancestor 
              node to check whether this node became 
              unbalanced */
        int balance = getBalance(node); 
  
        // If this node becomes unbalanced, then there 
        // are 4 cases Left Left Case 
        if (balance > 1 && key.isLessThan( node.left.key) ) {
            return rightRotate(node); 
        }
        // Right Right Case 
        if (balance < -1 && node.right.key.isLessThan(key)) {
            return leftRotate(node); 
        }
        // Left Right Case 
        if (balance > 1 && node.left.key.isLessThan(key)) { 
            node.left = leftRotate(node.left); 
            return rightRotate(node); 
        } 
  
        // Right Left Case 
        if (balance < -1 && key.isLessThan(node.right.key)) { 
            node.right = rightRotate(node.right); 
            return leftRotate(node); 
        } 
  
        /* return the (unchanged) node pointer */
        return node; 
    } 
  
    // A utility function to print preorder traversal 
    // of the tree. 
    // The function also prints height of every node 
  public void preOrder(Node node) { 
        if (node != null) { 
            System.out.print(node.key.getcustomorid() + " "); 
            preOrder(node.left); 
            preOrder(node.right); 
        } 
    } 
  
 int addMonthlyData(Object node, String targ,MonthlyData md) {
	 if(((Node) node).getKey().getcustomorid().equals(targ)) {
		 return 1;
	 }else {
		 if(((Node) node).Right()!=null && ((Node) node).Left() !=null) {
			return addMonthlyData(((Node) node).Right(),targ,md) + addMonthlyData(((Node) node).Left(),targ,md);

		 }else if(((Node) node).Right()!=null){
			 return addMonthlyData(((Node) node).Right(),targ,md);
		 }else if(((Node) node).Left() !=null) {
			 return addMonthlyData(((Node) node).Left(),targ,md);
		 }else {
			 return 0;
		 }
		 
	 }
	 
	
 }
  
}
