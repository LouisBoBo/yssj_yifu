// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.infos;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class YJdetailActivity$$ViewBinder<T extends com.yssj.ui.activity.infos.YJdetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131234201, "field 'tvMyYj'");
    target.tvMyYj = finder.castView(view, 2131234201, "field 'tvMyYj'");
    view = finder.findRequiredView(source, 2131234350, "field 'tvQinglingYj'");
    target.tvQinglingYj = finder.castView(view, 2131234350, "field 'tvQinglingYj'");
    view = finder.findRequiredView(source, 2131234650, "field 'tvYiTiXian'");
    target.tvYiTiXian = finder.castView(view, 2131234650, "field 'tvYiTiXian'");
    view = finder.findRequiredView(source, 2131232477, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131230913, "method 'onViewClicked'");
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
    target.tvMyYj = null;
    target.tvQinglingYj = null;
    target.tvYiTiXian = null;
  }
}
