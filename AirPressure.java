public class AirPressure extends Sensor
{
	float airP; //stored in atm
	public AirPressure ()
	{
		super();
		airP = 1;
	}
	public AirPressure (float pressure)
	{
		super();
		airP = pressure;
	}
	public void chngAirPressure (float pressure)
	{
		airP = pressure; 
	}
	public float getAirPressure ()
	{
		return airP;
	}
}
