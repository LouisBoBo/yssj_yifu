// Generated code from Butter Knife. Do not modify!
package com.yssj.huanxin.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChatActivity$$ViewBinder<T extends com.yssj.huanxin.activity.ChatActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131233719, "field 'tvTitleBase'");
    target.tvTitleBase = finder.castView(view, 2131233719, "field 'tvTitleBase'");
    view = finder.findRequiredView(source, 2131234603, "field 'tvWxh'");
    target.tvWxh = finder.castView(view, 2131234603, "field 'tvWxh'");
    view = finder.findRequiredView(source, 2131232513, "field 'llWxin' and method 'onViewClicked'");
    target.llWxin = finder.castView(view, 2131232513, "field 'llWxin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131231707, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.tvTitleBase = null;
    target.tvWxh = null;
    target.llWxin = null;
  }
}