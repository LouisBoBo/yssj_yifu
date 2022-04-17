// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.infos;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MyWalletActivity$$ViewBinder<T extends com.yssj.ui.activity.infos.MyWalletActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131234640, "field 'tvYue'");
    target.tvYue = finder.castView(view, 2131234640, "field 'tvYue'");
    view = finder.findRequiredView(source, 2131234170, "field 'tvMyShouyi'");
    target.tvMyShouyi = finder.castView(view, 2131234170, "field 'tvMyShouyi'");
  }

  @Override public void unbind(T target) {
    target.tvYue = null;
    target.tvMyShouyi = null;
  }
}
