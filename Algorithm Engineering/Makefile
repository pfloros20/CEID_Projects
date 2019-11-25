name = program
src = $(wildcard src/*.cpp)
obj = $(src:.c=.o)

BOOSTDIR = '/usr/include'

CC = g++

CFLAGS = -std=c++0x -O3 -g


all: $(name)

$(name): $(obj)
	$(CC) $(CFLAGS) -o $@ $^ -I$(BOOSTDIR)

run:
	./$(name)

clean:
	rm -f $(name)
