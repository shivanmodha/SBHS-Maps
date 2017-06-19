package studios.vanish.PathMapper;
import java.awt.Font;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
public class Editor
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
	int ShowDialog = 0;
	boolean EditMode = false;
	boolean BuildPath = false;
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
	boolean RenderNodes = true;
	boolean RenderPaths = true;
	boolean RenderTitle = true;
	boolean RenderStructure = true;
	int StructureSelect = -1;
	public Editor()
	{
		window = new Window("Node Graph Editor", new Size(800, 600), true);
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
		/*ToolbarButtons[0] = new Button(window, "File", new Point(0, 0), new Size(50, BorderSize.Height));
		ToolbarButtons[0].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[0].Type = ButtonType.Rectangle;
		ToolbarButtons[0].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[0].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[0].OnClick.Add(this, "Event_Toolbar_00");*/
		
		ToolbarButtons[1] = new Button(window, "New", new Point(0, 0), new Size(50, BorderSize.Height));
		ToolbarButtons[1].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[1].Type = ButtonType.Rectangle;
		ToolbarButtons[1].Type = ButtonType.Rectangle;
		ToolbarButtons[1].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[1].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[1].OnClick.Add(this, "Event_Toolbar_01");

		ToolbarBtns[0] = new Button[5];
		ToolbarBtns[0][0] = new Button(window, "Create", new Point(window.Size.Width - 55, window.Size.Height - 30), new Size(50, 25));
		ToolbarBtns[0][0].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarBtns[0][0].Type = ButtonType.Rectangle;
		ToolbarBtns[0][0].SetBackColor(Color.Green, ToolbarColors[2], ToolbarColors[4]);
		ToolbarBtns[0][0].SetForeColor(Color.White, ToolbarColors[3], ToolbarColors[5]);
		ToolbarBtns[0][0].OnClick.Add(this, "Event_Toolbar_Btns_0_0");

		ToolbarBtns[0][1] = new Button(window, "Cancel", new Point(window.Size.Width - 110, window.Size.Height - 30), new Size(50, 25));
		ToolbarBtns[0][1].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarBtns[0][1].Type = ButtonType.Rectangle;
		ToolbarBtns[0][1].SetBackColor(Color.Orange, ToolbarColors[2], ToolbarColors[4]);
		ToolbarBtns[0][1].SetForeColor(Color.White, ToolbarColors[3], ToolbarColors[5]);
		ToolbarBtns[0][1].OnClick.Add(this, "Event_Toolbar_Btns_0_1");

		ToolbarBtns[0][2] = new Button(window, "", new Point(146, 100), new Size(25, 10));
		ToolbarBtns[0][2].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarBtns[0][2].Type = ButtonType.Rectangle;
		ToolbarBtns[0][2].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarBtns[0][2].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);

		ToolbarBtns[0][3] = new Button(window, "", new Point(246, 100), new Size(25, 10));
		ToolbarBtns[0][3].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarBtns[0][3].Type = ButtonType.Rectangle;
		ToolbarBtns[0][3].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarBtns[0][3].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		
		ToolbarBtns[0][4] = new Button(window, "", new Point(346, 100), new Size(25, 10));
		ToolbarBtns[0][4].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarBtns[0][4].Type = ButtonType.Rectangle;
		ToolbarBtns[0][4].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarBtns[0][4].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);

		ToolbarButtons[2] = new Button(window, "Open", new Point(50, 0), new Size(50, BorderSize.Height));
		ToolbarButtons[2].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[2].Type = ButtonType.Rectangle;
		ToolbarButtons[2].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[2].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[2].OnClick.Add(this, "Event_Toolbar_02");

		ToolbarButtons[3] = new Button(window, "Save", new Point(100, 0), new Size(50, BorderSize.Height));
		ToolbarButtons[3].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[3].Type = ButtonType.Rectangle;
		ToolbarButtons[3].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[3].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[3].OnClick.Add(this, "Event_Toolbar_03");

		ToolbarButtons[4] = new Button(window, "3D", new Point(150, 0), new Size(50, BorderSize.Height));
		ToolbarButtons[4].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[4].Type = ButtonType.Rectangle;
		ToolbarButtons[4].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[4].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[4].OnClick.Add(this, "Event_Toolbar_04");

		ToolbarButtons[5] = new Button(window, "\u21F1", new Point(200, 0), new Size(BorderSize.Height, BorderSize.Height));
		ToolbarButtons[5].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[5].Type = ButtonType.Rectangle;
		ToolbarButtons[5].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[5].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[5].OnClick.Add(this, "Event_Toolbar_05");
		
		ToolbarButtons[6] = new Button(window, "\u203B", new Point(200 + BorderSize.Height, 0), new Size(BorderSize.Height, BorderSize.Height));
		ToolbarButtons[6].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[6].Type = ButtonType.Rectangle;
		ToolbarButtons[6].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[6].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[6].OnClick.Add(this, "Event_Toolbar_06");

		ToolbarButtons[7] = new Button(window, "\u21BB", new Point(200 + (BorderSize.Height * 2), 0), new Size(BorderSize.Height, BorderSize.Height));
		ToolbarButtons[7].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[7].Type = ButtonType.Rectangle;
		ToolbarButtons[7].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[7].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[7].OnClick.Add(this, "Event_Toolbar_07");
		
		ToolbarButtons[8] = new Button(window, "Edit", new Point(200 + (BorderSize.Height * 3), 0), new Size(50, BorderSize.Height));
		ToolbarButtons[8].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[8].Type = ButtonType.Rectangle;
		ToolbarButtons[8].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[8].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[8].OnClick.Add(this, "Event_Toolbar_08");

		ToolbarButtons[9] = new Button(window, "\u25BC", new Point(250 + (BorderSize.Height * 3), 0), new Size(BorderSize.Height, BorderSize.Height));
		ToolbarButtons[9].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[9].Type = ButtonType.Rectangle;
		ToolbarButtons[9].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[9].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[9].OnClick.Add(this, "Event_Toolbar_09");

		ToolbarButtons[10] = new Button(window, "\u25B2", new Point(250 + (BorderSize.Height * 4), 0), new Size(BorderSize.Height, BorderSize.Height));
		ToolbarButtons[10].TextFont = new Font("Helvetica", Font.PLAIN, 12);
		ToolbarButtons[10].Type = ButtonType.Rectangle;
		ToolbarButtons[10].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		ToolbarButtons[10].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		ToolbarButtons[10].OnClick.Add(this, "Event_Toolbar_10");
		
		Edit_Build_Path = new Button(window, "Add Path", new Point(window.Size.Width - 100, window.Size.Height - 30), new Size(95, 25));
		Edit_Build_Path.TextFont = new Font("Helvetica", Font.PLAIN, 12);
		Edit_Build_Path.Type = ButtonType.Rectangle;
		Edit_Build_Path.SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
		Edit_Build_Path.SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);
		Edit_Build_Path.OnClick.Add(this, "Event_Build_Path");
		
		EditGraph[0] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 25), new Size(20, 20));
		EditGraph[0].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[0].Type = ButtonType.Rectangle;
		EditGraph[0].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[0].SetForeColor(Color.Gold, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[0].OnClick.Add(this, "Event_Rename_Graph");

		EditGraph[1] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 255), new Size(20, 20));
		EditGraph[1].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[1].Type = ButtonType.Rectangle;
		EditGraph[1].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[1].SetForeColor(Color.Gold, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[1].OnClick.Add(this, "Event_Rename_Node");
		
		EditGraph[2] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 65), new Size(20, 20));
		EditGraph[2].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[2].Type = ButtonType.Rectangle;
		EditGraph[2].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[2].SetForeColor(Color.Gold, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[2].OnClick.Add(this, "Event_X_Distance");

		EditGraph[3] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 85), new Size(20, 20));
		EditGraph[3].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[3].Type = ButtonType.Rectangle;
		EditGraph[3].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[3].SetForeColor(Color.Gold, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[3].OnClick.Add(this, "Event_Y_Distance");

		EditGraph[4] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 105), new Size(20, 20));
		EditGraph[4].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[4].Type = ButtonType.Rectangle;
		EditGraph[4].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[4].SetForeColor(Color.Gold, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[4].OnClick.Add(this, "Event_Z_Distance");
		
		EditGraph[5] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 295), new Size(20, 20));
		EditGraph[5].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[5].Type = ButtonType.Rectangle;
		EditGraph[5].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[5].SetForeColor(Color.Gold, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[5].OnClick.Add(this, "Event_Toggle_Enabled");

		EditGraph[6] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 150), new Size(20, 20));
		EditGraph[6].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[6].Type = ButtonType.Rectangle;
		EditGraph[6].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[6].SetForeColor(Color.SlateGray, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[6].OnClick.Add(this, "Event_Toggle_Render_Node");

		EditGraph[7] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 170), new Size(20, 20));
		EditGraph[7].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[7].Type = ButtonType.Rectangle;
		EditGraph[7].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[7].SetForeColor(Color.SlateGray, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[7].OnClick.Add(this, "Event_Toggle_Render_Path");

		EditGraph[8] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 190), new Size(20, 20));
		EditGraph[8].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[8].Type = ButtonType.Rectangle;
		EditGraph[8].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[8].SetForeColor(Color.SlateGray, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[8].OnClick.Add(this, "Event_Toggle_Render_Title");

		EditGraph[9] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 210), new Size(20, 20));
		EditGraph[9].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[9].Type = ButtonType.Rectangle;
		EditGraph[9].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[9].SetForeColor(Color.SlateGray, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[9].OnClick.Add(this, "Event_Toggle_Render_Structure");
		
		EditGraph[10] = new Button(window, "Top Left", new Point(0, 0), new Size(75, 20));
		EditGraph[10].TextFont = MainFont;
		EditGraph[10].Type = ButtonType.Rectangle;
		EditGraph[10].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[10].SetForeColor(Color.SlateGray, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[10].OnClick.Add(this, "Event_Toggle_TL");

		EditGraph[11] = new Button(window, "Top Right", new Point(0, 0), new Size(75, 20));
		EditGraph[11].TextFont = MainFont;
		EditGraph[11].Type = ButtonType.Rectangle;
		EditGraph[11].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[11].SetForeColor(Color.SlateGray, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[11].OnClick.Add(this, "Event_Toggle_TR");

		EditGraph[12] = new Button(window, "Bottom Left", new Point(0, 0), new Size(75, 20));
		EditGraph[12].TextFont = MainFont;
		EditGraph[12].Type = ButtonType.Rectangle;
		EditGraph[12].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[12].SetForeColor(Color.SlateGray, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[12].OnClick.Add(this, "Event_Toggle_BL");

		EditGraph[13] = new Button(window, "Bottom Right", new Point(0, 0), new Size(75, 20));
		EditGraph[13].TextFont = MainFont;
		EditGraph[13].Type = ButtonType.Rectangle;
		EditGraph[13].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[13].SetForeColor(Color.SlateGray, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[13].OnClick.Add(this, "Event_Toggle_BR");

		EditGraph[14] = new Button(window, "Clear", new Point(0, 0), new Size(30, 20));
		EditGraph[14].TextFont = new Font("Helvetica", Font.ITALIC, 9);
		EditGraph[14].Type = ButtonType.Rectangle;
		EditGraph[14].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[14].SetForeColor(Color.Red, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[14].OnClick.Add(this, "Event_Structure_Clear");
		
		EditGraph[15] = new Button(window, "\u25c9", new Point(window.Size.Width - 25, 355), new Size(20, 20));
		EditGraph[15].TextFont = new Font("Helvetica", Font.PLAIN, 15);
		EditGraph[15].Type = ButtonType.Rectangle;
		EditGraph[15].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
		EditGraph[15].SetForeColor(Color.Gold, ToolbarColors[3], ToolbarColors[5]);
		EditGraph[15].OnClick.Add(this, "Event_Toggle_StructureType");
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
	public void Event_Toggle_StructureType(Button Sender, Point MouseLocation, int Button)
	{
		if (Selected != null)
		{
			if (Selected.object.Type == StructureType.Building)
			{
				Selected.object.Type = StructureType.Path;				
			}
			else if (Selected.object.Type == StructureType.Path)
			{
				Selected.object.Type = StructureType.WastedSpace;				
			}
			else if (Selected.object.Type == StructureType.WastedSpace)
			{
				Selected.object.Type = StructureType.Building;				
			}
		}
	}
	public void Event_Structure_Clear(Button Sender, Point MouseLocation, int Button)
	{
		if (Selected != null)
		{
			Selected.object.Bound_Bottom_Left = null;
			Selected.object.Bound_Top_Left = null;
			Selected.object.Bound_Bottom_Right = null;
			Selected.object.Bound_Top_Right = null;
		}
	}
	public void Event_Toggle_TL(Button Sender, Point MouseLocation, int Button)
	{
		if (StructureSelect != 0)
		{
			StructureSelect = 0;
		}
		else
		{
			StructureSelect = -1;
		}
	}
	public void Event_Toggle_TR(Button Sender, Point MouseLocation, int Button)
	{
		if (StructureSelect != 1)
		{
			StructureSelect = 1;
		}
		else
		{
			StructureSelect = -1;
		}	
	}
	public void Event_Toggle_BL(Button Sender, Point MouseLocation, int Button)
	{
		if (StructureSelect != 2)
		{
			StructureSelect = 2;
		}
		else
		{
			StructureSelect = -1;
		}	
	}
	public void Event_Toggle_BR(Button Sender, Point MouseLocation, int Button)
	{
		if (StructureSelect != 3)
		{
			StructureSelect = 3;
		}
		else
		{
			StructureSelect = -1;
		}	
	}
	public void Event_Toggle_Render_Node(Button Sender, Point MouseLocation, int Button)
	{
		RenderNodes = !RenderNodes;
	}
	public void Event_Toggle_Render_Path(Button Sender, Point MouseLocation, int Button)
	{
		RenderPaths = !RenderPaths;
	}
	public void Event_Toggle_Render_Title(Button Sender, Point MouseLocation, int Button)
	{
		RenderTitle = !RenderTitle;
	}
	public void Event_Toggle_Render_Structure(Button Sender, Point MouseLocation, int Button)
	{
		RenderStructure = !RenderStructure;
	}
	public void Event_Toggle_Enabled(Button Sender, Point MouseLocation, int Button)
	{
		Selected.Enabled = !Selected.Enabled;
	}
	public void Event_Remove_Path(Button Sender, Point MouseLocation, int Button)
	{
		int index = -1;
		for (int i = 0; i < EditRemovePath.length; i++)
		{
			if (Sender == EditRemovePath[i])
			{
				index = i;
			}
		}
		for (int i = Selected.Neighbors.get(index).node.Neighbors.size() - 1; i >= 0; i--)
		{
			if (Selected.Neighbors.get(index).node.Neighbors.get(i).node == Selected && Selected.Neighbors.get(index).node.Neighbors.get(i).distance == Selected.Neighbors.get(index).distance)
			{
				Selected.Neighbors.get(index).node.Neighbors.remove(i);
				break;
			}
		}
		Selected.Neighbors.remove(index);
		EditRemovePath = new Button[Selected.Neighbors.size()];
		EditChangePath = new Button[Selected.Neighbors.size()];
		for (int i = 0; i < EditRemovePath.length; i++)
		{
			EditRemovePath[i] = new Button(window, xStr, new Point(window.Size.Width - 25, PathYBegin + (20 * i)), new Size(20, 20));
			EditRemovePath[i].TextFont = new Font("Helvetica", Font.PLAIN, 10);
			EditRemovePath[i].Type = ButtonType.Rectangle;
			EditRemovePath[i].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
			EditRemovePath[i].SetForeColor(Color.Red, ToolbarColors[3], ToolbarColors[5]);
			EditRemovePath[i].OnClick.Add(this, "Event_Remove_Path");

			EditChangePath[i] = new Button(window, "\u25c9", new Point(window.Size.Width - 45, PathYBegin + (20 * i)), new Size(20, 20));
			EditChangePath[i].TextFont = new Font("Helvetica", Font.PLAIN, 15);
			EditChangePath[i].Type = ButtonType.Rectangle;
			EditChangePath[i].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
			EditChangePath[i].SetForeColor(Color.Gold, ToolbarColors[3], ToolbarColors[5]);
			EditChangePath[i].OnClick.Add(this, "Event_Change_Path");
		}
	}
	public void Event_Change_Path(Button Sender, Point MouseLocation, int Button)
	{
		int index = -1;
		for (int i = 0; i < EditChangePath.length; i++)
		{
			if (Sender == EditChangePath[i])
			{
				index = i;
			}
		}
		Object obj = JOptionPane.showInputDialog(window.GetInnerWindow(), "Enter Direction", Selected.Neighbors.get(index).direction);
		if (obj instanceof String)
		{
			Selected.Neighbors.get(index).direction = (String)obj;
		}
	}
	public void Event_Rename_Node(Button Sender, Point MouseLocation, int Button)
	{
		if (Selected != null)
		{
			Object obj = JOptionPane.showInputDialog(window.GetInnerWindow(), "Enter Name", Selected.FriendlyName);
			if (obj instanceof String)
			{
				Selected.FriendlyName = (String)obj;
			}
		}
		else
		{
			JOptionPane.showMessageDialog(window.GetInnerWindow(), "Please select a node to rename.", "It's your fault, not mine", 0);
		}
	}
	public void Event_Rename_Graph(Button Sender, Point MouseLocation, int Button)
	{
		Object obj = JOptionPane.showInputDialog(window.GetInnerWindow(), "Enter Name", Map.Name);
		if (obj instanceof String)
		{
			Map.Name = (String)obj;
		}
	}
	public void Event_X_Distance(Button Sender, Point MouseLocation, int Button)
	{
		Object obj = JOptionPane.showInputDialog(window.GetInnerWindow(), "Enter X Distance", Map.XDistance);
		try
		{
			Map.XDistance = Double.parseDouble((String)obj);
		}
		catch (Exception e)
		{
			
		}
	}
	public void Event_Y_Distance(Button Sender, Point MouseLocation, int Button)
	{
		Object obj = JOptionPane.showInputDialog(window.GetInnerWindow(), "Enter Y Distance", Map.YDistance);
		try
		{
			Map.YDistance = Double.parseDouble((String)obj);
		}
		catch (Exception e)
		{
			
		}
	}
	public void Event_Z_Distance(Button Sender, Point MouseLocation, int Button)
	{
		Object obj = JOptionPane.showInputDialog(window.GetInnerWindow(), "Enter Z Distance", Map.ZDistance);
		try
		{
			Map.ZDistance = Double.parseDouble((String)obj);
		}
		catch (Exception e)
		{
			
		}
	}
	public void Event_Build_Path(Button Sender, Point MouseLocation, int Button)
	{
		BuildPath = !BuildPath;
	}
	public void Event_NodeClick(Button Sender, Point MouseLocation, int Button)
	{
		int x = 0;
		int y = 0;
		int z = 0;
		for (int i = 0; i < Map.GetYMax(); i++)
		{
			for (int j = 0; j < Map.GetXMax(); j++)
			{
				for (int k = 0; k < Map.GetZMax(); k++)
				{
					if (Nodes[i][j][k] == Sender)
					{
						y = i;
						x = j;
						z = k;
					}
				}
			}
		}
		if (ShowDialog == 0 && StructureSelect < 0)
		{
			MouseLocation = window.GetMousePosition().subtract(window.Location).subtract(new Point(9, 31));
			if (MouseLocation.X < window.Size.Width - BorderSize.Width && MouseLocation.Y > 20)
			{
				if (EditMode)
				{
					if (Selected == null)
					{
						Selected = Map.Get(x, y, z);
					}
					else
					{
						if (BuildPath)
						{
							double distance = Math.sqrt(Math.pow((x - NodeGrid.GetNodeX(Selected)) * Map.XDistance, 2) + Math.pow((y - NodeGrid.GetNodeY(Selected)) * Map.YDistance, 2) + Math.pow((z - NodeGrid.GetNodeZ(Selected)) * Map.ZDistance, 2));
							Selected.add(Map.Get(x, y, z), distance, true);
						}
						else
						{
							Selected = Map.Get(x, y, z);		
						}
					}
					EditRemovePath = new Button[Selected.Neighbors.size()];
					EditChangePath = new Button[Selected.Neighbors.size()];
					for (int i = 0; i < EditRemovePath.length; i++)
					{
						EditRemovePath[i] = new Button(window, xStr, new Point(window.Size.Width - 25, PathYBegin + (20 * i)), new Size(20, 20));
						EditRemovePath[i].TextFont = new Font("Helvetica", Font.PLAIN, 10);
						EditRemovePath[i].Type = ButtonType.Rectangle;
						EditRemovePath[i].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
						EditRemovePath[i].SetForeColor(Color.Red, ToolbarColors[3], ToolbarColors[5]);
						EditRemovePath[i].OnClick.Add(this, "Event_Remove_Path");					
	
						EditChangePath[i] = new Button(window, "\u25c9", new Point(window.Size.Width - 45, PathYBegin + (20 * i)), new Size(20, 20));
						EditChangePath[i].TextFont = new Font("Helvetica", Font.PLAIN, 15);
						EditChangePath[i].Type = ButtonType.Rectangle;
						EditChangePath[i].SetBackColor(ControlDark, ToolbarColors[2], ToolbarColors[4]);
						EditChangePath[i].SetForeColor(Color.Gold, ToolbarColors[3], ToolbarColors[5]);
						EditChangePath[i].OnClick.Add(this, "Event_Change_Path");					
					}
				}
				else
				{
					if (Path_Start == null)
					{
						Path_Start = Map.Get(x, y, z);
						Path_End = null;
						ShortestPath = null;
					}
					else if (Path_End == null)
					{
						Path_End = Map.Get(x, y, z);
						ShortestPath = null;
						Calculate(false);
					}
					else
					{
						Path_Start = Map.Get(x, y, z);
						Path_End = null;
						ShortestPath = null;
					}
				}
			}
		}
		if (StructureSelect == 0)
		{
			Selected.object.Bound_Top_Left = new GraphVertex(x, y, z);
			StructureSelect = 1;
		}
		else if (StructureSelect == 1)
		{
			Selected.object.Bound_Top_Right = new GraphVertex(x, y, z);	
			StructureSelect = 2;
		}
		else if (StructureSelect == 2)
		{
			Selected.object.Bound_Bottom_Left = new GraphVertex(x, y, z);
			StructureSelect = 3;
		}
		else if (StructureSelect == 3)
		{
			Selected.object.Bound_Bottom_Right = new GraphVertex(x, y, z);		
			StructureSelect = -1;
		}
	}
	public void Calculate(boolean print)
	{
		ShortestPath = Map.GetPath(Path_Start, Path_End);
	}
	public void Event_Toolbar_Btns_0_0(Button Sender, Point MouseLocation, int Button)
	{
		ShowDialog = 0;
		New(NEW_X, NEW_Y, NEW_Z, "Untitled");
	}
	public void Event_Toolbar_Btns_0_1(Button Sender, Point MouseLocation, int Button)
	{
		ShowDialog = 0;
	}
	public void Event_Toolbar_00(Button Sender, Point MouseLocation, int Button)
	{
		
	}
	public void Event_Toolbar_01(Point MouseLocation, int Button)
	{
		ShowDialog = 1;
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
	public void Event_Toolbar_08(Button Sender, Point MouseLocation, int Button)
	{
		EditMode = !EditMode;
		if (EditMode)
		{
			ToolbarButtons[8].SetBackColor(Toggled[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[8].SetForeColor(Toggled[1], ToolbarColors[3], ToolbarColors[5]);		
		}
		else
		{
			ToolbarButtons[8].SetBackColor(ToolbarColors[0], ToolbarColors[2], ToolbarColors[4]);
			ToolbarButtons[8].SetForeColor(ToolbarColors[1], ToolbarColors[3], ToolbarColors[5]);				
		}
		Selected = null;
		Path_Start = null;
		Path_End = null;
		ShortestPath = null;
		StructureSelect = -1;
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
		if (ShowDialog == 0)
		{
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
		if (ToolbarBtns[0][2].GetState() == 2)
		{
			ToolbarBtns[0][2].Location.Y = MouseLocation.Y - 7;
			if (ToolbarBtns[0][2].Location.Y < 100)
			{
				ToolbarBtns[0][2].Location.Y = 100;
			}
			else if (ToolbarBtns[0][2].Location.Y > 400)
			{
				ToolbarBtns[0][2].Location.Y = 400;				
			}
			int x = (int)ToolbarBtns[0][2].Location.Y - 100;
			x /= 6;
			NEW_X = x + 5;
		}
		if (ToolbarBtns[0][3].GetState() == 2)
		{
			ToolbarBtns[0][3].Location.Y = MouseLocation.Y - 7;
			if (ToolbarBtns[0][3].Location.Y < 100)
			{
				ToolbarBtns[0][3].Location.Y = 100;
			}
			else if (ToolbarBtns[0][3].Location.Y > 400)
			{
				ToolbarBtns[0][3].Location.Y = 400;				
			}
			int y = (int)ToolbarBtns[0][3].Location.Y - 100;
			y /= 6;
			NEW_Y = y + 5;
		}
		if (ToolbarBtns[0][4].GetState() == 2)
		{
			ToolbarBtns[0][4].Location.Y = MouseLocation.Y - 7;
			if (ToolbarBtns[0][4].Location.Y < 100)
			{
				ToolbarBtns[0][4].Location.Y = 100;
			}
			else if (ToolbarBtns[0][4].Location.Y > 240)
			{
				ToolbarBtns[0][4].Location.Y = 240;				
			}
			int z = (int)ToolbarBtns[0][4].Location.Y - 100;
			z /= 10;
			NEW_Z = z + 1;
		}
	}
	public void Event_Update()
	{
		for (int i = 0; i < 4; i++)
		{
			if (i != StructureSelect)
			{
				EditGraph[10 + i].BackColor[0] = ControlDark;
				EditGraph[10 + i].ForeColor[0] = Color.SlateGray;				
			}
			else
			{
				EditGraph[10 + StructureSelect].BackColor[0] = Toggled[0];
				EditGraph[10 + StructureSelect].ForeColor[0] = Toggled[1];				
			}
		}
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
		if (EditMode)
		{
			if (BorderSize.Width < BorderSizeMax.Width)
			{
				window.Wait(1);
				BorderSize.Width++;
				Event_Resize(null, null);
			}
			Edit_Build_Path.Enabled = true;
			EditGraph[0].Enabled = true;
			EditGraph[0].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 25);
			EditGraph[1].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 255);
			EditGraph[2].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 65);
			EditGraph[3].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 85);
			EditGraph[4].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 105);
			EditGraph[5].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 295);
			EditGraph[6].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 150);
			EditGraph[7].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 170);
			EditGraph[8].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 190);
			EditGraph[9].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 210);
			EditGraph[15].Location = new Point(window.Size.Width - 25 + (BorderSizeMax.Width - BorderSize.Width), 355);
			Edit_Build_Path.Location = new Point(window.Size.Width - 100 + (BorderSizeMax.Width - BorderSize.Width), window.Size.Height - 30);
			if (BuildPath)
			{
				Edit_Build_Path.SetBackColor(Toggled[0], ToolbarColors[2], ToolbarColors[4]);
				Edit_Build_Path.SetForeColor(Toggled[1], ToolbarColors[3], ToolbarColors[5]);		
			}
			else
			{
				Edit_Build_Path.SetBackColor(Color.Honeydew, ToolbarColors[2], ToolbarColors[4]);
				Edit_Build_Path.SetForeColor(Color.Green, ToolbarColors[3], ToolbarColors[5]);				
			}
		}
		else
		{
			if (BorderSize.Width > 0)
			{
				EditRemovePath = null;
				EditChangePath = null;
				window.Wait(1);
				BorderSize.Width--;
				Event_Resize(null, null);
				Edit_Build_Path.Enabled = false;
				EditGraph[0].Enabled = false;
			}
		}
		if (ShowDialog != 0)
		{
			for (int i = 0; i < ToolbarButtons.length; i++)
			{
				if (ToolbarButtons[i] != null)
				{
					ToolbarButtons[i].Enabled = false;
				}
			}
			for (int i = 0; i < ToolbarBtns[ShowDialog - 1].length; i++)
			{
				if (ToolbarBtns[ShowDialog - 1][i] != null)
				{
					ToolbarBtns[ShowDialog - 1][i].Enabled = true;
				}
			}
			Edit_Build_Path.Enabled = false;
		}
		else
		{
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
		}
		if (View3D)
		{			
			for (int i = 0; i < Map.GetYMax(); i++)
			{
				for (int j = 0; j < Map.GetXMax(); j++)
				{
					for (int k = 0; k < Map.GetZMax(); k++)
					{
						if (!hold && Nodes[i][j][k] != null)
						{
							if (POINTER == 0)
							{
								
								if (Map.Get(j, i, k).Neighbors.size() == 0)
								{
									Nodes[i][j][k].Enabled = EditMode;	
								}
								else
								{
									Nodes[i][j][k].Enabled = true;										
								}		
							}
							else
							{
								Nodes[i][j][k].Enabled = false;
							}
						}
					}
				}
			}
		}
		else
		{
			for (int i = 0; i < Map.GetYMax(); i++)
			{
				for (int j = 0; j < Map.GetXMax(); j++)
				{
					for (int k = 0; k < Map.GetZMax(); k++)
					{
						if (!hold && Nodes[i][j][k] != null)
						{
							if (k == RenderFloor2D)
							{
								if (POINTER == 0)
								{							
									if (Map.Get(j, i, k).Neighbors.size() == 0)
									{
										Nodes[i][j][k].Enabled = EditMode;	
									}
									else
									{
										Nodes[i][j][k].Enabled = true;										
									}		
								}
								else
								{
									Nodes[i][j][k].Enabled = false;
								}				
							}
							else
							{
								Nodes[i][j][k].Enabled = false;
							}
						}
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
		if (ShowDialog != 0)
		{
			Render_Dialog(Graphics);
		}
		window.SetTitle("Graph Editor - " + Map.Name);
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
		if (ShowDialog == 0)
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
	}	
	public void Render_Clear(GraphicsUnit Graphics)
	{
		Graphics.FillRectangle(Color.White, new Point(0, 0), window.Size);
	}
	public void Render_2D(GraphicsUnit Graphics)
	{
		Size ContentSize = window.Size.subtract(BorderSize);
		double xmax = Map.GetYMax();
		double ymax = Map.GetXMax();
		Point Offset3D = new Point(0.50, 0);
		Vertex OriginVertex = new Vertex(-Offset3D.X - (ymax / 2), -Offset3D.Y - ((xmax / 2)) - 1, 0);
		OriginVertex = OriginVertex.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
		OriginVertex = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, OriginVertex);
		OriginVertex = Graphics.D3D_ToProjection(OriginVertex, ContentSize, new Vertex(0, -20, 0));
		Point Origin = new Point(OriginVertex.X, OriginVertex.Y);

		Vertex XVertex = new Vertex(-Offset3D.X - (ymax / -2), -Offset3D.Y - (xmax / 2) - 1, 0);
		XVertex = XVertex.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
		XVertex  = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, XVertex);
		XVertex = Graphics.D3D_ToProjection(XVertex, ContentSize, new Vertex(0, -20, 0));
		Point XPT = new Point(XVertex.X, XVertex.Y);

		Vertex YVertex = new Vertex(-Offset3D.X - (ymax / 2), -Offset3D.Y - (xmax / -2) - 1, 0);
		YVertex = YVertex.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
		YVertex  = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, YVertex);
		YVertex = Graphics.D3D_ToProjection(YVertex, ContentSize, new Vertex(0, -20, 0));
		Point YPT = new Point(YVertex.X, YVertex.Y);

		Graphics.DrawLine(Color.Firebrick, new Point[]{Origin, XPT});
		Graphics.DrawLine(Color.Firebrick, new Point[]{Origin, YPT});
		
		
		int k = RenderFloor2D;
		for (int i = 0; i < Map.GetYMax(); i++)
		{
			for (int j = 0; j < Map.GetXMax(); j++)
			{
				if (RenderStructure)
				{
					if (Map.Get(j, i, k).object.Bound_Top_Left != null && Map.Get(j, i, k).object.Bound_Top_Right != null && Map.Get(j, i, k).object.Bound_Bottom_Left != null && Map.Get(j, i, k).object.Bound_Bottom_Right != null)
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
							Graphics.FillPolygon(Color.WhiteSmoke, polygon);
							Graphics.DrawPolygon(Color.Gainsboro, polygon);
						}
						if (type == StructureType.WastedSpace)
						{
							if (!EditMode)
							{
								Graphics.FillPolygon(Color.Black, polygon);
							}
							else if (EditMode)
							{
								Graphics.FillPolygon(Color.Silver, polygon);
							}
						}
						if (type == StructureType.Path)
						{
							Graphics.FillPolygon(Color.PaleGoldenRod, polygon);
						}
					}
				}
			}
		}
		if (Selected != null && BuildPath)
		{
			Graphics.DrawLine(Color.SkyBlue, new Point[]{Nodes[NodeGrid.GetNodeY(Selected)][NodeGrid.GetNodeX(Selected)][NodeGrid.GetNodeZ(Selected)].Location, window.GetMousePosition().subtract(window.Location).subtract(new Point(5, 30))});
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
						if (EditMode)
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
				Vertex Location = new Vertex((j - 1) - (ymax / 2),  (i - 1) - (xmax / 2), 0);
				Location = Location.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
				Location = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph2DRotation, Location);
				Location = Graphics.D3D_ToProjection(Location, ContentSize, new Vertex(0, -20, 0));
				if (i == 0)
				{
					boolean change = false;
					if (Location.Y > window.Size.Height - 20)
					{
						Location.Y = window.Size.Height - 20;
						change = true;
					}
					if (!change || (change && j != 0))
					{
						String str = j + "";
						if (j < 10)
						{
							str = "0" + str;
						}
						Graphics.DrawString(str, Color.Black, Location.toPoint(), new Font("Courier New", Font.PLAIN, 15));
					}
				}
				if (j == 0)
				{
					boolean change = false;
					if (Location.X < 5)
					{
						Location.X = 5;
						change = true;
					}
					if (!change || (change && i != 0))
					{
						String str = i + "";
						if (i < 10)
						{
							str = "0" + str;
						}
						Graphics.DrawString(str, Color.Black, Location.toPoint(), new Font("Courier New", Font.PLAIN, 15));	
					}
				}
			}
		}
		if (EditMode)
		{
			Point MouseLocation = window.GetMousePosition().subtract(window.Location).subtract(new Point(9, 31));
			Graphics.DrawLine(Color.Silver, new Point[]{new Point(0, MouseLocation.Y), new Point(window.Size.Width, MouseLocation.Y)});
			Graphics.DrawLine(Color.Silver, new Point[]{new Point(MouseLocation.X, 0), new Point(MouseLocation.X, window.Size.Height)});
		}
	}
	public void Render_3D(GraphicsUnit Graphics)
	{
		Size ContentSize = window.Size.subtract(BorderSize);
		double xmax = Map.GetYMax();
		double ymax = Map.GetXMax();
		double zmax = Map.GetZMax();
		Point Offset3D = new Point(0.5, 1.5);
		Vertex OriginVertex = new Vertex(-Offset3D.X - (ymax / 2), -Offset3D.Y - ((xmax / 2)), (zmax / 2) + 1);
		OriginVertex = OriginVertex.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
		OriginVertex = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph3DRotation, OriginVertex);
		OriginVertex = Graphics.D3D_ToProjection(OriginVertex, ContentSize, new Vertex(0, -20, 0));
		Point Origin = new Point(OriginVertex.X, OriginVertex.Y);

		Vertex XVertex = new Vertex(-Offset3D.X - (ymax / -2) + 0.25, -Offset3D.Y - (xmax / 2), (zmax / 2) + 1);
		XVertex = XVertex.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
		XVertex  = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph3DRotation, XVertex);
		XVertex = Graphics.D3D_ToProjection(XVertex, ContentSize, new Vertex(0, -20, 0));
		Point XPT = new Point(XVertex.X, XVertex.Y);

		Vertex YVertex = new Vertex(-Offset3D.X - (ymax / 2), -Offset3D.Y - (xmax / -2) + 0.25, (zmax / 2) + 1);
		YVertex = YVertex.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
		YVertex  = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph3DRotation, YVertex);
		YVertex = Graphics.D3D_ToProjection(YVertex, ContentSize, new Vertex(0, -20, 0));
		Point YPT = new Point(YVertex.X, YVertex.Y);
		
		Vertex ZVertex = new Vertex(-Offset3D.X - (ymax / 2), -Offset3D.Y - ((xmax / 2)), -(zmax / 2) + 1);
		ZVertex = ZVertex.multiply(new Vertex(Map.XDistance, Map.YDistance, Map.ZDistance));
		ZVertex  = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Graph3DRotation, ZVertex);
		ZVertex = Graphics.D3D_ToProjection(ZVertex, ContentSize, new Vertex(0, -20, 0));
		Point ZPT = new Point(ZVertex.X, ZVertex.Y);
		
		Graphics.DrawLine(Color.Firebrick, new Point[]{Origin, XPT});
		Graphics.DrawLine(Color.Firebrick, new Point[]{Origin, YPT});	
		Graphics.DrawLine(Color.Firebrick, new Point[]{Origin, ZPT});
		
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
								if (!EditMode)
								{
									Graphics.FillPolygon(Color.Black, polygon);
								}
								else if (EditMode)
								{
									Graphics.FillPolygon(Color.Silver, polygon);
								}
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
		if (Selected != null && BuildPath)
		{
			Graphics.DrawLine(Color.SkyBlue, new Point[]{Nodes[NodeGrid.GetNodeY(Selected)][NodeGrid.GetNodeX(Selected)][NodeGrid.GetNodeZ(Selected)].Location, window.GetMousePosition().subtract(window.Location).subtract(new Point(5, 30))});
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
							if (EditMode)
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
		if (EditMode)
		{
			Point MouseLocation = window.GetMousePosition().subtract(window.Location).subtract(new Point(9, 31));
			Graphics.DrawLine(Color.Silver, new Point[]{new Point(0, MouseLocation.Y), new Point(window.Size.Width, MouseLocation.Y)});
			Graphics.DrawLine(Color.Silver, new Point[]{new Point(MouseLocation.X, 0), new Point(MouseLocation.X, window.Size.Height)});
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
		if (EditMode)
		{
			Edit_Build_Path.Render(Graphics);
			for (int i = 0; i < EditGraph.length; i++)
			{
				if (EditGraph[i] != null)
				{
					EditGraph[i].Render(Graphics);
				}
			}
			if (EditRemovePath != null)
			{
				for (int i = 0; i < EditRemovePath.length; i++)
				{
					if (EditRemovePath[i] != null)
					{
						EditRemovePath[i].Render(Graphics);
						EditChangePath[i].Render(Graphics);
					}
				}
			}
		}
		else
		{
			if (ShortestPath != null)
			{
				Graphics.DrawString("Distance: " + ShortestPath.distance, Color.Silver, new Point(0, 20), MainFont);	
				Graphics.DrawString("Direction: " + ShortestPath.GetDynamicDirections(), Color.Silver, new Point(0, 40), MainFont);				
			}
		}
	}
	public void Render_Dialog(GraphicsUnit Graphics)
	{
		int dialogLevel = ShowDialog - 1;
		Graphics.FillRectangle(DialogBackground[dialogLevel], new Point(0, 0), window.Size);
		if (dialogLevel == 0)
		{
			Graphics.DrawString("New Graph", Color.Yellow, new Point(50, 50), BigFont);
			Graphics.DrawString("Warning: All unsaved work will be lost.", Color.Red, new Point(window.Size.Width - 375, window.Size.Height - 30), BigFont);
			Graphics.DrawString("Size:", Color.Yellow, new Point(70, 70), BigFont);
			Graphics.DrawString("" + NEW_X, Color.White, new Point(150, 70), BigFont);
			Graphics.DrawString("" + NEW_Y, Color.White, new Point(250, 70), BigFont);
			Graphics.DrawString("" + NEW_Z, Color.White, new Point(350, 70), BigFont);
			Graphics.DrawRectangle(Color.Silver, new Point(157, 100), new Size(1, 310));
			Graphics.DrawRectangle(Color.Silver, new Point(257, 100), new Size(1, 310));
			Graphics.DrawRectangle(Color.Silver, new Point(357, 100), new Size(1, 150));			
		}
		for (int i = 0; i < ToolbarBtns[dialogLevel].length; i++)
		{
			if (ToolbarBtns[dialogLevel][i] != null)
			{
				ToolbarBtns[dialogLevel][i].Render(Graphics);
			}
		}
	}
}
