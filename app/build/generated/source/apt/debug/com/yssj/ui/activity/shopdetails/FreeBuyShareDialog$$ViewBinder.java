// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.shopdetails;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FreeBuyShareDialog$$ViewBinder<T extends com.yssj.ui.activity.shopdetails.FreeBuyShareDialog> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131232134, "field 'ivClose' and method 'onViewClicked'");
    target.ivClose = finder.castView(view, 2131232134, "field 'ivClose'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131234495, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131234495, "field 'tvTitle'");
    view = finder.findRequiredView(source, 2131232476, "field 'llShare' and method 'onViewClicked'");
    target.llShare = finder.castView(view, 2131232476, "field 'llShare'");
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
    target.ivClose = null;
    target.tvTitle = null;
    target.llShare = null;
  }
}
