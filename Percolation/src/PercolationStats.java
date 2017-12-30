import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;




public class PercolationStats {
    private static final double TIMES = 1.96;

    private  final double[] threshold; //  Xi
    private  final double mean;
    private  final double stddev;

    public PercolationStats(int n, int trials) {    //  perform trials independent experiments on an n-by-n grid
        if (n <= 0  || trials <= 0)
            throw new IllegalArgumentException("n ≤ 0 or trials ≤ 0");

        threshold = new double[trials];

        for (int i = 0; i < threshold.length; i++)
        {
            threshold[i] =  getMean(n);
        }

        mean =  StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);

    }
    public static void main(String[] args) {    //  test client (described below)
        int n = Integer.parseInt(args[0]);
        int tails = Integer.parseInt(args[1]);
        if (n <= 0 || tails <= 0)
            throw new IllegalArgumentException("any argurment is zero");

        PercolationStats stats = new PercolationStats(n, tails);
        System.out.println("mean                          = " + stats.mean());
        System.out.println("stddev                        = "+stats.stddev());
        System.out.println("95% confidence interval       = ["+stats.confidenceLo()+","+stats.confidenceHi()+"]");
    }


    private  double getMean(int curN) {         // get 1 mean from a curN-by-curN grid
        Percolation p = new Percolation(curN);
        while (!p.percolates())
        {
            int row;
            int col;
            do {
                row = StdRandom.uniform(1, curN+1);
                col = StdRandom.uniform(1, curN+1);

                // System.out.println("random row=" + row + "   col=" + col);
            } while (p.isOpen(row, col));
            // System.out.println("get "+row +" "+col +" is not open");
            p.open(row, col);
        }
         int vacancy = p.numberOfOpenSites();
         // System.out.println("use "+vacancy +" steps to be percolated .");
         // double probability = (double) vacancy/(double) (curN*curN);
        double probability = (double) vacancy/(double) (curN*curN);
        // System.out.println("probability:" + probability);
         return probability;
    }

    public double mean() {                 //  sample mean of percolation threshold

        return  mean;
    }
    public double stddev() {    //  sample standard deviation of percolation threshold
        return stddev;
    }

    public double confidenceHi()  { //  high endpoint of 95% confidence interval

        return mean + (TIMES*stddev)/Math.sqrt(threshold.length);
    }
    public double confidenceLo() {                 //  low  endpoint of 95% confidence interval

        return mean - (TIMES*stddev)/Math.sqrt(threshold.length);
    }
}
