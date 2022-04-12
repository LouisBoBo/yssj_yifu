// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.infos;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class YJdetailActivity$$ViewBinder<T extends com.yssj.ui.activity.infos.YJdetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131234172, "field 'tvMyYj'");
    target.tvMyYj = finder.castView(view, 2131234172, "field 'tvMyYj'");
    view = finder.findRequiredView(source, 2131234321, "field 'tvQinglingYj'");
    target.tvQinglingYj = finder.castView(view, 2131234321, "field 'tvQinglingYj'");
    view = finder.findRequiredView(source, 2131234622, "field 'tvYiTiXian'");
    target.tvYiTiXian = finder.castView(view, 2131234622, "field 'tvYiTiXian'");
    view = finder.findRequiredView(source, 2131232465, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131230909, "method 'onViewClicked'");
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
