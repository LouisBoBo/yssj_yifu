package com.yssj;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;

import com.ansen.http.entity.HttpConfig;
import com.ansen.http.net.HTTPCaller;
import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.ClassicFlattener;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;

import com.yssj.YConstance.Pref;
import com.yssj.activity.R;
import com.yssj.app.AppManager;
import com.yssj.app.SAsyncTask;
import com.yssj.data.DBService;
import com.yssj.entity.ReturnInfo;
import com.yssj.model.ComModel2;
import com.yssj.service.LocationService;
import com.yssj.ui.activity.CommonActivity;
import com.yssj.ui.activity.MainFragment;
import com.yssj.ui.activity.MainMenuActivity;
import com.yssj.ui.activity.MessageCenterActivity;
import com.yssj.ui.activity.infos.FundDetailsActivity;
import com.yssj.ui.activity.infos.StatusInfoActivity;
import com.yssj.ui.activity.main.FilterResultActivity;
import com.yssj.ui.activity.main.SearchResultActivity;
import com.yssj.ui.activity.payback.PaybackCommonFragmentActivity;
import com.yssj.ui.activity.shopdetails.MatchDetailsActivity;
import com.yssj.ui.activity.shopdetails.ShopDetailsActivity;
import com.yssj.ui.activity.shopdetails.ShopDetailsGroupIndianaActivity;
import com.yssj.ui.activity.shopdetails.ShopDetailsIndianaActivity;
import com.yssj.ui.activity.shopdetails.ShopDetailsMoneyIndianaActivity;
import com.yssj.ui.dialog.NewSignCommonDiaolg;
import com.yssj.ui.fragment.circles.SignFragment;
import com.yssj.ui.fragment.circles.SignListAdapter;
import com.yssj.utils.DialogUtils;
import com.yssj.utils.LogYiFu;
import com.yssj.utils.SharedPreferencesUtil;
import com.yssj.utils.SignCompleteDialogUtil;
import com.yssj.utils.YCache;
import com.zinc.libpermission.utils.JPermissionHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import update.UpdateAppUtils;

//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;

//import android.support.multidex.MultiDexApplication;
//import android.support.multidex.MultiDexApplication;
//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

//public class YJApplication extends Application {

//??????65536?????????Application
@SuppressWarnings("WrongConstant")
public class YJApplication extends MultiDexApplication {

    public static int myState = 0;

    public static int imagelimit = 0;
    public static int imagemax = 20;
    public static int bigimagelimit = 0;
    public static int bigimagemax = 10;
    public static int sdimage = 0;
    public static int sdimagemax = 20;

    public LocationService locationService;
    public Vibrator mVibrator;


    public static boolean hasRedPacketTask;//???????????????????????????????????????
    // private final long DAYTIME = 60 * 60 * 24 * 1000;

    public static HashMap<String, String> mapKuaidi;
    public static HashMap<String, Integer> mapIcon;

    public static YJApplication instance;

    public static boolean debug = true;

    // private SharedPreferences sp;
    // private Editor edit;

    public static Context mContext;

    public static boolean isLogined = false;

    // private static ImageLoader imageLoader;

    private boolean isLoginSucess = false;

    // private PushAgent mPushAgent;

    private List<String> taskMap;

    public List<String> getTaskMap() {
        return taskMap;
    }

    public void setTaskMap(List<String> taskMap) {
        this.taskMap = taskMap;
    }

    public boolean isLoginSucess() {
        return isLoginSucess;
    }

    public void setLoginSucess(boolean isLoginSucess) {
        this.isLoginSucess = isLoginSucess;
    }

    public static boolean needShowQianyuanHongBao;

    public static long serviceDifferenceTime;//??????????????????????????????


    //????????????????????????
    public static void startFukuanYndao() {
//        handler.removeCallbacks(runnable);
//
//        long tiem = 60000L;
//        try {
//            tiem = Long.parseLong(SharedPreferencesUtil.getStringData(diolgContext, "FUKUANYINDAOTIEMSHIJIAN", "60000"));
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//        handler.postDelayed(runnable, tiem); //???????????????????????????2?????????????????????-----?????????10???

    }

    public static Handler handler = new Handler();
    public static Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler???????????????????????????
            try {
                final SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");


                //?????????????????????
                String fuchuanyindaodialog = SharedPreferencesUtil.getStringData(diolgContext, "FUCHUANYINDAODIALOG", "");

                // ????????????
                final String datee = sdff.format(new Date());
                if (!datee.equals(fuchuanyindaodialog)) {//?????????????????????  ----???????????????????????????
                    if (!SharedPreferencesUtil.getBooleanData(diolgContext, Pref.ISMADMONDAY, false)) {//?????????????????????

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

//                                    if (YJApplication.instance.isLoginSucess || YJApplication.instance.isLoginSucess) {
//
//
//                                        //??????????????????????????????
//                                        boolean hasNoPayOrder = ModQingfeng.getNotFUoder(diolgContext);
//                                        if (hasNoPayOrder) {
//
//                                            ((Activity) diolgContext).runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//
//                                                    if (!YJApplication.hasRedPacketTask) {//????????????????????????????????????
//                                                        needShowQianyuanHongBao = true;
//                                                        if (HomePageFragment.homeIsShow) {
//                                                            SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
//                                                            DialogUtils.redPacketDownDialog(diolgContext);
//                                                            //???????????????????????????
//                                                            String dateeNow = sdff.format(new Date());
//                                                            SharedPreferencesUtil.saveStringData(mContext, "FUCHUANYINDAODIALOG", dateeNow);
//                                                            YJApplication.needShowQianyuanHongBao = false;
//                                                        }
//
//
//                                                    }
//
//
//                                                }
//                                            });
//
//
//                                        }
//
//                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }).start();

                    }
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };


    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        mContext = getApplicationContext();
        UpdateAppUtils.init(mContext);
        JPermissionHelper.injectContext(this);


        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);


        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setAgent(YUrl.debug);//?????????????????????????????????
        httpConfig.setDebug(true);//??????debug?????? ?????????debug????????????log
        httpConfig.setTagName("ansen");//??????log???tagname

        //?????????????????????????????? ????????????????????????
        httpConfig.addCommonField("pf", "android");
        httpConfig.addCommonField("version_code", "1");

        //?????????HTTPCaller???
        HTTPCaller.getInstance().setHttpConfig(httpConfig);


        initXlog();


        SharedPreferencesUtil.saveStringData(mContext, Pref.PAYSUCCESSDIALOG_SHOW_DIALOG, "-1");


//        volleyQueue = Volley.newRequestQueue(getApplicationContext());

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

//        if (YConstance.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
//        }
        initDataAPP();
        // mContext.getSharedPreferences(Pref.UM_FRIST_OPEN,
        // Context.MODE_PRIVATE).edit()
        // .putInt(Pref.UM_FRIST_OPEN,
        // Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();


    }

//    // ??????????????????
//    public static RequestQueue volleyQueue;
//
//    // ??????Volley???HTTP??????????????????
//    public static RequestQueue getRequestQueue() {
//        return volleyQueue;
//    }

    // ????????????????????????????????????
    private SharedPreferences sp;
    private boolean isShow = false;

    public void isShowUpdateDialog(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean getIsShowUpdateDialog() {
        return isShow;
    }

    private int isOpen = 0;
    private boolean isMeal = false;
    private String shop_type = "1";

    public String getShop_type() {
        return shop_type;
    }

    public boolean isMeal() {
        return isMeal;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public int isOpen() {
        return isOpen;
    }

    public void setOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    private int checkID = 3;

    public int getCheckID() {
        return checkID;
    }

    public void setCheckID(int checkID) {
        this.checkID = checkID;
    }

//    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            abortBroadcast();
//            String msgid = intent.getStringExtra("msgid");
//            String from = intent.getStringExtra("from");
//            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
//            if (conversation != null) {
//                // ???message????????????
//                EMMessage msg = conversation.getMessage(msgid);
//                if (msg != null) {
//                    msg.isAcked = true;
//                }
//            }
//
//        }
//    };


    public static Context diolgContext;

    public static void setDialogContext(Context context) {
        diolgContext = context;
    }


    public static TimerTask signFenzhongTask(final NewSignCommonDiaolg.SignRefreshDataListener refreshData) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // ????????????????????????????????????
                // ToastUtil.showShortText(context, "?????????????????????????????????");
                if (YJApplication.isLogined || YJApplication.instance.isLoginSucess()) {
                    SignListAdapter.isForceLookTimeOut = true;

                    // ??????
                    new SAsyncTask<Void, Void, HashMap<String, Object>>((FragmentActivity) diolgContext, 0) {

                        @Override
                        protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
                                throws Exception {

                            // ??????????????????????????????????????????

                            return ComModel2.getSignIn(diolgContext, false, false,
                                    SignListAdapter.indexMap.get(YConstance.SCAN_SHOP_TIME),
                                    SignListAdapter.classMap.get(YConstance.SCAN_SHOP_TIME));

                        }

                        protected boolean isHandleException() {
                            return true;
                        }


                        @Override
                        protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result,
                                                     Exception e) {
                            super.onPostExecute(context, result, e);
                            if (e == null && result != null) {

                                SignListAdapter.minuteMap.clear();

                                if (SignFragment.signIsShow) { //???????????????????????????????????????
                                    SignListAdapter.neeedFenzhongCompleteDiaog = false;

//                                    ToastUtil.showDialog(new SignFengZhongCompleteDialog(diolgContext, R.style.DialogQuheijiao, "bankuailiulanwancheng", SignFragment.signFragment));

                                    if (Integer.valueOf(result.get("isNewbie01") + "") == 1) {
                                        SignCompleteDialogUtil.firstClickInGoToZP(diolgContext);
                                        return;
                                    }

                                    String jianglivalue = SignListAdapter.jiangliValueMap.get(YConstance.SCAN_SHOP_TIME);
                                    SignCompleteDialogUtil.showSignComplete(diolgContext, jianglivalue);


                                } else {//???????????????????????????????????????????????????
                                    if (Integer.valueOf(result.get("isNewbie01") + "") == 1) {
                                        SignListAdapter.showFirstClickInSuccseeDialog = true;
                                        return;
                                    }
                                    SignListAdapter.neeedFenzhongCompleteDiaog = true;
                                }


                                refreshData.timeOut();

                            } else {

                            }

                        }

                    }.execute();
                }


            }

        };

        return task;
    }


    /**
     * ??????????????? //??????????????????????????? NewMessageBroadcastReceiver???
     */
//    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // ????????????
//            abortBroadcast();
//
//            // ??????id??????????????????????????????????????????id????????????SDK?????????
//            String msgId = intent.getStringExtra("msgid");
//            // ?????????
//            String username = intent.getStringExtra("from");
//            // ??????????????????????????????message?????????db??????????????????????????????id??????mesage??????
//            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
//            EMConversation conversation = EMChatManager.getInstance().getConversation(username);
//            // ?????????????????????????????????group id
//            if (null != message) {
//                if (message.getChatType() == ChatType.GroupChat) {
//                    username = message.getTo();
//                }
//            }
//            if (!username.equals(username)) {
//                // ?????????????????????????????????return
//                return;
//            }
//        }
//    }
    public void pushConunt(final Context context, final int code_type, final String message_id) {
        LogYiFu.e("push", "11111");
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    LogYiFu.e("push", ">>>>" + code_type);
                    ReturnInfo info = ComModel2.UMPushCount(context, code_type, message_id);
                    LogYiFu.e("push", "*****" + info.getStatus());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }

    // public static ImageLoader getLoader() {
    // if (imageLoader == null) {
    // ImageLoaderConfiguration config = new
    // ImageLoaderConfiguration.Builder(instance).threadPoolSize(3)
    // .threadPriority(Thread.NORM_PRIORITY -
    // 2).denyCacheImageMultipleSizesInMemory()
    // // .memoryCacheSize()
    // .memoryCache(new LruMemoryCache((int) Runtime.getRuntime().maxMemory() /
    // 8))
    // .diskCacheSize(5 * 1024 * 1024).diskCacheFileNameGenerator(new
    // Md5FileNameGenerator())
    // .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().diskCacheFileCount(100)
    // // ?????????????????????
    // .imageDownloader(new BaseImageDownloader(instance, 5 * 1000, 30 *
    // 1000)).build();// ????????????
    // imageLoader = ImageLoader.getInstance();
    // imageLoader.init(config);
    // }
    // return imageLoader;
    // }
    //
    // public static ImageLoader imageLoader;

    // public static void getLoader(Context context) {
    //
    // ImageLoaderConfiguration config = new
    // ImageLoaderConfiguration.Builder(context).threadPoolSize(3)
    // .threadPriority(Thread.NORM_PRIORITY -
    // 2).denyCacheImageMultipleSizesInMemory()
    // // .memoryCacheSize()
    // .memoryCache(new LruMemoryCache((int) Runtime.getRuntime().maxMemory() /
    // 8))
    // .diskCacheSize(5 * 1024 * 1024).diskCacheFileNameGenerator(new
    // Md5FileNameGenerator())
    // .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().diskCacheFileCount(100)
    // // ?????????????????????
    // .imageDownloader(new BaseImageDownloader(instance, 5 * 1000, 30 *
    // 1000)).build();// ????????????
    // ImageLoader.getInstance().init(config);
    //
    // }

    private void initDataAPP() {
        DBService db = new DBService(this);
        db.openDatabase(); // ?????????????????????

    }

    // ????????????
    static {
        mapKuaidi = new HashMap<String, String>();
        mapKuaidi.put("yuantong", "??????");
        mapKuaidi.put("shentong", "??????");
        mapKuaidi.put("ems", "EMS");
        mapKuaidi.put("shunfeng", "??????");
        mapKuaidi.put("zhongtong", "??????");
        mapKuaidi.put("yunda", "??????");
        mapKuaidi.put("tiantian", "??????");
        mapKuaidi.put("huitongkuaidi", "??????");
        mapKuaidi.put("quanfengkuaidi", "??????");
        mapKuaidi.put("debangwuliu", "??????");
        mapKuaidi.put("zhaijisong", "?????????");
        mapKuaidi.put("youzhengguonei", "????????????/??????");
        mapKuaidi.put("guotongkuaidi", "??????");
        mapKuaidi.put("zengyisudi", "??????");
        mapKuaidi.put("suer", "??????");
        mapKuaidi.put("ztky", "????????????");
        mapKuaidi.put("ganzhongnengda", "??????");
        mapKuaidi.put("youshuwuliu", "??????");
        mapKuaidi.put("quanfengkuaidi", "??????");

        mapIcon = new HashMap<String, Integer>();
        mapIcon.put("shop/type/waitao.png", R.drawable.waitao);
        mapIcon.put("shop/type/waitao1.png", R.drawable.waitao1);
        mapIcon.put("shop/type/shangyi.png", R.drawable.shangyi);
        mapIcon.put("shop/type/shangyi1.png", R.drawable.shangyi1);
        mapIcon.put("shop/type/qunzi.png", R.drawable.qunzi);
        mapIcon.put("shop/type/qunzi1.png", R.drawable.qunzi1);
        mapIcon.put("shop/type/kuzi.png", R.drawable.kuzi);
        mapIcon.put("shop/type/kuzi1.png", R.drawable.kuzi1);
        mapIcon.put("shop/type/temai.png", R.drawable.temai);
        mapIcon.put("shop/type/temai1.png", R.drawable.temai1);
        mapIcon.put("shop/type/remai.png", R.drawable.remai);
        mapIcon.put("shop/type/remai1.png", R.drawable.remai1);
        mapIcon.put("shop/type/zongtaozhuang.png", R.drawable.zongtaozhuang);
        mapIcon.put("shop/type/zongtaozhuang1.png", R.drawable.zongtaozhuang1);
    }

    public static int getState() {
        return myState;
    }

    public static void setState(int s) {
        myState = s;
    }

    // lc???sd???????????????
    public static int getsdimage() {
        return sdimage;
    }

    public static void setsdimage(int s) {
        sdimage = s;
    }

    public static int getsdimagemax() {
        return imagemax;
    }

    public static void setsdimagemax(int s) {
        imagemax = s;
    }

    // lc:???????????????
    // public static int getbigimagelimit() {
    // return bigimagelimit;
    // }
    //
    // public static void setbigimagelimit(int s) {
    // bigimagelimit = s;
    // }
    //
    // public static int getbigimagemax() {
    // return bigimagemax;
    // }
    //
    // public static void setbigimagemax(int s) {
    // bigimagemax = s;
    // }
    //
    // // lc??????????????????
    // public static int getimagelimit() {
    // return imagelimit;
    // }
    //
    // public static void setimagelimit(int s) {
    // imagelimit = s;
    // }
    //
    // public static int getimagemax() {
    // return imagemax;
    // }
    //
    // public static void setimagemax(int s) {
    // imagemax = s;
    // }
    // @Override
    // protected void attachBaseContext(Context base) {
    // // TODO Auto-generated method stub
    // super.attachBaseContext(base);
    // }

    // @SuppressLint("NewApi")
    // private void dexTool() {
    //
    // File dexDir = new File(getFilesDir(), "dlibs");
    // dexDir.mkdir();
    // File dexFile = new File(dexDir, "libs.apk");
    // File dexOpt = getCacheDir();
    // try {
    // InputStream ins = getAssets().open("libs.apk");
    // if (dexFile.length() != ins.available()) {
    // FileOutputStream fos = new FileOutputStream(dexFile);
    // byte[] buf = new byte[4096];
    // int l;
    // while ((l = ins.read(buf)) != -1) {
    // fos.write(buf, 0, l);
    // }
    // fos.close();
    // }
    // ins.close();
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // }
    //
    // ClassLoader cl = getClassLoader();
    // ApplicationInfo ai = getApplicationInfo();
    // String nativeLibraryDir = null;
    // if (Build.VERSION.SDK_INT > 8) {
    // nativeLibraryDir = ai.nativeLibraryDir;
    // } else {
    // nativeLibraryDir = "/data/data/" + ai.packageName + "/lib/";
    // }
    // DexClassLoader dcl = new DexClassLoader(dexFile.getAbsolutePath(),
    // dexOpt.getAbsolutePath(), nativeLibraryDir, cl.getParent());
    //
    // try {
    // Field f = ClassLoader.class.getDeclaredField("parent");
    // f.setAccessible(true);
    // f.set(cl, dcl);
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // }
    // }

    /**
     * ??????Acitity??????
     */
    private int activityAount = 0;
    public static long APPstartTime = 0L;
    private long APPendTime = 0L;

    private Timer overtimer;
    TimerTask overtask;
    public static boolean tongJiYet = false; // ???????????????APP????????????

    /**
     * Activity ?????????????????????????????????app?????????????????????
     */
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activityAount == 0) {
                APPstartTime = System.currentTimeMillis();
                if (overtimer != null) {
                    overtimer.cancel();
                }
                // ??????APP???????????????APP??????????????????30??????????????????????????????APP????????????
                if (!tongJiYet) {
                    boolean isLogin = SharedPreferencesUtil.getBooleanData(mContext, "ISCHUCHIDNEGLU", false);
                    // ????????????APP??????????????????
                    if (isLogin) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ComModel2.APPuseCount(mContext);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                }

            }
            activityAount++;

        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityAount--;
            if (activityAount == 0) {
                APPendTime = System.currentTimeMillis();
//				ToastUtil.showShortText(mContext, "????????????,APP????????????" + (APPendTime - APPstartTime) / 1000 + "???");
                // APP??????????????????
                if (overtimer != null) {
                    overtimer = null;
                }
                if (overtask != null) {
                    overtask = null;
                }
                overtimer = new Timer();
                // ?????????
                overtask = new TimerTask() {
                    @Override
                    public void run() {
//						ToastUtil.showShortText(mContext, "??????????????????30???");
                        tongJiYet = false;

                    }

                };

                overtimer.schedule(overtask, 30 * 1000);
                // APP??????????????????
                if (YJApplication.isLogined || YJApplication.instance.isLoginSucess()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ComModel2.APPuseTime(mContext, (APPendTime - APPstartTime) + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }

            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    private void initXlog() {
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(com.elvishew.xlog.BuildConfig.DEBUG ? LogLevel.ALL             // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
                        : LogLevel.ALL)
//                .tag(getString(R.string.global_tag))                   // Specify TAG, default: "X-LOG"
                .t()                                                // Enable thread info, disabled by default
                .st(2)                                              // Enable stack trace info with depth 2, disabled by default
                .b()                                                // Enable border, disabled by default
                // .jsonFormatter(new MyJsonFormatter())               // Default: DefaultJsonFormatter
                // .xmlFormatter(new MyXmlFormatter())                 // Default: DefaultXmlFormatter
                // .throwableFormatter(new MyThrowableFormatter())     // Default: DefaultThrowableFormatter
                // .threadFormatter(new MyThreadFormatter())           // Default: DefaultThreadFormatter
                // .stackTraceFormatter(new MyStackTraceFormatter())   // Default: DefaultStackTraceFormatter
                // .borderFormatter(new MyBoardFormatter())            // Default: DefaultBorderFormatter
                // .addObjectFormatter(AnyClass.class,                 // Add formatter for specific class of object
                //     new AnyClassObjectFormatter())                  // Use Object.toString() by default
                //.addInterceptor(new BlacklistTagsFilterInterceptor(    // Add blacklist tags filter
                //        "blacklist1", "blacklist2", "blacklist3"))
                // .addInterceptor(new Whitel\istTagsFilterInterceptor( // Add whitelist tags filter
                //     "whitelist1", "whitelist2", "whitelist3"))
                // .addInterceptor(new MyInterceptor())                // Add a log interceptor
                .build();

        Printer androidPrinter = new AndroidPrinter();             // Printer that print the log using com.elvishew.xlog.XLog
        Printer filePrinter = new FilePrinter                      // Printer that print the log to the file system
                // .Builder("/data/xlog/")
                .Builder(this.getFilesDir().getPath())
                //.Builder(new File(Environment.getExternalStorageDirectory(), "XLOG").getPath())       // Specify the path to save log file
                .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
                // .backupStrategy(new MyBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
                .logFlattener(new ClassicFlattener())                  // Default: DefaultFlattener
                .build();

//        XLog.init(                                                 // Initialize XLog
//                config,                                                // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
//                androidPrinter,                                        // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
//                filePrinter);
//
//        XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);
        XLog.init(LogLevel.NONE);


    }


}
