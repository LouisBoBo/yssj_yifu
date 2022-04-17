package com.yssj.ui.fragment.contributions;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.app.AppManager;
import com.yssj.custom.view.FilterTitleView;
import com.yssj.data.YDBHelper;
import com.yssj.entity.ShopCart;
import com.yssj.entity.VipDikouData;
import com.yssj.entity.VipInfo;
import com.yssj.eventbus.MessageEvent;
import com.yssj.network.HttpListener;
import com.yssj.network.YConn;
import com.yssj.ui.activity.testfile.TestActivity;
import com.yssj.ui.activity.testfile.UpLoadUtil;
import com.yssj.ui.base.BasicActivity;
import com.yssj.ui.dialog.SelectPicDialog;
import com.yssj.ui.fragment.ItemClassficationFragment;
import com.yssj.utils.ImageGetFromHttp;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class CommitContributionsActivity extends BasicActivity implements View.OnClickListener {

    public static List<ShopCart> listClick = new ArrayList<ShopCart>();
    public static List<Uri> uriList = new ArrayList<>();
    public static CommitContributionsActivity instance;
    private Context context;
    private View img_back;
    private View img_example;

    private View add_img1;
    private View add_img2;
    private View add_img3;
    private View add_img4;
    private View add_img5;
    private View add_img6;
    private View pub_img;
    private TextView sub_tv;
    private View zhedie_view;
    private View type_view;
    private View size_view;
    private TextView type_content;
    private TextView size_content;
    private EditText edit_tips;
    private FlexboxLayout flexboxLayout_type;
    private FlexboxLayout flexboxLayout_size;
    private List<TextView> type_textViews = new ArrayList<>();
    private List<TextView> size_textViews = new ArrayList<>();
    private List<ImageView> show_imagViews = new ArrayList<>();
    private List<ImageView> del_imagViews = new ArrayList<>();
    private ImageView show_image1;
    private ImageView show_image2;
    private ImageView show_image3;
    private ImageView show_image4;
    private ImageView show_image5;
    private ImageView show_image6;

    private ImageView delimg1;
    private ImageView delimg2;
    private ImageView delimg3;
    private ImageView delimg4;
    private ImageView delimg5;
    private ImageView delimg6;
    private int type_id;//分类id

    private SelectPicDialog selectPicDialog;
    private int select_image_index =1;
    private int select_image_id=-1;
    private int contribution_status = 999;

//    private List<Integer> image_ids = new ArrayList<>();
    Integer[] image_ids = new Integer[6];
    Integer[] type_ids = new Integer[6];

    public static final int TAKE_CAMERA_PERMISSION_REQUEST_CODE = 100;
    public static final int TAKE_GALLERY_PERMISSION_REQUEST_CODE = 101;
    public static final int TAKE_CAMERA_PIC_FILE_REQUEST_CODE = 200;
    public static final int TAKE_GALLERY_PIC_FILE_REQUEST_CODE = 201;

    private static int RESULT_LOAD_IMAGE = 3;
    private static int RESULT_LOAD_PICTURE = 4;
    private static final int RESULT_OK = -1;

    private Uri uriImageData;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent message){
        if(message.getMessage().equals("审核返回")){
            onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SelectType message){
        type_id = message.type_id;
        if(message.type == 1){
            type_content.setText(message.name);
        }else {
            size_content.setText(message.name);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        instance = this;
        listClick.clear();
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.commit_contributions);

        initView();
        initData();
    }

    private void initData(){
        image_ids[0] = 0;
        image_ids[1] = 0;
        image_ids[2] = 0;
        image_ids[3] = 0;
        image_ids[4] = 0;
        image_ids[5] = 0;

        type_ids[0] = 1;
        type_ids[1] = 2;
        type_ids[2] = 3;
        type_ids[3] = 4;
        type_ids[4] = 5;
        type_ids[5] = 6;
    }

    private void initView() {
        instance = this;
        EventBus.getDefault().register(this);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            contribution_status = bundle.getInt("contribution_status");
        }

        add_img1 = findViewById(R.id.add_image1);
        add_img2 = findViewById(R.id.add_image2);
        add_img3 = findViewById(R.id.add_image3);
        add_img4 = findViewById(R.id.add_image4);
        add_img5 = findViewById(R.id.add_image5);
        add_img6 = findViewById(R.id.add_image6);

        show_image1 = add_img1.findViewById(R.id.show_img);
        show_image2 = add_img2.findViewById(R.id.show_img);
        show_image3 = add_img3.findViewById(R.id.show_img);
        show_image4 = add_img4.findViewById(R.id.show_img);
        show_image5 = add_img5.findViewById(R.id.show_img);
        show_image6 = add_img6.findViewById(R.id.show_img);



        show_imagViews.add(show_image1);
        show_imagViews.add(show_image2);
        show_imagViews.add(show_image3);
        show_imagViews.add(show_image4);
        show_imagViews.add(show_image5);
        show_imagViews.add(show_image6);

        TextView addmark1 = add_img1.findViewById(R.id.add_mark);
        addmark1.setText("正面上传");

        TextView addmark2 = add_img2.findViewById(R.id.add_mark);
        addmark2.setText("背面上传");

        TextView addmark3 = add_img3.findViewById(R.id.add_mark);
        addmark3.setText("上传面料");

        TextView addmark4 = add_img4.findViewById(R.id.add_mark);
        addmark4.setText("上传面料");

        TextView addmark5 = add_img5.findViewById(R.id.add_mark);
        addmark5.setText("上传辅料");

        TextView addmark6 = add_img6.findViewById(R.id.add_mark);
        addmark6.setText("上传辅料");

        delimg1 = add_img1.findViewById(R.id.del_mark);
        delimg2 = add_img2.findViewById(R.id.del_mark);
        delimg3 = add_img3.findViewById(R.id.del_mark);
        delimg4 = add_img4.findViewById(R.id.del_mark);
        delimg5 = add_img5.findViewById(R.id.del_mark);
        delimg6 = add_img6.findViewById(R.id.del_mark);

        del_imagViews.add(delimg1);
        del_imagViews.add(delimg2);
        del_imagViews.add(delimg3);
        del_imagViews.add(delimg4);
        del_imagViews.add(delimg5);
        del_imagViews.add(delimg6);

        img_back = findViewById(R.id.img_back);
        img_example = findViewById(R.id.explain_limit);
        sub_tv = findViewById(R.id.submit);
        zhedie_view = findViewById(R.id.zhedie_base);
        type_view = findViewById(R.id.type_base);
        size_view = findViewById(R.id.size_base);
        type_content = findViewById(R.id.type_content);
        size_content = findViewById(R.id.size_content);
        edit_tips = findViewById(R.id.tips);

        img_back.setOnClickListener(this);
        sub_tv.setOnClickListener(this);
        img_example.setOnClickListener(this);
        add_img1.setOnClickListener(this);
        add_img2.setOnClickListener(this);
        add_img3.setOnClickListener(this);
        add_img4.setOnClickListener(this);
        add_img5.setOnClickListener(this);
        add_img6.setOnClickListener(this);
        zhedie_view.setOnClickListener(this);
        type_view.setOnClickListener(this);
        size_view.setOnClickListener(this);

        delimg1.setOnClickListener(this);
        delimg2.setOnClickListener(this);
        delimg3.setOnClickListener(this);
        delimg4.setOnClickListener(this);
        delimg5.setOnClickListener(this);
        delimg6.setOnClickListener(this);

        if(contribution_status == 3){//如果是3显示用户已申请的供款信息
            initContributionStatusData();
        }
    }

    //获取供款状态-供款信息
    public void initContributionStatusData(){

        HashMap<String, String> pairsMap = new HashMap<>();

        YConn.httpPost(this, YUrl.CLOUD_API_WAR_SUPPLYMATERIAL_FINDSUPPLY, pairsMap, new HttpListener<ContributionStatusBean>() {
            @Override
            public void onSuccess(ContributionStatusBean result) {

                if(result.getData() != null){

                    type_content.setText(result.getData().getShop_specification());
                    size_content.setText(result.getData().getShop_size());

                    int i =0;
                    for (ContributionStatusBean.DataDTO.SupplyMaterialImageEntitysDTO supplyMaterialImageEntity : result.getData().getSupplyMaterialImageEntitys()) {
                        image_ids[i] = supplyMaterialImageEntity.getId();
                        String imageurl = YUrl.imgurl + supplyMaterialImageEntity.getReal_path();

                        ImageView show_image = show_imagViews.get(i);
                        Glide.with(context).load(imageurl).into(show_image);
                        show_image.setVisibility(View.VISIBLE);

                        ImageView del_img = del_imagViews.get(i);
                        del_img.setVisibility(View.VISIBLE);

                        i++;
                    }
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 动态创建TextView
     * @param book
     * @return
     */
    private TextView createNewFlexItemTextView(final Book book, final int type) {
        final TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText(book.getName());
        textView.setTextSize(12);
        textView.setTextColor(getResources().getColor(R.color.light_gray));
        textView.setBackgroundResource(R.drawable.bg_choice_btn_default);
        textView.setTag(book.getId());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("sdafjsf", book.getName());

                List<TextView> textViews = type == 1?type_textViews:size_textViews;
                for (TextView textView1 : textViews) {
                    textView1.setTextColor(getResources().getColor(R.color.light_gray));
                    textView1.setBackgroundResource(R.drawable.bg_choice_btn_default);
                }

                TextView vv = (TextView)view;
                vv.setTextColor(getResources().getColor(R.color.white));
                vv.setBackgroundResource(R.drawable.bg_choice_btn_checked);
            }
        });
        int padding = TagUtil.dpToPixel(context, 4);
        int paddingLeftAndRight = TagUtil.dpToPixel(context, 8);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, padding, paddingLeftAndRight, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = TagUtil.dpToPixel(context, 6);
        int marginTop = TagUtil.dpToPixel(context, 0);
        layoutParams.setMargins(margin, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);

        if(type ==1){
            type_textViews.add(textView);
        }else {
            size_textViews.add(textView);
        }

        return textView;
    }

    public void onClick(View v) {
        super.onClick(v);
        if(v == delimg1){
            image_ids[0] = 0;
            delimg1.setVisibility(View.GONE);
            show_image1.setVisibility(View.GONE);
        }else if(v == delimg2){
            image_ids[1] = 0;
            delimg2.setVisibility(View.GONE);
            show_image2.setVisibility(View.GONE);
        }else if(v == delimg3){
            image_ids[2] = 0;
            delimg3.setVisibility(View.GONE);
            show_image3.setVisibility(View.GONE);
        }else if(v == delimg4){
            image_ids[3] = 0;
            delimg4.setVisibility(View.GONE);
            show_image4.setVisibility(View.GONE);
        }else if(v == delimg5){
            image_ids[4] = 0;
            delimg5.setVisibility(View.GONE);
            show_image5.setVisibility(View.GONE);
        }else if(v == delimg6){
            image_ids[5] = 0;
            delimg6.setVisibility(View.GONE);
            show_image6.setVisibility(View.GONE);
        }else {
            switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.submit:
                submitData();
                break;
            case R.id.add_image1:
                select_image_index =1;
                selectPicDialog(v);
                break;
            case R.id.add_image2:
                select_image_index =2;
                selectPicDialog(v);
                break;
            case R.id.add_image3:
                select_image_index =3;
                selectPicDialog(v);
                break;
            case R.id.add_image4:
                select_image_index =4;
                selectPicDialog(v);
                break;
            case R.id.add_image5:
                select_image_index =5;
                selectPicDialog(v);
                break;
            case R.id.add_image6:
                select_image_index =6;
                selectPicDialog(v);
                break;
            case R.id.explain_limit:
                Intent intent = new Intent(context, ScanEeamplesActivity.class);
                startActivity(intent);
                break;
            case R.id.zhedie_base:
                zhedie_view.setSelected(!zhedie_view.isSelected());
                type_view.setVisibility(zhedie_view.isSelected()?View.INVISIBLE:View.VISIBLE);
                size_view.setVisibility(zhedie_view.isSelected()?View.INVISIBLE:View.VISIBLE);
                break;
            case R.id.type_base:
                Intent intent1 = new Intent(context, ContributionClassActivity.class);
                Bundle bundleSimple1 = new Bundle();
                bundleSimple1.putString("type", "1");
                intent1.putExtras(bundleSimple1);

                startActivity(intent1);
                break;
            case R.id.size_base:
                Intent intent2 = new Intent(context, ContributionClassActivity.class);
                Bundle bundleSimple2 = new Bundle();
                bundleSimple2.putString("type", "2");
                intent2.putExtras(bundleSimple2);
                startActivity(intent2);
                break;
            default:
                break;
        }
        }

    }

    //数据去重
    public static List<Uri> removeDuplicate(List<Uri> list)

    {
        Set set = new LinkedHashSet<String>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public void submitData(){

        String imgaeids = "";
        for(int i =0;i<image_ids.length;i++)
        {
            if(image_ids[i] != 0)
            {
                imgaeids += image_ids[i];
                if(i != image_ids.length-1){
                    imgaeids += ",";
                }
            }else {
                String tips = "";
                switch (i){
                    case 0:
                        tips = "请上传正面图片";
                        break;
                    case 1:
                        tips = "请上传背面图片";
                        break;
                    case 2:
                        tips = "请上传面料图片";
                        break;
                    case 3:
                        tips = "请上传面料图片";
                        break;
                    case 4:
                        tips = "请上传辅料图片";
                        break;
                    case 5:
                        tips = "请上传辅料图片";
                        break;
                }
                ToastUtil.showShortText2(tips);
                return;
            }
        }

        if(type_content.getText().equals("请选择分类")){
            ToastUtil.showShortText2("请选择分类");
            return;
        }

        if(size_content.getText().equals("请选择尺码")){
            ToastUtil.showShortText2("请选择尺码");
            return;
        }


        HashMap<String, String> pairsMap = new HashMap<>();
        //appVersion=V3.8.6 &channel=18&version=V1.32&app_id=wx8c5fe3e40669c535&authKey=E8B4CCB44B0A0E45BD988 F7A9BF4F8EB&I10o=HJrGDODNMtM0MtLLXOU0DUTOEJq4HtdLEUTQDOY4HUS=&token=EWHH10 9ELNAXIYKQ7HQ0&supplyMaterialImages=1,2,3&shop_specification=裙子-半身裙
        pairsMap.put("supplyMaterialImages",imgaeids);
        pairsMap.put("shop_specification",type_content.getText().toString());
        pairsMap.put("shop_size",size_content.getText().toString());
        if(edit_tips.getText().toString().length()>0){
            pairsMap.put("remark",edit_tips.getText().toString());
        }


        String url = contribution_status==3?YUrl.CLOUD_API_WAR_SUPPLYMATERIAL_UPDATESUPPLY:YUrl.CLOUD_API_WAR_SUPPLYMATERIAL_SUPPLY;
        //提交供款
        YConn.httpPost(context, url, pairsMap, new HttpListener<VipDikouData>() {
            @Override
            public void onSuccess(VipDikouData result) {

                if(contribution_status == 3) {
                    ToastUtil.showShortText2("修改成功");
                }else {
                    Intent intent = new Intent(context, ContributionStatusActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onError() {

            }
        });

    }

    public void selectPicDialog(View v){
//        pub_img = (ImageView) v;
        selectPicDialog = new SelectPicDialog(context, new SelectPicDialog.OnSelectPicDialogBtnClickListener() {
            @Override
            public void click(int btnId) {
                if (btnId == R.id.iv_camera) {
                    takePicFromCamera();
                    selectPicDialog.dismiss();
                } else if (btnId == R.id.iv_gallery) {
                    takePicFromGallery();
                    selectPicDialog.dismiss();
                }
            }
        });

        selectPicDialog.show();
    }
    private void takePicFromCamera() {
        //相机的话 相机权限和存储权限都需要
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                )
        ) {
            String[] permissions = new String[2];
            permissions[0] = Manifest.permission.CAMERA;
            permissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(permissions, TAKE_CAMERA_PERMISSION_REQUEST_CODE);
            }
            return;
        }
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_CAMERA_PIC_FILE_REQUEST_CODE);

    }

    private void takePicFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {

            String[] permissions = new String[2];
            permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(permissions, TAKE_GALLERY_PERMISSION_REQUEST_CODE);
            }
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ArrayList<String> mimeTypes = new ArrayList();
            mimeTypes.add("image/jpeg");
            mimeTypes.add("image/png");
            mimeTypes.add("image/jpg");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "选择图片"
                ), TAKE_GALLERY_PIC_FILE_REQUEST_CODE
        );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_CAMERA_PIC_FILE_REQUEST_CODE:
//                    final Uri uriImageData;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

//                    pub_img.setImageBitmap(bitmap);

                    for(int i=0;i<show_imagViews.size();i++)
                    {
                        if(i+1==select_image_index){
                            ImageView show_image = show_imagViews.get(i);
                            show_image.setImageBitmap(bitmap);
                            show_image.setVisibility(View.VISIBLE);

                            ImageView del_image = del_imagViews.get(i);
                            del_image.setVisibility(View.VISIBLE);
                        }
                    }
//                    switch (select_image_index){
//                        case 1:
//                            show_image1.setImageBitmap(bitmap);
//                            show_image1.setVisibility(View.VISIBLE);
//                            break;
//                        case 2:
//                            show_image2.setImageBitmap(bitmap);
//                            show_image2.setVisibility(View.VISIBLE);
//                            break;
//                        case 3:
//                            show_image3.setImageBitmap(bitmap);
//                            show_image3.setVisibility(View.VISIBLE);
//                            break;
//                        case 4:
//                            show_image4.setImageBitmap(bitmap);
//                            show_image4.setVisibility(View.VISIBLE);
//                            break;
//                        case 5:
//                            show_image5.setImageBitmap(bitmap);
//                            show_image5.setVisibility(View.VISIBLE);
//                            break;
//                        case 6:
//                            show_image6.setImageBitmap(bitmap);
//                            show_image6.setVisibility(View.VISIBLE);
//                            break;
//                    }
                    if (null != data.getData()) {
                        uriImageData = data.getData();
                    } else {
                        uriImageData = Uri.parse(

                                MediaStore.Images.Media.insertImage(
                                        context.getContentResolver(),
                                        bitmap,
                                        null,
                                        null
                                )
                        );
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                UpLoadPic(uriImageData);
                                uriList.add(uriImageData);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                    break;
                case TAKE_GALLERY_PIC_FILE_REQUEST_CODE:
                    final Uri gallerySelectUrl = data.getData();
//                    pub_img.setImageURI(gallerySelectUrl);

                    for(int i=0;i<show_imagViews.size();i++)
                    {
                        if(i+1==select_image_index){
                            ImageView show_image = show_imagViews.get(i);
                            show_image.setImageURI(gallerySelectUrl);
                            show_image.setVisibility(View.VISIBLE);

                            ImageView del_image = del_imagViews.get(i);
                            del_image.setVisibility(View.VISIBLE);
                        }
                    }

//                    switch (select_image_index){
//                        case 1:
//                            show_image1.setImageURI(gallerySelectUrl);
//                            show_image1.setVisibility(View.VISIBLE);
//                            break;
//                        case 2:
//                            show_image2.setImageURI(gallerySelectUrl);
//                            show_image2.setVisibility(View.VISIBLE);
//                            break;
//                        case 3:
//                            show_image3.setImageURI(gallerySelectUrl);
//                            show_image3.setVisibility(View.VISIBLE);
//                            break;
//                        case 4:
//                            show_image4.setImageURI(gallerySelectUrl);
//                            show_image4.setVisibility(View.VISIBLE);
//                            break;
//                        case 5:
//                            show_image5.setImageURI(gallerySelectUrl);
//                            show_image5.setVisibility(View.VISIBLE);
//                            break;
//                        case 6:
//                            show_image6.setImageURI(gallerySelectUrl);
//                            show_image6.setVisibility(View.VISIBLE);
//                            break;
//                    }


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                UpLoadPic(gallerySelectUrl);
                                uriList.add(gallerySelectUrl);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    break;
                default:


                    break;
            }
        }
    }

    //上传图片
    private void UpLoadPic(Uri gallerySelectUrl) throws IOException {

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = this.managedQuery(gallerySelectUrl, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        int material_type = type_ids[select_image_index-1];

        UpLoadUtil.uploadImage(context,material_type,img_path, new UpLoadUtil.UpLoadImgListener() {
            @Override
            public void upLoadSuccess(int imgId) {
                select_image_id = imgId;
                Log.i("sfakfa", "upLoadSuccess: ");
//                ToastUtil.showShortText2("上传成功");
                image_ids[select_image_index-1] = select_image_id;
            }

            @Override
            public void upLoadFail() {
                Log.i("fajfaj", "upLoadFail: ");
//                ToastUtil.showShortText2("上传失败");
            }
        });
    }

    //收到消息回主UI刷新界面
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 123:
//                    image_ids[select_image_index-1] = select_image_id;
                    try {
                        UpLoadPic(uriImageData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uriList.add(uriImageData);
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case TAKE_CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePicFromCamera();
                    return;
                }
                //拒绝了
//                ToastUtils.showNormalToast(getActivity(),"请点权限，并允许授权相机和存储权限。");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent i = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        String pkg = "com.android.settings";
                        String cls = "com.android.settings.applications.InstalledAppDetails";
                        i.setComponent(new ComponentName(pkg, cls));
                        i.setData(Uri.parse("package:" + context.getPackageName()));
                        startActivity(i);
                    }
                }, 1000);
                break;
            case TAKE_GALLERY_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicFromGallery();
                    return;
                }
                //拒绝了
//                ToastUtils.showNormalToast(getActivity(),"请点权限，并允许授权存储权限。");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent i = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        String pkg = "com.android.settings";
                        String cls = "com.android.settings.applications.InstalledAppDetails";
                        i.setComponent(new ComponentName(pkg, cls));
                        i.setData(Uri.parse("package:" + context.getPackageName()));
                        startActivity(i);
                    }
                }, 1000);
                break;
        }
    }

    //图片上传
    public void UploadImgData(String src){

        HashMap<String, String> pairsMap = new HashMap<>();

        List<HashMap> maps = new ArrayList<>();
        HashMap fmap = new HashMap();
        fmap.put("key","filedata");
        fmap.put("type","file");
        fmap.put("src",src);
        maps.add(fmap);


        HashMap map = new HashMap();
        map.put("mode","formdata");
        map.put("formdata",maps);

        pairsMap.put("body", map.toString());

        YConn.httpPost(context, YUrl.CLOUD_API_WAR_SUPPLYMATERIAL_ADDMULTIPLE, pairsMap, new HttpListener<UpdatePhotoBean>() {
            @Override
            public void onSuccess(UpdatePhotoBean result) {

                ToastUtil.showShortText2("上传成功");
            }

            @Override
            public void onError() {
                ToastUtil.showShortText2("上传失败");
            }
        });

    }
}
