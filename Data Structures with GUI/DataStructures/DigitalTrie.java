package DataStructures;
import java.util.ArrayList;
public class DigitalTrie<T> {
	DigitalTrieNode root;
	String[] alphabet;	//Holds the characters of the alphabet given.
	public DigitalTrie(String inputalphabet){	//Creates trie initializes root to a new node iputalphabet turns to characters 
		alphabet=new String[inputalphabet.length()];	//and they make up the alphabet
		root=new DigitalTrieNode();
		alphabet=inputalphabet.split("");
	}

	
	public void insert(T t,String key){
		insert(t,key,root);
	}
	
	private void insert(T t,String key,DigitalTrieNode n){
		DigitalTrieNode node=n;	//Puts current node in dode
		DigitalTrieNode newNode=null;	
		String[] arrayKey;
		arrayKey=key.split("");	//input is split in characters and stored in array.
		for(int i=0;i<key.length();i++)	//for each character
			for(int j=0;j<alphabet.length;j++)	//for the length of the alphabet
				if(arrayKey[i].equals(alphabet[j])&&node.children[j]==null){	//if the character equals the alphabet character 
					newNode=new DigitalTrieNode();		//and current node has no children.
					node.children[j]=newNode;	//create children on the index equal to the index of the alphabet character.
					newNode.parent=node;	//Put in new node's parent node
					node=newNode;		//set current node to the new node
					if(i==key.length()-1)	// if its the last character of the word put in the endsWord true to know that a word ends.
						newNode.endsWord=true;}
				else if(arrayKey[i].equals(alphabet[j]))	//else if child exists set current node to the appropriate child.
					node=node.children[j];
		if(node.endsWord&&t!=null){	//If its the end of a word and t is not empty put object t in node's object list.
			node.leafs.add(t);
		}
			
			
		
	}
	
	public ArrayList<T> search(String key){
		DigitalTrieNode access=search(key,root);
		if(access!=null&&!access.leafs.isEmpty())
			return access.leafs;
		return null;}
	
	private DigitalTrieNode search(String key,DigitalTrieNode n){
		DigitalTrieNode node=n;	//Current node is nod.
		int counter=0;	
		String[] arrayKey;
		arrayKey=key.split("");	//Key is split into characters
		for(int i=0;i<key.length();i++)	//for each character
			for(int j=0;j<alphabet.length;j++)	//for the length of the alphabet
				if(arrayKey[i].equals(alphabet[j])&&node.children[j]!=null){	//if character is equal to the alphabet character
					node=node.children[j];	//and that child is not empty set current node to the child.
					counter++;}
		if(counter==key.length()&&node.endsWord)	//if the counter is equal to the key's length and current node is a word's end
			return node;	//Return the node.
		else
			return null;	//Not Found.
	}

	class DigitalTrieNode{
		DigitalTrieNode[] children=new DigitalTrie.DigitalTrieNode[alphabet.length];
		DigitalTrieNode parent;
		boolean endsWord=false;
		ArrayList<T> leafs=new ArrayList<>();
		
	}
}
