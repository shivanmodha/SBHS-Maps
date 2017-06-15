package studios.vanish.maps;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;

public class design_content_directions extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    AutoCompleteTextView design_content_directions_autotext1;
    MapComponent design_content_directions_map1;
    AutoCompleteTextView design_content_directions_autotext2;
    MapComponent design_content_directions_map2;
    Button design_content_directions_start;
    Program Handler;
    String Event;
    public design_content_directions()
    {

    }
    public void SetHandlers(Program prog, String event)
    {
        Handler = prog;
        Event = event;
    }
    public static design_content_directions newInstance(String param1, String param2)
    {
        design_content_directions fragment = new design_content_directions();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View LayoutView = inflater.inflate(R.layout.design_content_directions, container, false);
        design_content_directions_map1 = (MapComponent)LayoutView.findViewById(R.id.design_content_directions_map1);
        design_content_directions_autotext1 = (AutoCompleteTextView)LayoutView.findViewById(R.id.design_content_directions_autotext1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LayoutView.getContext(), android.R.layout.simple_dropdown_item_1line, MapComponent.Map.GetAllWithFriendlyString());
        design_content_directions_autotext1.setAdapter(adapter);
        design_content_directions_autotext1.addTextChangedListener(new TextWatcher()
        {
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                design_content_directions_map1.CenterAround("" + s);
                design_content_directions_map1.Camera.Location.Z -= 25;
                design_content_directions_map1.Update();
            }
            public void afterTextChanged(Editable s)
            {

            }
        });
        design_content_directions_map2 = (MapComponent)LayoutView.findViewById(R.id.design_content_directions_map2);
        design_content_directions_autotext2 = (AutoCompleteTextView)LayoutView.findViewById(R.id.design_content_directions_autotext2);
        design_content_directions_autotext2.setAdapter(adapter);
        design_content_directions_autotext2.addTextChangedListener(new TextWatcher()
        {
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                design_content_directions_map2.CenterAround("" + s);
                design_content_directions_map2.Camera.Location.Z -= 25;
                design_content_directions_map2.Update();
            }
            public void afterTextChanged(Editable s)
            {

            }
        });
        design_content_directions_start = (Button)LayoutView.findViewById(R.id.design_content_directions_start);
        design_content_directions_start.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Spark.Spark.InvokeMethod(Handler, Event, (String)("" + design_content_directions_autotext1.getText()), (String)("" + design_content_directions_autotext2.getText()));
            }
        });
        return LayoutView;
    }
}
