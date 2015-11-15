package safepackage;

public class ComboSensor extends Sensor
{
	protected float RPM,speedMin;
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
	public float getLim ()
	{
		return speedMin;
	}
	public void changeMin (float speedMin)
	{
		this.speedMin = speedMin;
	}
	public void opening (float RPM)
	{
		this.RPM = RPM; 
	}
	public String getInitial ()
	{
		return "0";
	}
}
