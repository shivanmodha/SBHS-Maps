package studios.vanish.maps;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Graph.Path;

public class design_content_explore extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    MapComponent design_content_explore_map;
    String CenteredNode = "";
    Path RenderPath = null;
    public design_content_explore()
    {

    }
    public void SetPath(Path p)
    {
        RenderPath = p;
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
    public void onStart()
    {
        super.onStart();
    }
    public void onStop()
    {
        super.onStop();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View LayoutView = inflater.inflate(R.layout.design_content_explore, container, false);
        design_content_explore_map = (MapComponent)LayoutView.findViewById(R.id.design_content_explore_map);
        design_content_explore_map.SetPath(RenderPath);
        if (!CenteredNode.equals(""))
        {
            design_content_explore_map.CenterAround(CenteredNode);
            CenteredNode = "";
        }
        return LayoutView;
    }
    public void CenterAround(String _node)
    {
        CenteredNode = _node;
        design_content_explore_map.CenterAround(_node);
    }
}
