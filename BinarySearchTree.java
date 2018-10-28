public class BinarySearchTree {
	public static  Node root;
	public BinarySearchTree(){
		this.root = null;
	}

	public boolean find(customer id){
		Node current = root;
		while(current!=null){
			if(current.person==id){
				return true;
			}else if(!(current.person.isLessThan(id))){
				current = current.left;
			}else{
				current = current.right;
			}
		}
		return false;
	}
	public boolean delete(customer id){
		Node parent = root;
		Node current = root;
		boolean isLeftChild = false;
		while(current.person!=id){
			parent = current;
			if(!(current.person.isLessThan(id))){
				isLeftChild = true;
				current = current.left;
			}else{
				isLeftChild = false;
				current = current.right;
			}
			if(current ==null){
				return false;
			}
		}
		//if i am here that means we have found the node
		//Case 1: if node to be deleted has no children
		if(current.left==null && current.right==null){
			if(current==root){
				root = null;
			}
			if(isLeftChild ==true){
				parent.left = null;
			}else{
				parent.right = null;
			}
		}
		//Case 2 : if node to be deleted has only one child
		else if(current.right==null){
			if(current==root){
				root = current.left;
			}else if(isLeftChild){
				parent.left = current.left;
			}else{
				parent.right = current.left;
			}
		}
		else if(current.left==null){
			if(current==root){
				root = current.right;
			}else if(isLeftChild){
				parent.left = current.right;
			}else{
				parent.right = current.right;
			}
		}else if(current.left!=null && current.right!=null){

			//now we have found the minimum element in the right sub tree
			Node successor	 = getSuccessor(current);
			if(current==root){
				root = successor;
			}else if(isLeftChild){
				parent.left = successor;
			}else{
				parent.right = successor;
			}
			successor.left = current.left;
		}
		return true;
	}

	public Node getSuccessor(Node deleleNode){
		Node successsor =null;
		Node successsorParent =null;
		Node current = deleleNode.right;
		while(current!=null){
			successsorParent = successsor;
			successsor = current;
			current = current.left;
		}
		//check if successor has the right child, it cannot have left child for sure
		// if it does have the right child, add it to the left of successorParent.
	//successsorParent
		if(successsor!=deleleNode.right){
			successsorParent.left = successsor.right;
			successsor.right = deleleNode.right;
		}
		return successsor;
	}
	public void insert(customer id){
		Node newNode = new Node(id);
		if(root==null){
			root = newNode;
			return;
		}
		Node current = root;
		Node parent = null;
		while(true){
			parent = current;
			if(id.isLessThan(current.person)){
				current = current.left;
				if(current==null){
					parent.left = newNode;
					return;
				}
			}else{
				current = current.right;
				if(current==null){
					parent.right = newNode;
					return;
				}
			}
		}
	}
	public void display(Node root){
		if(root!=null){
			display(root.left);
			System.out.print(" " + root.person.getcustomorid());
			display(root.right);
		}
	}
	public static void main(String arg[]){
		BinarySearchTree b = new BinarySearchTree();
		b.insert(new customer("22332"));b.insert(new customer("334421"));
		b.insert(new customer("1123421"));b.insert(new customer("432131"));b.insert(new customer("122342"));b.insert(new customer("8475733"));b.insert(new customer("75834849"));b.insert(new customer("4254695"));
		b.insert(new customer("34234"));b.insert(new customer("857342"));b.insert(new customer("9484734"));b.insert(new customer("5653543"));
		System.out.println("Original Tree: \n");
		b.display(b.root);
		System.out.println("");
		System.out.println("\nCheck whether Node with name 432131 exists : " + b.find(new customer("432131")));
		System.out.println("\nDelete Node with no children (8475733) : " + b.delete(new customer("8475733")));
		b.display(root);
		System.out.println("\n Delete Node with one child (432131) : " + b.delete(new customer("432131")));
		b.display(root);
		System.out.println("\n Delete Node with Two children (75834849) : " + b.delete(new customer("75834849")));
		b.display(root);
	}
}
