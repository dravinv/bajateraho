package com.awesome.dravin.appdemo1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

public class VideoList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int playingMode = 1;

    VideoDatabaseAdapter videoDatabaseAdapter;
    String[] items, newItems;
    String struri, UriStr, NameStr;
    Uri uri;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toast.makeText(VideoList.this, "Update the list from the nav drawer", Toast.LENGTH_SHORT).show();

        listView = (ListView) findViewById(R.id.VideoList);

        videoDatabaseAdapter = new VideoDatabaseAdapter(this);

        UriStr = videoDatabaseAdapter.getAllUri();

        NameStr = videoDatabaseAdapter.getAllName();

        items = UriStr.split("\n");

        newItems = NameStr.split("\n");

        Integer[] imgArr = {R.drawable.video_list_icon};
        //ArrayAdapter<String> adp = new ArrayAdapter<String>(VideoList.this, android.R.layout.simple_list_item_1, newItems);

        CustomVideoList adp = new CustomVideoList(VideoList.this, newItems, imgArr);

        listView.setAdapter(adp);


        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        struri = items[position];

                        if (playingMode == 1) {
                            Toast.makeText(VideoList.this, "Playing", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VideoList.this, VideoPlayer.class).putExtra("uri", items[position]);
                            startActivity(intent);

                        } else if (playingMode == 0) {
                            Toast.makeText(VideoList.this, "Sharing", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VideoList.this, MainActivity.class).putExtra("uri", items[position]).putExtra("flag", 1);
                            startActivity(intent);
                        } else if(playingMode == 2){
                            File file = new File(items[position]);
                            Toast.makeText(VideoList.this, "Sharing Externally", Toast.LENGTH_SHORT).show();
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                            shareIntent.setType("video/*");
                            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                        }
                    }

                }
        );

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.video_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            playingMode = 1;
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            playingMode = 0;
        } else if (id == R.id.nav_share) {
            String str = null;
            videoDatabaseAdapter.deleteDB();
            Toast.makeText(VideoList.this, "Search initiated please wait a few seconds", Toast.LENGTH_LONG).show();
            new FindAsync().execute(str);
        }else if(id == R.id.nav_share_ext){
            playingMode = 2;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class FindAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(VideoList.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //dialog.setMax(100);
            dialog.setMessage("Finding Videos please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            findsongs(Environment.getExternalStorageDirectory());
            dialog.dismiss();
            return null;

        }

        public void findsongs(File root) {
            File[] files = root.listFiles();
            String str1, str2;
            for (File singlefile : files)
                if (singlefile.isDirectory() && !singlefile.isHidden()) {
                    findsongs(singlefile);

                } else {
                    if (singlefile.getName().endsWith(".mp4")) {
                        str1 = singlefile.getName().toString();
                        uri = Uri.parse(singlefile.toString());
                        str2 = uri.toString();
                        add(str1, str2);
                    }
                }

        }

        public void add(String s1, String s2) {
            long id = videoDatabaseAdapter.insertData(s1, s2);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(VideoList.this, "Search Successfull, navigate to req mode", Toast.LENGTH_SHORT).show();
            listView = (ListView) findViewById(R.id.VideoList);
            videoDatabaseAdapter = new VideoDatabaseAdapter(VideoList.this);
            UriStr = videoDatabaseAdapter.getAllUri();

            NameStr = videoDatabaseAdapter.getAllName();

            items = UriStr.split("\n");

            newItems = NameStr.split("\n");

            ArrayAdapter<String> adp = new ArrayAdapter<String>(VideoList.this, android.R.layout.simple_list_item_1, newItems);
            listView.setAdapter(adp);
            super.onPostExecute(s);
        }
    }
}
