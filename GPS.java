package safepackage;

public class GPS extends Sensor
{
	float longi,lat,initialLongi,initialLat;
	public GPS ()
	{
		longi = (float) 43.262285;
		lat = (float) -79.920341;
		initialLongi = (float) 43.262285;
		initialLat = (float) -79.920341;
	}
	public GPS (float longi,float lat)
	{
		this.longi = longi;
		this.lat = longi;
		initialLongi = longi;
		initialLat = lat;
	}
	public float getLat ()
	{
		return lat;
	}
	public float getLongi ()
	{
		return longi;
	}
	public void setLat(float newLat)
	{
		lat = newLat;
	}
	public void setLongi (float newLongi)
	{
		longi = newLongi;
	}
	public float [] getInitial ()
	{
		return new float [] {initialLongi, initialLat};
	}
}
