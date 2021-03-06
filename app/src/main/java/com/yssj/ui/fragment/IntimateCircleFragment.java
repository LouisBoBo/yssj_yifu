package com.yssj.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yssj.YJApplication;
import com.yssj.activity.R;
import com.yssj.app.SAsyncTask;
import com.yssj.custom.view.CustImageGalleryIntimate;
import com.yssj.custom.view.FlowLayout;
import com.yssj.custom.view.FolderTextView;
import com.yssj.custom.view.RoundCornersImageView;
import com.yssj.custom.view.XListViewMatch;
import com.yssj.data.YDBHelper;
import com.yssj.entity.ReturnInfo;
import com.yssj.model.ComModel2;
import com.yssj.ui.activity.CommonActivity;
import com.yssj.ui.activity.circles.SweetFriendsDetails;
import com.yssj.ui.activity.classfication.ManufactureActivity;
import com.yssj.ui.activity.logins.LoginActivity;
import com.yssj.ui.activity.main.FilterResultActivity;
import com.yssj.ui.activity.main.ForceLookActivity;
import com.yssj.ui.activity.main.WordSearchResultActivity;
import com.yssj.utils.DP2SPUtil;
import com.yssj.utils.GlideUtils;
import com.yssj.utils.LimitDoubleClicked;
import com.yssj.utils.PicassoUtils;
import com.yssj.utils.TimeUtils;
import com.yssj.utils.ToastUtil;
import com.yssj.utils.YCache;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class IntimateCircleFragment extends Fragment implements View.OnClickListener {
	public static ArrayList<String> hashSet = new ArrayList<String>();

	private Context mContext;
	private XListViewMatch mListView;
	// private SnatchScrollList mView;
	private List<HashMap<String, Object>> mListDatas;
	private DataAdapter mAdater;
	private int curPager;
//	private int pagerSize = 10;
	public int width;
	public int height;
	private java.text.DecimalFormat pFormate;
	private String mType;// ????????????1 ???????????? 2??????
	private LayoutInflater mInflater;
	private LinearLayout horistolVg;
	private List<HashMap<String,String>> listTopTag;
	private YDBHelper dbHelp;
	private View nodataView;
	private TextView nodataTv;
	private View to_login_view;
	private  Button to_login_btn;
	
	/**
	 * @param title
	 * @param context
	 * @param type //??????1 ???????????? 2??????
	 */
	public static IntimateCircleFragment newInstances(String title, Context context, String type) {
		IntimateCircleFragment fragment = new IntimateCircleFragment();
		Bundle args = new Bundle();
		args.putString("tag", title);
		args.putString("type", type);
//		mContext = context;
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mType = (String) getArguments().get("type");
		mContext = getActivity();
		return inflater.inflate(R.layout.intimate_circle_fragment, container, false);

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dbHelp = new YDBHelper(mContext);
		pFormate = new DecimalFormat("#0.0");
		nodataView =getView().findViewById(R.id.groups_nodata);
		getView().findViewById(R.id.btn_view_allcircle).setVisibility(View.GONE);
		nodataTv = (TextView) getView().findViewById(R.id.tv_qin);
		nodataTv.setText("??????????????????~");
		getView().findViewById(R.id.tv_no_join).setVisibility(View.GONE);
		to_login_view = getView().findViewById(R.id.to_login_view);
		to_login_btn = (Button) getView().findViewById(R.id.to_login_btn);
		to_login_btn.setOnClickListener(this);
		
		listTopTag= new ArrayList<HashMap<String,String>>();
		mListView = (XListViewMatch) getView().findViewById(R.id.dataList);
		mListView.setPullLoadEnable(true);
		curPager = 1;
		mInflater = LayoutInflater.from(mContext);
		View headView = mInflater.inflate(R.layout.intimate_circle_horistol_view, null);
		horistolVg = (LinearLayout) headView.findViewById(R.id.container);
		String sql = "select * from friend_circle_tag where is_show = 1 and type = 1 order by sort desc";
	    listTopTag =  dbHelp.query(sql);
		for (int i = 0; i < listTopTag.size()+1; i++) {
			View childView = mInflater.inflate(R.layout.intimate_circle_horistol_textview, null);
			TextView tv = (TextView) childView.findViewById(R.id.top_horizontal_textview);
			if (i == 0) {
				tv.setText("??????");
				tv.setBackgroundResource(R.drawable.shape_match_attr);
				tv.setTextColor(Color.WHITE);
//				tv.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Intent  intent = new Intent(mContext, CommonActivity.class);
//						intent.putExtra("tag", "");
//						intent.putExtra("tagName", "??????");
//						((FragmentActivity) mContext).startActivity(intent);
//						((FragmentActivity) mContext).overridePendingTransition(R.anim.activity_from_right,R.anim.activity_search_close);
//					}
//				});
			} else {
				final String tagName = listTopTag.get(i-1).get("name");
				final String tagId = listTopTag.get(i-1).get("_id");
				tv.setText(tagName);
				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent  intent = new Intent(mContext, CommonActivity.class);
						intent.putExtra("tag", tagId);
						intent.putExtra("tagName", tagName);
						((FragmentActivity) mContext).startActivity(intent);
//						getRootFragment().startActivityForResult(intent, 14341);
						((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
					}
				});
			}
			horistolVg.addView(childView);
		}
		mListView.addHeaderView(headView);

		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);

		width = dm.widthPixels;
		height = dm.heightPixels;
		mListView.setXListViewListener(new XListViewMatch.IXListViewListener() {

			@Override
			public void onRefresh() {
			}

			@Override
			public void onLoadMore() {
				curPager++;
				getIntimateList();
			}
		});
		mListDatas = new ArrayList<HashMap<String, Object>>();
		mAdater = new DataAdapter(getActivity(), mListDatas);
		mListView.setAdapter(mAdater);
		getIntimateList();

	}

	public void refresh() {
		curPager = 1;
		getIntimateList();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
//		getIntimateList();
	}

	public void setListVisibility(){
		if(mListView!=null){
			mListView.setVisibility(View.GONE);
		}
		if(nodataView!=null){
			nodataView.setVisibility(View.GONE);
		}
		if(to_login_view!=null){
			to_login_view.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * ??????????????????????????? 
	 * type //??????1 ???????????? 2??????
	 */
	private void getIntimateList() {
		if(!YJApplication.instance.isLoginSucess()&&"2".equals(mType)){//??????????????????
			mListView.stopLoadMore();
			mListView.setVisibility(View.GONE);
			nodataView.setVisibility(View.GONE);
			to_login_view.setVisibility(View.VISIBLE);
			return;
		}
		new SAsyncTask<Void, Void, List<HashMap<String, Object>>>((FragmentActivity) mContext, R.string.wait) {

//			@Override
//			protected void onPreExecute() {
//				// TODO Auto-generated method stub
//				super.onPreExecute();
//				LoadingDialog.show(getActivity());
//
//			}

			@Override
			protected List<HashMap<String, Object>> doInBackground(FragmentActivity context, Void... params)
					throws Exception {

				return ComModel2.getIntimateList(context, mType,curPager,"");// 
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity context, List<HashMap<String, Object>> result, Exception e) {
				if (e != null) {// ????????????
					mListView.stopLoadMore();
					return;
				}
				List<HashMap<String, Object>> dataList = result;
//				String intimateListSize = SharedPreferencesUtil.getStringData(context, Pref.INTIMATE_LIST_SIZE, "0");
				mListView.setVisibility(View.VISIBLE);
				nodataView.setVisibility(View.GONE);
				to_login_view.setVisibility(View.GONE);
				if (dataList != null) {
					if (dataList.size() == 0) {
						if(curPager > 1){
//							ToastUtil.showShortText(context, "??????????????????");
						}else{
							mListView.setVisibility(View.GONE);
							nodataView.setVisibility(View.VISIBLE);
							to_login_view.setVisibility(View.GONE);
						}
					}else {
						if (curPager == 1) {
							mListDatas.clear();
						}
						mListDatas.addAll(dataList);
						mAdater.notifyDataSetChanged();
					}
				} else {
					if (curPager == 1) {
						mListDatas.clear();
						mAdater.notifyDataSetChanged();
						mListView.setVisibility(View.GONE);
						nodataView.setVisibility(View.VISIBLE);
						to_login_view.setVisibility(View.GONE);
					} else {
//						ToastUtil.showShortText(context, "??????????????????");
					}
				}
				mListView.stopLoadMore();
				super.onPostExecute(context, result, e);
			}

		}.execute();
	}

	public static IntimateCircleFragment newInstances(String title, Context context) {
		IntimateCircleFragment fragment = new IntimateCircleFragment();
		Bundle args = new Bundle();
		args.putString("tag", title);
//		mContext = context;
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case  R.id.to_login_btn:
			toLogin();
			break;
		default:
			break;
		}
	}

	private class DataAdapter extends BaseAdapter {
		private Context context;
		private List<HashMap<String, Object>> listData;
		private LayoutInflater mInflater;

		public DataAdapter(Context context, List<HashMap<String, Object>> listData) {
			this.context = context;
			this.listData = listData;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public Object getItem(int position) {
			return listData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final Holder holder;
			if (convertView == null) {
				holder = new Holder();
				convertView = mInflater.inflate(R.layout.intimate_cicle_fragment_list, null);
				holder.custGallery = (CustImageGalleryIntimate) convertView.findViewById(R.id.intimate_custom_images);
				holder.imgHead = (ImageView) convertView.findViewById(R.id.img_head);
				holder.imgHeadV = (ImageView)convertView.findViewById(R.id.img_user_head_v);
				holder.containerBottom = (FlowLayout)convertView.findViewById(R.id.container_bottmo_biaoqian);
//				holder.myGridView = (MyGridView2)convertView.findViewById(R.id.main_img_mgv);
				holder.mainImgCountTv = (TextView) convertView.findViewById(R.id.main_img_count_tv);
				holder.mainImgIv = (RoundCornersImageView) convertView.findViewById(R.id.main_img_iv);
				holder.nickNameTv = (TextView) convertView.findViewById(R.id.nick_name);
				holder.locationTv = (TextView) convertView.findViewById(R.id.tv_location);
				holder.sendTimeTv = (TextView) convertView.findViewById(R.id.tv_time);
				holder.contentTv = (FolderTextView) convertView.findViewById(R.id.tv_content);
				holder.commentCountTv = (TextView) convertView.findViewById(R.id.tv_num_pingjia);
				holder.applaudNumTv = (TextView) convertView.findViewById(R.id.tv_num_dianzan);
				holder.applaudNumIcon = (ImageView) convertView.findViewById(R.id.tv_dianzan);
				holder.addIntiamte = convertView.findViewById(R.id.ll_add_intiamte);
				holder.addIntiamteGray = convertView.findViewById(R.id.tv_add_intiamte_gray);
				holder.dianzanIcon = convertView.findViewById(R.id.ll_dianzan);
				holder.pingjiaIcon = convertView.findViewById(R.id.ll_pingjia);
				holder.deleteIcon = convertView.findViewById(R.id.ll_delete);
				holder.shoucangIcon = convertView.findViewById(R.id.ll_shoucang);
				holder.shoucangNumTv = (TextView) convertView.findViewById(R.id.tv_num_shoucang);
				holder.shoucangNumIcon= (ImageView) convertView.findViewById(R.id.iv_icon_shoucang);
				holder.containerComments= (LinearLayout) convertView.findViewById(R.id.container_comments);
				convertView.setTag(holder);

			} else {
				holder = (Holder) convertView.getTag();
			}
			final HashMap<String, Object> datas = listData.get(position);
			final String theme_id = (String)datas.get("theme_id");
			final String user_id = (String)datas.get("user_id");//????????????ID
			final String thisUserId = YJApplication.instance.isLoginSucess()?YCache.getCacheUserSafe(mContext).getUser_id()+"":"";//????????????ID
			final String theme_type = (String)datas.get("theme_type");
			final List<HashMap<String, Object>> shop_list = (List<HashMap<String, Object>>) datas.get("shop_list");
//			SetImageLoader.initImageLoader(mContext, holder.imgHead, (String) datas.get("head_pic"), "");
//			PicassoUtils.initImage(mContext, (String) datas.get("head_pic"), holder.imgHead);
            GlideUtils.initRoundImage(Glide.with(context),context,(String) datas.get("head_pic"),holder.imgHead);

			String v_ident = (String) datas.get("v_ident");
			if("1".equals(v_ident)){
				holder.imgHeadV.setImageResource(R.drawable.v_red);
				holder.imgHeadV.setVisibility(View.VISIBLE);
			}else if("2".equals(v_ident)){
				holder.imgHeadV.setImageResource(R.drawable.v_blue);
				holder.imgHeadV.setVisibility(View.VISIBLE);
			}else{
				holder.imgHeadV.setVisibility(View.GONE);
			}
			String nickname = (String)datas.get("nickname");
			if(nickname.length()>8){
				nickname = nickname.substring(0,8)+"...";
			}
			holder.nickNameTv.setText(nickname);
			String location = (String)datas.get("location");
			if(!TextUtils.isEmpty(location)){
				if(location.length()>6){
					location = location.substring(0,6)+"...";
				}
				holder.locationTv.setText(location);
			}else{
				holder.locationTv.setText("????????????");
			}
			String send_time = (String)datas.get("send_time");
			long longSendTime=0;
			try {
				longSendTime = Long.parseLong(send_time);
			} catch (NumberFormatException e) {
				longSendTime=0;
			}
			if(longSendTime!=0){
			  String showSendTime = TimeUtils.showSendTime(longSendTime);
			  holder.sendTimeTv.setText(showSendTime);
			}
			//??????
			
			String title = (String) datas.get("title");
			String content = (String) datas.get("content");
			if(!TextUtils.isEmpty(title)&&"2".equals(theme_type)){
				title = "#"+title+"#";
				holder.contentTv.setTitle(title);
			}else{
				holder.contentTv.setTitle("");
			}
			String textContent ="2".equals(theme_type)?title+" "+content:content;
			if(!TextUtils.isEmpty(textContent.trim())){
//				SpannableString textSpan = new SpannableString(textContent);
//				textSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#FF3F8B")), 0, title.length(),
//						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.contentTv.setText(textContent);
				holder.contentTv.setVisibility(View.VISIBLE);
			}else{
				holder.contentTv.setVisibility(View.GONE);
			}
			
			//??????
			holder.containerBottom.removeAllViews();
			holder.containerBottom.setVisibility(View.GONE);
			if("1".equals(theme_type)){//????????????
				List<String> tagIds = (List<String>) datas.get("tags");
				for (int i = 0; i < tagIds.size(); i++) {
					String sql = "select * from friend_circle_tag where _id = " + tagIds.get(i);
					List<HashMap<String, String>> listTag = dbHelp.query(sql);
					if(listTag!=null&&listTag.size()>0){
						holder.containerBottom.setVisibility(View.VISIBLE);
//						View childView = mInflater.inflate(R.layout.intimate_circle_horistol_textview, null);
//						TextView tv = (TextView) childView.findViewById(R.id.top_horizontal_textview);
						TextView childView = (TextView) mInflater.inflate(R.layout.search_label_tv, holder.containerBottom, false);
						final String tagName = listTag.get(0).get("name");
						final String tagId = listTag.get(0).get("_id");
						String tagType = listTag.get(0).get("type");////1???????????????2???????????????
//						if ("1".equals(tagType)) {//1???????????? ????????????
//							tv.setText(tagName);
//							tv.setBackgroundResource(R.drawable.shape_pink_intimate);
//							tv.setTextColor(Color.parseColor("#FF3F8B"));
//						} else {
//							tv.setText(tagName);
//						}
						childView.setText(tagName);
						childView.setTextSize(12);
						childView.setOnClickListener(new OnClickListener() {
			
							@Override
							public void onClick(View v) {
								Intent  intent = new Intent(mContext, CommonActivity.class);
								intent.putExtra("tag", tagId);
								intent.putExtra("tagName", tagName);
								((FragmentActivity) mContext).startActivity(intent);
//								getRootFragment().startActivityForResult(intent, 14341);
								((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
							}
						});
						holder.containerBottom.addView(childView);
					}else{
						holder.containerBottom.setVisibility(View.GONE);	
					}
				}
			}else if("2".equals(theme_type)){//??????
				
				final String styleId = (String) datas.get("style");
				final String supp_label_id = (String) datas.get("supp_label_id");
				List<String> tagIds = (List<String>) datas.get("tags");
				for (int i = 0; i < tagIds.size(); i++) {
					String sql = "select * from friend_circle_tag where _id = " + tagIds.get(i);
					List<HashMap<String, String>> listTag = dbHelp.query(sql);
					if(listTag!=null&&listTag.size()>0){
						holder.containerBottom.setVisibility(View.VISIBLE);
//						View childView = mInflater.inflate(R.layout.intimate_circle_horistol_textview, null);
//						TextView tv = (TextView) childView.findViewById(R.id.top_horizontal_textview);
						TextView childView = (TextView) mInflater.inflate(R.layout.search_label_tv, holder.containerBottom, false);
						final String tagName = listTag.get(0).get("name");
						final String tagId = listTag.get(0).get("_id");
						String tagType = listTag.get(0).get("type");////1???????????????2???????????????
//						if ("1".equals(tagType)) {//1???????????? ????????????
//							tv.setText(tagName);
//							tv.setBackgroundResource(R.drawable.shape_pink_intimate);
//							tv.setTextColor(Color.parseColor("#FF3F8B"));
//						} else {
//							tv.setText(tagName);
//						}
						childView.setText(tagName);
						childView.setTextSize(12);
						childView.setOnClickListener(new OnClickListener() {
			
							@Override
							public void onClick(View v) {
								Intent  intent = new Intent(mContext, CommonActivity.class);
								intent.putExtra("tag", tagId);
								intent.putExtra("tagName", tagName);
								((FragmentActivity) mContext).startActivity(intent);
//								getRootFragment().startActivityForResult(intent, 14341);
								((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
							}
						});
						holder.containerBottom.addView(childView);
					}
				}
				final List<HashMap<String, String>> listStyle = dbHelp
						.query("select * from tag_info where p_id = 2 and is_show = 1 and _id = " + styleId
								+ " order by sequence");//p_id = 2  ??????
				if (listStyle != null && listStyle.size() > 0) {
					final String strStyle = listStyle.get(0).get("attr_name");
//					View childViewStyle = mInflater.inflate(R.layout.intimate_circle_horistol_textview, null);
//					TextView tvStyle = (TextView) childViewStyle.findViewById(R.id.top_horizontal_textview);
//					tvStyle.setBackgroundResource(R.drawable.shape_pink_intimate);
//					tvStyle.setTextColor(Color.parseColor("#FF3F8B"));
					TextView childView = (TextView) mInflater.inflate(R.layout.search_label_tv, holder.containerBottom, false);
					childView.setText(strStyle);
					childView.setTextSize(12);
					holder.containerBottom.addView(childView);
					childView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							HashMap<String, String> map = listStyle.get(0);
							HashMap<String, Object> mapRequest = new HashMap<String, Object>();
							mapRequest.put("style", map);
							
							Intent intent = new Intent(mContext, FilterResultActivity.class);
							intent.putExtra("shop_name", strStyle);
							Bundle bundle = new Bundle();
							bundle.putSerializable("condition", mapRequest);
//							bundle.putString("id", 6 + "");// ??????????????????
//							bundle.putString("title", "??????");
							intent.putExtras(bundle);
							((FragmentActivity) mContext).startActivity(intent);
							((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
						}
					});
				}
				
				String sql = "select * from supp_label where _id = " + supp_label_id + " order by _id";
				final List<HashMap<String, String>> listSupp = dbHelp.query(sql);// ??????
				if (listSupp != null && listSupp.size() > 0) {
					String  strBrand = listSupp.get(0).get("name");
					if(!"??????".equals(strBrand)){//???????????????
//						View childViewSupp = mInflater.inflate(R.layout.intimate_circle_horistol_textview, null);
//						TextView tvSupp = (TextView) childViewSupp.findViewById(R.id.top_horizontal_textview);
//						tvSupp.setBackgroundResource(R.drawable.shape_pink_intimate);
//						tvSupp.setTextColor(Color.parseColor("#FF3F8B"));
						TextView childView = (TextView) mInflater.inflate(R.layout.search_label_tv, holder.containerBottom, false);
						childView.setText(strBrand);
						childView.setTextSize(12);
						holder.containerBottom.addView(childView);
						childView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext, ManufactureActivity.class);
								intent.putExtra("supple_id", supp_label_id);
								((FragmentActivity) mContext).startActivity(intent);
								((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
							}
						});
					}
				}
				
				holder.containerBottom.setVisibility(View.VISIBLE);
			}else if("3".equals(theme_type)){//????????????
				List<String> tagIds = (List<String>) datas.get("tags");
				for (int i = 0; i < tagIds.size(); i++) {
					String sql = "select * from friend_circle_tag where _id = " + tagIds.get(i);
					List<HashMap<String, String>> listTag = dbHelp.query(sql);
					if(listTag!=null&&listTag.size()>0){
						holder.containerBottom.setVisibility(View.VISIBLE);
//						View childView = mInflater.inflate(R.layout.intimate_circle_horistol_textview, null);
//						TextView tv = (TextView) childView.findViewById(R.id.top_horizontal_textview);
						TextView childView = (TextView) mInflater.inflate(R.layout.search_label_tv, holder.containerBottom, false);
						final String tagName = listTag.get(0).get("name");
						final String tagId = listTag.get(0).get("_id");
						String tagType = listTag.get(0).get("type");////1???????????????2???????????????
//						if ("1".equals(tagType)) {//1???????????? ????????????
//							tv.setText(tagName);
//							tv.setBackgroundResource(R.drawable.shape_pink_intimate);
//							tv.setTextColor(Color.parseColor("#FF3F8B"));
//						} else {
//							tv.setText(tagName);
//						}
						childView.setText(tagName);
						childView.setTextSize(12);
						childView.setOnClickListener(new OnClickListener() {
			
							@Override
							public void onClick(View v) {
								Intent  intent = new Intent(mContext, CommonActivity.class);
								intent.putExtra("tag", tagId);
								intent.putExtra("tagName", tagName);
								((FragmentActivity) mContext).startActivity(intent);
//								getRootFragment().startActivityForResult(intent, 14341);
								((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
							}
						});
						holder.containerBottom.addView(childView);
					}else{
						holder.containerBottom.setVisibility(View.GONE);
					}
				}
			}else{//3.4.5????????????????????????????????????????????????
				List<HashMap<String, Object>>  supp_label_list = (List<HashMap<String, Object>>) datas.get("supp_label_list");
				LinkedHashMap<String, String> hasmapSupple1 = new LinkedHashMap<String, String>();//???????????? ??????
				LinkedHashMap<String, String> hasmapSupple2 = new LinkedHashMap<String, String>();//??????????????? ??????
				final LinkedHashMap<String, String> hasmapSuppleOnly2 = new LinkedHashMap<String, String>();//???????????????  only_id 
				LinkedHashMap<String, String> hasmapStyle = new LinkedHashMap<String, String>();
				//??????????????? label_id ????????????  0???????????????????????????????????? 1 ?????????????????????????????????
				final LinkedHashMap<String, String> hasmapRept = new LinkedHashMap<String, String>();
				for (int i = 0; i < supp_label_list.size(); i++) {
					HashMap<String, Object> supp_label_hashmap = supp_label_list.get(i);
					String label_id = (String) supp_label_hashmap.get("label_id");
					String label_type = (String) supp_label_hashmap.get("label_type");
					String only_id = (String) supp_label_hashmap.get("only_id");
					String style = (String) supp_label_hashmap.get("style");
					if("1".equals(label_type)){
						if(!hasmapSupple1.containsKey(label_id)){//???????????? ???????????????????????? ?????????????????????
							hasmapSupple1.put(label_id+"", label_id);
						}
					}else{
						if(!hasmapSupple2.containsKey(label_id)){
							hasmapSupple2.put(label_id+"", label_id);
							hasmapSuppleOnly2.put(label_id+"", only_id);
							hasmapRept.put(label_id+"", "0");
						}else{
							hasmapRept.put(label_id+"", "1");//?????????ID ????????????
						}
					}
					if(!hasmapStyle.containsKey(style)){
						hasmapStyle.put(style+"", style);
					}
				}
				//??????????????????????????? 
				for (int i = 0; i < 3; i++) {
					LinkedHashMap<String, String> hashMap = null;
					if(i==0){
						hashMap= hasmapSupple1;
					}else if(i==1){
						hashMap= hasmapSupple2;
					}else if(i==2){
						hashMap= hasmapStyle;
					}
					Iterator<Entry<String, String>> iterator = hashMap.entrySet().iterator();
					for (int j = 0; j < hashMap.size(); j++) {
						Map.Entry<String,String> entry = iterator.next();
						String id = entry.getKey();
						if(i==0||i==1){//??????
							final boolean isUserdefined = i==1?true:false;//????????????????????????
							final String supp_label_id = id;
							String sql = "select * from supp_label where _id = " + supp_label_id + " order by _id";
							final List<HashMap<String, String>> listSupp = dbHelp.query(sql);// ??????
							if (listSupp != null && listSupp.size() > 0) {
								final String  strBrand = listSupp.get(0).get("name");
								if(!"??????".equals(strBrand)){//???????????????
									TextView childView = (TextView) mInflater.inflate(R.layout.search_label_tv, holder.containerBottom, false);
									childView.setText(strBrand);
									childView.setTextSize(12);
									holder.containerBottom.addView(childView);
									childView.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											if(isUserdefined){
												if("1".equals(hasmapRept.get(supp_label_id))){//?????????????????????only_id ??????????????? ???????????????
													if(shop_list!=null&&shop_list.size()>=20){
														Intent intent2 = new Intent(context,ForceLookActivity.class);
														intent2.putExtra("title","????????????");
														intent2.putExtra("isMoreShop", true);
														intent2.putExtra("themeId", theme_id);
														context.startActivity(intent2);
														((Activity) context).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
													}
												}else{
													Intent intent = new Intent();
													intent = new Intent(mContext, WordSearchResultActivity.class);
													intent.putExtra("isCustomLeable", true);
													intent.putExtra("mTheme_id", theme_id);
													intent.putExtra("only_id", hasmapSuppleOnly2.get(supp_label_id));
													intent.putExtra("label_name", strBrand);
													context.startActivity(intent);
													((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
												}
												return;
											}
											Intent intent = new Intent(mContext, ManufactureActivity.class);
											intent.putExtra("supple_id", supp_label_id);
											((FragmentActivity) mContext).startActivity(intent);
											((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
										}
									});
								}
							}
						}else if(i==2){//??????
							final String styleId = id;
							final List<HashMap<String, String>> listStyle = dbHelp
									.query("select * from tag_info where p_id = 2 and is_show = 1 and _id = " + styleId
											+ " order by sequence");//p_id = 2  ??????
							if (listStyle != null && listStyle.size() > 0) {
								final String strStyle = listStyle.get(0).get("attr_name");
								TextView childView = (TextView) mInflater.inflate(R.layout.search_label_tv, holder.containerBottom, false);
								childView.setText(strStyle);
								childView.setTextSize(12);
								holder.containerBottom.addView(childView);
								childView.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										HashMap<String, String> map = listStyle.get(0);
										HashMap<String, Object> mapRequest = new HashMap<String, Object>();
										mapRequest.put("style", map);
										Intent intent = new Intent(mContext, FilterResultActivity.class);
										intent.putExtra("shop_name", strStyle);
										Bundle bundle = new Bundle();
										bundle.putSerializable("condition", mapRequest);
										intent.putExtras(bundle);
										((FragmentActivity) mContext).startActivity(intent);
										((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
									}
								});
							}
						}
					}
				}
				List<String> tagIds = (List<String>) datas.get("tags");
				for (int i = 0; i < tagIds.size(); i++) {
					String sql = "select * from friend_circle_tag where _id = " + tagIds.get(i);
					List<HashMap<String, String>> listTag = dbHelp.query(sql);
					if(listTag!=null&&listTag.size()>0){
						holder.containerBottom.setVisibility(View.VISIBLE);
						TextView childView = (TextView) mInflater.inflate(R.layout.search_label_tv, holder.containerBottom, false);
						final String tagName = listTag.get(0).get("name");
						final String tagId = listTag.get(0).get("_id");
						String tagType = listTag.get(0).get("type");////1???????????????2???????????????
						childView.setText(tagName);
						childView.setTextSize(12);
						childView.setOnClickListener(new OnClickListener() {
			
							@Override
							public void onClick(View v) {
								Intent  intent = new Intent(mContext, CommonActivity.class);
								intent.putExtra("tag", tagId);
								intent.putExtra("tagName", tagName);
								((FragmentActivity) mContext).startActivity(intent);
								((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
							}
						});
						holder.containerBottom.addView(childView);
					}
				}
				if(supp_label_list.size()>0||tagIds.size()>0){
					holder.containerBottom.setVisibility(View.VISIBLE);
				}else{
					holder.containerBottom.setVisibility(View.GONE);
				}
				
			}
			
			//?????????
//			holder.myGridView.setOnTouchInvalidPositionListener(new OnTouchInvalidPositionListener() {
//	            @Override
//	            public boolean onTouchInvalidPosition(int motionEvent) {
//	                return false; //????????????????????????????????????????????????
//	            }
//	        });
//			holder.myGridView.setPadding(0, 0, 0, 0);
			holder.mainImgIv.setCorners(DP2SPUtil.dp2px(mContext,5));//??????????????????
			if("1".equals(theme_type)&&shop_list!=null){//????????????
//				if(shop_list.size() == 1){
//					holder.myGridView.setNumColumns(1);
//					holder.myGridView.setPadding(0, 0, (width/5)*2-DP2SPUtil.dp2px(mContext, 20), 0);//??????????????????????????? ???????????????(width/5)*3
//				}else if (shop_list.size() == 3||shop_list.size()>4) {
//					holder.myGridView.setNumColumns(3);
//				} else if (shop_list.size() == 2 || shop_list.size() == 4) {
//					holder.myGridView.setNumColumns(2);
//				}
//				GvDataAdapter gvAdapter = new GvDataAdapter(mContext, shop_list,user_id,theme_type);
//				holder.myGridView.setAdapter(gvAdapter);
				if(shop_list.size()>0) {
					if(shop_list.size() >1){
						holder.mainImgCountTv.setVisibility(View.VISIBLE);
						holder.mainImgCountTv.setText(shop_list.size()+"???");
					}else{
						holder.mainImgCountTv.setVisibility(View.GONE);
					}
					ViewGroup.LayoutParams lp = holder.mainImgIv.getLayoutParams();
					lp.width = width-DP2SPUtil.dp2px(mContext,95);
					lp.height = lp.width*900/600;
					holder.mainImgIv.setLayoutParams(lp);
					String shop_code = shop_list.get(0).get("shop_code").toString();
					String url = shop_code.substring(1, 4) + File.separator + shop_code
							+ File.separator + shop_list.get(0).get("def_pic").toString();
					PicassoUtils.initImage(mContext, url + "!450", holder.mainImgIv);
				}else {
					holder.mainImgIv.setVisibility(View.GONE);
				}
			}else{
				String pics = (String) datas.get("pics");
				String picImg[];
				if(TextUtils.isEmpty(pics)){
					picImg = new String[0];
				}else{
					picImg = pics.split(",");
				}
//				if(picImg.length == 1){
//					holder.myGridView.setNumColumns(1);
//					holder.myGridView.setPadding(0, 0, (width/5)*2-DP2SPUtil.dp2px(mContext, 20), 0);//??????????????????????????? ???????????????(width/5)*3
//				}else if (picImg.length == 3||picImg.length>4) {
//					holder.myGridView.setNumColumns(3);
//				} else if (picImg.length == 2 || picImg.length == 4) {
//					holder.myGridView.setNumColumns(2);
//				}
//				GvDataAdapter gvAdapter = new GvDataAdapter(mContext, picImg,user_id,theme_type);
//				holder.myGridView.setAdapter(gvAdapter);


				if(picImg.length >0){
					if(picImg.length >1){
						holder.mainImgCountTv.setVisibility(View.VISIBLE);
						holder.mainImgCountTv.setText(picImg.length+"???");
					}else{
						holder.mainImgCountTv.setVisibility(View.GONE);
					}
					double rate = 1;
					try {
						rate = Double.parseDouble(picImg[0].split(":")[1]);
					} catch (Exception e) {

					}
					ViewGroup.LayoutParams lp = holder.mainImgIv.getLayoutParams();
					lp.width = width-DP2SPUtil.dp2px(mContext,95);
					lp.height = (int) (lp.width/rate);
					holder.mainImgIv.setLayoutParams(lp);
					PicassoUtils.initImage(mContext, myqTheme+"/"+user_id+"/"+picImg[0].split(":")[0]+"!450",  holder.mainImgIv);
					holder.mainImgIv.setVisibility(View.VISIBLE);
				}else {
					holder.mainImgIv.setVisibility(View.GONE);
				}



			}
			
			//???????????????
			//"1".equals((String) datas.get("applaud_status"));//???????????????????????????  false ????????? ture ?????????
			//"1".equals((String) datas.get("attention_status"));//??????????????????????????????  ??????????????????????????????????????????????????? ????????????????????? ???ture
			if("1".equals(mType)){
				if(!YJApplication.instance.isLoginSucess()||(!thisUserId.equals(user_id)&&!"1".equals((String) datas.get("attention_status")))){
					holder.addIntiamte.setVisibility(View.VISIBLE);
					holder.addIntiamteGray.setVisibility(View.GONE);
				}else{
					if(thisUserId.equals(user_id)){
						holder.addIntiamte.setVisibility(View.GONE);
						holder.addIntiamteGray.setVisibility(View.GONE);
					}else{
						holder.addIntiamte.setVisibility(View.GONE);
						holder.addIntiamteGray.setVisibility(View.VISIBLE);
					}
				}
			}else{//??????????????????????????????
				if(thisUserId.equals(user_id)){
					holder.addIntiamte.setVisibility(View.GONE);
					holder.addIntiamteGray.setVisibility(View.GONE);
				}else{
					holder.addIntiamte.setVisibility(View.GONE);
					holder.addIntiamteGray.setVisibility(View.VISIBLE);
				}
			}
			//???????????????   type 1????????????2????????? ?????????????????????
			holder.addIntiamte.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!YJApplication.instance.isLoginSucess()){
						toLogin();
					}else if(!thisUserId.equals(user_id)){//????????????????????????????????????
						sweetAttention((String)datas.get("user_id"),"1",datas,holder.addIntiamte,holder.addIntiamteGray);
					}
				}
			});
			//??????????????????  type 1????????????2????????? ?????????????????????
			holder.addIntiamteGray.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!YJApplication.instance.isLoginSucess()){
						toLogin();
					}else if(!thisUserId.equals(user_id)){//????????????????????????????????????
						sweetAttention((String)datas.get("user_id"),"2",datas,holder.addIntiamte,holder.addIntiamteGray);
					}
				}
			});
			if("1".equals((String) datas.get("applaud_status"))){
				holder.applaudNumIcon.setImageResource(R.drawable.sweet_icon_xihuan_pre);
				holder.applaudNumTv.setTextColor(Color.parseColor("#FF3F8B"));
			}else{
				holder.applaudNumIcon.setImageResource(R.drawable.sweet_icon_xihuan);
				holder.applaudNumTv.setTextColor(Color.parseColor("#A8A8A8"));
			}
			String applaud_num = (String) datas.get("applaud_num");
//			final long applaud_num_long = Long.parseLong(applaud_num);
			holder.applaudNumTv.setText(applaud_num);
			
			String comment_count = (String) datas.get("comment_count");
			if(Long.parseLong(comment_count)<0){
				holder.commentCountTv.setText("0");
			}else{
				holder.commentCountTv.setText(comment_count);
			}
			//?????? ??? ????????????
			holder.dianzanIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!YJApplication.instance.isLoginSucess()){
						toLogin();
						return;
					}
					if (LimitDoubleClicked.isFastDoubleClick500()){
						return;//??????????????????
					}
					if(!"1".equals((String) datas.get("applaud_status"))){//??????
						dianZan(theme_id, "1",theme_id,datas,holder.applaudNumTv,holder.applaudNumIcon); //datas????????? ????????? ????????? ????????????????????? ?????????+1 ??????????????????1??????????????????
					}else{//????????????
						removeZan(theme_id, "1",theme_id,datas,holder.applaudNumTv,holder.applaudNumIcon);
					}
				}
			});
			//??????????????????
			holder.pingjiaIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,SweetFriendsDetails.class);
					intent.putExtra("theme_id", theme_id);
					intent.putExtra("isComment", true);
					((FragmentActivity)mContext).startActivity(intent);
//					getRootFragment().startActivityForResult(intent, 14341);
					((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
				}
			});
			//????????????????????? ?????? ????????????
			if(YJApplication.instance.isLoginSucess()&&thisUserId.equals(user_id)){
				holder.deleteIcon.setVisibility(View.VISIBLE);
				holder.shoucangIcon.setVisibility(View.GONE);
			}else{
				holder.deleteIcon.setVisibility(View.GONE);
				holder.shoucangIcon.setVisibility(View.VISIBLE);
				if("1".equals((String) datas.get("collection_status"))){
					holder.shoucangNumIcon.setImageResource(R.drawable.icon_yishoucang);
//					holder.shoucangNumTv.setTextColor(Color.parseColor("#A8A8A8"));
				}else{
					holder.shoucangNumIcon.setImageResource(R.drawable.icon_shoucang);
//					holder.shoucangNumTv.setTextColor(Color.parseColor("#A8A8A8"));
				}
				String shoucang_num = (String) datas.get("collect_num");
				holder.shoucangNumTv.setText(shoucang_num);
			}
			//????????????
			holder.deleteIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					deleteData(position,theme_id);
				}
			});
			//?????????????????????
			holder.shoucangIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!YJApplication.instance.isLoginSucess()){
						toLogin();
						return;
					}
					if (LimitDoubleClicked.isFastDoubleClick500()){
						return;//??????????????????
					}
					if(!"1".equals((String) datas.get("collection_status"))){//??????
						addCollect(theme_id,datas,holder.shoucangNumTv,holder.shoucangNumIcon); 
					}else{//????????????
						delCollect(theme_id,datas,holder.shoucangNumTv,holder.shoucangNumIcon);
					}
				}
			});
			//????????????
			if(shop_list.size()>0&&!"1".equals(theme_type)){//???????????? ????????? ????????????
				holder.custGallery.setData(shop_list);
				holder.custGallery.setVisibility(View.VISIBLE);
			}else{
				holder.custGallery.setVisibility(View.GONE);
			}
			//??????????????????????????????
			List<HashMap<String, String>> comments_list = (List<HashMap<String, String>>)datas.get("comments_list");
			addComments(holder.containerComments,comments_list);
			
			
			holder.contentTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,SweetFriendsDetails.class);
					intent.putExtra("theme_id", theme_id);
					((FragmentActivity)mContext).startActivity(intent);
					((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,SweetFriendsDetails.class);
					intent.putExtra("theme_id", theme_id);
					((FragmentActivity)mContext).startActivity(intent);
//					getRootFragment().startActivityForResult(intent, 14341);
					((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
				}
			});
//			holder.myGridView.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					Intent intent = new Intent(mContext,SweetFriendsDetails.class);
//					intent.putExtra("theme_id", theme_id);
//					((FragmentActivity)mContext).startActivity(intent);
//					((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
//				}
//
//			});

			return convertView;
		}

		/**
		 * ??????????????? ????????????????????? theme_id:?????????????????????ID
		 * @date 2017???3???27?????????2:26:19
		 */
		private void deleteData(final int position,final String theme_id){
			final Dialog dialog = new Dialog(mContext, R.style.DialogQuheijiao2);
			View view = View.inflate(mContext, R.layout.delete_my_intimate_data_dialog, null);
			view.findViewById(R.id.icon_close).setOnClickListener(new OnClickListener() {
				// ?????? ??????
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			view.findViewById(R.id.btn_left).setOnClickListener(new OnClickListener() {
				// ?????? ??????
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			view.findViewById(R.id.btn_right).setOnClickListener(new OnClickListener() {
				// ????????????
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					listData.remove(position);
					notifyData();
					deleteTheme(theme_id);
				}
			});

			// // ?????????????????????dialog
			dialog.setContentView(view,
					new LinearLayout.LayoutParams(DP2SPUtil.dp2px(mContext, 280), LinearLayout.LayoutParams.MATCH_PARENT));
			dialog.show();
		}
		private void notifyData(){
			this.notifyDataSetChanged();
		}
		class Holder {
			CustImageGalleryIntimate custGallery;// ????????????
			ImageView imgHead;
//			LinearLayout containerBottom;
//			MyGridView2 myGridView;
			RoundCornersImageView mainImgIv;
			TextView nickNameTv,locationTv,sendTimeTv,commentCountTv,applaudNumTv,shoucangNumTv,mainImgCountTv;
			FolderTextView contentTv;
			ImageView imgHeadV,applaudNumIcon,shoucangNumIcon;
			View addIntiamte;//?????????
			View addIntiamteGray;//??????????????????
			View dianzanIcon,pingjiaIcon,deleteIcon,shoucangIcon;
			FlowLayout containerBottom;
			LinearLayout containerComments;//???????????????
		}

//		class HolderGV {
//			ImageView img_big, img_small,img_big_one;
//		}

        private String myqTheme = "myq/theme";//????????????????????????
//		????????????????????? ??????
//		class GvDataAdapter extends BaseAdapter {
//			private Context context;
//			private String[] listData;
//			private List<HashMap<String, Object>> shop_list;
//			private String theme_type;
//			private String user_id;//???????????????ID
//
//
//			/**
//			 * @param context
//			 * @param listData
//			 */
//			public GvDataAdapter(Context context, String[] listData,String user_id,String theme_type) {
//				super();
//				this.context = context;
//				this.listData = listData;
//				this.user_id = user_id;
//				this.theme_type = theme_type;
//			}
//			public GvDataAdapter(Context context,List<HashMap<String, Object>> shop_list ,String user_id,String theme_type) {
//				super();
//				this.context = context;
//				this.shop_list = shop_list;
//				this.user_id = user_id;
//				this.theme_type = theme_type;
//			}
//
//			@Override
//			public int getCount() {
//				// TODO Auto-generated method stub
//				if("1".equals(theme_type)){
//					return shop_list.size();
//				}else{
//					return listData.length;
//				}
//			}
//
//			@Override
//			public Object getItem(int position) {
//				// TODO Auto-generated method stub
//				if("1".equals(theme_type)){
//					return shop_list.get(position);
//				}else{
//					return listData[position];
//				}
//
//			}
//
//			@Override
//			public long getItemId(int position) {
//				// TODO Auto-generated method stub
//				return position;
//			}
//
//			@Override
//			public View getView(final int position, View convertView, ViewGroup parent) {
//				// TODO Auto-generated method stub
//				HolderGV holder = null;
//				if (convertView == null) {
//					holder = new HolderGV();
//					convertView = mInflater.inflate(R.layout.intimate_cicle_item_img_list, null);
//					holder.img_big = (ImageView) convertView.findViewById(R.id.img_big);
//					holder.img_big_one = (ImageView) convertView.findViewById(R.id.img_big_one);
//					holder.img_small = (ImageView) convertView.findViewById(R.id.img_small);
//					convertView.setTag(holder);
//
//				} else {
//					holder = (HolderGV) convertView.getTag();
//				}
//				if(getCount() == 1){
//					int img_big_one_width = (width/5)*3;
//					int img_big_one_maxheigth = (width/10)*9;
//					holder.img_big_one.setVisibility(View.VISIBLE);
//					holder.img_big.setVisibility(View.GONE);
//					holder.img_small.setVisibility(View.GONE);
//					if("1".equals(theme_type)){
//						ViewGroup.LayoutParams lp = holder.img_big_one.getLayoutParams();
//						lp.width = img_big_one_width;
//						holder.img_big_one.setMaxHeight(img_big_one_maxheigth);
//						holder.img_big_one.setLayoutParams(lp);
//						String shop_code = shop_list.get(position).get("shop_code").toString();
//						String url = shop_code.substring(1, 4)+File.separator+shop_code
//								+File.separator+shop_list.get(position).get("def_pic").toString();
////						SetImageLoader.initImageLoader(mContext, holder.img_small,url, "!450");
//						PicassoUtils.initImage(mContext,  url+"!450",holder.img_big_one);
//					}else{
////						SetImageLoader.initImageLoader(mContext, holder.img_small, myqTheme+"/"+user_id+"/"+listData[position].split(":")[0], "!450");
//						double rate = 1;
//						try {
//							rate = Double.parseDouble(listData[position].split(":")[1]);
//						} catch (Exception e) {
//
//						}
//						ViewGroup.LayoutParams lp = holder.img_big_one.getLayoutParams();
//						lp.width = img_big_one_width;
//						lp.height = (int) (lp.width/rate>img_big_one_maxheigth?img_big_one_maxheigth:lp.width/rate);
//						holder.img_big_one.setLayoutParams(lp);
//						PicassoUtils.initImage(mContext, myqTheme+"/"+user_id+"/"+listData[position].split(":")[0]+"!450",  holder.img_big_one);
//					}
//
//				}else if (getCount() == 3||getCount()>4) {
//					ViewGroup.LayoutParams lp = holder.img_small.getLayoutParams();
//					lp.width = width/3-DP2SPUtil.dp2px(mContext, 7);
//					lp.height =  width/3-DP2SPUtil.dp2px(mContext, 7);
//					holder.img_small.setLayoutParams(lp);
//					holder.img_small.setVisibility(View.VISIBLE);
//					holder.img_big.setVisibility(View.GONE);
//					holder.img_big_one.setVisibility(View.GONE);
//					if("1".equals(theme_type)){
//						String shop_code = shop_list.get(position).get("shop_code").toString();
//						String url = shop_code.substring(1, 4)+File.separator+shop_code
//								+File.separator+shop_list.get(position).get("def_pic").toString();
////						SetImageLoader.initImageLoader(mContext, holder.img_small,url, "!450");
//						PicassoUtils.initImage(mContext,  url+"!382",holder.img_small);
//					}else{
////						SetImageLoader.initImageLoader(mContext, holder.img_small, myqTheme+"/"+user_id+"/"+listData[position].split(":")[0], "!450");
//						PicassoUtils.initImage(mContext, myqTheme+"/"+user_id+"/"+listData[position].split(":")[0]+"!382",  holder.img_small);
//
//					}
//				} else if (getCount() == 2 || getCount() == 4) {
//					ViewGroup.LayoutParams lp = holder.img_big.getLayoutParams();
//					lp.width = width/2-DP2SPUtil.dp2px(mContext, 9);
//					lp.height =  width/2-DP2SPUtil.dp2px(mContext, 9);
//					holder.img_big.setLayoutParams(lp);
//					holder.img_small.setVisibility(View.GONE);
//					holder.img_big.setVisibility(View.VISIBLE);
//					holder.img_big_one.setVisibility(View.GONE);
//					if("1".equals(theme_type)){
//						String shop_code = shop_list.get(position).get("shop_code").toString();
//						String url = shop_code.substring(1, 4)+File.separator+shop_code
//								+File.separator+shop_list.get(position).get("def_pic").toString();
////						SetImageLoader.initImageLoader(mContext, holder.img_big,url, "!450");
//						PicassoUtils.initImage(mContext, url+"!382", holder.img_big);
//
//					}else{
//						PicassoUtils.initImage(mContext, myqTheme + "/" + user_id + "/" + listData[position].split(":")[0]+"!382", holder.img_big);
//
////						SetImageLoader.initImageLoader(mContext, holder.img_big, myqTheme+"/"+user_id+"/"+listData[position].split(":")[0], "!450");
//					}
//				} else {
//					holder.img_small.setVisibility(View.GONE);
//					holder.img_big.setVisibility(View.GONE);
//					holder.img_big_one.setVisibility(View.GONE);
//				}
//
////				convertView.setOnClickListener(new OnClickListener() {
////
////					@Override
////					public void onClick(View v) {
////						ArrayList<String> imageList = new ArrayList<String>();
////						if("1".equals(theme_type)){
////							for (int i = 0; i < shop_list.size(); i++) {
////								String shop_code = shop_list.get(i).get("shop_code").toString();
////								String url = shop_code.substring(1, 4)+File.separator+shop_code
////										+File.separator+shop_list.get(i).get("def_pic").toString();
////								imageList.add(url);
////							}
////						}else{
////							for (int i = 0; i < listData.length; i++) {
////								String url =  myqTheme+"/"+user_id+"/"+listData[i].split(":")[0];
////								imageList.add(url);
////							}
////						}
////
////						Intent intent = new Intent(mContext,ShowIntimateImageActivity.class);
////						intent.putStringArrayListExtra("imageList", imageList);
////						intent.putExtra("indext", position);
////						((FragmentActivity)mContext).startActivity(intent);
////					}
////				});
//				return convertView;
//
//			}
//
//		}
	}
	
	
	/**
	 * ????????????
	 * 
	 * @param this_id
	 *            ??????id?????????id
	 * @param type:1
	 *            ?????????2 ??????
	 */
	public void dianZan(final String this_id, final String type,final String theme_id,final HashMap<String, Object> datas , final TextView applaudTv,final ImageView applaudIv) {
		new SAsyncTask<String, Void, ReturnInfo>((FragmentActivity) mContext, 0) {

			protected ReturnInfo doInBackground(FragmentActivity mContext, String... params) throws Exception {

				return ComModel2.sweetZan(mContext, this_id, type,theme_id);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity mContext, ReturnInfo result, Exception e) {
				super.onPostExecute(mContext, result, e);
				if (null == e) {
					applaudTv.setText((Long.parseLong((String) datas.get("applaud_num"))+1)+"");	
					applaudTv.setTextColor(Color.parseColor("#FF3F8B"));
					applaudIv.setImageResource(R.drawable.sweet_icon_xihuan_pre);
					datas.put("applaud_status", "1");
					datas.put("applaud_num", Long.parseLong((String) datas.get("applaud_num"))+1+"");
				}
			}

		}.execute();
	}

	/**
	 * ???????????????
	 * 
	 * @param this_id
	 *            ??????id?????????id
	 * @param type:1
	 *            ?????????2 ??????
	 */
	public void removeZan(final String this_id, final String type,final String theme_id,final HashMap<String, Object> datas , final TextView applaudTv,final ImageView applaudIv) {
		new SAsyncTask<String, Void, ReturnInfo>((FragmentActivity) mContext, 0) {

			protected ReturnInfo doInBackground(FragmentActivity mContext, String... params) throws Exception {

				return ComModel2.sweetRemoveZan(mContext, this_id, type,theme_id);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity mContext, ReturnInfo result, Exception e) {
				super.onPostExecute(mContext, result, e);
				if (null == e) {
					applaudTv.setText((Long.parseLong((String) datas.get("applaud_num"))-1)+"");
					applaudTv.setTextColor(Color.parseColor("#A8A8A8"));
					applaudIv.setImageResource(R.drawable.sweet_icon_xihuan);
					datas.put("applaud_status", "0");
					datas.put("applaud_num", Long.parseLong((String) datas.get("applaud_num"))-1+"");
				}
			}

		}.execute();
	}
	/**
	 * 
	 * @param friend_user_id ????????????ID
	 * @param type  type 1????????????2????????? ?????????????????????
	 * @param datas
	 * @param addIntiamte  ????????? ?????????
	 * @param addIntiamteGray ?????????
	 */
	public void sweetAttention(final String friend_user_id, final String type,final HashMap<String, Object> datas , final View addIntiamte,final View addIntiamteGray) {
		new SAsyncTask<String, Void, ReturnInfo>((FragmentActivity) mContext, 0) {
			
			protected ReturnInfo doInBackground(FragmentActivity mContext, String... params) throws Exception {
				
				return ComModel2.sweetAttention(mContext, friend_user_id, type);
			}
			
			@Override
			protected boolean isHandleException() {
				return true;
			}
			
			@Override
			protected void onPostExecute(FragmentActivity mContext, ReturnInfo result, Exception e) {
				super.onPostExecute(mContext, result, e);
				if (null == e) {
					if("1".equals(type)){//???????????????
						datas.put("attention_status", "1");
						addIntiamte.setVisibility(View.GONE);
						addIntiamteGray.setVisibility(View.VISIBLE);
					}else if("2".equals(type)){//??????????????????
						datas.put("attention_status", "0");
						addIntiamte.setVisibility(View.VISIBLE);
						addIntiamteGray.setVisibility(View.GONE);
					}
				}
			}
			
		}.execute();
	}
	
	/**
	 * ?????????????????????
	 * @date 2017???3???31?????????9:16:26
	 */
	public void deleteTheme(final String theme_id) {
		new SAsyncTask<String, Void, ReturnInfo>((FragmentActivity) mContext, 0) {
			
			protected ReturnInfo doInBackground(FragmentActivity mContext, String... params) throws Exception {
				
				return ComModel2.deleteTheme(mContext, theme_id);
			}
			
			@Override
			protected boolean isHandleException() {
				return true;
			}
			
			@Override
			protected void onPostExecute(FragmentActivity mContext, ReturnInfo result, Exception e) {
				super.onPostExecute(mContext, result, e);
				if (null == e) {
					
				}
			}
			
		}.execute();
	}
	
	/**
	 * ????????????
	 */
	public void addCollect(final String theme_id,
			final HashMap<String, Object> datas, final TextView shoucangNumTv, final ImageView shoucangNumIcon) {
		new SAsyncTask<String, Void, ReturnInfo>((FragmentActivity) mContext, 0) {

			protected ReturnInfo doInBackground(FragmentActivity mContext, String... params) throws Exception {

				return ComModel2.addCollect(mContext, theme_id);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity mContext, ReturnInfo result, Exception e) {
				super.onPostExecute(mContext, result, e);
				if (null == e) {
					ToastUtil.showShortText(mContext, "????????????");
					shoucangNumTv.setText((Long.parseLong((String) datas.get("collect_num")) + 1) + "");
//					applaudTv.setTextColor(Color.parseColor("#FF3F8B"));
					shoucangNumIcon.setImageResource(R.drawable.icon_yishoucang);
					datas.put("collection_status", "1");
					datas.put("collect_num", Long.parseLong((String) datas.get("collect_num")) + 1 + "");
				}
			}

		}.execute();
	}

	/**
	 * ??????????????????
	 * 
	 */
	public void delCollect(final String theme_id,
			final HashMap<String, Object> datas, final TextView shoucangNumTv, final ImageView shoucangNumIcon) {
		new SAsyncTask<String, Void, ReturnInfo>((FragmentActivity) mContext, 0) {

			protected ReturnInfo doInBackground(FragmentActivity mContext, String... params) throws Exception {

				return ComModel2.delCollect(mContext, theme_id);
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}

			@Override
			protected void onPostExecute(FragmentActivity mContext, ReturnInfo result, Exception e) {
				super.onPostExecute(mContext, result, e);
				if (null == e) {
					shoucangNumTv.setText((Long.parseLong((String) datas.get("collect_num")) - 1) + "");
//					applaudTv.setTextColor(Color.parseColor("#A8A8A8"));
					shoucangNumIcon.setImageResource(R.drawable.icon_shoucang);
					datas.put("collection_status", "0");
					datas.put("collect_num", Long.parseLong((String) datas.get("collect_num")) - 1 + "");
				}
			}

		}.execute();
	}
	
	/**
	 * ?????????????????? ????????????
	 */
	private void addComments(LinearLayout containerComments,List<HashMap<String, String>> comments_list) {
		if(comments_list!=null&&comments_list.size()>0){
			containerComments.removeAllViews();
			containerComments.setVisibility(View.VISIBLE);
			int length = comments_list.size()<5?comments_list.size():5;//???????????????????????????
			SpannableString ssComment;
			for (int i = 0; i <length; i++) {
				TextView tv = (TextView) mInflater.inflate(R.layout.intimate_list_comments_layout, null);
				String comments_type = comments_list.get(i).get("comments_type");
				if("1".equals(comments_type)){//????????????
					String nickname= comments_list.get(i).get("nickname");
					String comment = nickname+": "+comments_list.get(i).get("content");
					ssComment= new SpannableString(comment);
					ssComment.setSpan(new ForegroundColorSpan(Color.parseColor("#FF3F8B")), 0, nickname.length()+1,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv.setText(ssComment);
				}else if("2".equals(comments_type)){//????????????
					String send_nickname = comments_list.get(i).get("send_nickname");
					String receive_nickname = comments_list.get(i).get("receive_nickname");
					String comment="";
					if(TextUtils.isEmpty(receive_nickname)){
						comment = send_nickname +": "+comments_list.get(i).get("content");
						ssComment= new SpannableString(comment);
						ssComment.setSpan(new ForegroundColorSpan(Color.parseColor("#FF3F8B")), 0, send_nickname.length()+1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						tv.setText(ssComment);
					}else{
						comment = send_nickname + "??????" +receive_nickname+": "+comments_list.get(i).get("content");
						ssComment= new SpannableString(comment);
						ssComment.setSpan(new ForegroundColorSpan(Color.parseColor("#FF3F8B")), 0, send_nickname.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						ssComment.setSpan(new ForegroundColorSpan(Color.parseColor("#FF3F8B")), send_nickname.length()+2, send_nickname.length()+2+send_nickname.length()+1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						tv.setText(ssComment);
					}
				}
				containerComments.addView(tv);
			}
		}else{
			containerComments.setVisibility(View.GONE);
		}
	}
	/**
	 * ?????????
	 */
	private void toLogin(){
		Intent intent = new Intent(mContext, LoginActivity.class);
		intent.putExtra("login_register", "login");
		((FragmentActivity)mContext).startActivity(intent);
		((FragmentActivity)mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_match);
	}
//	/**
//	 * ?????????Fragment
//	 * 
//	 * @return
//	 */
//	private Fragment getRootFragment() {
//		Fragment fragment = getParentFragment();
//		while (fragment.getParentFragment() != null) {
//			fragment = fragment.getParentFragment();
//		}
//		return fragment;
//
//	}

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == 14341) {
//			if (resultCode == 14342) { //???????????????
//				if(curPager != 1){
//					mListView.scrollTo(0, 0);
//					mListView.setSelection(0);
//				}
//				refresh();
//			}
//
//		}
//
//	}
	
}
