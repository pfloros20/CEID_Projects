echo "Compiling CUDA code..."
nvcc Covariance.cu -o Covariance-CUDA
echo "Running CUDA code..."
./Covariance-CUDA -w