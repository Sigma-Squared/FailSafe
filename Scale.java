public class Scale extends Sensor
{
	float currentWeight;
	public Scale (float weight)
	{
		super();
		currentWeight = weight;
	}
	public float getWeight ()
	{
		return currentWeight;
	}
	public void setWeight (float newWeight)
	{
		currentWeight = newWeight;
	}
}
