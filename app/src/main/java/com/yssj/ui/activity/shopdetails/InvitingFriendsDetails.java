package com.yssj.ui.activity.shopdetails;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.yssj.Constants;
import com.yssj.YConstance;
import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.app.SAsyncTask;
import com.yssj.custom.view.RoundImageButton;
import com.yssj.entity.Store;
import com.yssj.entity.UserInfo;
import com.yssj.model.ComModel2;
import com.yssj.ui.base.BasicActivity;
import com.yssj.ui.dialog.PublicToastDialog;
import com.yssj.ui.fragment.circles.SignListAdapter;
import com.yssj.utils.LogYiFu;
import com.yssj.utils.MD5Tools;
import com.yssj.utils.PicassoUtils;
import com.yssj.utils.QRCreateUtil;
import com.yssj.utils.ShareUtil;
import com.yssj.utils.SignCompleteDialogUtil;
import com.yssj.utils.StringUtils;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.YCache;

import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

public class InvitingFriendsDetails extends BasicActivity implements OnClickListener {
    private RoundImageButton mUserImage, mDummyImage1, mDummyImage2, mDummyImage3, mDummyImage4, mDummyImage5;// ????????????
    private TextView mUserName, mDummyName1, mDummyName2, mDummyName3, mDummyName4, mDummyName5;// ?????????
    private TextView mUserMoney, mDummyMoney1, mDummyMoney2, mDummyMoney3, mDummyMoney4, mDummyMoney5;// ?????????????????????
    private TextView mFansCount, mInviteFriends;// ??????????????????????????????
    private Context mContext;
    private LinearLayout img_back;
    private Exception ee = null;
    private String link;// ??????????????????
    private String mJiuLink;// ???????????????
    private Intent qqShareIntent = ShareUtil.shareMultiplePictureToQZone(ShareUtil.getImage()); // ?????????QQ??????
    private Intent weixinShareIntent = ShareUtil.shareToWechat(ShareUtil.getImage());// ?????????????????????
    private UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR_SHARE);
    private Handler mHandle = new Handler();
    private Runnable mRunnable;
    private int[] pics = {R.drawable.inviting01, R.drawable.inviting02, R.drawable.inviting03, R.drawable.inviting04,
             R.drawable.inviting06, R.drawable.inviting07, R.drawable.inviting08,
            R.drawable.inviting09};
    private boolean mClickFlag = false;// ???????????????????????????????????????
    private boolean isComplete = false;// ???????????????????????????true???????????????????????????????????????
    private PublicToastDialog shareWaitDialog;
    private String[] mNotice = {"????????????????????????????????????????????????????????????~?????????", "??????????????????????????????????????????????????????~?????????", "??????????????????????????????????????????~?????????",
            "??????????????????????????????????????????????????????"};
    private TextView mTvNotice;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inviting_friends_details);
        mContext = this;
        isComplete = getIntent().getBooleanExtra("isComplete", false);
        Store store = YCache.getCacheStoreSafe(mContext);
        link = YUrl.YSS_URL_ANDROID_H5 + "view/download/6.html?realm=" + YCache.getCacheUser(mContext).getUser_id();
        mJiuLink = YUrl.YSS_URL_ANDROID_H5 + "view/download/7.html?realm=" + YCache.getCacheUser(mContext).getUser_id();
        initView();
        initData();
        if (null == mRunnable) {
            mRunnable = new Runnable() {

                @Override
                public void run() {
                    changeData();
                    mHandle.postDelayed(mRunnable, 5 * 60 * 1000);// ?????????????????????????????????????????????

                }
            };
        } else {
            mRunnable = null;
            mRunnable = new Runnable() {

                @Override
                public void run() {
                    changeData();
                    mHandle.postDelayed(mRunnable, 5 * 60 * 1000);

                }

            };
        }
        mHandle.post(mRunnable);
    }

    private void initData() {
        getFansMoney();
        UserInfo info = YCache.getCacheUserSafe(mContext);
        mUserName.setText(info.getNickname());
//		SetImageLoader.initImageLoader(mContext, mUserImage, info.getPic(), "");
        PicassoUtils.initImage(mContext, info.getPic(), mUserImage);
        changeData();
    }

    private void initView() {
        String price = getIntent().getStringExtra("signMoney");
        mTvNotice = (TextView) findViewById(R.id.invite_tv_notice);
//		String s = "???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" + price
//				+ "???????????????????????????????????????????????????????????????????????????~";
//		mTvNotice.setText(s);
        mUserImage = (RoundImageButton) findViewById(R.id.img_user_pic);
        mDummyImage1 = (RoundImageButton) findViewById(R.id.img_dummy1);
        mDummyImage2 = (RoundImageButton) findViewById(R.id.img_dummy2);
        mDummyImage3 = (RoundImageButton) findViewById(R.id.img_dummy3);
        mDummyImage4 = (RoundImageButton) findViewById(R.id.img_dummy4);
        mDummyImage5 = (RoundImageButton) findViewById(R.id.img_dummy5);
        mUserName = (TextView) findViewById(R.id.tv_user_name);
        mDummyName1 = (TextView) findViewById(R.id.tv_name1);
        mDummyName2 = (TextView) findViewById(R.id.tv_name2);
        mDummyName3 = (TextView) findViewById(R.id.tv_name3);
        mDummyName4 = (TextView) findViewById(R.id.tv_name4);
        mDummyName5 = (TextView) findViewById(R.id.tv_name5);
        mUserMoney = (TextView) findViewById(R.id.tv_get_money);
        mDummyMoney1 = (TextView) findViewById(R.id.tv_money1);
        mDummyMoney2 = (TextView) findViewById(R.id.tv_money2);
        mDummyMoney3 = (TextView) findViewById(R.id.tv_money3);
        mDummyMoney4 = (TextView) findViewById(R.id.tv_money4);
        mDummyMoney5 = (TextView) findViewById(R.id.tv_money5);
        mFansCount = (TextView) findViewById(R.id.tv_fans_num);
        mInviteFriends = (TextView) findViewById(R.id.tv_invite);
        mInviteFriends.setOnClickListener(this);
        img_back = (LinearLayout) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
    }

    public void changeData() {
//		SetImageLoader.initImageLoader(mContext, mDummyImage1, "defaultcommentimage/" + StringUtils.getDefaultImg(),
//				"");
//		SetImageLoader.initImageLoader(mContext, mDummyImage2, "defaultcommentimage/" + StringUtils.getDefaultImg(),
//				"");
//		SetImageLoader.initImageLoader(mContext, mDummyImage3, "defaultcommentimage/" + StringUtils.getDefaultImg(),
//				"");
//		SetImageLoader.initImageLoader(mContext, mDummyImage4, "defaultcommentimage/" + StringUtils.getDefaultImg(),
//				"");
//		SetImageLoader.initImageLoader(mContext, mDummyImage5, "defaultcommentimage/" + StringUtils.getDefaultImg(),
//				"");

        PicassoUtils.initImage(mContext, "defaultcommentimage/" + StringUtils.getDefaultImg(), mDummyImage1);
        PicassoUtils.initImage(mContext, "defaultcommentimage/" + StringUtils.getDefaultImg(), mDummyImage2);
        PicassoUtils.initImage(mContext, "defaultcommentimage/" + StringUtils.getDefaultImg(), mDummyImage3);
        PicassoUtils.initImage(mContext, "defaultcommentimage/" + StringUtils.getDefaultImg(), mDummyImage4);
        PicassoUtils.initImage(mContext, "defaultcommentimage/" + StringUtils.getDefaultImg(), mDummyImage5);


        mDummyName1.setText(StringUtils.getVirtualName() + "***" + StringUtils.getVirtualName());
        mDummyName2.setText(StringUtils.getVirtualName() + "***" + StringUtils.getVirtualName());
        mDummyName3.setText(StringUtils.getVirtualName() + "***" + StringUtils.getVirtualName());
        mDummyName4.setText(StringUtils.getVirtualName() + "***" + StringUtils.getVirtualName());
        mDummyName5.setText(StringUtils.getVirtualName() + "***" + StringUtils.getVirtualName());
        Random random = new Random();
        mDummyMoney1.setText((random.nextInt(151) + 50) + "." + random.nextInt(10) + "???");
        mDummyMoney2.setText((random.nextInt(151) + 50) + "." + random.nextInt(10) + "???");
        mDummyMoney3.setText((random.nextInt(151) + 50) + "." + random.nextInt(10) + "???");
        mDummyMoney4.setText((random.nextInt(151) + 50) + "." + random.nextInt(10) + "???");
        mDummyMoney5.setText((random.nextInt(151) + 50) + "." + random.nextInt(10) + "???");
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_invite:
                getShareDialog();
                break;
            default:
                break;
        }

    }

    public void getShareDialog() {
        final Dialog shareDialog = new Dialog(mContext, R.style.invate_dialog_style);
        View view = View.inflate(mContext, R.layout.dialog_invite_share, null);
        TextView mClose = (TextView) view.findViewById(R.id.tv_close);
        mClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
            }
        });
        TextView mTvSweep = (TextView) view.findViewById(R.id.dialog_tv_sweep);// ????????????????????????
        mTvSweep.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                shareDialog.dismiss();
                getCodeLink(3);

            }
        });
        ImageView mWeiXinShare = (ImageView) view.findViewById(R.id.iv_wxin_circle_share);// ???????????????
        mWeiXinShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
                getCodeLink(1);

            }
        });
        ImageView mQQZoneShare = (ImageView) view.findViewById(R.id.dialog_iv_qq_share);// ?????????QQ??????
        mQQZoneShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
                getCodeLink(0);
            }
        });
        ImageView mFriendsCicleShare = (ImageView) view.findViewById(R.id.iv_wxin_share);// ??????????????????
        mFriendsCicleShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
                getCodeLink(2);
            }
        });
        shareDialog.setContentView(view);
        shareDialog.setCancelable(true);
        shareDialog.setCanceledOnTouchOutside(false);
        shareDialog.show();
    }

    public void performShare(SHARE_MEDIA platform, final Intent intent) {
        UMImage umImage;

        // File bmg = null;
        // Drawable img_invite_friend =
        // getResources().getDrawable(R.drawable.invite_friend);
        // BitmapDrawable bd = (BitmapDrawable) img_invite_friend;
        umImage = new UMImage(mContext, R.drawable.inviting_single);

        ShareUtil.setShareContent(mContext, umImage, "????????????????????????????????????????????????????????????1????????????", link);

        mController.postShare(mContext, platform, new SnsPostListener() {

            @Override
            public void onStart() {
                LogYiFu.e("showText", "asdsafdsf");
                // chooseDialog();
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = platform.toString();
                LogYiFu.e("showText", showText);

                if (eCode == StatusCode.ST_CODE_SUCCESSED) {

                    ToastUtil.showShortText(mContext, "????????????");
                } else {

                }

            }
        });
    }

    /**
     * ?????????????????????
     */
    private void shareToWeChat(String link, UMImage umImage) {
//		WeiXinShareContent wei = new WeiXinShareContent();
//		// wei.setTitle("??????????????????????????????????????????");
//		wei.setShareContent("????????????????????????????????????????????????????????????1????????????");
//		// wei.setTitle("????????????????????????");
//		wei.setTitle("???????????????????????????600+");
//		// wei.setShareContent("?????????????????????????????????>>");
//		wei.setTargetUrl(link);
//		wei.setShareMedia(umImage);
//		mController.setShareMedia(wei);
//		performShare(SHARE_MEDIA.WEIXIN, weixinShareIntent);
    }

    public void getCodeLink(final int flag) {
        if (!mClickFlag && !isComplete) {
            sign();
        }
        // link = result.get("link").toString();
        if (flag == 0) {// ?????????QQ??????
            ShareUtil.addQQQZonePlatform(InvitingFriendsDetails.this);
            ToastUtil.showShortText(InvitingFriendsDetails.this, "???????????????????????????~");

            performShare(SHARE_MEDIA.QZONE, qqShareIntent);
        } else if (flag == 1) {// ???????????????
            ShareUtil.addWXPlatform(mContext);
            shareToWeChat(link, new UMImage(mContext, R.drawable.inviting_single));
        } else if (flag == 2) {// ??????????????????
            if (null == shareWaitDialog) {
                shareWaitDialog = new PublicToastDialog(this, R.style.DialogStyle1, "");
            }
            shareWaitDialog.show();
            shareTo(link);
//			getPicture();
            // getLong2Short();
            // new Thread(new Runnable() {
            //
            // @Override
            // public void run() {
            // shareTo(mTopLink);
            // }
            // }).start();

        } else if (flag == 3) {// ???????????????
            Intent intent = new Intent(mContext, SweepImageActivity.class);
            startActivity(intent);
            ((FragmentActivity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);

        }

    }

    private void shareTo(String url) {
        saveFile();
        // qqShareIntent =
        // ShareUtil.shareMultiplePictureToQZone(ShareUtil.getImage());
        UMImage umImage = new UMImage(InvitingFriendsDetails.this, R.drawable.inviting_single);
        Intent wXinShareIntent = ShareUtil.shareMultiplePictureToTimeLine(ShareUtil.getImage());
        wXinShareIntent.putExtra("Kdescription", mNotice[new Random().nextInt(4)] + url);
        shareWaitDialog.dismiss();
        if (ShareUtil.intentIsAvailable(InvitingFriendsDetails.this, wXinShareIntent)) {
            InvitingFriendsDetails.this.startActivity(wXinShareIntent);
        } else {
            // qqShareIntent =
            // ShareUtil.shareMultiplePictureToQZone(ShareUtil.getImage());
            // performShare(SHARE_MEDIA.QZONE, qqShareIntent);
            ToastUtil.showShortText(mContext, "????????????");
        }

    }

    public void saveFile() {// ??????????????????????????????
        File fileDirec = new File(YConstance.savePicPath);
        if (!fileDirec.exists()) {
            fileDirec.mkdir();
        }
        File[] listFiles = new File(YConstance.savePicPath).listFiles();
        if (listFiles.length != 0) {
            LogYiFu.e("TAG", "??????????????? ?????????????????????");
            for (File file : listFiles) {
                file.delete();
            }
        }
        FileOutputStream bigOutputStream = null;

        for (int i = 0; i < pics.length; i++) {
            if (i == 4) {
                Drawable img_invite_friend = getResources().getDrawable(pics[4]);
                LogYiFu.e("TAGS", "?????????" + i + "?????????");
                BitmapDrawable bd = (BitmapDrawable) img_invite_friend;
                Bitmap bmBg = bd.getBitmap();
                Bitmap bmQr = QRCreateUtil.createQrImage(mJiuLink, 200, 200);
                // Bitmap bm =
                // QRCreateUtil.drawNewBitmapNine2(InvitingFriendsDetails.this,
                // bmQr, bmg);
                Bitmap bm = QRCreateUtil.drawInVitingBitmapNine(mContext, bmBg, bmQr);
                // Bitmap
                // bm=QRCreateUtil.createInvitingImage(link,600,600,mContext);
                QRCreateUtil.saveBitmap(bm, YConstance.savePicPath, MD5Tools.md5(String.valueOf(i)) + ".jpg");// ?????????????????????
            } else {

                Drawable img_invite_friend = getResources().getDrawable(pics[i]);
                LogYiFu.e("TAGS", "?????????" + i + "?????????");
                BitmapDrawable bd = (BitmapDrawable) img_invite_friend;
                Bitmap bmg = bd.getBitmap();

                // YConstance.savePicPath; "/sdcard/share_pic.png"
                // "/sdcard/yssjaa/";
                String file = YConstance.savePicPath + MD5Tools.md5(String.valueOf(i)) + ".jpg"; // ??????

                try {
                    bigOutputStream = new FileOutputStream(file);
                    bmg.compress(Bitmap.CompressFormat.JPEG, 100, bigOutputStream);// ?????????????????????
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bigOutputStream.flush();
                        bigOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // ????????????
    private void downloadPic(String picPath, int i) {
        try {
            URL url = new URL(YUrl.imgurl + picPath);
            // ????????????
            URLConnection con = url.openConnection();
            // ?????????????????????
            int contentLength = con.getContentLength();
            // ?????????
            InputStream is = con.getInputStream();
            // 1K???????????????
            byte[] bs = new byte[8192];
            // ????????????????????????
            int len;
            // ?????????????????? /sdcard/yssj/
            File file = new File(YConstance.savePicPath, MD5Tools.md5(String.valueOf(i)) + ".jpg");
            if (file.exists()) {
                file.delete();
            }
            LogYiFu.e("TAG", "??????????????????????????????????????????");
            OutputStream os = new FileOutputStream(file);
            // ????????????
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            LogYiFu.i("TAG", "?????????????????????file=" + file.toString());
            // ???????????????????????????
            os.close();
            is.close();
        } catch (Exception e) {
            LogYiFu.e("TAG", "????????????");
            e.printStackTrace();
        }
    }

    private void sign() {
        // ??????
        new SAsyncTask<Void, Void, HashMap<String, Object>>((FragmentActivity) mContext, 0) {

            @Override
            protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
                    throws Exception {

                return ComModel2.getSignIn(InvitingFriendsDetails.this, false, false, SignListAdapter.signIndex,
                        SignListAdapter.doClass);

            }

            protected boolean isHandleException() {
                return true;
            }

            ;

            @Override
            protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
                super.onPostExecute(context, result, e);
                if (e == null && result != null) {
                    mClickFlag = true;

                    if (Integer.valueOf(result.get("isNewbie01") + "") == 1) {
                        SignCompleteDialogUtil.firstClickInGoToZP(mContext);
                        return;
                    }

                    // TODO:
                } else {
                    // ToastUtil.showLongText(mContext, "????????????");
                }

            }

        }.execute();
    }

    /**
     * ????????????????????????????????????
     */
    private void getFansMoney() {
        new SAsyncTask<Void, Void, HashMap<String, Object>>((FragmentActivity) mContext, R.string.wait) {

            @Override
            protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
                    throws Exception {
                return ComModel2.getFansMoney(context);
            }

            @Override
            protected boolean isHandleException() {
                return true;
            }

            @Override
            protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
                super.onPostExecute(context, result, e);

                if (null == e && result != null) {
                    String fans = "" + result.get("fans");
                    String money = "" + result.get("money");
                    String remark = "" + result.get("remark");
                    mUserMoney.setText("" + new DecimalFormat("#0.00").format(Double.parseDouble(money)) + "???");
                    mFansCount.setText("" + fans + "???");
                    mTvNotice.setText(remark);
                }
            }

        }.execute();
    }

    // /**
    // * ??????????????????
    // */
    // private void getLong2Short() {
    // new SAsyncTask<Void, Void, String>(InvitingFriendsDetails.this,
    // R.string.wait) {
    //
    // @Override
    // protected String doInBackground(FragmentActivity context, Void... params)
    // throws Exception {
    // Store store = YCache.getCacheStoreSafe(mContext);
    // return ComModel2.long2short(context, mTopLink);
    // }
    //
    // @Override
    // protected boolean isHandleException() {
    // return true;
    // }
    //
    // @Override
    // protected void onPostExecute(FragmentActivity context, final String
    // result, Exception e) {
    // super.onPostExecute(context, result, e);
    //
    // if (null == e && result != null) {
    // new Thread(new Runnable() {
    //
    // @Override
    // public void run() {
    // shareTo(result);
    // }
    // }).start();
    // }
    // }
    //
    // }.execute();
    // }

    // ?????????????????????
    private byte[] picByte;

    public void getPicture() {
        new Thread(runnables).start();
    }

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 99) {
                if (picByte != null) {
                    try {
                        String s = new String(picByte, HTTP.UTF_8).toString().trim();
                        LogYiFu.e("hello", s);
                        // shareTo(s);
                        if (null != s && s.length() > 5 && s.length() < 30 && picByte.length > 5) {
                            // String ss=s.substring(0,4);
                            // String str2 = convertEncodingFormat(ss,
                            // "iso-8859-1", "UTF-8");
                            // if("http".equals(str2)){
                            shareTo(s);
                            // }else{
                            // shareTo(link);
                            // }
                        } else {
                            shareTo(link);
                        }

                    } catch (Exception e) {
                        shareTo(link);
                        e.printStackTrace();
                    }
                } else {
                    shareTo(link);
                }
            } else if (msg.what == 100) {
                shareTo(link);
            }
        }
    };

    Runnable runnables = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://rrd.me/api.php?url=" + link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setReadTimeout(10000);
                if (conn.getResponseCode() == 200) {
                    InputStream fis = conn.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while ((length = fis.read(bytes)) != -1) {
                        bos.write(bytes, 0, length);
                    }
                    picByte = bos.toByteArray();
                    bos.close();
                    fis.close();

                    Message message = new Message();
                    message.what = 99;
                    handle.sendMessage(message);
                } else {
                    shareTo(link);
                }

            } catch (IOException e) {
                Message message = new Message();
                message.what = 100;
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }
    };

}
