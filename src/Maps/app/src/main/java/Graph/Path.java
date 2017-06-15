package Graph;
import java.util.ArrayList;
public class Path
{
	public double distance;
	public ArrayList<Node> Nodes = new ArrayList<Node>();
	public String toString()
	{
		String _return = "";
		try
		{
			for (int i = 0; i < Nodes.size() - 1; i++)
			{
				_return += Nodes.get(i).name + "<" + Nodes.get(i).get(Nodes.get(i + 1).name).distance + "> ";
			}
			_return += Nodes.get(Nodes.size() - 1).name;
		}
		catch (Exception e)
		{

		}
		return _return;
	}
	public Node GetStart()
	{
		return Nodes.get(0);
	}
	public Node GetEnd()
	{
		return Nodes.get(Nodes.size() - 1);
	}
	public void CalculateDistance()
	{
		distance = 0;
		for (int i = 0; i < Nodes.size() - 1; i++)
		{
			distance += Nodes.get(i).get(Nodes.get(i + 1).name).distance;
		}
	}
	public boolean Check(Node start, Node end)
	{
		if (Nodes.size() > 0)
		{
			if ((GetStart() == start) && (GetEnd() == end))
			{
				return true;
			}
		}
		return false;
	}
	public ArrayList<String> GetDirections()
	{
		ArrayList<String> _return = new ArrayList<String>();
		for (int i = 0; i < Nodes.size() - 1; i++)
		{
			for (int j = 0; j < Nodes.get(i).Neighbors.size(); j++)
			{
				if (Nodes.get(i).Neighbors.get(j).node == Nodes.get(i + 1))
				{
					_return.add(Nodes.get(i).Neighbors.get(j).direction);
				}
			}
		}
		return _return;
	}
	public ArrayList<Direction> GetDynamicDirections()
	{
		ArrayList<Direction> _return = new ArrayList<Direction>();
		int SUMANGLE = 0;
		String name = Nodes.get(0).FriendlyName;
		if (name.startsWith("("))
		{
			name = name.substring(4);
		}
		Vector CLocation = new Vector(0, 0, 0);
		Vector NLocation = new Vector(NodeGrid.GetNodeX(Nodes.get(1).name), NodeGrid.GetNodeY(Nodes.get(1).name), 0).subtract(new Vector(NodeGrid.GetNodeX(Nodes.get(0).name), NodeGrid.GetNodeY(Nodes.get(0).name), 0));
		double SP = CLocation.dot(NLocation);
		double MP = CLocation.magnitude() * NLocation.magnitude();
		double angle = Math.acos(SP / MP);
		int ANGLE = (int)Math.toDegrees(angle);
		Vector CP = CLocation.cross(NLocation);
		if (CP.X > 0 || CP.Y > 0 || CP.Z > 0)
		{
			ANGLE *= -1;
		}
		SUMANGLE += ANGLE;
		_return.add(new Direction("Exit " + name, SUMANGLE, Nodes.get(0).name, Nodes.get(0).name, Nodes.get(0).name));
		for (int i = 1;  i < Nodes.size() - 1; i++)
		{
			String Verb = "Towards";
			name = Nodes.get(i + 1).FriendlyName;
			if (name.startsWith("("))
			{
				Verb = "Onto";
				name = name.substring(4);
			}
			Node Parent = Nodes.get(i - 1);
			Node Current = Nodes.get(i);
			Node Next = Nodes.get(i + 1);
			Vector ParentLocation = new Vector(NodeGrid.GetNodeX(Parent), NodeGrid.GetNodeY(Parent), NodeGrid.GetNodeZ(Parent));
			Vector CurrentLocation = new Vector(NodeGrid.GetNodeX(Current), NodeGrid.GetNodeY(Current), NodeGrid.GetNodeZ(Current));
			Vector NextLocation = new Vector(NodeGrid.GetNodeX(Next), NodeGrid.GetNodeY(Next), NodeGrid.GetNodeZ(Next));
			if (CurrentLocation.Z == NextLocation.Z)
			{
				ParentLocation.Z = CurrentLocation.Z;
				NextLocation = NextLocation.subtract(CurrentLocation);
				CurrentLocation = CurrentLocation.subtract(ParentLocation);
				double ScalarProduct = CurrentLocation.dot(NextLocation);
				double MagnitudeProduct = CurrentLocation.magnitude() * NextLocation.magnitude();
				angle = Math.acos(ScalarProduct / MagnitudeProduct);
				ANGLE = (int)Math.toDegrees(angle);
				Vector CrossProduct = CurrentLocation.cross(NextLocation);
				if (CrossProduct.X > 0 || CrossProduct.Y > 0 || CrossProduct.Z > 0)
				{
					ANGLE *= -1;
					angle *= -1;
				}
				SUMANGLE += ANGLE;
				if (ANGLE >= -30 && ANGLE <= 30)
				{
					if (_return.size() <= 0 || !_return.get(_return.size() - 1).Instruction.equals("Keep Straight"))
					{
						_return.add(new Direction("Keep Straight", SUMANGLE, Parent.name, Current.name, Next.name));
					}
				}
				else if (ANGLE > 30 && ANGLE < 60)
				{
					if (_return.size() <= 0 || !_return.get(_return.size() - 1).Instruction.equals("Slightly Right " + Verb + " " + name))
					{
						_return.add(new Direction("Slightly Right " + Verb + " " + name, SUMANGLE, Parent.name, Current.name, Next.name));
					}
				}
				else if (ANGLE >= 60)
				{
					if (_return.size() <= 0 || !_return.get(_return.size() - 1).Instruction.equals("Right " + Verb + " " + name))
					{
						_return.add(new Direction("Right " + Verb + " " + name, SUMANGLE, Parent.name, Current.name, Next.name));
					}
				}
				else if (ANGLE >= -60 && ANGLE <= -30)
				{
					if (_return.size() <= 0 || !_return.get(_return.size() - 1).Instruction.equals("Slightly Left " + Verb + " " + name))
					{
						_return.add(new Direction("Slightly Left " + Verb + " " + name, SUMANGLE, Parent.name, Current.name, Next.name));
					}
				}
				else if (ANGLE <= -60)
				{
					if (_return.size() <= 0 || !_return.get(_return.size() - 1).Instruction.equals("Left " + Verb + " " + name))
					{
						_return.add(new Direction("Left " + Verb + " " + name, SUMANGLE, Parent.name, Current.name, Next.name));
					}
				}
				else
				{
					_return.add(new Direction("" + angle, SUMANGLE, Parent.name, Current.name, Next.name));
				}
			}
			else if (CurrentLocation.Z > NextLocation.Z)
			{
				if (_return.size() <= 0 || !_return.get(_return.size() - 1).Instruction.equals("Down " + name))
				{
					_return.add(new Direction("Down " + name, 0, Parent.name, Current.name, Next.name));
				}
			}
			else if (CurrentLocation.Z < NextLocation.Z)
			{
				if (_return.size() <= 0 || !_return.get(_return.size() - 1).Instruction.equals("Up " + name))
				{
					_return.add(new Direction("Up " + name, 0, Parent.name, Current.name, Next.name));
				}
			}
		}
		return _return;
	}
}