package com.mycompany.project_mid;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class EditActivity extends AppCompatActivity {
    private Person person;
    private int position;
    private String acty_acty;

    private ImageView imageView;
    private EditText editText_name ;
    private EditText editText_birth ;
    private EditText editText_death ;
    private EditText editText_brief ;
    private EditText editText_nativePlace;
    private RadioGroup radioGroup_sex;
    private RadioButton radioButton_sex;
    private RadioGroup radioGroup_nation;
    private RadioButton radioButton_nation;
    private TextInputLayout textInputLayout;
    private SeekBar seekBar1, seekBar2, seekBar3;
    private TextView value_SB1,value_SB2,value_SB3;

    private  Bitmap inibitmap;

    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Window window = getWindow();
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        imageView   = findViewById(R.id.add_new_imageView);
        editText_name = findViewById(R.id.add_new_name);
        editText_birth = findViewById(R.id.add_new_birth_editText);
        editText_death = findViewById(R.id.add_new_death_editText);
        editText_brief = findViewById(R.id.add_new_other_editText);
        editText_nativePlace = findViewById(R.id.add_new_nativePlace);
        textInputLayout = findViewById(R.id.add_new_nameWrapper);
        radioGroup_sex = findViewById(R.id.add_new_radioGroup_sex);
        radioGroup_nation = findViewById(R.id.add_new_radioGroup_nation);

        inibitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.zhaoyun);

        setImageView();
        setSeekBar();
        setViewContent();
    }

    //创建actionbar按钮菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.actionbar_menu_infoact, menu);
        MenuItem menuItem = menu.findItem(R.id.action_edit);
        menuItem.setIcon(R.drawable.selected);
        menuItem.setTitle("完成");

        return true;
    }

    //为actionbar按钮设置点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            if(!getViewContent()){
                textInputLayout.setError("姓名不能为空");
            }
            else{
                if(acty_acty.equals("Main_Edit")) {
                    person.save();
                    finish();
                }
                else if(acty_acty.equals("Info_Edit")) {
                    person.update(person.getId());
                    EventBus.getDefault().post(new MessageEvent_person_position(person.getId(),position));
                    finish();
                }
            }
        }
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void setImageView(){
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] choices = {"拍摄","从相册中选择"};
                final AlertDialog alertDialog = new AlertDialog.Builder(EditActivity.this).
                        setItems(choices, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which){
                                switch (which){
                                    case 0:
                                        //拍照选择
                                        chooseFromCamera();
                                        break;
                                    case 1:
                                        //从相册选取
                                        chooseFromGallery();
                                        break;
                                    default:
                                        break;
                                }

                            }
                        }).create();
                alertDialog.setTitle("设置图片");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface,int which){
                        //alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                return false;
            }
        });
    }

    private void setSeekBar(){
        seekBar1 = (SeekBar) findViewById(R.id.add_new_seekBar_Wen);
        seekBar2 = (SeekBar) findViewById(R.id.add_new_seekBar_Wu);
        seekBar3 = (SeekBar) findViewById(R.id.add_new_seekBar_Yun);
        value_SB1 = (TextView) findViewById(R.id.value_Wen);
        value_SB2 = (TextView) findViewById(R.id.value_Wu);
        value_SB3 = (TextView) findViewById(R.id.value_Yun);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                value_SB1.setText(String.valueOf(progress));
            }
            // 开始拖动
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}
            // 停止拖动
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                value_SB2.setText(String.valueOf(progress));
            }
            // 开始拖动
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}
            // 停止拖动
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                value_SB3.setText(String.valueOf(progress));
            }
            // 开始拖动
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}
            // 停止拖动
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    //完成设置时调用 设置person
    private  boolean getViewContent(){
        radioGroup_sex = findViewById(R.id.add_new_radioGroup_sex);
        radioButton_sex = findViewById(radioGroup_sex.getCheckedRadioButtonId());
        radioGroup_nation = findViewById(R.id.add_new_radioGroup_nation);
        radioButton_nation = findViewById(radioGroup_nation.getCheckedRadioButtonId());

        String name = editText_name.getText().toString();
        if("".equals(name))return false;
        String birth = editText_birth.getText().toString();
        String death = editText_death.getText().toString();
        String brief = editText_brief.getText().toString();
        String sex = radioButton_sex.getText().toString();
        String nation = radioButton_nation.getText().toString();
        String nativePlace = editText_nativePlace.getText().toString();
        int wisdom = seekBar1.getProgress();
        int force = seekBar2.getProgress();
        int luck = seekBar3.getProgress();

        switch (acty_acty){
            case "Info_Edit":
                person.setAll(person.getId(),person.getImageId(),name,birth,death,brief,sex,nation,nativePlace,"",wisdom,force,luck);
                break;
            case "Main_Edit":
                Bundle bundle =getIntent().getExtras();
                int listLen  = bundle.getInt("listLen");
                person.setAll(listLen,person.getImageId(),name, birth, death, brief, sex, nation, nativePlace,"",wisdom,force,luck);
                break;
            default:break;
        }
        return true;

    }

    //跳转到编辑界面，设置控件内容
    private void setViewContent() {
        Bundle bundle = getIntent().getExtras();
        acty_acty = bundle.getString("change");
        if ("Info_Edit".equals(acty_acty)) {
            int personId = bundle.getInt("personId");
            person = DataSupport.find(Person.class, personId);
            position = bundle.getInt("position");
            Bitmap bitmap = BitmapFactory.decodeByteArray(person.getImageId(),0,person.getImageId().length);
            imageView.setImageBitmap(bitmap);
            editText_name.setText(person.getName());
            editText_birth.setText(person.getBirth());
            editText_death.setText(person.getDeath());
            editText_brief.setText(person.getBrief());
            editText_nativePlace.setText(person.getNativePlace());
            seekBar1.setProgress(person.getWisdom());
            seekBar2.setProgress(person.getForce());
            seekBar3.setProgress(person.getLuck());

            //radioGroup_sex.clearCheck();
            //radioGroup_nation.clearCheck();
            RadioButton radioButton;
            switch (person.getSex()){
                case "男":
                    radioButton = findViewById(R.id.add_new_radioButtion_boy);
                    radioButton.setChecked(true);
                    break;
                case "女":
                    radioButton = findViewById(R.id.add_new_radioButtion_girl);
                    radioButton.setChecked(true);
                    break;
                default:break;
            }

           // radioGroup_nation.clearCheck();
            switch (person.getNation()){
                case "魏":
                    radioButton = findViewById(R.id.add_new_radioButtion_Wei);
                    radioButton.setChecked(true);
                    break;
                case "蜀":
                    radioButton = findViewById(R.id.add_new_radioButtion_Shu);
                    radioButton.setChecked(true);
                    break;
                case "吴":
                    radioButton = findViewById(R.id.add_new_radioButtion_Wu);
                    radioButton.setChecked(true);
                    break;
                case "其他":
                    radioButton = findViewById(R.id.add_new_RadioButton_QiTa);
                    radioButton.setChecked(true);
                    break;
                default:break;
            }
        }
        else if("Main_Edit".equals(acty_acty)){
            person = new Person();
            person.setImageId(MainActivity.bytes_inibitmap);
            imageView.setImageBitmap(inibitmap);
        }
    }

    /**
     * 拍照选择图片
     */
    private void chooseFromCamera() {
        //构建隐式Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //调用系统相机
        startActivityForResult(intent, CAMERA_CODE);
    }

    /**
     * 从相册选择图片
     */
    private void chooseFromGallery() {
        //构建一个内容选择的Intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //设置选择类型为图片类型
        intent.setType("image/*");
        //打开图片选择
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CAMERA_CODE:
                //用户点击了取消
                if(data == null){
                    return;
                }else{
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        //获得拍的照片
                        Bitmap bm = extras.getParcelable("data");
                        //将Bitmap转化为uri
                        Uri uri = saveBitmap(bm, "temp");
                        //启动图像裁剪
                        startImageZoom(uri);
                    }
                }
                break;
            case GALLERY_CODE:
                if (data == null){
                    return;
                }else{
                    //用户从图库选择图片后会返回所选图片的Uri
                    Uri uri;
                    //获取到用户所选图片的Uri
                    uri = data.getData();
                    //返回的Uri为content类型的Uri,不能进行复制等操作,需要转换为文件Uri
                    uri = convertUri(uri);
                    startImageZoom(uri);
                }
                break;
            case CROP_CODE:
                if (data == null){
                    return;
                }else{
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        //获取到裁剪后的图像
                        Bitmap bm = extras.getParcelable("data");
                        imageView.setImageBitmap(bm);
                        person.setImageId(BitmapMethod.ConvertBitmaptoBytes(bm));
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 将content类型的Uri转化为文件类型的Uri
     * @param uri
     * @return
     */
    private Uri convertUri(Uri uri){
        InputStream is;
        try {
            //Uri ----> InputStream
            is = getContentResolver().openInputStream(uri);
            //InputStream ----> Bitmap
            Bitmap bm = BitmapFactory.decodeStream(is);
            //关闭流
            is.close();
            return saveBitmap(bm, "temp");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将Bitmap写入SD卡中的一个文件中,并返回写入文件的Uri
     * @param bm
     * @param dirPath
     * @return
     */
    private Uri saveBitmap(Bitmap bm, String dirPath) {
        //新建文件夹用于存放裁剪后的图片
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/" + dirPath);
        if (!tmpDir.exists()){
            tmpDir.mkdir();
        }

        //新建文件存储裁剪后的图片
        File img = new File(tmpDir.getAbsolutePath() + "/avator.png");
        try {
            //打开文件输出流
            FileOutputStream fos = new FileOutputStream(img);
            //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            //刷新输出流
            fos.flush();
            //关闭输出流
            fos.close();
            //返回File类型的Uri
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过Uri传递图像信息以供裁剪
     * @param uri
     */
    private void startImageZoom(Uri uri){
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 125);
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_CODE);
    }





}
