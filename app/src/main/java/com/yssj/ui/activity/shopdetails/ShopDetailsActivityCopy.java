//package com.yssj.ui.activity.shopdetails;
//
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//
//import android.R.integer;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.view.animation.CycleInterpolator;
//import android.view.animation.TranslateAnimation;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//import com.yssj.YConstance.SizeIndex;
//import com.yssj.YUrl;
//import com.yssj.activity.R;
//import com.yssj.app.SAsyncTask;
//import com.yssj.custom.view.BadgeView;
//import com.yssj.custom.view.StickyScrollView.ScrollViewListener;
//import com.yssj.entity.ReturnInfo;
//import com.yssj.entity.Shop;
//import com.yssj.entity.StockType;
//import com.yssj.entity.Store;
//import com.yssj.entity.UserInfo;
//import com.yssj.model.ComModel;
//import com.yssj.ui.activity.MainActivity;
//import com.yssj.utils.StringUtils;
//import com.yssj.utils.ToastUtil;
//import com.yssj.utils.YCache;
//
//
///***
// * ????????????
// * @author Administrator
// *
// */
//public class ShopDetailsActivity extends FragmentActivity implements OnClickListener , ScrollViewListener {
//
//	
//	private int width , height;
//	private Shop shop ;
//	private TextView tv_clothes_name , tv_sjprive , tv_prive , tv_discount , tv_free_email ,
//					tv_discount_time ,tv_volume,tv_feedback_rate ,tv_like , tv_collect;
//	private TextView tv_xq , tv_cm,tv_pj , tv_shop_car, tv_buy ,tv_back;
//	private ImageView img_shop_cart , img_fenx,img_xin , img_share ,img_header , img_addxin;
//	private BadgeView badgeView;
//	
//	private FragmentManager fm;
//	private FragmentTransaction ft;
//	private EvaluateFragment eFragment ;
//	private DetailsFragment  dFragment;
//	private SizeFragment sFragment;
//	private int frType ; 
//	public ImageLoader imageLoader;
//	private DisplayImageOptions options;
//	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
//	
//	private String shopCarFragment ; 
//	private Store store;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		super.onCreate(savedInstanceState);		
//		setContentView(R.layout.activity_shop_details);
//		getWindownPixes();
//		
//		initView();
//		//initFragment();
//		shopCarFragment = getIntent().getStringExtra("shopCarFragment");
//		String code = getIntent().getStringExtra("code");
//		if (!TextUtils.isEmpty(code)) {
//			queryShopDetails(code);
//		}
//	
//		//queryShopDetails("10");
//	}
//	
//	/***
//	 * ??????????????????????????? ??????
//	 */
//	private void getWindownPixes(){
//		DisplayMetrics  dm = new DisplayMetrics();      
//		getWindowManager().getDefaultDisplay().getMetrics(dm);      
//		width = dm.widthPixels;                
//		height  = dm.heightPixels; 
//
//	}
//	
//	private void initView() {
//		LinearLayout lay_content = (LinearLayout) findViewById(R.id.lay_content);
//		LayoutParams lp = (LayoutParams) lay_content.getLayoutParams();
//		int lay_px = ToastUtil.dip2px(this, 105) ;
//		int lay_height = height - lay_px - getIntent().getIntExtra("height", 0);
//		lp.height = lay_height;
//		lay_content.setLayoutParams(lp);
//		tv_xq = (TextView) findViewById(R.id.tv_xq);
//		tv_xq.setOnClickListener(this);
//		tv_cm = (TextView) findViewById(R.id.tv_cm);
//		tv_cm.setOnClickListener(this);
//		tv_pj = (TextView) findViewById(R.id.tv_pj);
//		tv_pj.setOnClickListener(this);
//		
//		tv_shop_car = (TextView) findViewById(R.id.tv_shop_car);
//		tv_shop_car.setOnClickListener(this);
//		tv_buy = (TextView) findViewById(R.id.tv_buy);
//		tv_buy.setOnClickListener(this);
//		img_shop_cart = (ImageView) findViewById(R.id.img_shop_cart);//?????????
//		img_shop_cart.setOnClickListener(this);
//		badgeView = new BadgeView(this, img_shop_cart);
//		img_fenx = (ImageView) findViewById(R.id.img_fenx);//??????
//		img_fenx.setOnClickListener(this);
//		img_xin = (ImageView) findViewById(R.id.img_xin);//??????
//		img_xin.setOnClickListener(this);
//		tv_clothes_name = (TextView) findViewById(R.id.tv_clothes_name);
//		tv_sjprive = (TextView) findViewById(R.id.tv_sjprive);
//		tv_prive = (TextView) findViewById(R.id.tv_prive);
//		tv_discount = (TextView) findViewById(R.id.tv_discount);
//		tv_free_email = (TextView) findViewById(R.id.tv_free_email);
//		tv_discount_time = (TextView) findViewById(R.id.tv_discount_time);
//		tv_volume = (TextView) findViewById(R.id.tv_volume);
//		tv_feedback_rate  = (TextView) findViewById(R.id.tv_feedback_rate);
//		tv_like = (TextView) findViewById(R.id.tv_like);
//		tv_collect = (TextView) findViewById(R.id.tv_collect);
//		img_share = (ImageView) findViewById(R.id.img_share);
//		tv_back = (TextView) findViewById(R.id.tv_back);
//		tv_back.setOnClickListener(this);
//		img_header = (ImageView) findViewById(R.id.img_header);
//		tv_xq.setTextColor(getResources().getColor(R.color.red));
//		img_addxin = (ImageView) findViewById(R.id.img_addxin);
//		
//	}
//	/***
//	 * ?????????fragment
//	 */
//	private void initFragment() {
//		Bundle bundle = new Bundle();
//		if (shop != null) {
//			bundle.putSerializable("shop", shop);
//		}
//		
//		dFragment = new DetailsFragment();
//		dFragment.setArguments(bundle);
//		sFragment = new SizeFragment();
//		sFragment.setArguments(bundle);
//		eFragment = new EvaluateFragment();
//		eFragment.setArguments(bundle);
//		fm = getSupportFragmentManager();
//		ft = fm.beginTransaction();
//		ft.add(R.id.container, dFragment);
//		ft.add(R.id.container, eFragment);
//		ft.add(R.id.container, sFragment);
//		ft.show(dFragment);
//		ft.hide(eFragment);
//		ft.hide(sFragment);
//
//		ft.commitAllowingStateLoss();
//	}
//	@Override
//	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.tv_back:
//			finish();
//			
//			break;
//		case R.id.tv_xq://??????
//			frType = 0;
//			chooseFragment(frType);
//			break;
//		case R.id.tv_cm://??????
//			frType = 1;
//			chooseFragment(frType);
//			break;
//		case R.id.tv_pj://??????
//			frType = 2;
//			chooseFragment(frType);
//			break;
//		
//		case R.id.tv_shop_car://???????????????
//			//showPopWindow(0);
//			queryShopQueryAttr(1);
//			break;
//		case R.id.tv_buy://????????????
//			//showPopWindow(1);
//			queryShopQueryAttr(0);
//			break;
//		case R.id.img_shop_cart://???????????????
//			if (null!= shopCarFragment && shopCarFragment.equals("shopCarFragment")) {
//				Intent intent2 = new Intent();
//				intent2.putExtra("shopCarFragment", shopCarFragment);
//				setResult(102, intent2);
//			}
//			
//			finish();
//			break;
//		case R.id.img_xin:
//			addLikeShop(img_xin);
//			break;
//		case R.id.img_fenx:
//			break;
//		default:
//			break;
//		}
//	}
//
//	/***
//	 * ?????????????????????????????????
//	 */
//	private void addLikeShop(View v) {
//		if (shop != null) {
//			int like_id = shop.getLike_id();
//			if (like_id == -1) {//??????????????????
//				MyLogYiFu.e("like_id  == ", " "+like_id );
//				MyLogYiFu.e("shop_code  == ", " "+shop.getShop_code());
//				AlphaAnimation _alphaAnimation0 = new AlphaAnimation(1.0f, 0.2f);
//				_alphaAnimation0.setDuration(1000);
//				_alphaAnimation0.setFillAfter(true);// ??????????????????????????????
//				img_addxin.startAnimation(_alphaAnimation0);
//				img_addxin.setVisibility(View.VISIBLE);
//
//				/**
//				 * ???????????????????????????0.2?????????
//				 */
//				AlphaAnimation _alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
//				_alphaAnimation.setDuration(3000);
//				_alphaAnimation.setFillAfter(true);// ??????????????????????????????
//				img_addxin.startAnimation(_alphaAnimation);
//				shakeAnimation(5);
//
//				new SAsyncTask<String, Void, ReturnInfo>(this, v, R.string.wait) {
//
//					@Override
//					protected ReturnInfo doInBackground(FragmentActivity context,
//							String... params) throws Exception {
//
//						return ComModel.addLikeShop(ShopDetailsActivity.this,
//								YCache.getCacheToken(ShopDetailsActivity.this),
//								params[0]);
//					}
//
//					@Override
//					protected void onPostExecute(FragmentActivity context,
//							ReturnInfo result) {
//						img_addxin.clearAnimation();
//						img_addxin.setVisibility(View.GONE);
//						if (null != result) {
//							Toast.makeText(ShopDetailsActivity.this, "????????????????????????",
//									Toast.LENGTH_SHORT).show();
//							img_xin.setBackgroundResource(R.drawable.hx0);
//							shop.setLike_id(1);
//							
//						} else {
//							Toast.makeText(ShopDetailsActivity.this, "????????????????????????",
//									Toast.LENGTH_SHORT).show();
//						}
//
//						super.onPostExecute(context, result);
//					}
//
//				}.execute(shop.getShop_code());
//			}else {//??????????????????
//				new SAsyncTask<String, Void, ReturnInfo>(this, v, R.string.wait) {
//
//					@Override
//					protected ReturnInfo doInBackground(FragmentActivity context,
//							String... params) throws Exception {
//
//						return ComModel.deleteLikeShop(ShopDetailsActivity.this,
//								YCache.getCacheToken(ShopDetailsActivity.this),
//								params[0]);
//					}
//
//					@Override
//					protected void onPostExecute(FragmentActivity context,
//							ReturnInfo result) {
//				
//						if (null != result) {
//							Toast.makeText(ShopDetailsActivity.this, "????????????????????????",
//									Toast.LENGTH_SHORT).show();
//							img_xin.setBackgroundResource(R.drawable.kx0);
//							shop.setLike_id(-1);
//							
//						} else {
//							Toast.makeText(ShopDetailsActivity.this, "????????????????????????",
//									Toast.LENGTH_SHORT).show();
//						}
//
//						super.onPostExecute(context, result);
//					}
//
//				}.execute(shop.getShop_code());
//			}
//		}
//	}
//	
//
//	private Animation mShakeAnimation;
//	// CycleTimes?????????????????????
//	public void shakeAnimation(int CycleTimes) {
//		if (null == mShakeAnimation) {
//			mShakeAnimation = new TranslateAnimation(0, 5, 0, 5);
//			mShakeAnimation.setInterpolator(new CycleInterpolator(5));
//			mShakeAnimation.setDuration(1000);
//			mShakeAnimation.setRepeatMode(Animation.REVERSE);//????????????????????? 
//		
//		}
//		img_addxin.startAnimation(mShakeAnimation);
//		
//		//
//	}
//
//	/***
//	 * ?????????????????????
//	 */
//	private void queryShopDetails(String shopid){
//		
//		new SAsyncTask<String, Void, Shop>(this, null, R.string.wait) {
//			@Override
//			protected Shop doInBackground(FragmentActivity context,String... params) throws Exception {
//				Shop shop  = ComModel.queryShopDetails(ShopDetailsActivity.this, 
//						YCache.getCacheToken(ShopDetailsActivity.this),params[0]);
//				return shop;
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context, Shop shopd,
//					Exception e) {
//
//				if (e != null) {//????????????
//					Toast.makeText(ShopDetailsActivity.this, "????????????????????????", Toast.LENGTH_LONG).show();
//					
//				}  else {//???????????????????????????????????????
//					if (shopd != null) {
//						shop = shopd;
//						refreshView(shop);
//						initFragment();
//					}
//				}
//
//			};
//
//			@Override
//			protected boolean isHandleException() {
//				return true;
//			};
//		}.execute(shopid);
//		
//	}
//	/***
//	 * ????????????
//	 * @param shop
//	 */
//	private void refreshView(Shop shop) {
//		int like_id = shop.getLike_id();
//		if (like_id == -1) {
//			
//			img_xin.setBackgroundResource(R.drawable.kx0);
//		}else {//??????
//			img_xin.setBackgroundResource(R.drawable.hx0);	
//		}
//		String cart_count = shop.getCart_count()+"";
//		badgeView.setText(cart_count);
//		badgeView.setTextSize(12);
//		badgeView.setBadgePosition(2);
//		badgeView.show();
//		
//		String shop_name = shop.getShop_name();
//		if (!TextUtils.isEmpty(shop_name)) {
//			tv_clothes_name.setText(shop_name);
//		}
//		String shop_se_price = String.valueOf(shop.getShop_se_price());
//		if (!TextUtils.isEmpty(shop_se_price)) {
//			tv_sjprive.setText("???"+shop_se_price);
//		}
//		String shop_price = ""+shop.getShop_price();
//		if (!TextUtils.isEmpty(shop_price)) {
//			ToastUtil.addStrikeSpan(tv_prive, shop_price);
//		}
//		double s_price = shop.getShop_se_price();
//		double price = shop.getShop_price();
//		
//		if (price != 0) {
//			BigDecimal  b  =  new BigDecimal( s_price/price);  
//			double  f1  =  b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
//			String str_discount = String.valueOf( f1);
//			if (!TextUtils.isEmpty(str_discount)) {
//				tv_discount.setText(str_discount + "???");
//			}
//		}else {
//			tv_discount.setText("?????????");
//		}
//		if (s_price == price) {
//			tv_discount.setText("?????????");
//		}
//		long discount_time = shop.getShop_discount_time();
//		long time = StringUtils.getNowTime(discount_time);
//		TimeCount tCount = new TimeCount(time,1000, tv_discount_time);
//		tCount.start();
//		
//		String actual_sales = ""+shop.getActual_sales();
//		if (!TextUtils.isEmpty(actual_sales)) {
//			tv_volume.setText("?????? ");
//			ToastUtil.addForeColorSpan(tv_volume, getResources().getColor(R.color.red), actual_sales+"???");//????????????
//		}
//		int p_count = shop.getPraise_count();
//		int e_count = shop.getEva_count();
//		if (e_count != 0 ) {
//			int ratio = p_count/e_count;
//			String positive_ratio = "("+ratio +")";
//			tv_feedback_rate.setText("?????????");
//			ToastUtil.addForeColorSpan(tv_feedback_rate,getResources().getColor(R.color.red), positive_ratio);
//		}else {
//			tv_feedback_rate.setText("????????????");
//		}
//		
//		String love_num = shop.getLove_num()+""; 
//		if (!TextUtils.isEmpty(love_num)) {
//			tv_like.setText("??????  ");
//			ToastUtil.addForeColorSpan(tv_like,  getResources().getColor(R.color.red), love_num+ " ???");
//		}
//		String def_pic = shop.getDef_pic();
//		if (!TextUtils.isEmpty(def_pic)) {
//			def_pic = YUrl.imgurl + def_pic ; 
//			imageLoader = ImageLoader.getInstance();
//			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_stub)
//					.showImageForEmptyUri(R.drawable.ic_empty)
//					.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
//					.cacheOnDisk(true).considerExifParams(true)
//					.displayer(new FadeInBitmapDisplayer(35)).build();
//			ImageLoader.getInstance().displayImage(def_pic,
//					img_header, options, animateFirstListener);
//		}
//		
//	}
//
//	private static class AnimateFirstDisplayListener extends
//			SimpleImageLoadingListener {
//
//		static final List<String> displayedImages = Collections
//				.synchronizedList(new LinkedList<String>());
//
//		@Override
//		public void onLoadingComplete(String imageUri, View view,
//				Bitmap loadedImage) {
//			if (loadedImage != null) {
//				ImageView imageView = (ImageView) view;
//				boolean firstDisplay = !displayedImages.contains(imageUri);
//				if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
//					displayedImages.add(imageUri);
//				}
//			}
//		}
//	}
//	
//	/***
//	 * 
//	 * @param i
//	 * 0 ??????
//	 * 1 ??????
//	 * 2 ??????
//	 */
//	private void chooseFragment(int i ){
//		if (dFragment != null && eFragment != null && sFragment != null) {
//			if (i== 0) {
//				ft = fm.beginTransaction();
//				ft.show(dFragment);
//				ft.hide(eFragment);
//				ft.hide(sFragment);
//				ft.commit();
//
//			}else if (i== 1) {
//				ft = fm.beginTransaction();
//				if (shop != null) {
//					refreshSFView(shop);
//					ft.hide(dFragment);
//					ft.show(sFragment);
//					ft.hide(eFragment);
//					ft.commit();
//				}
//			}else {
//				ft = fm.beginTransaction();
//				eFragment.setRefreshListView(shop);
//				ft.hide(dFragment);
//				ft.hide(sFragment);
//				ft.show(eFragment);
//				ft.commit();	
//			}
//		}
//		changeTextColor(i);
//	}
//	
//	
//	private void changeTextColor(int i){
//		
//		if (i== 0) {
//			tv_xq.setTextColor(getResources().getColor(R.color.red));
//			tv_cm.setTextColor(getResources().getColor(R.color.text_hui));
//			tv_pj.setTextColor(getResources().getColor(R.color.text_hui));
//			
//		}else if (i== 1) {
//			tv_xq.setTextColor(getResources().getColor(R.color.text_hui));
//			tv_cm.setTextColor(getResources().getColor(R.color.red));
//			tv_pj.setTextColor(getResources().getColor(R.color.text_hui));
//			
//			
//		}else {
//			tv_xq.setTextColor(getResources().getColor(R.color.text_hui));
//			tv_cm.setTextColor(getResources().getColor(R.color.text_hui));
//			tv_pj.setTextColor(getResources().getColor(R.color.red));
//			
//		}
//		
//	}
//	/***
//	 * ??????????????????
//	 */
//	private void queryShopQueryAttr(final int i) {
//		if (shop != null) {
//			List<StockType> list = shop.getList_stock_type();
//			if (list != null && list.size()> 0 ) {
//				showPopWindow(i);
//			}else {
//				new SAsyncTask<String, Void, Shop>(this, null, R.string.wait) {
//					@Override
//					protected Shop doInBackground(FragmentActivity context,String... params) throws Exception {
//						ComModel.queryShopQueryAttr(ShopDetailsActivity.this,shop, params[0]);
//						return shop;
//					}
//
//					@Override
//					protected void onPostExecute(FragmentActivity context, Shop shop,
//							Exception e) {
//
//						if (e != null) {//????????????
//							Toast.makeText(ShopDetailsActivity.this, "????????????????????????", Toast.LENGTH_LONG).show();
//							
//						}  else {//???????????????????????????????????????
//							if (shop != null) {
//								ShopDetailsActivity.this.shop = shop;
//								showPopWindow(i);
//							}
//						}
//
//					};
//
//					@Override
//					protected boolean isHandleException() {
//						return true;
//					};
//				}.execute( "false");
//			}
//			
//		}
//	}
//	/****
//	 * ?????????????????????
//	 * @param i
//	 */
//	private void showPopWindow(int i){
//		if (shop != null) {
//			final ShopDetailsDialog dlg = new ShopDetailsDialog(this, R.style.DialogStyle , height ,shop ,options, animateFirstListener , i);
//			Window window = dlg.getWindow();
//			window.setGravity(Gravity.BOTTOM);
//			window.setWindowAnimations(R.style.dlg_down_to_top);
//			dlg.show();	
//			
//			dlg.callBackShopCart = new ShopDetailsDialog.OnCallBackShopCart() {
//				
//				@Override
//				public void callBackChoose(int type, String size, String color, double price, int shop_num , int stock_type_id, int stock , String pic,View v) {
//					dlg.dismiss();
//					if (type == 1) {//??????
//						Intent intent = new Intent(ShopDetailsActivity.this,SubmitOrderActivity.class);
//						Bundle bundle = new Bundle();
//						bundle.putSerializable("shop", shop);
//						intent.putExtras(bundle);
//						intent.putExtra("size", size);
//						intent.putExtra("color", color);
//						intent.putExtra("shop_num", shop_num);
//						intent.putExtra("stock_type_id", stock_type_id);
//						intent.putExtra("stock", stock);
//						intent.putExtra("price", price);
//						startActivity(intent);
//					}else {//???????????????
//						joinShopCart(size, color, shop_num,stock_type_id,pic, v);
//					}
//				}
//			};
//		}
//	}
//	/***
//	 * ???????????????
//	 * @param size
//	 * @param color
//	 * @param shop_num
//	 * @param v
//	 */
//	private void joinShopCart(String size, String color, int shop_num,int stock_type_id,String pic , View v){
//		new SAsyncTask<String, Void, ReturnInfo>(this,v, R.string.wait){
//
//			@Override
//			protected ReturnInfo doInBackground(FragmentActivity context,
//					String... params) throws Exception {
//				UserInfo user = YCache.getCacheUser(ShopDetailsActivity.this);
//				Store store = YCache.getCacheStore(ShopDetailsActivity.this);
//				
//				return ComModel.joinShopCart(ShopDetailsActivity.this, 
//						params[0], params[1],params[2],params[3], params[4],""+ user.getUser_id(),
//						YCache.getCacheToken(ShopDetailsActivity.this), shop,store.getS_code());
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context,
//					ReturnInfo result) {
//			
//				if(null != result && result.getStatus().equals("1")){
//					Toast.makeText(ShopDetailsActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
//				}else {
//					Toast.makeText(ShopDetailsActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();	
//				}
//				
//				super.onPostExecute(context, result);
//			}
//			
//		}.execute(size,color , String.valueOf(shop_num) , String.valueOf(stock_type_id) , pic);
//		
//	}
//	
//	
//	boolean flag0 = true, flag1 = true ;
//	/***
//	 * Scrollchanged????????????
//	 */
//	@Override
//	public void onScrollChanged(int x, int y, int oldx, int oldy) {
//		
//		if (flag0 && dFragment != null && eFragment != null && sFragment != null) {
//			flag0 = false;
//			ft = fm.beginTransaction();
//			dFragment.refeshImagView();
//			ft.show(dFragment);
//			ft.hide(eFragment);
//			ft.hide(sFragment);
//			ft.commit();
//			tv_xq.setTextColor(getResources().getColor(R.color.red));
//			tv_cm.setTextColor(getResources().getColor(R.color.text_hui));
//			tv_pj.setTextColor(getResources().getColor(R.color.text_hui));	
//		} 
//		
//	}
//	
//	
//	private void refreshSFView(Shop shop){
//		if (flag1) {
//		    int l = 0 ;
//		  
//			List<String[]> list = shop.getShop_attr();
//			if (list == null || list.size() == 0) {
//				return;
//			}
//			List<String[]> listSize = new ArrayList<String[]>();
//			for (int i = 0; i < list.size(); i++) {//????????????
//				String str[] = list.get(i);
//				if (str[0] .equals(SizeIndex.id)) {
//					int length = str.length;
//					if (length > l) {
//						l = length;
//					}
//					listSize.add(str);
//				}
//			}
//			l = l - 2;
//			int lSize = l/3;
//			int mond = l%3;
//			if (mond >0) {
//				lSize++;
//			}
//			List<String[]> listAll = toList(lSize, listSize);
//			sFragment.refeshListview(listAll);
//			flag1 = false;
//		}
//		
//	}
//	
//	private  List<String[]> toList(int lSize ,List<String[]> list){
//		int p = 1;
//		String strFenGe[] = {"??????"};
//		List<String[]> arrayList = new ArrayList<String[]>();
//		for (int i = 0; i < lSize; i++) {
//			for (int j = 0; j < list.size(); j++) {	
//				String strY[] = list.get(j);
//				String str[] = new String [4];
//				str[0] = strY[1];
//				int l = strY.length;
//
//				if ((3*p - 1) < l ) {
//					str[1] = strY[(3*p - 1)];
//				}
//				if ((3*p ) < l ) {
//					str[2] = strY[(3*p)];
//				}
//				if ((3*p +1) < l ) {
//					str[3] = strY[3*p + 1 ];
//				}
//				arrayList.add(str);
//				
//			}
//			p++;
//			if (i!=lSize-1) {
//				arrayList.add(strFenGe);
//			}
//		}
//		
//		return arrayList;
//		
//	}
//	
//	class TimeCount extends CountDownTimer {
//		private TextView tv = null;
//
//		public TimeCount(long millisInFuture, long countDownInterval,TextView tv) {
//			super(millisInFuture, countDownInterval);// ????????????????????????,????????????????????????
//			this.tv = tv;
//
//		}
//
//		@Override
//		public void onFinish() {// ?????????????????????
//			try {
//				tv.setText("????????????");
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		@Override
//		public void onTick(long millisUntilFinished) {// ??????????????????
//			try {
//				long nd = 1000*24*60*60;//??????????????????
//				long nh = 1000*60*60;//?????????????????????
//				long nm = 1000*60;//?????????????????????
//				long ns = 1000;//?????????????????????
//				long diff = millisUntilFinished;
//				long day = diff/nd;//??????????????????
//				long hour = diff%nd/nh;//?????????????????????
//				long min = diff%nd%nh/nm;//?????????????????????
//				long sec = diff%nd%nh%nm/ns;//??????????????????//????????????
//				
//				tv.setText("?????????????????????"+day +"???    "+hour + "??????"+min + "???"+ sec + "???");
//				
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}
