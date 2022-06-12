// Generated code from Butter Knife. Do not modify!
package com.yssj.utils;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class OneBuyChouJiangActivity$$ViewBinder<T extends com.yssj.utils.OneBuyChouJiangActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231718, "field 'imgBack' and method 'onClick'");
    target.imgBack = finder.castView(view, 2131231718, "field 'imgBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131231690, "field 'imScan'");
    target.imScan = finder.castView(view, 2131231690, "field 'imScan'");
    view = finder.findRequiredView(source, 2131231681, "field 'idStartBtn' and method 'onClick'");
    target.idStartBtn = finder.castView(view, 2131231681, "field 'idStartBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131232355, "field 'listView1'");
    target.listView1 = finder.castView(view, 2131232355, "field 'listView1'");
    view = finder.findRequiredView(source, 2131234514, "field 'tvTime' and method 'onViewClicked'");
    target.tvTime = finder.castView(view, 2131234514, "field 'tvTime'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked();
        }
      });
    view = finder.findRequiredView(source, 2131234052, "field 'tvGuize' and method 'onClick'");
    target.tvGuize = finder.castView(view, 2131234052, "field 'tvGuize'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.imgBack = null;
    target.imScan = null;
    target.idStartBtn = null;
    target.listView1 = null;
    target.tvTime = null;
    target.tvGuize = null;
  }
}
