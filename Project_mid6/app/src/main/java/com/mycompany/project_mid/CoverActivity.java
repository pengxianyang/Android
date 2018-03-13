package com.mycompany.project_mid;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static android.R.attr.delay;

/**
 * Created by Administrator on 2017/11/20.
 */

public class CoverActivity extends AppCompatActivity {
    private int imgIds[] = {R.mipmap.cover0, R.mipmap.cover1, R.mipmap.cover2,R.mipmap.cover3,R.mipmap.cover4};
    private List<View> viewList = new ArrayList<>();
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ObjectAnimator animator_button;
    Button button;
    private int numOfPage = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();
        Window window = getWindow();
        int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);

        setButton();
        init();
        setMove();
        setAnimate();

    }

    public void setAnimate()
    {
        animator_button = new ObjectAnimator();
        animator_button = ObjectAnimator.ofFloat(button,"alpha", 0,1);
        animator_button.setDuration(2000);
    }

    public void init()
    {
        for (int i = 0; i < numOfPage; i++) {
            View rootView = View.inflate(CoverActivity.this, R.layout.activity_cover, null);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView_cover);
//            Bitmap bm = BitmapMethod.readBitMap(this,imgIds[i]);
//            imageView.setImageBitmap(bm);
            imageView.setImageResource(imgIds[i]);
            //TextView textView = (TextView) rootView.findViewById(R.id.textView_coverindex);
            //textView.setText(String.valueOf(i));
            viewList.add(i, rootView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyAdapter());
        //viewPager.setPageTransformer(true ,new SimplePageTransform());

        circleIndicator = (CircleIndicator)findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);
    }


    public void setMove()
    {

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==3)
                {
                    button.setVisibility(View.VISIBLE);
                    animator_button.start();
                }
                else {
                    button.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
    }

    public void setButton()
    {
        button = findViewById(R.id.cover_button);
        button.setClickable(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoverActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return numOfPage;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

    }

    public class SimplePageTransform implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            int width = view.getWidth();
            int pivotX = 0;
            if (position <= 1 && position > 0) {// right scrolling
                pivotX = 0;
            } else if (position == 0) {

            } else if (position < 0 && position >= -1) {// left scrolling
                pivotX = width;
            }
            //设置x轴的锚点
            view.setPivotX(pivotX);
            //设置绕Y轴旋转的角度
            view.setRotationY(90f * position);
        }
    }

}
