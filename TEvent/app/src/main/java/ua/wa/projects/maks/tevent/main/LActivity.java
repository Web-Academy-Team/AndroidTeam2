package ua.wa.projects.maks.tevent.main;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import ua.wa.projects.maks.tevent.DOMParser.DOMParser;
import ua.wa.projects.maks.tevent.DOMParser.RSSFeed;
import ua.wa.projects.maks.tevent.DOMParser.RSSItem;
import ua.wa.projects.maks.tevent.R;

/**
 * Created by Max on 18.02.2016.
 */
public class LActivity extends AppCompatActivity implements android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener{

    //feed
    RSSFeed fFeed;
    RSSItem feedItem;
    String imageLink;
    String imageLink2;
    String feedTitle;
    String feedDate;

    //Home ListView
    ListView list;
    CustomListAdapter adapter;

    //Dynamic ListView
    private List<String> mUrls;
    private List<String> mFeeds;
    SQLiteDatabase mydb;
    CustomDynamicAdapter adapter_dynamic;
    ListView listfeed;
    String feedcustom;
    String feedcustom2;

    //Others
    Intent intent;
    SwipeRefreshLayout swiperefresh;

    //menu items
    Menu menu;
    MenuItem addfeed;
    MenuItem default_feeds;
    MenuItem xda;

    //Connectivity manager
    ConnectivityManager cM;

    //Context theme wrapper
    ContextThemeWrapper themewrapper;

    //navigation drawer
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    //the default feed
    String feedURL = TEvent.default_feed_value;




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //initialize the feeds items
        fFeed = (RSSFeed) getIntent().getExtras().get("feed");

        //initialize connectivity manager
        cM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //apply preferences

        //apply activity's theme if dark theme is enabled
        themewrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());


        //set the view
        setContentView(R.layout.home);



        //initialize our navigation drawer

        //initialize the Drawer Layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Tie DrawerLayout events to the ActionBarToggle:
        //it adds the hamburger icon and handle the drawer slide event
       /* mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name) {

            //Called when the drawer is opened
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);

                //change toolbar title to 'Add a feed'
                toolbar.setTitle(getResources().getString(R.string.feedmenu));

                //show the add feed menu option
                showAddFeed();

            }

            //Called when the drawer is closed
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //change toolbar title to the app's name
                toolbar.setTitle(getResources().getString(R.string.app_name));

                //hide the add feed menu option
                hideAddFeed();

            }

        };
*/
        //this handle the hamburger animation
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //initialize swipe to refresh layout
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        //set on refresh listener
        swiperefresh.setOnRefreshListener(this);

        //set the default color of the arrow
        swiperefresh.setColorSchemeResources(R.color.refresh_color);

        //set the articles ListView and the dynamic ListView for custom feeds

        //let's start with the home's ListView

        //initialize the main ListView where items will be added
        list = (ListView) findViewById(android.R.id.list);

        //set the main ListView custom adapter
        adapter = new CustomListAdapter(this);

        list.setAdapter(adapter);

        //handle main ListView clicks
       list.setOnItemClickListener(new

                                            AdapterView.OnItemClickListener() {

                                                @Override
                                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                                        long arg3) {
                                                    int pos = arg2;

                                                    //on item click we send the feed to article activity
                                                    //using intents
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("feed", fFeed);
                                                    Intent intent = new Intent(LActivity.this,
                                                            ArticleActivity.class);
                                                    intent.putExtras(bundle);
                                                    intent.putExtra("pos", pos);

                                                    //and we start the article activity to read the story
                                                    startActivity(intent);
                                                }
                                            }
        );

        //dynamic ListView and database handling

        //initialize the dynamic ListView
        listfeed = (ListView) findViewById(R.id.listfeed);

        //initialize a sqlite database
        //here we open or create a sqlite database where we are going to add 2 tables with the feeds values
        mydb = LActivity.this.openOrCreateDatabase("feedsDB", MODE_PRIVATE, null);

        //create a table called feedslist with a column called "url" where items (the feeds urls) will be stored
        mydb.execSQL("CREATE TABLE IF NOT EXISTS feedslist (id INTEGER PRIMARY KEY AUTOINCREMENT,url varchar);");

        //create a table called subtitles list with a column called "name" where items (the feeds names) will be stored
        mydb.execSQL("CREATE TABLE IF NOT EXISTS subtitleslist (id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar);");

        //using a cursor we're going to read the tables
        final Cursor cursor2 = mydb.rawQuery("SELECT * FROM feedslist;", null);
        final Cursor cursor3 = mydb.rawQuery("SELECT * FROM subtitleslist;", null);

        //we create two new array list to be populated with the tables items (names & urls)

        //for feeds urls
        mUrls = new ArrayList<>();

        //for feeds names
        mFeeds = new ArrayList<>();

        if (cursor2 != null && cursor2.moveToFirst()) {

            while (!cursor2.isAfterLast()) {

                //add items from column "url" into dynamic list
                mUrls.add(cursor2.getString(cursor2.getColumnIndex("url")));
                cursor2.moveToNext();
            }
            cursor2.close();
        }

        if (cursor3 != null && cursor3.moveToFirst()) {

            while (!cursor3.isAfterLast()) {

                //add items from column "name" into dynamic list
                mFeeds.add(cursor3.getString(cursor3.getColumnIndex("name")));
                cursor3.moveToNext();
            }
            cursor3.close();
        }

        //initialize the dynamic list array adapter, we set a template layout and the dynamic list formerly populated
        adapter_dynamic = new CustomDynamicAdapter(this, mUrls, mFeeds);

        //refresh the dynamic list
        adapter_dynamic.notifyDataSetChanged();

        //set the custom adapter
        listfeed.setAdapter(adapter_dynamic);

        //handle the dynamic list items click
   /*     listfeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //display the feed using openNewFeed method
                openNewFeed(mUrls.get(arg2));

            }
        });*/

    }
















    @Override
    public void onRefresh() {

        //detect if there's a connection issue or not: if there's a connection problem stop refreshing and show message
        if (cM.getActiveNetworkInfo() == null) {
            Toast toast = Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT);
            toast.show();
            swiperefresh.setRefreshing(false);

        } else {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    DOMParser tmpDOMParser = new DOMParser();
                    fFeed = tmpDOMParser.parseXml(feedURL);
                    LActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (fFeed != null && fFeed.getItemCount() > 0) {
                                adapter.notifyDataSetChanged();
                                swiperefresh.setRefreshing(false);
                            }
                        }
                    });
                }
            });
            thread.start();
        }

    }


    //this is the custom list adapter for the home ListView
    class CustomListAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public CustomListAdapter(LActivity activity) {

            //initialize layout inflater
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //get items count
        @Override
        public int getCount() {
            return fFeed.getItemCount();
        }

        //get items position
        @Override
        public Object getItem(int position) {
            return position;
        }

        //get items id at selected position
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {

            Activity activity = LActivity.this;
            View listItem = convertView;

            //initialize feedItem
            feedItem = fFeed.getItem(pos);

            //get the feed content
            imageLink = feedItem.getImage();
            imageLink2 = feedItem.getImage2();
            feedTitle = feedItem.getTitle();
            feedDate = feedItem.getDate();

            if (listItem == null) {

                //set the main ListView's layout
                listItem = layoutInflater.inflate(R.layout.items, parent, false);
            }

            //get the chosen items text size from preferences
            //float size = Preferences.resolveTextSizeListResId(getBaseContext());

            //initialize the dynamic items (the title, subtitle)
            TextView lfflTitle = (TextView) listItem.findViewById(R.id.title);
            TextView pubDate = (TextView) listItem.findViewById(R.id.date);

            //dynamically set title and subtitle according to the feed data

            //title
            lfflTitle.setText(feedTitle);

            //subtitle= publication date
            pubDate.setText(feedDate);

            //set the list items text size from preferences in SP unit
            lfflTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            pubDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

            //initialize the ImageView
            ImageView lfflImage = (ImageView) listItem.findViewById(R.id.thumb);

            //if the preference is enabled remove the linear layout containing the ImageView
            if (imagesRemoved(getBaseContext())) {

                LinearLayout linearLayout = (LinearLayout) listItem.findViewById(R.id.layout);
                linearLayout.removeAllViewsInLayout();

            }

            //else, load the image
            //if getImage() method fails (i.e when img is in content:encoded) load image2
            else if (imageLink.isEmpty()) {

                //use glide to load the image into the ImageView (lfflimage)
                Glide.with(activity).load(imageLink2)

                        //set a placeholder image
                        .placeholder(R.drawable.image_area)

                                //disable cache to avoid garbage collection that may produce crashes
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(lfflImage);

                //else use image
            } else {

                Glide.with(activity).load(imageLink)

                        //set a placeholder image
                        .placeholder(R.drawable.image_area)

                                //disable cache to avoid garbage collection that may produce crashes
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(lfflImage);
            }

            return listItem;
        }
    }


    //this is the custom dynamic adapter for the custom feeds ListView
    //we set two items for feeds names/urls and a visible TextView for feeds names

    class CustomDynamicAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        //for feeds urls
        private List<String> mList;

        //for feeds names
        private List<String> mList2;

        public CustomDynamicAdapter(LActivity activity, List<String> list, List<String> list2) {

            //for urls
            mList = list;

            //for feeds names
            mList2 = list2;

            //initialize layout inflater
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //get items (urls) count
        @Override
        public int getCount() {
            return mList.size();
        }

        //get items (urls) position
        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        //get items (urls) id at selected position
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View listItem = convertView;

            if (listItem == null) {

                //set the main ListView's layout
                listItem = layoutInflater.inflate(R.layout.dynamic_items, parent, false);
            }

            //initialize the dynamic items (feeds titles)
            TextView Title = (TextView) listItem.findViewById(R.id.title_dyn);

            //dynamically set feed's title text according to the feed data

            //set feeds titles
            Title.setText(mList2.get(position));

            return listItem;
        }
    }

    public static boolean imagesRemoved(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("images", false);
    }

}
