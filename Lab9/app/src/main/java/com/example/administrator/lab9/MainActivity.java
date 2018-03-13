package com.example.administrator.lab9;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    public List<Github> list_infos;
    public RecyclerView recyclerView_info;
    public Button button_fetch;
    public Button button_clear;
    public EditText editText_search;
    public Observer<Github> githubObserver;
    public CommonAdapter commonAdapter = null;
    public ProgressBar progressBar_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        recyclerView_info = (RecyclerView)findViewById(R.id.RecyclerView_info);
        button_clear = (Button)findViewById(R.id.button_clear);
        button_fetch = (Button)findViewById(R.id.button_fetch);
        editText_search = (EditText)findViewById(R.id.editText_search);
        progressBar_load = (ProgressBar)findViewById(R.id.progressBar_load);
        list_infos = new ArrayList<>();

        set_RecyclerView();
        set_buttons();
        set_Observer();

    }

    public void set_Observer()
    {
        githubObserver = new Observer<Github>() {
            @Override
            public void onSubscribe(Disposable d) {
                recyclerView_info.setVisibility(View.GONE);
                progressBar_load.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(Github value) {
                list_infos.add(value);
                commonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                recyclerView_info.setVisibility(View.VISIBLE);
                progressBar_load.setVisibility(View.GONE);

            }
        };
    }

    public void set_buttons()
    {
        button_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = editText_search.getText().toString();
                if(user_name.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "user name is empty!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    start_Service(user_name);
                }
            }
        });
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_search.setText("");
                list_infos.clear();
                commonAdapter.notifyDataSetChanged();
            }
        });
    }

    public void start_Service(String name)
    {
        Retrofit retrofit = ServiceFactory.createRetrofit();
        ServiceFactory.GithubService github = retrofit.create(ServiceFactory.GithubService.class);
        Observable<Github> observable = github.getUser(name);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(githubObserver);
    }

    public void set_RecyclerView()
    {

        // good_list.setLayoutManager(new LinearLayoutManager(this));
        progressBar_load.setVisibility(View.GONE);

         commonAdapter = new CommonAdapter<Github>(this, R.layout.layout_cardview, list_infos) {
            @Override
            public void convert(View_Holder holder, Github github) {

                TextView textView_1 = holder.getView(R.id.textView_1);
                TextView textView_2 = holder.getView(R.id.textView_2);
                TextView textView_3 = holder.getView(R.id.textView_3);

                textView_1.setText(github.getLogin());
                textView_2.setText(Integer.toString(github.getId()));
                textView_3.setText(github.getBlog());

            }
        };

        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this,RepositActivity.class);
                intent.putExtra("name",list_infos.get(position).getLogin());
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                list_infos.remove(position);
                commonAdapter.notifyDataSetChanged();
            }
        });

        recyclerView_info.setAdapter(commonAdapter);
        recyclerView_info.setLayoutManager(new LinearLayoutManager(this));
    }
}
