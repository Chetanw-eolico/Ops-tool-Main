/*package com.fineart.uploader;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.core.Constants;
import com.core.blackbaze.AuthorizeAccountResponse;
import com.google.gson.Gson;

public class UpdateBucket {

	private static final long serialVersionUID = 1L;
	static String backBlazeAuthorizeAccountResponse;
	static String backBlazeGetUploadUrlResponse;
	
	public static void main(String[] args) {
		String applicationKeyId = Constants.BACKBLAZE_APP_KEY_ID; // Obtained from your B2 account page.
		String applicationKey = Constants.BACKBLAZE_APP_KEY; // Obtained from your B2 account page.
		String bucketId = Constants.BACKBLAZE_FINEART_BUCKET_ID; // The ID of the bucket you want to upload your file to
		String accountId = "709077e74957";
		
		HttpURLConnection connection = null;
		String headerForAuthorizeAccount = "Basic " + Base64.getEncoder().encodeToString((applicationKeyId + ":" + applicationKey).getBytes());
		try {
			System.out.println("Getting response from the image host...");
	        //Authorize - Once
		    URL url = new URL("https://api.backblazeb2.com/b2api/v2/b2_authorize_account");
		    connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Authorization", headerForAuthorizeAccount);
		    InputStream in = new BufferedInputStream(connection.getInputStream());    
		    backBlazeAuthorizeAccountResponse = myInputStreamReader(in);
		    
		    
		    //Get Bucket - Once
		    String postParams = "{\"accountId\":\"" + accountId + "\",\"bucketId\":\"" + bucketId + "\",\"bucketInfo\": {\"Cache-Control\":\"max-age=31536000\"}}";
		    byte postData[] = postParams.getBytes(StandardCharsets.UTF_8);
		    url = new URL(getBackBlazeApiUrl() + "/b2api/v2/b2_update_bucket");
	        connection = (HttpURLConnection)url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Authorization", getBackBlazeAccountAuthorizeToken());
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        connection.setRequestProperty("charset", "utf-8");
	        connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
	        connection.setDoOutput(true);
	        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
	        writer.write(postData);
	        backBlazeGetUploadUrlResponse = myInputStreamReader(connection.getInputStream());
	        System.out.println(backBlazeGetUploadUrlResponse);
	     
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String myInputStreamReader(InputStream in) throws IOException {
	    InputStreamReader reader = new InputStreamReader(in);
	    StringBuilder sb = new StringBuilder();
	    int c = reader.read();
	    while (c != -1) {
	        sb.append((char)c);
	        c = reader.read();
	    }
	    reader.close();
	    return sb.toString();
	}
	
	public static String getBackBlazeApiUrl() {
		Gson gson = new Gson();
	    AuthorizeAccountResponse authorizeAccountResponse = gson.fromJson(backBlazeAuthorizeAccountResponse, AuthorizeAccountResponse.class); 
	    return authorizeAccountResponse.getApiUrl();
	}
	
	public static String getBackBlazeAccountAuthorizeToken() {
		Gson gson = new Gson();
	    AuthorizeAccountResponse authorizeAccountResponse = gson.fromJson(backBlazeAuthorizeAccountResponse, AuthorizeAccountResponse.class); 
	    return authorizeAccountResponse.getAuthorizationToken();
	}

}
*/