package edu.princeton.cs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioAlert{
	
	private static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
	private static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
	private static final String TWILIO_SMS_NUMBER = "+12563673806";
	private String userNumber;
	
	public TwilioAlert(String numPath) throws IOException {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		userNumber = "+1";
		File file = new File(numPath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st = br.readLine();
		for (String sec : st.split("-")) {
			userNumber += sec;
		}
		br.close();
	}
	
	public void send(String messageText) {
		Message message = Message.creator(
				new PhoneNumber(userNumber),    // to number 
				new PhoneNumber(TWILIO_SMS_NUMBER), // from number
				messageText).create();
		System.out.println(message.getSid());
	}
	
	public void send(String messageText, String media) {
		Message message = Message.creator(
				new PhoneNumber(userNumber),    // to number 
				new PhoneNumber(TWILIO_SMS_NUMBER), // from number
				messageText)
	            .setMediaUrl(
	                Arrays.asList(URI.create(media)))
	            .create();
		System.out.println(message.getSid());
	}
	
	public static void main(String[] args) throws IOException {
		TwilioAlert connection = new TwilioAlert("test_phone_number.txt");
		connection.send("Twilio Alert Test");
	}
}
