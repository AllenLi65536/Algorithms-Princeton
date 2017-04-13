/******************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints
 *  Dependencies: LineSegment, Point
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    private LineSegment[] lines;
    private int lineN = 0;
    private int n = 0;
    
    public BruteCollinearPoints(Point[] points) {
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
        if (points.length < 4)
            return;
        lines = new LineSegment[n];

        for (int i = 0; i < points.length; i++)
            for (int j = i+1; j < points.length; j++)
                for (int k = j+1; k < points.length; k++)
                    for (int l = k+1; l < points.length; l++) 
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])
                            && points[i].slopeTo(points[j]) == points[i].slopeTo(points[l])) {
                            int lo = i;
                            int hi = i;
                            if (points[lo].compareTo(points[j]) > 0)
                                lo = j;
                            if (points[lo].compareTo(points[k]) > 0)
                                lo = k;
                            if (points[lo].compareTo(points[l]) > 0)
                                lo = l;
                            if (points[hi].compareTo(points[j]) < 0)
                                hi = j;
                            if (points[hi].compareTo(points[k]) < 0)
                                hi = k;
                            if (points[hi].compareTo(points[l]) < 0)
                                hi = l;
                            if (lineN == n)
                                resize(2*n);

                            lines[lineN++] = new LineSegment(points[lo], points[hi]);
                            // StdOut.println(i+" "+j+" "+k+" "+l);
                            // StdOut.println(points[i].slopeTo(points[l])+" "+points[i].slopeTo(points[j]));
                            // StdOut.println(lines[lineN-1]);
                        }

    }
    private void resize(int newN) {
        LineSegment[] copy = new LineSegment[newN];
        for (int i = 0; i < lineN; i++) {
            copy[i] = lines[i];        
        }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        // for (LineSegment segment : collinear.segments()) {
        LineSegment[] segment = collinear.segments();
        for (int i = 0; i < collinear.numberOfSegments(); i++) {
            StdOut.println(segment[i]);
            segment[i].draw();
        }
        StdDraw.show();

    }
}
