#include <stdio.h>
#include <stdlib.h>

int main (int argc,char **argv) {
	if(argc<3)
		return 0;
	long int line;
	long int n=atoi(argv[3]);
	double num[n];
	FILE *in=fopen(argv[1],"r");
	FILE *out=fopen(argv[2],"w");
	while(!feof(in)){
		fread(&line,sizeof(long),1,in);
		fread(num,sizeof(double),n,in);
		if(feof(in))
			break;
		fprintf(out,"%ld\t",line);
		for(int i=0;i<n;i++)
			fprintf(out,"%19.15f",num[i]);
		fprintf(out,"\n");
	}
   return(0);
}