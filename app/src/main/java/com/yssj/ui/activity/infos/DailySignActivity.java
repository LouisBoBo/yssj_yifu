//package com.yssj.ui.activity.infos;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
//import java.security.spec.MGF1ParameterSpec;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.FragmentActivity;
//import android.text.TextUtils;
//import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.view.WindowManager;
//import android.widget.Adapter;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.bean.SocializeEntity;
//import com.umeng.socialize.bean.StatusCode;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
//import com.umeng.socialize.media.UMImage;
//import com.yssj.Constants;
//import com.yssj.YConstance;
//import com.yssj.YJApplication;
//import com.yssj.YUrl;
//import com.yssj.YConstance.Pref;
//import com.yssj.activity.R;
//import com.yssj.app.AppManager;
//import com.yssj.app.SAsyncTask;
//import com.yssj.custom.view.NewPDialog;
//import com.yssj.entity.Store;
//import com.yssj.entity.UserInfo;
//import com.yssj.model.ComModel2;
//import com.yssj.ui.activity.MainFragment;
//import com.yssj.ui.activity.MainMenuActivity;
//import com.yssj.ui.activity.TaskMineLikeActivity;
//import com.yssj.ui.activity.shopdetails.NoShareActivity;
//import com.yssj.ui.base.BasicActivity;
//import com.yssj.ui.dialog.PublicToastDialog;
//import com.yssj.ui.fragment.MineShopFragment;
//import com.yssj.utils.MD5Tools;
//import com.yssj.utils.LogYiFu;
//import com.yssj.utils.QRCreateUtil;
//import com.yssj.utils.ShareUtil;
//import com.yssj.utils.SharedPreferencesUtil;
//import com.yssj.utils.ToastUtil;
//import com.yssj.utils.YCache;
//
//public class DailySignActivity extends BasicActivity implements OnClickListener {
//	private String shop_code;
//
//	private TextView tv_sign_intergral;
//	private LinearLayout img_back;
//	private TextView ll_sign_add;
//	private Button btn_right;
//	private boolean Task12, Task13, Task14, Task15, Task16, Task17, Task7, Task8, Task9, Task10, Task11, Task18, Task19,
//			Task20, Task21, Task22, Task23, Task24, Task25, Task26;
//
//	private TextView tv_one_day, tv_two_day, tv_three_day, tv_four_day, tv_five_day, tv_six_day, tv_seven_day;
//
//	private LinearLayout myorder;
//
//	private TextView integral_income, integral_outcome;
//	private TextView tv_undo1, tv_undo2, tv_undo3, tv_undo4, tv_undo5, tv_undo6, tv_undo7, tv_undo8, tv_undo9,
//			tv_undo10;
//	private ImageView img_right1, img_right2, img_right3, img_right4, img_right5, img_right6, img_right7, img_right8,
//			img_right9, img_right10;
//
//	private TextView tv_newer_undo1, tv_newer_undo2, tv_newer_undo3, tv_newer_undo4, tv_newer_undo5, tv_newer_undo6,
//			tv_newer_undo7, tv_newer_undo8, tv_newer_undo9, tv_newer_undo10;
//
//	private RelativeLayout rel_morning_share_cloth, rel_noon_share_cloth, rel_add_shop2store, rel_buy_0_shop,
//			rel_buy_nomal_shop, rel_change_model, rel_publish_new_announce, rel_update_carousel, rel_update_faver,
//			rel_share_my_store;
//
//	private RelativeLayout rel_open_store, rel_first_share_shop, rel_replace_model, rel_first_update_my_store_name,
//			rel_replace_store_pic, rel_first_publish_announce, rel_first_replace_carousel, rel_first_replace_faver,
//			rel_first_buy_nomal_shop, rel_perfect_preferences;// ????????????
//	private int dayOfweek;
//	private Store store;
//
//	private String token;
//
//	private Integer signDays, is_sign, intergrals;
//
//	private MyPopupwindow myPopupwindow;
//	private ListView mListView;
//	private MyAdapter mAdapter;
//	private int count;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.my_integral_sign);
//		shareWaitDialog = new PublicToastDialog(this, R.style.DialogStyle1,"");
//		Calendar cal = Calendar.getInstance();
//		dayOfweek = cal.get(Calendar.DAY_OF_WEEK);
//		store = YCache.getCacheStore(this);
//		token = YCache.getCacheToken(this);
//		aBar.hide();
//		for (int i = 0; i < 10; i++) {
//			mListNewTask.add("" + i);
//		}
//		initView();
//		initData();
//		// bindEmail();
//		// bindPhone();
//
//	}
//
//	private List<String> mListNewTask = new ArrayList<String>();
//
//	public class MyAdapter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			if (mListNewTask.size() % 2 == 0) {
//				count = mListNewTask.size() / 2;
//			} else {
//				count = mListNewTask.size() / 2 + 1;
//			}
//			return count;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			View view;
//			view = View.inflate(context, R.layout.item_new_task, null);
//			RelativeLayout mRlTask = (RelativeLayout) view.findViewById(R.id.rl_task);
//			RelativeLayout mRlTask2 = (RelativeLayout) view.findViewById(R.id.rl_task2);
//			ImageView mPic = (ImageView) view.findViewById(R.id.img_pic);
//			TextView mPoint = (TextView) view.findViewById(R.id.tv_point);
//			TextView mName = (TextView) view.findViewById(R.id.tv_name);
//			TextView mReward = (TextView) view.findViewById(R.id.tv_reward);
//			ImageView mPic2 = (ImageView) view.findViewById(R.id.img_pic2);
//			TextView mPoint2 = (TextView) view.findViewById(R.id.tv_point2);
//			TextView mName2 = (TextView) view.findViewById(R.id.tv_name2);
//			TextView mReward2 = (TextView) view.findViewById(R.id.tv_reward2);
//			if (position == count - 1 && mListNewTask.size() % 2 != 0) {
//				mRlTask2.setBackgroundResource(R.drawable.white_fanzao);
//				// ????????????listView???????????????
//				mRlTask2.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//
//					}
//				});
//			}
//			if ("0".equals(mListNewTask.get(position * 2))) {
//				mPic.setBackgroundResource(R.drawable.img_open_my_store);
//				mName.setText("??????????????????");
//				mReward.setText("??????:300??????");
//				mPoint.setBackgroundResource(R.drawable.img_undo_red_point);
//				mRlTask.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						if (Task17 == true) {
//							ToastUtil.showShortText(context, "???????????????");
//							return;
//						}
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//				});
//			}
//			if ("1".equals(mListNewTask.get(position * 2))) {
//				mPic.setBackgroundResource(R.drawable.img_first_share_shop);
//				mName.setText("??????????????????????????????");
//				mReward.setText("??????:300??????");
//				mPoint.setBackgroundResource(R.drawable.img_undo_red_point);
//				mRlTask.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						if (Task18 == true) {
//							ToastUtil.showShortText(context, "???????????????");
//							return;
//						}  
//						
//						
//						//???????????????????????????
//						// ????????????
//						ToastUtil.showShortText(context, "???????????????????????????~");
//						 share();
////						showMyPopuWindow(DailySignActivity.this);
//
//					}
//				});
//			}
//			if ("2".equals(mListNewTask.get(position * 2))) {
//				mPic.setBackgroundResource(R.drawable.img_replace_model);
//				mName.setText("????????????????????????");
//				mReward.setText("??????:50??????");
//				mPoint.setBackgroundResource(R.drawable.img_undo_red_point);
//				mRlTask.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						UserInfo userInfo;
//						userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//						if (Task19 == true) {
//							ToastUtil.showShortText(context, "???????????????");
//							return;
//						}
//						if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//							NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//							dialog.setL(new NewPDialog.TaskLintener() {
//
//								@Override
//								public void onOKClickLintener() {
//									// ??????
//									AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//									if (MainMenuActivity.instances != null)
//										((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//												.findFragmentByTag("tag")).setIndex(0);
//
//								}
//
//								@Override
//								public void onShouZhiClickLintener() {
//									// TODO Auto-generated method stub
//
//								}
//							});
//							dialog.show();
//							return;
//						}
//
//						// ??????h5??????????????????
//						ModelModify();
//
//					}
//				});
//			}
//			if ("3".equals(mListNewTask.get(position * 2))) {
//				mPic.setBackgroundResource(R.drawable.img_first_update_my_store_name);
//				mName.setText("????????????????????????");
//				mReward.setText("??????:50??????");
//				mPoint.setBackgroundResource(R.drawable.img_undo_red_point);
//				mRlTask.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						UserInfo userInfo;
//						userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//						if (Task20 == true) {
//							ToastUtil.showShortText(context, "???????????????");
//							return;
//						}
//						if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//							NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//							dialog.setL(new NewPDialog.TaskLintener() {
//
//								@Override
//								public void onOKClickLintener() {
//									// ??????
//									AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//									if (MainMenuActivity.instances != null)
//										((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//												.findFragmentByTag("tag")).setIndex(0);
//
//								}
//
//								@Override
//								public void onShouZhiClickLintener() {
//									// TODO Auto-generated method stub
//
//								}
//							});
//							dialog.show();
//							return;
//						}
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//						if (((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//								.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")) == null) {
//							MineShopFragment.url = YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/editName.html"
//									+ "?realm=" + store.getRealm() + "&token=" + token + "&isAndroid=true";
//						} else {
//							((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1"))
//											.getWebview()
//											.loadUrl(YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/editName.html"
//													+ "?realm=" + store.getRealm() + "&token=" + token
//													+ "&isAndroid=true");
//						}
//
//					}
//				});
//			}
//			if ("4".equals(mListNewTask.get(position * 2))) {
//				mPic.setBackgroundResource(R.drawable.img_replace_store_pic);
//				mName.setText("????????????????????????");
//				mReward.setText("??????:25??????");
//				mPoint.setBackgroundResource(R.drawable.img_undo_red_point);
//				mRlTask.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						UserInfo userInfo;
//						userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//						if (Task21 == true) {
//							ToastUtil.showShortText(context, "???????????????");
//							return;
//						}
//						if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//							NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//							dialog.setL(new NewPDialog.TaskLintener() {
//
//								@Override
//								public void onOKClickLintener() {
//									// ??????
//									AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//									if (MainMenuActivity.instances != null)
//										((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//												.findFragmentByTag("tag")).setIndex(0);
//
//								}
//
//								@Override
//								public void onShouZhiClickLintener() {
//									// TODO Auto-generated method stub
//
//								}
//							});
//							dialog.show();
//							return;
//						}
//
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//						if (((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//								.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")) == null) {
//							MineShopFragment.url = YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/edit.html" + "?realm="
//									+ store.getRealm() + "&token=" + token + "&isAndroid=true";
//						} else {
//							((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1"))
//											.getWebview()
//											.loadUrl(YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/edit.html" + "?realm="
//													+ store.getRealm() + "&token=" + token + "&isAndroid=true");
//						}
//
//					}
//				});
//			}
//			if ("5".equals(mListNewTask.get(position * 2))) {
//				mPic.setBackgroundResource(R.drawable.img_first_publish_announce);
//				mName.setText("?????????????????????????????????????????????");
//				mReward.setText("??????:50??????");
//				mPoint.setBackgroundResource(R.drawable.img_undo_red_point);
//				mRlTask.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						UserInfo userInfo;
//						userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//						if (Task22 == true) {
//							ToastUtil.showShortText(context, "???????????????");
//							return;
//						}
//						if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//							NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//							dialog.setL(new NewPDialog.TaskLintener() {
//
//								@Override
//								public void onOKClickLintener() {
//									// ??????
//									AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//									if (MainMenuActivity.instances != null)
//										((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//												.findFragmentByTag("tag")).setIndex(0);
//
//								}
//
//								@Override
//								public void onShouZhiClickLintener() {
//									// TODO Auto-generated method stub
//
//								}
//							});
//							dialog.show();
//							return;
//						}
//						NoticeModify();
//
//					}
//				});
//			}
//			if ("6".equals(mListNewTask.get(position * 2))) {
//				mPic.setBackgroundResource(R.drawable.img_first_replace_carousel);
//				mName.setText("??????????????????????????????????????????????????????");
//				mReward.setText("??????:50??????");
//				mPoint.setBackgroundResource(R.drawable.img_undo_red_point);
//				mRlTask.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						UserInfo userInfo;
//						userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//						if (Task23 == true) {
//							ToastUtil.showShortText(context, "???????????????");
//							return;
//						}
//						if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//							NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//							dialog.setL(new NewPDialog.TaskLintener() {
//
//								@Override
//								public void onOKClickLintener() {
//									// ??????
//									AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//									if (MainMenuActivity.instances != null)
//										((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//												.findFragmentByTag("tag")).setIndex(0);
//
//								}
//
//								@Override
//								public void onShouZhiClickLintener() {
//									// TODO Auto-generated method stub
//
//								}
//							});
//							dialog.show();
//							return;
//						}
//						DailySignActivity.this.getSharedPreferences("EverydayTaskMondayFriday", Context.MODE_PRIVATE)
//								.edit().putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//						// ???????????????
//						FhufflingModify();
//
//					}
//				});
//			}
//			if ("7".equals(mListNewTask.get(position * 2))) {
//				mPic.setBackgroundResource(R.drawable.img_first_replace_faver);
//				mName.setText("?????????????????????????????????????????????????????????");
//				mReward.setText("??????:50??????");
//				mPoint.setBackgroundResource(R.drawable.img_undo_red_point);
//				mRlTask.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						UserInfo userInfo;
//						userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//						if (Task24 == true) {
//							ToastUtil.showShortText(context, "???????????????");
//							return;
//						}
//						if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//							NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//							dialog.setL(new NewPDialog.TaskLintener() {
//
//								@Override
//								public void onOKClickLintener() {
//									// ??????
//									AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//									if (MainMenuActivity.instances != null)
//										((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//												.findFragmentByTag("tag")).setIndex(0);
//
//								}
//
//								@Override
//								public void onShouZhiClickLintener() {
//									// TODO Auto-generated method stub
//
//								}
//							});
//							dialog.show();
//							return;
//						}
//						FavoriteModify();
//
//					}
//				});
//			}
//			if ("8".equals(mListNewTask.get(position * 2))) {
//				mPic.setBackgroundResource(R.drawable.img_first_buy_nomal_shop);
//				mName.setText("????????????????????????");
//				mReward.setText("??????:300??????");
//				mPoint.setBackgroundResource(R.drawable.img_undo_red_point);
//				mRlTask.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						if (Task25 == true) {
//							ToastUtil.showShortText(context, "???????????????");
//							return;
//						}
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(1);
//
//					}
//				});
//			}
//			if ("9".equals(mListNewTask.get(position * 2))) {
//				mPic.setBackgroundResource(R.drawable.perfect_preferences);
//				mName.setText("????????????\n??????????????????????????????");
//				mReward.setText("??????:100??????");
//				mPoint.setBackgroundResource(R.drawable.img_undo_red_point);
//				mRlTask.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						UserInfo userInfo;
//						userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//						if (Task26 == true) {
//							ToastUtil.showShortText(context, "???????????????");
//							return;
//						}
//						// ????????????????????????
//						if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//							// ??????
//							AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//							if (MainMenuActivity.instances != null)
//								((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//										.findFragmentByTag("tag")).setIndex(0);
//
//							return;
//						}
//						// ??????????????????
//						Intent it = new Intent(DailySignActivity.this, TaskMineLikeActivity.class);
//						startActivity(it);
//
//					}
//				});
//			}
//			if (position != count - 1 || mListNewTask.size() % 2 == 0) {
//				if ("0".equals(mListNewTask.get(position * 2 + 1))) {
//					mPic2.setBackgroundResource(R.drawable.img_open_my_store);
//					mName2.setText("??????????????????");
//					mReward2.setText("??????:300??????");
//					mPoint2.setBackgroundResource(R.drawable.img_undo_red_point);
//					mRlTask2.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							if (Task17 == true) {
//								ToastUtil.showShortText(context, "???????????????");
//								return;
//							}
//							// ??????
//							AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//							if (MainMenuActivity.instances != null)
//								((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//										.findFragmentByTag("tag")).setIndex(0);
//
//						}
//					});
//				}
//				if ("1".equals(mListNewTask.get(position * 2 + 1))) {
//					mPic2.setBackgroundResource(R.drawable.img_first_share_shop);
//					mName2.setText("??????????????????????????????");
//					mReward2.setText("??????:300??????");
//					mPoint2.setBackgroundResource(R.drawable.img_undo_red_point);
//					mRlTask2.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							if (Task18 == true) {
//								ToastUtil.showShortText(context, "???????????????");
//								return;
//							}
//							// ????????????
//							 share();
////							showMyPopuWindow(DailySignActivity.this);
//
//						}
//					});
//				}
//				if ("2".equals(mListNewTask.get(position * 2 + 1))) {
//					mPic2.setBackgroundResource(R.drawable.img_replace_model);
//					mName2.setText("????????????????????????");
//					mReward2.setText("??????:50??????");
//					mPoint2.setBackgroundResource(R.drawable.img_undo_red_point);
//					mRlTask2.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							UserInfo userInfo;
//							userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//							if (Task19 == true) {
//								ToastUtil.showShortText(context, "???????????????");
//								return;
//							}
//							if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//								NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//								dialog.setL(new NewPDialog.TaskLintener() {
//
//									@Override
//									public void onOKClickLintener() {
//										// ??????
//										AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//										if (MainMenuActivity.instances != null)
//											((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//													.findFragmentByTag("tag")).setIndex(0);
//
//									}
//
//									@Override
//									public void onShouZhiClickLintener() {
//										// TODO Auto-generated method stub
//
//									}
//								});
//								dialog.show();
//								return;
//							}
//
//							// ??????h5??????????????????
//							ModelModify();
//
//						}
//					});
//				}
//				if ("3".equals(mListNewTask.get(position * 2 + 1))) {
//					mPic2.setBackgroundResource(R.drawable.img_first_update_my_store_name);
//					mName2.setText("????????????????????????");
//					mReward2.setText("??????:50??????");
//					mPoint2.setBackgroundResource(R.drawable.img_undo_red_point);
//					mRlTask2.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							UserInfo userInfo;
//							userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//							if (Task20 == true) {
//								ToastUtil.showShortText(context, "???????????????");
//								return;
//							}
//							if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//								NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//								dialog.setL(new NewPDialog.TaskLintener() {
//
//									@Override
//									public void onOKClickLintener() {
//										// ??????
//										AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//										if (MainMenuActivity.instances != null)
//											((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//													.findFragmentByTag("tag")).setIndex(0);
//
//									}
//
//									@Override
//									public void onShouZhiClickLintener() {
//										// TODO Auto-generated method stub
//
//									}
//								});
//								dialog.show();
//								return;
//							}
//							AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//							if (MainMenuActivity.instances != null)
//								((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//										.findFragmentByTag("tag")).setIndex(0);
//							if (((MineShopFragment) ((MainFragment) MainMenuActivity.instances
//									.getSupportFragmentManager().findFragmentByTag("tag")).getChildFragmentManager()
//											.findFragmentByTag("1")) == null) {
//								MineShopFragment.url = YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/editName.html"
//										+ "?realm=" + store.getRealm() + "&token=" + token + "&isAndroid=true";
//							} else {
//								((MineShopFragment) ((MainFragment) MainMenuActivity.instances
//										.getSupportFragmentManager().findFragmentByTag("tag")).getChildFragmentManager()
//												.findFragmentByTag("1")).getWebview().loadUrl(
//														YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/editName.html"
//																+ "?realm=" + store.getRealm() + "&token=" + token
//																+ "&isAndroid=true");
//							}
//
//						}
//					});
//				}
//				if ("4".equals(mListNewTask.get(position * 2 + 1))) {
//					mPic2.setBackgroundResource(R.drawable.img_replace_store_pic);
//					mName2.setText("????????????????????????");
//					mReward2.setText("??????:25??????");
//					mPoint2.setBackgroundResource(R.drawable.img_undo_red_point);
//					mRlTask2.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							UserInfo userInfo;
//							userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//							if (Task21 == true) {
//								ToastUtil.showShortText(context, "???????????????");
//								return;
//							}
//							if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//								NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//								dialog.setL(new NewPDialog.TaskLintener() {
//
//									@Override
//									public void onOKClickLintener() {
//										// ??????
//										AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//										if (MainMenuActivity.instances != null)
//											((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//													.findFragmentByTag("tag")).setIndex(0);
//
//									}
//
//									@Override
//									public void onShouZhiClickLintener() {
//										// TODO Auto-generated method stub
//
//									}
//								});
//								dialog.show();
//								return;
//							}
//
//							AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//							if (MainMenuActivity.instances != null)
//								((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//										.findFragmentByTag("tag")).setIndex(0);
//							if (((MineShopFragment) ((MainFragment) MainMenuActivity.instances
//									.getSupportFragmentManager().findFragmentByTag("tag")).getChildFragmentManager()
//											.findFragmentByTag("1")) == null) {
//								MineShopFragment.url = YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/edit.html"
//										+ "?realm=" + store.getRealm() + "&token=" + token + "&isAndroid=true";
//							} else {
//								((MineShopFragment) ((MainFragment) MainMenuActivity.instances
//										.getSupportFragmentManager().findFragmentByTag("tag")).getChildFragmentManager()
//												.findFragmentByTag("1"))
//														.getWebview()
//														.loadUrl(YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/edit.html"
//																+ "?realm=" + store.getRealm() + "&token=" + token
//																+ "&isAndroid=true");
//							}
//
//						}
//					});
//				}
//				if ("5".equals(mListNewTask.get(position * 2 + 1))) {
//					mPic2.setBackgroundResource(R.drawable.img_first_publish_announce);
//					mName2.setText("?????????????????????????????????????????????");
//					mReward2.setText("??????:50??????");
//					mPoint2.setBackgroundResource(R.drawable.img_undo_red_point);
//					mRlTask2.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							UserInfo userInfo;
//							userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//							if (Task22 == true) {
//								ToastUtil.showShortText(context, "???????????????");
//								return;
//							}
//							if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//								NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//								dialog.setL(new NewPDialog.TaskLintener() {
//
//									@Override
//									public void onOKClickLintener() {
//										// ??????
//										AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//										if (MainMenuActivity.instances != null)
//											((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//													.findFragmentByTag("tag")).setIndex(0);
//
//									}
//
//									@Override
//									public void onShouZhiClickLintener() {
//										// TODO Auto-generated method stub
//
//									}
//								});
//								dialog.show();
//								return;
//							}
//							NoticeModify();
//
//						}
//					});
//				}
//				if ("6".equals(mListNewTask.get(position * 2 + 1))) {
//					mPic2.setBackgroundResource(R.drawable.img_first_replace_carousel);
//					mName2.setText("??????????????????????????????????????????????????????");
//					mReward2.setText("??????:50??????");
//					mPoint2.setBackgroundResource(R.drawable.img_undo_red_point);
//					mRlTask2.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							UserInfo userInfo;
//							userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//							if (Task23 == true) {
//								ToastUtil.showShortText(context, "???????????????");
//								return;
//							}
//							if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//								NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//								dialog.setL(new NewPDialog.TaskLintener() {
//
//									@Override
//									public void onOKClickLintener() {
//										// ??????
//										AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//										if (MainMenuActivity.instances != null)
//											((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//													.findFragmentByTag("tag")).setIndex(0);
//
//									}
//
//									@Override
//									public void onShouZhiClickLintener() {
//										// TODO Auto-generated method stub
//
//									}
//								});
//								dialog.show();
//								return;
//							}
//							DailySignActivity.this
//									.getSharedPreferences("EverydayTaskMondayFriday", Context.MODE_PRIVATE).edit()
//									.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//							// ???????????????
//							FhufflingModify();
//
//						}
//					});
//				}
//				if ("7".equals(mListNewTask.get(position * 2 + 1))) {
//					mPic2.setBackgroundResource(R.drawable.img_first_replace_faver);
//					mName2.setText("?????????????????????????????????????????????????????????");
//					mReward2.setText("??????:50??????");
//					mPoint2.setBackgroundResource(R.drawable.img_undo_red_point);
//					mRlTask2.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							UserInfo userInfo;
//							userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//							if (Task24 == true) {
//								ToastUtil.showShortText(context, "???????????????");
//								return;
//							}
//							if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//								NewPDialog dialog = new NewPDialog(DailySignActivity.this, R.layout.task_dialog4);
//								dialog.setL(new NewPDialog.TaskLintener() {
//
//									@Override
//									public void onOKClickLintener() {
//										// ??????
//										AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//										if (MainMenuActivity.instances != null)
//											((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//													.findFragmentByTag("tag")).setIndex(0);
//
//									}
//
//									@Override
//									public void onShouZhiClickLintener() {
//										// TODO Auto-generated method stub
//
//									}
//								});
//								dialog.show();
//								return;
//							}
//							FavoriteModify();
//
//						}
//					});
//				}
//				if ("8".equals(mListNewTask.get(position * 2+1))) {
//					mPic2.setBackgroundResource(R.drawable.img_first_buy_nomal_shop);
//					mName2.setText("????????????????????????");
//					mReward2.setText("??????:300??????");
//					mPoint2.setBackgroundResource(R.drawable.img_undo_red_point);
//					mRlTask2.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							if (Task25 == true) {
//								ToastUtil.showShortText(context, "???????????????");
//								return;
//							}
//							AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//							if (MainMenuActivity.instances != null)
//								((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//										.findFragmentByTag("tag")).setIndex(1);
//
//						}
//					});
//				}
//				if ("9".equals(mListNewTask.get(position * 2 + 1))) {
//					mPic2.setBackgroundResource(R.drawable.perfect_preferences);
//					mName2.setText("????????????\n??????????????????????????????");
//					mReward2.setText("??????:100??????");
//					mPoint2.setBackgroundResource(R.drawable.img_undo_red_point);
//					mRlTask2.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							UserInfo userInfo;
//							userInfo = YCache.getCacheUserSafe(DailySignActivity.this);
//							if (Task26 == true) {
//								ToastUtil.showShortText(context, "???????????????");
//								return;
//							}
//							// ????????????????????????
//							if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//								// ??????
//								AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//								if (MainMenuActivity.instances != null)
//									((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//											.findFragmentByTag("tag")).setIndex(0);
//
//								return;
//							}
//							// ??????????????????
//							Intent it = new Intent(DailySignActivity.this, TaskMineLikeActivity.class);
//							startActivity(it);
//						}
//					});
//				}
//			}
//			return view;
//		}
//
//	}
//
//	private void initView() {
//		findViewById(R.id.img_back).setOnClickListener(this);
//		((TextView) findViewById(R.id.tvTitle_base)).setText("????????????");
//		mListView = (ListView) findViewById(R.id.grid_view_task);
//		View view = View.inflate(context, R.layout.item_new_task_headview, null);
//		mListView.addHeaderView(view);
//		tv_sign_intergral = (TextView) findViewById(R.id.tv_my_intergral);
//		myorder = (LinearLayout) findViewById(R.id.myorder);
//		myorder.setBackgroundColor(Color.WHITE);
//		tv_one_day = (TextView) findViewById(R.id.tv_one_day);
//		tv_two_day = (TextView) findViewById(R.id.tv_two_day);
//		tv_three_day = (TextView) findViewById(R.id.tv_three_day);
//		tv_four_day = (TextView) findViewById(R.id.tv_four_day);
//		tv_five_day = (TextView) findViewById(R.id.tv_five_day);
//		tv_six_day = (TextView) findViewById(R.id.tv_six_day);
//		tv_seven_day = (TextView) findViewById(R.id.tv_seven_day);
//
//		ll_sign_add = (TextView) findViewById(R.id.ll_sign_add);
//		ll_sign_add.setOnClickListener(this);
//
//		btn_right = (Button) findViewById(R.id.btn_right);
//		btn_right.setVisibility(View.GONE);
//
//		img_back = (LinearLayout) findViewById(R.id.img_back);
//		img_back.setOnClickListener(this);
//
//		// ????????????
//		rel_morning_share_cloth = (RelativeLayout) findViewById(R.id.rel_morning_share_cloth);
//		rel_morning_share_cloth.setOnClickListener(this);// ??????????????????
//		rel_noon_share_cloth = (RelativeLayout) findViewById(R.id.rel_noon_share_cloth);
//		rel_noon_share_cloth.setOnClickListener(this);// ??????????????????
//		rel_add_shop2store = (RelativeLayout) findViewById(R.id.rel_add_shop2store);
//		rel_add_shop2store.setOnClickListener(this);// ?????????????????????
//		rel_buy_0_shop = (RelativeLayout) findViewById(R.id.rel_buy_0_shop);
//		rel_buy_0_shop.setOnClickListener(this);// ??????0?????????????????????
//		rel_buy_nomal_shop = (RelativeLayout) findViewById(R.id.rel_buy_nomal_shop);
//		rel_buy_nomal_shop.setOnClickListener(this);// ???????????????????????????
//		rel_change_model = (RelativeLayout) findViewById(R.id.rel_change_model);
//		rel_change_model.setOnClickListener(this);// ????????????
//		rel_publish_new_announce = (RelativeLayout) findViewById(R.id.rel_publish_new_announce);
//		rel_publish_new_announce.setOnClickListener(this);// ???????????????
//		rel_update_carousel = (RelativeLayout) findViewById(R.id.rel_update_carousel);
//		rel_update_carousel.setOnClickListener(this);// ??????????????????
//		rel_update_faver = (RelativeLayout) findViewById(R.id.rel_update_faver);
//		rel_update_faver.setOnClickListener(this);// ??????????????????
//		rel_share_my_store = (RelativeLayout) findViewById(R.id.rel_share_my_store);
//		rel_share_my_store.setOnClickListener(this);// ??????????????????
//		rel_perfect_preferences = (RelativeLayout) findViewById(R.id.rel_perfect_preferences);
//		rel_perfect_preferences.setOnClickListener(this);// ????????????
//
//		// ????????????
//		rel_open_store = (RelativeLayout) findViewById(R.id.rel_open_store);
//		rel_open_store.setOnClickListener(this);// ????????????
//		rel_first_share_shop = (RelativeLayout) findViewById(R.id.rel_first_share_shop);
//		rel_first_share_shop.setOnClickListener(this);// ?????????????????????
//		rel_replace_model = (RelativeLayout) findViewById(R.id.rel_replace_model);
//		rel_replace_model.setOnClickListener(this);// ??????????????????
//		rel_first_update_my_store_name = (RelativeLayout) findViewById(R.id.rel_first_update_my_store_name);
//		rel_first_update_my_store_name.setOnClickListener(this);// ??????????????????????????????
//		rel_replace_store_pic = (RelativeLayout) findViewById(R.id.rel_replace_store_pic);
//		rel_replace_store_pic.setOnClickListener(this);
//		rel_first_publish_announce = (RelativeLayout) findViewById(R.id.rel_first_publish_announce);
//		rel_first_publish_announce.setOnClickListener(this);
//		rel_first_replace_carousel = (RelativeLayout) findViewById(R.id.rel_first_replace_carousel);
//		rel_first_replace_carousel.setOnClickListener(this);
//		rel_first_replace_faver = (RelativeLayout) findViewById(R.id.rel_first_replace_faver);
//		rel_first_replace_faver.setOnClickListener(this);
//		rel_first_buy_nomal_shop = (RelativeLayout) findViewById(R.id.rel_first_buy_nomal_shop);
//		rel_first_buy_nomal_shop.setOnClickListener(this);
//
//		findViewById(R.id.lin_integral_income).setOnClickListener(this);
//		findViewById(R.id.lin_integral_outcome).setOnClickListener(this);
//
//		integral_income = (TextView) findViewById(R.id.integral_income);// ??????????????????
//		integral_outcome = (TextView) findViewById(R.id.integral_outcome);// ??????????????????
//
//		tv_undo1 = (TextView) findViewById(R.id.tv_undo1);
//		img_right1 = (ImageView) findViewById(R.id.img_right1);
//		tv_undo2 = (TextView) findViewById(R.id.tv_undo2);
//		img_right2 = (ImageView) findViewById(R.id.img_right2);
//		tv_undo3 = (TextView) findViewById(R.id.tv_undo3);
//		img_right3 = (ImageView) findViewById(R.id.img_right3);
//		tv_undo4 = (TextView) findViewById(R.id.tv_undo4);
//		img_right4 = (ImageView) findViewById(R.id.img_right4);
//		tv_undo5 = (TextView) findViewById(R.id.tv_undo5);
//		img_right5 = (ImageView) findViewById(R.id.img_right5);
//		tv_undo6 = (TextView) findViewById(R.id.tv_undo6);
//		img_right6 = (ImageView) findViewById(R.id.img_right6);
//		tv_undo7 = (TextView) findViewById(R.id.tv_undo7);
//		img_right7 = (ImageView) findViewById(R.id.img_right7);
//		tv_undo8 = (TextView) findViewById(R.id.tv_undo8);
//		img_right8 = (ImageView) findViewById(R.id.img_right8);
//		tv_undo9 = (TextView) findViewById(R.id.tv_undo9);
//		img_right9 = (ImageView) findViewById(R.id.img_right9);
//		tv_undo10 = (TextView) findViewById(R.id.tv_undo10);
//		img_right10 = (ImageView) findViewById(R.id.img_right10);
//
//		tv_newer_undo1 = (TextView) findViewById(R.id.tv_newer_undo1);
//		tv_newer_undo2 = (TextView) findViewById(R.id.tv_newer_undo2);
//		tv_newer_undo3 = (TextView) findViewById(R.id.tv_newer_undo3);
//		tv_newer_undo4 = (TextView) findViewById(R.id.tv_newer_undo4);
//		tv_newer_undo5 = (TextView) findViewById(R.id.tv_newer_undo5);
//		tv_newer_undo6 = (TextView) findViewById(R.id.tv_newer_undo6);
//		tv_newer_undo7 = (TextView) findViewById(R.id.tv_newer_undo7);
//		tv_newer_undo8 = (TextView) findViewById(R.id.tv_newer_undo8);
//		tv_newer_undo9 = (TextView) findViewById(R.id.tv_newer_undo9);
//		tv_newer_undo10 = (TextView) findViewById(R.id.tv_newer_undo10);
//		setVisible();
//	}
//
//	private void setVisible() {
//		// Date curDates = new Date(System.currentTimeMillis());// ??????????????????
//		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//		// String strs = sdf.format(curDates);
//		// // ?????????????????? ?????????
//		// String[] dds = strs.split(":");
//		currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//		switch (dayOfweek) {
//
//		case 1:// ?????????
//		case 7:
//			rel_change_model.setVisibility(View.GONE);
//			rel_publish_new_announce.setVisibility(View.GONE);
//			rel_update_carousel.setVisibility(View.GONE);
//			rel_update_faver.setVisibility(View.GONE);
//			rel_share_my_store.setVisibility(View.GONE);
//			break;
//		case 2:// ?????????
//			rel_publish_new_announce.setVisibility(View.GONE);
//			rel_update_carousel.setVisibility(View.GONE);
//			rel_update_faver.setVisibility(View.GONE);
//			rel_share_my_store.setVisibility(View.GONE);
//			break;
//		case 3:// ?????????
//			rel_change_model.setVisibility(View.GONE);
//			rel_update_carousel.setVisibility(View.GONE);
//			rel_update_faver.setVisibility(View.GONE);
//			rel_share_my_store.setVisibility(View.GONE);
//			break;
//		case 4:// ?????????
//			rel_change_model.setVisibility(View.GONE);
//			rel_publish_new_announce.setVisibility(View.GONE);
//			rel_update_faver.setVisibility(View.GONE);
//			rel_share_my_store.setVisibility(View.GONE);
//			break;
//		case 5:
//			rel_change_model.setVisibility(View.GONE);
//			rel_publish_new_announce.setVisibility(View.GONE);
//			rel_update_carousel.setVisibility(View.GONE);
//			rel_share_my_store.setVisibility(View.GONE);
//			break;
//		case 6:
//			rel_change_model.setVisibility(View.GONE);
//			rel_publish_new_announce.setVisibility(View.GONE);
//			rel_update_carousel.setVisibility(View.GONE);
//			rel_update_faver.setVisibility(View.GONE);
//			break;
//		default:
//			break;
//		}
//
//		if (currentHour >= 14) {
//			tv_undo1.setVisibility(View.GONE);
//		} else {
//			tv_undo2.setVisibility(View.GONE);
//		}
//
//	}
//
//	private int currentHour;
//
//	private void initData() {
//		new SAsyncTask<Void, Void, HashMap<String, Object>>(this, R.string.wait) {
//
//			@Override
//			protected boolean isHandleException() {
//				// TODO Auto-generated method stub
//				return true;
//			}
//
//			@Override
//			protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
//					throws Exception {
//				// TODO Auto-generated method stub
//				return ComModel2.queryMyIntegral(context);
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
//				super.onPostExecute(context, result, e);
//				if (null == e) {
//					setData(result);
//					List<String> list = (List<String>) result.get("fulfill");
//					if (list != null && result.get("everyDayFulfill") != null) {
//						list.addAll((List<String>) result.get("everyDayFulfill"));
//					}
//					YJApplication.instance.setTaskMap(list);
//
//					mAdapter = new MyAdapter();
//					mListView.setAdapter(mAdapter);
//				}
//			}
//
//		}.execute();
//	}
//
//	private void setData(HashMap<String, Object> result) {
//		intergrals = (Integer) result.get("integral");
//		signDays = (Integer) result.get("signDay");
//
//		if (null == result.get("income")) {
//			integral_income.setText("0");
//		} else {
//			integral_income.setText((Integer) result.get("income") + "");
//		}
//
//		if (null == result.get("expense")) {
//			integral_outcome.setText("0");
//		} else {
//			integral_outcome.setText((Integer) result.get("expense") + "");
//		}
//		if (null == intergrals || intergrals == 0) {
//			tv_sign_intergral.setText("0");
//
//		} else {
//			int intergral = intergrals;
//			if ((intergral + "").length() > 7) {
//				tv_sign_intergral.setTextSize(16.0f);
//			}
//			tv_sign_intergral.setText("?????????\n" + intergrals);
//
//		}
//		if (signDays == 1) {
//			tv_one_day.setBackgroundResource(R.drawable.sign_day);
//			tv_one_day.setTextColor(android.graphics.Color.WHITE);
//			// tv_sign_everyday.setText("10");
//		} else if (signDays == 2) {
//			tv_one_day.setBackgroundResource(R.drawable.sign_day);
//			tv_one_day.setTextColor(android.graphics.Color.WHITE);
//			tv_two_day.setBackgroundResource(R.drawable.sign_day);
//			tv_two_day.setTextColor(android.graphics.Color.WHITE);
//			// tv_sign_everyday.setText("15");
//		} else if (signDays == 3) {
//			tv_one_day.setBackgroundResource(R.drawable.sign_day);
//			tv_one_day.setTextColor(android.graphics.Color.WHITE);
//			tv_two_day.setBackgroundResource(R.drawable.sign_day);
//			tv_two_day.setTextColor(android.graphics.Color.WHITE);
//			tv_three_day.setBackgroundResource(R.drawable.sign_day);
//			tv_three_day.setTextColor(android.graphics.Color.WHITE);
//			// tv_sign_everyday.setText("25");
//		} else if (signDays == 4) {
//			tv_one_day.setBackgroundResource(R.drawable.sign_day);
//			tv_one_day.setTextColor(android.graphics.Color.WHITE);
//			tv_two_day.setBackgroundResource(R.drawable.sign_day);
//			tv_two_day.setTextColor(android.graphics.Color.WHITE);
//			tv_three_day.setBackgroundResource(R.drawable.sign_day);
//			tv_three_day.setTextColor(android.graphics.Color.WHITE);
//			tv_four_day.setBackgroundResource(R.drawable.sign_day);
//			tv_four_day.setTextColor(android.graphics.Color.WHITE);
//			// tv_sign_everyday.setText("50");
//		} else if (signDays == 5) {
//			tv_one_day.setBackgroundResource(R.drawable.sign_day);
//			tv_one_day.setTextColor(android.graphics.Color.WHITE);
//			tv_two_day.setBackgroundResource(R.drawable.sign_day);
//			tv_two_day.setTextColor(android.graphics.Color.WHITE);
//			tv_three_day.setBackgroundResource(R.drawable.sign_day);
//			tv_three_day.setTextColor(android.graphics.Color.WHITE);
//			tv_four_day.setBackgroundResource(R.drawable.sign_day);
//			tv_four_day.setTextColor(android.graphics.Color.WHITE);
//			tv_five_day.setBackgroundResource(R.drawable.sign_day);
//			tv_five_day.setTextColor(android.graphics.Color.WHITE);
//			// tv_sign_everyday.setText("100");
//		} else if (signDays == 6) {
//			tv_one_day.setBackgroundResource(R.drawable.sign_day);
//			tv_one_day.setTextColor(android.graphics.Color.WHITE);
//			tv_two_day.setBackgroundResource(R.drawable.sign_day);
//			tv_two_day.setTextColor(android.graphics.Color.WHITE);
//			tv_three_day.setBackgroundResource(R.drawable.sign_day);
//			tv_three_day.setTextColor(android.graphics.Color.WHITE);
//			tv_four_day.setBackgroundResource(R.drawable.sign_day);
//			tv_four_day.setTextColor(android.graphics.Color.WHITE);
//			tv_five_day.setBackgroundResource(R.drawable.sign_day);
//			tv_five_day.setTextColor(android.graphics.Color.WHITE);
//			tv_six_day.setBackgroundResource(R.drawable.sign_day);
//			tv_six_day.setTextColor(android.graphics.Color.WHITE);
//			// tv_sign_everyday.setText("200");
//		} else if (signDays == 7) {
//			tv_one_day.setBackgroundResource(R.drawable.sign_day);
//			tv_one_day.setTextColor(android.graphics.Color.WHITE);
//			tv_two_day.setBackgroundResource(R.drawable.sign_day);
//			tv_two_day.setTextColor(android.graphics.Color.WHITE);
//			tv_three_day.setBackgroundResource(R.drawable.sign_day);
//			tv_three_day.setTextColor(android.graphics.Color.WHITE);
//			tv_four_day.setBackgroundResource(R.drawable.sign_day);
//			tv_four_day.setTextColor(android.graphics.Color.WHITE);
//			tv_five_day.setBackgroundResource(R.drawable.sign_day);
//			tv_five_day.setTextColor(android.graphics.Color.WHITE);
//			tv_six_day.setBackgroundResource(R.drawable.sign_day);
//			tv_six_day.setTextColor(android.graphics.Color.WHITE);
//			tv_seven_day.setBackgroundResource(R.drawable.sign_day);
//			tv_seven_day.setTextColor(android.graphics.Color.WHITE);
//			// tv_sign_everyday.setText("5");
//		}
//		List<String> list = (List<String>) result.get("fulfill");
//		for (String i : list) {
//			switch (Integer.valueOf(i)) {
//			case 17:
//				tv_newer_undo1.setVisibility(View.GONE);
//				mListNewTask.remove("0");
//				// rel_open_store.setClickable(false);
//				Task17 = true;
//				break;
//			case 18:
//				tv_newer_undo2.setVisibility(View.GONE);
//				// rel_first_share_shop.setClickable(false);
//				Task18 = true;
//				mListNewTask.remove("1");
//				break;
//			case 19:
//				tv_newer_undo3.setVisibility(View.GONE);
//				// rel_replace_model.setClickable(false);
//				Task19 = true;
//				mListNewTask.remove("2");
//				break;
//			case 20:
//				tv_newer_undo4.setVisibility(View.GONE);
//				// rel_first_update_my_store_name.setClickable(false);
//				Task20 = true;
//				mListNewTask.remove("3");
//				break;
//
//			case 21:
//				tv_newer_undo5.setVisibility(View.GONE);
//				// rel_replace_store_pic.setClickable(false);
//				Task21 = true;
//				mListNewTask.remove("4");
//				break;
//			case 22:
//				tv_newer_undo6.setVisibility(View.GONE);
//				// rel_first_publish_announce.setClickable(false);
//				Task22 = true;
//				mListNewTask.remove("5");
//				break;
//			case 23:
//				tv_newer_undo7.setVisibility(View.GONE);
//				// rel_first_replace_carousel.setClickable(false);
//				Task23 = true;
//				mListNewTask.remove("6");
//				break;
//			case 24:
//				tv_newer_undo8.setVisibility(View.GONE);
//				// rel_first_replace_faver.setClickable(false);
//				Task24 = true;
//				mListNewTask.remove("7");
//				break;
//			case 25:
//				tv_newer_undo9.setVisibility(View.GONE);
//				// rel_first_buy_nomal_shop.setClickable(false);
//				Task25 = true;
//				mListNewTask.remove("8");
//				break;
//			case 26:
//				tv_newer_undo10.setVisibility(View.GONE);
//				Task26 = true;
//				mListNewTask.remove("9");
//			default:
//				break;
//			}
//		}
//
//		List<String> listDailySign = (List<String>) result.get("everyDayFulfill");
//		for (String i : listDailySign) {
//			switch (Integer.valueOf(i)) {
//			case 7:
//				tv_undo1.setVisibility(View.GONE);
//				img_right1.setImageResource(R.drawable.img_right_alreadydo);
//				// rel_morning_share_cloth.setClickable(false);
//				Task7 = true;
//				break;
//			case 8:
//				tv_undo2.setVisibility(View.GONE);
//				img_right2.setImageResource(R.drawable.img_right_alreadydo);
//				// rel_noon_share_cloth.setClickable(false);
//				Task8 = true;
//				break;
//			case 9:
//				tv_undo3.setVisibility(View.GONE);
//				img_right3.setImageResource(R.drawable.img_right_alreadydo);
//				// rel_add_shop2store.setClickable(false);
//				Task9 = true;
//				break;
//			case 10:
//				tv_undo4.setVisibility(View.GONE);
//				img_right4.setImageResource(R.drawable.img_right_alreadydo);
//				// rel_buy_0_shop.setClickable(false);
//				Task10 = true;
//				break;
//
//			case 11:
//				tv_undo5.setVisibility(View.GONE);
//				img_right5.setImageResource(R.drawable.img_right_alreadydo);
//				// rel_buy_nomal_shop.setClickable(false);
//				Task11 = true;
//				break;
//			case 12:
//				tv_undo6.setVisibility(View.GONE);
//				img_right6.setImageResource(R.drawable.img_right_alreadydo);
//				// rel_change_model.setClickable(false);
//				Task12 = true;
//				break;
//			case 13:
//				tv_undo7.setVisibility(View.GONE);
//				img_right7.setImageResource(R.drawable.img_right_alreadydo);
//				// rel_publish_new_announce.setClickable(false);
//				Task13 = true;
//				break;
//			case 14:
//				tv_undo8.setVisibility(View.GONE);
//				img_right8.setImageResource(R.drawable.img_right_alreadydo);
//				// rel_update_carousel.setClickable(false);
//				Task14 = true;
//				break;
//			case 15:
//				tv_undo9.setVisibility(View.GONE);
//				img_right9.setImageResource(R.drawable.img_right_alreadydo);
//				// rel_update_faver.setClickable(false);
//				Task15 = true;
//				break;
//			case 16:
//				tv_undo10.setVisibility(View.GONE);
//				img_right10.setImageResource(R.drawable.img_right_alreadydo);
//				// rel_share_my_store.setClickable(false);
//				Task15 = true;
//				break;
//
//			default:
//				break;
//			}
//		}
//
//	}
//
//	private void dailySign(View v) {
//		if ("0".equals(is_sign)) {
//			ToastUtil.showShortText(context, "?????????????????????????????????");
//			return;
//		}
//		new SAsyncTask<Void, Void, HashMap<String, Object>>(DailySignActivity.this, v, R.string.wait) {
//
//			@Override
//			protected HashMap<String, Object> doInBackground(FragmentActivity context, Void... params)
//					throws Exception {
//				return ComModel2.dailySign(context);
//			}
//
//			@Override
//			protected boolean isHandleException() {
//				return true;
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context, HashMap<String, Object> result, Exception e) {
//				super.onPostExecute(context, result, e);
//				if (null == e) {
//					if (result != null && "1".equals(result.get("status"))) {
//						is_sign = 0;
//						if (signDays == 7) {
//							signDays = 1;
//						} else {
//							signDays += 1;
//						}
//						intergrals = (Integer) result.get("integral");
//						ToastUtil.showShortText(context, "???????????????");
//						initData();
//					} else {
//						showToast("??????????????????~~~");
//					}
//				}
//			}
//
//		}.execute((Void[]) null);
//	}
//	private PublicToastDialog shareWaitDialog = null;
//	@Override
//	public void onClick(View arg0) {
//		super.onClick(arg0);
//		Intent intent = null;
//		UserInfo userInfo;
//		userInfo = YCache.getCacheUserSafe(this);
//		switch (arg0.getId()) {
//		case R.id.ll_sign_add:
//			dailySign(arg0);
//			break;
//		case R.id.img_back:
//			finish();
//			break;
//		case R.id.lin_integral_income:// ????????????
//			intent = new Intent(context, IntergralDetailActivity.class);
//			intent.putExtra("page", 0);
//			startActivity(intent);
//			break;
//		case R.id.lin_integral_outcome:// ????????????
//			intent = new Intent(context, IntergralDetailActivity.class);
//			intent.putExtra("page", 1);
//			startActivity(intent);
//			break;
//		case R.id.rel_morning_share_cloth:// ??????????????????????????????
//			if (Task7 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (currentHour > 14 || currentHour == 14) {
//				ToastUtil.showShortText(context, "?????????????????????????????????");
//				return;
//			}
//			this.getSharedPreferences("EveryDayShareAm", Context.MODE_PRIVATE).edit()
//					.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//			// ????????????
//			
////			ToastUtil.showShortText(context, "???????????????????????????~");
//			share();
////			showMyPopuWindow(this);
//			break;
//		case R.id.rel_noon_share_cloth:// ??????????????????????????????
//			if (Task8 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if ((currentHour > 7 || currentHour == 7) && currentHour < 14) {
//				ToastUtil.showShortText(context, "?????????????????????????????????");
//				return;
//			}
//			this.getSharedPreferences("EveryDaySharePm", Context.MODE_PRIVATE).edit()
//					.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//			// ????????????
////			ToastUtil.showShortText(context, "???????????????????????????~");
//			 share();
////			showMyPopuWindow(this);
//			break;
//		case R.id.rel_add_shop2store:// ??????5???????????????????????????
//			if (Task9 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//			this.getSharedPreferences("EverydayTaskMondayFriday", Context.MODE_PRIVATE).edit()
//					.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//			// ????????????
//			AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//			if (MainMenuActivity.instances != null)
//				((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//						.setIndex(1);
//			break;
//		case R.id.rel_buy_0_shop:// ??????0?????????????????????
//			if (Task10 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			// ??????0??????
//			AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//			if (MainMenuActivity.instances != null)
//				((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//						.setIndex(3);
//			break;
//		case R.id.rel_buy_nomal_shop:// ?????????????????????????????????
//			if (Task11 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			// ????????????
//			AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//			if (MainMenuActivity.instances != null)
//				((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//						.setIndex(1);
//			break;
//		case R.id.rel_change_model:// ???????????????????????????
//			if (Task12 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//			this.getSharedPreferences("EverydayTaskMondayFriday", Context.MODE_PRIVATE).edit()
//					.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//			// ???????????????
//			ModelModify();
//			break;
//
//		case R.id.rel_publish_new_announce:// ?????????????????????????????????
//			if (Task13 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//			this.getSharedPreferences("EverydayTaskMondayFriday", Context.MODE_PRIVATE).edit()
//					.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//			// ???????????????
//			NoticeModify();
//
//			break;
//		case R.id.rel_update_carousel:// ????????????????????????????????????
//			if (Task14 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//			this.getSharedPreferences("EverydayTaskMondayFriday", Context.MODE_PRIVATE).edit()
//					.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//			// ???????????????
//			FhufflingModify();
//			break;
//		case R.id.rel_update_faver:// ???????????????????????????????????????
//			if (Task15 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//			this.getSharedPreferences("EverydayTaskMondayFriday", Context.MODE_PRIVATE).edit()
//					.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//			// ???????????????
//			FavoriteModify();
//			break;
//		case R.id.rel_share_my_store:// ??????????????????????????????
//			if (Task16 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//			// ????????????
//			AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//			if (MainMenuActivity.instances != null)
//				((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//						.setIndex(0);
//			break;
//		case R.id.rel_open_store:// ??????????????????
//			if (Task17 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			// ??????
//			AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//			if (MainMenuActivity.instances != null)
//				((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//						.setIndex(0);
//
//			break;
//
//		case R.id.rel_perfect_preferences:// ????????????
//			/*
//			 * if (Task17 == true) { ToastUtil.showShortText(context, "???????????????");
//			 * return; } // ????????????
//			 * AppManager.getAppManager().finishAllActivityOfEveryDayTask(); if
//			 * (MainMenuActivity.instances != null) ((MainFragment)
//			 * MainMenuActivity.instances
//			 * .getSupportFragmentManager().findFragmentByTag("tag"))
//			 * .setIndex(0);
//			 */
//			// ????????????????????????
//			if (Task26 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			// ????????????????????????
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				// ??????
//				AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//				if (MainMenuActivity.instances != null)
//					((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//							.setIndex(0);
//
//				return;
//			}
//			// ??????????????????
//			Intent it = new Intent(DailySignActivity.this, TaskMineLikeActivity.class);
//			startActivity(it);
//
//			break;
//
//		case R.id.rel_first_share_shop:// ??????????????????????????????
//			if (Task18 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			// ????????????
//			// share();
//			showMyPopuWindow(this);
//			break;
//		case R.id.rel_replace_model:// ????????????????????????
//			if (Task19 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//
//			// ??????h5??????????????????
//			ModelModify();
//			break;
//		case R.id.rel_first_update_my_store_name:// ????????????????????????
//			if (Task20 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//			AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//			if (MainMenuActivity.instances != null)
//				((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//						.setIndex(0);
//			if (((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//					.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")) == null) {
//				MineShopFragment.url = YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/editName.html" + "?realm="
//						+ store.getRealm() + "&token=" + token + "&isAndroid=true";
//			} else {
//				((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//						.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")).getWebview()
//								.loadUrl(YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/editName.html" + "?realm="
//										+ store.getRealm() + "&token=" + token + "&isAndroid=true");
//			}
//			break;
//		case R.id.rel_replace_store_pic:// ????????????????????????
//			if (Task21 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//
//			AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//			if (MainMenuActivity.instances != null)
//				((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//						.setIndex(0);
//			if (((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//					.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")) == null) {
//				MineShopFragment.url = YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/edit.html" + "?realm="
//						+ store.getRealm() + "&token=" + token + "&isAndroid=true";
//			} else {
//				((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//						.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")).getWebview()
//								.loadUrl(YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/edit.html" + "?realm="
//										+ store.getRealm() + "&token=" + token + "&isAndroid=true");
//			}
//
//			break;
//		case R.id.rel_first_publish_announce:// ?????????????????????????????????????????????
//			if (Task22 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//			NoticeModify();
//			break;
//		case R.id.rel_first_replace_carousel:// ???????????????????????????
//			if (Task23 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//			this.getSharedPreferences("EverydayTaskMondayFriday", Context.MODE_PRIVATE).edit()
//					.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//			// ???????????????
//			FhufflingModify();
//
//			break;
//		case R.id.rel_first_replace_faver:// ?????????????????????????????????????????????????????????
//			if (Task24 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			if (null == userInfo.getHobby() || userInfo.getHobby().equals("0")) {
//				NewPDialog dialog = new NewPDialog(this, R.layout.task_dialog4);
//				dialog.setL(new NewPDialog.TaskLintener() {
//
//					@Override
//					public void onOKClickLintener() {
//						// ??????
//						AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//						if (MainMenuActivity.instances != null)
//							((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//									.findFragmentByTag("tag")).setIndex(0);
//
//					}
//
//					@Override
//					public void onShouZhiClickLintener() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//				dialog.show();
//				return;
//			}
//			FavoriteModify();
//			break;
//		case R.id.rel_first_buy_nomal_shop:// ????????????????????????
//			if (Task25 == true) {
//				ToastUtil.showShortText(context, "???????????????");
//				return;
//			}
//			AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//			if (MainMenuActivity.instances != null)
//				((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//						.setIndex(1);
//			break;
//
//		}
//	}
//
//	/** ????????????????????? */
//
//	public void share() {
//		ShareUtil.configPlatforms(context);// ????????????????????????
//		new SAsyncTask<String, Void, HashMap<String, String>>((FragmentActivity) this, R.string.wait) {
//
//			
//
//			@Override
//			protected HashMap<String, String> doInBackground(FragmentActivity context, String... params)
//					throws Exception {
//				// TODO Auto-generated method stub
//				return ComModel2.getShareShopLink(context, "");
//			}
//
//			@Override
//			protected boolean isHandleException() {
//				return true;
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context, HashMap<String, String> result, Exception e) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(context, result, e);
//				if (null == e) {
//					if (result.get("status").equals("1")) {
//						shareWaitDialog.show();
//						LogYiFu.e("pic", result.get("shop_pic"));
//						String[] picList = result.get("shop_pic").split(",");
//						String link = result.get("link");
//						
//						shop_code = result.get("shop_code");
//						
////						download(null, picList, result.get("shop_code"), result.get("shop_name"), result, link);
//						getNineBmBg(null, picList, result.get("shop_code"), result.get("shop_name"), result, link);
//					} else if (result.get("status").equals("1050")) {// ??????
//						Intent intent = new Intent(context, NoShareActivity.class);
//						intent.putExtra("isNomal", true);
//						context.startActivity(intent); // ?????????????????????
//					}
//				}
//			}
//
//		}.execute();
//	}
//	
//	private  String[]  picListNine;
//	private String shop_codeNine;
//	private String shop_nameNine;
//	private HashMap<String, String> mapInfosNine;
//	private String linkNine;
//	/**
//	 * ???????????????????????????????????????
//	 * @param context
//	 */
//	public void getNineBmBg(View v, final String[] picList, final String shop_code, final String shop_name,
//			final HashMap<String, String> mapInfos, final String link){ 
//		new SAsyncTask<Void, Void, String>(DailySignActivity.this, R.string.wait) {
//			@Override
//			protected boolean isHandleException() {
//				// TODO Auto-generated method stub
//				return true;
//			}
//
//			@Override
//			protected String doInBackground(FragmentActivity context, Void... params)
//					throws Exception {
//				// TODO Auto-generated method stub
//				return ComModel2.getShareBg(context);
//
//			}
//
//			protected void onPostExecute(FragmentActivity context, String result, Exception e) {
//				super.onPostExecute(context, result, e);
////						Bitmap bmNineBg = downloadPic(result);
////						download(null, picList, shop_code, shop_name,mapInfos, link,bmNineBg);
//					picListNine = picList;
//					shop_codeNine = shop_code;
//					shop_nameNine = shop_name;
//					mapInfosNine = mapInfos;
//					linkNine = link;
//					getPicture(result);
//				}
//			}.execute();
//	} 
//	private byte[] picByte;
//	private Bitmap nineBitmap;
//	
//	public void getPicture(final String picPath) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				if(TextUtils.isEmpty(picPath)){
//					Message message = new Message();
//					message.what = 99;
//					handle.sendMessage(message);
//				}else{
//				try {
//					URL url = new URL(YUrl.imgurl + picPath);
//					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//					conn.setRequestMethod("GET");
//					conn.setReadTimeout(10000);
//
//					if (conn.getResponseCode() == 200) {
//						InputStream fis = conn.getInputStream();
//						ByteArrayOutputStream bos = new ByteArrayOutputStream();
//						byte[] bytes = new byte[1024];
//						int length = -1;
//						while ((length = fis.read(bytes)) != -1) {
//							bos.write(bytes, 0, length);
//						}
//						picByte = bos.toByteArray();
//						bos.close();
//						fis.close();
//
//						Message message = new Message();
//						message.what = 99;
//						handle.sendMessage(message);
//					}
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				}
//			}
//		}).start();
//	}
//	Handler handle = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (msg.what == 99) {
//				if (picByte != null) {
//					nineBitmap = BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
//					download(null, picListNine, shop_codeNine, shop_nameNine,mapInfosNine, linkNine,nineBitmap);
//				}else{
//					download(null, picListNine, shop_codeNine, shop_nameNine,mapInfosNine, linkNine,null);
//				}
//			}
//		}
//	};
//	
//	private Bitmap bmBg;
//	private void download(View v, final String[] picList, final String shop_code, final String shop_name,
//			final HashMap<String, String> mapInfos, final String link,final Bitmap bmNineBg) {
//		new SAsyncTask<Void, Void, Void>((FragmentActivity) this, v, R.string.wait) {
//
//			@Override
//			protected Void doInBackground(Void... params) {
//				// TODO Auto-generated method stub
//				File fileDirec = new File(YConstance.savePicPath);
//				if (!fileDirec.exists()) {
//					fileDirec.mkdir();
//				}
//				File[] listFiles = new File(YConstance.savePicPath).listFiles();
//				if (listFiles.length != 0) {
//					LogYiFu.e("TAG", "??????????????? ?????????????????????");
//					for (File file : listFiles) {
//						file.delete();
//					}
//				}
//				// LogYiFu.i("TAG", "piclist=" + picList.length);
//				List<String> pics = new ArrayList<String>();
//				for (int j = 0; j < picList.length; j++) {
//					if (!picList[j].contains("reveal_") && !picList[j].contains("detail_")
//							&& !picList[j].contains("real_")) {
//						picList[j] = shop_code.substring(1, 4) + "/" + shop_code + "/" + picList[j];
//						LogYiFu.e("??????", picList[j]);
//						pics.add(picList[j]);
//					}
//				}
//				int j = pics.size() + 1;
//				if (pics.size() > 8) {
//					j = 9;
//				}
//				int nP = j>5?4:j-1;
//				for (int i = 0; i < j; i++) {
//					if (i == nP) {
//						/*
//						 * ComModel2.saveQRCode(PaymentSuccessActivity.this,
//						 * shop_code);
//						 */
////						Bitmap bm = QRCreateUtil.createImage(mapInfos.get("QrLink"), 500, 700,
////								mapInfos.get("shop_se_price"), DailySignActivity.this);// ?????????????????????
////						QRCreateUtil.saveBitmap(bm, YConstance.savePicPath, MD5Tools.md5(String.valueOf(9)) + ".jpg");// ?????????????????????
//						// ???????????????????????????
////						Bitmap bmQr = QRCreateUtil.createQrImage(mapInfos.get("QrLink"), 250, 250);
////						Bitmap bm = QRCreateUtil.drawNewBitmapNine(DailySignActivity.this, bmQr,
////								(String) mapInfos.get("shop_se_price"), false);
//						Bitmap bmQr = QRCreateUtil.createQrImage(mapInfos.get("QrLink"), 190, 190);
//						Bitmap bm = QRCreateUtil.drawNewBitmapNine2(DailySignActivity.this, bmQr,bmNineBg);
//						QRCreateUtil.saveBitmap(bm, YConstance.savePicPath,
//								MD5Tools.md5(String.valueOf(i)) + ".jpg");// ?????????????????????
//						// downloadPic(mapInfos.get("qr_pic"), 9);
//						continue;
//					}
//					int m  = i>4?i-1:i;
//					downloadPic(pics.get(m) + "!450", i);
//				}
//				bmBg = downloadPic(
//							shop_code.substring(1, 4) + "/" + shop_code + "/" + mapInfos.get("four_pic").toString().split(",")[2] + "!450");
//				
//				return super.doInBackground(params);
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context, Void result) {
//				// TODO Auto-generated method stub
//				// showShareDialog();
//				// MyLogYiFu.e("TAG", "????????????=" + shop.getShop_name() + ",????????????=" +
//				// result);
//				if (null != DailySignActivity.this && null != context.getWindow().getDecorView()) {
//					shareWaitDialog.dismiss();
//					UMImage umImage = new UMImage(context, bmBg);
////					ShareUtil.setShareContent(context, umImage, shop_name, link);
//					ShareUtil.setShareContent(context, umImage, "????????????????????????????????????????????????????????????????????????~", link);
//					
//					// ShareUtil.share(ShopDetailsActivity.this);
//					// showMyPopuWindow(context);
//					// qq??????
////					if (myPopupwindow.getShareId() == R.id.iv_qq_share) {
////						myPopupwindow.shareToQQ();
////						
////						yunYunTongJi(shop_code, 104, 2);
////						System.out.println("??????shop_code="+shop_code);
////					}
////					// ?????????????????????
////					if (myPopupwindow.getShareId() == R.id.iv_wxin_share) {
//						shareToWXin();
////						
////						yunYunTongJi(shop_code, 1, 2);
////						System.out.println("?????????shop_code="+shop_code);
////					}
//					
//					
//					
//				}
//				super.onPostExecute(context, result);
//
//			}
//
//		}.execute();
//	}
//	private Bitmap downloadPic(String picPath) {
//		try {
//			URL url = new URL(YUrl.imgurl + picPath);
//			// ????????????
//			URLConnection con = url.openConnection();
//			// ?????????????????????
//			int contentLength = con.getContentLength();
////			System.out.println("?????? :" + contentLength);
//			// ?????????
//			InputStream is = con.getInputStream();
//			// 1K???????????????
//			byte[] bs = new byte[8192];
//			// ????????????????????????
//			int len;
//			BitmapDrawable bmpDraw = new BitmapDrawable(is);
//
//			// ???????????????????????????
//			is.close();
//			return bmpDraw.getBitmap();
//		} catch (Exception e) {
//			LogYiFu.e("TAG", "????????????");
//
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//	/**
//	 * ?????????????????????popuwindow
//	 * 
//	 * @param context
//	 */
//	private void showMyPopuWindow(FragmentActivity context) {
////		myPopupwindow = new MyPopupwindow(context, 0, 1);
//		if (!DailySignActivity.this.isFinishing() && null != DailySignActivity.this.getWindow().getDecorView()) {
//			myPopupwindow.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//		}
//
//	}
//
//	private void downloadPic(String picPath, int i) {
//		try {
//			URL url = new URL(YUrl.imgurl + picPath);
//			// ????????????
//			URLConnection con = url.openConnection();
//			// ?????????????????????
//			int contentLength = con.getContentLength();
//			// ?????????
//			InputStream is = con.getInputStream();
//			// 1K???????????????
//			byte[] bs = new byte[8192];
//			// ????????????????????????
//			int len;
//			// ?????????????????? /sdcard/yssj/
//			File file = new File(YConstance.savePicPath, MD5Tools.md5(String.valueOf(i)) + ".jpg");
//			if (file.exists()) {
//				file.delete();
//			}
//			LogYiFu.e("TAG", "??????????????????????????????????????????");
//			OutputStream os = new FileOutputStream(file);
//			// ????????????
//			while ((len = is.read(bs)) != -1) {
//				os.write(bs, 0, len);
//			}
//			LogYiFu.i("TAG", "?????????????????????file=" + file.toString());
//			// ???????????????????????????
//			os.close();
//			is.close();
//		} catch (Exception e) {
//			LogYiFu.e("TAG", "????????????");
//			e.printStackTrace();
//		}
//	}
//
//	// ?????????????????????
//	private void ModelModify() {
//		AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//		if (MainMenuActivity.instances != null)
//			((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//					.setIndex(0);
//		if (((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//				.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")) == null) {
//			MineShopFragment.url = YUrl.YSS_URL_ANDROID_H5 + "view/store/selectTheme.html" + "?realm="
//					+ store.getRealm() + "&token=" + token + "&isAndroid=true";
//		} else {
//			((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//					.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")).getWebview()
//							.loadUrl(YUrl.YSS_URL_ANDROID_H5 + "view/store/selectTheme.html" + "?realm="
//									+ store.getRealm() + "&token=" + token + "&isAndroid=true");
//		}
//	}
//
//	private void FhufflingModify() {
//		AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//		if (MainMenuActivity.instances != null)
//			((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//					.setIndex(0);
//		if (((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//				.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")) == null) {
//			MineShopFragment.url = YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/editBanner.html" + "?realm="
//					+ store.getRealm() + "&token=" + token + "&isAndroid=true";
//		} else {
//			((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//					.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")).getWebview()
//							.loadUrl(YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/editBanner.html" + "?realm="
//									+ store.getRealm() + "&token=" + token + "&isAndroid=true");
//		}
//	}
//
//	private void FavoriteModify() {
//		AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//		if (MainMenuActivity.instances != null)
//			((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//					.setIndex(0);
//		if (((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//				.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")) == null) {
//			MineShopFragment.url = YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/setRecommend.html" + "?realm="
//					+ store.getRealm() + "&token=" + token + "&isAndroid=true";
//		} else {
//			((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//					.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")).getWebview()
//							.loadUrl(YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/setRecommend.html" + "?realm="
//									+ store.getRealm() + "&token=" + token + "&isAndroid=true");
//		}
//	}
//
//	private void NoticeModify() {
//		AppManager.getAppManager().finishAllActivityOfEveryDayTask();
//		if (MainMenuActivity.instances != null)
//			((MainFragment) MainMenuActivity.instances.getSupportFragmentManager().findFragmentByTag("tag"))
//					.setIndex(0);
//		if (((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//				.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")) == null) {
//			MineShopFragment.url = YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/editNotice.html" + "?realm="
//					+ store.getRealm() + "&token=" + token + "&isAndroid=true";
//		} else {
//			((MineShopFragment) ((MainFragment) MainMenuActivity.instances.getSupportFragmentManager()
//					.findFragmentByTag("tag")).getChildFragmentManager().findFragmentByTag("1")).getWebview()
//							.loadUrl(YUrl.YSS_URL_ANDROID_H5 + "view/store/theme/editNotice.html" + "?realm="
//									+ store.getRealm() + "&token=" + token + "&isAndroid=true");
//		}
//
//	}
//
//	@Override
//	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//		if (arg0 == 1001) {
//			initData();
//		}
//		super.onActivityResult(arg0, arg1, arg2);
//	}
//
//	class MyPopupwindow extends PopupWindow implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			
//		}
////		private UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR_SHARE);
////		private Activity mActivity;
////
////		private Intent qqShareIntent = ShareUtil.shareMultiplePictureToQZone(ShareUtil.getImage());
////		private Intent wXinShareIntent = ShareUtil.shareMultiplePictureToTimeLine(ShareUtil.getImage());
////		private Intent sinaShareIntent = ShareUtil.shareMultiplePictureToSina(ShareUtil.getImage());
////		private WindowManager.LayoutParams params;
////
////		private double feedBack;
////
////		private boolean isSecondShare;
////
////		private boolean isEveryDayTask;
////
////		private TextView tv_title;
////
////		private int isNoFeed = 0;
////
////		public MyPopupwindow(Activity activity, double feedBack) {//
////			super(activity);
////			this.feedBack = feedBack;
////			this.mActivity = activity;
////			isSecondShare = true;
////			initView(activity);
////		}
////
////		public MyPopupwindow(Activity activity, double feedBack, int isNoFeed) {// ???????????????
////																				// ?????????????????????
////																				// ?????????????????????????????????
////																				// isNoFeed
////																				// =
////																				// 1
////			super(activity);
////			this.feedBack = feedBack;
////			this.mActivity = activity;
////			isSecondShare = true;
////			this.isNoFeed = isNoFeed;
////			initView(activity);
////		}
////
////		public MyPopupwindow(Activity activity, double feedBack, boolean isSecondShare) {
////			this.feedBack = feedBack;
////			this.mActivity = activity;
////			this.isSecondShare = isSecondShare;
////			initView(activity);
////		}
////
////		@SuppressWarnings("deprecation")
////		private void initView(Context context) {
////			View rootView = LayoutInflater.from(context).inflate(R.layout.share_popupwindow, null);
////			rootView.setBackgroundColor(Color.WHITE);
////			rootView.findViewById(R.id.iv_qq_share).setOnClickListener(this);
////			rootView.findViewById(R.id.iv_wxin_share).setOnClickListener(this);
////			rootView.findViewById(R.id.iv_sina_share).setOnClickListener(this);
////			rootView.findViewById(R.id.ll_wxin).setVisibility(View.GONE);
////			// rootView.findViewById(R.id.btn_cancle).setOnClickListener(this);
////			tv_title = (TextView) rootView.findViewById(R.id.tv_title);
////			qqShareIntent = ShareUtil.shareMultiplePictureToQZone(ShareUtil.getImage());
////			wXinShareIntent = ShareUtil.shareMultiplePictureToTimeLine(ShareUtil.getImage());
////			sinaShareIntent = ShareUtil.shareMultiplePictureToSina(ShareUtil.getImage());
////
////			TextView tv_kickback = (TextView) rootView.findViewById(R.id.kick_back);
////			if (feedBack == 0) {
////				tv_kickback.setVisibility(View.GONE);
//////				if (isNoFeed == 0)
//////					tv_title.setText("??????????????????????????????????????????????????????");
//////				else
//////					tv_title.setText("?????????????????????~??????????????????????????????");
////			} else {
////				tv_kickback.setVisibility(View.VISIBLE);
////				tv_title.setText("?????????????????????~??????????????????????????????");
////			}
////			tv_kickback.setText(context.getString(R.string.kick_back, feedBack + ""));
////			setContentView(rootView);
////			setWidth(LayoutParams.MATCH_PARENT);
////			setHeight(LayoutParams.WRAP_CONTENT);
////			setAnimationStyle(R.style.mypopwindow_anim_style);
////			setFocusable(true);
////			setTouchable(true);
////			setBackgroundDrawable(new ColorDrawable(R.color.white));
////			params = mActivity.getWindow().getAttributes();
////			params.alpha = 0.5f;
////			mActivity.getWindow().setAttributes(params);
////			setOnDismissListener(new OnDismissListener() {
////
////				@Override
////				public void onDismiss() {
////					params.alpha = 1.0f;
////					mActivity.getWindow().setAttributes(params);
////				}
////			});
////		}
////
////		private int shareId;
////
////		public int getShareId() {
////			return shareId;
////		}
////
////		public void setShareId(int shareId) {
////			this.shareId = shareId;
////		}
////
////		@Override
////		public void onClick(View v) {
////			ToastUtil.showShortText(mActivity, "???????????????????????????~");
////			shareId = v.getId();
////			share();// ?????????????????????
////			switch (shareId) {
////			case R.id.iv_qq_share:
////			
////				// shareToQQ();
////				// ??????????????????????????? ?????????????????????????????????
////				break;
////			case R.id.iv_wxin_share:
////				
////				// shareToWXin();
////				break;
////			case R.id.iv_sina_share:
////				if (isSecondShare) {// ??????????????????
////					yunYunTongJi(shop_code, 106, 2);
//////					System.out.println("??????shop_code="+shop_code);
////					this.dismiss();
////					onceShare(sinaShareIntent, "????????????");
////					recordShare();
////				} else {
////					performShare(SHARE_MEDIA.SINA, sinaShareIntent);
////				}
////				break;
////			// case R.id.btn_cancel:
////			// dismiss();
////			// break;
////			default:
////				break;
////			}
////		}
////
////		public void shareToWXin() {
////
////			tongjifenxiangCount();
////			if (isSecondShare) {
////				this.dismiss();
////				onceShare(wXinShareIntent, "??????");
////				// recordShare();
////				// String currentTime = DateFormat.format("HH", new
////				// Date(System.currentTimeMillis())).toString();
////				int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
////				if ((hour > 7 || hour == 7) && hour < 14) {
////					recordEveryDayTask("7");
////					mActivity.getSharedPreferences("EveryDayShareAm", Context.MODE_PRIVATE).edit()
////							.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
////				} else if ((hour > 14 || hour == 14)) {
////					recordEveryDayTask("8");
////					mActivity.getSharedPreferences("EveryDaySharePm", Context.MODE_PRIVATE).edit()
////							.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
////				}
////			} else {
////				performShare(SHARE_MEDIA.WEIXIN_CIRCLE, wXinShareIntent);
////			}
////		}
////
////		public void shareToQQ() {
////
////			tongjifenxiangCount();
////
////			if (ShareUtil.intentIsAvailable(mActivity, qqShareIntent)) {
////				onceShare(qqShareIntent, "qq??????");
////				recordShare();
////			} else {
////				performShare(SHARE_MEDIA.QZONE, qqShareIntent);
////			}
////		}
////
////		private void onceShare(Intent intent, String perform) {
////			if (ShareUtil.intentIsAvailable(mActivity, intent)) {
////				mActivity.startActivityForResult(intent, 1001);// (intent);
////			} else {
////				Toast.makeText(mActivity, "????????????" + perform + "?????????", Toast.LENGTH_SHORT).show();
////			}
////		}
////
////		// ???????????????????????????
////		private void recordShare() {
////			new SAsyncTask<Void, Void, Void>((FragmentActivity) mActivity, R.string.wait) {
////
////				@Override
////				protected boolean isHandleException() {
////					// TODO Auto-generated method stub
////					return true;
////				}
////
////				@Override
////				protected Void doInBackground(FragmentActivity context, Void... params) throws Exception {
////					// TODO Auto-generated method stub
////					return ComModel2.getIntegral(context);
////				}
////
////				@Override
////				protected void onPostExecute(FragmentActivity context, Void result, Exception e) {
////					super.onPostExecute(context, result, e);
////				}
////
////			}.execute();
////		}
////
////		private void performShare(SHARE_MEDIA platform, final Intent intent) {
////			mController.postShare(mActivity, platform, new SnsPostListener() {
////
////				@Override
////				public void onStart() {
////
////				}
////
////				@Override
////				public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
////					String showText = platform.toString();
////					if (eCode == StatusCode.ST_CODE_SUCCESSED) {
////						showText += "??????????????????";
////						Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
////						// recordShare();
////						// ??????????????????
////						String currentTime = DateFormat.format("HH", new Date()).toString();
////						int hour = Integer.parseInt(currentTime);
////						if ((hour > 7 || hour == 7) && hour < 14) {
////							recordEveryDayTask("7");
////							mActivity.getSharedPreferences("EveryDayShareAm", Context.MODE_PRIVATE).edit()
////									.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
////						} else if ((hour > 14 || hour == 14) && hour < 20) {
////							recordEveryDayTask("8");
////							mActivity.getSharedPreferences("EveryDaySharePm", Context.MODE_PRIVATE).edit()
////									.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
////						}
////
////						/*
////						 * if (isSecondShare) { if
////						 * (ShareUtil.intentIsAvailable(mActivity, intent)) {
////						 * mActivity.startActivity(intent); } else {
////						 * Toast.makeText(mActivity, "????????????" + showText + "?????????",
////						 * Toast.LENGTH_SHORT).show(); } }
////						 */
////					} else {
////						showText += "??????????????????";
////						Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
////					}
////					dismiss();
////				}
////			});
////		}
////
////		private void recordEveryDayTask(final String id) {
////			new SAsyncTask<Void, Void, Integer>((FragmentActivity) mActivity, R.string.wait) {
////
////				@Override
////				protected boolean isHandleException() {
////					// TODO Auto-generated method stub
////					return true;
////				}
////
////				@Override
////				protected Integer doInBackground(FragmentActivity context, Void... params) throws Exception {
////					// TODO Auto-generated method stub
////					return ComModel2.doMission(context, id);
////				}
////
////				@Override
////				protected void onPostExecute(FragmentActivity context, Integer result, Exception e) {
////					super.onPostExecute(context, result, e);
////				}
////
////			}.execute();
////		}
//	}
//
//	private void tongjifenxiangCount() {
//
//		new SAsyncTask<Integer, Void, String>((FragmentActivity) context, R.string.wait) {
//
//			@Override
//			protected boolean isHandleException() {
//				return true;
//			}
//
//			@Override
//			protected String doInBackground(FragmentActivity context, Integer... params) throws Exception {
//				// TODO Auto-generated method stub
//				return ComModel2.tongjifenxiangshu(context);
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context, String result, Exception e) {
//				super.onPostExecute(context, result, e);
//
//				if (e == null) {
//					LogYiFu.e("????????????????????????", result + "");
//				}
//
//			}
//
//		}.execute();
//	}
//	
//	
//	
//	private void yunYunTongJi(final String shop_code, final int type, final int tab_type) {
//		new SAsyncTask<Void, Void, HashMap<String, String>>(this, R.string.wait) {
//
//			@Override
//			protected boolean isHandleException() {
//				// TODO Auto-generated method stub
//				return true;
//			}
//
//			@Override
//			protected HashMap<String, String> doInBackground(FragmentActivity context, Void... params)
//					throws Exception {
//
//				return ComModel2.getOperator(context, shop_code, type, tab_type);
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context, HashMap<String, String> result, Exception e) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(context, result, e);
//				
//			}
//
//		}.execute();
//	}
//
//	
//	public void shareToWXin() {
//
//		tongjifenxiangCount();
////		if (isSecondShare) {
////			
//		
//		// recordShare();
//		// String currentTime = DateFormat.format("HH", new
//		// Date(System.currentTimeMillis())).toString();
//		wXinShareIntent = ShareUtil.shareMultiplePictureToTimeLine(ShareUtil.getImage());
//		boolean WxCircleFlag = SharedPreferencesUtil.getBooleanData(context, Pref.RECORD_WXCIRCLE, false);
//		if(WxCircleFlag){
//			onceShare(wXinShareIntent, "??????");
//			SharedPreferencesUtil.saveBooleanData(context,Pref.RECORD_WXCIRCLE, false);
//			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//			if ((hour > 7 || hour == 7) && hour < 14) {
//				recordEveryDayTask("7");
//				getApplicationContext().getSharedPreferences("EveryDayShareAm", Context.MODE_PRIVATE).edit()
//						.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//			} else if ((hour > 14 || hour == 14)) {
//				recordEveryDayTask("8");
//				getApplicationContext().getSharedPreferences("EveryDaySharePm", Context.MODE_PRIVATE).edit()
//						.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//			}	
//		}else {
//			SharedPreferencesUtil.saveBooleanData(context,Pref.RECORD_WXCIRCLE, true);
//			performShare(SHARE_MEDIA.WEIXIN_CIRCLE, wXinShareIntent);
//		}
//	}
//	
//	
//	private Intent wXinShareIntent /*= ShareUtil.shareMultiplePictureToTimeLine(ShareUtil.getImage())*/;
//	private UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR_SHARE);
//	private void recordEveryDayTask(final String id) {
//		new SAsyncTask<Void, Void, Integer>((FragmentActivity) this, R.string.wait) {
//
//			@Override
//			protected boolean isHandleException() {
//				// TODO Auto-generated method stub
//				return true;
//			}
//
//			@Override
//			protected Integer doInBackground(FragmentActivity context, Void... params) throws Exception {
//				// TODO Auto-generated method stub
//				return ComModel2.doMission(context, id);
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context, Integer result, Exception e) {
//				super.onPostExecute(context, result, e);
//			}
//
//		}.execute();
//	}
//	
//	
//	private void performShare(SHARE_MEDIA platform, final Intent intent) {
//		mController.postShare(getApplicationContext(), platform, new SnsPostListener() {
//
//			@Override
//			public void onStart() {
//
//			}
//
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//				String showText = platform.toString();
//				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
//					showText += "??????????????????";
//					Toast.makeText(getApplicationContext(), showText, Toast.LENGTH_SHORT).show();
//					// recordShare();
//					// ??????????????????
////					String currentTime = DateFormat.format("HH", new Date()).toString();
////					int hour = Integer.parseInt(currentTime);
//					int  hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//					if ((hour > 7 || hour == 7) && hour < 14) {
//						recordEveryDayTask("7");
//						getApplicationContext().getSharedPreferences("EveryDayShareAm", Context.MODE_PRIVATE).edit()
//								.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//					} else if ((hour > 14 || hour == 14) && hour < 20) {
//						recordEveryDayTask("8");
//						getApplicationContext().getSharedPreferences("EveryDaySharePm", Context.MODE_PRIVATE).edit()
//								.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).commit();
//					}
//
//					/*
//					 * if (isSecondShare) { if
//					 * (ShareUtil.intentIsAvailable(mActivity, intent)) {
//					 * mActivity.startActivity(intent); } else {
//					 * Toast.makeText(mActivity, "????????????" + showText + "?????????",
//					 * Toast.LENGTH_SHORT).show(); } }
//					 */
//				} else {
//					showText += "??????????????????";
//					Toast.makeText(getApplicationContext(), showText, Toast.LENGTH_SHORT).show();
//				}
//				
//			}
//		});
//	}
//	
//	private void onceShare(Intent intent, String perform) {
//		if (ShareUtil.intentIsAvailable(getApplicationContext(), intent)) {
//			this.startActivityForResult(intent, 1001);// (intent);
//		} else {
//			Toast.makeText(getApplicationContext(), "????????????" + perform + "?????????", Toast.LENGTH_SHORT).show();
//		}
//	}
//}
