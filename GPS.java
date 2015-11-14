public class GPS 
{
	float longi,lat;
	public GPS ()
	{
		longi = (float) 43.262285;
		lat = (float) -79.920341;
	}
	public GPS (float longi,float lat)
	{
		this.longi = longi;
		this.lat = longi;
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
}
