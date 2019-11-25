group=(lif1d lif1d_1a lif1d_1b lif1d_1c_O0 lif1d_1c_O3 lif1d_2b_O0 lif1d_2b_O3 lif1d_2b_all lif1d_3)
parallel=(lif1d_2a_it_O0 lif1d_2a_it_O3 lif1d_2a_i_O0 lif1d_2a_i_O3  lif1d_2a_j_O0 lif1d_2a_j_O3)
threads=(1 2 4)
output=measurements.txt
IFS='$'
declare -a args=(--n 1000 --r 350 --n 2000 --r 700 --n 3000 --r 1000 --n 4000 --r 1300 --n 5000 --r 1600)

make clean
make all
rm -f $output
for exec in ${group[@]}
do
	let i=0
	let limit=${#args[@]}-1
	date
	while [ $i -lt ${#args[@]} ]
	do
		echo "Executing $exec ${args[i]} ${args[i+1]} ${args[i+2]} ${args[i+3]} ..."
		results=$(./$exec ${args[i]} ${args[i+1]} ${args[i+2]} ${args[i+3]}|grep -E "Time for|Total")
		echo "Writing measurements to file $output ..."
		echo $exec ${args[i]} ${args[i+1]} ${args[i+2]} ${args[i+3]}>> $output
		echo $results >> $output
		echo >> $output
		date
		let i+=4
	done
done
for exec in ${parallel[@]}
do
	for thread in ${threads[@]}
	do
		let i=0
		let limit=${#args[@]}-1
		while [ $i -lt ${#args[@]} ]
		do
			echo "Executing $exec ${args[i]} ${args[i+1]} ${args[i+2]} ${args[i+3]} $thread Thread(s)..."
			export OMP_NUM_THREADS=$thread
			results=$(./$exec ${args[i]} ${args[i+1]} ${args[i+2]} ${args[i+3]}|grep -E "Time for|Total")
			echo "Writing measurements to file $output ..."
			echo $exec ${args[i]} ${args[i+1]} ${args[i+2]} ${args[i+3]} $thread "Thread(s)">> $output
			echo $results >> $output
			echo >> $output
			date
		let i+=4
		done
	done
done
