// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.vip;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VipSubsidiesActivity$$ViewBinder<T extends com.yssj.ui.activity.vip.VipSubsidiesActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131233744, "field 'tvTitleBase'");
    target.tvTitleBase = finder.castView(view, 2131233744, "field 'tvTitleBase'");
    view = finder.findRequiredView(source, 2131234097, "field 'tvJiangliTitle'");
    target.tvJiangliTitle = finder.castView(view, 2131234097, "field 'tvJiangliTitle'");
    view = finder.findRequiredView(source, 2131234449, "field 'tvShuoming1'");
    target.tvShuoming1 = finder.castView(view, 2131234449, "field 'tvShuoming1'");
    view = finder.findRequiredView(source, 2131234450, "field 'tvShuoming2'");
    target.tvShuoming2 = finder.castView(view, 2131234450, "field 'tvShuoming2'");
    view = finder.findRequiredView(source, 2131234451, "field 'tvShuoming3'");
    target.tvShuoming3 = finder.castView(view, 2131234451, "field 'tvShuoming3'");
    view = finder.findRequiredView(source, 2131234452, "field 'tvShuoming4'");
    target.tvShuoming4 = finder.castView(view, 2131234452, "field 'tvShuoming4'");
    view = finder.findRequiredView(source, 2131234091, "field 'tvJiangli2'");
    target.tvJiangli2 = finder.castView(view, 2131234091, "field 'tvJiangli2'");
    view = finder.findRequiredView(source, 2131234092, "field 'tvJiangli3'");
    target.tvJiangli3 = finder.castView(view, 2131234092, "field 'tvJiangli3'");
    view = finder.findRequiredView(source, 2131234322, "field 'tvPrice1'");
    target.tvPrice1 = finder.castView(view, 2131234322, "field 'tvPrice1'");
    view = finder.findRequiredView(source, 2131234323, "field 'tvPrice2'");
    target.tvPrice2 = finder.castView(view, 2131234323, "field 'tvPrice2'");
    view = finder.findRequiredView(source, 2131234324, "field 'tvPrice3'");
    target.tvPrice3 = finder.castView(view, 2131234324, "field 'tvPrice3'");
    view = finder.findRequiredView(source, 2131234095, "field 'tvJiangliDay'");
    target.tvJiangliDay = finder.castView(view, 2131234095, "field 'tvJiangliDay'");
    view = finder.findRequiredView(source, 2131234557, "field 'tvTotalPayMoneyShifuHg'");
    target.tvTotalPayMoneyShifuHg = finder.castView(view, 2131234557, "field 'tvTotalPayMoneyShifuHg'");
    view = finder.findRequiredView(source, 2131234040, "field 'tvGoPayHg' and method 'onViewClicked'");
    target.tvGoPayHg = finder.castView(view, 2131234040, "field 'tvGoPayHg'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131233176, "field 'rlBotToPayHg'");
    target.rlBotToPayHg = finder.castView(view, 2131233176, "field 'rlBotToPayHg'");
    view = finder.findRequiredView(source, 2131234558, "field 'tvTotalPayMoneyShifuHgBot'");
    target.tvTotalPayMoneyShifuHgBot = finder.castView(view, 2131234558, "field 'tvTotalPayMoneyShifuHgBot'");
    view = finder.findRequiredView(source, 2131233827, "field 'tvBuyYuanjiaBot'");
    target.tvBuyYuanjiaBot = finder.castView(view, 2131233827, "field 'tvBuyYuanjiaBot'");
    view = finder.findRequiredView(source, 2131234293, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.tvTitleBase = null;
    target.tvJiangliTitle = null;
    target.tvShuoming1 = null;
    target.tvShuoming2 = null;
    target.tvShuoming3 = null;
    target.tvShuoming4 = null;
    target.tvJiangli2 = null;
    target.tvJiangli3 = null;
    target.tvPrice1 = null;
    target.tvPrice2 = null;
    target.tvPrice3 = null;
    target.tvJiangliDay = null;
    target.tvTotalPayMoneyShifuHg = null;
    target.tvGoPayHg = null;
    target.rlBotToPayHg = null;
    target.tvTotalPayMoneyShifuHgBot = null;
    target.tvBuyYuanjiaBot = null;
  }
}
