package safepackage;

public class Thermometer extends Sensor
{
	protected float currentIntTemp, currentExtTemp,initialIntTemp,initialExtTemp;
	public Thermometer()
	{
		super();
		this.currentIntTemp = 20;
		this.currentExtTemp = 20;
		this.initialIntTemp = 20;
		this.initialExtTemp = 20;
	}
	public Thermometer(float currentIntTemp,float currentExtTemp)
	{
		super();
		this.currentIntTemp = currentIntTemp;
		this.currentExtTemp = currentExtTemp;
		this.initialIntTemp = currentIntTemp;
		this.initialExtTemp = currentExtTemp;
	}
	public float getIntTemp ()
	{
		return currentIntTemp;
	}
	public float getExtTemp ()
	{
		return currentExtTemp;
	}
	public void setIntTemp (float temp)
	{
		currentIntTemp = temp;
	}
	public void setExtTemp (float temp)
	{
		currentExtTemp = temp;
	}
	public String getInitial ()
	{
		String initial = String.valueOf(initialIntTemp) + ", " + String.valueOf(initialExtTemp);
		return initial;
	}
}
