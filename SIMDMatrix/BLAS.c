#include "mkl.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
float maxMatrixRow(const unsigned int N, float* A);
float maxMatrixColumn(const unsigned int N, float* A);
float* getIdentityMatrix(const unsigned int N);
int main(int argc, char** argv) {
	int N = atoi(argv[1]), M = atoi(argv[2]);
	float* A = (float*)malloc(N * N * sizeof(float));
	float* invA = (float*)malloc(N * N * sizeof(float));
	for (unsigned int i = 0; i < N; ++i) {
		for (unsigned int j = 0; j < N; ++j) {
			A[i * N + j] = (i + j) % 4;
		}
	}
	// Получение обратной матрицы
	float maxRow = maxMatrixRow(N, A);
	float maxColumn = maxMatrixColumn(N, A);
	float* I = (float*)malloc(N * N * sizeof(float));
	I = getIdentityMatrix(N);
	float* R = (float*)malloc(N * N * sizeof(float));
	//Умножение матриц: R = ((-1) / ||A||_1 / ||A||_inf * A^T) * A
	cblas_sgemm(CblasRowMajor, CblasTrans, CblasNoTrans, N, N, N, -1 / maxRow / maxColumn, A, N, A, N, 0, R, N);
	//Вычисление константы, умноженной на вектор плюс вектор: R = 1 * I + R результат
	записывается в R
	cblas_saxpy(N * N, 1, I, 1, R, 1);
	float* tmpR = (float*)malloc(N * N * sizeof(float));
	float* powR = (float*)malloc(N * N * sizeof(float));
	tmpR = getIdentityMatrix(N);
	memcpy(powR, R, N * N * sizeof(float));
	for (unsigned int i = 0; i < M; ++i) {
		//Умножение матриц: powR = tmpR * R
		cblas_sgemm(CblasRowMajor, CblasNoTrans, CblasNoTrans, N, N, N, 1, tmpR, N, R, N, 0, powR, N);
		memcpy(tmpR, powR, N * N * sizeof(float));
		//powR = 1 * invA + powR
		cblas_saxpy(N * N, 1, invA, 0, powR, 0);
	}
	//Умножение матриц: (1 / ||A||_1 / ||A||_inf * invA) * A^T
	cblas_sgemm(CblasRowMajor, CblasNoTrans, CblasTrans, N, N, N, 1 / maxRow / maxColumn, invA, N, A, N, 0, powR, N);
	memcpy(invA, powR, N * N * sizeof(float));
	free(A);

	free(invA);
	free(I);
	free(R);
	free(powR);
	free(tmpR);
	return 0;
}

float maxMatrixRow(const unsigned int N, float* A) {
	float S[N];
	for (unsigned int i = 0; i < N; ++i) {
		//Считаем промежуточные абсолютные суммы строк
		S[i] = cblas_sasum(N, A + N * i, 1);
	}
	//Возращаем значение по индексу с максимальной суммой из S
	return S[cblas_isamax(N, S, 1)];
}

float maxMatrixColumn(const unsigned int N, float* A) {
	float S[N];
	for (unsigned int i = 0; i < N; ++i) {
		//Считаем промежуточные абсолютные суммы столбцов
		S[i] = cblas_sasum(N, A + i, N);
	}
	//Возращаем значение по индексу с максимальной суммой из S
	return S[cblas_isamax(N, S, 1)];
}

float* getIdentityMatrix(const unsigned int N) {
	float* I = (float*)malloc(N * N * sizeof(float));
	memset(I, 0, N * N * sizeof(float));
	for (unsigned int i = 0; i < N; ++i) {
		I[i * N + i] = 1;
	}
	return I;
}