package com.mycompany.project_mid;

import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.transition.Visibility;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.project_mid.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class InfoActivity extends AppCompatActivity {

    private Person person;
    private int position = 0;
    private int numOfPage = 2;
    //private int[] imgIds;
    private Bitmap[] bitmaps;
    private List<View> viewList = new ArrayList<>();

    //private ImageView imageView;
    //private  View rootView;
    private TextView textView_name;
    private TextView textView_sex;
    private TextView textView_birth_death;
    private TextView textView_nation;
    private TextView textView_brief;
    private TextView textView_sentence;
    private ViewPager viewPager;
    private MyAdapter myAdapter;
    private Button button;

    private CircleIndicator circleIndicator;

    private ObjectAnimator animator_name;
    private ObjectAnimator animator_setence;
    private ObjectAnimator animator_sex;
    private ObjectAnimator animator_birth_death;
    private ObjectAnimator animator_nation;
    private ObjectAnimator animator_brief;



    @Override
    protected void onCreate(Bundle savedInstanceState){

        Window window = getWindow();
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        //imageView = (ImageView) findViewById(R.id.info_head_sculpture);
        //imgIds = new int[] {R.mipmap.diaochan, R.mipmap.zhenji};
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        circleIndicator = (CircleIndicator)findViewById(R.id.indicator);
        textView_name = (TextView) findViewById(R.id.info_name);
        textView_brief = (TextView) findViewById(R.id.info_brief);
        textView_birth_death = (TextView) findViewById(R.id.info_birth_death);
        textView_nation = (TextView) findViewById(R.id.info_belonging);
        textView_sex = (TextView) findViewById(R.id.info_sex);
        textView_sentence = (TextView) findViewById(R.id.info_sentence);
        button = (Button)findViewById(R.id.info_button);
        //rootView = View.inflate(InfoActivity.this, R.layout.info, null);
        //imageView = (ImageView) rootView.findViewById(R.id.info_head_sculpture);
        bitmaps = new Bitmap[2];
        //bitmaps[1]=BitmapFactory.decodeResource(getResources(),R.mipmap.zhenji);
        bitmaps[1]=BitmapMethod.readBitMap(this,R.mipmap.cover_info2);

        EventBus.getDefault().register(this);

        textView_sex.setVisibility(GONE);
        textView_nation.setVisibility(GONE);
        textView_birth_death.setVisibility(GONE);
        textView_brief.setVisibility(GONE);
        textView_name.setVisibility(VISIBLE);
        textView_sentence.setVisibility(VISIBLE);
        //imageView.setVisibility(VISIBLE);



        setButton();
        setViewContentByIntent();
        initViewPage();
        setMove();
        setAnimate();

    }

    public void setAnimate()
    {
        animator_name = new ObjectAnimator();
        animator_setence = new ObjectAnimator();
        animator_sex = new ObjectAnimator();
        animator_birth_death = new ObjectAnimator();
        animator_nation = new ObjectAnimator();
        animator_brief = new ObjectAnimator();

        animator_name = ObjectAnimator.ofFloat(textView_name,"alpha", 0,1);
        animator_name.setDuration(2000);
        //animator_name.start();
        animator_setence = ObjectAnimator.ofFloat(textView_sentence,"alpha", 0,1);
        animator_setence.setDuration(2000);

        animator_sex = ObjectAnimator.ofFloat(textView_sex,"alpha", 0,1);
        animator_sex.setDuration(2000);

        animator_birth_death = ObjectAnimator.ofFloat(textView_birth_death,"alpha", 0,1);
        animator_birth_death.setDuration(2000);

        animator_brief = ObjectAnimator.ofFloat(textView_brief,"alpha", 0,1);
        animator_brief.setDuration(2000);

        animator_nation = ObjectAnimator.ofFloat(textView_nation,"alpha", 0,1);
        animator_nation.setDuration(2000);

        animator_name.start();
        animator_setence.start();
    }

    @Override
    protected  void onRestart(){
        super.onRestart();
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //创建actionbar按钮菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.actionbar_menu_infoact, menu);
        MenuItem menuItem = menu.findItem(R.id.action_edit);
        menuItem.setIcon(R.drawable.edit);
        menuItem.setTitle("编辑");
        return true;
    }

    //为actionbar按钮设置点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            //Toast.makeText(InfoActivity.this,"编辑按钮被点击",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(InfoActivity.this, EditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("personId", person.getId());
            bundle.putInt("position", position);
            bundle.putString("change", "Info_Edit");
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setfonts()
    {
        Typeface textfont1 = Typeface.createFromAsset(getAssets(),"fonts/yanti.ttf");
        textView_name.setTypeface(textfont1);
        textView_brief.setTypeface(textfont1);
        textView_name.setTypeface(textfont1);
        textView_birth_death.setTypeface(textfont1);
        textView_nation.setTypeface(textfont1);
        textView_sex.setTypeface(textfont1);
        textView_sentence.setTypeface(textfont1);
    }


    private void initViewPage()
    {
            for (int i = 0; i < numOfPage; i++) {
                View rootView = View.inflate(InfoActivity.this, R.layout.viewpage, null);
                ImageView imageView = (ImageView) rootView.findViewById(R.id.viewpage_imageView);
                //imageView.setImageResource(imgIds[i]);
                imageView.setImageBitmap(bitmaps[i]);
                viewList.add(i, rootView);
            }
            myAdapter = new MyAdapter();
            viewPager.setAdapter(myAdapter);
            viewPager.setPageTransformer(true ,new SimplePageTransform());

            circleIndicator.setViewPager(viewPager);
    }

    private void setViewContent(Person person){
        //设置图像
        //Bitmap bitmap = BitmapFactory.decodeByteArray(person.getImageId(),0,person.getImageId().length);
        //imageView.setImageBitmap(bitmap);

        //设置名字
        if(person.getName().length()==3){
            textView_name.setTextSize(70);
        }
        else if(person.getName().length()==4) {
            textView_name.setTextSize(50);
        }
        else {
            textView_name.setTextSize(100);
        }
        textView_name.setText(person.getName());
        textView_birth_death.setText("生卒年月: " + person.getBirth() + "~" + person.getDeath());
        textView_sex.setText("性别: " + person.getSex());
        textView_nation.setText("所属势力: " + person.getNation());
        if(!"".equals(person.getNativePlace())){
            textView_brief.setText(person.getNativePlace()+"人，"+person.getBrief());
        }
        else{textView_brief.setText(person.getBrief());}

//        textView_sex.setVisibility(GONE);
//        textView_nation.setVisibility(GONE);
//        textView_birth_death.setVisibility(GONE);
//        textView_brief.setVisibility(GONE);
//        textView_name.setVisibility(VISIBLE);
//        textView_sentence.setVisibility(VISIBLE);
//        imageView.setVisibility(VISIBLE);

        setfonts();
    }

    private void setButton(){
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(InfoActivity.this,DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("personId", person.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        button.setVisibility(GONE);
    }

    public void setMove()
    {

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {
                    textView_sex.setVisibility(GONE);
                    textView_nation.setVisibility(GONE);
                    textView_birth_death.setVisibility(GONE);
                    textView_brief.setVisibility(GONE);
                    textView_name.setVisibility(VISIBLE);
                    textView_sentence.setVisibility(VISIBLE);
                    button.setVisibility(GONE);

                    animator_name.start();
                    animator_setence.start();
                    animator_nation.end();
                    animator_brief.end();
                    animator_sex.end();
                    animator_birth_death.end();

                    //setfonts();
                }
                else if(position==1)
                {
                    textView_sex.setVisibility(VISIBLE);
                    textView_nation.setVisibility(VISIBLE);
                    textView_birth_death.setVisibility(VISIBLE);
                    textView_brief.setVisibility(VISIBLE);
                    textView_name.setVisibility(GONE);
                    textView_sentence.setVisibility(GONE);
                    button.setVisibility(VISIBLE);

                    animator_name.end();
                    animator_setence.end();
                    animator_nation.start();
                    animator_brief.start();
                    animator_sex.start();
                    animator_birth_death.start();

                    //setfonts();
                }
//                else
//                {
//                    textView_sex.setVisibility(GONE);
//                    textView_nation.setVisibility(GONE);
//                    textView_birth_death.setVisibility(GONE);
//                    textView_brief.setVisibility(GONE);
//                    textView_name.setVisibility(GONE);
//                    textView_sentence.setVisibility(GONE);
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setViewContentByIntent(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int personId = bundle.getInt("personId");
        person = DataSupport.find(Person.class, personId);
        position = (int)bundle.getInt("position");
        //设置图片
        bitmaps[0] = BitmapFactory.decodeByteArray(person.getImageId(),0,person.getImageId().length);
        setViewContent(person);
        //
    }

    //
    private void setTextView_name()
    {
        textView_name.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this,DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("personId", person.getId());
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenMainThread(MessageEvent_person_position event) {
        //Toast.makeText(MainActivity.this,"inside_receive",Toast.LENGTH_SHORT).show();
        int personId = event.getPersonId();
        person = DataSupport.find(Person.class, personId);
        bitmaps[0] = BitmapFactory.decodeByteArray(person.getImageId(),0,person.getImageId().length);
        View rootView = View.inflate(InfoActivity.this, R.layout.viewpage, null);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.viewpage_imageView);
        imageView.setImageBitmap(bitmaps[0]);
        viewList.set(0, rootView);
        myAdapter.notifyDataSetChanged();

        setViewContent(person);
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
            //container.removeView(viewList.get(position));
            container.removeView((View)object);
        }


        @Override
        public int getItemPosition(Object object) {
            // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法
            return POSITION_NONE;
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
