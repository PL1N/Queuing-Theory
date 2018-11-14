import java.util.Scanner;

public class Kronecker {

    public static int[][] readMAtrix(Scanner in) {
        int n = in.nextInt();
        int [][] arr = new int[n][n];
        for (int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                arr[i][j] = in.nextInt();
        return arr;
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

    private static void test2(int [][] a, int [][] b, int [][] e) {
        int [][] firstArr = test(a, e);
        int [][] secondArr = test(b, e);
        int [][] result = kroneckerSum(firstArr,secondArr);
        System.out.println("Result : ");
        print_matrix(result);
    }

    public static void main(final String[] args) {
        Scanner in = new Scanner(System.in);
        int [][] a = Kronecker.readMAtrix(in);
        int [][] b = Kronecker.readMAtrix(in);
        int [][] e = Kronecker.createE(in);
        test2(a,b,e);
    }

}