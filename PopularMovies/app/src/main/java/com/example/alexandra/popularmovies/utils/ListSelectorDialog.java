package com.example.alexandra.popularmovies.utils;


import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

import com.example.alexandra.popularmovies.models.Video;

/**
 * Created by Alexandra on 23-11-2015.
 */
public class ListSelectorDialog {
    Context context;
    Builder adb;
    String title;

    public interface listSelectorInterface {
        void selectedItem(String key, String item);
        void selectorCanceled();
    }


    public ListSelectorDialog(Context c, String newTitle) {
        this.context = c;
        this.title = newTitle;
    }

    ListSelectorDialog setTitle(String newTitle) {
        this.title = newTitle;
        return this;
    }

    public ListSelectorDialog show(ArrayList<Video> videoArrayList, final listSelectorInterface di) {
        String nameList[] = new String[videoArrayList.size()];
        String keyList[] = new String[videoArrayList.size()];
        for(int i=0; i< videoArrayList.size(); i++){
            nameList[i]= videoArrayList.get(i).getName();
            keyList[i] = videoArrayList.get(i).getKey();
        }

        show(nameList, keyList, di);
        return this;
    }

    public ListSelectorDialog show(final String[] itemList, final String[] keyList,
                            final listSelectorInterface di) {
        adb = new AlertDialog.Builder(context);
        adb.setCancelable(false);
        adb.setItems(itemList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int n) {
                d.dismiss();
                di.selectedItem(keyList[n], itemList[n]);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            // when user clicks cancel, notify our interface
            public void onClick(DialogInterface d, int n) {
                d.dismiss();
                di.selectorCanceled();
            }
        });
        adb.setTitle(title);
        adb.show();
        return this;
    }
}