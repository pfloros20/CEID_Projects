#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>

/* Problem size */
#define NI 5000
#define NJ 5000


const unsigned int THREADS_PER_BLOCK_Y = 32; 
const unsigned int THREADS_PER_BLOCK_X = 32; 

__global__ void Convolution(double* A, double* B, double* C)
{
	int j = threadIdx.x + blockDim.x * blockIdx.x+1;
	int i = threadIdx.y + blockDim.y * blockIdx.y+1;

	if(i<NI-1 && j<NJ-1)
		B[i*NJ + j] = C[0] * A[(i - 1)*NJ + (j - 1)]  +  C[3] * A[(i + 0)*NJ + (j - 1)]  +  C[6] * A[(i + 1)*NJ + (j - 1)]
				    + C[1] * A[(i - 1)*NJ + (j + 0)]  +  C[4] * A[(i + 0)*NJ + (j + 0)]  +  C[7] * A[(i + 1)*NJ + (j + 0)] 
				    + C[2] * A[(i - 1)*NJ + (j + 1)]  +  C[5] * A[(i + 0)*NJ + (j + 1)]  +  C[8] * A[(i + 1)*NJ + (j + 1)];

}

void init(double* A_h)
{
	int i, j;

	for (i = 0; i < NI; ++i) {
		for (j = 0; j < NJ; ++j) {
			A_h[i*NJ + j] = (double)rand()/RAND_MAX;
        	}
    	}
}

int main(int argc, char *argv[])
{
	double		*A_h;
	double		*B_h;
	struct timeval	gpu_start, gpu_end;

	
	A_h = (double*)malloc(NI*NJ*sizeof(double));
	B_h = (double*)malloc(NI*NJ*sizeof(double));
	double C_h[]= {+0.2, +0.5, -0.8, -0.3, +0.6, -0.9, +0.4, +0.7, +0.10};
	//initialize the array
	init(A_h);
	
	double		*A_d;
	double		*B_d;
	double		*C_d;

	cudaMalloc((void**)&A_d, NI*NJ*sizeof(double));
	cudaMalloc((void**)&B_d, NI*NJ*sizeof(double));
	cudaMalloc((void**)&C_d, 9*sizeof(double));
	gettimeofday(&gpu_start, NULL);
	cudaMemcpy(A_d, A_h, NI*NJ*sizeof(double), cudaMemcpyHostToDevice);
	cudaMemcpy(C_d, C_h, 9*sizeof(double), cudaMemcpyHostToDevice);
	
	//const unsigned int numBlocks = (NI-2)*(NJ-2)/THREADS_PER_BLOCK + 1;
	const unsigned int numBlocksInRow = ceil((double)(NJ-2)/THREADS_PER_BLOCK_X);
	const unsigned int numBlocksInCol = ceil((double)(NI-2)/THREADS_PER_BLOCK_Y);
	dim3 gridDim(numBlocksInRow, numBlocksInCol, 1), blockDim(THREADS_PER_BLOCK_X, THREADS_PER_BLOCK_Y, 1);

	Convolution <<< gridDim, blockDim >>>(A_d, B_d, C_d);
	cudaDeviceSynchronize();
	cudaMemcpy(B_h, B_d , sizeof(double)*NI*NJ, cudaMemcpyDeviceToHost); 
	gettimeofday(&gpu_end, NULL);
	fprintf(stdout, "GPU Runtime: %0.6lfs\n", ((gpu_end.tv_sec - gpu_start.tv_sec) * 1000000.0 + (gpu_end.tv_usec - gpu_start.tv_usec)) / 1000000.0);

	//Write results to file
	if(argc == 2)
		if(strcmp(argv[1],"-w") == 0){
			FILE *fp = fopen ("datasetA.txt","w");
		    if (fp == NULL)
		        printf ("File not created.\n");
			
			fwrite(A_h,sizeof(double),NI*NJ,fp) ;
			fclose(fp);
			FILE *fp1 = fopen ("datasetB.txt","w");
			fwrite(B_h,sizeof(double),NI*NJ,fp1) ;
			fclose(fp1);
		}
		        	

	free(A_h);
	free(B_h);

	cudaFree(A_d); 
	cudaFree(B_d); 
	
	return 0;
}

