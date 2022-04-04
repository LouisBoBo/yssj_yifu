// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.shopdetails;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ShopDetailsActivity$$ViewBinder<T extends com.yssj.ui.activity.shopdetails.ShopDetailsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230943, "field 'btnBuyLeftLl'");
    target.btnBuyLeftLl = finder.castView(view, 2131230943, "field 'btnBuyLeftLl'");
    view = finder.findRequiredView(source, 2131230944, "field 'btnBuyLeftTextLl'");
    target.btnBuyLeftTextLl = finder.castView(view, 2131230944, "field 'btnBuyLeftTextLl'");
    view = finder.findRequiredView(source, 2131230945, "field 'btnBuyLeftTopText'");
    target.btnBuyLeftTopText = finder.castView(view, 2131230945, "field 'btnBuyLeftTopText'");
    view = finder.findRequiredView(source, 2131230942, "field 'btnBuyLeftBottomText'");
    target.btnBuyLeftBottomText = finder.castView(view, 2131230942, "field 'btnBuyLeftBottomText'");
    view = finder.findRequiredView(source, 2131230947, "field 'btnBuyRightLl'");
    target.btnBuyRightLl = finder.castView(view, 2131230947, "field 'btnBuyRightLl'");
    view = finder.findRequiredView(source, 2131230948, "field 'btnBuyRightTextLl'");
    target.btnBuyRightTextLl = finder.castView(view, 2131230948, "field 'btnBuyRightTextLl'");
    view = finder.findRequiredView(source, 2131230949, "field 'btnBuyRightTopText'");
    target.btnBuyRightTopText = finder.castView(view, 2131230949, "field 'btnBuyRightTopText'");
    view = finder.findRequiredView(source, 2131230946, "field 'btnBuyRightBottomText'");
    target.btnBuyRightBottomText = finder.castView(view, 2131230946, "field 'btnBuyRightBottomText'");
    view = finder.findRequiredView(source, 2131234091, "field 'tvLeftRmb'");
    target.tvLeftRmb = finder.castView(view, 2131234091, "field 'tvLeftRmb'");
    view = finder.findRequiredView(source, 2131234363, "field 'tvRightRmb'");
    target.tvRightRmb = finder.castView(view, 2131234363, "field 'tvRightRmb'");
    view = finder.findRequiredView(source, 2131232429, "method 'onViewClicked'");
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
    target.btnBuyLeftLl = null;
    target.btnBuyLeftTextLl = null;
    target.btnBuyLeftTopText = null;
    target.btnBuyLeftBottomText = null;
    target.btnBuyRightLl = null;
    target.btnBuyRightTextLl = null;
    target.btnBuyRightTopText = null;
    target.btnBuyRightBottomText = null;
    target.tvLeftRmb = null;
    target.tvRightRmb = null;
  }
}
