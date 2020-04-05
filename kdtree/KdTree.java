import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int nodeNum;

    private class Node implements Comparable<Node> {
        private Point2D p;
        private Node left, right;
        private boolean even;
        private RectHV rect;

        public Node(Point2D p, boolean even, RectHV rect) {
            this.p = p;
            this.even = even;
            this.left = null;
            this.right = null;
            this.rect = rect;
        }

        @Override
        public int compareTo(Node that) {
            if (even) {
                return Double.compare(this.p.y(), that.p.y());
            }
            else {
                return Double.compare(this.p.x(), that.p.x());
            }
        }
    }

    // construct an empty set of points
    public KdTree() {
        this.root = null;
        this.nodeNum = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.nodeNum == 0;
    }

    // number of points in the set
    public int size() {
        return this.nodeNum;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        if (root == null) {
            root = new Node(p, false, new RectHV(0, 0, 1, 1));
            nodeNum++;
        }
        else {
            if (insertRec(root, p)) nodeNum++;
        }
    }

    private boolean insertRec(Node current, Point2D p) {
        if (current.p.equals(p)) return false;

        Node node = new Node(p, !current.even, null);

        if (current.compareTo(node) > 0) {
            if (current.even) {
                node.rect = new RectHV(current.rect.xmin(), current.rect.ymin(),
                                       current.rect.xmax(), current.p.y());
            }
            else {
                node.rect = new RectHV(current.rect.xmin(), current.rect.ymin(),
                                       current.p.x(), current.rect.ymax());
            }

            if (current.left == null) {
                current.left = node;
                return true;
            }
            else {
                return insertRec(current.left, p);
            }
        }
        else {
            if (current.even) {
                node.rect = new RectHV(current.rect.xmin(), current.p.y(),
                                       current.rect.xmax(), current.rect.ymax());
            }
            else {
                node.rect = new RectHV(current.p.x(), current.rect.ymin(),
                                       current.rect.xmax(), current.rect.ymax());
            }

            if (current.right == null) {
                current.right = node;
                return true;
            }
            else {
                return insertRec(current.right, p);
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        Node current = root;
        Node node = new Node(p, false, null);
        while (current != null) {
            if (current.p.x() == p.x() && current.p.y() == p.y())
                return true;

            if (current.compareTo(node) > 0) current = current.left;
            else current = current.right;
        }

        return false;
    }

    // draw all points to standard draw
    public void draw() {
        drawRec(root, 0, 0, 1, 1);
    }

    private void drawRec(Node current, double minX, double minY, double maxX, double maxY) {
        if (current == null) return;

        if (current.even) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(minX, current.p.y(), maxX, current.p.y());
            if (current.right != null) {
                drawRec(current.right, minX, current.p.y(), maxX, maxY);
            }
            if (current.left != null) {
                drawRec(current.left, minX, minY, maxX, current.p.y());
            }
        }
        else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(current.p.x(), minY, current.p.x(), maxY);
            if (current.right != null) {
                drawRec(current.right, current.p.x(), minY, maxX, maxY);
            }
            if (current.left != null) {
                drawRec(current.left, minX, minY, current.p.x(), maxY);
            }
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> list = new ArrayList<Point2D>();

        rangeRec(root, rect, list);

        return list;
    }

    private void rangeRec(Node current, RectHV rect, ArrayList<Point2D> list) {
        if (current == null) return;

        if (current.rect.intersects(rect)) {
            if (rect.contains(current.p))
                list.add(current.p);
            rangeRec(current.right, rect, list);
            rangeRec(current.left, rect, list);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        return recNearest(root, new RectHV(0, 0, 1, 1), p, null);
    }

    private Point2D recNearest(Node current, RectHV rect, Point2D p, Point2D candidate) {
        Point2D nearest = candidate;
        if (current == null) return nearest;

        if (nearest == null || p.distanceSquaredTo(nearest) > rect.distanceSquaredTo(current.p)) {
            if (nearest == null || p.distanceSquaredTo(nearest) > p.distanceSquaredTo(current.p)) {
                nearest = current.p;
            }

            if (current.even) {
                if (p.x() <= current.p.x()) {
                    if (current.left != null)
                        nearest = recNearest(current.left, current.left.rect, p, nearest);
                    if (current.right != null)
                        nearest = recNearest(current.right, current.right.rect, p, nearest);
                }
                else {
                    if (current.right != null)
                        nearest = recNearest(current.right, current.right.rect, p, nearest);
                    if (current.left != null)
                        nearest = recNearest(current.left, current.left.rect, p, nearest);
                }
            }
            else {
                if (p.y() <= current.p.y()) {
                    if (current.left != null)
                        nearest = recNearest(current.left, current.left.rect, p, nearest);
                    if (current.right != null)
                        nearest = recNearest(current.right, current.right.rect, p, nearest);
                }
                else {
                    if (current.right != null)
                        nearest = recNearest(current.right, current.right.rect, p, nearest);
                    if (current.left != null)
                        nearest = recNearest(current.left, current.left.rect, p, nearest);
                }
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
