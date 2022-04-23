// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.fragment.orderinfo;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ShareGroupChatActivity$$ViewBinder<T extends com.yssj.ui.fragment.orderinfo.ShareGroupChatActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231845, "field 'imgShopPic'");
    target.imgShopPic = finder.castView(view, 2131231845, "field 'imgShopPic'");
    view = finder.findRequiredView(source, 2131234407, "field 'tvShopName'");
    target.tvShopName = finder.castView(view, 2131234407, "field 'tvShopName'");
    view = finder.findRequiredView(source, 2131234306, "field 'tvProductColor'");
    target.tvProductColor = finder.castView(view, 2131234306, "field 'tvProductColor'");
    view = finder.findRequiredView(source, 2131234309, "field 'tvProductSize'");
    target.tvProductSize = finder.castView(view, 2131234309, "field 'tvProductSize'");
    view = finder.findRequiredView(source, 2131234294, "field 'tvPrice'");
    target.tvPrice = finder.castView(view, 2131234294, "field 'tvPrice'");
    view = finder.findRequiredView(source, 2131234626, "field 'tvYikan'");
    target.tvYikan = finder.castView(view, 2131234626, "field 'tvYikan'");
    view = finder.findRequiredView(source, 2131234643, "field 'tvZaikan'");
    target.tvZaikan = finder.castView(view, 2131234643, "field 'tvZaikan'");
    view = finder.findRequiredView(source, 2131231221, "field 'cpbProgresbar2'");
    target.cpbProgresbar2 = finder.castView(view, 2131231221, "field 'cpbProgresbar2'");
    view = finder.findRequiredView(source, 2131232343, "field 'listView1'");
    target.listView1 = finder.castView(view, 2131232343, "field 'listView1'");
    view = finder.findRequiredView(source, 2131234395, "field 'tvShare1'");
    target.tvShare1 = finder.castView(view, 2131234395, "field 'tvShare1'");
    view = finder.findRequiredView(source, 2131232513, "field 'llWxin' and method 'onViewClicked'");
    target.llWxin = finder.castView(view, 2131232513, "field 'llWxin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131234627, "field 'tvYikanEndStr'");
    target.tvYikanEndStr = finder.castView(view, 2131234627, "field 'tvYikanEndStr'");
    view = finder.findRequiredView(source, 2131231707, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131232459, "method 'onViewClicked'");
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
    target.tvYikan = null;
    target.tvZaikan = null;
    target.cpbProgresbar2 = null;
    target.listView1 = null;
    target.tvShare1 = null;
    target.llWxin = null;
    target.tvYikanEndStr = null;
  }
}
