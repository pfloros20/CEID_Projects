echo "Compiling CUDA code..."
nvcc 2D-Convolution.cu -o 2D-Convolution-CUDA 
# echo "Compiling test code..."
# gcc 2D-Convolution-Result-Check.c -o 2D-Convolution-Result-Check
echo "Running CUDA code..."
./2D-Convolution-CUDA 
# echo "Running test code..."
# ./2D-Convolution-Result-Check |wc -l