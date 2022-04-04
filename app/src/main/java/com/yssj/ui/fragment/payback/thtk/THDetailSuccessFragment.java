package com.yssj.ui.fragment.payback.thtk;

import java.text.DecimalFormat;

import org.apache.commons.lang.time.DateFormatUtils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yssj.activity.R;
import com.yssj.entity.ReturnShop;
import com.yssj.huanxin.activity.ChatAllHistoryActivity;
import com.yssj.ui.activity.infos.FundDetailsActivity;
import com.yssj.ui.base.BaseFragment;
import com.yssj.utils.WXminiAppUtil;

/**
 * 退货退款，退货成功
 * @author Administrator
 *
 */
public class THDetailSuccessFragment extends BaseFragment {
	
	private TextView tvTitle_base;
	private LinearLayout img_back;
	private ImageView img_right_icon;
	
	private TextView tv_order_je,tv_order_code,tv_end_date,tv_apply_date,tv_return_money,tv_th_status,tv_th_je,tv_th_reason;
	
	private Button btn_view_record;
	
	private ReturnShop returnShop;
	
	@Override
	public View initView() {
		view = View.inflate(context, R.layout.activity_payback_th_detail_success, null);
		view.setBackgroundColor(Color.WHITE);
		tvTitle_base = (TextView) view.findViewById(R.id.tvTitle_base);
		tvTitle_base.setText("退货详情");
		img_back = (LinearLayout) view.findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		
		img_right_icon = (ImageView) view.findViewById(R.id.img_right_icon);
		img_right_icon.setVisibility(View.VISIBLE);
		img_right_icon.setImageResource(R.drawable.mine_message_center);
		img_right_icon.setOnClickListener(this);
		
		tv_order_je = (TextView) view.findViewById(R.id.tv_order_je);	// 订单金额
		tv_order_code = (TextView) view.findViewById(R.id.tv_order_code);	// 订单号码
		tv_end_date = (TextView) view.findViewById(R.id.tv_end_date);	// 完成时间
		tv_apply_date = (TextView) view.findViewById(R.id.tv_apply_date);	// 完成时间
		
		tv_return_money = (TextView) view.findViewById(R.id.tv_return_money);	// 退货金额
		
		tv_th_status = (TextView) view.findViewById(R.id.tv_th_status);	// 退货状态
		tv_th_je = (TextView) view.findViewById(R.id.tv_th_je);	// 退货金额
		tv_th_reason = (TextView) view.findViewById(R.id.tv_th_reason);	// 退货金额
		
		btn_view_record = (Button) view.findViewById(R.id.btn_view_record);
		btn_view_record.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void initData() {
		Bundle bundle = getArguments();
		if(bundle != null){
			returnShop = (ReturnShop) bundle.getSerializable("returnShop");
			if(returnShop != null){
				
				String money = new DecimalFormat("0.00").format(returnShop.getMoney());
				
				tv_order_je.setText("订单金额（包邮):¥" + new DecimalFormat("#0.00").format(returnShop.getMoney()));
				tv_order_code.setText("订单号:" + returnShop.getOrder_code());
				tv_end_date.setText("完成时间:" + DateFormatUtils.format(returnShop.getEnd_time(), "yyyy-MM-dd HH:mm:ss"));
				tv_apply_date.setText(DateFormatUtils.format(returnShop.getEnd_time(), "yyyy-MM-dd HH:mm:ss"));
				
				tv_return_money.setText("商家已收到你退的商品，交易款项" + money + "已归还至您的账户");
				
				tv_th_status.setText("退货成功");

				tv_th_je.setText(money + "元");
				tv_th_reason.setText(returnShop.getCause());
			}
		}
	}
	
	
	@Override
	public void onClick(View v) {
		Intent intent; 
		switch (v.getId()) {
		case R.id.img_back:
			getActivity().finish();
			break;
		case R.id.btn_view_record:		// 查看退款记录
			intent = new Intent(context, FundDetailsActivity.class);
			intent.putExtra("index", 2);
			startActivity(intent);
			break;
		case R.id.img_right_icon:// 消息盒子
			WXminiAppUtil.jumpToWXmini(getActivity());

			break;
		}

	}
	


}
