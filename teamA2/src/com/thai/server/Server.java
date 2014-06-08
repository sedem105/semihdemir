package com.thai.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFrame;

import com.google.gson.Gson;
import com.thai.client.*;

public class Server {

	private static final int PORT = 9001;

    /**
     * The set of all names of clients in the chat room.  Maintained
     * so that we can check that new clients are not registering name
     * already in use.
     */
    private static HashSet<String> names = new HashSet<String>();

    /**
     * The set of all the print writers for all the clients.  This
     * set is kept so we can easily broadcast messages.
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    public void runServer() throws IOException{
    	
    	
    	
    	   ServerSocket listener = new ServerSocket(PORT);
    	   try {
          	 
               while (true) {
                  
                   new Handler(listener.accept()).start();
              
               }
             
           } finally {
           
               listener.close();
              
           }
    	   
    	   
           
    }
   

    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for a dealing with a single client
     * and broadcasting its messages.
     */
    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Services this thread's client by repeatedly requesting a
         * screen name until a unique one has been submitted, then
         * acknowledges the name and registers the output stream for
         * the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        public void run() {
            try {

                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                    out.println("SUBMITNAME");
                    
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                      
                            break;
                        }
                    }
                }

                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                out.println("NAMEACCEPTED");
                writers.add(out);
                List<String>clientarray = new ArrayList<String>();
              
                
                for(String client : names){
                	
              	  clientarray.add(client);
                }
                
                
                List<String>newarray = new ArrayList<String>();
               
                int size = clientarray.size()-1;
                while(size>=0){
                	
                	newarray.add(clientarray.get(size));
                	size-=1;
                }
                  ServerInfo info = new ServerInfo();
                  info.setList(newarray);
                  Gson gson = new Gson();
                  String jsoninfo = gson.toJson(info);
                  for (PrintWriter writer : writers) {
                  	
                      writer.println("Json"+jsoninfo);
                    
                  }
               
                while (true) {
                    String input = in.readLine();
                  
                    
                  
                    if (input == null) {
                        return;
                    }
                    
                    for (PrintWriter writer : writers) {
                     
                    	if(input.startsWith("Kick"))
                    	{
                    	
                    		
                                     	
                            
                    		writer.println(input);   
                    		
                             
                      
                                 
                             
                    	}
                    	else{
                    		
                    		Gson g = new Gson();
                    		Message newmess = new Message();
                    		newmess.setName(name);
                    		newmess.setInfo(input);
                    		String json = g.toJson(newmess);
                    		writer.println("MESSAGE " + json);
                    	}
                    	
                        
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
