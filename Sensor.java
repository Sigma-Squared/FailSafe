
public class Sensor 
{
	protected boolean active;
	public Sensor ()
	{
		this.active = true;
	}
	protected void disabled ()
	{
		this.active = false;
	}
}
