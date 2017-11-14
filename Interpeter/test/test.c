
/**
 * a very simple c program, just for test
 */

int a = 3;
double b = 3.1415;

{
	int a = 4;
	printf(a);
	printf(b);

	double b = 5.12;
	printf(b);
}

{
	double a = 3.14;

	{
		int a = 5;
		printf(a);
	}

	printf(a);
}

printf(a);

while(a > 0){
	printf("hello world");
	a = a - 1;
}

for(int i = 0; i < 5; i = i + 1){
	printf("for statement");
}
