
%{
#include "y.tab.h"  
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int tabs=0;
void print(char option);
%}
%option noyywrap
%option yylineno
/*------------------------------------------------------------------*
 *							Primitives								*
 *------------------------------------------------------------------*/
c	 				.
ws					[ \t]
nl					[\n]
bool				true|false
digit  				[0-9]
num					{digit}+
letter 				[a-zA-Z]
date				[0-3][0-9]\/[0-1][0-9]\/[0-2][0-9][0-9][0-9]
time				[0-2][0-9]:[0-2][0-9]
datetime			{date}{ws}{time}
alpha				({letter}*{digit}*)*
string				({alpha}?[ ]?[\.\\\!\$\n()]?)+

/*------------------------------------------------------------------*
 *							Attributes								*
 *------------------------------------------------------------------*/
Name					{ws}ss:Name=\"{alpha}\"
Protected				({ws}ss:Protected=\"{bool}\")?
ID 						{ws}ss:ID=\"{alpha}\"
ExpandedColumnCount		({ws}ss:ExpandedColumnCount=\"{num}\")?
ExpandedRowCount		({ws}ss:ExpandedRowCount=\"{num}\")?
StyleID					({ws}ss:StyleID=\"{alpha}\")?
Hidden					({ws}ss:Hidden=\"{bool}\")?
Width					({ws}ss:Width=\"{num}\")?
Height					({ws}ss:Height=\"{num}\")?
MergeAcross				({ws}ss:MergeAcross=\"{num}\")?
MergeDown				({ws}ss:MergeDown=\"{num}\")?
Type					{ws}ss:Type=\"{alpha}\"

/*------------------------------------------------------------------*
 *								Tags								*
 *------------------------------------------------------------------*/
Workbook		<ss:Workbook>
WorkbookEnd		<\/ss:Workbook>
Styles			<ss:Styles>
StylesEnd		<\/ss:Styles>
Worksheet		<ss:Worksheet{Protected}{Name}{Protected}{ws}?>
WorksheetEnd	<\/ss:Worksheet>
Style			<ss:Style{ID}{ws}?><\/ss:Style>
Table			<ss:Table({ExpandedColumnCount}{ExpandedRowCount}{StyleID}{ws}?)*>
TableEnd		<\/ss:Table>
Column			<ss:Column({Hidden}{Width}{StyleID}{ws}?)*\/>
Row				<ss:Row({Hidden}{Height}{StyleID}{ws}?)*>
RowEnd			<\/ss:Row>
Cell			<ss:Cell({MergeAcross}{MergeDown}{StyleID}{ws}?)*>
CellEnd			<\/ss:Cell>
Data			<ss:Data{Type}{ws}?>
DataEnd			<\/ss:Data>
Comment			<\!--{c}*-->


/*------------------------------------------------------------------*
 *								Rules								*
 *------------------------------------------------------------------*/
%%
{ws} 			{}
{nl}			{}
{num} 			{ print('x');return NUMBER;}
{bool}			{ print('x');return BOOLEAN;}
{datetime}		{ print('x');return DATETIME;}
{Workbook}		{ print('i');return Workbook; }
{WorkbookEnd}	{ print('d');return WorkbookEnd; }
{Styles}		{ print('i');return Styles; }
{StylesEnd}		{ print('d');return StylesEnd; }
{Worksheet}		{ print('i');return Worksheet; }
{WorksheetEnd}	{ print('d');return WorksheetEnd; }
{Style}			{ print('x');yylval.str=strdup(yytext);return Style; }
{Table}			{ print('i');yylval.str=strdup(yytext);return Table; }
{TableEnd}		{ print('d');return TableEnd; }
{Column}		{ print('x');yylval.str=strdup(yytext);return Column; }
{Row}			{ print('i');yylval.str=strdup(yytext);return Row; }
{RowEnd}		{ print('d');return RowEnd; }
{Cell}			{ print('i');yylval.str=strdup(yytext);return Cell; }
{CellEnd}		{ print('d');return CellEnd; }
{Data}			{ print('i');yylval.str=strdup(yytext);return Data; }
{DataEnd}		{ print('d');return DataEnd; }
{Comment} 		{ print('x'); }
{string}		{ print('x');return STRING; }
{c}				{ }
							    
%%
/*------------------------------------------------------------------*
 *							Functions								*
 *------------------------------------------------------------------*/
void print(char option){
	if(option=='d')
		tabs--;		//decrement
	for(int i=0;i<tabs && option!='z';i++)
		printf("\t");
	printf("%s",yytext);
	if(option=='i')
		tabs++;		//increment
	printf("\n");
}

void yyerror(const char *s) {
    fprintf(stderr, "Error in line %d:\n%s\n", yylineno, s);
}									
