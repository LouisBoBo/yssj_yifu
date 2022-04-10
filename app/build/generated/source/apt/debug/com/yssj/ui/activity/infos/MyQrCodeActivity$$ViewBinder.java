// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.infos;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MyQrCodeActivity$$ViewBinder<T extends com.yssj.ui.activity.infos.MyQrCodeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131233719, "field 'tvTitleBase'");
    target.tvTitleBase = finder.castView(view, 2131233719, "field 'tvTitleBase'");
    view = finder.findRequiredView(source, 2131232185, "field 'ivQrCode'");
    target.ivQrCode = finder.castView(view, 2131232185, "field 'ivQrCode'");
    view = finder.findRequiredView(source, 2131233215, "field 'rlQrcode'");
    target.rlQrcode = finder.castView(view, 2131233215, "field 'rlQrcode'");
    view = finder.findRequiredView(source, 2131234368, "field 'tvSave' and method 'onViewClicked'");
    target.tvSave = finder.castView(view, 2131234368, "field 'tvSave'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131231707, "method 'onViewClicked'");
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
