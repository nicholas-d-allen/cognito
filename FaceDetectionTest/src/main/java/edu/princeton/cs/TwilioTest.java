package edu.princeton.cs;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioTest {
	
	private static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
	private static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
	private static final String TWILIO_SMS_NUMBER = "+12563673806";
	
	public TwilioTest() {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	}
	
	public void send(String messageText) {
		Message message = Message.creator(
				new PhoneNumber("+15852505798"),    // to number 
				new PhoneNumber(TWILIO_SMS_NUMBER), // from number
				messageText).create();
		System.out.println(message.getSid());
	}
	
	public static void main(String[] args) {
		TwilioTest connection = new TwilioTest();
		connection.send("This is an SMS message sent by Twilio at HackPrinceton F2019!");
	}
}
