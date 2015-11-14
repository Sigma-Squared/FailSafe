public class Safe 
{
	protected boolean electronicLock, physicalLock,moving,connected;
	boolean tempInSent,tempExtSent,airPSent;
	protected float tempIn,tempExt,airP,hum,comboRPM,currentWeight,tempChange,sum;
	protected int cycleCnt;
	protected float recentTemp[] = new float [20];
	Thermometer therm = new Thermometer();
	AirPressure airPSensor = new AirPressure();
	Humidity humSensor = new Humidity();
	ComboSensor combo = new ComboSensor(0);
	Scale scale = new Scale(10);
	Accelerometer accel = new Accelerometer(0.3f);
	
	public Safe ()
	{
		electronicLock = true;
		physicalLock = true;
		moving = false;
		tempChange = 0;
		tempIn = therm.getIntTemp();
		tempExt = therm.getExtTemp();
		airP = airPSensor.getAirPressure();
		hum = humSensor.getHum();
		comboRPM = 0;
		currentWeight = scale.getWeight();
		for (int i = 0; i<20;i++)
		{
			recentTemp[i] = 0;
		}
		connected = true;
	}
	public void monitor()
	{
		if (tempIn != therm.getIntTemp())
		{
			SafeServer.sendToDevice("WARN:Change in internal temperature.");
			tempIn = therm.getIntTemp();
		}
		if (tempExt != therm.getIntTemp())
		{
			tempChange = tempExt + therm.getExtTemp();
			recentTemp [cycleCnt] = Math.abs(tempChange);
			sum = 0;
			for (int i = 0;i<20;i++)
			{
				sum+=recentTemp[i];
			}
			if (sum > 10)
			{
				SafeServer.sendToDevice("WARN:Abnormal external temperature influx.");
			}
		}
		if (accel.getAccel() > Math.abs(accel.getAccelLim()) || accel.getAccel()< -1*Math.abs(accel.getAccelLim()))
		{
			SafeServer.sendToDevice("WARN:Safe is being moved.");
		}
		if (combo.getRPM() < combo.getLim())
		{
			SafeServer.sendToDevice ("WARN:Physical unlock taking longer than normal.");
		}
		if (hum != humSensor.getHum())
		{
			SafeServer.sendToDevice ("WARN:Safe humidity has changed.");
			hum = humSensor.getHum ();
		}
		if (airP != airPSensor.getAirPressure())
		{
			SafeServer.sendToDevice ("WARN:Internal air pressure has changed");
		}
		cycleCnt++;
		if (cycleCnt == 20)
		{
			cycleCnt=0;
		}
	}
	public void initiateLockDown ()
	{
		
	}
	public void incinerateContents()
	{
		
	}
	public void physicalUnlock ()
	{
		physicalLock = false;
	}
	public void lostConnection ()
	{
		connected = false;
	}
}
