package safepackage;

public class Scale extends Sensor
{
	float currentWeight,initial;
	public Scale (float weight)
	{
		super();
		currentWeight = weight;
		initial = weight;
	}
	public float getWeight ()
	{
		return currentWeight;
	}
	public void setWeight (float newWeight)
	{
		currentWeight = newWeight;
	}
	public String getInitial()
	{
		return String.valueOf(initial);
	}
}
