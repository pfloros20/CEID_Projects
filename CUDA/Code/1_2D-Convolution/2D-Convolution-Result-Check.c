#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <math.h>
#include <sys/time.h>

/* Problem size */
#define NI 5000
#define NJ 5000

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

void init(double* A,double* C)
{
	int i, j;

	FILE *fp = fopen ("datasetA.txt","r");
	//printf("Reading A from file...\n");
	fread(A,sizeof(double),NI*NJ,fp);
	fclose(fp);
	FILE* fp1 = fopen ("datasetB.txt","r");
	//printf("Reading B from file...\n");
	fread(C,sizeof(double),NI*NJ,fp1);

	fclose(fp1);
	//printf("Initialization done!\n");
}

int main(int argc, char *argv[])
{
	double		*A;
	double		*B;
	double		*C;
	struct timeval	cpu_start, cpu_end;

	
	A = (double*)malloc(NI*NJ*sizeof(double));
	B = (double*)malloc(NI*NJ*sizeof(double));
	C = (double*)malloc(NI*NJ*sizeof(double));

	//initialize the arrays
	init(A,C);
	
	gettimeofday(&cpu_start, NULL);
	Convolution(A, B);
	gettimeofday(&cpu_end, NULL);
	//fprintf(stdout, "CPU Runtime: %0.6lfs\n", ((cpu_end.tv_sec - cpu_start.tv_sec) * 1000000.0 + (cpu_end.tv_usec - cpu_start.tv_usec)) / 1000000.0);
	for (int i = 0; i < NI; ++i) {
		for (int j = 0; j < NJ; ++j) {
			double error=fabs(B[i*NJ + j] -C[i*NJ+j]);
			if(error>0.000000000000001){
				//printf("Output is not correct!\n");
				printf("Error %.20f in (%d,%d)\n",error,i,j);
				//printf("C %.20f\n",C[i*NJ+j]);
			}
			 // else
			 // 	printf("yay in (%d,%d)\n",i,j);
		}
	}
	free(A);
	free(B);
	
	return 0;
}

