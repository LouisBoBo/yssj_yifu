// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HomePage3Fragment$$ViewBinder<T extends com.yssj.ui.fragment.HomePage3Fragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131234487, "field 'tvTime'");
    target.tvTime = finder.castView(view, 2131234487, "field 'tvTime'");
  }

  @Override public void unbind(T target) {
    target.tvTime = null;
  }
}
