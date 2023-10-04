import model.Line;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.LineRasterizerTrivial;
import rasterize.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Point;


/**
 * @author PGRF FIM UHK
 * @version 2023.b
 */

public class App {

	private JPanel panel;
	private RasterBufferedImage raster;
	private LineRasterizer lineRasterizer;

	public App(int width, int height) {
		JFrame frame = new JFrame();

		frame.setLayout(new BorderLayout());

		frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
		frame.setResizable(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		raster = new RasterBufferedImage(width, height);
//		lineRasterizer = new LineRasterizerGraphics(raster);
		lineRasterizer = new LineRasterizerTrivial(raster);

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
		frame.pack();
		frame.setVisible(true);

		panel.requestFocus();
		panel.requestFocusInWindow();
		panel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);

				raster.clear();

				Point p1 = new Point(width / 2, height / 2);
				Point p2 = new Point(e.getX(), e.getY());
				Line line  = new Line(p1, p2, 0xffff00);
				lineRasterizer.rasterize(line);

				panel.repaint();
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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new App(800, 600).start());
	}

}
