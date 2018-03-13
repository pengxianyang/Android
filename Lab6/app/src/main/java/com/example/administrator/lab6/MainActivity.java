package com.example.administrator.lab6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public List<Github> list_infos;
    public RecyclerView recyclerView_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView_info = (RecyclerView)findViewById(R.id.RecyclerView_info);


        set_RecyclerView();

    }

    public void set_RecyclerView()
    {

        // good_list.setLayoutManager(new LinearLayoutManager(this));

        final CommonAdapter commonAdapter = new CommonAdapter<Map<String, Object>>(this, R.layout.layout_cardview, list_infos) {
            @Override
            public void convert(View_Holder holder, Map<String, Object> s) {

                TextView textView_1 = holder.getView(R.id.textView_1);
                TextView textView_2 = holder.getView(R.id.textView_2);
                TextView textView_3 = holder.getView(R.id.textView_3);

            }
        };

        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {


            }

            @Override
            public void onLongClick(int position) {


            }
        });

        recyclerView_info.setAdapter(commonAdapter);
        recyclerView_info.setLayoutManager(new LinearLayoutManager(this));
    }
}
