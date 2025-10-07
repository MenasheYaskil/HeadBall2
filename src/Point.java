public class Point {
    private int x, y;

    public Point(int x, int y) { this.x = x; this.y = y; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public void set(int x, int y) { this.x = x; this.y = y; }

    public double distance(Point p) { return Math.hypot(x - p.x, y - p.y); }
}
