public class Safe 
{
	boolean electronicLock, physicalLock,moving;
	float tempIn,tempExt,airP,hum,comboRPM,currentWeight;
	Thermometer therm = new Thermometer();
	AirPressure airPSensor = new AirPressure();
	Humidity humSensor = new Humidity();
	ComboSensor combo = new ComboSensor(0);
	Scale scale = new Scale(10);
	Accelerometer accel = new Accelerometer();
	
	public Safe ()
	{
		electronicLock = true;
		physicalLock = true;
		moving = false; 
		tempIn = therm.getIntTemp();
		tempExt = therm.getExtTemp();
		airP = airPSensor.getAirPressure();
		hum = humSensor.getHum();
		comboRPM = 0;
		currentWeight = scale.getWeight();
	}
	public void monitor()
	{
		if (tempIn != therm.getIntTemp())
		{
		}
	}
}
