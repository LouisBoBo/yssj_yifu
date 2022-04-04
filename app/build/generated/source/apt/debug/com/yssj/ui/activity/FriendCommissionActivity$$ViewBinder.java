// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FriendCommissionActivity$$ViewBinder<T extends com.yssj.ui.activity.FriendCommissionActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131233697, "field 'tv2'");
    target.tv2 = finder.castView(view, 2131233697, "field 'tv2'");
    view = finder.findRequiredView(source, 2131233699, "field 'tv3'");
    target.tv3 = finder.castView(view, 2131233699, "field 'tv3'");
    view = finder.findRequiredView(source, 2131233701, "field 'tv4'");
    target.tv4 = finder.castView(view, 2131233701, "field 'tv4'");
    view = finder.findRequiredView(source, 2131233693, "field 'tv1'");
    target.tv1 = finder.castView(view, 2131233693, "field 'tv1'");
  }

  @Override public void unbind(T target) {
    target.tv2 = null;
    target.tv3 = null;
    target.tv4 = null;
    target.tv1 = null;
  }
}
