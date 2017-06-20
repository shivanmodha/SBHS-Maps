package studios.vanish.Viewer;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import studios.vanish.engine.Button;
import studios.vanish.engine.Camera;
import studios.vanish.engine.Color;
import studios.vanish.engine.FillMode;
import studios.vanish.engine.GraphicsUnit;
import studios.vanish.engine.Point;
import studios.vanish.engine.Size;
import studios.vanish.engine.Vertex;
import studios.vanish.engine.Window;
import studios.vanish.engine.Button.ButtonType;
import studios.vanish.graph.Neighbor;
import studios.vanish.graph.Node;
import studios.vanish.graph.NodeGrid;
import studios.vanish.graph.Path;
import studios.vanish.graph.StructureType;
import studios.vanish.graph.GraphVertex;

import studios.vanish.utility.EventHandler;
public class Viewer
{
	public EventHandler OnClose = new EventHandler();
	String ReturnValue = "";
	Window window;
	Size BorderSizeMax = new Size(250, 20);
	Size BorderSize = new Size(0, 20);
	Button[] ToolbarButtons = new Button[11];
	Button[][] ToolbarBtns = new Button[1][2];
	Button[] EditGraph = new Button[16];
	Button[] EditRemovePath = null;
	Button[] EditChangePath = null;
	Button Edit_Build_Path;
	Color Control = Color.WhiteSmoke;
	Color ControlDark = Color.Ivory;
	Color[] Toggled = new Color[]
	{
		Color.SkyBlue,
		Color.White
	};
	Color[] ToolbarColors = new Color[]
	{
		Control, Color.Gray,
		Color.Gainsboro, Color.Black,
		Color.Gray, Color.White
	};
	Color[] DialogBackground = new Color[]
	{
		new Color(Color.Black, 100)
	};
	Font MainFont;
	Font BigFont;
	NodeGrid Map;
	Button[][][] Nodes;
	int Distance = 15;
	Size NodeSize = new Size(5, 5);
	Size Border = new Size(24, 25);
	Size Offset = new Size(-7, -7);
	int RenderFloor2D = 0;
	Vertex Graph2DRotation = new Vertex(0, 0, 0);
	Vertex Graph2DLocation = new Vertex(0, 0, -50);
	Vertex Graph3DRotation = new Vertex(0, 0, 0);
	Vertex Graph3DLocation = new Vertex(0, 0, -50);	
	Node Selected = null;
	Node Path_Start = null;
	Node Path_End = null;
	public static Path ShortestPath = null;
	boolean View3D = false;
	boolean hold = false;
	String xStr = "\u2573"; //2573
	Point MousePreviousLocation = null;
	volatile int POINTER = 0; //0 = Selection; 1 = Move; 2 = Rotate
	int NEW_X = 5;
	int NEW_Y = 5;
	int NEW_Z = 5;
	int PathYBegin = 375;
	boolean RenderNodes = false;
	boolean RenderPaths = false;
	boolean RenderTitle = false;
	boolean RenderStructure = true;
	public Viewer()
	{
		window = new Window("Viewer", new Size(800, 600), true);
		window.Initialize(200);
		window.OnPaint.Add(this, "Event_Render");
		window.OnResize.Add(this, "Event_Resize");
		window.OnMouseWheel.Add(this, "Event_Wheel");
		window.SetMinimumSize(new Size(800, 600));
		window.Initialize3D(window.Size, 1024, FillMode.Wireframe, false, false);
		window.Camera = new Camera();
		window.Camera.Location = new Vertex(0, 0, -50);
		window.Show();
		Initialize();
		while (true)
		{
			Event_Input();
			Event_Update();
		}
	}
	private void Initialize()
	{
		MainFont = new Font("Helvetica", Font.PLAIN, 12);
		BigFont = new Font("Helvetica", Font.PLAIN, 15);
		
		ToolbarButtons[2] = new Button(window, "Open", new Point(0, 0), new Size(50, BorderSize.Height));
		ToolbarButtons[2].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[2].Type = ButtonType.Rectangle;
		ToolbarButtons[2].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[2].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[2].OnClick.Add(this, "Event_Toolbar_02");

		ToolbarButtons[3] = new Button(window, "Save", new Point(50, 0), new Size(50, BorderSize.Height));
		ToolbarButtons[3].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[3].Type = ButtonType.Rectangle;
		ToolbarButtons[3].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[3].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[3].OnClick.Add(this, "Event_Toolbar_03");

		ToolbarButtons[4] = new Button(window, "3D", new Point(100, 0), new Size(50, BorderSize.Height));
		ToolbarButtons[4].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[4].Type = ButtonType.Rectangle;
		ToolbarButtons[4].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[4].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[4].OnClick.Add(this, "Event_Toolbar_04");

		ToolbarButtons[5] = new Button(window, "\u21F1", new Point(150, 0), new Size(BorderSize.Height, BorderSize.Height));
		ToolbarButtons[5].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[5].Type = ButtonType.Rectangle;
		ToolbarButtons[5].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[5].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[5].OnClick.Add(this, "Event_Toolbar_05");
		
		ToolbarButtons[6] = new Button(window, "\u203B", new Point(150 + BorderSize.Height, 0), new Size(BorderSize.Height, BorderSize.Height));
		ToolbarButtons[6].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[6].Type = ButtonType.Rectangle;
		ToolbarButtons[6].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[6].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[6].OnClick.Add(this, "Event_Toolbar_06");

		ToolbarButtons[7] = new Button(window, "\u21BB", new Point(150 + (BorderSize.Height * 2), 0), new Size(BorderSize.Height, BorderSize.Height));
		ToolbarButtons[7].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[7].Type = ButtonType.Rectangle;
		ToolbarButtons[7].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[7].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[7].OnClick.Add(this, "Event_Toolbar_07");

		ToolbarButtons[9] = new Button(window, "\u25BC", new Point(150 + (BorderSize.Height * 3), 0), new Size(BorderSize.Height, BorderSize.Height));
		ToolbarButtons[9].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[9].Type = ButtonType.Rectangle;
		ToolbarButtons[9].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[9].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[9].OnClick.Add(this, "Event_Toolbar_09");

		ToolbarButtons[10] = new Button(window, "\u25B2", new Point(150 + (BorderSize.Height * 4), 0), new Size(BorderSize.Height, BorderSize.Height));
		ToolbarButtons[10].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[10].Type = ButtonType.Rectangle;
		ToolbarButtons[10].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[10].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[10].OnClick.Add(this, "Event_Toolbar_10");
		
		New(NEW_X, NEW_Y, NEW_Z, "Untitled");
	}
	private void New(int x, int y, int z, String name)
	{
		hold = true;
		Map = new NodeGrid(x, y, z, "Untitled", false, false, false, false, false, false, false);
		Nodes = new Button[y][x][z];
		for (int i = 0; i < Map.GetYMax(); i++)
		{
			for (int j = 0; j < Map.GetXMax(); j++)
			{
				for (int k = 0; k < Map.GetZMax(); k++)
				{
					Nodes[i][j][k] = new Button(window, "", new Point(0, 0), NodeSize);
					Nodes[i][j][k].FillMode = FillMode.Wireframe;
					Nodes[i][j][k].Type = ButtonType.Circle;
					Nodes[i][j][k].SetBackColor(Color.Black, ToolbarColors[1], ToolbarColors[2]);
					Nodes[i][j][k].OnClick.Add(this, "Event_NodeClick");
				}
			}
		}
		hold = false;
	}
	public void Calculate(boolean print)
	{
		ShortestPath = Map.GetPath(Path_Start, Path_End);
	}
	public void Event_Toolbar_02(Button Sender, Point MouseLocation, int Button)
	{
		JFileChooser dialog = new JFileChooser();
		dialog.setFileFilter(new FileFilter()
		{
			 public boolean accept(File f)
			 {
		        if (f.isDirectory())
		            return true;
		        return (f.getName().toLowerCase().endsWith("ngm"));
		    }
		    public String getDescription()
		    {
		        return "Node Graph Map (.ngm)";
		    }
		});
		if (dialog.showOpenDialog(window.GetInnerWindow()) == JFileChooser.APPROVE_OPTION)
		{
			hold = true;
			Map.CreateFromJSON(dialog.getSelectedFile().getAbsolutePath());
			
			Nodes = new Button[Map.GetYMax()][Map.GetXMax()][Map.GetZMax()];
			for (int i = 0; i < Map.GetYMax(); i++)
			{
				for (int j = 0; j < Map.GetXMax(); j++)
				{
					for (int k = 0; k < Map.GetZMax(); k++)
					{
						Nodes[i][j][k] = new Button(window, "", new Point(0, 0), NodeSize);
						Nodes[i][j][k].FillMode = FillMode.Wireframe;
						Nodes[i][j][k].Type = ButtonType.Circle;
						Nodes[i][j][k].SetBackColor(Color.Black, ToolbarColors[1], ToolbarColors[2]);
						Nodes[i][j][k].OnClick.Add(this, "Event_NodeClick");
					}
				}
			}			
			Selected = null;
			Path_Start = null;
			Path_End = null;
			ShortestPath = null;
			hold = false;
		}
	}
	public void Event_Toolbar_03(Button Sender, Point MouseLocation, int Button)
	{
		JFileChooser dialog = new JFileChooser();
		dialog.setFileFilter(new FileFilter()
		{
			 public boolean accept(File f)
			 {
		        if (f.isDirectory())
		            return true;
		        return (f.getName().toLowerCase().endsWith("ngm"));
		    }
		    public String getDescription()
		    {
		        return "Node Graph Map (.ngm)";
		    }
		});
		if (dialog.showSaveDialog(window.GetInnerWindow()) == JFileChooser.APPROVE_OPTION)
		{
			Map.SaveToJSON(dialog.getSelectedFile().getAbsolutePath() + ".ngm");
		}
	}
	public void Event_Toolbar_04(Button Sender, Point MouseLocation, int Button)
	{
		View3D = !View3D;
		if (View3D)
		{
			ToolbarButtons[4].SetBackColor(Toggled[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[4].SetForeColor(Toggled[1], ToolbarColors[3], ToolbarColors[5]);		
		}
		else
		{
			ToolbarButtons[4].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[4].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);				
		}
	}
	public void Event_Toolbar_05(Button Sender, Point MouseLocation, int Button)
	{
		POINTER = 0;
	}
	public void Event_Toolbar_06(Button Sender, Point MouseLocation, int Button)
	{
		POINTER = 1;
	}
	public void Event_Toolbar_07(Button Sender, Point MouseLocation, int Button)
	{
		POINTER = 2;
	}
	public void Event_Toolbar_09(Button Sender, Point MouseLocation, int Button)
	{
		RenderFloor2D--;
		if (RenderFloor2D < 0)
		{
			RenderFloor2D = 0;
		}
	}
	public void Event_Toolbar_10(Button Sender, Point MouseLocation, int Button)
	{
		RenderFloor2D++;
		if (RenderFloor2D > Map.GetZMax() - 1)
		{
			RenderFloor2D = Map.GetZMax() - 1;
		}
	}
	public void Event_Input()
	{
		Point MouseLocation = window.GetMousePosition().subtract(window.Location).subtract(new Point(0, 30));
		Point moved = new Point(0, 0);
		double MouseSpeed = Math.abs(Graph2DLocation.Z) / 150;
		if (MousePreviousLocation != null)
		{
			moved = MouseLocation.subtract(MousePreviousLocation);
		}
		MousePreviousLocation = MouseLocation;		
		if (window.Mouse[1] && MouseLocation.X < window.Size.Width - BorderSize.Width && MouseLocation.Y > 20)
		{
			if (!View3D)
			{
				if (POINTER == 1)
				{
					Graph2DLocation.X -= moved.X * MouseSpeed / 7;
					Graph2DLocation.Y += moved.Y * MouseSpeed / 7;
				}
				else if (POINTER == 2)
				{
					//Graph2DRotation.Y += (moved.X + moved.Y) * MouseSpeed;
					//Graph2DRotation.X -= moved.Y * MouseSpeed;
				}
			}
			else
			{
				if (POINTER == 1)
				{
					Graph3DLocation.X -= moved.X * MouseSpeed / 7;
					Graph3DLocation.Y += moved.Y * MouseSpeed / 7;
				}
				else if (POINTER == 2)
				{
					Graph3DRotation.Z -= moved.X * MouseSpeed;
					Graph3DRotation.X -= moved.Y * MouseSpeed;
				}
			}
		}
	}
	public void Event_Update()
	{
		if (POINTER == 0)
		{
			ToolbarButtons[5].SetBackColor(Toggled[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[5].SetForeColor(Toggled[1], ToolbarColors[3], ToolbarColors[5]);
			ToolbarButtons[6].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[6].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
			ToolbarButtons[7].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[7].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		}
		else if (POINTER == 1)
		{
			ToolbarButtons[5].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[5].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
			ToolbarButtons[6].SetBackColor(Toggled[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[6].SetForeColor(Toggled[1], ToolbarColors[3], ToolbarColors[5]);
			ToolbarButtons[7].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[7].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		}
		else if (POINTER == 2)
		{
			ToolbarButtons[5].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[5].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
			ToolbarButtons[6].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[6].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
			ToolbarButtons[7].SetBackColor(Toggled[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[7].SetForeColor(Toggled[1], ToolbarColors[3], ToolbarColors[5]);
		}
		for (int i = 0; i < ToolbarButtons.length; i++)
		{
			if (ToolbarButtons[i] != null)
			{
				ToolbarButtons[i].Enabled = true;
			}
		}
		for (int i = 0; i < ToolbarBtns.length; i++)
		{
			for (int j = 0; j < ToolbarBtns[i].length; j++)
			{

				if (ToolbarBtns[i][j] != null)
				{
					ToolbarBtns[i][j].Enabled = false;
				}
			}
		}
		for (int i = 0; i < Map.GetYMax(); i++)
		{
			for (int j = 0; j < Map.GetXMax(); j++)
			{
				for (int k = 0; k < Map.GetZMax(); k++)
				{
					if (!hold && Nodes[i][j][k] != null)
					{
							Nodes[i][j][k].Enabled = false;
					}
				}
			}
		}
		if (!View3D)
		{
			window.Camera.Location = Graph2DLocation;
		}
		else
		{
			window.Camera.Location = Graph3DLocation;
		}
	}
	public void Event_Render(GraphicsUnit Graphics)
	{
		Render_Clear(Graphics);
		if (!View3D)
		{
			Render_2D(Graphics);
		}
		else
		{
			Render_3D(Graphics);
		}
		Render_UI(Graphics);
		window.SetTitle("Graph Viewer - " + Map.Name);
	}
	public void Event_Resize(Size oldSize, Size newSize)
	{
		window.Resolution = window.Size.subtract(BorderSize);
		ToolbarBtns[0][0].Location = new Point(window.Size.Width - 55, window.Size.Height - 30);
		ToolbarBtns[0][1].Location = new Point(window.Size.Width - 110, window.Size.Height - 30);
		if (EditRemovePath != null)
		{
			for (int i = 0; i < EditRemovePath.length; i++)
			{
				EditRemovePath[i].Location = new Point(window.Size.Width - 25, PathYBegin + (20 * i));
				EditChangePath[i].Location = new Point(window.Size.Width - 45, PathYBegin + (20 * i));
			}
		}
	}
	public void Event_Wheel(int ScrollAmount)
	{
		if (!View3D)
		{
			//Point MouseLocation = window.GetMousePosition().subtract(window.Location).subtract(new Point(0, 30));
			if (!window.Mouse[3])
			{
				Graph2DLocation.Z -= ScrollAmount;
			}
			else
			{
				RenderFloor2D += Math.abs(ScrollAmount) / ScrollAmount;
				if (RenderFloor2D < 0)
				{
					RenderFloor2D = 0;
				}
				else if (RenderFloor2D >= Map.GetZMax())
				{
					RenderFloor2D = Map.GetZMax() - 1;
				}
			}
		}
		else
		{
			Graph3DLocation.Z -= ScrollAmount;
		}
	}	
	public void Render_Clear(GraphicsUnit Graphics)
	{
		Graphics.FillRectangle(new Color(223, 223, 223, 255), new Point(0, 0), window.Size);
	}
	public void Render_2D(GraphicsUnit Graphics)
	{
		Size ContentSize = window.Size.subtract(BorderSize);
		double xmax = Map.GetYMax();
		double ymax = Map.GetXMax();
		
		int k = RenderFloor2D;
		for (int i = 0; i < Map.GetYMax(); i++)
		{ 
			for (int j = 0; j < Map.GetXMax(); j++)
			{
				if (RenderStructure)
				{
					if (Map.Get(j, i, k).object.Bound_Top_Left != null && Map.Get(j, i, k).object.Bound_Top_Right != null && Map.Get(j, i, k).object.Bound_Bottom_Left != null && Map.Get(j, i, k).object.Bound_Bottom_Right != null)
					{
						if (Map.Get(j, i, k).object.Bound_Top_Right.Z == Map.Get(j, i, k).object.Bound_Top_Left.Z && Map.Get(j, i, k).object.Bound_Top_Right.Z == Map.Get(j, i, k).object.Bound_Bottom_Right.Z && Map.Get(j, i, k).object.Bound_Top_Right.Z == Map.Get(j, i, k).object.Bound_Bottom_Left.Z)
						{
							GraphVertex gv = Map.Get(j, i, k).object.Bound_Top_Left.subtract(new GraphVertex((ymax / 2), (xmax / 2), 1)).multiply(new GraphVertex(Map.XDistance, Map.YDistance, 0));
							Vertex TL = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							TL = Graphics.D3D_ToProjection(TL, ContentSize, new Vertex(0, -20, 0));
							TL = TL.add(new Vertex(10, 10, 0));
							
							gv = Map.Get(j, i, k).object.Bound_Top_Right.subtract(new GraphVertex((ymax / 2), (xmax / 2), 1)).multiply(new GraphVertex(Map.XDistance, Map.YDistance, 0));
							Vertex TR = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							TR = Graphics.D3D_ToProjection(TR, ContentSize, new Vertex(0, -20, 0));
							TR = TR.add(new Vertex(10, 10, 0));
							
							gv = Map.Get(j, i, k).object.Bound_Bottom_Left.subtract(new GraphVertex((ymax / 2), (xmax / 2), 1)).multiply(new GraphVertex(Map.XDistance, Map.YDistance, 0));
							Vertex BL = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							BL = Graphics.D3D_ToProjection(BL, ContentSize, new Vertex(0, -20, 0));
							BL = BL.add(new Vertex(10, 10, 0));
							
							gv = Map.Get(j, i, k).object.Bound_Bottom_Right.subtract(new GraphVertex((ymax / 2), (xmax / 2), 1)).multiply(new GraphVertex(Map.XDistance, Map.YDistance, 0));
							Vertex BR = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							BR = Graphics.D3D_ToProjection(BR, ContentSize, new Vertex(0, -20, 0));
							BR = BR.add(new Vertex(10, 10, 0));							
							Point[] polygon = new Point[]
							{
								TL.toPoint(),
								TR.toPoint(),
								BR.toPoint(),
								BL.toPoint()
							};
							StructureType type = Map.Get(j, i, k).object.Type;
							if (type == StructureType.WastedSpace)
							{								
								Graphics.FillPolygon(new Color(223, 223, 223, 255), polygon);
							}
							if (type == StructureType.Path)
							{
								Graphics.FillPolygon(Color.White, polygon);
								Point midpoint = new Point();
								midpoint.X = polygon[0].X + polygon[1].X + polygon[2].X + polygon[3].X;
								midpoint.X /= 4;
								midpoint.Y = polygon[0].Y + polygon[1].Y + polygon[2].Y + polygon[3].Y;
								midpoint.Y /= 4;
								if (Map.Get(j, i, k).FriendlyName.contains("(h)"))
								{
									String str = Map.Get(j, i, k).FriendlyName.substring(3);
									Font renderFont = new Font("Helvetica", Font.PLAIN, 10);
									if (Math.abs(Map.Get(j, i, k).object.Bound_Top_Left.X - Map.Get(j, i, k).object.Bound_Top_Right.X) < Math.abs(Map.Get(j, i, k).object.Bound_Top_Left.Y - Map.Get(j, i, k).object.Bound_Bottom_Left.Y))
									{
										AffineTransform rotateMatrix = new AffineTransform();
										rotateMatrix.rotate(Math.PI / 2, 0, 0);
										renderFont = renderFont.deriveFont(rotateMatrix);
									}
									Graphics.DrawString(str, Color.Black, midpoint, renderFont);
								}
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < Map.GetYMax(); i++)
		{
			for (int j = 0; j < Map.GetXMax(); j++)
			{
				if (RenderStructure)
				{
					if (Map.Get(j, i, k).object.Bound_Top_Left != null && Map.Get(j, i, k).object.Bound_Top_Right != null && Map.Get(j, i, k).object.Bound_Bottom_Left != null && Map.Get(j, i, k).object.Bound_Bottom_Right != null)
					{
						if (Map.Get(j, i, k).object.Bound_Top_Right.Z == Map.Get(j, i, k).object.Bound_Top_Left.Z && Map.Get(j, i, k).object.Bound_Top_Right.Z == Map.Get(j, i, k).object.Bound_Bottom_Right.Z && Map.Get(j, i, k).object.Bound_Top_Right.Z == Map.Get(j, i, k).object.Bound_Bottom_Left.Z)
						{
							GraphVertex gv = Map.Get(j, i, k).object.Bound_Top_Left.subtract(new GraphVertex((ymax / 2), (xmax / 2), 1)).multiply(new GraphVertex(Map.XDistance, Map.YDistance, 0));
							Vertex TL = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							TL = Graphics.D3D_ToProjection(TL, ContentSize, new Vertex(0, -20, 0));
							TL = TL.add(new Vertex(10, 10, 0));
							
							gv = Map.Get(j, i, k).object.Bound_Top_Right.subtract(new GraphVertex((ymax / 2), (xmax / 2), 1)).multiply(new GraphVertex(Map.XDistance, Map.YDistance, 0));
							Vertex TR = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							TR = Graphics.D3D_ToProjection(TR, ContentSize, new Vertex(0, -20, 0));
							TR = TR.add(new Vertex(10, 10, 0));
							
							gv = Map.Get(j, i, k).object.Bound_Bottom_Left.subtract(new GraphVertex((ymax / 2), (xmax / 2), 1)).multiply(new GraphVertex(Map.XDistance, Map.YDistance, 0));
							Vertex BL = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							BL = Graphics.D3D_ToProjection(BL, ContentSize, new Vertex(0, -20, 0));
							BL = BL.add(new Vertex(10, 10, 0));
							
							gv = Map.Get(j, i, k).object.Bound_Bottom_Right.subtract(new GraphVertex((ymax / 2), (xmax / 2), 1)).multiply(new GraphVertex(Map.XDistance, Map.YDistance, 0));
							Vertex BR = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							BR = Graphics.D3D_ToProjection(BR, ContentSize, new Vertex(0, -20, 0));
							BR = BR.add(new Vertex(10, 10, 0));							
							Point[] polygon = new Point[]
							{
								TL.toPoint(),
								TR.toPoint(),
								BR.toPoint(),
								BL.toPoint()
							};
							StructureType type = Map.Get(j, i, k).object.Type;
							if (type == StructureType.Building)
							{
								Graphics.FillPolygon(new Color(242, 242, 242, 255), polygon);
								Graphics.DrawPolygon(new Color(200, 200, 200, 255), polygon);
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < Map.GetYMax() + 1; i++)
		{
			for (int j = 0; j < Map.GetXMax() + 1; j++)
			{
				if (i < Map.GetYMax() && j < Map.GetXMax() && k < Map.GetZMax())
				{
					Vertex Location = new Vertex(j - (ymax / 2),  i - (xmax / 2), 0);
					Location = Location.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
					Location = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, Location);
					Location = Graphics.D3D_ToProjection(Location, ContentSize, new Vertex(0, -20, 0));
					Nodes[i][j][k].Location = new Point(Location.X + (NodeSize.Width * 2), Location.Y + (NodeSize.Height * 2));
					boolean changed = false;
					if (Map.Get(j, i, k) == Selected)
					{
						Nodes[i][j][k].FillMode = FillMode.Solid;
						Nodes[i][j][k].SetBackColor(Color.SkyBlue, ToolbarColors[1], ToolbarColors[2]);
						changed = true;
					}
					else if (Map.Get(j, i, k) == Path_Start)
					{
						Nodes[i][j][k].FillMode = FillMode.Solid;
						Nodes[i][j][k].SetBackColor(Color.Green, ToolbarColors[1], ToolbarColors[2]);
						changed = true;
					}
					else if (Map.Get(j, i, k) == Path_End)
					{
						Nodes[i][j][k].FillMode = FillMode.Solid;
						Nodes[i][j][k].SetBackColor(Color.Red, ToolbarColors[1], ToolbarColors[2]);
						changed = true;
					}
					if (ShortestPath != null)
					{
						for (int l = 1; l < ShortestPath.Nodes.size() - 1; l++)
						{
							if (ShortestPath.Nodes.get(l) == Map.Get(j, i, k))
							{
								Nodes[i][j][k].FillMode = FillMode.Solid;
								Nodes[i][j][k].SetBackColor(Color.Orange, Color.Blue, Color.White);
								changed = true;
							}
						}
					}
					if (!changed)
					{
						Nodes[i][j][k].FillMode = FillMode.Wireframe;
						Nodes[i][j][k].SetBackColor(Color.Black, ToolbarColors[1], ToolbarColors[2]);						
					}
					for (Neighbor neighbor : Map.Get(j, i, k).Neighbors)
					{
						int x = NodeGrid.GetNodeX(neighbor.node);
						int y = NodeGrid.GetNodeY(neighbor.node);
						int z = NodeGrid.GetNodeZ(neighbor.node);
						Color col = Color.Black;
						if (ShortestPath != null)
						{
							for (int l = 0; l < ShortestPath.Nodes.size() - 1; l++)
							{
								if (Map.Get(j, i, k) == ShortestPath.Nodes.get(l))
								{
									if (neighbor.node == ShortestPath.Nodes.get(l + 1))
									{
										col = Color.Orange;
									}
									if (l != 0)
									{
										if (neighbor.node == ShortestPath.Nodes.get(l - 1))
										{
											col = Color.Orange;
										}
									}
								}	
								if (Map.Get(j, i, k) == ShortestPath.Nodes.get(l + 1))
								{
									if (neighbor.node == ShortestPath.Nodes.get(l))
									{
										col = Color.Orange;
									}
								}							
							}
						}
						if (k == z)
						{
							if (RenderPaths || col == Color.Orange)
							{
								Graphics.DrawLine(col, new Point[]{Nodes[i][j][k].Location, Nodes[y][x][z].Location});
							}
						}
					}
					if (Map.Get(j, i, k).Neighbors.size() == 0)
					{
					}
					else
					{
						if (RenderNodes || changed)
						{
							Nodes[i][j][k].Render(Graphics);
						}
						if (!Map.Get(j, i, k).FriendlyName.equals("") && RenderTitle)
						{
							Graphics.DrawString(Map.Get(j, i, k).FriendlyName, Color.Black, new Point(Location.X + (NodeSize.Width / 2), Location.Y + (NodeSize.Height / 2)), MainFont);
						}					
					}
				}
			}
		}
	}
	public void Render_3D(GraphicsUnit Graphics)
	{
		Size ContentSize = window.Size.subtract(BorderSize);
		double xmax = Map.GetYMax();
		double ymax = Map.GetXMax();
		double zmax = Map.GetZMax();
		
		for (int i = 0; i < Map.GetYMax(); i++)
		{
			for (int j = 0; j < Map.GetXMax(); j++)
			{
				for (int k = Map.GetZMax() - 1; k >= 0; k--)
				{
					if (RenderStructure)
					{
						if (Map.Get(j, i, k).object.Bound_Top_Left != null && Map.Get(j, i, k).object.Bound_Top_Right != null && Map.Get(j, i, k).object.Bound_Bottom_Left != null && Map.Get(j, i, k).object.Bound_Bottom_Right != null)
						{
							GraphVertex gv = Map.Get(j, i, k).object.Bound_Top_Left.subtract(new GraphVertex((ymax / 2) - 0.5, (xmax / 2) + 0.25, Map.Get(j, i, k).object.Bound_Top_Left.Z - ((zmax / 2) - Map.Get(j, i, k).object.Bound_Top_Left.Z))).multiply(new GraphVertex(Map.XDistance, Map.YDistance, Map.ZDistance));
							Vertex TL = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph3DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							TL = Graphics.D3D_ToProjection(TL, ContentSize, new Vertex(0, -20, 0));
							TL = TL.add(new Vertex(10, 10, 0));
							
							gv = Map.Get(j, i, k).object.Bound_Top_Right.subtract(new GraphVertex((ymax / 2) - 0.5, (xmax / 2) + 0.25, Map.Get(j, i, k).object.Bound_Top_Right.Z - ((zmax / 2) - Map.Get(j, i, k).object.Bound_Top_Right.Z))).multiply(new GraphVertex(Map.XDistance, Map.YDistance, Map.ZDistance));
							Vertex TR = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph3DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							TR = Graphics.D3D_ToProjection(TR, ContentSize, new Vertex(0, -20, 0));
							TR = TR.add(new Vertex(10, 10, 0));
							
							gv = Map.Get(j, i, k).object.Bound_Bottom_Left.subtract(new GraphVertex((ymax / 2) - 0.5, (xmax / 2) + 0.25, Map.Get(j, i, k).object.Bound_Bottom_Left.Z - ((zmax / 2) - Map.Get(j, i, k).object.Bound_Bottom_Left.Z))).multiply(new GraphVertex(Map.XDistance, Map.YDistance, Map.ZDistance));
							Vertex BL = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph3DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							BL = Graphics.D3D_ToProjection(BL, ContentSize, new Vertex(0, -20, 0));
							BL = BL.add(new Vertex(10, 10, 0));
							
							gv = Map.Get(j, i, k).object.Bound_Bottom_Right.subtract(new GraphVertex((ymax / 2) - 0.5, (xmax / 2) + 0.25, Map.Get(j, i, k).object.Bound_Bottom_Right.Z - ((zmax / 2) - Map.Get(j, i, k).object.Bound_Bottom_Right.Z))).multiply(new GraphVertex(Map.XDistance, Map.YDistance, Map.ZDistance));
							Vertex BR = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph3DRotation, new Vertex(gv.X, gv.Y, gv.Z));
							BR = Graphics.D3D_ToProjection(BR, ContentSize, new Vertex(0, -20, 0));
							BR = BR.add(new Vertex(10, 10, 0));							
							Point[] polygon = new Point[]
							{
								TL.toPoint(),
								TR.toPoint(),
								BR.toPoint(),
								BL.toPoint()
							};
							StructureType type = Map.Get(j, i, k).object.Type;
							if (type == StructureType.Building)
							{
								Graphics.FillPolygon(Color.WhiteSmoke, polygon);
								Graphics.DrawPolygon(Color.Gainsboro, polygon);
							}
							if (type == StructureType.WastedSpace)
							{
								Graphics.FillPolygon(Color.Black, polygon);
							}
							if (type == StructureType.Path)
							{
								Graphics.FillPolygon(Color.PaleGoldenRod, polygon);
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < Map.GetYMax() + 1; i++)
		{
			for (int j = 0; j < Map.GetXMax() + 1; j++)
			{
				for (int k = Map.GetZMax(); k >= 0; k--)
				{
					if (i < Map.GetYMax() && j < Map.GetXMax() && k < Map.GetZMax())
					{
						Vertex Location = new Vertex(j - (ymax / 2) + 0.5,  i - (xmax / 2) - 0.25, (zmax / 2) - k);
						Location = Location.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
						Location = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph3DRotation, Location);
						Location = Graphics.D3D_ToProjection(Location, ContentSize, new Vertex(0, -20, 0));
						Nodes[i][j][k].Location = new Point(Location.X + (NodeSize.Width * 2), Location.Y + (NodeSize.Height * 2));
						boolean changed = false;
						if (Map.Get(j, i, k) == Selected)
						{
							Nodes[i][j][k].FillMode = FillMode.Solid;
							Nodes[i][j][k].SetBackColor(Color.SkyBlue, ToolbarColors[1], ToolbarColors[2]);
							changed = true;
						}
						else if (Map.Get(j, i, k) == Path_Start)
						{
							Nodes[i][j][k].FillMode = FillMode.Solid;
							Nodes[i][j][k].SetBackColor(Color.Green, ToolbarColors[1], ToolbarColors[2]);
							changed = true;
						}
						else if (Map.Get(j, i, k) == Path_End)
						{
							Nodes[i][j][k].FillMode = FillMode.Solid;
							Nodes[i][j][k].SetBackColor(Color.Red, ToolbarColors[1], ToolbarColors[2]);
							changed = true;
						}
						if (ShortestPath != null)
						{
							for (int l = 1; l < ShortestPath.Nodes.size() - 1; l++)
							{
								if (ShortestPath.Nodes.get(l) == Map.Get(j, i, k))
								{
									Nodes[i][j][k].FillMode = FillMode.Solid;
									Nodes[i][j][k].SetBackColor(Color.Orange, Color.Blue, Color.White);
									changed = true;
								}
							}
						}
						if (!changed)
						{
							Nodes[i][j][k].FillMode = FillMode.Wireframe;
							Nodes[i][j][k].Type = ButtonType.Circle;
							Nodes[i][j][k].SetBackColor(Color.Black, ToolbarColors[1], ToolbarColors[2]);						
						}
						for (Neighbor neighbor : Map.Get(j, i, k).Neighbors)
						{
							int x = NodeGrid.GetNodeX(neighbor.node);
							int y = NodeGrid.GetNodeY(neighbor.node);
							int z = NodeGrid.GetNodeZ(neighbor.node);
							Color col = Color.Black;
							if (ShortestPath != null)
							{
								for (int l = 0; l < ShortestPath.Nodes.size() - 1; l++)
								{
									if (Map.Get(j, i, k) == ShortestPath.Nodes.get(l))
									{
										if (neighbor.node == ShortestPath.Nodes.get(l + 1))
										{
											col = Color.Orange;
										}
										if (l != 0)
										{
											if (neighbor.node == ShortestPath.Nodes.get(l - 1))
											{
												col = Color.Orange;
											}
										}
									}	
									if (Map.Get(j, i, k) == ShortestPath.Nodes.get(l + 1))
									{
										if (neighbor.node == ShortestPath.Nodes.get(l))
										{
											col = Color.Orange;
										}
									}							
								}
							}
							if (RenderPaths || col == Color.Orange)
							{
								Graphics.DrawLine(col, new Point[]{Nodes[i][j][k].Location, Nodes[y][x][z].Location});
							}
						}
						if (Map.Get(j, i, k).Neighbors.size() == 0)
						{
						}
						else
						{
							if (RenderNodes || changed)
							{
								Nodes[i][j][k].Render(Graphics);	
							}		
							if (!Map.Get(j, i, k).FriendlyName.equals("") && RenderTitle)
							{
								Graphics.DrawString(Map.Get(j, i, k).FriendlyName, Color.Black, new Point(Location.X + (NodeSize.Width / 2), Location.Y + (NodeSize.Height / 2)), MainFont);
							}				
						}
					}
					Vertex Location = new Vertex((j -0.5) - (ymax / 2),  (i - 1.25) - (xmax / 2), (zmax / 2) - (k - 1));
					Location = Location.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
					Location = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph3DRotation, Location);
					Location = Graphics.D3D_ToProjection(Location, ContentSize, new Vertex(0, -20, 0));
					if (i == 0 && k == 0)
					{
						String str = j + "";
						if (j < 10)
						{
							str = "0" + str;
						}
						Graphics.DrawString(str, Color.Black, Location.toPoint(), new Font("Courier New", Font.PLAIN, 15));
					}
					if (j == 0 && k == 0)
					{
						String str = i + "";
						if (i < 10)
						{
							str = "0" + str;
						}
						Graphics.DrawString(str, Color.Black, Location.toPoint(), new Font("Courier New", Font.PLAIN, 15));					
					}
					if (i == 0 && j == 0 && k != 0)
					{
						String str = k + "";
						if (k < 10)
						{
							str = "0" + str;
						}
						Graphics.DrawString(str, Color.Black, Location.toPoint(), new Font("Courier New", Font.PLAIN, 15));					
					}
				}
			}
		}
	}
	public void Render_UI(GraphicsUnit Graphics)
	{
		Graphics.FillRectangle(Control, new Point(0, 0), new Size(window.Size.Width, BorderSize.Height));
		for (int i = 0; i < ToolbarButtons.length; i++)
		{
			if (ToolbarButtons[i] != null)
			{
				ToolbarButtons[i].Render(Graphics);
			}
		}
		Graphics.FillRectangle(Color.Gainsboro, ToolbarButtons[4].Location, new Size(1, BorderSize.Height));
		Graphics.FillRectangle(Color.Gainsboro, ToolbarButtons[8].Location, new Size(1, BorderSize.Height));
		double line = BorderSize.Width / 3;
		if (BorderSize.Width == 0)
		{
			line = 100;
		}
		if (!View3D)
		{
			Graphics.DrawString("" + (RenderFloor2D + 1), Color.Black, new Point(255 + (BorderSize.Height * 5), 0), new Font("Courier New", Font.PLAIN, 15));	
		}
		Graphics.FillRectangle(ControlDark, new Point(window.Size.Width - BorderSize.Width, 0), new Size(BorderSize.Width, window.Size.Height));
		Graphics.DrawString("Graph", Color.Black, new Point(window.Size.Width - BorderSize.Width + 10, 0), BigFont);
		Graphics.DrawString("Name", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Name", MainFont).Width, 25), MainFont);
		Graphics.DrawString(Map.Name, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, 25), MainFont);
		Graphics.DrawString("Size", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Size", MainFont).Width, 45), MainFont);
		Graphics.DrawString("(" + Map.GetXMax() + ", " + Map.GetYMax() + ", " + Map.GetZMax() + ")", Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, 45), MainFont);
		Graphics.DrawString("Distance", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Distance", MainFont).Width, 65), MainFont);
		Graphics.DrawString("x", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line + 10, 65), MainFont);
		Graphics.DrawString("y", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line + 10, 85), MainFont);
		Graphics.DrawString("z", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line + 10, 105), MainFont);
		Graphics.DrawString("" + Map.XDistance, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 20, 65), MainFont);
		Graphics.DrawString("" + Map.YDistance, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 20, 85), MainFont);
		Graphics.DrawString("" + Map.ZDistance, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 20, 105), MainFont);
		Graphics.DrawString("Render", Color.Black, new Point(window.Size.Width - BorderSize.Width + 10, 125), BigFont);
		Graphics.DrawString("Nodes", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Nodes", MainFont).Width, 150), MainFont);
		Graphics.DrawString("Paths", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Paths", MainFont).Width, 170), MainFont);
		Graphics.DrawString("Titles", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Titles", MainFont).Width, 190), MainFont);
		Graphics.DrawString("Structures", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Structures", MainFont).Width, 210), MainFont);
		Graphics.DrawString("" + RenderNodes, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, 150), MainFont);
		Graphics.DrawString("" + RenderPaths, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, 170), MainFont);
		Graphics.DrawString("" + RenderTitle, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, 190), MainFont);
		Graphics.DrawString("" + RenderStructure, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, 210), MainFont);
		Graphics.DrawString("Node Properties", Color.Black, new Point(window.Size.Width - BorderSize.Width + 10, 230), BigFont);
		Graphics.DrawString("Name", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Name", MainFont).Width, 255), MainFont);
		Graphics.DrawString("Position", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Position", MainFont).Width, 275), MainFont);
		Graphics.DrawString("Enabled", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Enabled", MainFont).Width, 295), MainFont);
		Graphics.DrawString("Structure", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Structure", MainFont).Width, 315), MainFont);
		Graphics.DrawString("type", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line + 10, 355), MainFont);
		EditGraph[10].Location = new Point(window.Size.Width - BorderSize.Width + line + 10, 315);
		EditGraph[11].Location = new Point(window.Size.Width - BorderSize.Width + line + 85, 315);
		EditGraph[12].Location = new Point(window.Size.Width - BorderSize.Width + line + 10, 335);
		EditGraph[13].Location = new Point(window.Size.Width - BorderSize.Width + line + 85, 335);
		EditGraph[14].Location = new Point(window.Size.Width - BorderSize.Width + line - EditGraph[14].Size.Width, 335);
		Graphics.DrawString("Paths", Color.Gray, new Point(window.Size.Width - BorderSize.Width + line - Graphics.GetTextSize("Paths", MainFont).Width, PathYBegin), MainFont);
		if (Selected != null)
		{
			Graphics.DrawString("" + Selected.object.Type.toString(), Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 40, 355), MainFont);
			if (Selected.object.Bound_Top_Left == null)
			{
				EditGraph[10].Text = "Top Left";
			}
			else
			{
				EditGraph[10].Text = "(" + (int)(Selected.object.Bound_Top_Left.X + 1) + ", " + (int)(Selected.object.Bound_Top_Left.Y + 1) + ", " + (int)(Selected.object.Bound_Top_Left.Z + 1) + ")";
			}
			if (Selected.object.Bound_Top_Right == null)
			{
				EditGraph[11].Text = "Top Right";
			}
			else
			{
				EditGraph[11].Text = "(" + (int)(Selected.object.Bound_Top_Right.X + 1) + ", " + (int)(Selected.object.Bound_Top_Right.Y + 1) + ", " + (int)(Selected.object.Bound_Top_Right.Z + 1) + ")";
			}
			if (Selected.object.Bound_Bottom_Left == null)
			{
				EditGraph[12].Text = "Bottom Left";
			}
			else
			{
				EditGraph[12].Text = "(" + (int)(Selected.object.Bound_Bottom_Left.X + 1) + ", " + (int)(Selected.object.Bound_Bottom_Left.Y + 1) + ", " + (int)(Selected.object.Bound_Bottom_Left.Z + 1) + ")";
			}
			if (Selected.object.Bound_Bottom_Right == null)
			{
				EditGraph[13].Text = "Bottom Right";
			}
			else
			{
				EditGraph[13].Text = "(" + (int)(Selected.object.Bound_Bottom_Right.X + 1) + ", " + (int)(Selected.object.Bound_Bottom_Right.Y + 1) + ", " + (int)(Selected.object.Bound_Bottom_Right.Z + 1) + ")";
			}
			Graphics.DrawString("" + Selected.Enabled, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, 295), MainFont);
			if (!Selected.FriendlyName.equals(""))
			{
				Graphics.DrawString(Selected.FriendlyName, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, 255), MainFont);
			}
			else
			{
				Graphics.DrawString(Selected.name, Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, 255), MainFont);				
			}
			int x = NodeGrid.GetNodeX(Selected) + 1;
			int y = NodeGrid.GetNodeY(Selected) + 1;
			int z = NodeGrid.GetNodeZ(Selected) + 1;
			Graphics.DrawString("(" + x + ", " + y + ", " + z + ")", Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, 275), MainFont);
			int loy = PathYBegin;
			for (Neighbor neighbor : Selected.Neighbors)
			{
				double d = Math.round(neighbor.distance * 100.0) / 100.0;
				x = NodeGrid.GetNodeX(neighbor.node) + 1;
				y = NodeGrid.GetNodeY(neighbor.node) + 1;
				z = NodeGrid.GetNodeZ(neighbor.node) + 1;
				Graphics.DrawString(d + ": " + "(" + x + ", " + y + ", " + z + ")", Color.Silver, new Point(window.Size.Width - BorderSize.Width + line + 10, loy), MainFont);
				loy += 20;
			}
		}
		else
		{
			EditGraph[10].Text = "";
			EditGraph[11].Text = "";
			EditGraph[12].Text = "";
			EditGraph[13].Text = "";
		}
		if (ShortestPath != null)
		{
			Graphics.DrawString("Distance: " + ShortestPath.distance, Color.Silver, new Point(0, 20), MainFont);	
			Graphics.DrawString("Direction: " + ShortestPath.GetDynamicDirections(), Color.Silver, new Point(0, 40), MainFont);				
		}
	}
}
