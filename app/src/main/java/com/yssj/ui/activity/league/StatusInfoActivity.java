//package com.yssj.ui.activity.league;
//
//import com.yssj.YJApplication;
//import com.yssj.activity.R;
//import com.yssj.ui.activity.GuideActivity;
//import com.yssj.ui.base.BasicActivity;
//import com.yssj.utils.LogYiFu;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//
//public class StatusInfoActivity extends BasicActivity implements
//		OnCheckedChangeListener,OnClickListener {
//
//	private int index = 0;
//	private String key = "index";
//	private View barView;
//	private ImageView img_search;
//	private LinearLayout img_back;
//	private String strTitle = "ζηδΉ°ε";
//
//	private MyBuyOrderFragment buyFragment;
//	private MySellOrderFragment sellFragment;
//	private FragmentTransaction ft;
//	private FragmentManager fm;
//	private RadioButton rb_sale_orders;
//	private RadioButton rb_pay_orders;
//
//	private RadioGroup rg_orders;
//
//	private String user_id;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_status_info);
//		user_id = getIntent().getSerializableExtra("user_id").toString();
//		//user_id = YCache.getCacheUser(this).getUser_id()+"";
////		aBar.hide();
//		// index = getIntent().getIntExtra(key, 0);
//		initView();
//	}
//
//	private void initView() {
//		rg_orders = (RadioGroup) findViewById(R.id.rg_orders);
//		rg_orders.setOnCheckedChangeListener(this);
//
//		// rb_sale_orders = (RadioButton) findViewById(R.id.rb_sale_orders);
//		// rb_sale_orders.setOnClickListener(this);
//		// rb_pay_orders = (RadioButton) findViewById(R.id.rb_pay_orders);
//		// rb_pay_orders.setOnClickListener(this);
//		img_back = (LinearLayout) findViewById(R.id.img_back);
//		img_back.setOnClickListener(this);
//		img_search = (ImageView) findViewById(R.id.img_search);
//		img_search.setVisibility(View.GONE);
//		img_search.setOnClickListener(this);
//		buyFragment = new MyBuyOrderFragment(0,user_id);
//		sellFragment = new MySellOrderFragment();
//		fm = getSupportFragmentManager();
//		ft = fm.beginTransaction();
//		/*
//		 * if (strTitle.equals("ζηδΉ°ε")) { rb_pay_orders.setChecked(true);
//		 * ft.add(R.id.container_f, buyFragment); } else {// ζηεε
//		 * rb_sale_orders.setChecked(true); ft.add(R.id.container_f,
//		 * sellFragment); }
//		 */
//		ft.add(R.id.container_f, buyFragment);
//		ft.commit();
//	}
//	@Override
//	protected void onResume() {
//		super.onResume();
////		MobclickAgent.onResume(this);
//	}
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
////		YJApplication.getLoader().stop();
////		MobclickAgent.onPause(this);
//	}
//	@Override
//	public void onClick(View v) {
//
//		ft = fm.beginTransaction();
//		switch (v.getId()) {
//		case R.id.img_back:
//			setResult(1);
//			MyBuyOrderFragment.pos = -1;
//			finish();
//			break;
//		case R.id.img_search:// ζη΄’
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		MyBuyOrderFragment.pos = -1;
//	}
//
//	@Override
//	public void onCheckedChanged(RadioGroup arg0, int arg1) {
//		// TODO Auto-generated method stub
//		ft = fm.beginTransaction();
//		switch (arg1) {
//		case R.id.rb_pay_orders:
//			buyFragment = new MyBuyOrderFragment(0,user_id);
//			ft.replace(R.id.container_f, buyFragment);
//			ft.commitAllowingStateLoss();
//			break;
//		case R.id.rb_sale_orders:
//			sellFragment = new MySellOrderFragment();
//			ft.replace(R.id.container_f, sellFragment);
//			ft.commitAllowingStateLoss();
//			break;
//
//		default:
//			break;
//		}
//	}
//
//
//	@Override
//	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(arg0, arg1, arg2);
//		LogYiFu.i("TAG", "ε·ζ°ζ°ζ?");
//		if (arg0 == 101) { // εΎδ»ζ¬Ύηι’ε·ζ°
//			fm.beginTransaction()
//					.replace(R.id.container_f, new MyBuyOrderFragment(1,user_id))
//					.commitAllowingStateLoss();
//		} else if (arg0 == 102) {// ε¨ι¨ηι’ε·ζ°
//			LogYiFu.i("TAG", "ε¨ι¨ηι’ε·ζ°");
//			fm.beginTransaction()
//					.replace(R.id.container_f, new MyBuyOrderFragment(0,user_id))
//					.commitAllowingStateLoss();
//		} else if (arg0 == 103) { // δ»θ―¦ζιζ―δ»ε?ζ ε·ζ°εΎδ»ζ¬Ύ
//			fm.beginTransaction()
//					.replace(R.id.container_f, new MyBuyOrderFragment(1,user_id))
//					.commitAllowingStateLoss();
//		} else if (arg0 == 104) { // δ»θ―¦ζζ―δ»ε?ζοΌε·ζ°ε¨ι¨ηι’
//			fm.beginTransaction()
//			.replace(R.id.container_f, new MyBuyOrderFragment(0,user_id))
//			.commitAllowingStateLoss();
//		}
//	}
//
//	public interface OnStatusListener{
//		public void setOnStatus(int pos);
//	}
//
//}
