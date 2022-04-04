// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VipShareGroupsDetailsActivity$$ViewBinder<T extends com.yssj.ui.activity.VipShareGroupsDetailsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231844, "field 'imgShopPic'");
    target.imgShopPic = finder.castView(view, 2131231844, "field 'imgShopPic'");
    view = finder.findRequiredView(source, 2131234404, "field 'tvShopName'");
    target.tvShopName = finder.castView(view, 2131234404, "field 'tvShopName'");
    view = finder.findRequiredView(source, 2131234303, "field 'tvProductColor'");
    target.tvProductColor = finder.castView(view, 2131234303, "field 'tvProductColor'");
    view = finder.findRequiredView(source, 2131234306, "field 'tvProductSize'");
    target.tvProductSize = finder.castView(view, 2131234306, "field 'tvProductSize'");
    view = finder.findRequiredView(source, 2131234291, "field 'tvPrice'");
    target.tvPrice = finder.castView(view, 2131234291, "field 'tvPrice'");
    view = finder.findRequiredView(source, 2131234394, "field 'tvShareCount'");
    target.tvShareCount = finder.castView(view, 2131234394, "field 'tvShareCount'");
    view = finder.findRequiredView(source, 2131231706, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131232458, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131234392, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131234393, "method 'onViewClicked'");
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
    target.imgShopPic = null;
    target.tvShopName = null;
    target.tvProductColor = null;
    target.tvProductSize = null;
    target.tvPrice = null;
    target.tvShareCount = null;
  }
}
