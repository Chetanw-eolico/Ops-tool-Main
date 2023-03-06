package com.core.blackbaze;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.core.Constants;
import com.google.gson.Gson;

public class BackblazeImageDeleter extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField fromTextField;
	private JTextField toTextField;
	private JTextArea consoleArea;
	String backBlazeAuthorizeAccountResponse;
	String backBlazeGetUploadUrlResponse;

	public static void initDeleter() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BackblazeImageDeleter frame = new BackblazeImageDeleter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public BackblazeImageDeleter() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFrom = new JLabel("From:");
		lblFrom.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblFrom.setBounds(10, 16, 100, 50);
		contentPane.add(lblFrom);
		
		fromTextField = new JTextField();
		fromTextField.setFont(new Font("Tahoma", Font.BOLD, 16));
		fromTextField.setBounds(84, 24, 117, 34);
		contentPane.add(fromTextField);
		fromTextField.setColumns(10);
		
		JLabel lblTo = new JLabel("To:");
		lblTo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTo.setBounds(211, 28, 46, 26);
		contentPane.add(lblTo);
		
		toTextField = new JTextField();
		toTextField.setFont(new Font("Tahoma", Font.BOLD, 16));
		toTextField.setColumns(10);
		toTextField.setBounds(250, 24, 117, 34);
		contentPane.add(toTextField);
		
		JButton btnNewButton = new JButton("Start Deleting");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
		            public String doInBackground() {
		            	startDownloadButtonActionPerformed();
		                return "";
		            }
		        };
		     // execute the background thread
			    worker.execute();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton.setBounds(416, 16, 158, 50);
		contentPane.add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 77, 554, 168);
		contentPane.add(scrollPane);
		
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		consoleArea = new JTextArea();
		consoleArea.setBounds(64, 108, 6, 20);
		scrollPane.add(consoleArea);
		
		scrollPane.setViewportView(consoleArea);
		
		initBackblaze();
	}

	private void initBackblaze() {
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

	protected void startDownloadButtonActionPerformed() {
		try {
			String filename = "C:\\Users\\Public\\datamigration\\imagenames.txt";
			int counter = 1;
			int startLine = Integer.parseInt(fromTextField.getText().trim());
			int endLine = Integer.parseInt(toTextField.getText().trim());
			String fileLine = "";
			
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
				while ((fileLine = br.readLine()) != null) {
					if(counter >= startLine && counter <= endLine) {
						try {
							String localImageFileName = String.valueOf(getCRC32(fileLine)) + ".jpg";
							
							System.out.println("\n" + "Original filename: " + fileLine + ", Encrypted One: " + localImageFileName);
							consoleArea.append("\n" + "Original filename: " + fileLine + ", Encrypted One: " + localImageFileName);
							consoleArea.append("\n");
							consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
							
							
							
						}  catch (Exception e) {
							e.printStackTrace();
							consoleArea.append("\n" + e.getMessage());
							consoleArea.append("\n");
							consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
						}
					}
					counter++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static long getCRC32(String input) {
        byte[] bytes = input.getBytes();
        Checksum checksum = new CRC32(); // java.util.zip.CRC32
        checksum.update(bytes, 0, bytes.length);

        return checksum.getValue();
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
}
