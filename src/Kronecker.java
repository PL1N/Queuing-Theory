import java.util.Arrays;
import java.util.Scanner;

public class Kronecker {
    static double[][] D0;
    static double[][] D1;
    static double[][] S;
    static double[] b;
    static double[][] e;
    static double[] S0;
    static double[][] Q00;
    static double[][] Q01;
    static double[][] Q10;
    static double[][] Q0;
    static double[][] Q1;
    static double[][] Q2;


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
        S0 = productMatrixAndVector(createNegativeMatrix(S, -1), createEAsVector(S.length));
        Q00 = D0;
        Q01 = D1;
        Q10 = kroneckerProductOfMatrixAndVector(createE(W + 1), S0);
        double[] S0B = product2Vectors(S0, b);
        Q0 = kroneckerProductOfMatrixAndVector(createE(W + 1), S0B);
        Q1 = kronekerSum(D0, S);
        Q2 = kroneckerProduct(D1, createE(M));
    }

    public static void print_generator() {
        System.out.println("SO");
        print_vector(S0);
        System.out.println();
        System.out.println("Q00");
        print_matrix(Q00);
        System.out.println();
        System.out.println("Q01");
        print_matrix(Q01);
        System.out.println();
        System.out.println("Q10");
        print_matrix(Q10);
        System.out.println();
        System.out.println("Q0");
        print_matrix(Q0);
        System.out.println();
        System.out.println("Q1");
        print_matrix(Q1);
        System.out.println();
        System.out.println("Q2");
        print_matrix(Q2);
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

    private double[] prodVM(double[] a, double[][] B) {
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

    public double calcLambda() throws Exception {
        int sum = 0;
        double[][] Smin1 = inversionMatrix(S);
        double[] res = prodVM(b, Smin1);
        double mu;
        mu = 0;
        for (int i = 0; i < res.length; i++) {
            mu += res[i];
        }
        mu *= (-1);
        mu /= mu;
        double[][] matrix = sum(D0, D1);
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][0] = 1;
        }
        double[] f = new double[matrix.length];
        Arrays.fill(f, 0);
        f[0] = 1;
        double[] tetta = GaussMethod.calculateSolutions(matrix, f, f.length);
        double lambda = 0;
        res = prodVM(tetta, D1);
        for (int i = 0; i < res.length; i++) {
            lambda += res[i];
        }
        if (lambda / mu >= 1) {
            throw new Exception();
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
        double[] f = new double[Q00.length];
        Arrays.fill(f, 0);
        f[0] = 1;
        for (int i = 0; i < F.length; i++) {
            Q00[i][0] = res[i];
        }
        Q00 = createNegativeMatrix(Q00, -1);
        return GaussMethod.calculateSolutions(Q00, f, f.length);
    }

    public double[][] calcP(Matrix[] F) {
        double[][] matrix = new double[F.length][F[0].getLength()];
        double[] p0 = this.calcP0(F);
        for (int k = 0; k < F.length; k++) {
            matrix[k] = prodVM(p0, F[k].getMatrixArray());
        }
        return matrix;
    }

    public static void main(final String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите матрицу D0 :");
        D0 = Kronecker.readMatrix(in);
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
        Kronecker.print_generator();
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
    private double matrixArray[][];
    private Scanner scanner;
    private double inverse[][];

    public Matrix(int n) {
        rowsCount = n;
        colsCount = n;
        matrixArray = new double[n][n];
        inverse = new double[n][n];
    }

    public void setMatrixArray(int i, int j, double num) {
        this.matrixArray[i][j] = num;
    }

    public void setMatrixInverse(double[][] inverse) {
        this.inverse = inverse;
    }

    public int getLength() {
        return rowsCount;
    }

    public double[][] getMatrixArray() {
        return this.matrixArray;
    }
}
