/******************************************************************************
 *  Compilation:  javac SeamCarver.java
 *  Execution:    java SeamCarver
 *  Dependencies: 
 *  
 *  
 *  For use on Coursera, Algorithms Part II programming assignment.
 *
 ******************************************************************************/
// import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }
    // current picture
    public Picture picture() {
        return new Picture(this.picture);
    }
    // width of current picture
    public int width() {
        return this.picture.width();
    }
    // height of current picture
    public int height() {
        return this.picture.height();
    }
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException();
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return 1000;

        return Math.sqrt(gradient(picture.getRGB(x-1, y), picture.getRGB(x+1, y)) 
                + gradient(picture.getRGB(x, y-1), picture.getRGB(x, y+1)));
    }

    private double gradientColor(Color rgb0, Color rgb1) {
        double r = Math.pow(rgb0.getRed() - rgb1.getRed(), 2);
        double g = Math.pow(rgb0.getGreen() - rgb1.getGreen(), 2);
        double b = Math.pow(rgb0.getBlue() - rgb1.getBlue(), 2);
        return r + g + b;
    }
    private double gradient(int rgb0, int rgb1) {
        double r = Math.pow((((rgb0 >> 16) & 0xFF) - ((rgb1 >> 16) & 0xFF)), 2);
        double g = Math.pow((((rgb0 >> 8) & 0xFF) - ((rgb1 >> 8) & 0xFF)), 2);
        double b = Math.pow((((rgb0 >> 0) & 0xFF) - ((rgb1 >> 0) & 0xFF)), 2);
        return r + g + b;
    }
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] ret = new int[height()];        
        double[][] energies = new double[height()][width()];
        int[][] edgeFrom = new int[height()][width()];
        double[][] distTo = new double[height()][width()];
        
        for (int j = 0; j < height(); j++)
            for (int i = 0; i < width(); i++) {
                energies[j][i] = energy(i, j);
                if (j == 0)
                    distTo[0][i] = 1000;
                else
                    distTo[j][i] = Double.POSITIVE_INFINITY;
            }

        for (int j = 1; j < height(); j++)
            for (int i = 0; i < width(); i++) {
                if (i > 0 && distTo[j][i] > distTo[j-1][i-1] + energies[j][i]) {
                    distTo[j][i] = distTo[j-1][i-1] + energies[j][i];
                    edgeFrom[j][i] = i-1;
                }
                if (distTo[j][i] > distTo[j-1][i] + energies[j][i]) {
                    distTo[j][i] = distTo[j-1][i] + energies[j][i];
                    edgeFrom[j][i] = i;
                }
                if (i < width() - 1 && distTo[j][i] > distTo[j-1][i+1] + energies[j][i]) {
                    distTo[j][i] = distTo[j-1][i+1] + energies[j][i];
                    edgeFrom[j][i] = i+1;
                }
            }
        double minDist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width(); i++) 
            if (distTo[height() - 1][i] < minDist) {
                minDist = distTo[height() - 1][i];
                ret[height() -1] = i;
            }
        for (int j = height() - 2; j >= 0; j--)
            ret[j] = edgeFrom[j+1][ret[j+1]];        

        return ret;
    }
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture original = this.picture;
        
        transposeThisPicture();

        int[] ret = findVerticalSeam();

        this.picture = original;

        return ret;
    }
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height() || width() <= 1)
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++)
            if (seam[i] - seam[i + 1] > 1 || seam[i] - seam[i + 1] < -1)
                throw new IllegalArgumentException();

        Picture carved = new Picture(width() - 1, height());
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < seam[j]; i++)
                carved.set(i, j, this.picture.get(i, j));
            for (int i = seam[j]; i < width()-1;  i++)
                carved.set(i, j, this.picture.get(i+1, j));
        }
        this.picture = carved;
    }
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width() || height() <= 1)
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++)
            if (seam[i] - seam[i + 1] > 1 || seam[i] - seam[i + 1] < -1)
                throw new IllegalArgumentException();
        
        transposeThisPicture();

        removeVerticalSeam(seam);
        
        transposeThisPicture();

    }

    private void transposeThisPicture() {
        Picture transpose = new Picture(height(), width());
        for (int j = 0; j < height(); j++)
            for (int i = 0; i < width(); i++) 
                transpose.set(j, i, this.picture.get(i, j));
        this.picture = transpose;
    }
}
