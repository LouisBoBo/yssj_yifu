package com.yssj.ui.adpter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yssj.activity.R;
import com.yssj.app.SAsyncTask;
import com.yssj.entity.MyBankCard;
import com.yssj.entity.ReturnInfo;
import com.yssj.model.ComModel;
import com.yssj.model.ComModel2;
import com.yssj.ui.activity.infos.MyWalletActivity;
import com.yssj.utils.ToastUtil;

public class MyBankCardListAdapter extends BaseAdapter {
    private Context context;
    private List<MyBankCard> list = new ArrayList<MyBankCard>();
    protected AlertDialog dialog;

    private int bankIcons[] = {R.drawable.zg_bank_icon, R.drawable.ny_bank_icon, R.drawable.gs_bank_icon, R.drawable.js_bank_icon, R.drawable.jt_bank_icon, R.drawable.yz_bank_icon,
            R.drawable.zs_bank_icon, R.drawable.zx_bank_icon, R.drawable.pf_bank_icon, R.drawable.gd_bank_icon};


    public MyBankCardListAdapter(Context context, List<MyBankCard> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.mywallet_bank_list_item, null);
            holder.iv_bank_icon = (ImageView) convertView.findViewById(R.id.iv_bank_icon);
            holder.tv_bank_name = (TextView) convertView.findViewById(R.id.tv_bank_name);
            holder.tv_bank_num = (TextView) convertView.findViewById(R.id.tv_bank_num);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String bank_name = list.get(position).getBank_name();

        if ("????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(R.drawable.icon_wechat);

        } else if ("????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(bankIcons[0]);
        } else if ("????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(bankIcons[1]);
        } else if ("????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(bankIcons[2]);
        } else if ("????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(bankIcons[3]);
        } else if ("????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(bankIcons[4]);
        } else if ("????????????".equals(bank_name) || "????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(bankIcons[5]);
        } else if ("????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(bankIcons[6]);
        } else if ("????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(bankIcons[7]);
        } else if ("????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(bankIcons[8]);
        } else if ("????????????".equals(bank_name)) {
            holder.iv_bank_icon.setImageResource(bankIcons[9]);
        } else {
//			??????????????????????????????
            holder.iv_bank_icon.setImageResource(bankIcons[9]);
        }

        holder.tv_bank_name.setText(list.get(position).getBank_name());
        holder.tv_bank_num.setText("**" + list.get(position).getBank_no());


        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"????????????".equals(bank_name)){
                    customDialog(list.get(position).getId().toString(), position);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView iv_bank_icon;
        TextView tv_bank_name;
        TextView tv_bank_num;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    private void customDialog(final String id, final int position) {
        AlertDialog.Builder builder = new Builder(context);
        // ???????????????????????????
        View view = View.inflate(context, R.layout.payback_esc_apply_dialog,
                null);
        TextView tv_des = (TextView) view.findViewById(R.id.tv_des);

//		tv_des.setText("  ?????????????????????????????????");
        tv_des.setText("  ??????????????????????????????");
        Button ok = (Button) view.findViewById(R.id.ok);
        ok.setBackgroundResource(R.drawable.payback_esc_apply_esc);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ???????????????????????????
                dialog.dismiss();

            }
        });

        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteBankCard(id, position);
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }


    private void deleteBankCard(final String id, final int position) {
        new SAsyncTask<Void, Void, ReturnInfo>((FragmentActivity) context, R.string.wait) {

            @Override
            protected ReturnInfo doInBackground(FragmentActivity context,
                                                Void... params) throws Exception {
                return ComModel.delMybankCard(context, id);
            }

            @Override
            protected boolean isHandleException() {
                return true;
            }

            @Override
            protected void onPostExecute(FragmentActivity context,
                                         ReturnInfo result, Exception e) {
                super.onPostExecute(context, result, e);
                if (null == e) {
                    if (result != null && "1".equals(result.getStatus())) {
                        ToastUtil.showShortText(context, result.getMessage());
                        list.remove(position);
                        notifyDataSetChanged();
                    } else {
                        ToastUtil.showShortText(context, "??????????????????~~~");
                    }
                }
            }

        }.execute();

    }

}
