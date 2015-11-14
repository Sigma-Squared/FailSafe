
public class Thermometer extends Sensor
{
	protected float currentIntTemp, currentExtTemp;
	public Thermometer()
	{
		super();
		this.currentIntTemp = 20;
		this.currentExtTemp = 20;
	}
	public Thermometer(float currentIntTemp,float currentExtTemp)
	{
		super();
		this.currentIntTemp = currentIntTemp;
		this.currentExtTemp = currentExtTemp;
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
}
