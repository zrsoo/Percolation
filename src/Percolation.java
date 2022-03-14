import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private final int n;
    private final int[][] system;
    private final OptimizedQuickUnion oqu;
    private int nrOpenSites;

    // creates n-by-n grid, with all sites blocked...
    public Percolation(int size)
    {
        if (size <= 0)
            throw new IllegalArgumentException("Size cannot be 0 or less...");

        n = size;
        system = new int[n + 1][n + 1];
        oqu = new OptimizedQuickUnion(n);
        nrOpenSites = 0;
    }

    // opens the site (row, col), if it is not open already...
    public void open(int row, int col)
    {
        if (row <= 0 || row > n || col <= 0 || col > n)
            throw new IllegalArgumentException("Open arguments out of bounds...");

        if (isOpen(row, col))
            return;

        system[row][col] = 1;

        // int site = twoDimToOneDim(row, col);
        int site = (row - 1) * n + col;
        int siteNeighbour;

        // Connect to open neighboring sites...
        if (row > 1)
            if (isOpen(row - 1, col)) {
                // siteNeighbour = twoDimToOneDim(row - 1, col);
                siteNeighbour = (row - 2) * n + col;
                oqu.union(site, siteNeighbour);
            }
        if (row < n)
            if (isOpen(row + 1, col)) {
                // siteNeighbour = twoDimToOneDim(row + 1, col);
                siteNeighbour = (row) * n + col;
                oqu.union(site, siteNeighbour);
            }
        if (col > 1)
            if (isOpen(row, col - 1)) {
                // siteNeighbour = twoDimToOneDim(row, col - 1);
                siteNeighbour = (row - 1) * n + col - 1;
                oqu.union(site, siteNeighbour);
            }
        if (col < n)
            if (isOpen(row, col + 1)) {
                // siteNeighbour = twoDimToOneDim(row, col + 1);
                siteNeighbour = (row - 1) * n + col + 1;
                oqu.union(site, siteNeighbour);
            }

        nrOpenSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (row <= 0 || row > n || col <= 0 || col > n)
            throw new IllegalArgumentException("isOpen arguments out of bounds...");

        return system[row][col] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if (row <= 0 || row > n || col <= 0 || col > n)
            throw new IllegalArgumentException("Open arguments out of bounds...");

        // For top sites...
        // If top site is closed, it is not full...
        if (!isOpen(row, col))
            return false;

        // If top site is open, it is full...

        if(row == 1) return true;

        // int site = twoDimToOneDim(row, col);
        int site = (row - 1) * n + col;
        int siteTop;

        // For each open site on the first row, check if site1 (current node)
        // is connected to it...
        for (int i = 1; i <= n; ++i)
        {
            // siteTop = twoDimToOneDim(1, i);
            siteTop = i;

            if (isOpen(1, i) && site != siteTop && oqu.connected(site, siteTop)) return true;
        }

        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return nrOpenSites;
    }

    // does the system percolate?
    public boolean percolates()
    {
        return oqu.connected(0, n * n + 1);
    }

    // test client
    public static void main(String[] args)
    {
        Percolation p = new Percolation(2);

        while (!p.percolates())
        {
            int randomRow = StdRandom.uniform(1, p.n + 1);
            int randomCol = StdRandom.uniform(1, p.n + 1);

            while (p.isOpen(randomRow, randomCol))
            {
                randomRow = StdRandom.uniform(1, p.n + 1);
                randomCol = StdRandom.uniform(1, p.n + 1);
            }

            p.open(randomRow, randomCol);
            p.nrOpenSites++;
        }

        //System.out.println(p.toPrint());
        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());
    }
}

class OptimizedQuickUnion
{
    private final int[] id;
    private final int[] sz;

    public OptimizedQuickUnion(int n)
    {
        int N = n * n;

        id = new int[N + 2];
        sz = new int[N + 2];

        for (int i = 0; i <= N + 1; ++i) {
            id[i] = i;
            sz[i] = 1;
        }

        // Connect top virtual node to all top nodes...
        for (int i = 1; i <= n; ++i)
            union(0, i);

        // Connect bottom virtual node to all bottom nodes...
        for (int i = N - n + 1; i <= N; ++i)
            union(N + 1, i);
    }

    private int root(int i)
    {
        while (id[i] != i)
        {
            id[i] = id[id[i]];
            i = id[i];
        }

        return i;
    }

    public boolean connected(int p, int q)
    {
        return root(p) == root(q);
    }

    public void union(int p, int q)
    {
        int i = root(p);
        int j = root(q);

        if (i == j) return;

        if (sz[i] < sz[j]) {
            id[i] = j;
            sz[j] += sz[i];
        }
        else {
            id[j] = i;
            sz[i] += sz[j];
        }
    }
}