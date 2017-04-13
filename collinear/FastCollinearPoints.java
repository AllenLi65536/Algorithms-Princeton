/******************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    java FastCollinearPoints
 *  Dependencies: LineSegment, Point
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
public class FastCollinearPoints {
    private LineSegment[] lines;
    private int lineN = 0;
    private int n = 0;
    
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new java.lang.NullPointerException();
        for (int i = 0; i < points.length; i++)
            if (points[i] == null)
                throw new java.lang.NullPointerException();
        for (int i = 0; i < points.length; i++)
            for (int j = i+1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0)
                    throw new java.lang.IllegalArgumentException();
        
        n = points.length;        
        if (n < 4)
            return;
        lines = new LineSegment[n];

        for (int i = 0; i < points.length; i++) {
            Point[] pointSort = new Point[points.length];
            for (int j = 0; j < points.length; j++)
                pointSort[j] = points[j];
            
            Arrays.sort(pointSort, points[i].slopeOrder());
               
            for (int j = 2; j < points.length-1; j++) {
                if (points[i].slopeTo(pointSort[j-1]) == points[i].slopeTo(pointSort[j])) {
                    int offset = j-1;
                    boolean inLoop = false;
                    while (j < points.length-1 && points[i].slopeTo(pointSort[j]) == points[i].slopeTo(pointSort[j+1])) {
                        j++;
                        inLoop = true;
                    }
                    if (inLoop) { 
                        j--;
                        int lo = 0;
                        int hi = 0;
                        for (int k = offset; k <= j+1; k++) {
                            if (pointSort[lo].compareTo(pointSort[k]) > 0)
                                lo = k;
                            if (pointSort[hi].compareTo(pointSort[k]) < 0)
                                hi = k;
                        }
                        if (lo != 0)
                            continue;

                        if (lineN == n)
                            resize(2*n);
                        lines[lineN++] = new LineSegment(pointSort[lo], pointSort[hi]);

                    }
                }
            }
        }
    }
    private void resize(int newN) {
        LineSegment[] copy = new LineSegment[newN];
        for (int i = 0; i < lineN; i++) 
            copy[i] = lines[i];
        
        n = newN;
        lines = copy;    
    }
    public int numberOfSegments() {
        return lineN;
    }
    public LineSegment[] segments() {
        LineSegment[] copy = new LineSegment[lineN];
        for (int i = 0; i < lineN; i++) {
            copy[i] = lines[i];        
        }
        return copy;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        // for (LineSegment segment : collinear.segments()) {
        LineSegment[] segment = collinear.segments();
        for (int i = 0; i < collinear.numberOfSegments(); i++) {
            StdOut.println(segment[i]);
            segment[i].draw();
        }
        // StdOut.println(collinear.numberOfSegments());
        StdDraw.show();

    }
}
