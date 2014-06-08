package com.thai.client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChangeClient {
	 private JFrame frame;
	 private JTextField textField;
	 private String name;
	 private Client client;
	 
	 public ChangeClient (String name,Client client ){
		 frame = new JFrame("Change");
		 this.name = name;
		 this.client = client;
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.getContentPane().setLayout(null);
		 frame.setBounds(100, 100, 500, 400);
		 frame.setVisible(true);
		 JLabel lblName = new JLabel(this.name);
		 lblName.setBounds(168, 38, 61, 16);
		 frame.getContentPane().add(lblName);
		 
		 textField = new JTextField();
		 textField.setBounds(137, 103, 134, 28);
		 frame.getContentPane().add(textField);
		 textField.setColumns(10);
		
		 JButton btnKick = new JButton("Kick");
		 btnKick.setBounds(32, 203, 117, 29);
		 btnKick.addActionListener(new KickAction());
		 frame.getContentPane().add(btnKick);
		 
		 JButton btnChange = new JButton("Change");
		 btnChange.setBounds(178, 203, 117, 29);
		 btnChange.addActionListener(new ChangeAction());
		 frame.getContentPane().add(btnChange);
		 
		 JButton btnCancel = new JButton("Cancel");
		 btnCancel.setBounds(327, 203, 117, 29);
		
		 btnCancel.addActionListener(new CancelAction());
		 
		 frame.getContentPane().add(btnCancel);
		 
	
         
         
	 }
	 class KickAction implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				
			
				client.sendInfo("Kick"+name);
				frame.setVisible(false);
			}
			
		}
		class CancelAction implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				
				frame.setVisible(false);
				
			}
			
		}
	 class ChangeAction implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				
				ClientName user =new ClientName();
				user.setName(name);
				if (textField.getText().length()>0) {
					user.setNeuName(textField.getText());
					
				}
				changeName.changeName(user);
				client.sendInfo("Kick nonen");
				frame.setVisible(false);
			}
			
	 
			 
		 
	 }
	 
	
}
