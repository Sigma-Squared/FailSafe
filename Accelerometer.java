public class Accelerometer extends Sensor
{
	protected float accel, accelLimit;
	public Accelerometer (float lim)
	{
		super();
		accel = 0;
		accelLimit = lim;
	}
	public void setAccelLim (float lim)
	{
		accelLimit = lim;
	}
	public float getAccelLim ()
	{
		return accelLimit;
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
