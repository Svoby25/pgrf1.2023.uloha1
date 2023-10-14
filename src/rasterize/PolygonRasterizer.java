package rasterize;

import model.Line;
import model.Point;
import model.Polygon;

/**
 * Tato třída slouží k rasterizaci polygonů
 */
public class PolygonRasterizer {
    private LineRasterizer rasterizer;

    public PolygonRasterizer(LineRasterizer rasterizer) {
        this.rasterizer = rasterizer;
    }

    public void rasterize(Polygon polygon) {
        for (int i = 0; i < polygon.size(); i++) {
            int index1 = i;
            int index2 = index1 + 1;

            Point point1 = polygon.getPoint(index1);
            if (index2 == polygon.size()) {
                index2 = 0;
            }

            Point point2 = polygon.getPoint(index2);

            this.rasterizer.rasterize(new Line(point1, point2, 0xffff00));
            if (polygon.size() == 2) {
                break;
            }
        }

    }

    public void setRasterizer(LineRasterizer rasterizer) {
        this.rasterizer = rasterizer;
    }
}
