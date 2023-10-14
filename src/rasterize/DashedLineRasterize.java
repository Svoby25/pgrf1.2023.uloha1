package rasterize;

/**
 * Tato třída vykresluje přerušovanou čáru, je rozšíření třídy LineRasterize
 * Využívá k tomu algoritmus Bresenham
 */
public class DashedLineRasterize extends LineRasterizer {

    public DashedLineRasterize(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int moveOnX = (x1 < x2) ? 1 : -1;
        int moveOnY = (y1 < y2) ? 1 : -1;

        int prediction = dx - dy;
        int currentX = x1;
        int currentY = y1;
        int skipCount = 0;

        while (currentX != x2 || currentY != y2) {
            int skipPixelInterval = 10;
            if (skipCount < skipPixelInterval) {
                skipCount++;
            } else {
                raster.setPixel(currentX, currentY, 0xffff00);
                skipCount = 0;
            }

            int newPrediction = 2 * prediction;

            if (newPrediction > -dy) {
                prediction -= dy;
                currentX += moveOnX;
            }

            if (newPrediction < dx) {
                prediction += dx;
                currentY += moveOnY;
            }
        }

    }
}
