// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MyYJfragment$$ViewBinder<T extends com.yssj.ui.fragment.MyYJfragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131233758, "field 'tvAllMoney'");
    target.tvAllMoney = finder.castView(view, 2131233758, "field 'tvAllMoney'");
    view = finder.findRequiredView(source, 2131234319, "field 'tvQingling'");
    target.tvQingling = finder.castView(view, 2131234319, "field 'tvQingling'");
    view = finder.findRequiredView(source, 2131234001, "field 'tvFriendCount'");
    target.tvFriendCount = finder.castView(view, 2131234001, "field 'tvFriendCount'");
    view = finder.findRequiredView(source, 2131234209, "field 'tvNoData'");
    target.tvNoData = finder.castView(view, 2131234209, "field 'tvNoData'");
    view = finder.findRequiredView(source, 2131234515, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131232410, "method 'onViewClicked'");
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
    target.tvAllMoney = null;
    target.tvQingling = null;
    target.tvFriendCount = null;
    target.tvNoData = null;
  }
}
