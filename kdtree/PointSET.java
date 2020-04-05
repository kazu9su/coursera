import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        this.set = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        if (!this.contains(p))
            this.set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        return this.set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : this.set)
            p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        ArrayList<Point2D> list = new ArrayList<Point2D>();

        for (Point2D p : this.set) {
            if (rect.distanceTo(p) == 0.0D)
                list.add(p);
        }

        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        double distance = 1.00;
        Point2D result = null;
        for (Point2D current : this.set) {
            if (distance > current.distanceTo(p)) {
                distance = current.distanceTo(p);
                result = current;
            }
        }

        return result;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();

            Point2D p = new Point2D(x, y);

            pointSET.insert(p);
        }

        StdOut.println(pointSET.size());
        StdOut.println(pointSET.contains(new Point2D(0.144, 0.179)));
        StdOut.println(pointSET.contains(new Point2D(0.000, 0.000)));
        pointSET.draw();
    }
}
