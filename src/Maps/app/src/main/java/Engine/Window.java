package Engine;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.SurfaceView;
import Spark.EventHandler;
public class Window extends SurfaceView
{
	private AppCompatActivity Activity;
	private volatile boolean Running;
	private Thread MainThread;
	public String Name = "New Window";
	public Size Size;
	public Size Resolution;
	public EventHandler OnPaint = new EventHandler();
	private volatile Bitmap RenderImage;
	public int FPS;
	public int FieldOfView = 256;
	public FillMode FillMode;
	public Camera Camera = new Camera();
	public boolean CalculateIntersections = true;
	public boolean AutomaticRender = true;
	public boolean RenderLights = false;
	public Window(String _name, AppCompatActivity _activity)
	{
		super(_activity);
		Activity = _activity;
		Name = _name;
		initializeFrame();
	}
	public void Initialize3D(Size Res, int _FOV, FillMode fillMode, boolean calculateIntersections, boolean automaticRender)
	{
		FieldOfView = _FOV;
		FillMode = fillMode;
		CalculateIntersections = calculateIntersections;
		AutomaticRender = automaticRender;
		Resolution = new Size(Res.Width, Res.Height);
	}
	public void initializeFrame()
	{
		Activity.setContentView(this);
		Activity.setTitle(Name);
		Display display = Activity.getWindowManager().getDefaultDisplay();
		android.graphics.Point size = new android.graphics.Point();
		display.getSize(size);
		Size = new Size(size.x, size.y);
		RenderImage = Bitmap.createBitmap(Size.Width, Size.Height, Bitmap.Config.ARGB_8888);
	}
	public void Start()
	{
		Running = true;
		MainThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (Running)
				{
					if (getHolder().getSurface().isValid())
					{
						long startFrameTime = System.currentTimeMillis();
						Canvas _canvas = getHolder().lockCanvas();
						Canvas renderCanvas = new Canvas(RenderImage);
						GraphicsUnit graphics = new GraphicsUnit(renderCanvas, FieldOfView, Size, Resolution, FillMode, Camera, CalculateIntersections, RenderLights);
						OnPaint.InvokeAll(graphics);
						if (AutomaticRender == true)
						{
							graphics.D3D_Queue_Render();
						}
						_canvas.drawBitmap(RenderImage, 0, 0, new Paint());
						getHolder().unlockCanvasAndPost(_canvas);
						long timeThisFrame = System.currentTimeMillis() - startFrameTime;
						if (timeThisFrame > 0)
						{
							FPS = (int)(1000 / timeThisFrame);
						}
					}
				}
			}
		});
		MainThread.start();
	}
	public void Stop()
	{
		Running = true;
		while (true)
		{
			try
			{
				MainThread.join();
			}
			catch (Exception e)
			{

			}
		}
	}
	public GraphicsUnit GetNullGraphicsUnit()
	{
		return new GraphicsUnit(null, FieldOfView, Size, Resolution, FillMode, Camera, CalculateIntersections, RenderLights);
	}
}
