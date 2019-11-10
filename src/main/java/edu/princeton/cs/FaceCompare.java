package edu.princeton.cs;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class FaceCompare {
    private static final String subscriptionKey = System.getenv("AZURE_SUBSCRIPTION_KEY");

    private static final String uriDetectBase =
        "https://mytestproject.cognitiveservices.azure.com/face/v1.0/detect";
    private static final String uriVerifyBase =
        "https://mytestproject.cognitiveservices.azure.com/face/v1.0/verify";

    private static String userFaceID;

    public FaceCompare(String userFacePath) {
    	HttpClient httpclient = HttpClientBuilder.create().build();
    	
	    try
	    {		
	        URIBuilder builder = new URIBuilder(uriDetectBase);
	
	        // Request parameters. All of them are optional.
	        builder.setParameter("returnFaceId", "true");
	        builder.setParameter("returnFaceLandmarks", "false");
	
	        // Prepare the URI for the REST API call.
	        URI uri = builder.build();
	        HttpPost request = new HttpPost(uri);
	
	        // Request headers.
	        request.setHeader("Content-Type", "application/octet-stream");
	        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
	
	        // Request body.
	        File file = new File(userFacePath);
	    	FileInputStream fileInputStreamReader = new FileInputStream(file);
	    	byte[] bytes = new byte[(int)file.length()];
	    	fileInputStreamReader.read(bytes);
	    	ByteArrayEntity reqEntity = new ByteArrayEntity(bytes, ContentType.APPLICATION_OCTET_STREAM);
	        request.setEntity(reqEntity);
	        fileInputStreamReader.close();
	
	        // Execute the REST API call and get the response entity.
	        HttpResponse response = httpclient.execute(request);
	        HttpEntity entity = response.getEntity();
	        
	        if (entity != null)
            {
                String jsonString = EntityUtils.toString(entity).trim();

                String[] data = jsonString.split("\"");
                for (int i = 0; i < data.length; i++) {
                	if (data[i].equals("faceId")) {
                		userFaceID = data[i + 2];
                	}
                }
            }
        }
        catch (Exception e)
        {
            // Display error message.
            System.out.println(e.getMessage());
        }
    }
    
    public String faceID() {
    	return userFaceID;
    }
    
    public boolean isSameFace(String otherFacePath) {
    	HttpClient httpclient = HttpClientBuilder.create().build();
    	boolean isIdentical = false;
	    try
	    {		
	        URIBuilder builder = new URIBuilder(uriVerifyBase);
	
	
	        // Prepare the URI for the REST API call.
	        URI uri = builder.build();
	        HttpPost request = new HttpPost(uri);
	
	        // Request headers.
	        request.setHeader("Content-Type", "application/json");
	        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
	
	        // Request body.
	        String requestBody = new String("{\"faceId1\":\"" + userFaceID + "\",\"faceId2\":\"" + faceID(otherFacePath) + "\"}");
	        //System.out.println(requestBody);
	        StringEntity reqEntity = new StringEntity(requestBody);
	        request.setEntity(reqEntity);
	
	        // Execute the REST API call and get the response entity.
	        HttpResponse response = httpclient.execute(request);
	        HttpEntity entity = response.getEntity();
	        
	        if (entity != null)
            {
                String jsonString = EntityUtils.toString(entity).trim();
                
                String[] data = jsonString.split("\"");
                for (int i = 0; i < data.length; i++) {
                	if (data[i].equals("isIdentical")) {
                		System.out.println("Not error");
                		isIdentical = Boolean.parseBoolean(data[i + 1].substring(1, data[i + 1].length() - 1).trim());
                	}
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
		return isIdentical;
    }
    
    public static String faceID(String filepath) {
    	FaceCompare test = new FaceCompare(filepath);
    	return test.faceID();
    }
    
    public static boolean isFace(String filepath) {
    	return (faceID(filepath) != null);
    }
    
	
	public static void main(String[] args) {
	    FaceCompare test = new FaceCompare("test_face.jpg");
	    System.out.println(test.isSameFace("temp.jpg"));
    }
}
