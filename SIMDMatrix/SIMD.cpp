#include <algorithm>
using std::swap;
#include <cmath>
using std::abs;
#include <cstring>
using std::memcpy;
using std::memset;
#include <ctime>
using std::clock;
#include <iostream>
using std::cout;
using std::cin;
#include <xmmintrin.h>
float* getInverseMatrix(const unsigned int, float*, const unsigned int);
float* getIdentityMatrix(const unsigned int);
float* getTransposedMatrix(const unsigned int, float*);
float* getScalarMulMatrices(const unsigned int, float*, float);
float* getSubMatrices(const unsigned int, float*, float*);
float* getSumMatrices(const unsigned int, float*, float*);
float* getMulMatrices(const unsigned int, float*, float*);
float maxMatrixColumn(const unsigned int, float*);
float maxMatrixRow(const unsigned int, float*);
int main()
{
unsigned int N, M;
cin >> N >> M;
float* A = new float[N * N];
float* invA = new float[N * N];
for (unsigned int i = 0; i < N; ++i) {
for (unsigned int j = 0; j < N; ++j) {
A[i * N + j] = (float)((i + j) % 4);
}
}
clock_t start = clock();
invA = getInverseMatrix(N, A, M);
clock_t end = clock();
cout << "Time spent: " << (double)(end - start) / CLOCKS_PER_SEC << "\n";
return 0;
}
float* getInverseMatrix(const unsigned int N, float* A, const unsigned int M) {
float* I = getIdentityMatrix(N);
float* tA = getTransposedMatrix(N, A);
10

float* B = getScalarMulMatrices(N, tA, (1.0f / (maxMatrixColumn(N, A) * maxMatrixRow(N,
A))));
float* R = getSubMatrices(N, I, getMulMatrices(N, B, A));
float* powR = new float[N * N];
delete[] tA;
memcpy(&powR, &R, sizeof(float));
for (unsigned int i = 0; i < M; ++i) {
I = getSumMatrices(N, I, powR);
powR = getMulMatrices(N, powR, R);
}
delete[] powR;
delete[] R;
I = getMulMatrices(N, I, B);
delete[] B;
return I;
}
float* getIdentityMatrix(const unsigned int N) {
float* I = new float[N * N];
memset(I, 0, N * N * sizeof(float));
for (unsigned int i = 0; i < N; i++) {
I[i * N + i] = 1;
}
return I;
}
float* getTransposedMatrix(const unsigned int N, float* A) {
float* tA = new float[N * N];
for (unsigned int i = 0; i < N; ++i) {
for (unsigned int j = 0; j < N; ++j) {
tA[i * N + j] = A[j * N + i];
}
}
return tA;
}
float maxMatrixColumn(const unsigned int N, float* A) {
float max = 0;
for (unsigned int i = 0; i < N; ++i) {
float curr = 0;
for (unsigned int j = 0; j < N; ++j) {
curr += abs(A[j * N + i]);
}
if (curr > max) {
max = curr;
}
}
return max;
}
float maxMatrixRow(const unsigned int N, float* A) {
float max = 0;
for (unsigned int i = 0; i < N; ++i) {
float curr = 0;
for (unsigned int j = 0; j < N; ++j) {
curr += abs(A[i * N + j]);
}

11

if (curr > max) {
max = curr;
}
}
return max;
}
float* getScalarMulMatrices(const unsigned int N, float* A, float K) {
float* kA = new float[N * N];
__m128* A_m = (__m128*)A;
__m128* kA_m = (__m128*)kA;
__m128 K_m = _mm_load_ps1(&K);
for (unsigned int i = 0; i < N * N / 4; ++i) {
kA_m[i] = _mm_mul_ps(A_m[i], K_m);
}
return kA;
}
float* getSubMatrices(const unsigned int N, float* A, float* B) {
float* C = new float[N * N];
__m128* A_m = (__m128*)A;
__m128* B_m = (__m128*)B;
__m128* C_m = (__m128*)C;
for (unsigned int i = 0; i < N * N / 4; ++i) {
C_m[i] = _mm_sub_ps(A_m[i], B_m[i]);
}
return C;
}
float* getSumMatrices(const unsigned int N, float* A, float* B) {
float* C = new float[N * N];
__m128* A_m = (__m128*)A;
__m128* B_m = (__m128*)B;
__m128* C_m = (__m128*)C;
for (unsigned int i = 0; i < N * N / 4; ++i) {
C_m[i] = _mm_add_ps(A_m[i], B_m[i]);
}
return C;
}
float* getMulMatrices(const unsigned int N, float* A, float* B) {
float* C = new float[N * N];
float* tB = getTransposedMatrix(N, B);
for (unsigned int i = 0; i < N; ++i) {
for (unsigned int j = 0; j < N; ++j) {
__m128 temp_m = _mm_setzero_ps();
for (unsigned int k = 0; k < N; k += 4) {
temp_m = _mm_add_ps(temp_m, (_mm_mul_ps(_mm_loadu_ps(&A[i * N + k]),

_mm_loadu_ps(&tB[j * N + k]))));

}
float temp[4];
_mm_store_ps(temp, temp_m);
C[i * N + j] = temp[0] + temp[1] + temp[2] + temp[3];
}
}
delete[] tB;
return C;
}