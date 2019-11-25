#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>

/* Problem size */
#define NI 5000
#define NJ 5000


#define THREADS_PER_BLOCK_Y 32 
#define THREADS_PER_BLOCK_X 32

void Convolution(double* A, double* B)
{
	int i, j;
	double c11, c12, c13, c21, c22, c23, c31, c32, c33;

	c11 = +0.2;  c21 = +0.5;  c31 = -0.8;
	c12 = -0.3;  c22 = +0.6;  c32 = -0.9;
	c13 = +0.4;  c23 = +0.7;  c33 = +0.10;


	for (i = 1; i < NI - 1; ++i) {
		for (j = 1; j < NJ - 1; ++j) {
			B[i*NJ + j] = c11 * A[(i - 1)*NJ + (j - 1)]  +  c12 * A[(i + 0)*NJ + (j - 1)]  +  c13 * A[(i + 1)*NJ + (j - 1)]
				    + c21 * A[(i - 1)*NJ + (j + 0)]  +  c22 * A[(i + 0)*NJ + (j + 0)]  +  c23 * A[(i + 1)*NJ + (j + 0)] 
				    + c31 * A[(i - 1)*NJ + (j + 1)]  +  c32 * A[(i + 0)*NJ + (j + 1)]  +  c33 * A[(i + 1)*NJ + (j + 1)];
		}
	}
}

__global__ void Convolution_kernel(double* A, double* B)
{
	int j = threadIdx.x + blockDim.x * blockIdx.x+1;
	int i = threadIdx.y + blockDim.y * blockIdx.y+1;
	double c11, c12, c13, c21, c22, c23, c31, c32, c33;

	c11 = +0.2;  c21 = +0.5;  c31 = -0.8;
	c12 = -0.3;  c22 = +0.6;  c32 = -0.9;
	c13 = +0.4;  c23 = +0.7;  c33 = +0.10;
	if(i<NI-1 && j<NJ-1)
		B[i*NJ + j] = c11 * A[(i - 1)*NJ + (j - 1)]  +  c12 * A[(i + 0)*NJ + (j - 1)]  +  c13 * A[(i + 1)*NJ + (j - 1)]
				    + c21 * A[(i - 1)*NJ + (j + 0)]  +  c22 * A[(i + 0)*NJ + (j + 0)]  +  c23 * A[(i + 1)*NJ + (j + 0)] 
				    + c31 * A[(i - 1)*NJ + (j + 1)]  +  c32 * A[(i + 0)*NJ + (j + 1)]  +  c33 * A[(i + 1)*NJ + (j + 1)];
}

__global__ void Convolution_Shared(double* A, double* B)
{
	int j = threadIdx.x + blockDim.x * blockIdx.x+1;
	int i = threadIdx.y + blockDim.y * blockIdx.y+1;
	int tx=threadIdx.y+1;
	int ty=threadIdx.x+1;
	const int sx=THREADS_PER_BLOCK_Y+2;
	const int sy=THREADS_PER_BLOCK_X+2;
	__shared__ double As[sx*sy];
	//if corner element of block matrix, load off block corner neighbor.
	if( tx==1 && ty==1 )
		As[0]=A[(i-1)*NJ+j-1];
	if( tx==sx-2 && ty==sy-2 )
		As[(sx-1)*sy+sy-1]=A[(i+1)*NJ+j+1];
	if( tx==1 && ty==sy-2 )
		As[sy-1]=A[(i-1)*NJ+j+1];
	if( tx==sx-2 && ty==1 )
		As[(sx-1)*sy]=A[(i+1)*NJ+j-1];
	//if border element of block matrix, load off block border neighbor.
	if(tx==1)
		As[ty]=A[(i-1)*NJ+j];
	if(ty==1)
		As[tx*sx]=A[i*NJ+j-1];
	if(tx==sx-2)
		As[(sx-1)*sy+ty]=A[(i+1)*NJ+j];
	if(ty==sy-2)
		As[tx*sy+sy-1]=A[i*NJ+j+1];
	//load cell to shared memory.
	As[tx*sy+ty]=A[i*NJ+j];

	__syncthreads();

	double c11, c12, c13, c21, c22, c23, c31, c32, c33;

	c11 = +0.2;  c21 = +0.5;  c31 = -0.8;
	c12 = -0.3;  c22 = +0.6;  c32 = -0.9;
	c13 = +0.4;  c23 = +0.7;  c33 = +0.10;
	if(i<NI-1 && j<NJ-1)
		B[i*NJ + j] = c11 * As[(tx - 1)*sy+ty - 1]  +  c12 * As[(tx + 0)*sy+ty - 1]  +  c13 * As[(tx + 1)*sy+ty - 1]
					+ c21 * As[(tx - 1)*sy+ty + 0]  +  c22 * As[(tx + 0)*sy+ty + 0]  +  c23 * As[(tx + 1)*sy+ty + 0] 
					+ c31 * As[(tx - 1)*sy+ty + 1]  +  c32 * As[(tx + 0)*sy+ty + 1]  +  c33 * As[(tx + 1)*sy+ty + 1];


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
	//Serial program variables
	double		*B;
	struct timeval	start, end;

	//Host and Device variables for CUDA
	double		*A_h;
	double		*B_h;	
	double		*A_d;
	double		*B_d;
	//Variables for shared memory implementation
	double		*A_s;
	double		*B_s;
	
	//Main Memory allocation
	B = (double*)malloc(NI*NJ*sizeof(double));
	A_h = (double*)malloc(NI*NJ*sizeof(double));
	B_h = (double*)malloc(NI*NJ*sizeof(double));
	//Initialize the array
	init(A_h);

	//GPU Gloval Memory allocation
	cudaMalloc((void**)&A_d, NI*NJ*sizeof(double));
	cudaMalloc((void**)&B_d, NI*NJ*sizeof(double));
	cudaMalloc((void**)&A_s, NI*NJ*sizeof(double));
	cudaMalloc((void**)&B_s, NI*NJ*sizeof(double));

	fprintf(stdout, "NI: %d NJ: %d\n", NI, NJ);

	const unsigned int numBlocksInRow = ceil((double)(NJ-2)/THREADS_PER_BLOCK_X);
	const unsigned int numBlocksInCol = ceil((double)(NI-2)/THREADS_PER_BLOCK_Y);
	dim3 gridDim(numBlocksInRow, numBlocksInCol, 1), blockDim(THREADS_PER_BLOCK_X, THREADS_PER_BLOCK_Y, 1);

	//Start of CUDA code

	gettimeofday(&start, NULL);
	cudaMemcpy(A_d, A_h, NI*NJ*sizeof(double), cudaMemcpyHostToDevice);

	Convolution_kernel <<< gridDim, blockDim >>>(A_d, B_d);
	cudaMemcpy(B_h, B_d , sizeof(double)*NI*NJ, cudaMemcpyDeviceToHost); 
	cudaDeviceSynchronize();
	gettimeofday(&end, NULL);
	fprintf(stdout, "GPU Runtime: %0.6lfs\n", ((end.tv_sec - start.tv_sec) * 1000000.0 + (end.tv_usec - start.tv_usec)) / 1000000.0);


	//Start of CUDA shared code
	gettimeofday(&start, NULL);
	cudaMemcpy(A_s, A_h, NI*NJ*sizeof(double), cudaMemcpyHostToDevice);

	Convolution_Shared <<< gridDim, blockDim >>>(A_s, B_s);
	cudaMemcpy(B_h, B_s , sizeof(double)*NI*NJ, cudaMemcpyDeviceToHost); 
	cudaDeviceSynchronize();
	gettimeofday(&end, NULL);
	fprintf(stdout, "GPU Shared Runtime: %0.6lfs\n", ((end.tv_sec - start.tv_sec) * 1000000.0 + (end.tv_usec - start.tv_usec)) / 1000000.0);



	//Start of Serial code
	gettimeofday(&start, NULL);
	Convolution(A_h, B);
	gettimeofday(&end, NULL);
	fprintf(stdout, "CPU Runtime: %0.6lfs\n", ((end.tv_sec - start.tv_sec) * 1000000.0 + (end.tv_usec - start.tv_usec)) / 1000000.0);

	//Error checking CUDA results according to Serial results
	int errors = 0;
	for (int i = 0; i < NI; ++i) {
		for (int j = 0; j < NJ; ++j) {
			double error=fabs((B[i*NJ + j] -B_h[i*NJ+j]));
			if(error>pow(10,-14)){
				printf("Error %.20f in (%d,%d)\n",error,i,j);
				errors++;
			}
		}
	}
	printf("Matrix\n\tTotal Results: %d\n\tError count: %d\n",NI*NJ,errors);
	

	free(A_h);
	free(B_h);
	free(B);

	cudaFree(A_d); 
	cudaFree(B_d); 
	cudaFree(A_s); 
	cudaFree(B_s); 
	
	return 0;
}

