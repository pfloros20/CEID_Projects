
%{
#include <stdio.h>
#include <string.h>
#include "list.h"
#include <stdlib.h>
extern FILE *yyin;								
extern FILE *yyout;
void yyerror(const char *);
int yylex();
char* attr_val(char* tag, char* attr);
int StyleIDCheck();
int checkType(char* type);
Node* list=NULL;
int rowCount=0;
int rowCeil=-1;
int colCount=0;
int colCeil=-1;
%}

/*------------------------------------------------------------------*
 *                              Tokens                              *
 *------------------------------------------------------------------*/ 
%token Workbook
%token Styles
%token Style
%token Worksheet
%token Table
%token Column
%token Row
%token Cell
%token Data
%token NUMBER
%token BOOLEAN
%token STRING
%token DATETIME
%token WorkbookEnd
%token StylesEnd
%token WorksheetEnd
%token TableEnd
%token RowEnd
%token CellEnd 
%token DataEnd
%define parse.error verbose
%locations

%union{
	char* str;
}
%%

program:wrkb						{printf("\n***File written correctly.***\n");} 
	; 
wrkb:	Workbook stls wrks WorkbookEnd
	;
stls:  |stls Styles stl StylesEnd
	;
stl:  |stl Style {char* val=attr_val(yylval.str,"ss:ID");
					if(SearchList(list,val)==0)
						Insert(&list,val);
					else{
						yyerror("ss:ID value is not unique!");
						return -1;
					}
				}
	;
wrks: Worksheet tbl WorksheetEnd|Worksheet wrks WorksheetEnd
	;
tbl:  |Table {
				if(StyleIDCheck()==-1)return -1;
				char* val=attr_val(yylval.str,"ss:ExpandedRowCount");
				if(val!=NULL){
					char* del=(char*)'"';
					rowCeil=(int)strtol(++val,&del,10);		
				}
				val=attr_val(yylval.str,"ss:ExpandedColumnCount");
				if(val!=NULL){
					char* del=(char*)'"';
					colCeil=(int)strtol(++val,&del,10);		
				}

			} col row TableEnd 
			{
				if(rowCeil!=-1&&rowCeil!=rowCount){
					yyerror("ss:ExpandedRowCount doesn't match number of row tags!");
					return -1;
				}
				if(colCeil!=-1&&colCeil!=colCount){
					yyerror("ss:ExpandedColumnCount doesn't match number of column tags!");
					return -1;
				}
				colCeil=-1;
				rowCeil=-1;
			}
	;
col:  |col Column {if(StyleIDCheck()==-1)return -1;colCount++;}
	;
row:  |row Row {if(StyleIDCheck()==-1)return -1;rowCount++;} 
		cell RowEnd
	;
cell:  |Cell {if(StyleIDCheck()==-1)return -1;} data CellEnd cell
	;
data:  |Data NUMBER {if(checkType("\"Number\"")==-1)return -1;} DataEnd
		|Data STRING {if(checkType("\"String\"")==-1)return -1;} DataEnd
		|Data BOOLEAN {if(checkType("\"Boolean\"")==-1)return -1;} DataEnd
		|Data DATETIME {if(checkType("\"DateTime\"")==-1)return -1;} DataEnd
		|Data DataEnd
	;
								    
%%								    

int main ( int argc, char **argv  ) 
  {
  ++argv; --argc;
  if ( argc > 0 )
        yyin = fopen( argv[0], "r" );
  else
        yyin = stdin;
  yyparse ();	
  printf("\n");  
  return 0;
  }

char* attr_val(char* tag, char* attr){

	char* tagcpy=(char*)malloc(strlen(tag)*sizeof(char));
	strcpy(tagcpy,tag);
	char* s=strstr(tagcpy,attr);
	if(s==NULL){
			return NULL;
	}
	int len=strlen(s);
	for(int i=0;i<len;i++)
		if(s[i]==' '||s[i]=='/')
			s[i]='>';

	char* token=strtok(s,"=");
	token=strtok(NULL,">");
	free(tagcpy);
	return token;
}
				
int StyleIDCheck(){
	char* val=attr_val(yylval.str,"ss:StyleID");
	if(val!=NULL&&SearchList(list,val)==0){
		yyerror("ss:StyleID value is not included in the ss:ID values!");
		return -1;
	}
}

int checkType(char* type){
	char* val=attr_val(yylval.str,"ss:Type");
	if(val!=NULL&&strcmp(val,type)!=0){
		yyerror("ss:Type is different from data entry!");
		return -1;
	}
	return 0;
}
