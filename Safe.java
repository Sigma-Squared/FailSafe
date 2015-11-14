public class Safe 
{
	protected boolean electronicLock, physicalLock,moving,connected,open,lockdown,pluggedIn;
	boolean tempInSent,tempExtSent,airPSent,humSent,comboRPMSent,currentWeightSent,moved,accelSent,RPMSent,pluggedInSent;
	boolean thermSent,airPSensorSent,humSensorSent,comboSent,scaleSent,accelSensorSent;
	protected float tempIn,tempExt,airP,hum,comboRPM,currentWeight,tempChange,sum,longi,lat;
	protected int cycleCnt;
	protected int batteryLevel = 604800;
	protected float recentTemp[] = new float [20];
	GPS gps = new GPS ();
	Thermometer therm = new Thermometer();
	AirPressure airPSensor = new AirPressure();
	Humidity humSensor = new Humidity();
	ComboSensor combo = new ComboSensor(0);
	Scale scale = new Scale(10);
	Accelerometer accel = new Accelerometer(0.3f);

	public Safe ()
	{
		connected = true;
		electronicLock = false;
		physicalLock = false;
		moving = false;
		tempChange = 0;
		tempIn = therm.getIntTemp();
		tempExt = therm.getExtTemp();
		airP = airPSensor.getAirPressure();
		hum = humSensor.getHum();
		longi = gps.getLongi();
		lat = gps.getLat();
		comboRPM = 0;
		currentWeight = scale.getWeight();
		for (int i = 0; i<20;i++)
		{
			recentTemp[i] = 0;
		}
	}
	public void monitor()
	{
		if (pluggedIn == true && batteryLevel < 604800)
		{
			batteryLevel++;
		}
		if (pluggedIn == false)
		{
			if (pluggedInSent == false)
			{
				SafeServer.sendToDevice ("WARN:The safe has been unplugged\n" + batteryLevel/3600 + " hours left");
				pluggedInSent = true;
			}
			batteryLevel--;
		}
		if (tempIn != therm.getIntTemp())
		{
			if (tempInSent==false)
			{
				SafeServer.sendToDevice("WARN:Change in internal temperature.");
				tempInSent = true;
			}
			tempIn = therm.getIntTemp();
		}
		if (tempExt != therm.getIntTemp())
		{
			tempChange = tempExt - therm.getExtTemp();
			recentTemp [cycleCnt] = Math.abs(tempChange);
			sum = 0;
			for (int i = 0;i<20;i++)
			{
				sum+=recentTemp[i];
			}
			if (sum > 10)
			{
				if (tempExtSent == false)
				{
					SafeServer.sendToDevice("WARN:Abnormal external temperature influx.");
					tempExtSent = true;
				}
			}
			tempExt = therm.getExtTemp();
		}
		if (accel.getAccel() > Math.abs(accel.getAccelLim()) || accel.getAccel()< -1*Math.abs(accel.getAccelLim()))
		{
			if (accelSent==false)
			{
				SafeServer.sendToDevice("WARN:Safe is being handled.");
				accelSent = true;
			}
		}
		if (combo.getRPM() < combo.getLim())
		{
			if (RPMSent == false)
			{
				SafeServer.sendToDevice ("WARN:Physical unlock taking longer than normal.");
				RPMSent = true;
			}
		}
		if (hum != humSensor.getHum())
		{
			if (humSent == false)
			{
				SafeServer.sendToDevice ("WARN:Safe humidity has changed.");
				humSent = true;
			}
			hum = humSensor.getHum ();
		}
		if (airP != airPSensor.getAirPressure())
		{
			if (airPSent == false)
			{
				SafeServer.sendToDevice ("WARN:Internal air pressure has changed");
				airPSent = true;
			}
		}
		if (longi != gps.getLongi() || lat !=gps.getLat())
		{
			if (moved == false)
			{
				SafeServer.sendToDevice ("DATA:The safe has been moved");
				moved = true;
			}
		}
		if (currentWeight != scale.getWeight() && accel.getAccel()==0)
		{
			if (currentWeight > scale.getWeight())
			{
				if (currentWeightSent == false)
				{
					SafeServer.sendToDevice("WARN:Contents have been added to the safe.");
					currentWeightSent = true;
				}
			}
			else
			{
				if (currentWeightSent == false)
				{
					SafeServer.sendToDevice("WARN:Contents have been removed from the safe.");
					currentWeightSent = true;
				}
			}
			currentWeight = scale.getWeight();
		}
		if (!therm.isActive())
		{
			if (thermSent==false)
			{
				SafeServer.sendToDevice("WARN:Thermometer Offline");
				thermSent=true;
			}
		}
		if (!airPSensor.isActive())
		{
			if (airPSensorSent==false)
			{
				SafeServer.sendToDevice("WARN:Air Pressure Sensor Offline");
				thermSent=true;
			}
		}
		if (!humSensor.isActive())
		{
			if (humSensorSent==false)
			{
				SafeServer.sendToDevice("WARN:Humidity Sensor Offline");
				thermSent=true;
			}
		}
		if (!combo.isActive())
		{
			if (comboSent==false)
			{
				SafeServer.sendToDevice("WARN:Combination Lock Sensor Offline");
				thermSent=true;
			}
		}
		if (!scale.isActive())
		{
			if (scaleSent==false)
			{
				SafeServer.sendToDevice("WARN:Internal Scale is Offline");
				thermSent=true;
			}
		}
		if (!accel.isActive())
		{
			if (accelSensorSent==false)
			{
				SafeServer.sendToDevice("WARN:Accelerometer Offline");
				thermSent=true;
			}
		}
		cycleCnt++;
		if (cycleCnt == 20)
		{
			cycleCnt=0;
		}
	}
	public void initiateLockDown ()
	{
		lockdown = true;
		electronicLock = false;
		physicalLock = false;
	}
	public void incinerateContents()
	{
		if (open == false)
		{
		therm.setIntTemp(666);
		}
		else
		{
			SafeServer.sendToDevice ("WARN:Can not initiate incineration while safe is open.");
		}
	}
	public void physicalUnlock ()
	{
		physicalLock = false;
	}
	public void lostConnection ()
	{
		connected = false;
	}
	public void resetNotifications ()
	{
		tempInSent = false;
		tempExtSent = false;
		airPSent = false;
		humSent = false;
		comboRPMSent = false;
		currentWeightSent = false;
		moved = false;
		accelSent = false;
		RPMSent = false;
		thermSent = false;
		airPSensorSent = false;
		humSensorSent = false;
		comboSent = false;
		scaleSent = false;
		accelSensorSent = false;
		pluggedIn = false;
	}
	public void unplug ()
	{
		pluggedIn = false;
	}
	public void openSafe ()
	{
		if (physicalLock == true && electronicLock == true)
		{
			open =true;
		}
	}
	public void doorClose ()
	{
		physicalLock = false;
	}
}
