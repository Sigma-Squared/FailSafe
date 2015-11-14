
public class Sensor 
{
	protected boolean active;
	public Sensor ()
	{
		this.active = true;
	}
	protected void disable ()
	{
		this.active = false;
	}
	protected void activate ()
	{
		this.active = true;
	}
	public boolean isActive ()
	{
		return active;
	}
}
