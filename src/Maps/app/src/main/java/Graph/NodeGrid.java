package Graph;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class NodeGrid
{
	private Node[][][] map;
	public String Name;
	public double XDistance = 1;
	public double YDistance = 1;
	public double ZDistance = 1;
	public ArrayList<Node>[] EnabledNodes;
	private ArrayList<String> FRIENDLYNAMES = null;
	public NodeGrid(int x, int y, int z, String _name, boolean XGrid, boolean YGrid, boolean ZGrid, boolean XYGrid, boolean XZGrid, boolean YZGrid, boolean XYZGrid)
	{
		Name = _name;
		map = new Node[y][x][z];
		for (int i = 0; i < y; i++)
		{
			for (int j = 0; j < x; j++)
			{
				for (int k = 0; k < z; k++)
				{
					map[i][j][k] = new Node("(" + j + ", " + i + ", " + k + ")");
				}
			}
		}
		if (XGrid)
		{
			for (int i = 0; i < y; i++)
			{
				for (int j = 0; j < x - 1; j++)
				{
					for (int k = 0; k < z; k++)
					{
						map[i][j][k].add(map[i][j + 1][k], 1, true);
					}
				}
			}
		}
		if (YGrid)
		{
			for (int i = 0; i < y - 1; i++)
			{
				for (int j = 0; j < x; j++)
				{
					for (int k = 0; k < z; k++)
					{
						map[i][j][k].add(map[i + 1][j][k], 1, true);
					}
				}
			}
		}
		if (ZGrid)
		{
			for (int i = 0; i < y; i++)
			{
				for (int j = 0; j < x; j++)
				{
					for (int k = 0; k < z - 1; k++)
					{
						map[i][j][k].add(map[i][j][k + 1], 1, true);
					}
				}
			}
		}
		if (XYGrid)
		{
			for (int i = 0; i < y - 1; i++)
			{
				for (int j = 0; j < x - 1; j++)
				{
					for (int k = 0; k < z; k++)
					{
						map[i][j][k].add(map[i + 1][j + 1][k], 1.41, true);
						map[i + 1][j][k].add(map[i][j + 1][k], 1.41, true);
					}
				}
			}
		}
		if (XZGrid)
		{
			for (int i = 0; i < y; i++)
			{
				for (int j = 0; j < x - 1; j++)
				{
					for (int k = 0; k < z - 1; k++)
					{
						map[i][j][k].add(map[i][j + 1][k + 1], 1.41, true);
						map[i][j][k + 1].add(map[i][j + 1][k], 1.41, true);
					}
				}
			}
		}
		if (YZGrid)
		{
			for (int i = 0; i < y - 1; i++)
			{
				for (int j = 0; j < x; j++)
				{
					for (int k = 0; k < z - 1; k++)
					{
						map[i][j][k].add(map[i + 1][j][k + 1], 1.41, true);
						map[i][j][k + 1].add(map[i + 1][j][k], 1.41, true);
					}
				}
			}
		}
		if (XYZGrid)
		{
			for (int i = 0; i < y - 1; i++)
			{
				for (int j = 0; j < x - 1; j++)
				{
					for (int k = 0; k < z - 1; k++)
					{
						map[i][j][k].add(map[i + 1][j + 1][k + 1], 1.73, true);
						map[i + 1][j][k].add(map[i][j + 1][k + 1], 1.73, true);
						map[i][j + 1][k].add(map[i + 1][j][k + 1], 1.73, true);
						map[i + 1][j + 1][k].add(map[i][j][k + 1], 1.73, true);
					}
				}
			}
		}
	}
	public int GetYMax()
	{
		return map.length;
	}
	public int GetXMax()
	{
		return map[0].length;
	}
	public int GetZMax()
	{
		return map[0][0].length;
	}
	public Node Get(int x, int y, int z)
	{
		return map[y][x][z];
	}
	public Node Get(String name)
	{
		for (int i = 0; i < GetXMax(); i++)
		{
			for (int j = 0; j < GetYMax(); j++)
			{
				for (int k = 0; k < GetZMax(); k++)
				{
					if (Get(i, j, k).FriendlyName.equals(name))
					{
						return Get(i, j, k);
					}
				}
			}
		}
		return null;
	}
	public Node GetFromRealName(String Name)
	{
		return Get(GetNodeX(Name), GetNodeY(Name), GetNodeZ(Name));
	}
	public ArrayList<Node> GetAllWithFriendly()
	{
		ArrayList<Node> nodes = new ArrayList<>();
		for (int i = 0; i < GetXMax(); i++)
		{
			for (int j = 0; j < GetYMax(); j++)
			{
				for (int k = 0; k < GetZMax(); k++)
				{
					if (!Get(i, j, k).FriendlyName.startsWith("(") && !Get(i, j, k).FriendlyName.equals("") && !Get(i, j, k).FriendlyName.contains("Entrance"))
					{
						nodes.add(Get(i, j, k));
					}
				}
			}
		}
		return nodes;
	}
	public ArrayList<String> GetAllWithFriendlyString()
	{
		if (FRIENDLYNAMES == null)
		{
			ArrayList<String> nodes = new ArrayList<>();
			for (int i = 0; i < GetXMax(); i++)
			{
				for (int j = 0; j < GetYMax(); j++)
				{
					for (int k = 0; k < GetZMax(); k++)
					{
						if (!Get(i, j, k).FriendlyName.startsWith("(") && !Get(i, j, k).FriendlyName.equals("") && !Get(i, j, k).FriendlyName.contains("Entrance"))
						{
							nodes.add(Get(i, j, k).FriendlyName);
						}
					}
				}
			}
			FRIENDLYNAMES = nodes;
			return nodes;
		}
		else
		{
			return FRIENDLYNAMES;
		}
	}
	public static int GetNodeX(Node n)
	{
		int x = -1;
		try
		{
			x = Integer.parseInt(n.name.substring(1, n.name.indexOf(",")));
		}
		catch (Exception e)
		{
			
		}
		return x;
	}
	public static int GetNodeY(Node n)
	{
		int y = -1;
		try
		{
			y = Integer.parseInt(n.name.substring(n.name.indexOf(",") + 2, n.name.lastIndexOf(",")));
		}
		catch (Exception e)
		{
			
		}
		return y;
	}
	public static int GetNodeZ(Node n)
	{
		int z = -1;
		try
		{
			z = Integer.parseInt(n.name.substring(n.name.lastIndexOf(",") + 2, n.name.length() - 1));
		}
		catch (Exception e)
		{
			
		}
		return z;
	}
	public static int GetNodeX(String n)
	{
		int x = -1;
		try
		{
			x = Integer.parseInt(n.substring(1, n.indexOf(",")));
		}
		catch (Exception e)
		{
			
		}
		return x;
	}
	public static int GetNodeY(String n)
	{
		int y = -1;
		try
		{
			y = Integer.parseInt(n.substring(n.indexOf(",") + 2, n.lastIndexOf(",")));
		}
		catch (Exception e)
		{
			
		}
		return y;
	}
	public static int GetNodeZ(String n)
	{
		int z = -1;
		try
		{
			z = Integer.parseInt(n.substring(n.lastIndexOf(",") + 2, n.length() - 1));
		}
		catch (Exception e)
		{
			
		}
		return z;
	}
	public void SaveToJSON(String FileName)
	{
		try
		{
			JSONObject root = new JSONObject();
			
			JSONObject config = new JSONObject();
			config.put("name", Name);
			
			JSONObject size = new JSONObject();
			size.put("x", GetXMax());
			size.put("y", GetYMax());
			size.put("z", GetZMax());
			config.put("size", size);
			
			JSONObject distance = new JSONObject();
			distance.put("x", XDistance);
			distance.put("y", YDistance);
			distance.put("z", ZDistance);
			config.put("distance", distance);
			
			config.put("X Distance", XDistance);
			
			root.put("configuration", config);
			
			JSONArray nodes = new JSONArray();
			for (int i = 0; i < GetXMax(); i++)
			{
				for (int j = 0; j < GetYMax(); j++)
				{
					for (int k = 0; k < GetZMax(); k++)
					{
						if (Get(i, j, k).Neighbors.size() > 0 || !Get(i, j, k).FriendlyName.equals("") || Get(i, j, k).object.Bound_Top_Left != null || Get(i, j, k).object.Bound_Top_Right != null || Get(i, j, k).object.Bound_Bottom_Left != null || Get(i, j, k).object.Bound_Bottom_Right != null)
						{
							JSONObject node = new JSONObject();
							Node n = Get(i, j, k);
							node.put("name", n.name);
							node.put("friendly", n.FriendlyName);
							node.put("enabled", n.Enabled);
							JSONObject location = new JSONObject();
							location.put("x", i);
							location.put("y", j);
							location.put("z", k);
							JSONObject structure = new JSONObject();
							JSONObject TL = new JSONObject();
							if (n.object.Bound_Top_Left != null)
							{
								TL.put("x", n.object.Bound_Top_Left.X);
								TL.put("y", n.object.Bound_Top_Left.Y);
								TL.put("z", n.object.Bound_Top_Left.Z);
							}
							else
							{
								TL.put("x", -1);
								TL.put("y", -1);
								TL.put("z", -1);
							}							
							structure.put("TL", TL);
							JSONObject TR = new JSONObject();
							if (n.object.Bound_Top_Right != null)
							{
								TR.put("x", n.object.Bound_Top_Right.X);
								TR.put("y", n.object.Bound_Top_Right.Y);
								TR.put("z", n.object.Bound_Top_Right.Z);
							}
							else
							{
								TR.put("x", -1);
								TR.put("y", -1);
								TR.put("z", -1);
							}							
							structure.put("TR", TR);
							JSONObject BL = new JSONObject();
							if (n.object.Bound_Bottom_Left != null)
							{
								BL.put("x", n.object.Bound_Bottom_Left.X);
								BL.put("y", n.object.Bound_Bottom_Left.Y);
								BL.put("z", n.object.Bound_Bottom_Left.Z);
							}
							else
							{
								BL.put("x", -1);
								BL.put("y", -1);
								BL.put("z", -1);
							}							
							structure.put("BL", BL);
							JSONObject BR = new JSONObject();
							if (n.object.Bound_Bottom_Right != null)
							{
								BR.put("x", n.object.Bound_Bottom_Right.X);
								BR.put("y", n.object.Bound_Bottom_Right.Y);
								BR.put("z", n.object.Bound_Bottom_Right.Z);
							}
							else
							{
								BR.put("x", -1);
								BR.put("y", -1);
								BR.put("z", -1);
							}							
							structure.put("BR", BR);
							node.put("position", location);
							node.put("object", structure);
							JSONArray neighbor = new JSONArray();
							for (int u = 0; u < Get(i, j, k).Neighbors.size(); u++)
							{
								Neighbor neigh = Get(i, j, k).Neighbors.get(u);
								JSONObject ne = new JSONObject();
								ne.put("distance", neigh.distance);
								ne.put("node", neigh.node.name);
								ne.put("direction", neigh.direction);
								neighbor.put(ne);
							}
							node.put("neighbor", neighbor);
							
							nodes.put(node);
						}
					}
				}
			}
			root.put("nodes", nodes);
			
			FileWriter file = new FileWriter(FileName);
			file.write(root.toString());
			file.flush();
			file.close();
			System.out.println(root);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public void CreateFromJSON(Context context, String FileName)
	{
        try
        {
            BufferedReader Reader = new BufferedReader(new InputStreamReader(context.getAssets().open(FileName)));
            JSONObject root = new JSONObject(Reader.readLine());
            Reader.close();
            JSONObject config = root.getJSONObject("configuration");
            Name = config.getString("name");
            JSONObject size = config.getJSONObject("size");
            int x = size.getInt("x");
            int y = size.getInt("y");
            int z = size.getInt("z");
            JSONObject distance = config.getJSONObject("distance");
            XDistance = distance.getDouble("x");
            YDistance = distance.getDouble("y");
            ZDistance = distance.getDouble("z");
            map = new Node[y][x][z];
            for (int i = 0; i < y; i++)
            {
                for (int j = 0; j < x; j++)
                {
                    for (int k = 0; k < z; k++)
                    {
                        map[i][j][k] = new Node("(" + j + ", " + i + ", " + k + ")");
                    }
                }
            }
            JSONArray nodes = root.getJSONArray("nodes");
            for (int i = 0; i < nodes.length(); i++)
            {
                JSONObject node = nodes.getJSONObject(i);
                String friendly = node.getString("friendly");
                String name = node.getString("name");
                boolean enabled = node.getBoolean("enabled");
                JSONArray ne = node.getJSONArray("neighbor");
                int u = GetNodeX(name);
                int v = GetNodeY(name);
                int w = GetNodeZ(name);
                Node n = Get(u, v, w);
                n.FriendlyName = friendly;
                n.name = name;
                n.Enabled = enabled;
                JSONObject structure = node.getJSONObject("object");
                JSONObject bound1 = structure.getJSONObject("BR");
                double x1 = bound1.getDouble("x");
                double y1 = bound1.getDouble("y");
                double z1 = bound1.getDouble("z");
                if (x1 >= 0)
                {
                    n.object.Bound_Bottom_Right = new Engine.Vertex(x1, y1, z1);
                }
                JSONObject bound2 = structure.getJSONObject("TR");
                double x2 = bound2.getDouble("x");
                double y2 = bound2.getDouble("y");
                double z2 = bound2.getDouble("z");
                if (x2 >= 0)
                {
                    n.object.Bound_Top_Right = new Engine.Vertex(x2, y2, z2);
                }
                JSONObject bound3 = structure.getJSONObject("BL");
                double x3 = bound3.getDouble("x");
                double y3 = bound3.getDouble("y");
                double z3 = bound3.getDouble("z");
                if (x3 >= 0)
                {
                    n.object.Bound_Bottom_Left = new Engine.Vertex(x3, y3, z3);
                }
                JSONObject bound4 = structure.getJSONObject("TL");
                double x4 = bound4.getDouble("x");
                double y4 = bound4.getDouble("y");
                double z4 = bound4.getDouble("z");
                if (x4 >= 0)
                {
                    n.object.Bound_Top_Left = new Engine.Vertex(x4, y4, z4);
                }
                String type = structure.getString("type");
                if (type.equals(StructureType.Building.toString()))
                {
                    n.object.Type = StructureType.Building;
                }
                else if (type.equals(StructureType.Path.toString()))
                {
                    n.object.Type = StructureType.Path;
                }
                else if (type.equals(StructureType.WastedSpace.toString()))
                {
                    n.object.Type = StructureType.WastedSpace;
                }
                for (int j = 0; j < ne.length(); j++)
                {
                    JSONObject nodeNe = ne.getJSONObject(j);
                    String neighborNode = nodeNe.getString("node");
                    double d = nodeNe.getDouble("distance");
                    String direction = nodeNe.getString("direction");
                    int f = GetNodeX(neighborNode);
                    int g = GetNodeY(neighborNode);
                    int h = GetNodeZ(neighborNode);
                    d = Math.sqrt(Math.pow((u - f) * XDistance, 2) + Math.pow((v - g) * YDistance, 2) + Math.pow((w - h) * ZDistance, 2));
                    Node a = Get(f, g, h);
                    n.add(a, d, direction, false);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        EnabledNodes = new ArrayList[GetZMax()];
        for (int i = 0; i < EnabledNodes.length; i++)
        {
            EnabledNodes[i] = new ArrayList<Node>();
        }
        for (int i = 0; i < GetXMax(); i++)
        {
            for (int j = 0; j < GetYMax(); j++)
            {
                for (int k = 0; k < GetZMax(); k++)
                {
                    Get(i, j, k).x = i;
                    Get(i, j, k).y = j;
                    Get(i, j, k).z = k;
                    if (Get(i, j, k).object.Bound_Top_Right != null)
                    {
                        EnabledNodes[k].add(Get(i, j, k));
                    }
                }
            }
        }
    }
	private boolean NodeInList(Node check, ArrayList<Node> list)
	{
		boolean _return = false;
		for (int i = 0; i < list.size(); i++)
		{
			if (check == list.get(i))
			{
				_return = true;
			}
		}
		return _return;
	}
	public Path GetPath(Node start, Node end)
	{
		ArrayList<Node> nodePool = new ArrayList<Node>();
		nodePool.add(start);
		for (int i = 0; i < GetXMax(); i++)
		{
			for (int j = 0; j < GetYMax(); j++)
			{
				for (int k = 0; k < GetZMax(); k++)
				{
					if (Get(i, j, k).Neighbors.size() > 0)
					{
						Get(i, j, k).distance = Integer.MAX_VALUE;
						Get(i, j, k).previous = null;
						if (Get(i, j, k) != start)
						{
							nodePool.add(Get(i, j, k));
						}
					}
				}
			}
		}
		start.distance = 0;
		while (nodePool.size() > 0)
		{
			double min = Integer.MAX_VALUE;
			int index = -1;
			for (int i = 0; i < nodePool.size(); i++)
			{
				if (nodePool.get(i).distance <= min)
				{
					min = nodePool.get(i).distance;
					index = i;
				}
			}
			Node n = nodePool.remove(index);
			for (Neighbor ne : n.Neighbors)
			{
				if (NodeInList(ne.node, nodePool) && (n.Enabled || n == start))
				{
					double dis = n.distance + ne.distance;
					if (dis < ne.node.distance)
					{
						ne.node.distance = dis;
						ne.node.previous = n;
					}
				}
			}
		}
		Path p = new Path();
		Node n = end;
		while (n != start)
		{
			p.Nodes.add(0, n);
			n = n.previous;
		}
		p.Nodes.add(0, n);
		p.distance = end.distance;
		for (int i = 0; i < GetXMax(); i++)
		{
			for (int j = 0; j < GetYMax(); j++)
			{
				for (int k = 0; k < GetZMax(); k++)
				{
					Get(i, j, k).distance = Integer.MAX_VALUE;
					Get(i, j, k).previous = null;
				}
			}
		}
		return p;
	}
}
