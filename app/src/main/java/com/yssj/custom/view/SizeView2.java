package com.yssj.custom.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.yssj.YConstance.SizeIndex;
import com.yssj.YUrl;
import com.yssj.activity.R;
import com.yssj.data.YDBHelper;
import com.yssj.entity.Shop;
import com.yssj.entity.ShopPrice;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SizeView2 extends LinearLayout {

	private YDBHelper helper;

	private TextView tv_shopconnect;
	private LinearLayout viewContainer;
	private LinearLayout priceviewContainer;
	private LinearLayout detailViewContainer;
	private LinearLayout basepriceviewContainer;

	private Shop shop;

	private Context context;

	private boolean isRefresh = false;

	private MyGridView sizeTag;

	private ImageView mealIndex;

	private TextView title, content;
	private TextView tv_shop_code;
	private LinearLayout mLlFristRow;
	private TextView mRowLong, mRowShort;

	private TextView order_shop_code;
	private ImageView order_shop_img;
	private TextView order_shop_price;
	private TextView tv_name;

	// private MyGridAdapter adapter1,adapter2,adapter3;

	public SizeView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.fragment_size2, this);
		helper = new YDBHelper(context);
		mLlFristRow = (LinearLayout) findViewById(R.id.size_ll_frist_row);
		mRowLong = (TextView) findViewById(R.id.size_tag_item_long);
		mRowShort = (TextView) findViewById(R.id.size_tag_item_short);
		tv_name = (TextView) findViewById(R.id.tv_name);
		viewContainer = (LinearLayout) findViewById(R.id.viewContainer);
		priceviewContainer = (LinearLayout) findViewById(R.id.priceViewContainer);
		detailViewContainer = (LinearLayout) findViewById(R.id.detailViewContainer);
		basepriceviewContainer = (LinearLayout) findViewById(R.id.base_price_view);

		tv_shopconnect = (TextView) findViewById(R.id.tv_shopconnect);

		sizeTag = (MyGridView) findViewById(R.id.size_tag);

		mealIndex = (ImageView) findViewById(R.id.meal_index);

		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);
		tv_shop_code = (TextView) findViewById(R.id.tv_shop_code);
		isRefresh = true;

		order_shop_code = findViewById(R.id.tv_detail_shop_code);
		order_shop_img = (ImageView)findViewById(R.id.detail_shop_img);
		order_shop_price = findViewById(R.id.tv_detail_shop_price);
	}

	public void setContent(Shop shop, boolean isMeal, int position) {

		this.shop = shop;
		if (shop != null && !TextUtils.isEmpty(shop.getContent())) {
			tv_shopconnect.setText(shop.getContent());
			tv_shop_code.setText("商品编号:" + shop.getShop_code());
		}

		if(shop !=null && shop.getNewfour_pic() != null){
			order_shop_code.setText(String.valueOf(shop.getShop_code()));
			order_shop_price.setText(String.valueOf(shop.getShop_se_price()));

			String four_pic = shop.getNewfour_pic() + "";
			String pic = YUrl.imgurl + shop.getShop_code().substring(1, 4) + "/" + shop.getShop_code() + "/" + four_pic.split(",")[2] + "!280";

			String url = YUrl.imgurl + shop.getShop_code().substring(1, 4) + "/" + shop.getShop_code() + "/" + shop.getDef_pic();
			Glide.with(context).load(pic).into(order_shop_img);
		}

		if (isRefresh) {
			isRefresh = false;
			if (isMeal) {
				findViewById(R.id.size_tag_ly).setVisibility(View.GONE);
				mealIndex.setVisibility(View.VISIBLE);
				findViewById(R.id.size_hint_s).setVisibility(View.GONE);
				findViewById(R.id.iv_chimabiao).setVisibility(View.GONE);
				findViewById(R.id.iv_size).setVisibility(View.GONE);
				findViewById(R.id.tv_shop_code).setVisibility(View.GONE);
				switch (position) {
				case 0: {
					mealIndex.setImageResource(R.drawable.shop_tag_one);
					addMealView(viewContainer, shop.getShop_attr());

				}
					break;
				case 1: {
					mealIndex.setImageResource(R.drawable.shop_tag_two);
					addMealView(viewContainer, shop.getShop_attr());
				}
					break;
				case 2: {
					mealIndex.setImageResource(R.drawable.shop_tag_three);
					addMealView(viewContainer, shop.getShop_attr());
				}
					break;

				default:
					break;
				}

			} else {
				findViewById(R.id.size_tag_ly).setVisibility(View.VISIBLE);
				mealIndex.setVisibility(View.GONE);
				List<HashMap<String, String>> list1;
				if (shop.getShop_tag() != null && shop.getShop_tag().length() > 1 && shop.getShop_tag().endsWith(",")) {
					String substring = (shop.getShop_tag()).substring(0, shop.getShop_tag().length() - 1);
					// list1 = helper.query("select * from tag_info where _id
					// in("+substring+") and is_show=1 order by sequence");
					list1 = helper
							.query("select t1.* from tag_info t1 LEFT JOIN tag_info t2 ON(t2._id = t1.p_id) where (t1._id in ("
									+ substring + ") and t1.is_show=1 ) order by t2.sequence");

				} else {
					// list1 = helper.query("select * from tag_info where _id
					// in("+shop.getShop_tag()+") and is_show=1 order by
					// sequence");
					list1 = helper
							.query("select t1.* from tag_info t1 LEFT JOIN tag_info t2 ON(t2._id = t1.p_id) where (t1._id in ("
									+ shop.getShop_tag() + ") and t1.is_show=1 ) order by t2.sequence");
				}
				List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				List<HashMap<String, String>> listNew = new ArrayList<HashMap<String, String>>();
				if (list1.size() > 0) {
					list.add(list1.get(0));
				}
				if (list1.size() > 7) {
					list.add(list1.get(7));
				}
				if (list1.size() > 3) {
					list.add(list1.get(3));
				}
				if (list1.size() > 6) {
					list.add(list1.get(6));
				}
				if (list1.size() > 4) {
					list.add(list1.get(4));
				}
				if (list1.size() > 1) {
					list.add(list1.get(1));
				}
				if (list1.size() > 2) {
					list.add(list1.get(2));
				}
				if (list1.size() > 5) {
					list.add(list1.get(5));
				}
				if (list1.size() > 8) {
					list.add(list1.get(8));
				}
				if (list1.size() > 9) {
					list.add(list1.get(9));
				}
				if (list1.size() > 10) {
					list.add(list1.get(10));
				}
				if (list1.size() > 11) {
					list.add(list1.get(11));
				}
				if (list1.size() > 12) {
					list.add(list1.get(12));
				}
				if (list1.size() > 13) {
					list.add(list1.get(13));
				}
				if (list1.size() > 14) {
					list.add(list1.get(14));
				}
				boolean flag1 = false;// 成份含量判断标志
				String str1 = "";
				boolean flag2 = false;// 羽绒服充绒量判断标志
				String str2 = "";
				boolean flag3 = false;// 第一个标签
				String str3 = "";
				if (list.size() == 0) {
					sizeTag.setAdapter(new MyGridAdapter(list));
					mLlFristRow.setVisibility(View.GONE);
				} else if (list.size() == 1) {
					if ("10".equals(list.get(0).get("p_id"))) {// 成份含量
						flag1 = true;
						str1 = list.get(0).get("attr_name");
						mRowLong.setText("主面料成份含量:" + str1);
						mRowShort.setVisibility(View.INVISIBLE);
					} else if ("115".equals(list.get(0).get("p_id"))) {// 羽绒服充绒量
						flag2 = true;
						str2 = list.get(0).get("attr_name");
						mRowLong.setText("羽绒服充绒量:" + str2);
						mRowShort.setVisibility(View.INVISIBLE);
					} else {
						sizeTag.setAdapter(new MyGridAdapter(list));
					}
				} else {
					for (int i = 0; i < list.size(); i++) {
						if ("10".equals(list.get(i).get("p_id"))) {
							flag1 = true;
							str1 = list.get(i).get("attr_name");
						} else if ("115".equals(list.get(i).get("p_id"))) {
							flag2 = true;
							str2 = list.get(i).get("attr_name");
						} else {
							listNew.add(list.get(i));
						}
					}
					if (listNew.size() > 0) {
						flag3 = true;
						str3 = listNew.get(0).get("attr_name");
						listNew.remove(0);
					}
					if (flag2 && flag3) {
						mRowLong.setText("羽绒服充绒量:" + str2);
						mRowShort.setText(str3);
					} else if (flag2) {
						mRowLong.setText("羽绒服充绒量:" + str2);
						mRowShort.setVisibility(View.INVISIBLE);
					} else if (flag1 && flag3) {
						mRowLong.setText("主面料成份含量:" + str1);
						mRowShort.setText(str3);
					} else if (flag1) {
						mRowLong.setText("主面料成份含量:" + str1);
						mRowShort.setVisibility(View.INVISIBLE);
					} else {
						mLlFristRow.setVisibility(View.GONE);
					}
					sizeTag.setAdapter(new MyGridAdapter(listNew));
				}

				// sizeTag.setAdapter(new MyGridAdapter(list));
				refreshSFView();

				if(shop.getShop_kind() !=null && shop.getShop_kind().equals("1") && shop.getShopPriceList() !=null && shop.getShopPriceList().size()>0){
					basepriceviewContainer.setVisibility(VISIBLE);
					refreshPriceView();
				}
			}
			/**
			 * switch (position) { case 0: {
			 * findViewById(R.id.size_hint_s).setVisibility(View.VISIBLE);
			 * if(isMeal){ mealIndex.setImageResource(R.drawable.shop_tag_one);
			 * findViewById(R.id.size_tag_ly).setVisibility(View.GONE); }else{
			 * 
			 * }
			 * 
			 * } break; case 1: {
			 * findViewById(R.id.size_tag_ly).setVisibility(View.GONE);
			 * findViewById(R.id.size_hint_s).setVisibility(View.GONE);
			 * mealIndex.setImageResource(R.drawable.shop_tag_two);
			 * sizeTag.setVisibility(View.GONE); } break; case 2: {
			 * findViewById(R.id.size_tag_ly).setVisibility(View.GONE);
			 * findViewById(R.id.size_hint_s).setVisibility(View.GONE);
			 * mealIndex.setImageResource(R.drawable.shop_tag_three);
			 * 
			 * 
			 * 
			 * } break;
			 * 
			 * default: break; }
			 */
		}
	}

	private void addMealView(LinearLayout container, List<String[]> listAll) {
		container.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(context);
		for (int i = 0; i < listAll.size(); i++) {
			View convertView = inflater.inflate(R.layout.meal_size_view, null);
			convertView.setLayoutParams(
					new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			String[] attrs = listAll.get(i);
			TextView title = (TextView) convertView.findViewById(R.id.title);
			TextView content = (TextView) convertView.findViewById(R.id.content);
			String x = "";
			for (int s = 2; s < attrs.length; s++) {
				x += attrs[s] + ",";
			}
			List<HashMap<String, String>> titlelist = helper
					.query("select * from attr_info where _id in (" + attrs[1] + ")");

			title.setText(titlelist.size() > 0 ? titlelist.get(0).get("attr_name") : "");

			List<HashMap<String, String>> list = helper
					.query("select * from attr_info where _id in (" + x.substring(0, x.length() - 1) + ")");
			if (list != null && list.size() > 0) {
				String txt = "";
				List<String> ss = new ArrayList<String>();
				for (int j = 0; j < list.size(); j++) {
					if (ss.contains(list.get(j).get("attr_name"))) {
						continue;
					}
					ss.add(list.get(j).get("attr_name"));
					txt += list.get(j).get("attr_name") + ",";
				}
				if (!TextUtils.isEmpty(txt)) {
					content.setText(txt.substring(0, txt.length() - 1));
				}
				container.addView(convertView);
			}
		}
	}

	private void addView(LinearLayout container, List<String[]> listAll,boolean isdel) {

		if(listAll.size()>4 && isdel) {
			listAll.remove(listAll.size() - 1);
			listAll.remove(listAll.size() - 1);
			listAll.remove(listAll.size() - 1);
			listAll.remove(listAll.size() - 1);
		}

		container.removeAllViews();
		int countUse = 0;
		LayoutInflater inflater = LayoutInflater.from(context);
		for (int i = 0; i < listAll.size(); i++) {
			View convertView = inflater.inflate(R.layout.listview_size, null);
			if (i == 0) {
				convertView.setBackgroundColor(Color.parseColor("#a8a8a8"));
			} else {
				convertView.setBackgroundColor(Color.parseColor("#ffffff"));
			}
			boolean flags = false;
			TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			TextView tv_size1 = (TextView) convertView.findViewById(R.id.tv_size1);
			TextView tv_size2 = (TextView) convertView.findViewById(R.id.tv_size2);
			TextView tv_size3 = (TextView) convertView.findViewById(R.id.tv_size3);
			TextView tv_size4 = (TextView) convertView.findViewById(R.id.tv_size4);
			TextView tv_size5 = (TextView) convertView.findViewById(R.id.tv_size5);
			if (i == 0) {
				tv_name.setTextColor(Color.parseColor("#ffffff"));
				tv_size1.setTextColor(Color.parseColor("#ffffff"));
				tv_size2.setTextColor(Color.parseColor("#ffffff"));
				tv_size3.setTextColor(Color.parseColor("#ffffff"));
				tv_size4.setTextColor(Color.parseColor("#ffffff"));
				tv_size5.setTextColor(Color.parseColor("#ffffff"));

			}
			String[] str = listAll.get(i);
			if (!TextUtils.isEmpty(str[0])) {
				if (str[0].equals("分割")) {
					convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 30));
				} else {
					for (int j = 0; j < str.length; j++) {
						if (TextUtils.isEmpty(helper.queryAttr_name(str[1])) || "".equals(helper.queryAttr_name(str[1]))
								|| null == helper.queryAttr_name(str[1])) {
							flags = true;
						} else {
							flags = false;
						}
						if (!TextUtils.isEmpty(str[j])) {
							String attr_name = helper.queryAttr_name(str[j]);
							if (!TextUtils.isEmpty(attr_name)) {
								if (j == 0) {
									tv_name.setText(attr_name);
								}
								// if (i == 0 && j == 1 &&
								// (helper.queryAttr_name(str[2])) == null) {
								// tv_size1.setText("均码");
								// } else
								if (j == 1) {
									tv_size1.setText(attr_name);
								}
								if (j == 2) {
									tv_size2.setText(attr_name);
								}
								if (j == 3) {
									tv_size3.setText(attr_name);
								}
								if (j == 4) {
									tv_size4.setText(attr_name);
								}
								if (j == 5) {
									tv_size5.setText(attr_name);
								}
							}
						}

					}
				}
			}
			if (!flags) {
				countUse++;
				if (i == 0) {
					convertView.setBackgroundColor(Color.parseColor("#a8a8a8"));
				} else if (countUse % 2 == 1) {
					convertView.setBackgroundColor(Color.parseColor("#f6f6f6"));
				} else {
					convertView.setBackgroundColor(Color.parseColor("#ffffff"));
				}
				container.addView(convertView);
			}

		}
	}

	private void addPriceListView(LinearLayout container){

		List<List<String>> mlist = new ArrayList<>();
		int k = 0;
		for (ShopPrice shopPrice : shop.getShopPriceList()) {

			List<String> mStrings = new ArrayList<>();
			String titlename = shopPrice.getType_name().toString();
			mStrings.add(titlename);
			int allprice =0;
			for (ShopPrice.ChildrenDTO child : shopPrice.getChildren()) {
				mStrings.add(child.getType_name());
				mStrings.add(String.valueOf(child.getPrice()));
				mStrings.add(String.valueOf(child.getType_use()));

				int totalprice = child.getPrice()*child.getType_use();
				allprice += totalprice;
				mStrings.add(String.valueOf(totalprice));
			}



			if(k == 0 ){
				if(shopPrice.getChildren().size()>1){
					mStrings.add("小计");
					mStrings.add("");
					mStrings.add("");
					mStrings.add(String.valueOf(allprice));
				}else if(shopPrice.getChildren().size()==1) {
					mStrings.add("-");
					mStrings.add("-");
					mStrings.add("-");
					mStrings.add("-");

					mStrings.add("小计");
					mStrings.add("");
					mStrings.add("");
					mStrings.add(String.valueOf(allprice));
				}

			}else if(k == 1){
				mStrings.add("小计");
				mStrings.add("");
				mStrings.add("");
				mStrings.add(String.valueOf(allprice));
			}else {
				if(shopPrice.getChildren().size()>1) {
					mStrings.add("小计");
					mStrings.add("");
					mStrings.add("");
					mStrings.add(String.valueOf(allprice));
				}
			}

			k ++ ;
			mlist.add(mStrings);
		}

		LayoutInflater inflater = LayoutInflater.from(context);

		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int new_head_width = dip2px(context,30);
		int space_heigh = dip2px(context,2);

		for(int i=0;i<mlist.size();i++){

			View convertView = inflater.inflate(R.layout.listview_price, null);

			GridLayout gridLayout = (GridLayout) convertView.findViewById(R.id.GirdLayout1);
			// 7行   5列
			gridLayout.setColumnCount(5);
			gridLayout.setRowCount(7);




			for(int j=0;j<mlist.get(i).size();j++){
				TextView textView = new TextView(context);
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();

				// 设置行列下标，和比重
//				params.rowSpec = GridLayout.spec((j+1)/5,1f);
//				params.columnSpec = GridLayout.spec((j+1)%5,1f);

				// 背景
				textView.setBackgroundColor(Color.WHITE);
				textView.setTextColor(Color.BLACK);



				// 字体颜色
				if(j == 0)
				{
					textView.setBackgroundColor(Color.parseColor("#a8a8a8"));
					textView.setTextColor(Color.WHITE);
					textView.setTextSize(11);
					textView.setPadding(15,0,15,0);
					params.rowSpec = GridLayout.spec((j+3)/4,6,1f);
					params.width = new_head_width;

				}else {

					params.width = (width*2/3-new_head_width-10)/4;

					textView.setTextSize(10);
					textView.setBackgroundColor(Color.parseColor("#ffffff"));

					if(mlist.get(i).size() >5){
						textView.setPadding(0,10,0,10);
					}else {
						textView.setPadding(0,40,0,40);
					}
				}


				// 居中显示
				textView.setGravity(Gravity.CENTER);

				// 设置边距
//				if(j/5 == 0){
//					params.setMargins(1,5,1,0);
//				}else {
//					params.setMargins(1,1,1,1);
//				}
				if (i == 0) {

					if(j <5){
						params.setMargins(1, 0, 1, 1);
					}else {
						params.setMargins(1,0,1,1);
					}
				}else {
					if(j <5){
						if(j==0){
							params.setMargins(1, space_heigh, 1, 0);
						}else {
							params.setMargins(1, space_heigh, 1, 0);
						}
					}else {
						params.setMargins(1,1,1,0);
					}
				}



				// 设置文字
				textView.setText(mlist.get(i).get(j));

				// 添加item
				gridLayout.addView(textView,params);

			}

			container.addView(convertView);
		}
	}

	private class MyGridAdapter extends BaseAdapter {
		private List<HashMap<String, String>> list;

		public MyGridAdapter(List<HashMap<String, String>> list) {
			super();
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {

			v = LayoutInflater.from(context).inflate(R.layout.size_item, parent, false);

			TextView tv = (TextView) v.findViewById(R.id.size_tag_item);

			tv.setText(list.get(position).get("attr_name"));
			return v;
		}

	}

	private void refreshPriceView(){
		addPriceListView(priceviewContainer);//处理工价单
	}
	private void refreshSFView() {
		int l = 0;

		List<String[]> list = shop.getShop_attr();
		if (list == null || list.size() == 0) {
			return;
		}
		List<String[]> listSize = new ArrayList<String[]>();
		for (int i = 0; i < list.size(); i++) {// 选择尺码
			String str[] = list.get(i);
			if (str[0].equals(SizeIndex.id)) {
				int length = str.length;
				if (length > l) {
					l = length;
				}
				listSize.add(str);
			}
		}
		l = l - 2;
		int lSize = l / 5;
		int mond = l % 5;

		if (mond > 0) {
			lSize++;
		}
		List<String[]> listAll = toList(lSize, listSize);
		addView(viewContainer, listAll,true);//处理尺码
	}

	private List<String[]> toList(int lSize, List<String[]> list) {
		int p = 1;
		String strFenGe[] = { "分割" };
		List<String[]> arrayList = new ArrayList<String[]>();
		for (int i = 0; i < lSize; i++) {
			for (int j = 0; j < list.size(); j++) {
				String strY[] = list.get(j);
				String str[] = new String[6];
				str[0] = strY[1];
				int l = strY.length;

				if ((5 * p - 3) < l) {
					str[1] = strY[(5 * p - 3)];
				}
				if ((5 * p - 2) < l) {
					str[2] = strY[(5 * p - 2)];
				}
				if ((5 * p - 1) < l) {
					str[3] = strY[(5 * p - 1)];
				}
				if ((5 * p) < l) {
					str[4] = strY[5 * p];
				}
				if ((5 * p + 1) < l) {
					str[5] = strY[5 * p + 1];
				}
				arrayList.add(str);

			}
			p++;
			if (i != lSize - 1) {
				arrayList.add(strFenGe);
			}
		}

		return arrayList;

	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
