package safepackage;

public class Safe 
{
	protected boolean electronicLock, physicalLock,moving,connected,open,lockdown,pluggedIn;
	boolean tempInSent,tempExtSent,airPSent,humSent,comboRPMSent,currentWeightSent,moved,accelSent,RPMSent,pluggedInSent;
	boolean thermSent,airPSensorSent,humSensorSent,comboSent,scaleSent,accelSensorSent,gpsSent;
	boolean full,secure,unsecure;
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
		full = true;
		connected = true;
		electronicLock = true;
		physicalLock = true;
		moving = false;
		pluggedIn = true;
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
	public int getStatus ()
	{
		int s=0;
		if (full == true)
		{
			s=0;
		}
		else if (secure == true)
		{
			s=1;
		}
		else if (unsecure == true)
		{
			s=2;
		}
		return s;
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
				if (full == true)
				{
					SafeServer.sendToDevice ("Warning: The safe has been unplugged\n" + batteryLevel/3600 + " hours left");
				}
				pluggedInSent = true;
			}
			batteryLevel--;
		}
		if (tempIn != therm.getIntTemp() && therm.isActive())
		{
			if (tempInSent==false)
			{
				if (full == true)
				{
					SafeServer.sendToDevice("Warning: Change in internal temperature.");
				}
				tempInSent = true;
			}
			tempIn = therm.getIntTemp();
		}
		if (tempExt != therm.getIntTemp() && therm.isActive())
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
					if (full == true)
					{
					SafeServer.sendToDevice("Warning: Abnormal external temperature influx.");
					}
					tempExtSent = true;
				}
			}
			tempExt = therm.getExtTemp();
		}
		if (accel.getAccel() > Math.abs(accel.getAccelLim()) || accel.getAccel()< -1*Math.abs(accel.getAccelLim()) && accel.isActive())
		{
			if (accelSent==false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice("Warning: Safe is being handled.");
				}
				accelSent = true;
			}
		}
		if (combo.getRPM() < combo.getLim() && combo.isActive())
		{
			if (RPMSent == false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice ("Warning: Physical unlock taking longer than normal.");
				}
				RPMSent = true;
			}
		}
		if (hum != humSensor.getHum() && humSensor.isActive())
		{
			if (humSent == false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice ("Warning: Safe humidity has changed.");
				}
				humSent = true;
			}
			hum = humSensor.getHum ();
		}
		if (airP != airPSensor.getAirPressure()&& airPSensor.isActive())
		{
			if (airPSent == false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice ("Warning: Internal air pressure has changed");
				}
				airPSent = true;
			}
		}
		if (longi != gps.getLongi() || lat !=gps.getLat()&& gps.isActive())
		{
			if (moved == false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice ("Warning: The safe has been moved");
				}
				moved = true;
			}
		}
		if (currentWeight != scale.getWeight() && scale.isActive())
		{
			if (currentWeight > scale.getWeight())
			{
				if (currentWeightSent == false)
				{
					if (full == true)
					{
					SafeServer.sendToDevice("Warning: Contents have been added to the safe.");
					}
					currentWeightSent = true;
				}
			}
			else
			{
				if (currentWeightSent == false)
				{
					if (full == true)
					{
					SafeServer.sendToDevice("Warning: Contents have been removed from the safe.");
					}
					currentWeightSent = true;
				}
			}
			currentWeight = scale.getWeight();
		}
		if (!therm.isActive())
		{
			if (thermSent==false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice("Warning: Thermometer Offline");
				}
				thermSent=true;
			}
		}
		if (!airPSensor.isActive())
		{
			if (airPSensorSent==false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice("Warning: Air Pressure Sensor Offline");
				}
				airPSensorSent=true;
			}
		}
		if (!humSensor.isActive())
		{
			if (humSensorSent==false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice("Warning: Humidity Sensor Offline");
				}
				humSensorSent=true;
			}
		}
		if (!combo.isActive())
		{
			if (comboSent==false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice("Warning: Combination Lock Sensor Offline");
				}
				comboSent=true;
			}
		}
		if (!scale.isActive())
		{
			if (scaleSent==false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice("Warning: Internal Scale is Offline");
				}
				scaleSent=true;
			}
		}
		if (!accel.isActive())
		{
			if (accelSensorSent==false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice("Warning: Accelerometer Offline");
				}
				accelSensorSent=true;
			}
		}
		if (!gps.isActive())
		{
			if (gpsSent ==false)
			{
				if (full == true)
				{
				SafeServer.sendToDevice("Warning: GPS Offline");
				}
				gpsSent = true;
			}
		}
		cycleCnt++;
		if (cycleCnt == 20)
		{
			cycleCnt=0;
		}
	}
	public void electricalUnlock ()
	{
		if (lockdown == false)
		{
			electronicLock = false;
		}
	}
	public void initiateLockdown ()
	{
		full = true;
		secure = false;
		unsecure = false;
		lockdown = true;
		electronicLock = true;
		physicalLock = true;
		SafeServer.sendToDevice("Warning: Lockdown Initiated");
	}
	public void endLockdown ()
	{
		lockdown = false;
	}
	public void incinerateContents()
	{
		if (open == false)
		{
			therm.setIntTemp(666);
		}
		else
		{
			if (full == true)
			{
			SafeServer.sendToDevice ("Warning: Can not initiate incineration while safe is open.");
			}
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
		pluggedIn = true;
		
		full = true;
		connected = true;
		electronicLock = true;
		physicalLock = true;
		moving = false;
		pluggedIn = true;
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
	public void unplug ()
	{
		pluggedIn = false;
	}
	public void openSafe ()
	{
		if (physicalLock == false && electronicLock == false)
		{
			open = true;
			if (full == true || secure == true)
			{
			SafeServer.sendToDevice("Warning: The Safe has been opened.");
			}
		}
	}
	public void doorClose ()
	{
		physicalLock = false;
	}
	public boolean getElectricalLock ()
	{
		return electronicLock;
	}
	public boolean getPhysicalLock ()
	{
		return physicalLock;
	}
	public void changeSecSettings(int n)
	{
		if (n==1)
		{
			full=false;
			secure = true;
			unsecure = false;
		}
		else if (n==2)
		{
			full=false;
			secure = false;
			unsecure = true;
		}
		else
		{
			full=true;
			secure = false;
			unsecure = false;
		}
		
	}
}
