public class Humidity extends Sensor
{
	protected float hum; // percent water
	public Humidity ()
	{
		super();
		hum = 70;
	}
	public Humidity (float hum)
	{
		super();
		this.hum = hum;
	}
	public float getHum ()
	{
		return hum;
	}
	public void setHum (float hum)
	{
		this.hum = hum;
	}
}
