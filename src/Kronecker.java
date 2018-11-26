import java.util.Scanner;

public class Kronecker {

    private static double[][] S0;
    private static double[][] Q00;
    private static double[][] Q01;
    private static double[][] Q10;
    private static double[][] Q0;
    private static double[][] Q1;
    private static double[][] Q2;

    public static int[][] readMAtrix(Scanner in) {
        int n = in.nextInt();
        int [][] arr = new int[n][n];
        for (int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                arr[i][j] = in.nextInt();
        return arr;
    }

    public static double[][] kronekerSum(double [][] D0,double [][] S) {
        double [][] e1 = createE(D0.length);
        double [][] e2 = createE(S.length);
        double [][] result = sum(kroneckerProduct(D0,e1) , kroneckerProduct(e2,S));
        return result;
    }


    public static double[][] createNegativeMatrix (double [][] matrix, double c) {
        for (int i = 0; i < matrix.length; ++i)
            for(int j = 0; j < matrix[0].length; ++j)
                matrix[i][j] = c * matrix[i][j];
        return matrix;
    }

    public static double[][] createEAsVector(int length) {
        double [][] result = new double[1][length];
        for (int i = 0 ; i < result.length; ++i) {
            result[0][i] = 1;
        }
        return result;
    }

    public static void infinitesimalGenerator(double [][] D0,double [][] D1, double [][]e,double [][] S,
                                              int W,int M , double [][] B) {
        S0 = product2Matrix(createNegativeMatrix(S, -1),createEAsVector(S.length));
        Q00 = D0;
        Q01 = D1;
        Q10 = kroneckerProduct(createE(W + 1),S0);
        double [][] S0B = product2Matrix(S0,B);
        Q0 = kroneckerProduct(createE(W + 1),S0B);
        Q1 = kronekerSum(D0,S);
        Q2 = kroneckerProduct(D1,createE(M));
    }

    public static double[][] product2Matrix(double [][] a, double [][] b) {
        if(a[0].length == b.length){
            double[][] Result = new double[a.length][b[0].length];
            for(int i = 0; i < a.length; i++) {
                for(int j = 0; j < b[0].length; j++) {
                    for(int k = 0; k < a.length; k++) {
                        Result[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
            return Result;
        }
        return null;
    }

    public static double[][] createE (int length) {
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

    public static double[][] createEWithScanner (Scanner in) {
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

    public static double [][] sum(double [][] a, double [][] b) {
        if(a.length == b.length && a[0].length == b[0].length){
            double[][] sum = new double[a.length][a[0].length];
            for(int i = 0; i < a.length; ++i) {
                for (int j = 0; j < a[0].length; ++j) {
                    sum[i][j] = a[i][j] + b[i][j];
                }
            }
        }
        return null;
    }

    public static double[][] kroneckerProduct(final double[][] a, final double[][] b) {
        final double[][] c = new double[a.length*b.length][];
        for (int ix = 0; ix < c.length; ix++) {
            final int num_cols = a[0].length*b[0].length;
            c[ix] = new double[num_cols];
        }
        for (int ia = 0; ia < a.length; ia++) {
            for (int ja = 0; ja < a[ia].length; ja++) {
                for (int ib = 0; ib < b.length; ib++) {
                    for (int jb = 0; jb < b[ib].length; jb++) {
                        c[b.length*ia+ib][b[ib].length*ja+jb] = a[ia][ja] * b[ib][jb];
                    }
                }
            }
        }
        return c;
    }

    public static void print_matrix(final double[][] m) {
        final String[][] sts = new String[m.length][];
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

    public static void main(final String[] args) {
        /*Scanner in = new Scanner(System.in);
        System.out.println("Введите матрицу D0 :");
        int [][] D0 = Kronecker.readMAtrix(in);
        System.out.println("Введите матрицу D1 :");
        int [][] D1 = Kronecker.readMAtrix(in);
        System.out.println("Введите матрицу S :");
        int [][] S = Kronecker.readMAtrix(in);
        System.out.println("Введите матрицу B :");
        int [][] B = Kronecker.readMAtrix(in);
        System.out.println("Введите размерность матрицы I :");
        int [][] e = Kronecker.createE(in);
        System.out.println("Введите число W :");
        int W = in.nextInt();
        System.out.println("Введите число M :");
        int M = in.nextInt();
        Kronecker.infinitesimalGenerator(D0,D1,e,S,W,M,B);
        print_matrix(D0);
        System.out.println();
        print_matrix(D1);
        System.out.println();
        print_matrix(e);
        System.out.println();*/
    }

}
