// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.dialog;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class JiZanCommonDialog$$ViewBinder<T extends com.yssj.ui.dialog.JiZanCommonDialog> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131234498, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131234498, "field 'tvTitle'");
    view = finder.findRequiredView(source, 2131233872, "field 'tvContent'");
    target.tvContent = finder.castView(view, 2131233872, "field 'tvContent'");
    view = finder.findRequiredView(source, 2131230898, "field 'btLeft' and method 'onClick'");
    target.btLeft = finder.castView(view, 2131230898, "field 'btLeft'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
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
    view = finder.findRequiredView(source, 2131231645, "method 'onClick'");
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
    target.tvTitle = null;
    target.tvContent = null;
    target.btLeft = null;
    target.btRight = null;
  }
}
