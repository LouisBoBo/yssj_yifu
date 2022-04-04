// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PinTuanSnatchActivity$$ViewBinder<T extends com.yssj.ui.activity.PinTuanSnatchActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131233534, "field 'tTile'");
    target.tTile = finder.castView(view, 2131233534, "field 'tTile'");
  }

  @Override public void unbind(T target) {
    target.tTile = null;
  }
}
