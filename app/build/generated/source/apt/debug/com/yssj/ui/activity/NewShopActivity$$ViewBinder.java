// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewShopActivity$$ViewBinder<T extends com.yssj.ui.activity.NewShopActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231917, "field 'imgbtnLeftIcon' and method 'onClick'");
    target.imgbtnLeftIcon = finder.castView(view, 2131231917, "field 'imgbtnLeftIcon'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131233636, "field 'title' and method 'onClick'");
    target.title = finder.castView(view, 2131233636, "field 'title'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131231218, "field 'mViewPager'");
    target.mViewPager = finder.castView(view, 2131231218, "field 'mViewPager'");
    view = finder.findRequiredView(source, 2131233744, "field 'tvTitleBase'");
    target.tvTitleBase = finder.castView(view, 2131233744, "field 'tvTitleBase'");
  }

  @Override public void unbind(T target) {
    target.imgbtnLeftIcon = null;
    target.title = null;
    target.mViewPager = null;
    target.tvTitleBase = null;
  }
}
