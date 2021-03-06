package com.yssj.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yssj.YConstance;
import com.yssj.YConstance.Pref;
import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.app.AppManager;
import com.yssj.app.SAsyncTask;
import com.yssj.data.YDBHelper;
import com.yssj.entity.ReturnInfo;
import com.yssj.entity.SwitchData;
import com.yssj.model.ComModel;
import com.yssj.model.ComModel2;
import com.yssj.model.ComModelL;
import com.yssj.network.HttpListener;
import com.yssj.network.YConn;
import com.yssj.ui.activity.setting.UserProtocolActivity;
import com.yssj.ui.base.BasicGuideActivity;
import com.yssj.utils.CheckStrUtil;
import com.yssj.utils.CheckVersionUtils;
import com.yssj.utils.CommonUtils;
import com.yssj.utils.DeviceUtils;
import com.yssj.utils.LogYiFu;
import com.yssj.utils.PicassoUtils;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.StringUtils;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.YCache;
import com.yssj.utils.YunYingTongJi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/***
 * ?????????
 *
 * @author Administrator
 *
 */
public class GuideActivity extends BasicGuideActivity implements OnPageChangeListener, OnClickListener {
    private RelativeLayout root;
    private String sortDateStr;// ???????????????
    private String attrDateStr;// ???????????????
    private String tagDateStr;// ???????????????
    private String busTagDateStr;// ?????????????????????
    private String typeTagDateStr;// ???????????????????????????
    private String suppLabelDataStr;// ???????????????
    private String friendCircleTagDataStr;// ??????????????????????????????

//    public static int currentChannel = 9;

    //    private boolean isFirst = true;
//    private static final int[] pics = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

    private List<View> views;
    private ViewPager vp;
    private PagerAdapter vpAdapter;
    public static boolean needShouquan = false;//??????????????????
    private boolean isCheckLast = false, isSyncFinish = false;
    private ImageView mIvLogo;
    // private ImageView[] dots;
    private int currentIndex;

    // private LinearLayout ll;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");

    private int addNUM;
    private ImageView mAddPic;
    private TextView mNextStep;

    public static boolean hasSign = true;

    //????????????
    public static boolean show1yuan = true;
    public static String app_every = "2";//1?????????????????????????????????


    public static String oneShopPrice = "1.0";
//    public static String oneShopPrice = "9.9";

    public static long startUserTime; // APP??????????????????
    // // ??????????????????
    //
    // public static String APP_ID;
    // public static String MCH_ID;
    // public static String API_KEY;
    // public static String APP_SECRET;

    private boolean isGuide = false;

    public static GuideActivity instance;

    private int sycDataNum = 0;

    private boolean isAready = false;

    private boolean isLogined;

    public static HashMap<String, HashMap<String, Object>> textMap = new HashMap<String, HashMap<String, Object>>();//????????????
    public static SwitchData switchData;

    public static boolean needFengKong = false;
    public static boolean channelFengkong = true;
    public static boolean needCheckClipboard = true;//???????????????????????????


//    public static boolean


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (!isTaskRoot()
                && intent != null
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.getAction() != null
                && intent.getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        //???????????????
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //??????
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_guide);
        AppManager.getAppManager().addActivity(this);

        instance = this;
//        try {
//            currentChannel = Integer.parseInt(DeviceUtils.getChannelCode(this));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //TODO:_MODIFY_????????????app????????????
        if ("1".equals(DeviceUtils.getChannelCode(this))) {//1?????????????????????????????????
            if (!SharedPreferencesUtil.getBooleanData(this, YCache.START_APP_YSQX, false)) {
                showYSQX();
            } else {
                initStartAPPdata();

            }
        } else {
            initStartAPPdata();

        }


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//                //?????????
//                startActivity(new Intent(GuideActivity.this, MainMenuActivity.class).putExtra("fromGuide",true));
//                // }
//                GuideActivity.this.finish();
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
//
//        }, 3000);//3????????????Runnable??????run??????


    }

    private void showYSQX() {

        final Dialog mDialog = new Dialog(instance, R.style.DialogQuheijiao2);
        View view = View.inflate(instance, R.layout.dialog_start_app_ysqx, null);

        TextView tv_bot = view.findViewById(R.id.tv_bot);

        view.findViewById(R.id.tv_yhxy).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(instance, UserProtocolActivity.class));
            }
        });
        view.findViewById(R.id.tv_ysqx).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(instance, UserProtocolActivity.class)
                        .putExtra("isYSQX", true)

                );

            }
        });

        view.findViewById(R.id.bt1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                finish();
                AppManager.getAppManager().finishAllActivity();
            }
        });
        view.findViewById(R.id.bt2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                SharedPreferencesUtil.saveBooleanData(instance, YCache.START_APP_YSQX, true);
                initStartAPPdata();

            }
        });

        tv_bot.setText("?????????\"?????????????????????????????????????????????,????????????????????????????????????????????????");
        // // ?????????????????????dialog
        mDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mDialog.setCancelable(false);
        mDialog.show();

    }

    private void initStartAPPdata() {
        //???????????????
        CheckVersionUtils.checkVersionAppStart(this, new CheckVersionUtils.OnNoUpdateListener() {
            @Override
            public void cancel() {
                initStartAPPdata2();

            }
        });

    }

    private void initStartAPPdata2() {
        getImageRadio();// ?????????????????????


        isLogined = getSharedPreferences(Pref.isLoginFlag, Context.MODE_PRIVATE).getBoolean(Pref.isLoginFlag,
                false);
        LogYiFu.e("????????????", "??????---" + isLogined);
        startUserTime = System.currentTimeMillis();

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        LogYiFu.e("hour", hour + "");

        SharedPreferencesUtil.saveBooleanData(instance, Pref.ISSHOWSTORE, true);

        YunYingTongJi.yunYingTongJi(this, 1); // ????????????APP??????

        Boolean sp = SharedPreferencesUtil.getBooleanData(getApplicationContext(), "isLoginLogin", false);
        LogYiFu.e("sp", sp + "");
        if (sp) {
            appStartCount(); // ??????????????? ??????APP????????????
        }



        YDBHelper helper = new YDBHelper(this);
        helper.query("select count(*) from sort_info");
        // aBar.hide();
        root = (RelativeLayout) findViewById(R.id.root);
        mAddPic = (ImageView) findViewById(R.id.new_add_pic);
        mNextStep = (TextView) findViewById(R.id.next_step);
        mIvLogo = (ImageView) findViewById(R.id.iv_logo);
        sortDateStr = getSharedPreferences(Pref.sync, Context.MODE_PRIVATE).getString(Pref.sync_sort_date,
                "8813A493A311F4D640B8184000F4B49F");
        attrDateStr = getSharedPreferences(Pref.sync, Context.MODE_PRIVATE).getString(Pref.sync_attr_date, "1");
        tagDateStr = getSharedPreferences(Pref.sync, Context.MODE_PRIVATE).getString(Pref.sync_tag_date,
                "9666D29AFA66DD843782158D3DC52942");
        busTagDateStr = getSharedPreferences(Pref.sync, Context.MODE_PRIVATE).getString(Pref.sync_bus_tag_date, "null");

        typeTagDateStr = getSharedPreferences(Pref.sync, Context.MODE_PRIVATE).getString(Pref.sync_type_tag_data,
                "null");
        // ??????
        suppLabelDataStr = getSharedPreferences(Pref.sync, Context.MODE_PRIVATE).getString(Pref.sync_supp_label_data,
                "null");
        // ???????????????
        friendCircleTagDataStr = getSharedPreferences(Pref.sync, Context.MODE_PRIVATE)
                .getString(Pref.sync_friend_circle_tag_data, "null");

//        isFirst = getSharedPreferences(Pref.is_show_guide, Context.MODE_PRIVATE).getBoolean(Pref.is_show_guide, true);
        this.getSharedPreferences("EveryDayTaskTimer", Context.MODE_PRIVATE).edit().putInt("day", 0).commit();
        // ??????APP
        activate();

        String currettime2 = df.format(new Date());
        SharedPreferencesUtil.saveStringData(getApplicationContext(), "APPStartcountTime", currettime2);

        updateData(); // ????????????


        File fileDirec = new File(YConstance.savePicPath);
        if (!fileDirec.exists()) {
            fileDirec.mkdir();
        }
    }

    private void initFengkong() {

        //???????????????????????????????????????
        if (isLogined) {
            needFengKong = SharedPreferencesUtil.getBooleanData(instance, YCache.NEED_FENG_KONG, false);
            return;
        }
        //??????????????????
        YConn.httpPost(GuideActivity.instance, YUrl.CONFIG_SWITCH, new HashMap<String, String>(), new HttpListener<SwitchData>() {
            @Override
            public void onSuccess(SwitchData result) {
                channelFengkong = result.getData().getMust_risk_management_channel() == 1;
                GuideActivity.needCheckClipboard = result.getData().getConfig_popularize() == 1;
                //??????????????????????????????
                needFengKong = channelFengkong;
                if (GuideActivity.needFengKong) {
                    return;
                }

                //?????????????????????
                if (!needCheckClipboard) {
                    return;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //?????????????????????????????????
                        String clipboardContent = CommonUtils.getClipboardContent(instance);
                        if (!StringUtils.isEmpty(clipboardContent)) {
                            if (YUrl.debug) {
                                ToastUtil.showShortText2("??????????????????:" + clipboardContent);
                            }
                            if (clipboardContent.startsWith("737072656164")) {
                                SharedPreferencesUtil.saveStringData(instance, YCache.FENGKONG_CLIPBOARDCONTENT, clipboardContent);
                            } else {
                                GuideActivity.needFengKong = true;

                            }
                        } else {
                            GuideActivity.needFengKong = true;
                        }
                    }
                }, 500);


            }

            @Override
            public void onError() {

            }
        });


    }


    private void appStartCount() {
        // TODO Auto-generated method stub

        String currettime1 = df.format(new Date());
        if (SharedPreferencesUtil.getStringData(getApplicationContext(), "APPStartcountTime", "0").equals("0")) {
            // ??????????????????
            SharedPreferencesUtil.saveStringData(getApplicationContext(), "APPStartcountTime", currettime1);
        }

        // ?????????????????????
        String getCount = SharedPreferencesUtil.getStringData(getApplicationContext(), "AppstartNUM", "0");
        LogYiFu.e("getCount", getCount);

        addNUM = Integer.parseInt(SharedPreferencesUtil.getStringData(getApplicationContext(), "addNUM", "0")); // ????????????

        if (getCount.equals("0") || getCount == null) { // ?????????????????????

            SharedPreferencesUtil.saveStringData(getApplicationContext(), "AppstartNUM", addNUM + "");
            SharedPreferencesUtil.saveStringData(getApplicationContext(), "addNUM", 1 + "");

        } else { // ???????????????

            String currettime3 = SharedPreferencesUtil.getStringData(getApplicationContext(), "APPStartcountTime", "0");

            LogYiFu.e("3currettime", currettime3);
            LogYiFu.e("1currettime", currettime1);

            if (currettime3.equals(currettime1)) {
                // ???????????????????????????

                addNUM += 1;
                // ????????????
                SharedPreferencesUtil.saveStringData(getApplicationContext(), "AppstartNUM", addNUM + "");
                SharedPreferencesUtil.saveStringData(getApplicationContext(), "addNUM", addNUM + "");

            } else {
                // ??????????????????????????????
                SharedPreferencesUtil.saveStringData(getApplicationContext(), "addNUM", "1");
                SharedPreferencesUtil.saveStringData(getApplicationContext(), "AppstartNUM", 0 + "");
            }

        }

    }

    public Handler handler = new Handler();





    @Override
    protected void onResume() {
        super.onResume();
        instance = this;
        isLogined = getSharedPreferences(Pref.isLoginFlag, Context.MODE_PRIVATE).getBoolean(Pref.isLoginFlag,
                false);
//        initFengkong();

        CheckVersionUtils.updateDialogShowEd = false;
//        getSwitch();


        // if (YJApplication.isLogined ||
        // YJApplication.instance.isLoginSucess()) {
        // // ????????????????????????
        // GetUserABClass.getUserABclass(this);
        // }
        //
        // MobclickAgent.onResume(this);
    }

    private void getSwitch() {

        YConn.httpPost(GuideActivity.instance, YUrl.CONFIG_SWITCH, new HashMap<String, String>(), new HttpListener<SwitchData>() {
            @Override
            public void onSuccess(SwitchData result) {
                switchData = result;
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        // MobclickAgent.onPause(this);
    }

    private List<ImageView> imgList = new ArrayList<ImageView>();


    private void activate() {
        final String channel = DeviceUtils.getChannelCode(this);
        final String imei = CheckStrUtil.getImei(GuideActivity.this);

        LogYiFu.e("AppstartNUM", SharedPreferencesUtil.getStringData(getApplicationContext(), "AppstartNUM", "0"));

        LogYiFu.e("channel", channel);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ComModel2.startActive(GuideActivity.this, imei, channel, Integer.parseInt(
                            SharedPreferencesUtil.getStringData(getApplicationContext(), "AppstartNUM", "0")));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                // intentTo();
//                if (isFirst) {
//                    if (isCheckLast && isSyncFinish) {
//                        // TODO: 11111??????
//                        intentTo();
//                        // getStartPic();
//                        // ImageUtils.releaseImg(imgList);
//                    }
//                } else {
//                    // TODO: 11111??????
//                    // intentTo();
////                    getStartPic();
//
//                    intentTo();
//                }

                intentTo();

            } else if (msg.what == 0) {
                // ????????? ???????????? ????????????
                if (sycDataNum > 2) {
                    // ToastUtil.showShortText(GuideActivity.this,
                    // "????????????????????????????????????");
//                    intentTo();
                } else {
                    updateData();
                }
            }
        }

        ;
    };

    private ReturnInfo ret;

    private void updateData() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    ret = ComModel2.syncDatas(GuideActivity.this, sortDateStr, tagDateStr, typeTagDateStr,
                            suppLabelDataStr, friendCircleTagDataStr, true);
                    LogYiFu.e("??????????????????", sortDateStr + "tagDateStr" + tagDateStr + "typeTagDateStr" + typeTagDateStr);
                    isSyncFinish = true;
                    mHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    sycDataNum++;
                    mHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private void intentTo() {
        startActivity(new Intent(GuideActivity.this, MainMenuActivity.class)
        .putExtra("fromGuide",true)


        );
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
//        setCurDot(arg0);
//        if (arg0 == pics.length - 1) {
//
//            // ????????????????????????
//            /*
//             * for (int i = 0; i < dots.length; i++) {
//             * dots[i].setVisibility(View.GONE); }
//             */
//            // ll.setVisibility(View.GONE);
//
//            isCheckLast = true;
//            // intentTo();
//        } else {
//            // ????????????????????????
//            /*
//             * for (int i = 0; i < dots.length; i++) {
//             * dots[i].setVisibility(View.VISIBLE); }
//             */
//            // ll.setVisibility(View.VISIBLE);
//            isCheckLast = false;
//        }
//        mHandler.sendEmptyMessage(1);
//        LogYiFu.e("TAG", "??????????????????");
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }


    /**
     * ??????Pic
     */
    private void getStartPic() {
        new SAsyncTask<Void, Void, HashMap<String, Object>>((FragmentActivity) GuideActivity.this, R.string.wait) {

            @Override
            protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
                    throws Exception {
                return ComModel.startGetPic(context);
            }

            @Override
            protected boolean isHandleException() {
                return true;
            }

            @Override
            protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
                super.onPostExecute(context, result, e);

                if (null == e && result != null && !("".equals(result))) {

                    String mTime = (String) result.get("time");
                    final String mStartPic = (String) result.get("pic") + "!450";
                    // SetImageLoader.initImageLoader(null, mAddPic, mStartPic,
                    // "");


                    LogYiFu.e("????????????", mStartPic);

                    PicassoUtils.initImage(GuideActivity.this, mStartPic, mAddPic);

                }

                AlphaAnimation am = new AlphaAnimation(1.0f, 1.0f);
                am.setDuration(1000); // ??????2s
                root.startAnimation(am);

                am.setAnimationListener(new AnimationListener() {

                    public void onAnimationEnd(Animation animation) {
                        // root.setBackgroundResource(R.drawable.guid_in_1);
                        mIvLogo.setVisibility(View.GONE);
                        mAddPic.setVisibility(View.VISIBLE);
                        mNextStep.setVisibility(View.VISIBLE);

                        // final AlphaAnimation am2 = new AlphaAnimation(1.0f,
                        // 1.0f);
                        // am2.setDuration(3000); // ??????2s

                        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnim.setFillAfter(true);
                        scaleAnim.setDuration(3000);
                        mAddPic.setAnimation(scaleAnim);
                        time = new TimeCount(4000, 1000);
                        time.start();

                         final Runnable runnable = new Runnable() {
                         @Override
                         public void run() {
                         if (!isAready) {
                         startActivity(new Intent(GuideActivity.this,
                         MainMenuActivity.class));
                         overridePendingTransition(android.R.anim.fade_in,
                         android.R.anim.fade_out);
                         }
                         }
                         };
                        mNextStep.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // scaleAnim.cancel();
                                // mAddPic.removeCallbacks(runnable);
                                // am2.cancel();
                                isAready = true;
                                startActivity(new Intent(GuideActivity.this, MainMenuActivity.class)
                                        .putExtra("fromGuide",true)
                                );
                                // }
                                 GuideActivity.this.finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        });
                        // scaleAnim.setAnimationListener(new
                        // AnimationListener() {
                        //
                        // @Override
                        // public void onAnimationStart(Animation arg0) {
                        // // TODO Auto-generated method stub
                        //
                        // }
                        //
                        // @Override
                        // public void onAnimationRepeat(Animation arg0) {
                        // // TODO Auto-generated method stub
                        //
                        // }
                        //
                        // @Override
                        // public void onAnimationEnd(Animation arg0) {
                        //// mAddPic.postDelayed(new Runnable() {
                        //// @Override
                        //// public void run() {
                        ////// if (!isAready) {
                        ////// startActivity(new Intent(GuideActivity.this,
                        // MainMenuActivity.class));
                        ////// // }
                        ////// // GuideActivity.this.finish();
                        ////// overridePendingTransition(android.R.anim.fade_in,
                        // android.R.anim.fade_out);
                        //////
                        ////// }
                        //// }
                        //// }, 2000);
                        // // mAddPic.postDelayed(runnable, 2000);
                        // }
                        // });

                        // overridePendingTransition(android.R.anim.fade_in,
                        // android.R.anim.fade_out);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }
                });
                //
                // recLen = deadTime - nowTime;
                // mTvTime.setVisibility(View.VISIBLE);-
            }

        }.execute();
    }

    private TimeCount time;

    // ?????????
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// ????????????????????????,????????????????????????

        }

        @Override
        public void onFinish() {// ?????????????????????
            if (!isAready) {
                startActivity(new Intent(GuideActivity.this, MainMenuActivity.class).putExtra("fromGuide",true));
                // }
                 GuideActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// ??????????????????
            try {
                int i = Integer.valueOf(millisUntilFinished / 1000 + "");
                mNextStep.setText("?????? " + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getImageRadio() {
        new SAsyncTask<Void, Void, HashMap<String, String>>((FragmentActivity) GuideActivity.this, 0) {

            @Override
            protected HashMap<String, String> doInBackground(FragmentActivity context, Void... params)
                    throws Exception {

                return ComModel2.getImageRadio(GuideActivity.this);

            }

            protected boolean isHandleException() {
                return true;
            }

            ;

            @Override
            protected void onPostExecute(FragmentActivity context, HashMap<String, String> result, Exception e) {
                super.onPostExecute(context, result, e);
                if (e == null && result != null && result.size() > 0) {
                    SharedPreferencesUtil.saveStringData(GuideActivity.this, Pref.IMAGE_RADIO,
                            "" + result.get("img_rate"));
                } else {
                    SharedPreferencesUtil.saveStringData(GuideActivity.this, Pref.IMAGE_RADIO, "450");
                }
            }

        }.execute();

    }


    private void haiWXshouquan() {

        new SAsyncTask<Void, Void, HashMap<String, String>>((FragmentActivity)
                this, 0) {

            @Override
            protected HashMap<String, String> doInBackground(FragmentActivity
                                                                     context, Void... params)
                    throws Exception {
                return ComModel2.get1QudaoLoginStyle(getApplicationContext());
            }

            protected boolean isHandleException() {
                return true;
            }

            ;

            @Override
            protected void onPostExecute(FragmentActivity context, HashMap<String,
                    String> result, Exception e) {
                super.onPostExecute(context, result, e);

                if (e == null && result != null) {

                    int style = 1; // 1?????????????????? 2//?????????????????????

                    try {
                        style = Integer.parseInt(result.get("data") + "");
                    } catch (Exception e2) {
                        // TODO: handle exception
                    }


                    if (style == 1) {
                        needShouquan = true;
                    } else {
                        needShouquan = false;
                    }


                }
            }

        }.execute();
    }


    /**
     * ????????????????????????H5??????
     */
    public void getAndroidH5Url() {
        new SAsyncTask<Void, Void, String>(GuideActivity.this, 0) {

            @Override
            protected String doInBackground(FragmentActivity context, Void... params)
                    throws Exception {

                return ComModelL.getAndroidH5Url(GuideActivity.this);

            }

            protected boolean isHandleException() {
                return true;
            }

            @Override
            protected void onPostExecute(FragmentActivity context, String result, Exception e) {
                super.onPostExecute(context, result, e);
                if (e == null && result != null) {
                    if (!result.startsWith("http://")) {
                        result = "http://" + result;
                    }

                    if (result.endsWith("/")) {
                        YUrl.YSS_URL_ANDROID_H5 = result;
                    } else {
                        YUrl.YSS_URL_ANDROID_H5 = result + "/";
                    }

                }
            }

        }.execute();

    }
}
