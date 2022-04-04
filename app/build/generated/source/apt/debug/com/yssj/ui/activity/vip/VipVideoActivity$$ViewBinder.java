// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.vip;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VipVideoActivity$$ViewBinder<T extends com.yssj.ui.activity.vip.VipVideoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131234857, "field 'videoView'");
    target.videoView = finder.castView(view, 2131234857, "field 'videoView'");
  }

  @Override public void unbind(T target) {
    target.videoView = null;
  }
}
