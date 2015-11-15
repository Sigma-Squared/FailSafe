package safepackage;

public class AirPressure extends Sensor
{
	protected float airP,initial; //stored in atm
	public AirPressure ()
	{
		super();
		airP = 1;
		initial = 1;
	}
	public AirPressure (float pressure)
	{
		super();
		airP = pressure;
		initial = pressure;
	}
	public void chngAirPressure (float pressure)
	{
		airP = pressure; 
	}
	public float getAirPressure ()
	{
		return airP;
	}
	public float getInitial ()
	{
		return initial;
	}
}
