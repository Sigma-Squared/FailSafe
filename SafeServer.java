package safepackage;

import java.io.*;
import java.net.*;

public class SafeServer {
	
	private static ObjectOutputStream outStream;
    private static ObjectInputStream inStream;
    private static ServerSocket serverSocket;
    private static Socket socket;
    
    private static final int PORT = 2222;
    private static final String AUTH_MSG = "M3C2015";
    
    public static boolean authenticated;

	public static void initSafeServer() {
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
					terminateConnections();
					break;
			}
			//e.printStackTrace(); 
			System.out.println("Session terminated");
			System.exit(0);
		}
	}
	
	private static void initServer() throws IOException, BindException{
		System.out.println("Opening server port " + PORT);
		serverSocket = new ServerSocket(PORT);
		System.out.println("Awaiting connection...");
		socket = serverSocket.accept();
		socket.setReuseAddress(true);
		System.out.println("Connection established");
	}
	
	private static void establishStreams() throws IOException {
		System.out.println("Enabling output steam");
		outStream = new ObjectOutputStream(socket.getOutputStream()); 
		outStream.flush();
		System.out.println("Enabling input steam");
		inStream = new ObjectInputStream(socket.getInputStream()); 
	}
	
	private static void authenticate() throws IOException, ClassNotFoundException {
		String datum = (String) inStream.readObject();
		System.out.println("Authentication: " +  datum);
		authenticated = datum.equals(AUTH_MSG);
		if (authenticated) {
			System.out.println("Authentication accepted");
			sendToDevice(AUTH_MSG);
		} else {
			System.err.println("Authentication failed.  Closing connections");
			terminateConnections();
		}
	}
	
	public static void receiveFromDevice(Safe safe){
		if (!authenticated) {
			System.err.println("Device not authenticated");
			return;
		}
		
		while (true) {
			try {
				String datum = (String) inStream.readObject();
				System.out.println("Receiving message: " +  datum);
				if (datum.substring(0, 2).equals("GL")){
					if (safe.getElectricalLock())
						sendToDevice("1");
					else
						sendToDevice("0");
				} else if (datum.substring(0, 2).equals("GS")){
					if (safe.getStatus() == 0)
						sendToDevice("2");
					else if (safe.getStatus() == 1)
						sendToDevice("1");
					else
						sendToDevice("0");
				} else if (datum.substring(0, 2).equals("GV")) {
					int value = Integer.parseInt(datum.substring(2));
					if (value == 1){
						if (!safe.therm.isActive())
							sendToDevice("<Sensor Offline>");
						else
							sendToDevice("Int. Temp: " + String.valueOf(safe.therm.getIntTemp()));
					} else if (value == 4){
						if (!safe.therm.isActive())
							sendToDevice("<Sensor Offline>");
						else
						sendToDevice("Ext. Temp: " + String.valueOf(safe.therm.getExtTemp()));
					} else if (value == 2){
						if (!safe.airPSensor.isActive())
							sendToDevice("<Sensor Offline>");
						else
						sendToDevice("Pressure: " + String.valueOf(safe.airPSensor.getAirPressure()));
					} else if (value == 3){
						if (!safe.humSensor.isActive())
							sendToDevice("<Sensor Offline>");
						else
						sendToDevice("Humidity: " + String.valueOf(safe.humSensor.getHum()));
					} else if (value == 5){
						if (!safe.scale.isActive())
							sendToDevice("<Sensor Offline>");
						else
							sendToDevice("Weight: " + String.valueOf(safe.scale.getWeight()));
					} else if (value == 6){
						if (!safe.gps.isActive())
							sendToDevice("<Sensor Offline>");
						else
							sendToDevice("GPS: " + String.valueOf(safe.gps.getLongi()) + ", " + String.valueOf(safe.gps.getLat()));
					} else {
						sendToDevice("Unknown");
					}
				} 
				
				if (datum.substring(0, 2).equals("SL")){
					if (datum.charAt(3) == '1')
						safe.electronicLock = true;
					else
						safe.electronicLock = false;
				} else if (datum.substring(0, 2).equals("SS")){
					int value = Integer.parseInt(datum.substring(3));
					safe.changeSecSettings(2-value);
				}
				
			} catch (Exception e){
				System.err.println("Unable to receive data");
				SafeServer.terminateConnections();
				return;
			}
		}
	}
	
	public static void sendToDevice(String message){
		if (!authenticated) {
			System.err.println("Authentication error; Unable to send message:");
			System.err.println(message);
			return;
		}
		try {
			System.out.println("Sending message: " + message);
			outStream.writeObject(message);
			outStream.flush();
		} catch (IOException e) {
			System.err.println("Unable to send message");
		}
	}
	
	public static void terminateConnections() {
		try {
			System.err.println("Terminating connections...");
			inStream.close();
        	outStream.close();
        	socket.close();
        	System.err.println("Connections terminated");
        	//System.err.println("Starting Server");
        	//SafeServer.initSafeServer();
		} catch (IOException e) {
			System.err.println("Unable to terminate combinations");
		}
    }   

}
