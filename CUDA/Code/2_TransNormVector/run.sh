echo "Compiling CUDA code..."
nvcc TransNormVector.cu -o TransNormVector-CUDA 
# echo "Compiling test code..."
# gcc TransNormVector-Result-Check.c -o TransNormVector-Result-Check
echo "Running CUDA code..."
./TransNormVector-CUDA
# echo "Running test code..."
# ./TransNormVector-Result-Check |wc -l