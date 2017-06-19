package studios.vanish.maps;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class map_main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.map_content_toolbar);
        AppBarLayout toolbar_container = (AppBarLayout)findViewById(R.id.map_content_toolbar_container);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.map_content_navigation);


        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            drawer.setStatusBarBackgroundColor(getResources().getColor(R.color.colorStatusBar));

            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)toolbar_container.getLayoutParams();
            lp.setMargins(8, GetStatusBarHeight() + 16, 8, 8);
            toolbar_container.setLayoutParams(lp);
            //toolbar_container.setPadding(0, GetStatusBarHeight(), 0, 0);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }
    public int GetStatusBarHeight()
    {
        int _return = 0;
        int id = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (id > 0)
        {
            _return =getResources().getDimensionPixelSize(id);
        }
        return _return;
    }
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        getMenuInflater().inflate(R.menu.map_layout_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.nav_camera)
        {

        }
        else if (id == R.id.nav_gallery)
        {

        }
        else if (id == R.id.nav_slideshow)
        {

        }
        else if (id == R.id.nav_manage)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
