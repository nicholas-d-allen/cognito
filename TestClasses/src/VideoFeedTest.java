import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class VideoFeedTest {
	public static void main(String[] args) {
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		
		WebcamPanel webcamPanel = new WebcamPanel(webcam);
		webcamPanel.setImageSizeDisplayed(true);
		webcamPanel.setFPSDisplayed(true);
		
		JFrame frame = new JFrame();
		frame.add(webcamPanel);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
