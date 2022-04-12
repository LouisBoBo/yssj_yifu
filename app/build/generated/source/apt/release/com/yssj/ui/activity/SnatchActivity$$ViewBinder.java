// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SnatchActivity$$ViewBinder<T extends com.yssj.ui.activity.SnatchActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131233535, "field 'tTile'");
    target.tTile = finder.castView(view, 2131233535, "field 'tTile'");
  }

  @Override public void unbind(T target) {
    target.tTile = null;
  }
}
