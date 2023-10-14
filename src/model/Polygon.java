package model;

import java.util.ArrayList;

public class Polygon {

    private ArrayList<Point> points;


    public Polygon()
    {
        this.points = new ArrayList<>();
    }

    public void addPoint(Point p)
    {
        this.points.add(p);
    }

    public int size()
    {
        return this.points.size();
    }


    public Point getPoint(int index)
    {
        return this.points.get(index);
    }

    public void setPoint(int index, Point point)
    {
        try {
            this.points.set(index, point);
        } catch (IndexOutOfBoundsException ex) {
            this.addPoint(point);
        }
    }

    public void clear()
    {
        this.points.clear();
    }
}
