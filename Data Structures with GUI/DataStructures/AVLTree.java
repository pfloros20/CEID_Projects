package DataStructures;
import java.lang.Math;
public class AVLTree <T>{
	private AVLNode root;
	public AVLTree(){
		root=null;}	//Initialize root with null when constructor is called.
	
	public void insert(T t,int key){
		AVLNode newNode=new AVLNode(t,key);	//Create new Node with content of t and key.
		if(root==null){
				root=newNode;	//Tree is empty use new node as root.
				return;
			}
		AVLNode copy;
		AVLNode accessNode=access(key,root,1);	//Search tree for a node with the same key starting from root.
		AVLNode newParent=null;
		if(accessNode==null)	//If access returns null node already exists.
			return;
		if(accessNode.key==key)	//If access returns a node with the same key return.
			return;
		if(newNode.key<accessNode.key){	//If the key to be inserted is less than the one returned the min is new and max is access.
			copy=new AVLNode(t,key);	//We replace the max node with the sub tree parent=min left son=min rightson =max.
			copy.parent=newNode;	//We copy the min value to copy and put copy's parent to min.
			newNode.leftSon=copy;	//Min leftson = copy
			newNode.rightSon=accessNode;// Min rightson=max
			boolean i=false;	//boolean will be used to check if the returned node is root, this is the second insert on the tree instance.
			if(accessNode.equals(root))i=true;
			newNode.parent=accessNode.parent;	//put the max node parent to new node parent.
			accessNode.parent=newNode;	//put the max parent =min.
			if(newNode.parent!=null)	//if access node wasnt root.
				if(newNode.parent.rightSon.equals(accessNode))	//we must connect the sub tree to the access node's old place.
					newNode.parent.rightSon=newNode;	//if access was a right son put the subtree on the accesses old parent in the right.
				else
					newNode.parent.leftSon=newNode;	//same but with left.
			if(i)root=newNode;	//if access was root put the new node as root.
			newParent=newNode;	//New parent is the parent of the sub tree.
			newNode.value=null;	//Empty T object.
		}else{
			copy=new AVLNode(accessNode.value,accessNode.key);	//If new key is max and access node key is min.
			accessNode.rightSon=newNode;	//min rightson=max.
			accessNode.leftSon=copy;	//min leftson=min.
			newNode.parent=accessNode;	//max parent=min.
			copy.parent=accessNode;	//min parent =min (copy is a copy of min node).
			copy.leftSon=null;	//Copy is leaf
			copy.rightSon=null;
			newParent=accessNode;	//Same as above.
			accessNode.value=null;
		}
		newParent.setHeight();	//Update height recursively starting with the parent of the subtree.
		AVLNode balance=newParent.setBalance(newParent);	//Update balance and return the node with balance either 2 or -2.
		if(balance!=null&&balance.balance==2)	//If balance is null the tree is already balanced.
			if(balance.rightSon.balance==1)
				singleRotation(balance.rightSon,0);
			else
				doubleRotation(balance.rightSon.leftSon,0);
		else if(balance!=null&&balance.balance==-2)
			if(balance.leftSon.balance==-1)
				singleRotation(balance.leftSon,1);
			else
				doubleRotation(balance.leftSon.rightSon,1);
		}

	public void delete(int key){	//Essentially destroys the copy created by insert and the node you want to delete.
		AVLNode accessNode=access(key,root,1);	//Find if node with equal key exists.
		if(accessNode==null)	//If it doesnt exist return.
			return;
		AVLNode grandParent=accessNode.parent.parent;
		AVLNode accessParent=accessNode.parent;
		AVLNode accessBrother;
		if(accessParent.rightSon.equals(accessNode))	//If access is a right son, his brother is his parent left son.
			accessBrother=accessParent.leftSon;
		else
			accessBrother=accessParent.rightSon;	//If access is a left son, his brother is his parent right son.
		if(grandParent.rightSon.equals(accessParent))	//Find which son is the access parent and put access brother in its place.
			grandParent.rightSon=accessBrother;
		else
			grandParent.leftSon=accessBrother;
		accessBrother.parent=grandParent;	//Put brothers parent grand parent.
		accessParent=null;	//Destroy access parent.
		accessNode=null;	//Destroy access.	
		grandParent.setHeight();	//Update height.
		AVLNode balance=grandParent.setBalance(grandParent);
		if(balance!=null&&balance.balance==2)	//Rebalance
			if(balance.rightSon.balance==1)
				singleRotation(balance.rightSon,0);
			else
				doubleRotation(balance.rightSon.leftSon,0);
		else if(balance!=null&&balance.balance==-2)
			if(balance.leftSon.balance==-1)
				singleRotation(balance.leftSon,1);
			else
				doubleRotation(balance.leftSon.rightSon,1);
	}
		
	private void singleRotation(AVLNode node,int mode){
		AVLNode temp=null;
		if(mode==0)	//left rotation
		{	boolean i=false;			
			if(node.parent!=null)
			if(node.parent.equals(root))i=true;
			temp=node.parent;	//temp=x
			if(node.parent!=null)node.parent=node.parent.parent;	//y.parent=x.parent
			if(!i)
				if(node.parent.rightSon.equals(temp))
					node.parent.rightSon=node;
				else
					node.parent.leftSon=node;
			node.leftSon.parent=temp;	//y.leftson.parent=x
			if(node.leftSon!=null&&temp.leftSon!=null)temp.rightSon=node.leftSon;	//x.rightson=y.leftson
			node.leftSon=temp;	//y.leftson=x
			temp.parent=node;	//x.parent=y
			temp.setHeight();	//Update height.
			temp.setBalance(temp);	//Update balance.
			if(i==true)root=node;
		}else if(mode==1)	//right rotation
		{	boolean i=false;			
			if(node.parent!=null)
			if(node.parent.equals(root))i=true;
			temp=node.parent;	//temp=x
			node.parent=node.parent.parent;	//y.parent=x.parent
			if(!i)
			if(node.parent.rightSon.equals(temp))
				node.parent.rightSon=node;
			else
				node.parent.leftSon=node;	//y.rightson.parent=x
			temp.leftSon=node.rightSon;	//x.leftson=y.rightson
			node.rightSon.parent=temp;
			node.rightSon=temp;
			temp.parent=node;	//x.parent=y
			temp.setHeight();	//Update height.
			temp.setBalance(temp);	//Update balance.
			if(i==true)root=node;
		}
	}
	
	private void doubleRotation(AVLNode node,int mode){
		if (mode== 0) { //Double Left Rotation
            singleRotation( node, 1 );    
            singleRotation( node, 0 );
        }else if (mode == 1){ //Double Right Rotation        
            singleRotation( node, 0 );
            singleRotation( node, 1 );
        }
		node.setBalance(node);
	}

	
	public T access(int key){
		AVLNode node=access(key,root,0);
		if(node!=null)
			return node.value;
		else
			return null;
	}
	
	private AVLNode access(int key,AVLNode node,int i){	//access searches for the node with the same key and has 2 modes according to i.
		if(node==null)return null;	//mode 0 if found is returned, mode 1 for insert if found return null.I node is null return null.
		if(key==node.key){	//if node key is equal to key and node is a leaf return if note continue searching.
			if(node.key==key&&node.isLeaf())
				return node;
			if(node.key==key&&!node.isLeaf()){
				AVLNode a=access(key,node.leftSon,i);	//Search left subtree.
				AVLNode b=access(key,node.rightSon,i);	//Search right subtree.
				if(a!=null)	//If found in left sub tree return.
					return a;				
				else if(b!=null)	//If found in right sub tree return.
					return b;
				else	//Not found.
					return null;
			}
		}
		if(key>node.key&&node.rightSon!=null){	//If key >than current node search on its right.
			node=access(key,node.rightSon,i);
			if(node!=null&&node.key==key)return node;
		}else if(key<node.key&&node.leftSon!=null){	//If key <than current node search on its right.
			node=access(key,node.leftSon,i);
			if(node!=null&&node.key==key)return node;
		}
		if(i==0)
			return null;
		else 
			return node;
	}
	public void printBinaryTree(){
		printBinaryTree(root,1);}
	
	void printBinaryTree(AVLNode root, int level){
	    if(root==null)
	         return;
	    printBinaryTree(root.rightSon, level+1);
	    if(level!=0){
	        for(int i=0;i<level;i++)
	            System.out.print("|\t");
	            System.out.println("|-------"+root.key);
	    }
	    else
	        System.out.println(root.key);
	    printBinaryTree(root.leftSon, level+1);
	} 
	 
		
	private class AVLNode {
		
		AVLNode parent;
		AVLNode leftSon;
		AVLNode rightSon;
		T value;
		int key;
		int height=0;
		int balance=0;
		
		public AVLNode(T value,int key)	{
			this.value=value;
			this.key=key;
		}
		AVLNode setBalance(AVLNode node){	//Recursively update balance on parents and return node with balance =2 or =-2.
			if(rightSon!=null&&leftSon!=null)balance=rightSon.height-leftSon.height;
			if(balance==2||balance==-2)
				return node;
			if(parent!=null)
				node=parent.setBalance(node.parent);
			return node;
		}
		
		void setHeight(){//Recursively update height on parents.
			if(rightSon!=null&&leftSon!=null)height=Math.max(rightSon.height,leftSon.height)+1;
			if(parent!=null)
				parent.setHeight();	}		
		
		boolean isLeaf(){	//Returns true if the node is leaf (has no sons).
			return leftSon==null&&rightSon==null;}
	}
}
