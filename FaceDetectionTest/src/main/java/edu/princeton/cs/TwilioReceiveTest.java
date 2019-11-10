package edu.princeton.cs;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import static spark.Spark.*;

public class TwilioReceiveTest {
	
	public static void main(String[] args) {
		get("/", (req, res) -> "Hello!");
		
		post("/sms", (req, res) -> {
			res.type("application/xml");
			Body body = new Body
					.Builder("Hey there!")
					.build();
			Message sms = new Message
					.Builder()
					.body(body)
					.build();
			MessagingResponse twiml = new MessagingResponse
					.Builder()
					.message(sms)
					.build();
			return twiml.toXml();
		});
	}

}
