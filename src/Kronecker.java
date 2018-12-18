import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Kronecker {
    static Matrix D0;
    static Matrix D1;
    static Matrix S;
    static double[] b;
    static Matrix e;
    static double[] S0;
    static Matrix Q00;
    static Matrix Q01;
    static Matrix Q10;
    static Matrix Q0;
    static Matrix Q1;
    static Matrix Q2;
    static double epsF = Math.pow(10, -8);


    public static double[][] readMatrix(Scanner in) {
        System.out.println("Введите размер квадртаной матрицы : ");
        int n = in.nextInt();
        double[][] arr = new double[n][n];
        for (int i = 0; i < n; ++i) {
            System.out.println("Ввелдие элементы " + (i + 1) + " строки");
            for (int j = 0; j < n; ++j)
                arr[i][j] = in.nextDouble();
        }
        return arr;
    }

    public static double[] readVector(Scanner in) {
        System.out.println("Введите кол-во элементов вектора : ");
        int n = in.nextInt();
        double[] arr = new double[n];
        for (int i = 0; i < arr.length; ++i) {
            System.out.println("Введите " + (i + 1) + " элемент");
            arr[i] = in.nextDouble();
        }
        return arr;
    }

    public static double[][] kronekerSum(double[][] D0, double[][] S) {
        double[][] e1 = createE(D0.length);
        double[][] e2 = createE(S.length);
        double[][] result = sum(kroneckerProduct(D0, e1), kroneckerProduct(e2, S));
        return result;
    }

    public static double[][] inversionMatrix(double[][] matrix) {
        double temp;
        double[][] E = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++) {
                E[i][j] = 0f;
                if (i == j)
                    E[i][j] = 1f;
            }
        for (int k = 0; k < matrix.length; k++) {
            temp = matrix[k][k];
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[k][j] /= temp;
                E[k][j] /= temp;
            }
            for (int i = k + 1; i < matrix.length; i++) {
                temp = matrix[i][k];
                for (int j = 0; j < matrix.length; j++) {
                    matrix[i][j] -= matrix[k][j] * temp;
                    E[i][j] -= E[k][j] * temp;
                }
            }
        }
        for (int k = matrix[0].length - 1; k > 0; k--) {
            for (int i = k - 1; i >= 0; i--) {
                temp = matrix[i][k];

                for (int j = 0; j < matrix[0].length; j++) {
                    matrix[i][j] -= matrix[k][j] * temp;
                    E[i][j] -= E[k][j] * temp;
                }
            }
        }
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                matrix[i][j] = E[i][j];
        return matrix;
    }

    public static double[][] createNegativeMatrix(double[][] matrix, double c) {
        for (int i = 0; i < matrix.length; ++i)
            for (int j = 0; j < matrix[0].length; ++j)
                matrix[i][j] = c * matrix[i][j];
        return matrix;
    }

    public static double[] createEAsVector(int length) {
        double[] result = new double[length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = 1;
        }
        return result;
    }

    public static void infinitesimalGenerator(int W, int M) {
        S0 = productMatrixAndVector(createNegativeMatrix(S.getMatrixArray(), -1), createEAsVector(S.getLength()));
        Q00 = D0;
        Q01 = D1;
        Q10 = new Matrix(kroneckerProductOfMatrixAndVector(createE(W + 1), S0));
        double[] S0B = product2Vectors(S0, b);
        Q0 = new Matrix(kroneckerProductOfMatrixAndVector(createE(W + 1), S0B));
        Q1 = new Matrix(kronekerSum(D0.getMatrixArray(), S.getMatrixArray()));
        Q2 = new Matrix(kroneckerProduct(D1.getMatrixArray(), createE(M)));
    }

    public static void print_generator() {
        System.out.println("SO");
        print_vector(S0);
        System.out.println();
        System.out.println("Q00");
        print_matrix(Q00.getMatrixArray());
        System.out.println();
        System.out.println("Q01");
        print_matrix(Q01.getMatrixArray());
        System.out.println();
        System.out.println("Q10");
        print_matrix(Q10.getMatrixArray());
        System.out.println();
        System.out.println("Q0");
        print_matrix(Q0.getMatrixArray());
        System.out.println();
        System.out.println("Q1");
        print_matrix(Q1.getMatrixArray());
        System.out.println();
        System.out.println("Q2");
        print_matrix(Q2.getMatrixArray());
        System.out.println();
    }

    public static double[] product2Vectors(double[] a, double[] b) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < b.length; ++j)
                result[i] = a[i] * b[j];
        }
        return result;
    }

    public static double[][] product2Matrix(double[][] a, double[][] b) {
        if (a[0].length == b.length) {
            double[][] Result = new double[a.length][b[0].length];
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < b[0].length; j++) {
                    for (int k = 0; k < a.length; k++) {
                        Result[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
            return Result;
        }
        return null;
    }

    public static double[] productMatrixAndVector(double[][] matrix, double[] vector) {
        double[] resultVector = new double[matrix[0].length];
        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                resultVector[j] += matrix[j][i] * vector[i];
            }
        }
        return resultVector;
    }

    public static double[][] createE(int length) {
        int k = length;
        double[][] e = new double[k][k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                if (i == j) {
                    e[i][j] = 1;
                } else {
                    e[i][j] = 0;
                }
            }
        }
        return e;
    }

    public static double[][] createEWithScanner(Scanner in) {
        int k = in.nextInt();
        double[][] e = new double[k][k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                if (i == j) {
                    e[i][j] = 1;
                } else {
                    e[i][j] = 0;
                }
            }
        }
        return e;
    }

    public static double[][] sum(double[][] a, double[][] b) {
        double[][] sum = {};
        if (a.length == b.length && a[0].length == b[0].length) {
            sum = new double[a.length][a[0].length];
            for (int i = 0; i < a.length; ++i) {
                for (int j = 0; j < a[0].length; ++j) {
                    sum[i][j] = a[i][j] + b[i][j];
                }
            }
        }
        return sum;
    }

    public static double[][] kroneckerProductOfMatrixAndVector(double[][] a, double[] b) {
        double[][] b1 = new double[1][b.length];
        for (int i = 0; i < b1[0].length; ++i) {
            b1[0][i] = b[i];
        }
        final double[][] c = new double[a.length * b1.length][];
        for (int ix = 0; ix < c.length; ix++) {
            final int num_cols = a[0].length * b1[0].length;
            c[ix] = new double[num_cols];
        }
        for (int ia = 0; ia < a.length; ia++) {
            for (int ja = 0; ja < a[ia].length; ja++) {
                for (int ib = 0; ib < b1.length; ib++) {
                    for (int jb = 0; jb < b1[ib].length; jb++) {
                        c[b1.length * ia + ib][b1[ib].length * ja + jb] = a[ia][ja] * b1[ib][jb];
                    }
                }
            }
        }
        return c;
    }

    public static double[][] kroneckerProduct(double[][] a, double[][] b) {
        double[][] c = new double[a.length * b.length][];
        for (int ix = 0; ix < c.length; ix++) {
            int num_cols = a[0].length * b[0].length;
            c[ix] = new double[num_cols];
        }
        for (int ia = 0; ia < a.length; ia++) {
            for (int ja = 0; ja < a[ia].length; ja++) {
                for (int ib = 0; ib < b.length; ib++) {
                    for (int jb = 0; jb < b[ib].length; jb++) {
                        c[b.length * ia + ib][b[ib].length * ja + jb] = a[ia][ja] * b[ib][jb];
                    }
                }
            }
        }
        return c;
    }

    public static void print_matrix(double[][] m) {
        String[][] sts = new String[m.length][];
        int max_length = 0;
        for (int im = 0; im < m.length; im++) {
            sts[im] = new String[m[im].length];
            for (int jm = 0; jm < m[im].length; jm++) {
                final String st = String.valueOf(m[im][jm]);
                if (st.length() > max_length) {
                    max_length = st.length();
                }
                sts[im][jm] = st;
            }
        }

        final String format = String.format("%%%ds", max_length);
        for (int im = 0; im < m.length; im++) {
            System.out.print("|");
            for (int jm = 0; jm < m[im].length - 1; jm++) {
                System.out.format(format, m[im][jm]);
                System.out.print(" ");
            }
            System.out.format(format, m[im][m[im].length - 1]);
            System.out.println("|");
        }
    }

    public static void print_vector(double[] m) {
        System.out.print("| ");
        for (int i = 0; i < m.length; ++i) {
            System.out.print(m[i] + " ");
        }
        System.out.print(" |");
    }

    private static double[][] testKronekerProduct(final double[][] a, final double[][] b) {
        System.out.println("Testing Kronecker product");
        System.out.println("Size of matrix a: " + a.length + " by " + a[0].length);
        System.out.println("Matrix a:");
        print_matrix(a);
        System.out.println("Size of matrix b: " + b.length + " by " + b[0].length);
        System.out.println("Matrix b:");
        print_matrix(b);
        System.out.println("Calculating matrix c as Kronecker product");
        final double[][] c = kroneckerProduct(a, b);
        System.out.println("Size of matrix c: " + c.length + " by " + c[0].length);
        System.out.println("Matrix c:");
        print_matrix(c);
        return c;
    }

    private static void test1() {
        final double[][] a = new double[2][];
        a[0] = new double[]{1, 2};
        a[1] = new double[]{3, 4};
        final double[][] b = new double[2][];
        b[0] = new double[]{0, 5};
        b[1] = new double[]{6, 7};
        testKronekerProduct(a, b);
    }

    private static double[] prodVM(double[] a, double[][] B) {
        double[] res = new double[B.length];

        double sum;
        for (int i = 0; i < B.length; i++) {
            sum = 0;
            for (int j = 0; j < B[i].length; j++) {
                sum += a[j] * B[j][i];
            }
            res[i] = sum;
        }
        return res;
    }

    public static double calcLambda() {
        int sum = 0;
        double[][] Smin1 = inversionMatrix(S.getMatrixArray());
        double[] res = prodVM(b, Smin1);
        double mu;
        mu = 0;
        for (int i = 0; i < res.length; i++) {
            mu += res[i];
        }
        mu *= (-1.0);
        mu = 1.0 / mu;
        double[][] matrix = sum(D0.getMatrixArray(), D1.getMatrixArray());
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][0] = 1;
        }
        double[] f = new double[matrix.length];
        Arrays.fill(f, 0);
        f[0] = 1;
        matrix = Kronecker.transposeMatrix(matrix);
        double[] tetta = GaussMethod.calculateSolutions(matrix, f, f.length);
        double lambda = 0;
        res = prodVM(tetta, D1.getMatrixArray());
        for (int i = 0; i < res.length; i++) {
            lambda += res[i];
        }
        System.out.println("mu = " + mu);
        if (lambda / mu >= 1) {
            System.out.println("Анин метод");
            ;
            System.out.println("Условие не выполняется");
        }
        return lambda;
    }

    private double[] sumV(double[] a, double[] b) {
        double[] res = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = a[i] + b[i];
        }
        return res;
    }

    public double[] calcP0(Matrix[] F) {
        double[][] matrix;
        double[] vector = new double[F[0].getLength()];
        double[] res = new double[F[0].getLength()];
        for (int k = 0; k < F.length; k++) {
            matrix = F[k].getMatrixArray();
            Arrays.fill(vector, 0);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    vector[j] += matrix[i][j];
                }
            }
            res = sumV(vector, res);
        }
        double[] f = new double[Q00.getLength()];
        Arrays.fill(f, 0);
        f[0] = 1;
        for (int i = 0; i < F.length; i++) {
            Q00.matrixArray[i][0] = res[i];
        }
        Q00 = new Matrix(createNegativeMatrix(Q00.getMatrixArray(), -1));
        return GaussMethod.calculateSolutions(Q00.getMatrixArray(), f, f.length);
    }

    public double[][] calcP(Matrix[] F) {
        double[][] matrix = new double[F.length][F[0].getLength()];
        double[] p0 = this.calcP0(F);
        for (int k = 0; k < F.length; k++) {
            matrix[k] = prodVM(p0, F[k].getMatrixArray());
        }
        return matrix;
    }

    public static double[][] transposeMatrix(double[][] M) {
        int N = M.length;
        double[][] transpose = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                transpose[i][j] = M[j][i];
            }
        }
        return transpose;
    }

    public static double[] getVectorFromFile(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        int n = scanner.nextInt();
        double[] mas = new double[n];
        for (int i = 0; i < n; i++) {
            mas[i] = scanner.nextDouble();
        }
        scanner.close();
        return mas;
    }

    public static void main(final String[] args) {
        try {
           /* Scanner in = new Scanner(System.in);
            System.out.println("Введите матрицу D.txt :");
            D.txt = Kronecker.readMatrix(in);
            System.out.println("Введите матрицу D1 :");
            D1 = Kronecker.readMatrix(in);
            System.out.println("Введите матрицу S :");
            S = Kronecker.readMatrix(in);
            System.out.println("Введите вектор b :");
            b = Kronecker.readVector(in);
            System.out.println("Введите размерность матрицы I :");
            e = Kronecker.createEWithScanner(in);
            System.out.println("Введите число W :");
            int W = in.nextInt();
            System.out.println("Введите число M :");
            int M = in.nextInt();
            Kronecker.infinitesimalGenerator(W, M);
            Kronecker.print_generator();*/


            Kronecker.D0 = new Matrix("inputData/D.txt");
            D0.getMatrixFromFile();
            System.out.println("D.txt:");
            D0.printMatrix();

            Kronecker.S = new Matrix("inputData/S.txt");
            S.getMatrixFromFile();
            System.out.println("S.txt:");
            S.printMatrix();

            double norm = Matrix.normMatrix(S);
            System.out.println("norma = " + norm);

            Matrix D1 = new Matrix("inputData/D1.txt");
            Kronecker.S = S;

            Kronecker.D1 = new Matrix("inputData/D1.txt");
            D1.getMatrixFromFile();
            System.out.println("D1.txt:");
            D1.printMatrix();

//            double l = Kronecker.calcLambda();
            //          System.out.println(l);
            Kronecker.b = Kronecker.getVectorFromFile("inputData/b.txt");
            System.out.println("b:");
            for (int i = 0; i < Kronecker.b.length; i++) {
                System.out.print(b[i] + " ");
            }

            //double l = Kronecker.calcLambda();
            //System.out.println(l);

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
        }


        double[][] Q0 = {{0, 0, 0, 0}, {0, 0, 0, 0}, {30, 0, 30, 0}, {0, 0, 0, 0}};
        double[][] Q1 = {{22, -30, 1, 0}, {0, -38, 0, 1}, {1, 0, 19, -30}, {0, 1, 0, -41}};
        double[][] Q2 = {{2, 0, 5, 0}, {0, 2, 0, 5}, {4, 0, 6, 0}, {0, 4, 0, 6}};
        double[][] Q10 = {{-30, 30, 0, 0}, {0, -30, 0, 0}, {0, 0, -30, 30}, {0, 0, 0, -30}};
        double[][] G0m = {{-0.84164, 1.50869, 0.719406, -1.20394}, {0., 0.789996, 0.,
                0.0192822}, {0.927345, -1.62638, -0.53449, 0.945648}, {0.,
                0.0192879, 0., 0.732196}};

        double[][] G4m = {{-8.8004, 0.4271, -9.2571, 0.7681}, {0., 0.0001, 0.,
                0.0001}, {-15.6387, 0.6764, -16.3621, 1.2166}, {0., 0.0001, 0.,
                0.0001}};

        Matrix G0 = new Matrix(G0m);
        Matrix G = new Matrix(G4m);
        Matrix[][] arrayQ = new Matrix[3][3];
        arrayQ[0][0] = Kronecker.D0;
        arrayQ[0][1] = Kronecker.D1;
        arrayQ[1][0] = new Matrix(Q10);
        arrayQ[1][1] = new Matrix(Q1);
        arrayQ[1][2] = new Matrix(Q2);
        arrayQ[2][1] = new Matrix(Q0);
        arrayQ[2][2] = new Matrix(Q1);



         Matrix[] resFi = Matrix.getFi(arrayQ, G0, G);
         for (int i = 0; i < resFi.length; i++) {
             System.out.println("F" + i);
             resFi[i].printMatrix();
             System.out.println();
         }


//            double[][] Matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
//            double[][] transpose = Kronecker.transposeMatrix(Matrix);
//            for (int i = 0; i < Matrix.length; i++) {
//                for (int j = 0; j < Matrix.length; j++) {
//                    System.out.print(transpose[i][j] + " ");
//                }
//                System.out.println();
//            }


    }

}

class GaussMethod {
    public static double[] calculateSolutions(double[][] matrix, double[] f, int n) {
        double[] x = new double[n];
        double tmp;
        for (int i = 0; i < n; i++) {
            tmp = matrix[i][i];
            f[i] /= tmp;
            for (int j = n - 1; j >= i; j--) {
                matrix[i][j] /= tmp;
            }
            for (int j = i + 1; j < n; j++) {
                tmp = matrix[j][i];
                f[j] -= tmp * f[i];
                for (int k = n - 1; k >= i; k--) {
                    matrix[j][k] -= tmp * matrix[i][k];
                }
            }
        }
        /*обратный ход*/
        x[n - 1] = f[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            x[i] = f[i];
            for (int j = i + 1; j < n; j++) {
                x[i] -= matrix[i][j] * x[j];
            }
        }
        return x;
    }
}


class Matrix {
    private String filePath;
    private int rowsCount;
    private int colsCount;
    public double matrixArray[][];
    private Scanner scanner;
    private double inverse[][];

    public Matrix(double[][] matrArray) {
        int n = matrArray.length;
        rowsCount = n;
        colsCount = n;
        matrixArray = matrArray;
        inverse = new double[n][n];
    }


    public static double normMatrix(Matrix A) {
        int N = A.getLength();
        double norma = 0;
        double tmp;
        for (int i = 0; i < N; i++) {
            tmp = 0;
            for (int j = 0; j < N; j++) {
                tmp += Math.abs(A.matrixArray[i][j]);
            }
            if (norma <= tmp) norma = tmp;
        }
        return norma;
    }


    private static Matrix I(int n) {
        Matrix I = new Matrix(n);
        for (int i = 0; i < n; i++) {
            I.matrixArray[i][i] = 1;
        }
        return I;
    }


    public static Matrix matrixMultipleMatrix(Matrix A, Matrix B) {
        int rows = A.rowsCount;
        int cols = B.colsCount;
        int N = A.colsCount;
        double sum;
        Matrix result = new Matrix(rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum = 0;
                for (int k = 0; k < N; k++) {
                    sum += A.matrixArray[i][k] * B.matrixArray[k][j];
                }
                result.setMatrixArray(i, j, sum);
            }
        }
        return result;
    }

    public Matrix multipleMatrixOnValue(double a) {
        Matrix tmp = new Matrix(this.rowsCount);
        for (int i = 0; i < this.rowsCount; i++)
            for (int j = 0; j < this.colsCount; j++) {
                tmp.setMatrixArray(i, j, this.matrixArray[i][j]);
            }

        for (int i = 0; i < this.getLength(); i++) {
            for (int j = 0; j < this.getLength(); j++) {
                tmp.matrixArray[i][j] = a * tmp.matrixArray[i][j];
            }
        }
        return tmp;
    }

    public void setMatrixArray(int i, int j, double num) {
        this.matrixArray[i][j] = num;
    }

    public void setMatrixInverse(double[][] inverse) {
        int rows = inverse.length;
        int cols = inverse[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.inverse[i][j] = inverse[i][j];
            }
        }
    }

    public void setMatrixArray(double[][] matrixArray) {
        int rows = matrixArray.length;
        int cols = matrixArray[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.matrixArray[i][j] = matrixArray[i][j];
            }
        }
    }

    public static double[][] calculateInverseMatrix(Matrix matrix) {
        Matrix remember = new Matrix(matrix.getLength());
        remember.setMatrixArray(matrix.matrixArray);
        int n = matrix.getLength();
        Matrix test = new Matrix(matrix.matrixArray);
        //создаем единичную матрицу для формирования обратной
        for (int i = 0; i < n; i++) {
            test.inverse[i][i] = 1;
        }

        double del;

        for (int i = 0; i < n; i++) {
            del = test.matrixArray[i][i];
            //делим строку
            for (int j = 0; j < n; j++) {
                test.matrixArray[i][j] /= del;
                test.inverse[i][j] /= del;
            }

            double[] str = new double[n];
            double[] str2 = new double[n];
            for (int w = 0; w < n; w++) {
                str[w] = test.matrixArray[i][w];
                str2[w] = test.inverse[i][w];
            }

            //зануляем элементы под единицей
            for (int t = i + 1; t < n; t++) { //выбираем строку для зануления
                del = test.matrixArray[t][i];
                double[] change1 = Matrix.multipleVectorValue(str, del);
                double[] change2 = Matrix.multipleVectorValue(str2, del);
                for (int k = 0; k < n; k++) { //зануляем элемент и изменяем строку
                    test.matrixArray[t][k] = test.matrixArray[t][k] - change1[k];
                    test.inverse[t][k] = test.inverse[t][k] - change2[k];
                }
            }
        }

        /*обратный ход*/
        double[] str = new double[n];

        for (int i = n - 1; i > 0; i--) {//столбцы
            for (int w = 0; w < n; w++) {
                str[w] = test.inverse[i][w];
            }
            for (int j = i - 1; j >= 0; j--) {//строки
                del = test.matrixArray[j][i];
                test.matrixArray[j][i] = 0;
                double[] tmp = str;
                for (int w = 0; w < n; w++) {
                    tmp[w] = str[w];
                    test.inverse[j][w] -= del * tmp[w];
                }
            }
        }
        matrix.setMatrixArray(remember.matrixArray);
        matrix.setMatrixInverse(test.inverse);

        return test.getMatrixInverse();
    }

    public double[][] getMatrixInverse() {
        return this.inverse;
    }

    private static double[] multipleVectorValue(double[] vector, double value) {
        double[] tmp = new double[vector.length];
        for (int j = 0; j < vector.length; j++) {
            tmp[j] = vector[j];
        }
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] *= value;
        }
        return tmp;
    }

    public static Matrix sumTwoMatrix(Matrix A, Matrix B) {
        int size1 = A.rowsCount;
        int size2 = B.colsCount;
        Matrix sum = new Matrix(size1);
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                sum.matrixArray[i][j] = A.matrixArray[i][j] + B.matrixArray[i][j];
            }
        }
        return sum;
    }

    public static Matrix[] getFi(Matrix[][] Q, Matrix G0, Matrix G) {
        int N = Q.length;
        Matrix[] F = new Matrix[N];
        Matrix Qshtrihii, Qshtrihij;
        F[0] = I(2);
        for (int i = 1; i < N; i++) {
            while (normMatrix(F[i - 1]) > Kronecker.epsF) {
                if (i == 1) {
                    Qshtrihii = Matrix.sumTwoMatrix(Kronecker.Q00, Matrix.matrixMultipleMatrix(Kronecker.Q01, G0));
                    Qshtrihij = Kronecker.Q01;
                } else {
                    Qshtrihii = Matrix.sumTwoMatrix(Kronecker.Q1, Matrix.matrixMultipleMatrix(Kronecker.Q2, G));
                    Qshtrihij = Kronecker.Q2;
                }


                Matrix multipleFQ = matrixMultipleMatrix(F[i - 1], Qshtrihij);
                Matrix tmp2 = Qshtrihii.multipleMatrixOnValue(-1);
                double[][] inverseQ = Matrix.calculateInverseMatrix(tmp2);
                Matrix inverse = new Matrix(inverseQ);
                F[i] = matrixMultipleMatrix(multipleFQ, inverse);

            }
        }
        return F;
    }

//    public static Matrix[] getFi(Matrix[][] Q, double epsF) {
//        int N = Q.length;
//        Matrix[] F = new Matrix[N];
//        F[0] = I(2);
//        for (int i = 1; i < N; i++) {
//            while (normMatrix(F[i-1]) > epsF){
//                Matrix multipleFQ = matrixMultipleMatrix(F[i-1], Q[i-1][i]);
//                Matrix tmp2 = Q[i][i].multipleMatrixOnValue(-1);
//                double[][] inverseQ = Matrix.calculateInverseMatrix(tmp2);
//                Matrix inverse = new Matrix(inverseQ);
//                F[i] =  matrixMultipleMatrix(multipleFQ, inverse);
//
//            }
//        }
//        return F;
//    }

    public Matrix(int n) {
        rowsCount = n;
        colsCount = n;
        matrixArray = new double[n][n];
        inverse = new double[n][n];
    }


    public Matrix(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.scanner = new Scanner(new File(this.filePath));
        this.rowsCount = scanner.nextInt();
        this.colsCount = scanner.nextInt();
        this.matrixArray = null;
    }

    public void getMatrixFromFile() {
        matrixArray = new double[this.rowsCount][this.colsCount];
        inverse = new double[this.rowsCount][this.colsCount];
        for (int i = 0; i < this.rowsCount; i++) {
            for (int j = 0; j < this.colsCount; j++) {
                matrixArray[i][j] = scanner.nextDouble();
            }
        }
    }

    public int getLength() {
        return rowsCount;
    }

    public double[][] getMatrixArray() {
        return this.matrixArray;
    }

    public void printMatrix() {
        if (this.matrixArray != null) {
            for (int i = 0; i < this.rowsCount; i++) {
                for (int j = 0; j < this.colsCount; j++) {
                    System.out.print(matrixArray[i][j] + " ");
                }
                System.out.println();
            }
        }
        System.out.println();
    }
}
