

public class Percolation  {
    private final WQUN fullWqun;
    private final WQUN topWqun;
    private  final int n;

    public Percolation(int n)  {           //  create n-by-n grid,   with all sites blocke
        if (n <= 0 )
            throw new java.lang.IllegalArgumentException("grid size should be bigger than one !");
        this.n = n;
        fullWqun = new WQUN(n, false);
        topWqun = new WQUN(n, true);

    }
    public static void main(String[] args)  {   //  test client (optional)

        Percolation p = new Percolation(3);
        p.open(1,  1);
        p.open(2,  2);
        p.open(3,  3);
        p.open(4,  4);
    }


    public void open(int row,   int col)  {     //  open site (row,   col) if it is not open already
        validate(row, col);
        fullWqun.open(row, col);
        topWqun.open(row, col);


    }
    public boolean isOpen(int row,   int col)  {   //  is site (row,   col) open?Boolean a =false;

        validate(row, col);
        return fullWqun.isOpen(row, col);
    }  
    public boolean isFull(int row,   int col)  {  //  is site (row,   col) full?  a open site can connected to top open site .
        validate(row, col);
        return topWqun.isFull(row, col);
    }  
    public int numberOfOpenSites()  {        //  number of open sites
        return fullWqun.numberOfOpenSites();
    }  
    public boolean percolates() { //  does the system percolate? equivalent connected

      return fullWqun.percolates();
    }
    private void validate(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n)
            throw new IllegalArgumentException("input row or col is not illegal!");
    }
    private class WQUN{
        private  final int n;
        private final int allRow;
        private  int[] id;

        private  boolean[] openArry;
        private  int count = 0;
        private  int[] sz;

        public WQUN(int n, boolean withoutBackwash)  {           //  create n-by-n grid,   with all sites blocke
            if (n <= 0)
                throw new java.lang.IllegalArgumentException();
            this.n = n;
            allRow = withoutBackwash?n-1:n;

            id = new int[(allRow+2)*n];
            openArry = new boolean[(allRow+2)*n];
            sz = new int[(allRow+2)*n];


            for (int i = 0;  i < id.length;  i++) {
                id[i] = i;
                openArry[i] = false;
                sz[i] = 1;
            }
            for (int col = 1; col <= n; col++) {
                union(0, col,0, n);
                int bIndex = getIndex(0, col);
                openArry[bIndex] = true;

                if (!withoutBackwash) {
                    union(n + 1, col, n + 1, n);
                    int eIndex = getIndex(n + 1, col);
                    openArry[eIndex] = true;
                }

            }
        }

        private int getIndex(int row,  int col)
        {
            return row* n +col-1;
        }
        private void union(int p,   int q)  {
            // System.out.println("union "+ p +" "+q);
            int root1 = root(p);
            int root2 = root(q);
            if (root1 == root2) {  // important ,   don't union twice.
                return; }
            if (sz[root1] <= sz[root2])  {
                id[root1] = root2;
                sz[root2] += sz[root1];
            }  // weight tracing tree.
            else  {
                id[root2] = root1;
                sz[root1] += sz[root2];
            }
        }
        private void union(int row1,  int col1,  int row2,  int col2) {

            // System.out.println("row1="+row1 +"   col1="+col1+"   row2="+row2 +"   col2="+col2);

            int pIndex = getIndex(row1,  col1);
            int qIndex = getIndex(row2,  col2);
            union(pIndex,  qIndex);
        }

        private  boolean connected(int pIndex,  int qIndex) {
            return root(pIndex) == root(qIndex);
        }

        private int root(int postion) {
            while (postion != id[postion]) {
                // System.out.println("root : postion="+postion + "   id[postion]=" + id[postion]);
                id[postion] = id[id[postion]];   // path compression
                postion = id[postion];
            }
            return postion;
        }

        public void open(int row,   int col)  {     //  open site (row,   col) if it is not open already
            if (row <= 0 || n < row || col <= 0 || n < col) {
                throw new java.lang.IllegalArgumentException();
            }

            if (!isOpen(row, col)) {
                count++;
            }

            // System.out.println("run steps:" + count);
            // System.out.println("open " + row + " " + col);
            int index = getIndex(row, col);
            openArry[index] = true;
            if ((col != 1) && isOpenInternal(row,   col - 1))  { // left
                // System.out.println("union left");
                union(row,   col,   row,   col - 1);
            }

            if ((col != n) && isOpenInternal(row,   col + 1))  { // right
                // System.out.println("run union right");
                union(row,   col,   row,   col + 1);
            }
//        if ((0 != row) && isOpenInternal(row - 1,   col))  { // 内部
            if ( isOpenInternal(row - 1,   col))  { // 内部
                // System.out.println("run union top");
                union(row,   col,   row - 1,   col);
            }

            //if (((n+1) != row) && isOpenInternal(row + 1,   col))  { // 内部
            if (((allRow+1) != row) && isOpenInternal(row + 1,   col))  { // 内部

                // System.out.println("run union bottom");
                union(row,   col,   row + 1,   col);
            }
        }
        private  boolean isOpenInternal(int row, int col) {
            int num = getIndex(row,  col);
            return openArry[num];
        }

        public boolean isOpen(int row,   int col)  {   //  is site (row,   col) open?Boolean a =false;
            if (row <= 0 || n < row || col <= 0 || n < col) {
                throw new java.lang.IllegalArgumentException();
            }
            return isOpenInternal(row, col);
        }
        public boolean isFull(int row,   int col)  {  //  is site (row,   col) full?  a open site can connected to top open site .
            if (row <= 0 || n < row || col <= 0 || n < col) {
                throw new java.lang.IllegalArgumentException();
            }
            int qIndex = getIndex(row,  col);

            return connected(0,  qIndex);
        }
        public int numberOfOpenSites()  {        //  number of open sites
            return count;
        }
        public boolean percolates() { //  does the system percolate? equivalent connected

            return connected(0,   id.length-1);
        }

    }

}
