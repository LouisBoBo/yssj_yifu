// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.dialog;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class OneBuyGroupsResultDialog$$ViewBinder<T extends com.yssj.ui.dialog.OneBuyGroupsResultDialog> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131232173, "field 'ivLing' and method 'onViewClicked'");
    target.ivLing = finder.castView(view, 2131232173, "field 'ivLing'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked();
        }
      });
  }

  @Override public void unbind(T target) {
    target.ivLing = null;
  }
}
