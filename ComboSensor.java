public class ComboSensor extends Sensor
{
	float RPM,speedMin;
	public ComboSensor (float speedMin)
	{
		super();
		RPM = 0;
		this.speedMin = speedMin;
	}
	public float getRPM ()
	{
		return RPM;
	}
	public void changeMin (float speedMin)
	{
		this.speedMin = speedMin;
	}
	public void opening (float RPM)
	{
		this.RPM = RPM; 
	}
}
