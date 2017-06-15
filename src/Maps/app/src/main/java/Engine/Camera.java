package Engine;
public class Camera
{
	public Vertex Location = new Vertex();
	public Vertex Rotation = new Vertex();
	private Point MousePreviousPosition;
	public boolean FreeFloating = true;
	public Camera()
	{

	}
	public Camera(Vertex _location, Vertex _rotation)
	{
		Location = new Vertex(_location);
		Rotation = new Vertex(_rotation);
	}
	public void SetMousePreviousPosition(Point MouseLocation)
	{
		MousePreviousPosition = new Point(MouseLocation);
	}
	public void Forward(double factor)
	{
		double radian = Rotation.Z * Math.PI / 180;
		double x = Math.sin(radian) * factor;
		double z = Math.cos(radian) * factor;
		Location.X -= x;
		Location.Z += z;
		radian = Rotation.X * Math.PI / 180;
		if (FreeFloating == true)
		{
			double y = Math.sin(radian) * factor;
			Location.Y += y;
		}
	}
	public void Right(double factor)
	{
		double radian = (Rotation.Z - 90) * Math.PI / 180;
		double x = Math.sin(radian) * factor;
		double z = Math.cos(radian) * factor;
		Location.X -= x;
		Location.Z += z;
		radian = (Rotation.X - 90) * Math.PI / 180;
	}
	public Vertex GetForwardPoint(double factor)
	{
		Vertex _return = new Vertex();
		double radian = Rotation.Z * Math.PI / 180;
		double x = Math.sin(radian) * factor;
		double z = Math.cos(radian) * factor;
		_return.X = Location.X - x;
		_return.Z = Location.Z + z;
		radian = Rotation.X * Math.PI / 180;
		double y = Math.sin(radian) * factor;
		_return.Y = Location.Y + y;
		return _return;
	}
}