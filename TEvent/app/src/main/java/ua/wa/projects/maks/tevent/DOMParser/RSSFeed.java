package ua.wa.projects.maks.tevent.DOMParser;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Created by Max on 18.02.2016.
 */
public class RSSFeed implements Serializable {


    //serializable ID
    private static final long serialVersionUID = 1L;

    //create a new item list
    private List<RSSItem> _itemlist;

    //create a new item count
    private int _itemcount = 0;

    //initialize the item list
    RSSFeed() {
        _itemlist = new Vector<>(0);
    }

    void addItem(RSSItem item) {

        //add an item to the Vector
        _itemlist.add(item);

        //increment the item count
        _itemcount++;
    }

    public RSSItem getItem(int location) {

        //return the item at the chosen position
        return _itemlist.get(location);
    }

    public int getItemCount() {

        //return the number of items in the feed
        return _itemcount;

    }
}
