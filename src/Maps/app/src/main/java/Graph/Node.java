package Graph;
import java.util.ArrayList;
public class Node
{
	public String name;
	public String FriendlyName = "";
	public boolean Enabled = true;
	public double distance = Integer.MAX_VALUE;
	public Node previous = null;
	public Structure object;
	public ArrayList<Neighbor> Neighbors = new ArrayList<Neighbor>();
	public int x;
	public int y;
	public int z;
	public Node(String _nam)
	{
		name = _nam;
		object = new Structure(this);
	}
	public void add(Node _nod, double _dis, boolean reversible)
	{
		if (Neighbors.size() == 0)
		{
			Neighbors.add(new Neighbor(_nod, _dis));
		}
		else
		{
			boolean added = false;
			for (int i = 0; i < Neighbors.size(); i++)
			{
				if (Neighbors.get(i).distance > _dis)
				{
					if (added == false)
					{
						added = true;
						Neighbors.add(i, new Neighbor(_nod, _dis));
						break;
					}
				}
			}
			if (added == false)
			{
				Neighbors.add(new Neighbor(_nod, _dis));
			}
		}
		if (reversible)
		{
			_nod.add(this, _dis, false);
		}
	}
	public void add(Node _nod, double _dis, String _dir, boolean reversible)
	{
		if (Neighbors.size() == 0)
		{
			Neighbors.add(new Neighbor(_nod, _dis, _dir));
		}
		else
		{
			boolean added = false;
			for (int i = 0; i < Neighbors.size(); i++)
			{
				if (Neighbors.get(i).distance > _dis)
				{
					if (added == false)
					{
						added = true;
						Neighbors.add(i, new Neighbor(_nod, _dis, _dir));
						break;
					}
				}
			}
			if (added == false)
			{
				Neighbors.add(new Neighbor(_nod, _dis, _dir));
			}
		}
		if (reversible)
		{
			_nod.add(this, _dis, false);
		}
	}
	public Neighbor get(String _nam)
	{
		for (int i = 0; i < Neighbors.size(); i++)
		{
			if (Neighbors.get(i).node.name.equals(_nam))
			{
				return Neighbors.get(i);
			}
		}
		return null;
	}
	public static Node[][] Grid(int x, int y)
	{
		Node[][] _return = new Node[y][x];
		for (int i = 0; i < y; i++)
		{
			for (int j = 0; j < x; j++)
			{
				_return[i][j] = new Node("(" + j + ", " + i + ")");
			}
		}
		for (int i = 0; i < y - 1; i++)
		{
			for (int j = 0; j < x; j++)
			{
				if (j < x - 1)
				{
					//_return[i][j].add(_return[i + 1][j + 1], 1.41, true);			
					//_return[i + 1][j].add(_return[i][j + 1], 1.41, true);		
				}
				//_return[i][j].add(_return[i + 1][j], 1, true);
			}
		}
		for (int i = 0; i < y; i++)
		{
			for (int j = 0; j < x - 1; j++)
			{
				//_return[i][j].add(_return[i][j + 1], 1, true);
			}
		}
		return _return;
	}
}