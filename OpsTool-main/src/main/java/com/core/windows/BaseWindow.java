package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;


public class BaseWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	protected boolean continueProcessing = true;
	protected JScrollPane scrollPane;
	protected JTextArea consoleArea;
	
	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected BaseWindow() {
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we)
		    { 
		        /*String ObjButtons[] = {"Yes","No"};
		        int PromptResult = JOptionPane.showOptionDialog(null,"This will also stop the currently running process, are you sure?", "Console Window", 
		        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
		        if(PromptResult==JOptionPane.YES_OPTION) {
		        	continueProcessing = false;
		        	setVisible(false);
		            dispose();
		        }*/
		    	continueProcessing = false;
	        	setVisible(false);
	            dispose();
		    }
		});
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setSize(628, 500);
			this.setPreferredSize(new java.awt.Dimension(628, 500));
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
			this.setTitle("Console Output...");
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			{
				scrollPane = new JScrollPane();
				getContentPane().add(scrollPane, BorderLayout.CENTER);
				scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				{
					consoleArea = new JTextArea();
					scrollPane.setViewportView(consoleArea);
				}
			}
			pack();
			this.setVisible(true);
			
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	protected void closeWindow() {
		this.setVisible(false);
		this.dispose();
	}
}
