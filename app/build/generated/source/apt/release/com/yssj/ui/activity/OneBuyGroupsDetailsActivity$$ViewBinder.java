// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class OneBuyGroupsDetailsActivity$$ViewBinder<T extends com.yssj.ui.activity.OneBuyGroupsDetailsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131232155, "field 'ivHongbao' and method 'onViewClicked'");
    target.ivHongbao = finder.castView(view, 2131232155, "field 'ivHongbao'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked();
        }
      });
  }

  @Override public void unbind(T target) {
    target.ivHongbao = null;
  }
}
