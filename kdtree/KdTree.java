/******************************************************************************
 *  Compilation:  javac KdTree.java
 *  Execution:    java KdTree
 *  Dependencies: 
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
public class KdTree {

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node right;
        private Node left;
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }
    private int n;
    private Node root;
    
    private Point2D nearestP;
    private double dist;
   
    // construct an empty set of points 
    public KdTree() { 
        root = null; // new Node();
        n = 0;
    }
   
   // is the set empty? 
    public boolean isEmpty() {
        return n == 0;
    }
    // number of points in the set 
    public int size() {
        return n;
    }
   
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();
         root = insert(root, p, true, 0, 0, 1, 1);
    }

    private Node insert(Node x, Point2D p, boolean isOdd, double xmin, double ymin, double xmax, double ymax) {
        if (x == null) {
            n++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }
        if (p.x() == x.p.x() && p.y() == x.p.y())
            return x;
        
        if (isOdd) {
            if (p.x() >= x.p.x())
                x.right = insert(x.right, p, !isOdd, x.p.x(), ymin, xmax, ymax);
            else 
                x.left = insert(x.left, p, !isOdd, xmin, ymin, x.p.x(), ymax);
        }
        else {
            if (p.y() >= x.p.y())
                x.right = insert(x.right, p, !isOdd, xmin, x.p.y(), xmax, ymax);
            else 
                x.left = insert(x.left, p, !isOdd, xmin, ymin, xmax, x.p.y());
        }
        return x;
    }
   
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        return lookFor(root, p, true);
    }
    private boolean lookFor(Node x, Point2D p, boolean isOdd) {
        if (x == null)
            return false;
        if (p.x() == x.p.x() && p.y() == x.p.y())
            return true;
        
        if (isOdd) {
            if (p.x() >= x.p.x())
                return lookFor(x.right, p, !isOdd);
            else 
                return lookFor(x.left, p, !isOdd);
        }
        else {
            if (p.y() >= x.p.y())
                return lookFor(x.right, p, !isOdd);
            else 
                return lookFor(x.left, p, !isOdd);
        }
        // return false;
    }
   
    // draw all points to standard draw 
    public void draw() {
        draw(root, true);   
    }
    private void draw(Node x, boolean isOdd) {
        if (x == null)
            return;
        
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        StdDraw.setPenRadius();

        if (isOdd) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        
        if (x.right != null)
            draw(x.right, !isOdd);
        if (x.left != null)
            draw(x.left, !isOdd);
    }
  
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException();
        Queue<Point2D> ret = new Queue<Point2D>();
        range(root, ret, rect);
        return ret;
    }
    private void range(Node x, Queue<Point2D> ret, RectHV rect) {
        if (x == null)
            return;

        if (rect.contains(x.p))
            ret.enqueue(x.p);

        if (x.right != null && x.right.rect.intersects(rect))
            range(x.right, ret, rect);
        if (x.left != null && x.left.rect.intersects(rect))
            range(x.left, ret, rect);
    }
  
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        if (isEmpty())
            return null;
        nearestP = null;
        dist = Double.POSITIVE_INFINITY;
        nearestP = nearest(p, root, true);
        return nearestP; // nearest(p, root, true);
    }
    private Point2D nearest(Point2D p, Node x, boolean isOdd) {
        if (x == null)
            return nearestP;
        
        if (x.p.distanceSquaredTo(p) < dist) {
            nearestP = x.p;
            dist = x.p.distanceSquaredTo(p);
        }

        if (isOdd) {
            if (p.x() >= x.p.x()) {
                nearest(p, x.right, !isOdd);
                if (x.left != null && dist > x.left.rect.distanceSquaredTo(p))
                    nearest(p, x.left, !isOdd);
            }
            else {
                nearest(p, x.left, !isOdd);
                if (x.right != null && dist > x.right.rect.distanceSquaredTo(p))
                    nearest(p, x.right, !isOdd);
            }
        }
        else {
            if (p.y() >= x.p.y()) {
                nearest(p, x.right, !isOdd);
                if (x.left != null && dist > x.left.rect.distanceSquaredTo(p))
                    nearest(p, x.left, !isOdd);
            }
            else {
                nearest(p, x.left, !isOdd);
                if (x.right != null && dist > x.right.rect.distanceSquaredTo(p))
                    nearest(p, x.right, !isOdd);
            }
        }

        return nearestP;
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        In in = new In(args[0]);
        KdTree test = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            StdOut.println(test.contains(new Point2D(x, y)));
            test.insert(new Point2D(x, y));
            StdOut.println(test.contains(new Point2D(x, y)));
        }


        test.draw();
        // StdDraw.setPenColor(StdDraw.BLUE);
        // StdDraw.setPenRadius(0.05);
    
    }

}
