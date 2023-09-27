import rasterize.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;

/**
 * @author PGRF FIM UHK
 * @version 2023.b
 */

public class App {

	private JPanel panel;
	private RasterBufferedImage raster;
	private int x,y;

	public App(int width, int height) {
		JFrame frame = new JFrame();

		frame.setLayout(new BorderLayout());

		frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
		frame.setResizable(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		raster = new RasterBufferedImage(width, height);

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
		// TODO: init draw
		panel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new App(800, 600).start());
	}

}
