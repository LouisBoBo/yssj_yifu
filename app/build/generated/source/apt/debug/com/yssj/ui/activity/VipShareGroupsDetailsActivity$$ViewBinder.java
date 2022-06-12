// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VipShareGroupsDetailsActivity$$ViewBinder<T extends com.yssj.ui.activity.VipShareGroupsDetailsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231856, "field 'imgShopPic'");
    target.imgShopPic = finder.castView(view, 2131231856, "field 'imgShopPic'");
    view = finder.findRequiredView(source, 2131234434, "field 'tvShopName'");
    target.tvShopName = finder.castView(view, 2131234434, "field 'tvShopName'");
    view = finder.findRequiredView(source, 2131234333, "field 'tvProductColor'");
    target.tvProductColor = finder.castView(view, 2131234333, "field 'tvProductColor'");
    view = finder.findRequiredView(source, 2131234336, "field 'tvProductSize'");
    target.tvProductSize = finder.castView(view, 2131234336, "field 'tvProductSize'");
    view = finder.findRequiredView(source, 2131234321, "field 'tvPrice'");
    target.tvPrice = finder.castView(view, 2131234321, "field 'tvPrice'");
    view = finder.findRequiredView(source, 2131234424, "field 'tvShareCount'");
    target.tvShareCount = finder.castView(view, 2131234424, "field 'tvShareCount'");
    view = finder.findRequiredView(source, 2131231718, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131232471, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131234422, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131234423, "method 'onViewClicked'");
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
