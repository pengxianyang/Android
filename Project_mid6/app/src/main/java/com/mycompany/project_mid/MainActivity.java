package com.mycompany.project_mid;

import android.app.ActionBar;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Data
    private List<Person> persons = new ArrayList<>();
    private int[] imageIds = new int[]{R.mipmap.zhaoyun, R.mipmap.lvbu, R.mipmap.guanyu, R.mipmap.sunshangxiang};
    private String [] names = new String[]{"趙雲", "呂布", "關羽", "孫尚香"};
    private String [] births = new String[]{};
    private String [] deaths = new String[]{};
    private String [] briefs = new String[]{};
    private String [] sexs = new String[]{};
    private String [] nations = new String[]{};
    private String [] nativePlaces = new String[]{};

    //UI
    private RecyclerView recyclerView;
    private CommonAdapter commonAdapter;
    private Button button;
    public static byte[] bytes_inibitmap; //供Edit界面使用

    //Music
    public static MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Window window = getWindow();
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);
        //window.setNavigationBarColor(Color.parseColor("#eee8aa"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.activity_main_recyclerView);
        button = findViewById(R.id.activity_main_button);

        DataSupport.deleteAll(Person.class);
        initDataBase();
        initData();
        setRecyclerView();
        setButton();
        setIntentToCover();
        setMusic();
        //EventBus.getDefault().register(this);
    }

    public void setMusic()
    {
        //musicService = new MusicService();
        mp = new MediaPlayer();
        try
        {
            mp.setDataSource(Environment.getExternalStorageDirectory() + "/BGM.mp3");
            mp.prepare();
            mp.setLooping(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mp.start();
    }

    @Override
    protected  void onRestart(){
        super.onRestart();
        //showMsg("Restart");
        persons.clear();
        persons.addAll(DataSupport.findAll(Person.class));
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.pause();
        //EventBus.getDefault().unregister(this);
    }

    public void setIntentToCover()
    {
        Intent intent = new Intent(MainActivity.this,CoverActivity.class);
        startActivity(intent);
    }
    //创建actionbar按钮菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getMenuInflater().inflate(R.menu.actionbar_menu_mainact, menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchManager searchManager=(SearchManager)this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView) searchItem.getActionView();
        if(searchView==null){
            showMsg("searchView is null");
            return true;
        }
        ComponentName cn=new ComponentName(this,SearchActivity.class);
        SearchableInfo info=searchManager.getSearchableInfo(cn);
        if(info==null){
            showMsg("cannot get the SearchableInfo");
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        SearchQueryTextListener searchQueryTextListener=new SearchQueryTextListener();
        searchView.setOnQueryTextListener(searchQueryTextListener);
        searchView.setIconifiedByDefault(false);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public class SearchQueryTextListener implements SearchView.OnQueryTextListener{

        @Override
        public boolean onQueryTextChange(String newText) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {

            return false;
        }

    }
    //创建数据库
    private void initDataBase(){
        LitePal.getDatabase();
        for(int i=0;i<names.length;i++){
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),imageIds[i]);
            Bitmap bitmap = BitmapMethod.readBitMap(this, imageIds[i]);
            byte [] bytes = BitmapMethod.ConvertBitmaptoBytes(bitmap);
            if(i==0)bytes_inibitmap = bytes;
            Person person = new Person(i,bytes, names[i],"","","","","","","此处为句子",50,50,50);
            person.save();
        }

    }

    private void initData(){
        persons = DataSupport.findAll(Person.class);
    }

    private void setRecyclerView(){
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        //initData();
        commonAdapter = new CommonAdapter<Person>(this, R.layout.item, persons) {
            @Override
            public void convert(ViewHolder holder, Person person) {
                ImageView imageView = holder.getView(R.id.item_imageView);
                //imageView.setImageResource(person.getImageId());
                Bitmap bitmap = BitmapFactory.decodeByteArray(person.getImageId(),0,person.getImageId().length);
                imageView.setImageBitmap(bitmap);

                TextView textView = holder.getView(R.id.item_textView);
                textView.setText(person.getName());
                //Typeface textfont1 = Typeface.createFromAsset(getAssets(),"fonts/yanti.ttf");
                //textView.setTypeface(textfont1);
            }
        };

        commonAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("personId", persons.get(position).getId());
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(final int position) {
                final AlertDialog.Builder message = new AlertDialog.Builder(MainActivity.this);
                //message.setTitle("移除");
                message.setMessage("是否移除该项");
                message.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i){
                    }
                });
                message.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i){
                        DataSupport.delete(Person.class, persons.get(position).getId());
                        persons.remove(position);
                        commonAdapter.notifyDataSetChanged();
                    }
                });
                message.create().show();
            }
        });

        recyclerView.setAdapter(commonAdapter);
        SpacesItemDecoration decoration=new SpacesItemDecoration(5);
        recyclerView.addItemDecoration(decoration);
    }

    private void setButton(){
        button.setClickable(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("change", "Main_Edit");
                bundle.putInt("listLen", persons.size());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }




}
