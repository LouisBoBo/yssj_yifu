// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.infos;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MyQrCodeActivity$$ViewBinder<T extends com.yssj.ui.activity.infos.MyQrCodeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131233744, "field 'tvTitleBase'");
    target.tvTitleBase = finder.castView(view, 2131233744, "field 'tvTitleBase'");
    view = finder.findRequiredView(source, 2131232197, "field 'ivQrCode'");
    target.ivQrCode = finder.castView(view, 2131232197, "field 'ivQrCode'");
    view = finder.findRequiredView(source, 2131233236, "field 'rlQrcode'");
    target.rlQrcode = finder.castView(view, 2131233236, "field 'rlQrcode'");
    view = finder.findRequiredView(source, 2131234397, "field 'tvSave' and method 'onViewClicked'");
    target.tvSave = finder.castView(view, 2131234397, "field 'tvSave'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131231718, "method 'onViewClicked'");
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
    target.tvTitleBase = null;
    target.ivQrCode = null;
    target.rlQrcode = null;
    target.tvSave = null;
  }
}
