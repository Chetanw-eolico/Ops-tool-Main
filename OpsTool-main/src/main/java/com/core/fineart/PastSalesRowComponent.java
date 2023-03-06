package com.core.fineart;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;

import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;

public class PastSalesRowComponent extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel basePanel;
	private JLabel fromLabel;
	protected JRadioButton radioButton;
	private JDateChooser toDateChooser;
	private JLabel toLabel;
	private JDateChooser fromDateChooser;
	
	public PastSalesRowComponent() {
		try {
			{
				this.setPreferredSize(new Dimension(650, 80));
				setLayout(null);
				{
					basePanel = new JPanel();
					basePanel.setBounds(10, 5, 630, 67);
					this.add(basePanel);
					basePanel.setPreferredSize(new java.awt.Dimension(556, 67));
					basePanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					basePanel.setLayout(null);
					{
						radioButton = new JRadioButton();
						basePanel.add(radioButton);
						radioButton.setFont(new java.awt.Font("Segoe UI",1,18));
						radioButton.setPreferredSize(new java.awt.Dimension(99, 26));
						radioButton.setBounds(6, 22, 120, 26);
						radioButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								christiesRadioButtonActionPerformed(evt);
							}
						});
					}
					{
						fromLabel = new JLabel();
						basePanel.add(fromLabel);
						fromLabel.setText("From:");
						fromLabel.setFont(new java.awt.Font("Segoe UI",0,14));
						fromLabel.setBounds(128, 21, 41, 33);
					}
					{
						fromDateChooser = new JDateChooser();
						basePanel.add(fromDateChooser);
						fromDateChooser.setFont(new java.awt.Font("Segoe UI",0,14));
						fromDateChooser.setBounds(191, 14, 173, 40);
					}
					{
						toLabel = new JLabel();
						basePanel.add(toLabel);
						toLabel.setText("To:");
						toLabel.setFont(new java.awt.Font("Segoe UI",0,14));
						toLabel.setBounds(374, 21, 28, 33);
					}
					{
						toDateChooser = new JDateChooser();
						basePanel.add(toDateChooser);
						toDateChooser.setFont(new java.awt.Font("Segoe UI",0,14));
						toDateChooser.setBounds(415, 14, 191, 40);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void christiesRadioButtonActionPerformed(ActionEvent evt) {
		FineartUtils.setComponentSelection(this);
	}
	
	public JRadioButton getRadioButton() {
		return radioButton;
	}

	public void setChristiesRadioButton(JRadioButton christiesRadioButton) {
		this.radioButton = christiesRadioButton;
	}

	public JDateChooser getToDateChooser() {
		return toDateChooser;
	}

	public void setToDateChooser(JDateChooser toDateChooser) {
		this.toDateChooser = toDateChooser;
	}

	public JDateChooser getFromDateChooser() {
		return fromDateChooser;
	}

	public void setFromDateChooser(JDateChooser fromDateChooser) {
		this.fromDateChooser = fromDateChooser;
	}
}