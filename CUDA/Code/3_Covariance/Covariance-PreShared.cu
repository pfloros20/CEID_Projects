#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <assert.h>
#include <unistd.h>
#include <sys/time.h>

/* Problem size */
#define M 1024
#define N 1024

#define FLOAT_N 3214212.01


const unsigned int THREADS_PER_BLOCK = 32;

void init_arrays(double* data)
{
	int i, j;

	for (i = 0; i < M; i++) {
		for (j = 0; j < N; j++) {
			data[i*N + j] = ((double) i*j) / M;
		}
	}
}

void covariance(double* data, double* symmat, double* mean)
{
	int	i, j, j1,j2;

  	/* Determine mean of column vectors of input data matrix */
	for (j = 0; j < M; j++) {
		mean[j] = 0.0;
		for (i = 0; i < N; i++) {
        		mean[j] += data[i*M + j];
		}
		mean[j] /= FLOAT_N;
	}

  	/* Center the column vectors. */
	for (i = 0; i < N; i++) {
		for (j = 0; j < M; j++) {
			data[i*M + j] -= mean[j];
		}
	}

  	/* Calculate the m * m covariance matrix. */
	for (j1 = 0; j1 < M; j1++) {
		for (j2 = j1; j2 < M; j2++) {
	       	symmat[j1*M + j2] = 0.0;
			for (i = 0; i < N; i++) {
				symmat[j1*M + j2] += data[i*M + j1] * data[i*M + j2];
			}
        	symmat[j2*M + j1] = symmat[j1*M + j2];
      	}
	}
}

__global__ void covariance_kernel(double* data, double* symmat, double* mean)
{
	int j = threadIdx.x + blockDim.x * blockIdx.x;
	int i,j1;
  	/* Determine mean of column vectors of input data matrix */
	mean[j] = 0.0;
	double temp = 0;
	for (i = 0; i < N; i++) {
    	temp += data[i*M + j];
	}
	double loc_mean =temp/FLOAT_N;
	mean[j] = loc_mean;
  	/* Center the column vectors. */
	for (i = 0; i < N; i++) {
		data[i*M + j] -= loc_mean;
	}
  	/* Calculate the m * m covariance matrix. */
	for (j1 = j; j1 < M; j1++) {
	    symmat[j*M + j1] = 0.0;
  		temp = 0;
		for (i = 0; i < N; i++) {
			 temp+= data[i*M + j] * data[i*M + j1];
		}
		symmat[j*M + j1] = temp;
        symmat[j1*M + j] = temp;
    }
}

int main(int argc, char *argv[])
{
	//Serial program variables
	double		*data;
	double		*symmat;
	double		*mean;
	struct timeval	cpu_start, cpu_end;
	//Host and Device variables for CUDA
	double		*data_h;
	double		*symmat_h;
	double		*mean_h;
	double		*data_d;
	double		*symmat_d;
	double		*mean_d;
	struct timeval	gpu_start, gpu_end;

	//Main Memory allocation
	data = (double*)malloc(M*N*sizeof(double));
	symmat = (double*)malloc(M*M*sizeof(double));
	mean = (double*)malloc(M*sizeof(double));

	data_h = (double*)malloc(M*N*sizeof(double));
	symmat_h = (double*)malloc(M*M*sizeof(double));
	mean_h = (double*)malloc(M*sizeof(double));

	//Initialize data
	init_arrays(data);
	init_arrays(data_h);

	//GPU Gloval Memory allocation
	cudaMalloc((void**)&data_d, M*N*sizeof(double));
	cudaMalloc((void**)&symmat_d, M*M*sizeof(double));
	cudaMalloc((void**)&mean_d, M*sizeof(double));

	//Start of CUDA code
	gettimeofday(&gpu_start, NULL);
	cudaMemcpy(data_d, data_h, M*N*sizeof(double), cudaMemcpyHostToDevice);

	//const unsigned int numBlocksInCol= ceil((double)M/THREADS_PER_BLOCK);
	const unsigned int numBlocksInRow= ceil((double)N/THREADS_PER_BLOCK);
	dim3 gridDim(numBlocksInRow, 1, 1), blockDim(THREADS_PER_BLOCK, 1, 1);

	//Kernel Call lul
	covariance_kernel <<< gridDim, blockDim >>>(data_d, symmat_d, mean_d);

	cudaMemcpy(symmat_h, symmat_d , M*M*sizeof(double), cudaMemcpyDeviceToHost);
	gettimeofday(&gpu_end, NULL);
	fprintf(stdout, "GPU Runtime: %0.6lfs\n", ((gpu_end.tv_sec - gpu_start.tv_sec) * 1000000.0 + (gpu_end.tv_usec - gpu_start.tv_usec)) / 1000000.0);

	//Start of Serial code
	gettimeofday(&cpu_start, NULL);
	covariance(data, symmat, mean);
	gettimeofday(&cpu_end, NULL);
	fprintf(stdout, "CPU Runtime: %0.6lfs\n", ((cpu_end.tv_sec - cpu_start.tv_sec) * 1000000.0 + (cpu_end.tv_usec - cpu_start.tv_usec)) / 1000000.0);

	//Error checking CUDA results according to Serial results
	int errors = 0;
	for (int i = 0; i < M; ++i) {
		for (int j = 0; j < M; ++j) {
			double error=fabs((symmat[i*M + j] -symmat_h[i*M+j])/symmat_h[i*M+j]);
			if(error>0.000000000000001){
				//printf("Error %.20f in (%d,%d)\n",error,i,j);
				//printf("Value %.20f in (%d,%d)\n",symmat_h[i*M+j],i,j);
				errors++;
			}
		}
	}
	printf("Symmat\n\tTotal Results: %d\n\tError count: %d\n",M*M,errors);



	// cudaMemcpy(data_h, data_d , M*N*sizeof(double), cudaMemcpyDeviceToHost);
	// errors = 0;
	// for (int i = 0; i < M; ++i) {
	// 	for (int j = 0; j < N; ++j) {
	// 		double error=fabs(data[i*N + j] -data_h[i*N+j]);
	// 		if(error>0.000000000000001){
	// 			//printf("Error %.20f in (%d,%d)\n",error,i,j);
	// 			errors++;
	// 		}
	// 	}
	// }
	// printf("Data\n\tTotal Results: %d\n\tError count: %d\n",M*M,errors);


	//Free allocated memory
	free(data);
	free(symmat);
	free(mean);
	free(data_h);
	free(symmat_h);
	free(mean_h);

	cudaFree(data_d); 
	cudaFree(symmat_d); 
	cudaFree(mean_d);

  	return 0;
}

