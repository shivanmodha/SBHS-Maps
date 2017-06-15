package Graph;
public class Neighbor
{
	public double distance;
	public Node node;
	public String direction = "";
	public Neighbor(Node _nod, double _dis)
	{
		node = _nod;
		distance = _dis;
	}
	public Neighbor(Node _nod, double _dis, String _dir)
	{
		node = _nod;
		distance = _dis;
		direction = _dir;
	}
	public String toString()
	{
		return distance + "";
	}
}