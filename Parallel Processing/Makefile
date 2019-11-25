CC=gcc
CFLAGS=-Wall -Wextra -lcblas -fopenmp
group=lif1d lif1d_1a lif1d_1b
parallel=lif1d_2a_it_O0 lif1d_2a_it_O3 lif1d_2a_i_O0 lif1d_2a_i_O3  lif1d_2a_j_O0 lif1d_2a_j_O3
blas=lif1d_1c_O0 lif1d_1c_O3 lif1d_2b_O0 lif1d_2b_O3
io=lif1d_2b_all lif1d_3 converter
binaries=${group} ${parallel} ${blas} ${io}
all: $(binaries)

#group
lif1d:
	$(CC) -o $@ $(CFLAGS) $@.c
lif1d_1a:
	$(CC) -o $@ $(CFLAGS) $@.c
lif1d_1b:
	$(CC) -o $@ $(CFLAGS) $@.c

#parallel
lif1d_2a_it_O0:
	$(CC) -o $@ $(CFLAGS) -O0 lif1d_2a_it.c
lif1d_2a_it_O3:
	$(CC) -o $@ $(CFLAGS) -O3 lif1d_2a_it.c
lif1d_2a_i_O0:
	$(CC) -o $@ $(CFLAGS) -O0 lif1d_2a_i.c
lif1d_2a_i_O3:
	$(CC) -o $@ $(CFLAGS) -O3 lif1d_2a_i.c
lif1d_2a_j_O0:
	$(CC) -o $@ $(CFLAGS) -O0 lif1d_2a_j.c
lif1d_2a_j_O3:
	$(CC) -o $@ $(CFLAGS) -O3 lif1d_2a_j.c

#blas
lif1d_1c_O0:
	$(CC) -o $@ $(CFLAGS) -O0 lif1d_1c.c
lif1d_1c_O3:
	$(CC) -o $@ $(CFLAGS) -O3 lif1d_1c.c
lif1d_2b_O0:
	$(CC) -o $@ $(CFLAGS) -O0 lif1d_2b.c
lif1d_2b_O3:
	$(CC) -o $@ $(CFLAGS) -O3 lif1d_2b.c


#io
lif1d_3: 
	$(CC) -o $@ $(CFLAGS) -DALL_RESULTS $@.c
lif1d_2b_all: 
	$(CC) -o $@ $(CFLAGS) -DALL_RESULTS lif1d_2b.c
converter: 
	$(CC) -o $@ $(CFLAGS) $@.c
clean:
	rm -f $(binaries)