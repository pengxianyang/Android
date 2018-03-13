package com.mycompany.project_mid;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.mycompany.project_mid.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class SearchActivity extends Activity{

    private RecyclerView recyclerView;
    private GifImageView gifImageView;
    private TextView textView;
    private CommonAdapter commonAdapter;
    private List<Person> persons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Window window = getWindow();
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.activity_search_recyclerView);
        gifImageView = findViewById(R.id.activity_search_gifImageView);
        textView = findViewById(R.id.activity_search_textView);


        EventBus.getDefault().register(this);
        String action=getIntent().getAction();
        if(action.equals(Intent.ACTION_SEARCH)){
            String queryString=getIntent().getStringExtra(SearchManager.QUERY);
            persons = DataSupport.where("name = ?",queryString).find(Person.class);
        }
        if(persons.size()==0){
            recyclerView.setVisibility(View.GONE);
            gifImageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            gifImageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            setRecyclerView();
        }
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setRecyclerView(){
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        commonAdapter = new CommonAdapter<Person>(this, R.layout.item, persons) {
            @Override
            public void convert(ViewHolder holder, Person person) {
                ImageView imageView = holder.getView(R.id.item_imageView);
                Bitmap bitmap = BitmapFactory.decodeByteArray(person.getImageId(),0,person.getImageId().length);
                imageView.setImageBitmap(bitmap);
                TextView textView = holder.getView(R.id.item_textView);
                textView.setText(person.getName());
            }
        };

        commonAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(SearchActivity.this, InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("personId", persons.get(position).getId());
                bundle.putInt("position",position);
                bundle.putString("change","Search_Info");
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(final int position) {

            }
    });

        recyclerView.setAdapter(commonAdapter);
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenMainThread(MessageEvent_person_position event) {
        int personId = event.getPersonId();
        persons.set(event.getPosition(),DataSupport.find(Person.class,personId));
        commonAdapter.notifyDataSetChanged();
    }
}
