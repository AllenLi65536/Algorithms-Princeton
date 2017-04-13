/******************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    java PointSET
 *  Dependencies: 
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
// import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
public class PointSET {
    private SET<Point2D> points;

    public PointSET() {                               // construct an empty set of points 
        points = new SET<Point2D>();
    }

    public boolean isEmpty() {                     // is the set empty? 
        return points.size() == 0;   
    }

    public int size() {                        // number of points in the set 
        return points.size();
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new NullPointerException();
        points.add(p);
    }

    public boolean contains(Point2D p) {           // does the set contain point p? 
        if (p == null)
            throw new NullPointerException();
        return points.contains(p);
    }

    public void draw() {                        // draw all points to standard draw 
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : points)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle 
        if (rect == null)
            throw new NullPointerException();
        Queue<Point2D> ret = new Queue<Point2D>();
        for (Point2D p : points) 
            if (rect.contains(p))
                ret.enqueue(p);
        return ret;
    }

    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null)
            throw new NullPointerException();
        if (points.size() == 0)
            return null;
        Point2D retP = null;
        double distance = Double.POSITIVE_INFINITY;
        for (Point2D ps : points)
            if (ps.distanceSquaredTo(p) < distance) {
                retP = ps;
                distance = ps.distanceSquaredTo(p);           
            }
        return retP;
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional) 
        In in = new In(args[0]);
        PointSET test = new PointSET();
        while (!in.isEmpty())
            test.insert(new Point2D(in.readDouble(), in.readDouble()));

        test.draw();
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.05);
        test.nearest(new Point2D(0.5, 1)).draw();
    }
}

