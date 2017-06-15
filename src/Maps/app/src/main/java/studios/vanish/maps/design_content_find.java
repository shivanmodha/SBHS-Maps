package studios.vanish.maps;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;

public class design_content_find extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ListView design_content_find_list;
    ArrayAdapter<String> adapter;
    Program Handler;
    String Event;
    public design_content_find()
    {

    }
    public void SetHandlers(Program prog, String event)
    {
        Handler = prog;
        Event = event;
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
    public void Filter(String filter)
    {
        if (adapter != null)
        {
            adapter.getFilter().filter(filter);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View LayoutView = inflater.inflate(R.layout.design_content_find, container, false);
        design_content_find_list = (ListView)LayoutView.findViewById(R.id.design_content_find_list);
        adapter = new ArrayAdapter<String>(LayoutView.getContext(), R.layout.support_simple_spinner_dropdown_item);
        ArrayList<String> strs = MapComponent.Map.GetAllWithFriendlyString();
        adapter.addAll(strs);
        adapter.sort(new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                return o1.compareToIgnoreCase(o2);
            }
        });
        design_content_find_list.setDivider(null);
        design_content_find_list.setDividerHeight(0);
        design_content_find_list.setAdapter(adapter);
        design_content_find_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Spark.Spark.InvokeMethod(Handler, Event, (String)design_content_find_list.getItemAtPosition(position));
            }
        });
        return LayoutView;
    }
}
