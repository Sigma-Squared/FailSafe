public class Accelerometer extends Sensor
{
	float accel;
	public Accelerometer ()
	{
		super();
		accel = 0;
	}
	public float getAccel()
	{
		return accel;
	}
	public void moving (float chng)
	{
		accel = chng;
	}
	public void notMoving ()
	{
		accel = 0;
	}
}
