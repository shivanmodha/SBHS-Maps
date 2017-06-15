package Graph;
import Engine.Vertex;
public class Structure
{
	public StructureType Type = StructureType.Building;
	public Node Center;
	public Vertex Bound_Bottom_Left = null;
	public Vertex Bound_Bottom_Right = null;
	public Vertex Bound_Top_Left = null;
	public Vertex Bound_Top_Right = null;
	public Structure(Node _center)
	{
		Center = _center;
	}
}