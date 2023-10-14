import model.Line;
import model.Point;
import model.Polygon;
import rasterize.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Canvas {

    private final JPanel panel;

    private final JTextPane modeTextPane;
    private final RasterBufferedImage raster;

    private Point pStart, pEnd;
    private LineRasterizer lineRasterizer;

    private PolygonRasterizer polygonRasterizer;

    private final Polygon polygon;

    private boolean isShiftActive = false;

    private MouseAdapter lineAdapter, polygonMouseAdapter;

    private MouseMotionAdapter lineMotionAdapter, polygonMouseMotionAdapter;

    public Canvas(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());

        frame.setTitle("UHK FIM PGRF PROJEKT 1 - SVOBODA");
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferedImage(width, height);

        this.lineRasterizer = new DashedLineRasterize(raster);

        this.polygon = new Polygon();
        this.polygonRasterizer = new PolygonRasterizer(this.lineRasterizer);

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);

        JPanel panel2 = new JPanel();
        panel2.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        this.modeTextPane = new JTextPane();
        this.modeTextPane.setText("Režim úsečky");
        this.modeTextPane.disable();

        panel2.add(this.modeTextPane);
        frame.add(panel2, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        this.initRasterizeListeners();

        panel.addMouseListener(lineAdapter);
        panel.addMouseMotionListener(lineMotionAdapter);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    // Klavesou "U" se zapne režim úsečky
                    case KeyEvent.VK_U -> {
                        panel.removeMouseListener(polygonMouseAdapter);
                        panel.removeMouseMotionListener(polygonMouseMotionAdapter);

                        panel.addMouseListener(lineAdapter);
                        panel.addMouseMotionListener(lineMotionAdapter);

                        JOptionPane.showMessageDialog(panel, "Přepnuto na kreslení úseček");
                        modeTextPane.setText("Režim úsečky");
                    }
                    // Klavesou "P" se zapne režim polygonu
                    case KeyEvent.VK_P -> {
                        panel.removeMouseListener(lineAdapter);
                        panel.removeMouseMotionListener(lineMotionAdapter);

                        panel.addMouseListener(polygonMouseAdapter);
                        panel.addMouseMotionListener(polygonMouseMotionAdapter);

                        JOptionPane.showMessageDialog(panel, "Přepnuto na kreslení polygonů");
                        modeTextPane.setText("Režim polygon");
                    }
                    // Klavesou "SHIFT" se zapne režim vodorovného, svislého, diagonálního kreslení
                    case KeyEvent.VK_SHIFT ->
                            isShiftActive = true;
                    case KeyEvent.VK_C -> clearScene();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    isShiftActive = false;
                }
            }
        });
    }

    public void clear(int color) {
        raster.setClearColor(color);
        raster.clear();

    }

    public void present(Graphics graphics) {
        raster.repaint(graphics);
    }

    public void start() {
        clear(0xaaaaaa);
        panel.repaint();
    }

    private void clearScene() {
        clear(0x000000);
        this.pStart = null;
        this.pEnd = null;
        this.lineRasterizer = new DashedLineRasterize(raster);
        this.polygonRasterizer = new PolygonRasterizer(this.lineRasterizer);
        this.polygon.clear();

        panel.repaint();

        JOptionPane.showMessageDialog(panel, "Scéna vyresetována!");

    }

    private void initRasterizeListeners() {
        this.lineAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pStart = new Point(e.getX(), e.getY());
                panel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                clear(0xffffff);

                lineRasterizer = new FullLineRasterize(raster);
                lineRasterizer.rasterize(new Line(pStart, pEnd, 0xffff00));

                panel.repaint();

                lineRasterizer = new DashedLineRasterize(raster);
            }
        };

        this.lineMotionAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                clear(0xaaaaaa);

                if (pStart != null) {
                    pEnd = new Point(e.getX(), e.getY());

                    pEnd.correctCoordinatesByMaxSize(raster.getWidth(), raster.getHeight());

                    if (isShiftActive) {
                        float dy = Math.abs(pEnd.y - pStart.y);
                        float dx = Math.abs(pEnd.x - pStart.x);

                        if (dy < 15) {
                            pEnd.y = pStart.y;
                        } else if (dx < 15) {
                            pEnd.x = pStart.x;
                        } else {
                            if (pEnd.y < pStart.y) {
                                pEnd.y -= Math.round(dx - dy);
                            } else {
                                pEnd.y += Math.round(dx - dy);
                            }
                        }
                    }

                    lineRasterizer.rasterize(new Line(pStart, pEnd, 0xffff00));
                    panel.repaint();
                }
            }
        };

        this.polygonMouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());

                polygon.addPoint(p);
                polygonRasterizer.rasterize(polygon);

                panel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                polygonRasterizer.setRasterizer(new FullLineRasterize(raster));
                polygonRasterizer.rasterize(polygon);

                panel.repaint();

                polygonRasterizer.setRasterizer(new DashedLineRasterize(raster));
            }
        };

        this.polygonMouseMotionAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                clear(0xaaaaaa);
                Point point = new Point(e.getX(), e.getY());

                if (!point.isInCanvasRange(raster.getWidth(), raster.getHeight())) {
                    return;
                }

                if (polygon.size() <= 2) {
                    polygon.setPoint(1, point);
                } else {
                    polygon.setPoint(polygon.size() - 1, point);
                }

                polygonRasterizer.rasterize(polygon);
                panel.repaint();
            }
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Canvas(800, 600).start());
    }
}
