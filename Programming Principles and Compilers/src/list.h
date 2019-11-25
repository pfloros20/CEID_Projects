typedef struct node{
	char* value;
	struct node* next;
}Node;

Node* CreateNode(char* value);
void Insert(Node** head,char*  newNode);
int SearchList(Node* head,char* value);
void DeleteList(Node** head);
void Print(Node* head);
