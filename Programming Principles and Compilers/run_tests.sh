dir="test/"
test_files=$(ls $dir)

for exec in ${test_files[@]}
do
	clear
	echo "/*-------------------------------------------------------* "
	echo "		Running $exec		"
	echo " *-------------------------------------------------------*/"
	./bin/parser $dir$exec
	echo "/*-------------------------------------------------------* "
	echo "		Press any key to continue...		"
	echo " *-------------------------------------------------------*/"
	read -n1
done