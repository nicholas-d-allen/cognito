package edu.princeton.cs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.math.geometry.shape.Rectangle;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class FaceCamCollector implements Runnable, WebcamPanel.Painter, ActionListener{
	
	private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
	private static final HaarCascadeDetector detector = new HaarCascadeDetector();
	private final String picturePath;
	private final String numberPath;

	private Webcam webcam = null;
	private WebcamPanel.Painter painter = null;
	private List<DetectedFace> faces = null;
	private JFormattedTextField inPNum;
	private JFrame window;
	
	/**
	 * Initiates the facial detection algorithm using OpenIMAJ, taking in webcam input using sarxos,
	 * @throws IOException
	 * @throws ParseException 
	 */
	public FaceCamCollector(Webcam webcamArg, String picPath, String numPath) throws IOException, ParseException {
		picturePath = picPath;
		numberPath = numPath;
		
		JPanel jPanelWebcam = new JPanel();
		JPanel jBottomPanel = new JPanel();
		
		webcam = webcamArg;
		
		WebcamPanel webcamPanel = new WebcamPanel(webcam, false);
		webcamPanel.setPreferredSize(WebcamResolution.VGA.getSize());
		webcamPanel.setPainter(this);
		webcamPanel.setFPSDisplayed(true);
		webcamPanel.setFPSLimited(true);
		webcamPanel.setFPSLimit(30);
		webcamPanel.start();
		painter = webcamPanel.getDefaultPainter();
		jPanelWebcam.add(webcamPanel);
		
		MaskFormatter pn = new MaskFormatter("###-###-####");
		inPNum = new JFormattedTextField(pn);
		jBottomPanel.add(inPNum);
		
		JButton getButton = new JButton("Retrieve Face");
		getButton.addActionListener(this);
		jBottomPanel.add(getButton);
		
		window = new JFrame("Face Detection Example");
		window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(jPanelWebcam);
		window.add(jBottomPanel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

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
	
	public boolean isOpen() {
		return window.isVisible();
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (faces.size() != 1) {
			JOptionPane.showMessageDialog(null, "Number of faces on screen must be 1.");
		} else if (inPNum.getText().split(" ").length > 1) {
			JOptionPane.showMessageDialog(null, "Invalid phone number");
		} else {
			try {
				ImageIO.write(getFace(), "JPG", new File(picturePath));
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(
			              new FileOutputStream(numberPath), "utf-8"))) {
					writer.write(inPNum.getText());
				}
				window.dispose();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the cropped image of the face in frame
	 * @return
	 */
	public BufferedImage getFace() {
		DetectedFace face = faces.get(0);
		Rectangle bounds = face.getBounds();
		Rectangle max = new Rectangle(0, 0, WebcamResolution.VGA.getWidth(), WebcamResolution.DVGA.getHeight());
		double subx = bounds.x - 0.5 * bounds.width;
		double suby = bounds.y - 0.5 * bounds.height;
		double subw = 2 * bounds.width;
		double subh = 2 * bounds.height;
		if ((bounds.x + 1.5 * bounds.width) > max.width) {
			subw = max.width - 0.5 * bounds.x;
		}
		if ((bounds.y + 1.5 * bounds.height) > max.height) {
			subw = max.height - 0.5 * bounds.y;
		}
		if (bounds.x < max.x) {
			subx = 0;
			subw -= bounds.x;
		}
		if (bounds.y < max.y) {
			suby = 0;
			subh -= bounds.x;
		}
		BufferedImage result = webcam.getImage().getSubimage((int)subx, (int)suby, (int)(subw - 0.5), (int)(subh - 0.5));
		return result;
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		new FaceCamCollector(Webcam.getDefault(), "test_face.jpg", "test_phone_number.txt");
	}

}
