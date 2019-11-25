#include "list.h"
#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
Node* CreateNode(char* value){
	Node* node=(Node*)malloc(sizeof(Node));
	node->value=(char*)malloc(sizeof(value));
	strcpy(node->value, value);
	node->next=NULL;
	return node;
}
void Insert(Node** head,char* newNode){
	if(*head==NULL){
		*head=CreateNode(newNode);
		return;
	}
	Node* iterator=*head;
	while(iterator->next!=NULL)
		iterator=iterator->next;
	iterator->next=CreateNode(newNode);
}
//0 not found, 1 found
int SearchList(Node* head,char* value){
	if(head==NULL)
			return 0;
	Node* iterator=head;
	while(iterator->next!=NULL){
		if(strcmp(iterator->value,value)==0)
			return 1;
		iterator=iterator->next;
	}
	if(strcmp(iterator->value,value)==0)
		return 1;
	return 0;
}
void DeleteList(Node** head){
		if(*head==NULL)
			return;
		if((*head)->next!=NULL)
			DeleteList(&((*head)->next));
		free((*head)->value);
		free(*head);
		*head=NULL;
}
void Print(Node* head){
	if(head==NULL)
		return;
	Node* iterator=head;
	while(iterator->next!=NULL){
		printf("%s\n",iterator->value);
		iterator=iterator->next;
	}
	printf("%s\n",iterator->value);
}
// int main(){
// 	Node* list;
// 	list=CreateNode("Start");
// 	printf("Created head.\n");
// 	Insert(&list,"\"s123\"");
// 	Insert(&list,"\"x123\"");
// 	Print(list);
// 	if(SearchList(list,"\"s123\""))
// 		printf("Oops!\n");
// 	if(SearchList(list,"2"))
// 		printf("Gut\n");
// 	if(SearchList(list,"Start"))
// 		printf("Gut\n");
// 	DeleteList(&list);
// 	Print(list);
// }
