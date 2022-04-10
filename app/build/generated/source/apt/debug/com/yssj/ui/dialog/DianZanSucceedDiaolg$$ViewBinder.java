// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.dialog;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DianZanSucceedDiaolg$$ViewBinder<T extends com.yssj.ui.dialog.DianZanSucceedDiaolg> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230904, "field 'btRight' and method 'onClick'");
    target.btRight = finder.castView(view, 2131230904, "field 'btRight'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131231645, "field 'iconClose' and method 'onClick'");
    target.iconClose = finder.castView(view, 2131231645, "field 'iconClose'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.btRight = null;
    target.iconClose = null;
  }
}
