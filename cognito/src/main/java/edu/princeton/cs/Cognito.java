package edu.princeton.cs;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

public class Cognito {
	public static void main(String args[]) throws IOException, ParseException {
		File face = new File("user_face.jpg");
		File number = new File("user_phone_number.txt");
		face.deleteOnExit();
		number.deleteOnExit();
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		
		FaceCamCollector dataCollection = new FaceCamCollector(webcam, "user_face.jpg", "user_phone_number.txt");
		while (dataCollection.isOpen()) {
			System.out.print("");
		}
		
		new ContinuousWebcamStream(webcam, "user_face.jpg", "user_phone_number.txt", false);
		
	}
}
