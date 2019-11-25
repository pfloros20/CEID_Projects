bison -y -d project.y -o src/y.tab.c
flex -o src/lex.yy.c project.l
gcc -c src/y.tab.c -o build/y.tab.o
gcc -c src/lex.yy.c -o build/lex.yy.o
gcc -c src/list.c -o build/list.o
gcc build/y.tab.o build/lex.yy.o build/list.o -o bin/parser
