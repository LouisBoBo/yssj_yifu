// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class YiYuanJoinRecordActivity$$ViewBinder<T extends com.yssj.ui.activity.view.YiYuanJoinRecordActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131233535, "field 'tTile'");
    target.tTile = finder.castView(view, 2131233535, "field 'tTile'");
    view = finder.findRequiredView(source, 2131234528, "field 'tvTotalCount' and method 'onViewClicked'");
    target.tvTotalCount = finder.castView(view, 2131234528, "field 'tvTotalCount'");
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
    target.tTile = null;
    target.tvTotalCount = null;
  }
}
