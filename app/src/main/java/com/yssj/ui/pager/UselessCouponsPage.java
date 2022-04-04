package com.yssj.ui.pager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yssj.activity.R;
import com.yssj.app.SAsyncTask;
import com.yssj.custom.view.LoadingDialog;
import com.yssj.entity.MyCoupon;
import com.yssj.model.ComModel2;
import com.yssj.ui.adpter.MyCouponsPagerAdapter;
import com.yssj.ui.base.BasePager;

public class UselessCouponsPage extends BasePager {
	private HashMap<String, Object> map;

	public UselessCouponsPage(Context context) {
		super(context);
	}

	private PullToRefreshListView lv_common;
	private int currPage = 1;
	private MyCouponsPagerAdapter mAdapter;
	
	private boolean flag = true;		// 判断上拉、下拉的标志
	
	private LinearLayout account_nodata;
	private Button btn_view_allcircle;
	private TextView tv_qin,tv_no_join;

	@Override
	public View initView() {
		view = View.inflate(context, R.layout.pulltorefreshlistview_coupons, null);
		lv_common = (PullToRefreshListView) view.findViewById(R.id.lv_common);
		
		account_nodata = (LinearLayout) view.findViewById(R.id.account_nodata);
		
		btn_view_allcircle = (Button) view.findViewById(R.id.btn_view_allcircle);
		btn_view_allcircle.setVisibility(View.GONE);
		
		tv_qin = (TextView) view.findViewById(R.id.tv_qin);
		tv_qin.setText("亲，暂时没有相关数据哦~");
		tv_no_join = (TextView) view.findViewById(R.id.tv_no_join);
		tv_no_join.setText("暂无优惠券");
		tv_no_join.setVisibility(View.GONE);
		
		return view;
	}
	
	@Override
	public void initData() {
		currPage = 1 ;
		queryUserlessConponsData();
		mAdapter = new MyCouponsPagerAdapter(context);
		lv_common.setAdapter(mAdapter);
		
		super.initIndicator(lv_common);
		
		lv_common.setMode(Mode.BOTH);
		
		lv_common.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
					flag = true;
					
					currPage = 1 ;
					queryUserlessConponsData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
					flag = false;
					currPage ++;
					queryUserlessConponsData();
			}
		});
	}
	
	
	
	private void queryUserlessConponsData() {
		
		new SAsyncTask<Integer, Void, List<HashMap<String, Object>>>(
				(FragmentActivity) context, R.string.wait) {
			
			
			
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				LoadingDialog.show((FragmentActivity) context);
			}
			
			
			
			@Override
			protected List<HashMap<String, Object>> doInBackground(FragmentActivity context,
					Integer... params) throws Exception {
				return ComModel2.getMyCouponList(context, currPage,"<","2","");
			}

			@Override
			protected boolean isHandleException() {
				return true;
			}
			
			@Override
			protected void onPostExecute(FragmentActivity context,
					List<HashMap<String, Object>> result, Exception e) {
				super.onPostExecute(context, result, e);
				
				if (e != null) {// 查询异常
					account_nodata.setVisibility(View.VISIBLE);
					lv_common.setVisibility(View.GONE);
					lv_common.onRefreshComplete();
				} else {
					
					if(flag){
						
						if(result != null && (result.size() == 0 || result.isEmpty())){
							account_nodata.setVisibility(View.VISIBLE);
							lv_common.setVisibility(View.GONE);
						}
						
						mAdapter.addItemFirst(result!=null?result:null);
					}else{
						mAdapter.addItemLast(result!=null?result:null);
					}
					
					mAdapter.notifyDataSetChanged();
					lv_common.onRefreshComplete();
				}
			}

		}.execute(currPage);
	}


}
