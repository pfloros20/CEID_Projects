#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <assert.h>
#include <unistd.h>
#include <sys/time.h>

/* Problem size. */
#define NX 5000
#define NY 5000
#ifndef M_PI
#define M_PI 3.14159
#endif

const unsigned int THREADS_PER_BLOCK = 32;

void init_array(double *x, double *A)
{
	int i, j;

	for (i = 0; i < NX; i++) {
		for (j = 0; j < NY; j++) {
			A[i*NY + j] = ((double) i*(j)) / NX;
		}
	}
	for (i = 0; i < NY; i++) {
		x[i] = i * M_PI;
	}
}

void trans_norm_vector(double* A, double* x, double* y, double* tmp)
{
	int i,j;
	
	for (i= 0; i < NY; i++) {
    	y[i] = 0;
	}
  
	for (i = 0; i < NX; i++) {
      		tmp[i] = 0;

	      	for (j = 0; j < NY; j++) {
			tmp[i] = tmp[i] + A[i*NY + j] * x[j];
		}
		
	      	for (j = 0; j < NY; j++) {
			y[j] = y[j] + A[i*NY + j] * tmp[i];
		}
	}
}

__global__ void trans_norm_vector_kernel(double* A, double* x, double* y, int transpose)
{
  	if(transpose == 0){
		int i = threadIdx.y + blockDim.y * blockIdx.y;
		if(i<NX){
		    y[i] = 0;
		    double tmp = 0;
			for (int j = 0; j < NY; j++)
				tmp = tmp + A[i*NY + j] * x[j];
			y[i] = tmp;
		}
  	}else{
		int j = threadIdx.y + blockDim.y * blockIdx.y;
		if(j<NY){
		   	y[j] = 0;
		   	double tmp = 0;
			for (int i = 0; i < NX; i++)
				tmp = tmp + A[j + i*NY] * x[i];
			y[j] = tmp;
		}
	}
}

__global__ void trans_norm_vector_shared(double* A, double* x, double* y, int transpose)
{
  	if(transpose == 0){
		int i = threadIdx.y + blockDim.y * blockIdx.y;
	    y[i] = 0;
		__shared__ double xs[THREADS_PER_BLOCK];
		double tmp = 0;
		for(int step=0;step<ceil((double)NY/THREADS_PER_BLOCK);step++){
			if(threadIdx.y+step*THREADS_PER_BLOCK>=NY)
				xs[threadIdx.y]=0;
			else
				xs[threadIdx.y]=x[threadIdx.y+step*THREADS_PER_BLOCK];
			__syncthreads();
			for (int j = 0; j < THREADS_PER_BLOCK; j++)
				tmp = tmp + A[i*NY + j + step*THREADS_PER_BLOCK] * xs[j];
			__syncthreads();
		}
		y[i] = tmp;
  	}else{
		int j = threadIdx.y + blockDim.y * blockIdx.y;
		if(j<NY){
		   	y[j] = 0;
		   	double tmp = 0;
			for (int i = 0; i < NX; i++)
				tmp = tmp + A[j + i*NY] * x[i];
			y[j] = tmp;
		}
	}
}

int main(int argc, char *argv[])
{
	//Serial program variables
	double		*y;
	//Host and Device variables for CUDA
	double		*A_h;
	double		*x_h;
	double		*y_h;
	double		*tmp_h;

	double		*A_d;
	double		*x_d;
	double		*y_d;
	double		*tmp_d;
	struct timeval	start, end;

	//Variables for shared memory implementation
	double		*A_s;
	double		*x_s;
	double		*y_s;
	double		*tmp_s;

	A_h = (double*)malloc(NX*NY*sizeof(double));
	x_h = (double*)malloc(NY*sizeof(double));
	y_h = (double*)malloc(NY*sizeof(double));
	tmp_h = (double*)malloc(NX*sizeof(double));
	y = (double*)malloc(NY*sizeof(double));

	init_array(x_h, A_h);


	cudaMalloc((void**)&A_d, NX*NY*sizeof(double));
	cudaMalloc((void**)&x_d, NY*sizeof(double));
	cudaMalloc((void**)&y_d, NY*sizeof(double));
	cudaMalloc((void**)&tmp_d, NX*sizeof(double));
	cudaMalloc((void**)&A_s, NX*NY*sizeof(double));
	cudaMalloc((void**)&x_s, NY*sizeof(double));
	cudaMalloc((void**)&y_s, NY*sizeof(double));
	cudaMalloc((void**)&tmp_s, NX*sizeof(double));

	fprintf(stdout, "NX: %d NY: %d\n", NX, NY);

	const unsigned int numBlocksInCol= ceil((double)NX/THREADS_PER_BLOCK);
	const unsigned int numBlocksInRow= ceil((double)NY/THREADS_PER_BLOCK);
	dim3 gridDim(1, numBlocksInCol, 1), blockDim(1, THREADS_PER_BLOCK, 1);
	dim3 gridDimT(1, numBlocksInRow, 1);

	//Start of CUDA code
	gettimeofday(&start, NULL);
	cudaMemcpy(A_d, A_h, NX*NY*sizeof(double), cudaMemcpyHostToDevice);
	cudaMemcpy(x_d, x_h, NY*sizeof(double), cudaMemcpyHostToDevice);

	trans_norm_vector_kernel <<< gridDim, blockDim >>>(A_d, x_d, tmp_d, 0);
	trans_norm_vector_kernel <<< gridDimT, blockDim >>>(A_d, tmp_d, y_d, 1);

	cudaMemcpy(y_h, y_d , sizeof(double)*NY, cudaMemcpyDeviceToHost);
	cudaDeviceSynchronize();
	gettimeofday(&end, NULL);
	fprintf(stdout, "GPU Runtime :%0.6lfs\n", ((end.tv_sec - start.tv_sec) * 1000000.0 + (end.tv_usec - start.tv_usec)) / 1000000.0);



	//Start of CUDA shared code
	gettimeofday(&start, NULL);
	cudaMemcpy(A_s, A_h, NX*NY*sizeof(double), cudaMemcpyHostToDevice);
	cudaMemcpy(x_s, x_h, NY*sizeof(double), cudaMemcpyHostToDevice);

	trans_norm_vector_shared <<< gridDim, blockDim >>>(A_s, x_s, tmp_s, 0);
	trans_norm_vector_shared <<< gridDimT, blockDim >>>(A_s, tmp_s, y_s, 1);

	cudaMemcpy(y_h, y_s , sizeof(double)*NY, cudaMemcpyDeviceToHost);
	cudaDeviceSynchronize();
	gettimeofday(&end, NULL);
	fprintf(stdout, "GPU Shared Runtime :%0.6lfs\n", ((end.tv_sec - start.tv_sec) * 1000000.0 + (end.tv_usec - start.tv_usec)) / 1000000.0);


	//Start of Serial code
	gettimeofday(&start, NULL);
	trans_norm_vector(A_h, x_h, y, tmp_h);
	gettimeofday(&end, NULL);
	fprintf(stdout, "CPU Runtime :%0.6lfs\n", ((end.tv_sec - start.tv_sec) * 1000000.0 + (end.tv_usec - start.tv_usec)) / 1000000.0);

	//Error checking CUDA results according to Serial results
	int errors = 0;
	for (int i = 0; i < NY; ++i) {
		double error=fabs((y[i] -y_h[i])/y[i]);
		if(error>pow(10,-14)){
			printf("Error %.20f in (%d)\n",error,i);
			errors++;
		}
	}
	printf("Vector\n\tTotal Results: %d\n\tError count: %d\n",NX*NY,errors);


	free(A_h);
	free(x_h);
	free(y_h);
	free(tmp_h);



	cudaFree(A_s); 
	cudaFree(x_s); 
	cudaFree(y_s); 
	cudaFree(tmp_s); 

  	return 0;
}

