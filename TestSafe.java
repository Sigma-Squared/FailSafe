package safepackage;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class TestSafe extends Applet implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Panel title, headers, dataCols, headerLeft, headerRight, data, set, sim, lockState;
    private static Label[] headerLabels;
    private static Label[] dataLabels;
    private static Label[] lockLabels;
    private static TextField [] setLabels;
    private static Button setButton;
    private static Button [] simButtons;
    
    private static boolean [] buttonStatus;
    private static boolean [] locks;
    
    private static final int VERT_PADDING = 6;
    private static final Color colourRed = new Color(255, 180, 180);
    private static final Color colourGreen = new Color(180, 255, 180);
    private boolean onFire = false;
    
    private Safe safe;
    
    @Override
    public void init() {
        resize(800, 290);
        
        safe = new Safe();
        
        initPanels();
        initLayouts();   
        initArrays();
        initHeader();
        initData();
        initSet();
        initSim();
        initColours();     
        initActionListeners();
        initFinalGraphics();
    	
    	//SafeServer.initSafeServer();
	    //System.out.println("Connection success");
       
        (new Thread() {
        	  public void run() {
        		  SafeServer.initSafeServer();
        		  System.out.println("Connection success");
 		  
        		  testSafeLoop();
        	  }
        }).start();
    }
    
    private void initPanels(){
    	title = new Panel();
        headers = new Panel(); 
        dataCols = new Panel();
        headerLeft = new Panel();
        headerRight = new Panel();
        data = new Panel(); 
        set = new Panel(); 
        sim = new Panel(); 
        lockState = new Panel();
    }
    
    //Set various layouts
    private void initLayouts(){
        title.setLayout(new FlowLayout());
        headers.setLayout(new GridLayout(1, 4));
        dataCols.setLayout(new GridLayout(1, 2, 8, 0));
        headerLeft.setLayout(new GridLayout(1, 2));
        headerRight.setLayout(new GridLayout(1, 2));
        lockState.setLayout(new GridLayout(1, 2));
        
        data.setLayout(new GridLayout(8, 1, 5, VERT_PADDING));
        set.setLayout(new GridLayout(8, 1, 5, VERT_PADDING));
        sim.setLayout(new GridLayout(8, 2, 5, VERT_PADDING/2));
    }
    
    private void initArrays(){
        title.add(new Label("Fail Safe Monitor GUI"));
        
        headerLabels = new Label [4];
        dataLabels = new Label [8];
        setLabels = new TextField [7];
        simButtons = new Button [16];
        lockLabels = new Label[2];
        
        buttonStatus = new boolean[17];
        locks = new boolean[]{true, true};
    }
    
    private void initHeader(){
    	headerLabels[0] = new Label("Readings");
        headerLabels[1] = new Label("Set State");
        headerLabels[2] = new Label("Status");        
        headerLabels[3] = new Label("Simulate");    
        
        for (byte i = 0 ; i < headerLabels.length ; i ++){
            headerLabels[i].setAlignment(Label.CENTER);
            headers.add(headerLabels[i]);
        }
    }
    
    private void initData(){
    	dataLabels[0] = new Label("Temp (Int, Ext): " + String.valueOf(safe.therm.getIntTemp()) + ", " + String.valueOf(safe.therm.getExtTemp()) + " C");
        dataLabels[1] = new Label("Air Pressure: " + String.valueOf(safe.airPSensor.getAirPressure()) + " atm");
        dataLabels[2] = new Label("Humidity: " + String.valueOf(safe.humSensor.getHum()) + " %");
        dataLabels[3] = new Label("Weight: " + String.valueOf(safe.scale.getWeight()) + " lb");
        dataLabels[4] = new Label("Combo RPM: " + String.valueOf(safe.combo.getRPM()) + " RPM");
        dataLabels[5] = new Label("Acceleration: " + String.valueOf(safe.accel.getAccel()) + " m/s^2");
        dataLabels[6] = new Label("GPS: " + String.valueOf(safe.gps.getLongi()) + String.valueOf(", ") + String.valueOf(safe.gps.getLat()));
        
        for (byte i = 0 ; i < dataLabels.length-1 ; i ++){
        	dataLabels[i].setAlignment(Label.CENTER);
            data.add(dataLabels[i]);
        }
        
        lockLabels[0] = new Label("Physical");
        lockLabels[0].setAlignment(Label.CENTER);
        lockLabels[0].setBackground(Color.GREEN);
        lockLabels[1] = new Label("Electronic");
        lockLabels[1].setAlignment(Label.CENTER);
        lockLabels[1].setBackground(Color.GREEN);
        
        lockState.add(lockLabels[0]);
        lockState.add(lockLabels[1]);
        
        data.add(lockState);
    }
    
    private void initSet(){
    	setLabels[0] = new TextField(String.valueOf(safe.therm.getIntTemp())+","+String.valueOf(safe.therm.getExtTemp()), 2);
        setLabels[1] = new TextField(String.valueOf(safe.airPSensor.getAirPressure()), 2);
        setLabels[2] = new TextField(String.valueOf(safe.humSensor.getHum()), 2);
        setLabels[3] = new TextField(String.valueOf(safe.scale.getWeight()), 2);
        setLabels[4] = new TextField(String.valueOf(safe.combo.getRPM()), 2);
        setLabels[5] = new TextField(String.valueOf(safe.accel.getAccel()), 2);
        setLabels[6] = new TextField(String.valueOf(safe.gps.getLongi())+", "+String.valueOf(safe.gps.getLat()), 2);
        
        for (byte i = 0 ; i < setLabels.length ; i ++){
        	setLabels[i].setColumns(6);
        	//setLabels[i].setAlignment(TextField.CENTER_ALIGNMENT);
            set.add(setLabels[i]);
        }
        
        setButton = new Button("Set Values");
        setButton.setBackground(new Color(200, 255, 200));
        set.add(setButton);
    }
    
    private void initSim(){
    	simButtons[0] = new Button("Thermometer");
        simButtons[2] = new Button("Barometer");
        simButtons[4] = new Button("Hydrometer");
        simButtons[6] = new Button("Scale");
        simButtons[8] = new Button("RPM Sensor");
        simButtons[10] = new Button("Accelerometer");
        simButtons[12] = new Button("GPS");
        simButtons[14] = new Button("<Reset>");
        
        simButtons[1] = new Button("Fire");
        simButtons[3] = new Button("Drill");
        simButtons[5] = new Button("Flood");
        simButtons[7] = new Button("Elevator");
        simButtons[9] = new Button("Neil Caffrey");
        simButtons[11] = new Button("Throw");
        simButtons[13] = new Button("Relocate");
        simButtons[15] = new Button("Lock Down");
        
        for (byte i = 0 ; i < simButtons.length ; i ++){
        	//setLabels[i].setAlignment(TextField.CENTER_ALIGNMENT);
        	if (i % 2 == 0)
        		simButtons[i].setBackground(colourGreen);
        	else
        		simButtons[i].setBackground(new Color(153, 204, 255));
            sim.add(simButtons[i]);
        }
    }
    
    private void initColours(){
    	title.setBackground(new Color(48, 48, 48));
        title.setForeground(Color.WHITE);
        headers.setBackground(new Color(60, 60, 60));
        headers.setForeground(Color.WHITE);
        headerLeft.setBackground(new Color(90, 90, 90));
        headerRight.setBackground(new Color(70, 70, 70));
        dataCols.setBackground(new Color(60, 60, 60));
        dataCols.setForeground(Color.WHITE);
        set.setForeground(Color.BLACK);
        headerRight.setForeground(Color.BLACK);
        lockState.setForeground(Color.BLACK);
    }
    
    private void initFinalGraphics(){
    	 dataCols.add(headerLeft);
         dataCols.add(headerRight);
         
         headerLeft.add(data);
         headerLeft.add(set);
         headerRight.add(sim);
         
         setLayout(new BorderLayout());

         add(title, "North");
         add(headers, "Center");
         add(dataCols, "South");
    }
    
    private void initActionListeners(){
    	setButton.addActionListener(this); 
    	for (byte i = 0 ; i < simButtons.length ; i ++)
    		simButtons[i].addActionListener(this);
    }
    
    private void testSafeLoop(){
    	(new Thread() {
      	  public void run() {
      		  SafeServer.receiveFromDevice(safe);;
      	  }
      }).start();
    	
    	for (;;) {
    		safe.monitor();
    		if (onFire) safe.therm.setExtTemp(safe.therm.getExtTemp()+5);
    		refreshDataLabels();
    		
    		try {Thread.sleep(1000);} catch (Exception e) {}
    	}
    }
    
    public void refreshDataLabels(){
    	dataLabels[0].setText("Temp (Int, Ext): " + String.valueOf(safe.therm.getIntTemp()) + ", " + String.valueOf(safe.therm.getExtTemp()) + " C");
        dataLabels[1].setText("Air Pressure: " + String.valueOf(safe.airPSensor.getAirPressure()) + " atm");
        dataLabels[2].setText("Humidity: " + String.valueOf(safe.humSensor.getHum()) + " %");
        dataLabels[3].setText("Weight: " + String.valueOf(safe.scale.getWeight()) + " lb");
        dataLabels[4].setText("Combo RPM: " + String.valueOf(safe.combo.getRPM()) + " RPM");
        dataLabels[5].setText("Acceleration: " + String.valueOf(safe.accel.getAccel()) + " m/s^2");
        dataLabels[6].setText("GPS: " + String.valueOf(safe.gps.getLongi()) + String.valueOf(", ") + String.valueOf(safe.gps.getLat()));
    
        for (byte i = 0 ; i < 13 ; i += 2){
        	if (buttonStatus[i])
        		dataLabels[i/2].setText("???");
        }
        
        setLabels[0].setText(String.valueOf(safe.therm.getIntTemp()) + ", " + String.valueOf(safe.therm.getExtTemp()));
        setLabels[1].setText(String.valueOf(safe.airPSensor.getAirPressure()));
        setLabels[2].setText(String.valueOf(safe.humSensor.getHum()));
        setLabels[3].setText(String.valueOf(safe.scale.getWeight()));
        setLabels[4].setText(String.valueOf(safe.combo.getRPM()));
        setLabels[5].setText(String.valueOf(safe.accel.getAccel()));
        setLabels[6].setText(String.valueOf(safe.gps.getLongi()) + String.valueOf(", ") + String.valueOf(safe.gps.getLat()));
    
        for (byte i = 0 ; i < 2 ; i ++){
        	if (locks[i]){
            	lockLabels[i].setBackground(Color.GREEN);
            } else {
            	lockLabels[i].setBackground(Color.RED);
            }
        }
    }
    
    public float stof(String value){
    	return Float.parseFloat(value);
    }
    
    //Called whenever an action is performed
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == setButton){ // Set Values
    		try {
    			String temp = setLabels[0].getText();
    			int tempComma = temp.indexOf(',');
    		
    			safe.therm.setIntTemp(stof(temp.substring(0, tempComma)));
    			safe.therm.setIntTemp(stof(temp.substring(tempComma+1)));
    			safe.airPSensor.chngAirPressure(stof(setLabels[1].getText()));
    			safe.humSensor.setHum(stof(setLabels[2].getText()));
    			safe.scale.setWeight(stof(setLabels[3].getText()));
    			safe.combo.opening(stof(setLabels[4].getText()));
    			safe.accel.moving(stof(setLabels[5].getText()));
    		
    			String gps = setLabels[6].getText();
    			int gpsComma = gps.indexOf(','); 		
    			safe.gps.setLongi(stof(gps.substring(0, gpsComma)));
    			safe.gps.setLat(stof(gps.substring(gpsComma+1)));
    		
    			refreshDataLabels();
    		} catch (NumberFormatException nfe){
    			showStatus("Parsing error has occurred.");
    		}
    	} 
    	else if (e.getSource() == simButtons[0]){ // Disable Thermometer 		
    		if (!buttonStatus[0]){
    			safe.therm.disable();
    			simButtons[0].setBackground(colourRed);
    			dataLabels[0].setText("???");
    		} else {
    			safe.therm.activate();
    			simButtons[0].setBackground(colourGreen);	
    			dataLabels[0].setText("Temp (Int, Ext): " + String.valueOf(safe.therm.getIntTemp()) + ", " + String.valueOf(safe.therm.getExtTemp()) + " C");
    		}
    		buttonStatus[0] = !buttonStatus[0];
    	} 
    	else if (e.getSource() == simButtons[2]){ // Disable Air Pressure
    		if (!buttonStatus[2]){
    			dataLabels[1].setText("???");
    			safe.airPSensor.disable();
    			simButtons[2].setBackground(colourRed);
    		} else {
    			dataLabels[1].setText("Air Pressure: " + String.valueOf(safe.airPSensor.getAirPressure()) + " atm");
    			safe.airPSensor.activate();
    			simButtons[2].setBackground(colourGreen);
    		}
    		buttonStatus[2] = !buttonStatus[2];
    	} 
    	else if (e.getSource() == simButtons[4]){ // Disable Humidity
    		if (!buttonStatus[4]){
    			dataLabels[2].setText("???");
    			safe.humSensor.disable();
    			simButtons[4].setBackground(colourRed);
    		} else {
    			dataLabels[2].setText("Humidity: " + String.valueOf(safe.humSensor.getHum()) + " %");    			
    			safe.humSensor.activate();
    			simButtons[4].setBackground(colourGreen);
    		}
    		buttonStatus[4] = !buttonStatus[4];
    	} 
    	else if (e.getSource() == simButtons[6]){ // Disable Scale
    		if (!buttonStatus[6]){
    			dataLabels[3].setText("???");
    			safe.scale.disable();
    			simButtons[6].setBackground(colourRed);
    		} else {
    			dataLabels[3].setText("Weight: " + String.valueOf(safe.scale.getWeight()) + " lb");
    			safe.scale.activate();
    			simButtons[6].setBackground(colourGreen);
    		}
    		buttonStatus[6] = !buttonStatus[6];
    	} 
    	else if (e.getSource() == simButtons[8]){ // Disable RPM Sensor
    		if (!buttonStatus[8]){
    			dataLabels[4].setText("???");
    			safe.combo.disable();
    			simButtons[8].setBackground(colourRed);
    		} else {
    			dataLabels[4].setText("Combo RPM: " + String.valueOf(safe.combo.getRPM()) + " RPM");
    			safe.combo.activate();
    			simButtons[8].setBackground(colourGreen);
    		}
    		buttonStatus[8] = !buttonStatus[8];
    	}
    	else if (e.getSource() == simButtons[10]){ // Disable Accelerometer
    		if (!buttonStatus[10]){
    			dataLabels[5].setText("???");
    			safe.accel.disable();
    			simButtons[10].setBackground(colourRed);
    		} else {
    			dataLabels[5].setText("Acceleration: " + String.valueOf(safe.accel.getAccel()) + " m/s^2");
    			safe.accel.activate();
    			simButtons[10].setBackground(colourGreen);
    		}
    		buttonStatus[10] = !buttonStatus[10];
    	}
    	else if (e.getSource() == simButtons[12]){ // Disable GPS
    		if (!buttonStatus[12]){
    			dataLabels[6].setText("???");
    			safe.gps.disable();
    			simButtons[12].setBackground(colourRed);
    		} else {
    			dataLabels[6].setText("GPS: " + String.valueOf(safe.gps.getLongi()) + String.valueOf(", ") + String.valueOf(safe.gps.getLat()));
    			safe.gps.activate();
    			simButtons[12].setBackground(colourGreen);
    		}
    		buttonStatus[12] = !buttonStatus[12];
    	}
    	
    	else if (e.getSource() == simButtons[1]){
    		onFire = true;
    	}
    	else if (e.getSource() == simButtons[3]){
    		safe.airPSensor.chngAirPressure(2.0f);
    	}
    	else if (e.getSource() == simButtons[5]){
    		safe.humSensor.setHum(100f);
    	}
    	else if (e.getSource() == simButtons[7]){
    		safe.scale.setWeight(3.14f);
    	}
    	else if (e.getSource() == simButtons[9]){
    		safe.combo.opening(0.2f);
    	}
    	else if (e.getSource() == simButtons[11]){
    		safe.gps.setLongi(39.15386f);
    		safe.gps.setLongi(81.03381f);
    	}
    }

    //Sends data to the Control class
    public void sendData() {
        //Control.receiveInitParams();
    }
}