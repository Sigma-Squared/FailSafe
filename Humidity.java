package safepackage;

public class Humidity extends Sensor
{
	protected float hum,initial; // percent water
	public Humidity ()
	{
		super();
		hum = 70;
		initial = 70;
	}
	public Humidity (float hum)
	{
		super();
		this.hum = hum;
		initial = hum;
	}
	public float getHum ()
	{
		return hum;
	}
	public void setHum (float hum)
	{
		this.hum = hum;
	}
	public float getInitial ()
	{
		return initial;
	}
}
