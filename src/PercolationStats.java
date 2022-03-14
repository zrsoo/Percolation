import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{
    private final int trials;
    private final double[] stats;

    // perform independent trials on an n-by-n grid...
    public PercolationStats(int size, int tr)
    {
        if (size <= 0 || tr <= 0)
            throw new IllegalArgumentException("Invalid size or trials in PercolationStats...");

        trials = tr;

        // RUN SIMULATION
        stats = new double[trials];
        for (int i = 0; i < trials; ++i)
        {
            // stats[i] = percolationSystem.runSimulation();
            Percolation percolationSystem = new Percolation(size);
            while (!percolationSystem.percolates())
            {
                int randomRow = StdRandom.uniform(1, size + 1);
                int randomCol = StdRandom.uniform(1, size + 1);

                while (percolationSystem.isOpen(randomRow, randomCol))
                {
                    randomRow = StdRandom.uniform(1, size + 1);
                    randomCol = StdRandom.uniform(1, size + 1);
                }

                percolationSystem.open(randomRow, randomCol);
                // percolationSystem.nrOpenSites++;
            }

            stats[i] =  (double) percolationSystem.numberOfOpenSites() / (size * size);
        }
    }

    // sample mean of percolation threshold...
    public double mean()
    {
        return StdStats.mean(stats);
    }

    // sample standard deviation of percolation threshold...
    public double stddev()
    {
        return StdStats.stddev(stats);
    }

    // low endpoint of 95% confidence interval...
    public double confidenceLo()
    {
        double mean = mean();
        double deviation = stddev();

        return mean - 1.96 * deviation / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval...
    public double confidenceHi()
    {
        double mean = mean();
        double deviation = stddev();

        return mean + 1.96 * deviation / Math.sqrt(trials);
    }

    public static void main(String[] args)
    {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(x, y);

        System.out.println("mean =                    " + ps.mean() +
                        "\nstddev =                  " + ps.stddev() +
                        "\n95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
