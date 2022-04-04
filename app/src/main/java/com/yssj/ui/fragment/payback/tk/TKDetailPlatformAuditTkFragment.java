package com.yssj.ui.fragment.payback.tk;

import java.text.DecimalFormat;

import org.apache.commons.lang.time.DateFormatUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yssj.activity.R;
import com.yssj.entity.ReturnShop;
import com.yssj.huanxin.activity.ChatAllHistoryActivity;
import com.yssj.ui.base.BaseFragment;
import com.yssj.utils.WXminiAppUtil;

/**
 * 退款，平台审核退款
 * 
 * @author Administrator
 * 
 */
public class TKDetailPlatformAuditTkFragment extends BaseFragment {

	private TextView tvTitle_base;
	private LinearLayout img_back;
	private ImageView img_right_icon;

	private TextView tv_order_je, tv_order_code, tv_apply_date, tv_end_time,
			tv_return_money, tv_tk_status, tv_tk_categroy, tv_tk_je,
			tv_tk_reason;

	private ReturnShop returnShop;

	@Override
	public View initView() {
		view = View.inflate(context,
				R.layout.activity_payback_tk_detail_auditpass, null);

		tvTitle_base = (TextView) view.findViewById(R.id.tvTitle_base);
		tvTitle_base.setText("退款详情");
		img_back = (LinearLayout) view.findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		img_right_icon = (ImageView) view.findViewById(R.id.img_right_icon);
		img_right_icon.setVisibility(View.VISIBLE);
		img_right_icon.setImageResource(R.drawable.mine_message_center);
		img_right_icon.setOnClickListener(this);
		img_right_icon.setVisibility(View.GONE);

		tv_order_je = (TextView) view.findViewById(R.id.tv_order_je); // 订单金额
		tv_order_code = (TextView) view.findViewById(R.id.tv_order_code); // 订单号码
		tv_apply_date = (TextView) view.findViewById(R.id.tv_apply_date); // 换货时间
		tv_end_time = (TextView) view.findViewById(R.id.tv_end_time); // 结束时间

		tv_return_money = (TextView) view.findViewById(R.id.tv_return_money); // 退款金额
		tv_tk_status = (TextView) view.findViewById(R.id.tv_tk_status); // 退款状态
		tv_tk_categroy = (TextView) view.findViewById(R.id.tv_tk_categroy); // 退款类型
		tv_tk_je = (TextView) view.findViewById(R.id.tv_tk_je); // 退款金额
		tv_tk_reason = (TextView) view.findViewById(R.id.tv_tk_reason); // 退款原因

		return view;
	}

	@Override
	public void initData() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			returnShop = (ReturnShop) bundle.getSerializable("returnShop");
			if (returnShop != null) {
				tv_order_je.setText("订单金额（包邮):¥"
						+ new DecimalFormat("#0.00").format(returnShop
								.getMoney()));
				tv_order_code.setText("订单号:" + returnShop.getOrder_code());

				if (null != returnShop.getEnd_time() && !"null".equals(returnShop.getEnd_time())) {
					tv_apply_date.setText("完成时间:"
							+ DateFormatUtils.format(returnShop.getEnd_time(),
									"yyyy-MM-dd HH:mm:ss"));
					tv_end_time.setText(DateFormatUtils.format(
							returnShop.getEnd_time(), "yyyy-MM-dd HH:mm:ss"));
				}
				tv_tk_status.setText("退款中");
				Integer return_type = returnShop.getReturn_type();

				String category = "";
				if (return_type == 1) {
					category = "换货";
				} else if (return_type == 2) {
					category = "退货";
				} else if (return_type == 2) {
					category = "退款";
				}

				tv_tk_categroy.setText(category);

				tv_tk_je.setText(new DecimalFormat("#0.00").format(returnShop
						.getMoney()) + "元");
				tv_tk_reason.setText(returnShop.getCause());
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			getActivity().finish();
			break;
		case R.id.img_right_icon:// 消息盒子
			WXminiAppUtil.jumpToWXmini(getActivity());

			break;

		}

	}

}
