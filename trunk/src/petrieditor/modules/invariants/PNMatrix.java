package petrieditor.modules.invariants;
/**
 * This support class provides many general matrix manipulation functions, as well as
 * a number of specialised matrix operations pertaining to Petri net analysis.
 * @author Manos Papantoniou & Michael Camacho
 * @version February 2004
 *
 * Based on the Jama Matrix class, the PNMatrix class offers a small subset
 * of the operations, and is used for matrices of integers only, as required
 * by the petri net analyser project.
 *
 * <P>
 * This Class provides the fundamental operations of numerical
 * linear algebra.  Various constructors create Matrices from two dimensional
 * arrays of integer numbers.  Various "gets" and
 * "sets" provide access to submatrices and matrix elements.  Several methods
 * implement basic matrix arithmetic, including matrix addition and
 * multiplication, and element-by-element array operations.
 * Methods for reading and printing matrices are also included.
 * <P>
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class PNMatrix {
    
    /**
     * Array for internal storage of elements.
     * @serial internal array storage.
     */
    private int[][] A;
    
    /**
     * Row and column dimensions.
     * @serial row dimension.
     * @serial column dimension.
     */
    private int m, n;
    
    /**
     * Construct an m-by-n matrix of zeros.
     * @param m    Number of rows.
     * @param n    Number of colums.
     */
    public PNMatrix(int m, int n) {
        this.m = m;
        this.n = n;
        A = new int[m][n];
    }
    
    /**
     * Construct an m-by-n constant matrix.
     * @param m Number of rows.
     * @param n Number of colums.
     * @param s Fill the matrix with this scalar value.
     */
    public PNMatrix(int m, int n, int s) {
        this.m = m;
        this.n = n;
        A = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = s;
            }
        }
    }
    
    /**
     * Construct a matrix from a 2-D array.
     * @param A Two-dimensional array of integers.
     * @exception IllegalArgumentException All rows must have the same length
     * @see #constructWithCopy
     */
    public PNMatrix(int[][] A) {
        if(A != null) {
            m = A.length;
            if(A.length >= 1) {
                n = A[0].length;
                for (int i = 0; i < m; i++) {
                    if (A[i].length != n) {
                        throw new IllegalArgumentException("All rows must have the same length.");
                    }
                }
                this.A = A;
            }
        }
    }
    
    /**
     * Construct a matrix quickly without checking arguments.
     * @param A Two-dimensional array of integers.
     * @param m Number of rows.
     * @param n Number of colums.
     */
    public PNMatrix(int[][] A, int m, int n) {
        this.A = A;
        this.m = m;
        this.n = n;
    }
    
    /**
     * Construct a matrix from a one-dimensional packed array
     * @param vals One-dimensional array of integers, packed by columns (ala Fortran).
     * @param m Number of rows.
     * @exception IllegalArgumentException Array length must be a multiple of m.
     */
    public PNMatrix(int vals[], int m) {
        this.m = m;
        n = (m != 0 ? vals.length/m : 0);
        if (m*n != vals.length) {
            throw new IllegalArgumentException("Array length must be a multiple of m.");
        }
        A = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = vals[i+j*m];
            }
        }
    }
    
    public PNMatrix mul( PNMatrix a, PNMatrix b ) {
        if( a.getColumnDimension() != b.getRowDimension() ) {
            System.out.println( " zle wymiary ");
            return null;
        }
        PNMatrix res = new PNMatrix( a.getRowDimension(), b.getColumnDimension());
        for( int i=0; i<a.getRowDimension(); i++ )
            for( int j=0; j<b.getColumnDimension(); j++ )
                for( int k=0; k<a.getColumnDimension(); k++ )
                    res.set( i, j, res.get(i,j) + a.get(i,k)*b.get(k,j) );
        return res;
    }
    
    public int[] getRow(int r) {
        return this.A[r];
    }
    
    public void setRow(int r, int row[]) {
        for( int i=0; i<getColumnDimension(); i++)
            A[r][i] = row[i];
    }
    
    public int[] getRowCopy(int r) {
        int row[] = new int[getColumnDimension()];
        for( int i=0; i<getColumnDimension();i++ )
            row[i] = A[r][i];
        return row;
    }
    
    /**
     * Construct a matrix from a copy of a 2-D array.
     * @param A Two-dimensional array of integers.
     * @return The copied matrix.
     * @exception IllegalArgumentException All rows must have the same length
     */
    public static PNMatrix constructWithCopy(int[][] A) {
        int m = A.length;
        int n = A[0].length;
        PNMatrix X = new PNMatrix(m,n);
        int[][] C = X.getArray();
        for (int i = 0; i < m; i++) {
            if (A[i].length != n) {
                throw new IllegalArgumentException
                        ("All rows must have the same length.");
            }
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j];
            }
        }
        return X;
    }
    
    /**
     * Make a deep copy of a matrix
     * @return The matrix copy.
     */
    public PNMatrix copy() {
        PNMatrix X = new PNMatrix(m,n);
        int[][] C = X.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j];
            }
        }
        return X;
    }
    
    /**
     * Clone the IntMatrix object.
     * @return  The clone of the current matrix.
     */
    public Object clone() {
        return this.copy();
    }
    
    /**
     * Eliminate a column from the matrix, column index is toDelete
     * @param  toDelete  The column number to delete.
     * @return The matrix with the required row deleted.
     */
    public PNMatrix eliminateCol(int toDelete) {
        int m = getRowDimension(), n = getColumnDimension();
        PNMatrix reduced = new PNMatrix(m, n);
        int [] cols = new int [n-1]; // array of cols which will not be eliminated
        int count = 0;
        
        // find the col numbers which will not be eliminated
        for (int i = 0; i < n; i++) {
            // if an index will not be eliminated, keep it in the new array cols
            if (i != toDelete)
                cols[count++] = i;
        }
        // System.out.print("Eliminating column " + toDelete + " from matrix below... keeping columns ");
        // printArray(cols);
        // print(2, 0);
        //  System.out.println("Reduced matrix");
        reduced = getMatrix(0, m-1, cols);
        //  reduced.print(2, 0);
        
        return reduced;
    }
    
    public PNMatrix eliminateRow(int row) {
        int m = getRowDimension(), n = getColumnDimension();
        int [] rows = new int [m-1]; // array of cols which will not be eliminated
        int count = 0;
        
        for (int i = 0; i < m; i++) {
            if (i != row)
                rows[count++] = i;
        }
        
        PNMatrix reduced = getMatrix(rows, 0, n-1);
        return reduced;
    }
    
    
    /**
     * Access the internal two-dimensional array.
     * @return Pointer to the two-dimensional array of matrix elements.
     */
    public int[][] getArray() {
        return A;
    }
    
    /**
     * Copy the internal two-dimensional array.
     * @return Two-dimensional array copy of matrix elements.
     */
    public int[][] getArrayCopy() {
        int[][] C = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j];
            }
        }
        return C;
    }
    
    /**
     * Make a one-dimensional row packed copy of the internal array.
     * @return Matrix elements packed in a one-dimensional array by rows.
     */
    public int[] getRowPackedCopy() {
        int[] vals = new int[m*n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                vals[i*n+j] = A[i][j];
            }
        }
        return vals;
    }
    
    /**
     * Get row dimension.
     * @return The number of rows.
     */
    public int getRowDimension() {
        return m;
    }
    
    /**
     * Get column dimension.
     * @return The number of columns.
     */
    public int getColumnDimension() {
        return n;
    }
    
    /**
     * Find the first non-zero row of a matrix.
     * @return Row index (starting from 0 for 1st row) of the first row from top that is not only zeros, -1 of there is no such row.
     */
    public int firstNonZeroRowIndex() {
        int m = getRowDimension(), n = getColumnDimension();
        int h = -1;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (get(i, j) != 0)
                    return i;
            }
        }
        return h;
    }
    
    /**
     * Form a matrix with columns the row indices of non-zero elements.
     * @return The matrix with columns the row indices of non-zero elements. First row has index 1.
     */
    public PNMatrix nonZeroIndices() {
        PNMatrix X = new PNMatrix(m, n);
        
        for (int i = 0; i < m; i++){
            for (int j = 0; j < n; j++){
                if ( get(i, j) == 0 )
                    X.set(i, j, 0);
                else X.set(i, j, i+1);
            }
        }
        
        return X;
    }
    
    /**
     * Find if a column vector has negative elements.
     * @return True or false.
     */
    public boolean hasNegativeElements() {
        boolean hasNegative = false;
        int m = getRowDimension();
        
        for (int i = 0; i < m ; i++)
            if (get(i, 0) < 0)
                return true;
        
        return hasNegative;
    }
    
    /**
     * Find the column index of the first non zero element of row h.
     * @param h The row to look for the non-zero element in
     * @return Column index (starting from 0 for 1st column) of the first non-zero element of row h, -1 if there is no such column.
     */
    public int firstNonZeroElementIndex(int h) {
        int n = getColumnDimension();
        int k = -1;
        
        for (int j = 0; j < n; j++) {
            if (get(h, j) != 0)
                return j;
        }
        return k;
    }
    
    /**
     * Find the column indices of all but the first non zero elements of row h.
     * @param h The row to look for the non-zero element in
     * @return Array of ints of column indices (starting from 0 for 1st column) of all but the first non-zero elements of row h.
     */
    public int[] findRemainingNZIndices(int h) {
        int n = getColumnDimension();
        int[] k = new int[n];
        int count = 0; // increases as we add new indices in the array of ints
        
        for (int j = 1; j < n; j++) {
            if (get(h, j) != 0)
                k[count++] = j;
        }
        return k;
    }
    
    /**
     * Find the coefficients corresponding to column indices of all but the first non zero elements of row h.
     * @param h The row to look for the non-zero coefficients in
     * @return Array of ints of coefficients of all but the first non-zero elements of row h.
     */
    public int[] findRemainingNZCoef(int h) {
        int n = getColumnDimension();
        int[] k = new int[n];
        int count = 0; // increases as we add new indices in the array of ints
        int anElement; // an element of the matrix
        
        for (int j = 1; j < n; j++) {
            if ((anElement = get(h, j)) != 0)
                k[count++] = anElement;
        }
        return k;
    }
    
    /**
     * Get a single element.
     * @param i Row index.
     * @param j Column index.
     * @return A(i,j)
     * @exception ArrayIndexOutOfBoundsException
     */
    public int get(int i, int j) {
        return A[i][j];
    }
    
    /** Get a submatrix.
     * @param i0 Initial row index
     * @param i1 Final row index
     * @param j0 Initial column index
     * @param j1 Final column index
     * @return A(i0:i1,j0:j1)
     * @exception ArrayIndexOutOfBoundsException Submatrix indices
     */
    public PNMatrix getMatrix(int i0, int i1, int j0, int j1) {
        PNMatrix X = new PNMatrix(i1-i0+1,j1-j0+1);
        int[][] B = X.getArray();
        try {
            for (int i = i0; i <= i1; i++) {
                for (int j = j0; j <= j1; j++) {
                    B[i-i0][j-j0] = A[i][j];
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }
    
    /**
     * Get a submatrix.
     * @param r Array of row indices.
     * @param c Array of column indices.
     * @return A(r(:),c(:))
     * @exception ArrayIndexOutOfBoundsException Submatrix indices
     */
    public PNMatrix getMatrix(int[] r, int[] c) {
        PNMatrix X = new PNMatrix(r.length,c.length);
        int[][] B = X.getArray();
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = 0; j < c.length; j++) {
                    B[i][j] = A[r[i]][c[j]];
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }
    
    /**
     * Get a submatrix.
     * @param i0 Initial row index
     * @param i1 Final row index
     * @param c Array of column indices.
     * @return A(i0:i1,c(:))
     * @exception ArrayIndexOutOfBoundsException Submatrix indices
     */
    public PNMatrix getMatrix(int i0, int i1, int[] c) {
        PNMatrix X = new PNMatrix(i1-i0+1,c.length);
        int[][] B = X.getArray();
        try {
            for (int i = i0; i <= i1; i++) {
                for (int j = 0; j < c.length; j++) {
                    B[i-i0][j] = A[i][c[j]];
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }
    
    /**
     * Get a submatrix.
     * @param r Array of row indices.
     * @param j0 Initial column index
     * @param j1 Final column index
     * @return A(r(:),j0:j1)
     * @exception ArrayIndexOutOfBoundsException Submatrix indices
     */
    
    public PNMatrix getMatrix(int[] r, int j0, int j1) {
        PNMatrix X = new PNMatrix(r.length,j1-j0+1);
        int[][] B = X.getArray();
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = j0; j <= j1; j++) {
                    B[i][j-j0] = A[r[i]][j];
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }
    
    /**
     * For row rowNo of the matrix received return the column indices of all the negative elements
     * @param rowNo row iside the Matrix to check for -ve elements
     * @return Integer array of indices of negative elements.
     * @exception ArrayIndexOutOfBoundsException Submatrix indices
     */
    public int[] getNegativeIndices(int rowNo) {
        int n = getColumnDimension(); // find the number of columns
        
        // create the single row submatrix for the required row
        try {
            PNMatrix A = new PNMatrix(1, n);
            A = getMatrix(rowNo, rowNo, 0, n-1);
            
            int count = 0; // index of a negative element in the returned array
            int[] negativesArray = new int[n];
            for (int i = 0; i < n; i++) // initialise to zero
                negativesArray[i] = 0;
            
            for (int i = 0; i < n; i++) {
                if (A.get(0, i) < 0)
                    negativesArray[count++] = i + 1;
            }
            
            return negativesArray;
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }
    
    /**
     * For row rowNo of the matrix received return the column indices of all the positive elements
     * @param rowNo row iside the Matrix to check for +ve elements
     * @return The integer array of indices of all positive elements.
     * @exception ArrayIndexOutOfBoundsException Submatrix indices
     */
    public int[] getPositiveIndices(int rowNo) {
        int n = getColumnDimension(); // find the number of columns
        
        // create the single row submatrix for the required row
        try {
            PNMatrix A = new PNMatrix(1, n);
            A = getMatrix(rowNo, rowNo, 0, n-1);
            
            int count = 0; // index of a positive element in the returned array
            int[] positivesArray = new int[n];
            for (int i = 0; i < n; i++) // initialise to zero
                positivesArray[i] = 0;
            
            for (int i = 0; i < n; i++) {
                if (A.get(0, i) > 0)
                    positivesArray[count++] = i + 1;
            }
            
            return positivesArray;
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }
    
    /**
     * Check if a matrix is all zeros.
     * @return true if all zeros, false otherwise
     */
    public boolean isZeroMatrix() {
        int m = getRowDimension(), n = getColumnDimension();
        boolean isZero = true;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (get(i, j) != 0)
                    return false;
            }
        }
        return isZero;
    }
    
    /**
     * isZeroRow returns true if the ith row is all zeros
     * @param r row to check for full zeros.
     * @return true if the row is full of zeros.
     */
    public boolean isZeroRow(int r) {
        PNMatrix A = new PNMatrix(1, getColumnDimension());
        A = getMatrix(r, r, 0, getColumnDimension()-1);
        return A.isZeroMatrix();
    }
    
    /**
     * Find if a matrix of invariants is covered.
     * @return  true if it is covered, false otherwise.
     */
    public boolean isCovered(){
        boolean isCovered = true;
        // if there is an all-zeros row then it is not covered
        for (int i = 0; i < m; i++){
            if (isZeroRow(i) || this.transpose().hasNegativeElements())
                return false;
        }
        return isCovered;
    }
    
    /**
     * Find the first row with a negative element in a matrix.
     * @return     Row index (starting from 0 for 1st row) of the first row from top that is has a negative element, -1 of there is no such row.
     */
    public int rowWithNegativeElement() {
        int m = getRowDimension(), n = getColumnDimension();
        int h = -1;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (get(i, j) < 0)
                    return i;
            }
        }
        return h;
    }
    
    /**
     * Set a single element.
     * @param i    Row index.
     * @param j    Column index.
     * @param s    A(i,j).
     * @exception  ArrayIndexOutOfBoundsException
     */
    public void set(int i, int j, int s) {
        A[i][j] = s;
    }
    
    /**
     * Set a submatrix.
     * @param i0   Initial row index
     * @param i1   Final row index
     * @param j0   Initial column index
     * @param j1   Final column index
     * @param X    A(i0:i1,j0:j1)
     * @exception  ArrayIndexOutOfBoundsException Submatrix indices
     */
    public void setMatrix(int i0, int i1, int j0, int j1, PNMatrix X) {
        try {
            for (int i = i0; i <= i1; i++) {
                for (int j = j0; j <= j1; j++) {
                    A[i][j] = X.get(i-i0,j-j0);
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }
    
    /**
     * Set a submatrix.
     * @param r    Array of row indices.
     * @param c    Array of column indices.
     * @param X    A(r(:),c(:))
     * @exception  ArrayIndexOutOfBoundsException Submatrix indices
     */
    public void setMatrix(int[] r, int[] c, PNMatrix X) {
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = 0; j < c.length; j++) {
                    A[r[i]][c[j]] = X.get(i,j);
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }
    
    /**
     * Set a submatrix.
     * @param r    Array of row indices.
     * @param j0   Initial column index
     * @param j1   Final column index
     * @param X    A(r(:),j0:j1)
     * @exception  ArrayIndexOutOfBoundsException Submatrix indices
     */
    public void setMatrix(int[] r, int j0, int j1, PNMatrix X) {
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = j0; j <= j1; j++) {
                    A[r[i]][j] = X.get(i,j-j0);
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }
    
    /**
     * Set a submatrix.
     * @param i0 Initial row index
     * @param i1 Final row index
     * @param c Array of column indices.
     * @param X A(i0:i1,c(:))
     * @exception ArrayIndexOutOfBoundsException Submatrix indices
     */
    public void setMatrix(int i0, int i1, int[] c, PNMatrix X) {
        try {
            for (int i = i0; i <= i1; i++) {
                for (int j = 0; j < c.length; j++) {
                    A[i][c[j]] = X.get(i-i0,j);
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }
    
    /**
     * Matrix transpose.
     * @return    A'
     */
    public PNMatrix transpose() {
        PNMatrix X = new PNMatrix(n,m);
        int[][] C = X.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[j][i] = A[i][j];
            }
        }
        return X;
    }
    
    /**
     * Unary minus
     * @return - A
     */
    public PNMatrix uminus() {
        PNMatrix X = new PNMatrix(m,n);
        int[][] C = X.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = -A[i][j];
            }
        }
        return X;
    }
    
    /**
     * C = A + B
     * @param B another matrix
     * @return A + B
     */
    public PNMatrix plus(PNMatrix B) {
        checkMatrixDimensions(B);
        PNMatrix X = new PNMatrix(m,n);
        int[][] C = X.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B.A[i][j];
            }
        }
        return X;
    }
    
    /**
     * A = A + B
     * @param B another matrix
     * @return A + B
     */
    public PNMatrix plusEquals(PNMatrix B) {
        checkMatrixDimensions(B);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = A[i][j] + B.A[i][j];
            }
        }
        return this;
    }
    
    /**
     * C = A - B
     * @param B another matrix
     * @return A - B
     */
    public PNMatrix minus(PNMatrix B) {
        checkMatrixDimensions(B);
        int[][] C = new int[m][n]; //= X.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B.A[i][j];
            }
        }
        PNMatrix X = new PNMatrix(C);
        return X;
    }
    
    /**
     * A = A - B
     * @param B another matrix
     * @return A - B
     */
    public PNMatrix minusEquals(PNMatrix B) {
        checkMatrixDimensions(B);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = A[i][j] - B.A[i][j];
            }
        }
        return this;
    }
    
    /**
     * Multiply a matrix by an int in place, A = s*A
     * @param s int multiplier
     * @return replace A by s*A
     */
    public PNMatrix timesEquals(int s) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = s*A[i][j];
            }
        }
        return this;
    }
    
    /**
     * Multiply a row matrix by a column matrix, A = s*A
     * @param B column vector
     * @return product of row vector A by column vector B
     */
    public int vectorTimes(PNMatrix B) {
        int product = 0;
        
        for (int j = 0; j < n; j++) {
            product += A[0][j] * B.get(j, 0);
        }
        
        return product;
    }
    
    /**
     * Divide a matrix by an int in place, A = s*A
     * @param s int divisor
     * @return replace A by A/s
     */
    public PNMatrix divideEquals(int s) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = A[i][j]/s;
            }
        }
        return this;
    }
    
    /**
     * Generate identity matrix]
     * @param m Number of rows.
     * @param n Number of colums.
     * @return An m-by-n matrix with ones on the diagonal and zeros elsewhere.
     */
    public static PNMatrix identity(int m, int n) {
        PNMatrix A = new PNMatrix(m,n);
        
        int[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = (i == j ? 1 : 0);
            }
        }
        return A;
    }
    
    /**
     * Print the matrix to stdout.   Line the elements up in columns
     * with a Fortran-like 'Fw.d' style format.
     * @param w    Column width.
     * @param d    Number of digits after the decimal.
     */
    public void print(int w, int d) {
        print(new PrintWriter(System.out,true),w,d);
    }
    
    /**
     * Print the matrix to a string.   Line the elements up in columns
     * with a Fortran-like 'Fw.d' style format.
     * @param w    Column width.
     * @param d    Number of digits after the decimal.
     * @return     The formated string to output.
     */
    public String printString(int w, int d) {
        if (isZeroMatrix())
            return "\nNone\n\n";
        
        ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
        
        print(new PrintWriter(arrayStream,true),w,d);
        
        String output = arrayStream.toString();
        
        return output;
    }
    
    /**
     * Print the matrix to the output stream.   Line the elements up in
     * columns with a Fortran-like 'Fw.d' style format.
     * @param output Output stream.
     * @param w Column width.
     * @param d Number of digits after the decimal.
     */
    public void print(PrintWriter output, int w, int d) {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.UK));
        format.setMinimumIntegerDigits(1);
        format.setMaximumFractionDigits(d);
        format.setMinimumFractionDigits(d);
        format.setGroupingUsed(false);
        print(output,format,w+2);
    }
    
    /**
     * Print the matrix to stdout.  Line the elements up in columns.
     * Use the format object, and right justify within columns of width
     * characters.
     * Note that if the matrix is to be read back in, you probably will want
     * to use a NumberFormat that is set to UK Locale.
     * @param format A Formatting object for individual elements.
     * @param width Field width for each column.
     * @see java.text.DecimalFormat#setDecimalFormatSymbols
     */
    public void print(NumberFormat format, int width) {
        print(new PrintWriter(System.out,true),format,width); }
    
    // DecimalFormat is a little disappointing coming from Fortran or C's printf.
    // Since it doesn't pad on the left, the elements will come out different
    // widths.  Consequently, we'll pass the desired column width in as an
    // argument and do the extra padding ourselves.
    
    /**
     * Print the matrix to the output stream.  Line the elements up in columns.
     * Use the format object, and right justify within columns of width
     * characters.
     * Note that is the matrix is to be read back in, you probably will want
     * to use a NumberFormat that is set to US Locale.
     * @param output the output stream.
     * @param format A formatting object to format the matrix elements
     * @param width Column width.
     * @see java.text.DecimalFormat#setDecimalFormatSymbols
     */
    public void print(PrintWriter output, NumberFormat format, int width) {
        output.println();  // start on new line.
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                String s = format.format(A[i][j]); // format the number
                int padding = Math.max(1,width-s.length()); // At _least_ 1 space
                for (int k = 0; k < padding; k++)
                    output.print(' ');
                output.print(s);
            }
            output.println();
        }
        output.println();   // end with blank line.
    }
    
    /**
     * Throws IllegalArgumentException if dimensions of A and B differ.
     *  @param   B   The matrix to check the dimensions.
     */
    private void checkMatrixDimensions(PNMatrix B) {
        if (B.m != m || B.n != n) {
            throw new IllegalArgumentException("Matrix dimensions must agree.");
        }
    }
    
    /**
     * Used to display intermediate results for checking
     * @param a  The array of integers to print.
     */
    public void printArray(int[] a){
        int n = a.length;
    } 
}
