package studios.vanish.maps;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import Graph.Direction;
import Graph.Node;
import Graph.Path;

public class Program extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    design_content_explore fragment_explore;
    design_content_directions fragment_directions;
    design_content_find fragment_find;
    MenuItem search_item;
    SearchView search_view;
    NavigationView navigationView;
    ArrayList<Direction> Directions;
    Path DirectionsPath;
    int directionIndex = 0;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design);
        InitializeComponents();
    }
    public void InitializeComponents()
    {
        fragment_explore = new design_content_explore();
        fragment_directions = new design_content_directions();
        fragment_find = new design_content_find();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().performIdentifierAction(R.id.nav_explore, 1);
        navigationView.setCheckedItem(R.id.nav_explore);
    }
    public void SetFragment(Fragment fragment)
    {
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.replace(R.id.design_content_fragment, fragment);
        FT.addToBackStack(null);
        FT.commit();
        fragment_find.SetHandlers(this, "OnSearchSelected");
        fragment_directions.SetHandlers(this, "OnDirectionStart");
        if (fragment instanceof design_content_directiondetail)
        {
            ((design_content_directiondetail) fragment).SetHandlers(this, "NextDirection", "PreviousDirection", DirectionsPath);
        }
    }
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (search_item != null)
        {
            search_item.collapseActionView();
            search_view.onActionViewCollapsed();
        }
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.design_appbar_menu, menu);
        search_item = menu.findItem(R.id.action_search);
        search_view = (SearchView) search_item.getActionView();
        search_view.setIconified(true);
        search_view.setOnSearchClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SetFragment(fragment_find);
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }
            public boolean onQueryTextChange(String newText)
            {
                fragment_find.Filter(newText);
                return false;
            }
        });
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_about)
        {
            SetFragment(new design_content_about());
            return true;
        }
        else if (id == R.id.action_search)
        {

        }
        return super.onOptionsItemSelected(item);
    }
    public void OnSearchSelected(String _node)
    {
        navigationView.getMenu().performIdentifierAction(R.id.nav_explore, 1);
        navigationView.setCheckedItem(R.id.nav_explore);
        SetFragment(fragment_explore);
        if (search_view != null)
        {
            search_view.onActionViewCollapsed();
        }
        fragment_explore.CenterAround(_node);
    }
    public void PreviousDirection()
    {
        directionIndex -= 2;
        if (directionIndex >= 0)
        {
            NextDirection();
        }
        else
        {
            directionIndex += 1;
            NextDirection();
        }
    }
    public void NextDirection()
    {
        if (directionIndex < Directions.size())
        {
            Fragment frag = new design_content_directiondetail();
            Bundle data = new Bundle();
            Direction d = Directions.get(directionIndex);
            data.putString("Parent", d.Parent);
            data.putString("Current", d.Current);
            data.putString("Child", d.Child);
            data.putString("Instruction", d.Instruction);
            data.putDouble("Angle", d.Angle);
            frag.setArguments(data);
            SetFragment(frag);
            directionIndex++;
        }
    }
    public void OnDirectionStart(String StartNode, String EndNode)
    {
        Node Start = MapComponent.Map.Get(StartNode);
        Node End = MapComponent.Map.Get(EndNode);
        Path DirectedPath = MapComponent.Map.GetPath(Start, End);
        Directions = DirectedPath.GetDynamicDirections();
        DirectionsPath = DirectedPath;
        directionIndex = 0;
        NextDirection();
    }
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.nav_explore)
        {
            SetFragment(fragment_explore);
        }
        else if (id == R.id.nav_directions)
        {
            SetFragment(fragment_directions);
        }
        else if (id == R.id.nav_find)
        {
            search_view.onActionViewExpanded();
            SetFragment(fragment_find);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
