package com.mycompany.project_mid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/11/18.
 */

public class DetailsActivity extends AppCompatActivity {

    private Person person;
    private int position;
    private TextView textView_name;
    private TextView textView_sentence;
    private TextView textView_t1;
    private TextView textView_t2;
    private TextView textView_t3;
    private TextView textView_total;
    private TextView textView_rank;
    private DonutProgress donutProgress;
    private ArcProgress arcProgress;
    private RoundCornerProgressBar roundCornerProgressBar_force;
    private RoundCornerProgressBar roundCornerProgressBar_defense;
    private RoundCornerProgressBar roundCornerProgressBar_luck;
    private Button button;

    private int max = 1000000000;
    private int total = 80;
    private int total_max = 100;
    private int i = 0;
    private double p = total/total_max;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        getSupportActionBar().hide();
        Window window = getWindow();
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textView_name = (TextView)findViewById(R.id.textView_name);
        textView_sentence = (TextView)findViewById(R.id.textView_sentence);
        textView_t1 = (TextView)findViewById(R.id.textView_t1);
        textView_t2 = (TextView)findViewById(R.id.textView_t2);
        textView_t3 = (TextView)findViewById(R.id.textView_t3);
        textView_total = (TextView)findViewById(R.id.textView_total);
        textView_rank = (TextView)findViewById(R.id.textView_rank);
        donutProgress = findViewById(R.id.donut_progress_total);
        roundCornerProgressBar_force = findViewById(R.id.progress_force);
        roundCornerProgressBar_defense = findViewById(R.id.progress_wisdom);
        roundCornerProgressBar_luck = findViewById(R.id.progress_luck);
        button = findViewById(R.id.detail_button);

        setFonts();
        setButton();
        setViewContentByIntent();
        setProgressBar();
        setDonutProgressBar();


    }

    public void setFonts()
    {
        Typeface textfont1 = Typeface.createFromAsset(getAssets(),"fonts/yanti.ttf");
        textView_name.setTypeface(textfont1);
        textView_sentence.setTypeface(textfont1);
        textView_t1.setTypeface(textfont1);
        textView_t2.setTypeface(textfont1);
        textView_t3.setTypeface(textfont1);
        textView_total.setTypeface(textfont1);
        textView_rank.setTypeface(textfont1);
    }

    public void setProgressBar()
    {

        roundCornerProgressBar_force.setMax(100);
        roundCornerProgressBar_force.setProgress(person.getWisdom());
        roundCornerProgressBar_force.setProgressColor(Color.parseColor("#ffffff"));
        roundCornerProgressBar_force.setPadding(2,2,2,2);

        roundCornerProgressBar_defense.setMax(100);
        roundCornerProgressBar_defense.setProgress(person.getForce());
        roundCornerProgressBar_defense.setProgressColor(Color.parseColor("#ffffff"));
        roundCornerProgressBar_defense.setPadding(2,2,2,2);

        roundCornerProgressBar_luck.setMax(100);
        roundCornerProgressBar_luck.setProgress(person.getLuck());
        roundCornerProgressBar_luck.setProgressColor(Color.parseColor("#ffffff"));
        roundCornerProgressBar_luck.setPadding(1,1,1,1);

    }

    public void setDonutProgressBar()
    {
        donutProgress.setMax(100);
        donutProgress.setProgress(80);
        donutProgress.setUnfinishedStrokeColor(Color.parseColor("#fffff0"));
        donutProgress.setFinishedStrokeColor(Color.parseColor("#000000"));
        donutProgress.setScrollBarSize(30);
        donutProgress.setTextColor(Color.parseColor("#000000"));
        donutProgress.setFinishedStrokeWidth(70);
        donutProgress.setTextSize(110);
        donutProgress.setText("");
        double compute_total = person.getWisdom()*0.3+person.getForce()*0.5+person.getLuck()*0.2;
        if(compute_total>75)
            textView_rank.setText("猛");
        else if(compute_total<75&&compute_total>50)
            textView_rank.setText("常");
        else if(compute_total<50)
            textView_rank.setText("庸");

//        for(i=0;i<=max*p;i++)
//        {
//            if(i%500==0)
//            {
//                new UpdateUI().start();
//            }
//        }

    }

    class UpdateUI extends Thread
    {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try
                    {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    //donutProgress.setProgress(i/max*100);
                    donutProgress.setProgress(70);
                }
            });
        }
    }


    private void setViewContent(Person person)
    {
        //设置图像
        //imageView.setImageResource(person.getImageId());
        //设置名字
        if(person.getName().length()==3){
            textView_name.setTextSize(70);
        }
        else if(person.getName().length()==4) {
            textView_name.setTextSize(50);
        }
        else {
            textView_name.setTextSize(70);
        }
        textView_name.setText(person.getName());
        textView_sentence.setText("----天下无双");

    }

    private void setViewContentByIntent()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int personId = bundle.getInt("personId");
        person = DataSupport.find(Person.class, personId);
        setViewContent(person);
    }

    private void setButton(){
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
               finish();
            }
        });
    }



}
