import java.util.Scanner;

public class Kronecker {

    private static int[][] S0;
    private static int[][] Q00;
    private static int[][] Q01;
    private static int[][] Q10;
    private static int[][] Q0;
    private static int[][] Q1;
    private static int[][] Q2;

    public static int[][] readMAtrix(Scanner in) {
        int n = in.nextInt();
        int [][] arr = new int[n][n];
        for (int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                arr[i][j] = in.nextInt();
        return arr;
    }

    public static int[][] createQ1(int [][] D0,int [][] S) {
        int [][] e1 = createEWithOutScanner(D0.length);
        int [][] e2 = createEWithOutScanner(S.length);
        int [][] result = kroneckerSum(product(D0,e1) , product(e2,S));
        return result;
    }

    public static void printAllMatrix() {
        //print_matrix();
    }

    public static int[][] createNegativeMatrix (int [][] matrix) {
        for (int i = 0; i < matrix.length; ++i)
            for(int j = 0; j < matrix[0].length; ++j)
                matrix[i][j] = - matrix[i][j];
        return matrix;
    }

    public static int[][] createEAsVector(int [][] S) {
        int [][] result = new int[1][S.length];
        for (int i = 0 ; i < result.length; ++i) {
            result[0][i] = 1;
        }
        return result;
    }

    public static void infinitesimalGenerator(int [][] D0,int [][] D1, int [][]e,int [][] S,
    int W,int M , int [][] B) {
        S0 = product2Matix(createNegativeMatrix(S),createEAsVector(S));
        Q00 = D0;
        Q01 = D1;
        Q10 = product(createEWithOutScanner(W + 1),S0);
        int [][] S0B = product2Matix(S0,B);
        Q0 = product(createEWithOutScanner(W + 1),S0B);
        Q1 = createQ1(D0,S);
        Q2 = product(D1,createEWithOutScanner(M));
    }

    public static int[][] product2Matix(int [][] a, int [][] b) {
        int aColLength = a[0].length;
        int bRowLength = b.length;
        if(aColLength != bRowLength) return null;
        int RRowLength = a.length;
        int RColLength = b[0].length;
        int[][] Result = new int[RRowLength][RColLength];
        for(int i = 0; i < RRowLength; i++) {
            for(int j = 0; j < RColLength; j++) {
                for(int k = 0; k < aColLength; k++) {
                    Result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return Result;
    }

    public static int[][] createEWithOutScanner (int length) {
        int k = length;
        int[][] e = new int[k][k];
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


    public static int[][] createE (Scanner in) {
        int k = in.nextInt();
        int[][] e = new int[k][k];
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

    public static int [][] kroneckerSum(int [][] a, int [][] b) {
            int rows = a.length;
            int columns = a[0].length;
            int[][] sum = new int[rows][columns];
            for(int i = 0; i < rows; ++i) {
                for (int j = 0; j < columns; ++j) {
                    sum[i][j] = a[i][j] + b[i][j];
                }
            }
            return sum;
    }

    public static int[][] product(final int[][] a, final int[][] b) {
        final int[][] c = new int[a.length*b.length][];
        for (int ix = 0; ix < c.length; ix++) {
            final int num_cols = a[0].length*b[0].length;
            c[ix] = new int[num_cols];
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

    public static void print_matrix(final int[][] m) {
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


    private static int[][] test(final int[][] a, final int[][] b) {
        System.out.println("Testing Kronecker product");
        System.out.println("Size of matrix a: " + a.length + " by " + a[0].length);
        System.out.println("Matrix a:");
        print_matrix(a);
        System.out.println("Size of matrix b: " + b.length + " by " + b[0].length);
        System.out.println("Matrix b:");
        print_matrix(b);
        System.out.println("Calculating matrix c as Kronecker product");
        final int[][] c = product(a, b);
        System.out.println("Size of matrix c: " + c.length + " by " + c[0].length);
        System.out.println("Matrix c:");
        print_matrix(c);
        return c;
    }

    private static void test1() {
        final int[][] a = new int[2][];
        a[0] = new int[]{1, 2};
        a[1] = new int[]{3, 4};
        final int[][] b = new int[2][];
        b[0] = new int[]{0, 5};
        b[1] = new int[]{6, 7};
        test(a, b);
    }

    private static int[][] test2(int [][] a, int [][] b, int [][] e) {
        int [][] firstArr = test(a, e);
        int [][] secondArr = test(b, e);
        int [][] result = kroneckerSum(firstArr,secondArr);
        System.out.println("Result : ");
        print_matrix(result);
        return result;
    }

    public static void main(final String[] args) {
        Scanner in = new Scanner(System.in);
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
        System.out.println();



    }

}