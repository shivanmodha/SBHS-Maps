package Engine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;

public class GraphicsUnit
{
	public Canvas graphics;
	public Paint graphicsContext = new Paint();
	public int FieldOfView;
	public Size WindowSize;
	public Size Resolution;
	public FillMode Fill;
	private D3D_renderStructure Queue = new D3D_renderStructure();
	private ArrayList<Light> Lights = new ArrayList<Light>();
	private ArrayList<Double> renderZ = new ArrayList<Double>();
	private boolean intersectionPoints = false;
	private Camera Camera;
	private boolean calcIntersections;
	private boolean RenderLights;
	private boolean GradientLightCalculation = false;
	public GraphicsUnit(Canvas g)
	{
		graphics = g;
	}
	public GraphicsUnit(Canvas g, int fov, Size size, Size res, FillMode fillMode, Camera _camera, boolean cI, boolean rl)
	{
		this(g);
		FieldOfView = fov;
		WindowSize = size;
		Resolution = res;
		Fill = fillMode;
		Camera = _camera;
		calcIntersections = cI;
		RenderLights = rl;
	}
	public int SetGraphicsColor(Color Color, int start)
	{
		graphicsContext.setColor(android.graphics.Color.argb(Color.A, Color.R, Color.G, Color.B));
		return -1;
	}
	public void FillRectangle(Color Color, Point Location, Size Size)
	{
		int col = 0;
		while (col != -1)
		{
			col = SetGraphicsColor(Color, col);
			graphicsContext.setStyle(Paint.Style.FILL);
			graphics.drawRect((float)Location.X, (float)Location.Y, (float)Location.X + Size.Width, (float)Location.Y + Size.Height, graphicsContext);
		}
	}
	public void DrawRectangle(Color Color, Point Location, Size Size)
	{
		int col = 0;
		while (col != -1)
		{
			col = SetGraphicsColor(Color, col);
			graphicsContext.setStyle(Paint.Style.STROKE);
			graphics.drawRect((float)Location.X, (float)Location.Y, (float)Location.X + Size.Width, (float)Location.Y + Size.Height, graphicsContext);
		}
	}
	public void FillEllipse(Color Color, Point Location, Size Size)
	{
		int col = 0;
		while (col != -1)
		{
			col = SetGraphicsColor(Color, col);
			graphicsContext.setStyle(Paint.Style.FILL);
			RectF r = new RectF();
			r.left = (float)Location.X;
			r.top = (float)Location.Y;
			r.right = (float)Location.X + Size.Width;
			r.bottom = (float)Location.Y + Size.Height;
			graphics.drawOval(r, graphicsContext);
		}
	}
	public void DrawEllipse(Color Color, Point Location, Size Size)
	{
		int col = 0;
		while (col != -1)
		{
			col = SetGraphicsColor(Color, col);
			graphicsContext.setStyle(Paint.Style.STROKE);
			RectF r = new RectF();
			r.left = (float)Location.X;
			r.top = (float)Location.Y;
			r.right = (float)Location.X + Size.Width;
			r.bottom = (float)Location.Y + Size.Height;
			graphics.drawOval(r, graphicsContext);
		}
	}
	public void DrawString(String Text, Color Color, Point Location, int Size)
	{
		int col = 0;
		while (col != -1)
		{
			col = SetGraphicsColor(Color, col);
			graphicsContext.setTextSize(Size);
			graphics.drawText(Text, (float)Location.X, (float)Location.Y, graphicsContext);
		}
	}
	public void DrawPolygon(Color Color, Point[] Points)
	{
		Path ln = new Path();
		ln.moveTo((float)Points[0].X, (float)Points[0].Y);
		for (int i = 1; i < Points.length; i++)
		{
			ln.lineTo((float)Points[i].X, (float)Points[i].Y);
		}
		ln.close();
		int col = 0;
		while (col != -1)
		{
			col = SetGraphicsColor(Color, col);
			graphicsContext.setStyle(Paint.Style.STROKE);
			graphics.drawPath(ln, graphicsContext);
		}
	}
	public void FillPolygon(Color Color, Point[] Points)
	{
		Path ln = new Path();
		ln.moveTo((float)Points[0].X, (float)Points[0].Y);
		for (int i = 1; i < Points.length; i++)
		{
			ln.lineTo((float)Points[i].X, (float)Points[i].Y);
		}
		ln.close();
		int col = 0;
		while (col != -1)
		{
			col = SetGraphicsColor(Color, col);
			graphicsContext.setStyle(Paint.Style.FILL);
			graphics.drawPath(ln, graphicsContext);
		}
	}
	public void DrawLine(Color Color, Point[] Points, int Size)
	{
		Path ln = new Path();
		ln.moveTo((float)Points[0].X, (float)Points[0].Y);
		ln.lineTo((float)Points[1].X, (float)Points[1].Y);
		int col = 0;
		while (col != -1)
		{
			col = SetGraphicsColor(Color, col);
			graphicsContext.setStyle(Paint.Style.STROKE);
			graphicsContext.setStrokeWidth(Size);
			graphics.drawPath(ln, graphicsContext);
			graphicsContext.setStrokeWidth(8);
		}
	}
	public Size GetTextSize(String Text, int Size)
	{
		graphicsContext.setTextSize(Size);
		int i = (int)graphicsContext.measureText(Text);
		return new Size(i, Size);
	}

	public Vertex D3D_ToProjection(Vertex vertex)
	{
		Size center = Resolution.half();
		double factor = FieldOfView / vertex.Z;
		factor = Math.abs(factor);
		Vertex position = new Vertex(vertex.X * factor + center.Width, vertex.Y * -factor + center.Height, vertex.Z);
		position.X = (position.X * WindowSize.Width) / Resolution.Width;
		position.Y = (position.Y * WindowSize.Height) / Resolution.Height;
		if (vertex.Z < 0)
		{
			position.X *= center.Width / 2;
			position.Y *= center.Height / 2;
		}
		return position;
	}
	public static Vertex D3D_FromProjection(Point point, Size resolution, Size windowSize, double FieldOfView, double CameraZ)
	{
		Vertex position = new Vertex((point.X * resolution.Width) / windowSize.Width, (point.Y * resolution.Height) / windowSize.Height, CameraZ);
		double factor = FieldOfView / CameraZ;
		Size center = resolution.half();
		position.X = (position.X - center.Width) / -factor;
		position.Y = (position.Y - center.Height) / factor;
		return position;
	}
	public Vertex D3D_Transformation_Translation(Vertex vertices, Vertex Location)
	{
		return vertices.add(Location);//new Vertex(vertices.X + Location.X, vertices.Y + Location.Y, vertices.Z + Location.Z);
	}
	public Vertex[] D3D_Transformation_Translation(Vertex[] vertices, Vertex Location)
	{
		Vertex[] vert = new Vertex[vertices.length];
		for (int i = 0; i < vertices.length; i++)
		{
			vert[i] = D3D_Transformation_Translation(vertices[i], Location);
		}
		return vert;
	}
	public Vertex D3D_Transformation_Rotation_X(Vertex vertices, Vertex Rotation)
	{
		if (Rotation.X == 0)
		{
			return vertices;
		}
		double x = vertices.X;
		double y = vertices.Y;
		double z = vertices.Z;
		if ((Rotation != null) && (Rotation.X != 0))
		{
			double radian = Rotation.X * Math.PI / 180;
			y = vertices.Y * Math.cos(radian) - vertices.Z * Math.sin(radian);
			z = vertices.Y * Math.sin(radian) + vertices.Z * Math.cos(radian);
		}
		return new Vertex(x, y, z);
	}
	public Vertex[] D3D_Transformation_Rotation_X(Vertex[] vertices, Vertex Rotation)
	{
		Vertex[] vert = new Vertex[vertices.length];
		for (int i = 0; i < vertices.length; i++)
		{
			vert[i] = D3D_Transformation_Rotation_X(vertices[i], Rotation);
		}
		return vert;
	}
	public Vertex D3D_Transformation_Rotation_Y(Vertex vertices, Vertex Rotation)
	{
		if (Rotation.Z == 0)
		{
			return vertices;
		}
		double x = vertices.X;
		double y = vertices.Y;
		double z = vertices.Z;
		if ((Rotation != null) && (Rotation.Z != 0))
		{
			double radian = Rotation.Z * Math.PI / 180;
			z = vertices.Z * Math.cos(radian) - vertices.X * Math.sin(radian);
			x = vertices.Z * Math.sin(radian) + vertices.X * Math.cos(radian);
		}
		return new Vertex(x, y, z);
	}
	public Vertex[] D3D_Transformation_Rotation_Y(Vertex[] vertices, Vertex Rotation)
	{
		Vertex[] vert = new Vertex[vertices.length];
		for (int i = 0; i < vertices.length; i++)
		{
			vert[i] = D3D_Transformation_Rotation_Y(vertices[i], Rotation);
		}
		return vert;
	}
	public Vertex D3D_Transformation_Rotation_Z(Vertex vertices, Vertex Rotation)
	{
		if (Rotation.Y == 0)
		{
			return vertices;
		}
		double x = vertices.X;
		double y = vertices.Y;
		double z = vertices.Z;
		if ((Rotation != null) && (Rotation.Y != 0))
		{
			double radian = Rotation.Y * Math.PI / 180;
			x = vertices.X * Math.cos(radian) - vertices.Y * Math.sin(radian);
			y = vertices.X * Math.sin(radian) + vertices.Y * Math.cos(radian);
		}
		return new Vertex(x, y, z);
	}
	public Vertex[] D3D_Transformation_Rotation_Z(Vertex[] vertices, Vertex Rotation)
	{
		Vertex[] vert = new Vertex[vertices.length];
		for (int i = 0; i < vertices.length; i++)
		{
			vert[i] = D3D_Transformation_Rotation_Z(vertices[i], Rotation);
		}
		return vert;
	}
	public Vertex D3D_Transformation_Scale(Vertex vertices, Vertex Scale)
	{
		return vertices.multiply(Scale);
	}
	public Vertex[] D3D_Transformation_Scale(Vertex[] vertices, Vertex Scale)
	{
		Vertex[] vert = new Vertex[vertices.length];
		for (int i = 0; i < vertices.length; i++)
		{
			vert[i] = D3D_Transformation_Scale(vertices[i], Scale);
		}
		return vert;
	}
	public Vertex D3D_Transformation(Vertex vertices, Vertex Location, Vertex Rotation, Vertex Scale, Vertex Revolution, Vertex RevolutionRadius)
	{
		return D3D_Transformation_Rotation_Z(D3D_Transformation_Rotation_X(D3D_Transformation_Rotation_Y(D3D_Transformation_Translation(D3D_Transformation_Rotation_Z(D3D_Transformation_Rotation_Y(D3D_Transformation_Rotation_X(D3D_Transformation_Translation(D3D_Transformation_Rotation_Z(D3D_Transformation_Rotation_Y(D3D_Transformation_Rotation_X(D3D_Transformation_Scale(vertices, Scale), Rotation), Rotation), Rotation), RevolutionRadius), Revolution), Revolution), Revolution), Location.subtract(Camera.Location)), Camera.Rotation), Camera.Rotation), Camera.Rotation);
	}
	public Vertex[] D3D_Transformation(Vertex[] vertices, Vertex Location, Vertex Rotation, Vertex Scale, Vertex Revolution, Vertex RevolutionRadius)
	{
		Vertex[] vert = new Vertex[vertices.length];
		for (int i = 0; i < vertices.length; i++)
		{
			vert[i] = D3D_Transformation(vertices[i], Location, Rotation, Scale, Revolution, RevolutionRadius);
		}
		return vert;
	}
	public void D3D_Queue_Render()
	{
		if (calcIntersections == true)
		{
			Queue = faceIntersection(Queue);
		}
		for (int f = 0; f < Queue.facesTransformed.size(); f++)
		{
			if (Queue.facesTransformed.get(f).length > 3)
			{
				Vertex[] face = Queue.facesTransformed.get(f);
				Vertex[] faceStatic = Queue.facesUntransformed.get(f);
				faceStatic = D3D_Transformation_Translation(D3D_Transformation_Rotation_Z(D3D_Transformation_Rotation_Y(D3D_Transformation_Rotation_X(D3D_Transformation_Translation(D3D_Transformation_Rotation_Z(D3D_Transformation_Rotation_Y(D3D_Transformation_Rotation_X(D3D_Transformation_Scale(faceStatic, Queue.Scale.get(f)), Queue.Rotation.get(f)), Queue.Rotation.get(f)), Queue.Rotation.get(f)), Queue.RevolutionRadius.get(f)), Queue.Revolution.get(f)), Queue.Revolution.get(f)), Queue.Revolution.get(f)), Queue.Location.get(f));
				drawFace(face, light(faceStatic, face, Queue.color.get(f)));
			}
		}
	}
	public int D3D_GetVertexCount()
	{
		int d3d_vertices = 0;
		for (int f = 0; f < Queue.facesTransformed.size(); f++)
		{
			d3d_vertices += Queue.facesTransformed.get(f).length;
		}
		return d3d_vertices;
	}
	public void D3D_Queue_Add(D3D_renderStructure add)
	{
		ArrayList<Double> vertexZTemp = new ArrayList<Double>();
		for (int f = 0; f < add.facesTransformed.size(); f++)
		{
			for (int i = 0; i < add.facesTransformed.get(f).length; i++)
			{
				vertexZTemp.add(add.facesTransformed.get(f)[i].Z);
			}
			int idx = 0;
			double addin = Object3D.GetZIndex(vertexZTemp);
			while ((idx < renderZ.size()) && (addin <= renderZ.get(idx)))
			{
				idx++;
			}
			Queue.color.add(idx, add.color.get(f));
			renderZ.add(idx, addin);
			Queue.facesTransformed.add(idx, add.facesTransformed.get(f));
			Queue.facesUntransformed.add(idx, add.facesUntransformed.get(f));
			Queue.Location.add(idx, add.Location.get(f));
			Queue.Rotation.add(idx, add.Rotation.get(f));
			Queue.Scale.add(idx, add.Scale.get(f));
			Queue.Revolution.add(idx, add.Revolution.get(f));
			Queue.RevolutionRadius.add(idx, add.RevolutionRadius.get(f));
			vertexZTemp.clear();
		}
	}
	public void D3D_Lights_Add(Light add)
	{
		Lights.add(add);
	}
	private D3D_renderStructure faceIntersection(D3D_renderStructure parse)
	{
		ArrayList<Double> vertexZTemp = new ArrayList<Double>();
		ArrayList<Vertex[]> renderFaces = new ArrayList<Vertex[]>();
		ArrayList<Vertex[]> renderFacesTrans = new ArrayList<Vertex[]>();
		ArrayList<Color> renderColor = new ArrayList<Color>();
		ArrayList<Double> renderZ = new ArrayList<Double>();
		ArrayList<Vertex> loc = new ArrayList<Vertex>();
		ArrayList<Vertex> rot = new ArrayList<Vertex>();
		ArrayList<Vertex> sca = new ArrayList<Vertex>();
		ArrayList<Vertex> rev = new ArrayList<Vertex>();
		ArrayList<Vertex> revRad = new ArrayList<Vertex>();
		for (int f1 = 0; f1 < parse.facesTransformed.size(); f1++)
		{
			for (int f2 = 0; f2 < parse.facesTransformed.size(); f2++)
			{
				if (f1 != f2)
				{
					Line planeLine1 = new Line(parse.facesUntransformed.get(f1)[0], parse.facesUntransformed.get(f1)[1]);
					Line planeLine2 = new Line(parse.facesUntransformed.get(f1)[1], parse.facesUntransformed.get(f1)[2]);
					Line planeLine3 = new Line(parse.facesUntransformed.get(f1)[2], parse.facesUntransformed.get(f1)[3]);
					Line planeLine4 = new Line(parse.facesUntransformed.get(f1)[3], parse.facesUntransformed.get(f1)[0]);
					Vertex intersection1 = planeLine1.Intersection(parse.facesUntransformed.get(f2));
					Vertex intersection2 = planeLine2.Intersection(parse.facesUntransformed.get(f2));
					Vertex intersection3 = planeLine3.Intersection(parse.facesUntransformed.get(f2));
					Vertex intersection4 = planeLine4.Intersection(parse.facesUntransformed.get(f2));
					Vertex intersectionVertex1 = D3D_Transformation(intersection1, parse.Location.get(f1), parse.Rotation.get(f1), parse.Scale.get(f1), parse.Revolution.get(f1), parse.RevolutionRadius.get(f1));
					Vertex intersectionVertex2 = D3D_Transformation(intersection2, parse.Location.get(f1), parse.Rotation.get(f1), parse.Scale.get(f1), parse.Revolution.get(f1), parse.RevolutionRadius.get(f1));
					Vertex intersectionVertex3 = D3D_Transformation(intersection3, parse.Location.get(f1), parse.Rotation.get(f1), parse.Scale.get(f1), parse.Revolution.get(f1), parse.RevolutionRadius.get(f1));
					Vertex intersectionVertex4 = D3D_Transformation(intersection4, parse.Location.get(f1), parse.Rotation.get(f1), parse.Scale.get(f1), parse.Revolution.get(f1), parse.RevolutionRadius.get(f1));
					Vertex point1 = D3D_ToProjection(intersectionVertex1);
					Vertex point2 = D3D_ToProjection(intersectionVertex2);
					Vertex point3 = D3D_ToProjection(intersectionVertex3);
					Vertex point4 = D3D_ToProjection(intersectionVertex4);
					ArrayList<Vertex> interPt = new ArrayList<Vertex>();
					ArrayList<Vertex> interPtTrans = new ArrayList<Vertex>();
					if (planeLine1.OnSegment(intersection1))
					{
						if (intersectionPoints == true)
						{
							FillEllipse(Color.Black, point1.toPoint().subtract(new Point(5, 5)), new Size(10, 10));
						}
						interPt.add(intersectionVertex1);
						interPtTrans.add(intersection1);
					}
					if (planeLine2.OnSegment(intersection2))
					{
						if (intersectionPoints == true)
						{
							FillEllipse(Color.Black, point2.toPoint().subtract(new Point(5, 5)), new Size(10, 10));
						}
						interPt.add(intersectionVertex2);
						interPtTrans.add(intersection2);
					}
					if (planeLine3.OnSegment(intersection3))
					{
						if (intersectionPoints == true)
						{
							FillEllipse(Color.Black, point3.toPoint().subtract(new Point(5, 5)), new Size(10, 10));
						}
						interPt.add(intersectionVertex3);
						interPtTrans.add(intersection3);
					}
					if (planeLine4.OnSegment(intersection4))
					{
						if (intersectionPoints == true)
						{
							FillEllipse(Color.Black, point4.toPoint().subtract(new Point(5, 5)), new Size(10, 10));
						}
						interPt.add(intersectionVertex4);
						interPtTrans.add(intersection4);
					}
					if ((interPt.size() >= 2) && !(interPt.get(0).equals(parse.facesTransformed.get(f1)[0])) && !(interPt.get(0).equals(parse.facesTransformed.get(f1)[1]))&& !(interPt.get(0).equals(parse.facesTransformed.get(f1)[2])) && !(interPt.get(0).equals(parse.facesTransformed.get(f1)[3]))&& !(interPt.get(1).equals(parse.facesTransformed.get(f1)[0])) && !(interPt.get(1).equals(parse.facesTransformed.get(f1)[1]))&& !(interPt.get(1).equals(parse.facesTransformed.get(f1)[2])) && !(interPt.get(1).equals(parse.facesTransformed.get(f1)[3])))
					{
						vertexZTemp.clear();
						Vertex[] face1 = new Vertex[4];
						face1[0] = parse.facesTransformed.get(f1)[0];
						face1[1] = parse.facesTransformed.get(f1)[1];
						face1[2] = interPt.get(0);
						face1[3] = interPt.get(1);
						Vertex[] face1Trans = new Vertex[4];
						face1Trans[0] = parse.facesUntransformed.get(f1)[0];
						face1Trans[1] = parse.facesUntransformed.get(f1)[1];
						face1Trans[2] = interPtTrans.get(0);
						face1Trans[3] = interPtTrans.get(1);
						for (int i = 0; i < face1.length; i++)
						{
							vertexZTemp.add(face1[i].Z);
						}
						int idx = 0;
						double addin = Object3D.GetZIndex(vertexZTemp);
						while ((idx < renderZ.size()) && (addin <= renderZ.get(idx)))
						{
							idx++;
						}
						renderColor.add(idx, parse.color.get(f1));
						renderZ.add(idx, addin);
						renderFaces.add(idx, face1);
						renderFacesTrans.add(idx, face1Trans);
						loc.add(idx, parse.Location.get(f1));
						rot.add(idx, parse.Rotation.get(f1));
						sca.add(idx, parse.Scale.get(f1));
						rev.add(idx, parse.Revolution.get(f1));
						revRad.add(idx, parse.RevolutionRadius.get(f1));
						vertexZTemp.clear();
						Vertex[] face2 = new Vertex[4];
						face2[0] = parse.facesTransformed.get(f1)[3];
						face2[1] = parse.facesTransformed.get(f1)[2];
						face2[2] = interPt.get(0);
						face2[3] = interPt.get(1);
						Vertex[] face2Trans = new Vertex[4];
						face2Trans[0] = parse.facesUntransformed.get(f1)[3];
						face2Trans[1] = parse.facesUntransformed.get(f1)[2];
						face2Trans[2] = interPtTrans.get(0);
						face2Trans[3] = interPtTrans.get(1);
						for (int i = 0; i < face2.length; i++)
						{
							vertexZTemp.add(face2[i].Z);
						}
						idx = 0;
						addin = Object3D.GetZIndex(vertexZTemp);
						vertexZTemp.clear();
						while ((idx < renderZ.size()) && (addin <= renderZ.get(idx)))
						{
							idx++;
						}
						renderColor.add(idx, parse.color.get(f1));
						renderZ.add(idx, addin);
						renderFaces.add(idx, face2);
						renderFacesTrans.add(idx, face2Trans);
						loc.add(idx, parse.Location.get(f1));
						rot.add(idx, parse.Rotation.get(f1));
						sca.add(idx, parse.Scale.get(f1));
						rev.add(idx, parse.Revolution.get(f1));
						revRad.add(idx, parse.RevolutionRadius.get(f1));
					}
					else
					{
						for (int i = 0; i < parse.facesTransformed.get(f1).length; i++)
						{
							vertexZTemp.add(parse.facesTransformed.get(f1)[i].Z);
						}
						int idx = 0;
						double addin = Object3D.GetZIndex(vertexZTemp);
						while ((idx < renderZ.size()) && (addin <= renderZ.get(idx)))
						{
							idx++;
						}
						renderColor.add(idx, parse.color.get(f1));
						renderZ.add(idx, addin);
						renderFaces.add(idx, parse.facesTransformed.get(f1));
						renderFacesTrans.add(idx, parse.facesUntransformed.get(f1));
						loc.add(idx, parse.Location.get(f1));
						rot.add(idx, parse.Rotation.get(f1));
						sca.add(idx, parse.Scale.get(f1));
						rev.add(idx, parse.Revolution.get(f1));
						revRad.add(idx, parse.RevolutionRadius.get(f1));
						vertexZTemp.clear();
					}
				}
			}
		}
		if (renderFaces.size() != 0)
		{
			return new D3D_renderStructure(renderFaces, renderFacesTrans, renderColor, loc, rot, sca, rev, revRad);
		}
		else
		{
			return parse;
		}
	}
	private Color light(Vertex[] object, Vertex[] cameraRelativity, Color color)
	{
		if (RenderLights)
		{
			if (GradientLightCalculation)
			{
				ColorLinearGradient _return = new ColorLinearGradient(2);
				for (int i = 0; i < 2; i++)
				{
					double ar = 0;
					double ag = 0;
					double ab = 0;
					for (int j = 0; j < Lights.size(); j++)
					{
						Light CurrentLight = Lights.get(j);
						double Distance = object[i].subtract(CurrentLight.Location).magnitude();
						double Factor = CurrentLight.Attenuation.X / Math.pow(Distance, 2);
						if (Distance > CurrentLight.Attenuation.X)
						{
							Factor *= CurrentLight.Attenuation.Y;
							double MaxFact = 1 / CurrentLight.Attenuation.X;
							if (Factor > MaxFact)
							{
								Factor = MaxFact;
							}
						}
						if (Factor < 0)
						{
							Factor = 0;
						}
						else if (Factor > 1)
						{
							Factor = 1;
						}
						double r = (color.R + CurrentLight.Diffuse.R) / 255.0;
						double g = (color.G + CurrentLight.Diffuse.G) / 255.0;
						double b = (color.B + CurrentLight.Diffuse.B) / 255.0;
						r *= Factor;
						g *= Factor;
						b *= Factor;
						ar += r;
						ag += g;
						ab += b;
					}
					int red = (int)(ar * 255);
					int gre = (int)(ag * 255);
					int blu = (int)(ab * 255);
					if (red > 255) red = 255;
					if (gre > 255) gre = 255;
					if (blu > 255) blu = 255;
					if (color.R > Light.Ambient.R && red < Light.Ambient.R) red = Light.Ambient.R;
					if (color.G > Light.Ambient.G && gre < Light.Ambient.G) gre = Light.Ambient.G;
					if (color.B > Light.Ambient.B && blu < Light.Ambient.B) blu = Light.Ambient.B;
					_return.Set(i, D3D_ToProjection(cameraRelativity[i]).toPoint(), new Color(red, gre, blu, color.A));
				}
				return _return;
			}
			else
			{
				Vertex Location = new Vertex();
				for (int i = 0; i < object.length; i++)
				{
					Location.X += object[i].X;
					Location.Y += object[i].Y;
					Location.Z += object[i].Z;
				}
				Location.X /= object.length;
				Location.Y /= object.length;
				Location.Z /= object.length;
				double ar = 0;
				double ag = 0;
				double ab = 0;
				for (int i = 0; i < Lights.size(); i++)
				{
					Light CurrentLight = Lights.get(i);
					double Distance = Location.subtract(CurrentLight.Location).magnitude();
					double Factor = CurrentLight.Attenuation.X / Math.pow(Distance, 2);
					if (Distance > CurrentLight.Attenuation.X)
					{
						Factor *= CurrentLight.Attenuation.Y;
						double MaxFact = 1 / CurrentLight.Attenuation.X;
						if (Factor > MaxFact)
						{
							Factor = MaxFact;
						}
					}
					if (Factor < 0)
					{
						Factor = 0;
					}
					else if (Factor > 1)
					{
						Factor = 1;
					}
					double r = (color.R + CurrentLight.Diffuse.R) / 255.0;
					double g = (color.G + CurrentLight.Diffuse.G) / 255.0;
					double b = (color.B + CurrentLight.Diffuse.B) / 255.0;
					r *= Factor;
					g *= Factor;
					b *= Factor;
					ar += r;
					ag += g;
					ab += b;
				}
				int red = (int)(ar * 255);
				int gre = (int)(ag * 255);
				int blu = (int)(ab * 255);
				if (red > 255) red = 255;
				if (gre > 255) gre = 255;
				if (blu > 255) blu = 255;
				if (color.R > Light.Ambient.R && red < Light.Ambient.R) red = Light.Ambient.R;
				if (color.G > Light.Ambient.G && gre < Light.Ambient.G) gre = Light.Ambient.G;
				if (color.B > Light.Ambient.B && blu < Light.Ambient.B) blu = Light.Ambient.B;
				return new Color(red, gre, blu, color.A);
			}
		}
		else
		{
			return color;
		}
	}
	private void drawFace(Vertex[] face, Color color)
	{
		Point[] pt = new Point[face.length];
		for (int v = 0; v < face.length; v++)
		{
			pt[v] = D3D_ToProjection(face[v]).toPoint();
		}
		if (Fill == FillMode.Solid)
		{
			FillPolygon(color, pt);
		}
		else if (Fill == FillMode.Wireframe)
		{
			DrawPolygon(color, pt);
		}
		else if (Fill == FillMode.Vertices)
		{
			Circle point = new Circle(color, pt[0], 2);
			for (int i = 0; i < pt.length; i++)
			{
				point.Center = pt[i];
				point.Render(this);
			}
		}
	}
	public void Render()
	{
		D3D_Queue_Render();
	}
}
