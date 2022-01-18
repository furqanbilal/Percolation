import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    // multidimensional grid for marking sites as open or close
    private boolean[][] grid;
    // side of the mult.dim grid
    private final int  side;
    //no. of open sites
    private int count = 0;
    private final WeightedQuickUnionUF uf;
    public Percolation(int n) {
        //throw error for values <= 0
        if (n <= 0) {
         throw new java.lang.IllegalArgumentException("Index is not between specified range");
        }
        //set side
         side = n;
          grid = new boolean[n][n]; 
          
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                //keeping all sites initially close
                grid[i][j] = false;
            }
            
        }
        /* 
         * one dimensional grid for backing up the multidimensional grid
         * first extra site for linking all the sites of top row to it
         *  second extra site for linking all the sites of bottom row to it 
         */
        uf= new WeightedQuickUnionUF(n*n + 2);
        for (int k = 0; k < side; k++)
        {
            //top row
            uf.union(side*side, k);
            //bottom row
            uf.union(side*side + 1, side*side - (k + 1));
        }
    }
    //for converting site of multidimensional grid to site of single dimensional grid
    private int md_Sd(int a, int b) {
        //formulae for conversion
        int d = (a - 1) * side + b - 1;
        return d;
    }
    //for validating
    private void validate(int row, int col)
    {
        if ( row <= 0 || col <= 0 || row > side || col > side) {
         throw new java.lang.IllegalArgumentException("index is not between 0 and " + (side));
        }
    }
    //opening a new site
    public void open(int row, int col) {
                 validate(row,col);
        //if already open do nothing         
        if (isOpen(row, col))
            return;
        else {
            //converting rows and columns to 0-indexing
            int row1 = row - 1, col1 = col - 1;
            //opening the site
            grid[row1][col1] = true;
            //keeping track of total open sites
            count++;
            //getting index of single dimension grid
            int e = md_Sd(row, col);
            //connect to left site if open
            if (row - 1 > 0 && isOpen(row - 1, col)) {
                uf.union(e, md_Sd(row - 1, col));
            }
            //connect to upper site if open
            if (col - 1 > 0 && isOpen(row, col - 1)) {
                uf.union(e, md_Sd(row, col - 1));
            }
            //connect to right site if open
            if (row + 1 <= side && isOpen(row + 1, col)) {
                uf.union(e, md_Sd(row + 1, col));
            }
            //connect to bottom site if open 
            if (col + 1 <= side && isOpen(row, col + 1)) {
                uf.union(e, md_Sd(row, col + 1));
            }
        }
    }
    public int numberOfOpenSites() {
        return count;   
    }
    public boolean isOpen(int row, int col) {
        validate(row,col);
        //0-indexing
        return grid[row - 1][col - 1];
        
    }
    //checks weather a site is connected to upper row
        public boolean isFull(int row, int col) {
            validate(row,col);
            
            if(isOpen(row,col) && uf.connected(side*side, md_Sd(row,col)))
                return true;
        
        return false;
    }
        //checks for percolation
    public boolean percolates()
    {
        //exception for a single site grid
        if (side == 1 && !isOpen(1,1))
    
            return false;
                
        //if upper row is connected to bottom row return true
            if(uf.connected(side*side, side*side + 1))
                return true;
        
        return false;
    }
    
    //client testing
    public static void main(String[] args)
    {
        Percolation obj = new Percolation(5);
        StdOut.print(obj.isOpen(1, 1));
        StdOut.print(obj.isOpen(1, 2));
        StdOut.print(obj.isFull(1, 1));
        StdOut.print(obj.isFull(1, 2));
        
        obj.open(1, 1);
        obj.open(2,1);
        obj.open(3,1);
        obj.open(4,1);
        obj.open(4,2);
        obj.open(5,2);
        obj.open(3,2);
        StdOut.print(obj.numberOfOpenSites());
        StdOut.print(obj.isFull(3,2));
        obj.open(5,5);
        StdOut.print(obj.isFull(5,5));
        StdOut.print(obj.percolates());
    }
}

