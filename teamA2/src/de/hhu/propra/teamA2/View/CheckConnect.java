package de.hhu.propra.teamA2.View;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;

import com.thai.client.ChangeClient;
import com.thai.client.Client;
import com.thai.client.Clientdelegate;
import com.thai.client.changeName;
import com.thai.server.Server;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public  class CheckConnect extends JFrame implements Clientdelegate{
	private JTable table;
	JLabel ipadress;
	private DefaultTableModel model;
	Client client;
	Thread thread1;
	CheckConnect() throws IOException{
		super();
		changeName.createList(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1123, 617);


		getContentPane().setLayout(null);
		 model = new DefaultTableModel(); 
		table = new JTable(model);
		model.addColumn("Name"); 
		model.addColumn("Spieler");
		model.addRow(new Object[]{"Name" ,"Spieler"});
		table.setBounds(104, 229, 315, 199);

		
		getContentPane().add(table);
		
		JLabel lblNewLabel = new JLabel("Check Connection");
		lblNewLabel.setBounds(106, 173, 200, 50);
		getContentPane().add(lblNewLabel);
		  
		InetAddress ip = InetAddress.getLocalHost();
		 ipadress = new JLabel("Current IP address : " + ip.getHostAddress());
	
	       
		
		ipadress.setBounds(800, 6, 282, 16);
		getContentPane().add(ipadress);
		
		JButton Host = new JButton("Host");
		Host.setBounds(767, 49, 117, 29);
		getContentPane().add(Host);
		
		JButton btnClient = new JButton("Client");
		btnClient.addActionListener(new CreateClient());
		btnClient.setBounds(919, 49, 117, 29);
		getContentPane().add(btnClient);
		Host.addActionListener(new CreateHost());

	}

	class CreateHost implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			   Thread thread2 = new Thread(){
		        	 public void run(){
		        		 Server server = new Server();
		        		 try {
							server.runServer();
						} catch (IOException e) {
							e.printStackTrace();
						}
		        		 
		        	 }
		        	   
		          };
		          thread2.start();
			makeClient(true);

			
		}
		
	}
	class CreateClient implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			
		          makeClient(false);
		}
		
	}

	private void makeClient(final boolean isHost){
		final Clientdelegate  test = this; 
		   thread1 = new Thread(){
	          	public void run(){
	          		
	          		
	  				try {
	  					client = new Client(test);
	  					
	  					 client.Clientrun(isHost);
	  					 
	  				} catch (IOException e) {
	  					e.printStackTrace();
	  				}
	                 
	          	}
	          };
	          thread1.start();
	}
	
	@Override
	public void update(String info) {


		
	}
	
	class changeclient implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			System.out.println(table.getSelectedColumnCount());
		}
		
		
	}
	@Override
	public void updateTable(final List<String> list) {
		table.setVisible(false);
		model = new DefaultTableModel(); 
		table = new JTable(model);
		model.addColumn("Name"); 
		model.addColumn("Spieler");
		model.addRow(new Object[]{"Name" ,"Spieler"});
		table.setBounds(104, 229, 315, 199);
		
		getContentPane().add(table);
		for(int i  =0; i <list.size();i++)
		{
			System.out.println(list.get(i));
			String name = list.get(i);
			String spieler ="Zuschauer";
			if(i ==0)
				spieler = "Host";
			if(i==1)
				spieler = "Spieler";
			
			
			String neuname = changeName.neueName(name);
			if(neuname!=null&&neuname.length()>1)
				name = neuname;
			model.addRow(new Object[]{name, spieler});
		}
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent event) {
		    	String nameclient = list.get(table.getSelectedRow()-1);
		    	ChangeClient change = new ChangeClient(nameclient, client);
		    	
		        if (table.getSelectedRow() > -1) {
		            // print first column value from selected row
		            System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());

		        }
		    }
		});
	}

	@Override
	public void kick() {
		thread1.stop();
		
	}
}
