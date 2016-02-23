package ua.wa.projects.maks.tevent.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;

import ua.wa.projects.maks.tevent.DOMParser.RSSFeed;
import ua.wa.projects.maks.tevent.R;

public class ArticleActivity extends AppCompatActivity {

    RSSFeed feed;

    //position
    int pos;

    //context
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //set the view
        setContentView(R.layout.article_activity);

        //get the feed using intents
        feed = (RSSFeed) getIntent().getExtras().get("feed");
        pos = getIntent().getExtras().getInt("pos");

        //get the context
        context = getBaseContext();

        //initialize ViewPager and the adapter
        ViewPager pager;
        PagerAdapter mPagerAdapter;

        //set the viewpager that allows to swipe through articles
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(mPagerAdapter);
        pager.setCurrentItem(pos);
        pager.setClipToPadding(false);

    }

    //viewpager custom adapter, use FragmentStatePagerAdapter to handle a large number of items
    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            ArticleFragment frag = new ArticleFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("feed", feed);
            bundle.putInt("pos", position);
            frag.setArguments(bundle);

            return frag;

        }

        @Override
        public int getCount() {
            return feed.getItemCount();
        }
    }
}

