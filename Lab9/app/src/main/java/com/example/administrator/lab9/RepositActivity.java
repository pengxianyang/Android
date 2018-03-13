package com.example.administrator.lab9;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/12/21.
 */

public class RepositActivity extends AppCompatActivity {

    public ListView listView_reposit;
    public List<Map<String,String>> list_infos;
    public Observer<List<Repositories>> repositObserver;
    public ProgressBar progressBar_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        getSupportActionBar().hide();

        listView_reposit = (ListView)findViewById(R.id.ListView_reposit);
        progressBar_load = (ProgressBar)findViewById(R.id.progressBar_load);
        list_infos = new ArrayList<>();


        set_Observer();
        set_Data();

    }



    public void set_Observer()
    {
        repositObserver = new Observer<List<Repositories>>() {
            @Override
            public void onSubscribe(Disposable d) {
                listView_reposit.setVisibility(View.GONE);
                progressBar_load.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(List<Repositories> value) {
                //list_infos.add(value);
                int len = value.size();


                for(int i=0;i<=len-1;i++)
                {
                    Map<String,String> map = new LinkedHashMap<>();

                    map.put("name",value.get(i).getName());
                    map.put("language",value.get(i).getLanguage());
                    map.put("description",value.get(i).getDescription());

                    list_infos.add(map);
                }



                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        RepositActivity.this,
                        list_infos,
                        R.layout.layout_cardview,
                        new String[]{"name", "language","description"},
                        new int[]{R.id.textView_1, R.id.textView_2,R.id.textView_3});

                listView_reposit.setAdapter(simpleAdapter);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                progressBar_load.setVisibility(View.GONE);
                listView_reposit.setVisibility(View.VISIBLE);

            }
        };
    }

    public void set_Data()
    {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        start_Service(name);
    }

    public void start_Service(String name)
    {
        Retrofit retrofit = ServiceFactory.createRetrofit();
        ServiceFactory.GithubService github = retrofit.create(ServiceFactory.GithubService.class);
        Observable<List<Repositories>> observable = github.getUserRepos(name);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repositObserver);
    }


}
