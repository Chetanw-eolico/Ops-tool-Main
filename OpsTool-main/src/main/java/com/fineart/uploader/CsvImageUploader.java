package com.fineart.uploader;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.core.Constants;
import com.core.blackbaze.AuthorizeAccountResponse;
import com.core.windows.BaseWindow;
import com.google.gson.Gson;

public class CsvImageUploader extends BaseWindow implements UploaderInterface {

	private static final long serialVersionUID = 1L;
	String imagesPath;
	String backBlazeAuthorizeAccountResponse;
	String backBlazeGetUploadUrlResponse;
	
	public CsvImageUploader() {
		super();
		String applicationKeyId = Constants.BACKBLAZE_APP_KEY_ID; // Obtained from your B2 account page.
		String applicationKey = Constants.BACKBLAZE_APP_KEY; // Obtained from your B2 account page.
		String bucketId = Constants.BACKBLAZE_FINEART_BUCKET_ID; // The ID of the bucket you want to upload your file to
		
		HttpURLConnection connection = null;
		String headerForAuthorizeAccount = "Basic " + Base64.getEncoder().encodeToString((applicationKeyId + ":" + applicationKey).getBytes());
		try {
			System.out.println("Getting response from the image host...");
	        consoleArea.append("Getting response from the image host...");
	        consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			//Authorize - Once
		    URL url = new URL("https://api.backblazeb2.com/b2api/v2/b2_authorize_account");
		    connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Authorization", headerForAuthorizeAccount);
		    InputStream in = new BufferedInputStream(connection.getInputStream());    
		    backBlazeAuthorizeAccountResponse = myInputStreamReader(in);
		    
		    //Get Bucket - Once
		    String postParams = "{\"bucketId\":\"" + bucketId + "\"}";
		    byte postData[] = postParams.getBytes(StandardCharsets.UTF_8);
		    url = new URL(getBackBlazeApiUrl() + "/b2api/v2/b2_get_upload_url");
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
	        consoleArea.append(backBlazeGetUploadUrlResponse);
	        consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
	    } catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    connection.disconnect();
		}
	}

	@Override
	public void initUploading(String imagesPath, String directoryPath) {
		this.imagesPath = imagesPath;
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            public String doInBackground() {
            	doUpload(imagesPath);
                return "";
            }
        };
       // execute the background thread
       worker.execute();
	}
	
	public void doUpload(String imagesPath) {
		try {
			File file = new File(imagesPath);
			File[] files = file.listFiles();
			Collections.shuffle(Arrays.asList(files));
            for(File imageFile : files) {
            	if(!continueProcessing) {
            		return;
            	}
            	System.out.println("Uploading Image " + imageFile.getName());
            	consoleArea.append("\nUploading Image " + imageFile.getName());
            	consoleArea.append("\n");
    			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    			uploadImageFromUrlToBackBlazeHost(imageFile);
    	    }
            System.out.println("Images Uploading Done!");
        	consoleArea.append("Images Uploading Done!");
        	consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			JOptionPane.showMessageDialog(null, "Images Uploading Done!", "Images Uploader", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void uploadImageFromUrlToBackBlazeHost(File imageFile) {
		HttpURLConnection httpUrlConnection = null;
		FileInputStream fileInputStream = null;
		try {
			byte[] fileData = new byte[(int) imageFile.length()];
			fileInputStream = new FileInputStream(imageFile);
            fileInputStream.read(fileData);
            
            //byte[] fileData = Files.readAllBytes(Paths.get(filePath));
		    URL url = new URL(getBackBlazeUploadUrl());
		    httpUrlConnection = (HttpURLConnection)url.openConnection();
		    httpUrlConnection.setRequestMethod("POST");
		    httpUrlConnection.setRequestProperty("Authorization", getBackBlazeUploadAuthorizationToken());
		    httpUrlConnection.setRequestProperty("Content-Type", "b2/x-auto");
		    httpUrlConnection.setRequestProperty("X-Bz-File-Name", imageFile.getName());
		    httpUrlConnection.setRequestProperty("X-Bz-Content-Sha1", "do_not_verify");
		    httpUrlConnection.setRequestProperty("Content-Length", fileData.length + 100 + "");
		    httpUrlConnection.setDoOutput(true);
		    DataOutputStream writer = new DataOutputStream(httpUrlConnection.getOutputStream());
		    writer.write(fileData);
		    myInputStreamReader(httpUrlConnection.getInputStream());
		    fileInputStream.close();
		    imageFile.delete();
		    System.out.println("image uploaded...");
		    consoleArea.append("\nimage uploaded...");
		    consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		} catch (Exception e) {
		    System.out.println("Error in uploading image " + e.getMessage());
		    consoleArea.append("\nError in uploading image " + e.getMessage());
		    consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		} finally {
			if(httpUrlConnection != null) {
				httpUrlConnection.disconnect();
			}
		}
	}
	
	public String getBackBlazeApiUrl() {
		Gson gson = new Gson();
	    AuthorizeAccountResponse authorizeAccountResponse = gson.fromJson(backBlazeAuthorizeAccountResponse, AuthorizeAccountResponse.class); 
	    return authorizeAccountResponse.getApiUrl();
	}
	
	public String getBackBlazeAccountAuthorizeToken() {
		Gson gson = new Gson();
	    AuthorizeAccountResponse authorizeAccountResponse = gson.fromJson(backBlazeAuthorizeAccountResponse, AuthorizeAccountResponse.class); 
	    return authorizeAccountResponse.getAuthorizationToken();
	}
	
	public String getBackBlazeUploadUrl() {
		Gson gson = new Gson();
	    AuthorizeAccountResponse authorizeAccountResponse = gson.fromJson(backBlazeGetUploadUrlResponse, AuthorizeAccountResponse.class); 
	    return authorizeAccountResponse.getUploadUrl();
	}
	
	public String getBackBlazeUploadAuthorizationToken() {
		Gson gson = new Gson();
	    AuthorizeAccountResponse authorizeAccountResponse = gson.fromJson(backBlazeGetUploadUrlResponse, AuthorizeAccountResponse.class); 
	    return authorizeAccountResponse.getAuthorizationToken();
	}
	
	public String myInputStreamReader(InputStream in) throws IOException {
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
}
