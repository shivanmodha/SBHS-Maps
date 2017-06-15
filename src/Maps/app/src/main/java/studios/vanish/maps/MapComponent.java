package studios.vanish.maps;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import Engine.Camera;
import Engine.Color;
import Engine.FillMode;
import Engine.GraphicsUnit;
import Engine.Point;
import Engine.Size;
import Engine.Vertex;
import Graph.Node;
import Graph.NodeGrid;
import Graph.Path;
import Graph.StructureType;

public class MapComponent extends SurfaceView
{
    public enum TouchType
    {
        Drag,
        Pinch,
        None
    }
    volatile TouchType Touch = TouchType.None;
    Size Resolution = new Size(100, 100);
    FillMode Fill = FillMode.Solid;
    volatile Camera Camera = new Camera(new Vertex(0, 0, -50), new Vertex(0));
    volatile GraphicsUnit Graphics;
    static volatile NodeGrid Map;
    float previousX;
    float previousY;
    float previousDistance;
    volatile Vertex PreviousLocation;
    volatile int floor = 0;
    volatile Bitmap RenderImage;
    volatile Canvas RenderCanvas;
    public int FPS;
    Point[][] StructuredPolygon = new Point[500][];
    public boolean Running = false;
    Thread RenderThread;
    Vertex Rotation = new Vertex(0, 0, 0);
    public Path RenderPath;
    public MapComponent(Context context)
    {
        super(context);
        InitializeComponents(context);
    }
    public MapComponent(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        InitializeComponents(context);
    }
    private void InitializeComponents(Context context)
    {
        if (Map == null)
        {
            Map = new NodeGrid(10, 10, 1, "MAP", false, false, false, false, false, false, false);
            try
            {
                Map.CreateFromJSON(context, "graph.ngm");
            }
            catch (Exception e)
            {

            }
        }
        RenderImage = Bitmap.createBitmap(Resolution.Width, Resolution.Height, Bitmap.Config.ARGB_8888);
        RenderCanvas = new Canvas(RenderImage);
        InitializeGraphics(RenderCanvas);
        getHolder().addCallback(new SurfaceHolder.Callback()
        {
            public void surfaceCreated(SurfaceHolder holder)
            {
                Start();
                Update();
            }
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {
                Update();
            }
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                Stop();
                Update();
            }
        });
    }
    private void InitializeGraphics(Canvas g)
    {
        Graphics = new GraphicsUnit(g, 1024, Resolution, Resolution, Fill, Camera, false, false);
    }
    public void Start()
    {
        Running = true;
        Update();
        RenderThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (Running)
                {
                    try
                    {
                        long startFrameTime = System.currentTimeMillis();
                        Canvas canvas = getHolder().lockCanvas(null);
                        render(canvas);
                        getHolder().unlockCanvasAndPost(canvas);
                        long timeThisFrame = System.currentTimeMillis() - startFrameTime;
                        if (timeThisFrame > 0)
                        {
                            FPS = (int) (1000 / timeThisFrame);
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
            }
        });
        RenderThread.start();
    }
    public void Stop()
    {
        Running = false;
        try
        {
            RenderThread.join();
        }
        catch (Exception e)
        {

        }
    }
    public void SetPath(Path p)
    {
        RenderPath = p;
    }
    protected void render(Canvas canvas)
    {
        Resolution = new Size(getWidth(), getHeight());
        InitializeGraphics(canvas);
        Graphics.FillRectangle(Color.White, new Point(0, 0), Resolution);
        double xmax = Map.GetXMax();
        double ymax = Map.GetYMax();
        if (Map.EnabledNodes != null)
        {
            for (int i = 0; i < Map.EnabledNodes[floor].size(); i++)
            {
                Node node = Map.EnabledNodes[floor].get(i);
                int x = Map.EnabledNodes[floor].get(i).x;
                int y = Map.EnabledNodes[floor].get(i).y;
                int z = Map.EnabledNodes[floor].get(i).z;
                Vertex Location = new Vertex(x - (xmax / 2) + 0.5,  y - (ymax / 2) - 0.25, floor * -50);
                Location = Location.multiply(new Vertex(Map.XDistance, Map.YDistance, 1));
                Location = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Rotation, Location);
                Location = Graphics.D3D_ToProjection(Location);
                if (node.object.Bound_Top_Left != null && node.object.Bound_Top_Right != null && node.object.Bound_Bottom_Left != null && node.object.Bound_Bottom_Right != null)
                {
                    try
                    {
                        StructureType type = Map.EnabledNodes[floor].get(i).object.Type;
                        if (type == StructureType.Building)
                        {
                            Graphics.FillPolygon(Color.WhiteSmoke, StructuredPolygon[i]);
                            Graphics.DrawPolygon(Color.Gainsboro, StructuredPolygon[i]);
                        }
                        if (type == StructureType.WastedSpace)
                        {
                            Graphics.FillPolygon(Color.Silver, StructuredPolygon[i]);
                        }
                        if (type == StructureType.Path)
                        {
                            Graphics.FillPolygon(Color.PaleGoldenRod, StructuredPolygon[i]);
                        }
                        double u = StructuredPolygon[i][0].X + StructuredPolygon[i][1].X + StructuredPolygon[i][2].X + StructuredPolygon[i][3].X;
                        double v = StructuredPolygon[i][0].Y + StructuredPolygon[i][1].Y + StructuredPolygon[i][2].Y + StructuredPolygon[i][3].Y;
                        u /= 4;
                        v /= 4;
                        if (!Map.EnabledNodes[floor].get(i).FriendlyName.equals("") && !Map.EnabledNodes[floor].get(i).FriendlyName.startsWith("{") && !Map.EnabledNodes[floor].get(i).FriendlyName.contains("Entrance"))
                        {
                            String s = Map.EnabledNodes[floor].get(i).FriendlyName;
                            if (s.contains("(") && !s.contains("(B") && !s.contains("(G"))
                            {
                                s = s.substring(0, s.indexOf("("));
                            }
                            if (s.contains(" "))
                            {
                                s.replaceAll(" ", "\n");
                            }
                            int zoom = (int) (Camera.Location.Z + (floor * 50));
                            zoom = 100 + zoom;
                            zoom -= 40;
                            if (zoom < 0)
                            {
                                zoom = 1;
                            }
                            Graphics.DrawString(s, Color.Black, new Point(u - (Graphics.GetTextSize(s, zoom).Width / 2), v + (zoom / 2)), zoom);
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
            }
        }
        if (RenderPath != null)
        {
            int zoom = (int) (Camera.Location.Z + (floor * 50));
            zoom = 100 + zoom;
            zoom -= 40;
            if (zoom < 5)
            {
                zoom = 5;
            }
            else if (zoom > 20)
            {
                zoom = 20;
            }
            for (int i = 0; i < RenderPath.Nodes.size() - 1; i++)
            {
                int x = RenderPath.Nodes.get(i).x;
                int y = RenderPath.Nodes.get(i).y;
                int z = RenderPath.Nodes.get(i).z;
                if (z == floor && RenderPath.Nodes.get(i + 1).z == floor)
                {
                    Vertex StartLocation = new Vertex(x - (xmax / 2) + 0.5, y - (ymax / 2) - 0.25, z * -50);
                    StartLocation = StartLocation.multiply(new Vertex(Map.XDistance, Map.YDistance, 1));
                    StartLocation = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Rotation, StartLocation);
                    StartLocation = Graphics.D3D_ToProjection(StartLocation);
                    x = RenderPath.Nodes.get(i + 1).x;
                    y = RenderPath.Nodes.get(i + 1).y;
                    z = RenderPath.Nodes.get(i + 1).z;
                    Vertex EndLocation = new Vertex(x - (xmax / 2) + 0.5, y - (ymax / 2) - 0.25, z * -50);
                    EndLocation = EndLocation.multiply(new Vertex(Map.XDistance, Map.YDistance, 1));
                    EndLocation = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Rotation, EndLocation);
                    EndLocation = Graphics.D3D_ToProjection(EndLocation);
                    Point[] linePoint = new Point[]
                            {
                                    StartLocation.toPoint().subtract(new Point(zoom, zoom)),
                                    EndLocation.toPoint().subtract(new Point(zoom, zoom))
                            };
                    Graphics.DrawLine(Color.SteelBlue, linePoint, zoom);
                }
            }
        }
    }
    public void CenterAround(String name)
    {
        Node n = Map.Get(name);
        if (n != null)
        {
            floor = n.z;
            Camera.Location.Z = floor * -50;
            Camera.Location.Z -= 15;
            int x = n.x - 50;
            int y = n.y - 50;
            Camera.Location.X = x;
            Camera.Location.Y = y;
        }
    }
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean _return = false;
        float x = event.getX();
        float y = event.getY();
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                _return = true;
                previousX = x;
                previousY = y;
                PreviousLocation = new Vertex(Camera.Location.X, Camera.Location.Y, Camera.Location.Z);
                invalidate();
                Touch = TouchType.Drag;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Touch = TouchType.Pinch;
                double fDX = event.getX(0) - event.getX(1);
                double fDY = event.getY(0) - event.getY(1);
                previousDistance = (float)Math.sqrt(fDX * fDX + fDY * fDY);
                break;
            case MotionEvent.ACTION_UP:
                Touch = TouchType.None;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Touch = TouchType.None;
                break;
            case MotionEvent.ACTION_MOVE:
                _return = true;
                if (Touch == TouchType.Drag)
                {
                    float dx = x - previousX;
                    float dy = y - previousY;
                    dx /= 25;
                    dy /= 25;
                    Camera.Location.X = PreviousLocation.X - dx;
                    Camera.Location.Y = PreviousLocation.Y + dy;
                }
                else if (Touch == TouchType.Pinch)
                {
                    double fingerDistanceX = event.getX(0) - event.getX(1);
                    double fingerDistanceY = event.getY(0) - event.getY(1);
                    float distance = (float) Math.sqrt(fingerDistanceX * fingerDistanceX + fingerDistanceY * fingerDistanceY);
                    float dd = distance - previousDistance;
                    dd /= 10;
                    Camera.Location.Z = PreviousLocation.Z + dd;
                    if (Camera.Location.Z > -100)
                    {
                        floor = 0;
                    }
                    else if (Camera.Location.Z < -100 && Camera.Location.Z > -175)
                    {
                        floor = 2;
                    }
                    else if (Camera.Location.Z < -175)
                    {
                        floor = 4;
                    }
                }
                break;
            default:
                break;
        }
        Update();
        return _return;
    }
    public void Update()
    {
        double xmax = Map.GetXMax();
        double ymax = Map.GetYMax();
        if (Map.EnabledNodes != null)
        {
            for (int i = 0; i < Map.EnabledNodes[floor].size(); i++)
            {
                Node node = Map.EnabledNodes[floor].get(i);
                if (node.object.Bound_Top_Left != null && node.object.Bound_Top_Right != null && node.object.Bound_Bottom_Left != null && node.object.Bound_Bottom_Right != null)
                {
                    node.object.Bound_Top_Left.Z = 0;
                    Vertex TL = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Rotation, node.object.Bound_Top_Left.subtract(new Vertex((ymax / 2), (xmax / 2), floor * 50)).multiply(new Vertex(Map.XDistance, Map.YDistance, 1)));
                    TL = Graphics.D3D_ToProjection(TL);
                    TL = TL.add(new Vertex(5, 5, 0));
                    node.object.Bound_Top_Right.Z = 0;
                    Vertex TR = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Rotation, node.object.Bound_Top_Right.subtract(new Vertex((ymax / 2), (xmax / 2), floor * 50)).multiply(new Vertex(Map.XDistance, Map.YDistance, 1)));
                    TR = Graphics.D3D_ToProjection(TR);
                    TR = TR.add(new Vertex(5, 5, 0));
                    node.object.Bound_Bottom_Left.Z = 0;
                    Vertex BL = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Rotation, node.object.Bound_Bottom_Left.subtract(new Vertex((ymax / 2), (xmax / 2), floor * 50)).multiply(new Vertex(Map.XDistance, Map.YDistance, 1)));
                    BL = Graphics.D3D_ToProjection(BL);
                    BL = BL.add(new Vertex(5, 5, 0));
                    node.object.Bound_Bottom_Right.Z = 0;
                    Vertex BR = Graphics.D3D_Transformation(new Vertex(0), new Vertex(0), new Vertex(0), new Vertex(1), Rotation, node.object.Bound_Bottom_Right.subtract(new Vertex((ymax / 2), (xmax / 2), floor * 50)).multiply(new Vertex(Map.XDistance, Map.YDistance, 1)));
                    BR = Graphics.D3D_ToProjection(BR);
                    BR = BR.add(new Vertex(5, 5, 0));
                    Point[] polygon = new Point[]
                            {
                                    TL.toPoint(),
                                    TR.toPoint(),
                                    BR.toPoint(),
                                    BL.toPoint()
                            };
                    StructuredPolygon[i] = polygon;
                }
            }
        }
    }
}
