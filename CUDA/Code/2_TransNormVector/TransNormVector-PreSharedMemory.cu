#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <assert.h>
#include <unistd.h>
#include <sys/time.h>

/* Problem size. */
#define NX 4096
#define NY 4096

#ifndef M_PI
#define M_PI 3.14159
#endif

const unsigned int THREADS_PER_BLOCK = 64;

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

__global__ void trans_norm_vector(double* A, double* x, double* y, int transpose)
{
  	if(transpose == 0){
		int i = threadIdx.y + blockDim.y * blockIdx.y;
	    	y[i] = 0;
	    	double tmp = 0;
		    for (int j = 0; j < NY; j++)
				tmp = tmp + A[i*NY + j] * x[j];
			y[i] = tmp;
  	}else{
		int j = threadIdx.y + blockDim.y * blockIdx.y;
	   		y[j] = 0;
	   		double tmp = 0;
			for (int i = 0; i < NX; i++)
				tmp = tmp + A[j + i*NY] * x[i];
			y[j] = tmp;
	}
}

int main(int argc, char *argv[])
{
	double		*A;
	double		*x;
	double		*y;
	double		*tmp;
	struct timeval	gpu_start, gpu_end;

	A = (double*)malloc(NX*NY*sizeof(double));
	x = (double*)malloc(NY*sizeof(double));
	y = (double*)malloc(NY*sizeof(double));
	tmp = (double*)malloc(NX*sizeof(double));

	init_array(x, A);

	double		*A_d;
	double		*x_d;
	double		*y_d;
	double		*tmp_d;

	cudaMalloc((void**)&A_d, NX*NY*sizeof(double));
	cudaMalloc((void**)&x_d, NY*sizeof(double));
	cudaMalloc((void**)&y_d, NY*sizeof(double));
	cudaMalloc((void**)&tmp_d, NX*sizeof(double));


	gettimeofday(&gpu_start, NULL);
	cudaMemcpy(A_d, A, NX*NY*sizeof(double), cudaMemcpyHostToDevice);
	cudaMemcpy(x_d, x, NY*sizeof(double), cudaMemcpyHostToDevice);

	const unsigned int numBlocksInCol= ceil((double)NX/THREADS_PER_BLOCK);
	const unsigned int numBlocksInRow= ceil((double)NY/THREADS_PER_BLOCK);
	dim3 gridDim(1, numBlocksInCol, 1), blockDim(1, THREADS_PER_BLOCK, 1);
	dim3 gridDimT(1, numBlocksInRow, 1);

	trans_norm_vector <<< gridDim, blockDim >>>(A_d, x_d, tmp_d, 0);
	trans_norm_vector <<< gridDimT, blockDim >>>(A_d, tmp_d, y_d, 1);
	cudaDeviceSynchronize();

	cudaMemcpy(y, y_d , sizeof(double)*NY, cudaMemcpyDeviceToHost);
	gettimeofday(&gpu_end, NULL);
	fprintf(stdout, "GPU Runtime :%0.6lfs\n", ((gpu_end.tv_sec - gpu_start.tv_sec) * 1000000.0 + (gpu_end.tv_usec - gpu_start.tv_usec)) / 1000000.0);

	//Write results to file
	if(argc == 2)
		if(strcmp(argv[1],"-w") == 0){
			FILE *fp = fopen ("datasetA.txt","w");
		    if (fp == NULL)
		        printf ("File not created.\n");
			
			fwrite(A,sizeof(double),NX*NY,fp) ;
			fclose(fp);
			FILE *fp1 = fopen ("datasetx.txt","w");
			fwrite(x,sizeof(double),NY,fp1) ;
			fclose(fp1);
			FILE *fp2 = fopen ("datasety.txt","w");
			fwrite(y,sizeof(double),NY,fp2) ;
			fclose(fp2);
		}

	free(A);
	free(x);
	free(y);
	free(tmp);

	cudaFree(A_d); 
	cudaFree(x_d); 
	cudaFree(y_d); 
	cudaFree(tmp_d); 

  	return 0;
}

