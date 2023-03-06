package com.core.windows;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.swing.SwingWorker;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

public class CurrencyRatesUploader extends BaseWindow {

	private static final long serialVersionUID = 6760577293695458569L;

	public void updateCurrency(String query, String currency, Long epochTime, Double rate, JdbcTemplate jdbcTemplate) {
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            public String doInBackground() {
            	processUploading(query, currency, epochTime, rate, jdbcTemplate);
                return "";
            }
        };
       // execute the background thread
       worker.execute();
	}
	public boolean processUploading(String query, String currency, Long epochTime, Double rate, JdbcTemplate jdbcTemplate) {
		 if(!continueProcessing) {
			 return false;
		 }
		 System.out.println("Processing currency: " + currency + " Rate: " + rate);
		 consoleArea.append("Processing currency: " + currency + " Rate: " + rate);
         consoleArea.append("\n");
		 consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		 return jdbcTemplate.execute(query,new PreparedStatementCallback<Boolean>() {
			 	
			 @Override  
			    public Boolean doInPreparedStatement(PreparedStatement preStatement)  
			            throws SQLException, DataAccessException {  
			              
			    	preStatement.setString(1, currency);
			        preStatement.setDate(2, new java.sql.Date(epochTime));
			        preStatement.setDouble(3, rate);
			        preStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			        preStatement.setTimestamp(5, null);
			        preStatement.setString(6, "Satyam");
			        preStatement.setString(7, null);  
			              
			        return preStatement.execute();  
			              
			    }  
		});  
	}
}
