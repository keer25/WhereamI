package com.example.root.whereami;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PuzzleActivity extends AppCompatActivity {

    String[] items;
    int[] colors;
    View parentView;
    View hotspotView;
    Spinner spinner;
    static SparseArray<ArrayList<Coordinate>> foundHash = new SparseArray<>();
    static SparseArray<ArrayList<Coordinate>> coordHash = new SparseArray<>();

    String[] displayItems;

    //Count of repeat objects
    static int count[] = new int[12];
    int left;
    int top;
    float widthScale;
    float heightScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        items = getResources().getStringArray(R.array.items);
        displayItems = getResources().getStringArray(R.array.display);
        colors = getResources().getIntArray(R.array.colors);
        assignCount();
        initiateHash(foundHash);
        initiateHash(coordHash);

        assignCoordHash();

        spinner = (Spinner) findViewById(R.id.spinner);
        CustomArrayAdapter adapter = new CustomArrayAdapter(this);
        spinner.setAdapter(adapter);

        parentView = findViewById(R.id.parentView);
        hotspotView = findViewById(R.id.image_areas);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP){

                    final int evX = (int) motionEvent.getX();
                    final int evY = (int) motionEvent.getY();

                    int touchColor = getHotspotColor(R.id.image_areas, evX, evY);
                    ColorTool ct = new ColorTool();
                    int tolerance = 25;
                    for (int i=0; i<colors.length; i++){
                        if(ct.closeMatch(colors[i], touchColor, tolerance)) {
                            if (foundHash.valueAt(i).size() != count[i]) {
                                //trigger getView
                                final int dummyi = i;
                                spinner.post(new Runnable() {
                                    public void run() {
                                        spinner.setSelection(dummyi + 1);
                                    }
                                });

                                if (coordHash.get(i).size() > 0) {
                                    Coordinate coordinate = compareClosestX(evX, evY, i);
                                    boolean contains = false;
                                    for (Coordinate co : foundHash.get(i)){
                                        if (co.eq(coordinate)) {
                                            contains = true;
                                            break;
                                        }
                                    }
                                    if (!contains) {
                                        foundHash.get(i).add(coordinate);
                                        String display = "You Found " + displayItems[i];
                                        Snackbar.make(parentView, display, Snackbar.LENGTH_SHORT).show();
                                    }
                                }else {
                                    foundHash.get(i).add(new Coordinate(evX, evY));
                                    String display = "You Found " + displayItems[i];
                                    Snackbar.make(parentView, display, Snackbar.LENGTH_SHORT).show();
                                }
                                if (countHash(foundHash) == 12) {
                                    Intent intent = new Intent(getApplicationContext(), WonActivity.class);
                                    startActivity(intent);
                                }
                            }
                            break;
                        }
                    }
                }
                return true;
            }
        });
    }

    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById (hotspotId);
        img.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        img.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ImageView imageView = (ImageView) findViewById(R.id.image);
            int[]  coords = {0, 0};
            imageView.getLocationOnScreen(coords);
            top = coords[1];
            left = coords[0];
            widthScale = (float) ((double) imageView.getWidth() /800.0);
            heightScale = (float) ((double) imageView.getHeight() /430.0);
        }

    }

    void assignCount(){
        count[0] = 1; count[1] = 1;
        count[2] = 2; count[3] = 1;
        count[4] = 5; count[5] = 1;
        count[6] = 1; count[7] = 2;
        count[8] = 2; count[9] = 4;
        count[10] = 1; count[11] = 1;
    }

    void initiateHash(SparseArray<ArrayList<Coordinate>> foundHash){
        for (int i=0; i<12; i++){
            foundHash.put(i, new ArrayList<Coordinate>());

        }
    }

    int countHash(SparseArray<ArrayList<Coordinate>> hash){
        int count = 0;
        for(int i=0; i<12; i++){
            if (hash.get(i).size() == coordHash.get(i).size()) {
                count += 1;
            }
        }
        return count;
    }

    void assignCoordHash() {
        //Ant
        coordHash.get(4).add(new Coordinate(33, 382));
        coordHash.get(4).add(new Coordinate(165, 286));
        coordHash.get(4).add(new Coordinate(772, 392));
        coordHash.get(4).add(new Coordinate(767, 79));
        coordHash.get(4).add(new Coordinate(663, 33));
        //flowers
        coordHash.get(9).add(new Coordinate(11, 158));
        coordHash.get(9).add(new Coordinate(24, 141));
        coordHash.get(9).add(new Coordinate(101, 135));
        coordHash.get(9).add(new Coordinate(186, 261));
        //owls
        coordHash.get(2).add(new Coordinate(198, 62));
        coordHash.get(2).add(new Coordinate(240, 124));
        //Cats
        coordHash.get(7).add(new Coordinate(686, 338));
        coordHash.get(7).add(new Coordinate(537, 226));
        //SLippers
        coordHash.get(8).add(new Coordinate(486, 305));
        coordHash.get(8).add(new Coordinate(682, 287));
    }

    Coordinate compareClosestX(int x, int y, int i){
        float diff = (int) Double.POSITIVE_INFINITY;
        Coordinate coordinate = new Coordinate(0, 0);
        for (Coordinate co : coordHash.get(i)){
            float newDiff = Math.abs(((float) x - (float) left)/widthScale - (float) co.x);
            if (newDiff < diff) {
                diff = newDiff;
                coordinate = co;
            }
        }
        return coordinate;
    }


}