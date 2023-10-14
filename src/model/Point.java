package model;

public class Point {

    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;

        if (this.y < 0) {
            this.y = 0;
        }

        if (this.x < 0) {
            this.x = 0;
        }
    }

    public Point(double x, double y) {
        this.x = (int) Math.round(x);
        this.y = (int) Math.round(y);

        if (this.y < 0) {
            this.y = 0;
        }

        if (this.x < 0) {
            this.x = 0;
        }
    }

    public boolean isInCanvasRange(int width, int height) {
        return (this.x < width && this.x > 0) && (this.y < height && this.y > 0);
    }

    public void correctCoordinatesByMaxSize(int width, int height)
    {
        if (this.x > width) {
            this.x = width;
        }

        if(this.y > height) {
            this.y = height;
        }
    }


}
