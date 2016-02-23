package ua.wa.projects.maks.tevent.main;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jsoup.Jsoup;

import ua.wa.projects.maks.tevent.DOMParser.RSSFeed;
import ua.wa.projects.maks.tevent.DOMParser.RSSItem;
import ua.wa.projects.maks.tevent.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {


    //feed
    RSSFeed fFeed;
    RSSItem feedItem;
    String feedDescription;
    String feedCompleteDescription;
    String datDescription;
    String imageLink;
    String imageLink2;
    String feedLink;
    String feedTitle;
    String feedDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //position
        int fPos;

        //initialize the feed (i.e. get all the data)
        fFeed = (RSSFeed) getArguments().getSerializable("feed");
        fPos = getArguments().getInt("pos");
        feedItem = fFeed.getItem(fPos);

        //get the content
        feedCompleteDescription = feedItem.getCompleteDescription();
        feedDescription = feedItem.getDescription();
        imageLink = feedItem.getImage();
        imageLink2 = feedItem.getImage2();
        feedLink = feedItem.getLink();
        feedTitle = feedItem.getTitle();
        feedDate = feedItem.getDate();
        datDescription = feedItem.getDescription();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set the view
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.article_fragment, container, false);

        //article title and subtitle
        //title
        TextView title = (TextView) rootView.findViewById(R.id.title);

        //subtitle
        TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle);

        //action Buttons

        //read more button
        ImageButton button_continue_reading = (ImageButton) rootView.findViewById(R.id.button_continue);

        //this is the method to handle the continue reading button click
        View.OnClickListener listener_forward = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                continueReading();

            }
        };

        //set Read more listener
        button_continue_reading.setOnClickListener(listener_forward);

        //share button
        ImageButton button_share = (ImageButton) rootView.findViewById(R.id.button_share);

        //this is the method to handle the share button click
        View.OnClickListener listener_share = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        };

        //set Share listener
        button_share.setOnClickListener(listener_share);

        //dynamically set title and subtitle according to the feed data
        //set title of the article
        title.setText(feedTitle);

        //set the date of the article to subtitle
        subtitle.setText(feedDate);

        //ImageView
        ImageView imageView = (ImageView) rootView.findViewById(R.id.img);

        //initialize the article view linear layout
        LinearLayout article_default_linearLayout = (LinearLayout) rootView.findViewById(R.id.article_linearlayout);

        if (LActivity.imagesRemoved(getContext())) {

            article_default_linearLayout.removeView(imageView);

        }

        //else, load the image
        //if getImage() method fails (i.e when img is in content:encoded) load image2
        else if (imageLink.isEmpty()) {

            //use glide to load the image into the ImageView (imageView)
            Glide.with(getActivity()).load(imageLink2)

                    //disable cache to avoid garbage collection that may produce crashes
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);

            //else use image
        } else {

            //load the parsed article's image using glide
            Glide.with(getActivity()).load(imageLink)

                    //disable cache to avoid garbage collection that may produce crashes
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        }


        //use complete description by default, but sometimes the method returns null
        if (feedCompleteDescription.contains("no desc")) {
            datDescription = Jsoup.parse(feedDescription).text().replace("Continua a leggere...", "");

            // else, use complete description
        } else {
            datDescription = Jsoup.parse(feedCompleteDescription).text().replace("Continua a leggere...", "");

        }

        //replace some items since this is a simple TextView
        String datDescription2format = datDescription.replace("Continue reading...", "");

        String datDescription3format = datDescription2format.replace("Visit on site http://www.noobslab.com", "");

        String datDescription4format = datDescription3format.replace("Read More", "");

        String datDescription5format = datDescription4format.replace("(read more)", "");

        //load the article inside a TextView
        TextView articletext = (TextView) rootView.findViewById(R.id.webv);

        //set the articles text
        articletext.setText(datDescription5format);


        return rootView;

    }

    //share method
    private void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, feedLink);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share) + " '" + feedTitle + "'"));
    }

    //read more method
    private void continueReading() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(feedLink));
        CharSequence title2 = getResources().getText(R.string.chooser_title);
        Intent chooser = Intent.createChooser(intent, title2);
        startActivity(chooser);
    }
}
