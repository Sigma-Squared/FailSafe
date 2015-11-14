package safepackage;

import java.io.*;
import java.net.*;

public class SafeServer {
	
	private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private ServerSocket serverSocket;
    private Socket socket;
    
    private final int PORT = 6969;
    private final String AUTH_MSG = "M3C2015";
    
    public boolean authenticated;

	public SafeServer() {
		byte errorCode = 0;
		
		try {
			initServer();
			
			errorCode = 1;
			establishStreams();
			
			errorCode = 2;
			authenticate();
			
		} catch (Exception e) {
			switch (errorCode){
				case 0:
					System.err.println("Unable to intialize server");
					break;
				case 1:
					System.err.println("Unable to establish streams");
					break;
				case 2:
					System.err.println("Unable to authenticate");
			    	authenticated = false;
			    	break;
				default:
					break;
			}
			e.printStackTrace(); 
			System.exit(0);
		}
	}
	
	private void initServer() throws IOException{
		System.out.println("Opening server port " + PORT);
		serverSocket = new ServerSocket(PORT);
		System.out.println("Awaiting connection...");
		socket = serverSocket.accept();
		System.out.println("Connection established");
	}
	
	private void establishStreams() throws IOException {
		System.out.println("Enabling output steam");
		outStream = new ObjectOutputStream(socket.getOutputStream()); 
		System.out.println("Enabling input steam");
		inStream = new ObjectInputStream(socket.getInputStream()); 
	}
	
	private void authenticate() throws IOException, ClassNotFoundException {
		String datum = (String) inStream.readObject();
		System.out.println("Authentication: " +  datum);
		authenticated = datum.equals(AUTH_MSG);
	}
	
	public String receiveFromDevice() throws IOException, ClassNotFoundException {
	    try {
	    	String datum = (String) inStream.readObject();
	    	System.out.println("Receiving: " +  datum);
	    	return datum;
	    } catch (IOException | ClassNotFoundException e){
	    	System.err.println("Unable to receive data");
	    	return null;
	    }
	}
	
	public void sendToDevice(String message) throws IOException{
		try {
			System.out.println("Transferring: " + message);
			outStream.writeObject(message);
			outStream.flush();
		} catch (IOException e) {
			System.err.println("Unable to send message");
		}
	}
	
	public void terminateConnections() {
		try {
			inStream.close();
        	outStream.close();
        	socket.close();
		} catch (IOException e) {
			System.err.println("Unable to terminate combinations");
		}
    }   

}
