// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.dialog;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class YiYuanDuoShareDialog$$ViewBinder<T extends com.yssj.ui.dialog.YiYuanDuoShareDialog> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131234230, "field 'tvNumber'");
    target.tvNumber = finder.castView(view, 2131234230, "field 'tvNumber'");
    view = finder.findRequiredView(source, 2131234425, "field 'tvShuomingTop'");
    target.tvShuomingTop = finder.castView(view, 2131234425, "field 'tvShuomingTop'");
    view = finder.findRequiredView(source, 2131234424, "field 'tvShuomingBot'");
    target.tvShuomingBot = finder.castView(view, 2131234424, "field 'tvShuomingBot'");
  }

  @Override public void unbind(T target) {
    target.tvNumber = null;
    target.tvShuomingTop = null;
    target.tvShuomingBot = null;
  }
}
