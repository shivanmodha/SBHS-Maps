package studios.vanish.maps;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import Engine.Camera;
import Engine.FillMode;
import Engine.GraphicsUnit;
import Engine.Size;
import Engine.Vertex;
import Graph.Node;
import Graph.Path;

public class design_content_directiondetail extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    MapComponent design_content_directiondetail_map;
    Program Handle;
    String Event1;
    String Event2;
    Path show;
    public design_content_directiondetail()
    {

    }
    public void SetHandlers(Program h, String e1, String e2, Path s)
    {
        Handle = h;
        Event1 = e1;
        Event2 = e2;
        show = s;
    }
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View LayoutView = inflater.inflate(R.layout.design_content_directiondetail, container, false);
        design_content_directiondetail_map = (MapComponent)LayoutView.findViewById(R.id.design_content_directiondetail_map);
        Bundle arguments = getArguments();
        Node Parent = MapComponent.Map.GetFromRealName(arguments.getString("Parent"));
        Node Current = MapComponent.Map.GetFromRealName(arguments.getString("Current"));
        Node Child = MapComponent.Map.GetFromRealName(arguments.getString("Child"));
        if (Parent == null)
        {
            Log.d("TEST", arguments.getString("Parent"));
        }
        String Instruction = arguments.getString("Instruction");
        double Angle = arguments.getDouble("Angle");
        design_content_directiondetail_map.SetPath(show);
        design_content_directiondetail_map.Rotation.Y = Angle;
        design_content_directiondetail_map.CenterAround(Current.name);

        design_content_directiondetail_map.floor = Current.z;
        design_content_directiondetail_map.Camera.Location.Z = design_content_directiondetail_map.floor * -50;
        design_content_directiondetail_map.Camera.Location.Z -= 15;
        int x = Current.x - 50;
        int y = Current.y - 50;
        Vertex v = new Vertex(x, y, 0);
        GraphicsUnit Graphics = new GraphicsUnit(null, 1024, new Size(100, 100), new Size(100, 100), FillMode.Solid, new Camera(), false, false);
        v = Graphics.D3D_Transformation_Rotation_Z(v, new Vertex(Angle, Angle, Angle));
        design_content_directiondetail_map.Camera.Location.X = v.X;
        design_content_directiondetail_map.Camera.Location.Y = v.Y;

        TextView ins = (TextView)LayoutView.findViewById(R.id.design_content_directiondetail_direction);
        ins.setText(Instruction);
        Button nxt = (Button)LayoutView.findViewById(R.id.design_content_directiondetail_next);
        nxt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Spark.Spark.InvokeMethod(Handle, Event1);
            }
        });
        Button pv = (Button)LayoutView.findViewById(R.id.design_content_directiondetail_previous);
        pv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Spark.Spark.InvokeMethod(Handle, Event2);
            }
        });
        return LayoutView;
    }
}
