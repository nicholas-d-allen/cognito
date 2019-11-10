package edu.princeton.cs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.math.geometry.shape.Rectangle;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class FaceDetectionFromWebcam extends JFrame implements Runnable, WebcamPanel.Painter{
	
	private static final long serialVersionUID = 1L;
	
	private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
	private static final HaarCascadeDetector detector = new HaarCascadeDetector();

	private Webcam webcam = null;
	private WebcamPanel.Painter painter = null;
	private List<DetectedFace> faces = null;
	
	/**
	 * Initiates the facial detection algorithm using OpenIMAJ, taking in webcam input using sarxos,
	 * showing the JFrame if {@code showFrame} is true
	 * @param showFrame whether or not we show the JFrame
	 * @throws IOException
	 */
	public FaceDetectionFromWebcam(boolean showFrame) throws IOException {
		super();
		
		webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.open();
		
		WebcamPanel panel = new WebcamPanel(webcam, false);
		panel.setPreferredSize(WebcamResolution.VGA.getSize());
		panel.setPainter(this);
		panel.setFPSDisplayed(true);
		panel.setFPSLimited(true);
		panel.setFPSLimit(30);
		panel.start();
		
		painter = panel.getDefaultPainter();

		add(panel);
		
		setTitle("Face Detection Example");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(showFrame);

		EXECUTOR.execute(this);
	}
	
	
	@Override
	/**
	 * Implements Runnable interface, runs while the webcam is open
	 */
	public void run() {
		while(true) {
			if (!webcam.isOpen()) {
				return;
			}
			faces = detector.detectFaces(ImageUtilities.createFImage(webcam.getImage()));
		}
	}
	
	@Override
	public void paintPanel(WebcamPanel panel, Graphics2D g2) {
		if (painter != null) {
			painter.paintPanel(panel, g2);
		}
	}
	
	@Override
	public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {

		if (painter != null) {
			painter.paintImage(panel, image, g2);
		}

		if (faces == null) {
			return;
		}

		Iterator<DetectedFace> dfi = faces.iterator();
		while (dfi.hasNext()) {

			DetectedFace face = dfi.next();
			Rectangle bounds = face.getBounds();

			int dx = (int) (0.1 * bounds.width);
			int dy = (int) (0.2 * bounds.height);
			int x = (int) bounds.x - dx;
			int y = (int) bounds.y - dy;
			int w = (int) bounds.width + 2 * dx;
			int h = (int) bounds.height + dy;

			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.RED);
			g2.drawRect(x, y, w, h);
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new FaceDetectionFromWebcam(true);
	}

}
