#include <iostream>
#include <math.h>
#include <omp.h>
using std::cout;
using std::atoi;
using std::string;

#define EPS 1e-5
#define TAU 0.000001
#define MAX_THREADS 12

void minimum_residual(int N, double** A, double* b, double* x);

int main(int argc, char** argv) {
    // Инициализация размера матрицы и выделение памяти для A, b, x
    int N = atoi(argv[1]);
    double** A = (double**)malloc(N * sizeof(double*));
    double* b = (double*)malloc(N * sizeof(double));
    double* x = (double*)malloc(N * sizeof(double));
    // Инициализация матрицы A и вектора b
    for (int i = 0; i < N; i++) {
        A[i] = (double*)malloc(N * sizeof(double));
        x[i] = 0;
        b[i] = N + 1.0;
        for (int j = 0; j < N; j++) {
            if (i == j) {
                A[i][j] = 2.0;
            } else {
                A[i][j] = 1.0;
            }
        }
    }

    // Замер времени начала выполнения
    double start = omp_get_wtime();

    // Вызов функции для вычисления минимального остатка
    minimum_residual(N, A, b, x);

    // Замер времени завершения выполнения
    double end = omp_get_wtime();

    // Вычисление и вывод времени выполнения и ускорения программы
    double Tp = 34.385; // Время выполнения на одном процессоре (Tp нужно измерить)
    double Sp = Tp / (end - start); // Ускорение
    cout << "Time taken: " << end - start << " sec\n";
    cout << "Sp: " << Sp << "\n";
    cout << "Ep: " << (Sp / MAX_THREADS) * 100 << "\n";

    // Освобождение выделенной памяти
    for (int i = 0; i < N; i++) {
        free(A[i]);
    }
    free(A);
    free(b);
    free(x);
    return 0;
}

// Функция для вычисления минимального остатка
void minimum_residual(int N, double** A, double* b, double* x) {
    double tau;
    double diff_norm;
    double b_norm;
    int complete = 0;
    double* y = (double*)malloc(N * sizeof(double));
    double* diff = (double*)malloc(N * sizeof(double));
    double* Ay = (double*)malloc(N * sizeof(double));
    // Параллельная обработка с помощью OpenMP
    #pragma omp parallel num_threads(MAX_THREADS)
    {
        for (int i = 0; i < 9999 && !complete; i++) {
            #pragma omp single
            {
                tau = 0.0;
                diff_norm = 0.0;
                b_norm = 0.0;
            }
            // Вычисление Ax - b
            #pragma omp for schedule(runtime)
            for (int j = 0; j < N; j++) {
                y[j] = 0;
                for (int k = 0; k < N; k++) {
                    y[j] += A[j][k] * x[k];
                }
            }

            // Вычисление Ax - b
            #pragma omp for schedule(runtime)
            for (int j = 0; j < N; j++) {
                y[j] -= b[j];
            }

            // Вычисление Ay
            #pragma omp for schedule(runtime)
            for (int j = 0; j < N; j++) {
                Ay[j] = 0;
                for (int k = 0; k < N; k++) {
                    Ay[j] += A[j][k] * y[k];
                }
            }
            #pragma omp for reduction(+:tau) schedule(runtime)
            for (int j = 0; j < N; j++) {
                tau += y[j] * Ay[j];
            }

            #pragma omp single
            {
                double Ay_norm = 0.0;
                for (int j = 0; j < N; j++) {
                    Ay_norm += Ay[j] * Ay[j];
                }
                tau /= Ay_norm;
            }

            #pragma omp for schedule(runtime)
            for (int j = 0; j < N; j++) {
                x[j] -= tau * y[j];
            }
            #pragma omp for reduction(+:diff_norm, b_norm) schedule(runtime)
            for (int j = 0; j < N; j++) {
                diff[j] = 0;
                for (int k = 0; k < N; k++) {
                    diff[j] += A[j][k] * x[k];
                }
                diff[j] -= b[j];
                diff_norm += diff[j] * diff[j];
                b_norm += b[j] * b[j];
            }

            #pragma omp single
            {
                if (diff_norm / b_norm < EPS) {
                    complete = 1;
                }

            }
        }
    }

    // Освобождение выделенной памяти
    free(y);
    free(diff);
    free(Ay);
}