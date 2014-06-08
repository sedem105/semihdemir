package com.thai.client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.omg.CORBA.portable.Delegate;

import com.google.gson.Gson;
import com.thai.server.ServerInfo;

import de.hhu.propra.teamA2.Model.SavePermission;


public class Client {
	private  Permission per = new Permission();
	private List<String>listclient;
	public Clientdelegate delegete;
	private boolean isHost;
	  BufferedReader in;
	    PrintWriter out;
	    
	    JFrame frame = new JFrame("Chatter");
	    JTextField textField = new JTextField(40);
	    JTextArea messageArea = new JTextArea(8, 40);


	    public void Clientrun(boolean isHost) throws IOException{
	    	this.isHost = isHost;
	    	run();
	    }
	    public void setDelegete (Clientdelegate delegete){
	    	this.delegete = delegete;
	    }
	    
	    public void sendInfo(String info){
	    	out.println(info);
	    }
	    public Client(Clientdelegate delegete) throws IOException {
	    	this.delegete = delegete;
	        // Layout GUI
	    	changeName.createList(null);
	    	 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	         frame.setVisible(true);
	    	 textField.setEditable(false);
	         messageArea.setEditable(false);
	         frame.getContentPane().add(textField, "North");
	         frame.getContentPane().add(new JScrollPane(messageArea), "Center");
	         frame.pack();

	         // Add Listeners
	         textField.addActionListener(new ActionListener() {
	             /**
	              * Responds to pressing the enter key in the textfield by sending
	              * the contents of the text field to the server.    Then clear
	              * the text area in preparation for the next message.
	              */
	             public void actionPerformed(ActionEvent e) {
	                 out.println(textField.getText());
	                 textField.setText("");
	             }
	         });
	     }

	     /**
	      * Prompt for and return the address of the server.
	     * @throws UnknownHostException 
	      */
	     private String getServerAddress() throws UnknownHostException {
	    	 
	    	 
	    	
	    	 if(isHost){
	    		 InetAddress ip = InetAddress.getLocalHost();
	    		 return ip.getHostAddress();
	    	 }
	         return JOptionPane.showInputDialog(
	             frame,
	             "Enter IP Address of the Server:",
	             "Welcome to the Chatter",
	             JOptionPane.QUESTION_MESSAGE);
	     }

	     /**
	      * Prompt for and return the desired screen name.
	      */
	     private String getName() {
	         return JOptionPane.showInputDialog(
	             frame,
	             "Choose a screen name:",
	             "Screen name selection",
	             JOptionPane.PLAIN_MESSAGE);
	         
	      
	         
	     }

	     /**
	      * Connects to the server then enters the processing loop.
	      */
	     private void run() throws IOException {

	         // Make connection and initialize streams
	         String serverAddress = getServerAddress();
	         Socket socket = new Socket(serverAddress, 9001);
	         
	         in = new BufferedReader(new InputStreamReader(
	             socket.getInputStream()));
	         out = new PrintWriter(socket.getOutputStream(), true);

	         // Process all messages from server, according to the protocol.
	         
	         while (true) {
	        	
	             String line = in.readLine();
	            
	             if(!line.equals("SUBMITNAME")&&!line.equals("NAMEACCEPTED"))
	             {
	            	 this.delegete.update(line.substring(8));
	             }
	             if (line.startsWith("SUBMITNAME")) {
	            	  
	            	  String name = null;
	                 out.println(name=getName());
	                 per.setNameSpiel(name);
	                 
	             } else if (line.startsWith("NAMEACCEPTED")) {
	                 textField.setEditable(true);
	                
	             } else if (line.startsWith("MESSAGE")) {
	            	 Gson g = new Gson();
	            	 
	            	 Message newmess =g.fromJson(line.substring(8), Message.class);
	            	 String neuname = changeName.neueName(newmess.getName());
	            	 if(neuname!=null)
	            		 newmess.setName(neuname);
	                 messageArea.append(newmess.getName()+":"+newmess.getInfo() + "\n");
	             }
	             else if (line.startsWith("Kick"))
	             {
	            	 String kickname = line.substring(4);
	            	 System.out.println("Kickname "+kickname);
	            	 System.out.print(per.getNameSpiel());
	            	 listclient.remove(kickname);
	            	 this.delegete.updateTable(listclient);
	            	 if(kickname.equals(per.getNameSpiel()))
	            	 {
	            		
	            		 socket.close();
	            		 frame.setVisible(false);
	            		 this.delegete.kick();
	            		 
	            		 
	            	 }
	             }
	             else if (line.startsWith("Json"))
	             {
	            	 per.setSpiel(false);
	            	 String json = line.substring(4);
	            	  System.out.println("update"+json);
	            	 Gson g = new Gson();
	            	 
	            	 ServerInfo info = g.fromJson(json, ServerInfo.class);
	            	 if(info.getList().size()>-1&&(info.getList().get(0).equals(per.getNameSpiel())||info.getList().get(0).equals(per.getNameSpiel())))
	            		 per.setSpiel(true);
	            	
	            	 System.out.print("Spieler is "+per.getNameSpiel());
	            	 SavePermission.savePermission(per);
	            	 listclient = info.getList();
	            		this.delegete.updateTable(info.getList());
	            	
	             }
	             
	         }
	     }

	   
	
	 }