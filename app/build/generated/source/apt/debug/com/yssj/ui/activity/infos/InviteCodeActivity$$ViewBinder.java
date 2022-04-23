// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.infos;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class InviteCodeActivity$$ViewBinder<T extends com.yssj.ui.activity.infos.InviteCodeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231307, "field 'edYaoqingma'");
    target.edYaoqingma = finder.castView(view, 2131231307, "field 'edYaoqingma'");
    view = finder.findRequiredView(source, 2131234172, "field 'tvMyYaoqingma'");
    target.tvMyYaoqingma = finder.castView(view, 2131234172, "field 'tvMyYaoqingma'");
    view = finder.findRequiredView(source, 2131232415, "field 'llFuzhi' and method 'onViewClicked'");
    target.llFuzhi = finder.castView(view, 2131232415, "field 'llFuzhi'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131232495, "field 'llTop1'");
    target.llTop1 = finder.castView(view, 2131232495, "field 'llTop1'");
    view = finder.findRequiredView(source, 2131234173, "field 'tvMyYaoqingma2'");
    target.tvMyYaoqingma2 = finder.castView(view, 2131234173, "field 'tvMyYaoqingma2'");
    view = finder.findRequiredView(source, 2131232496, "field 'llTop2' and method 'onViewClicked'");
    target.llTop2 = finder.castView(view, 2131232496, "field 'llTop2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131233719, "field 'tvTitleBase'");
    target.tvTitleBase = finder.castView(view, 2131233719, "field 'tvTitleBase'");
    view = finder.findRequiredView(source, 2131231707, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131234616, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131234461, "method 'onViewClicked'");
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
    target.edYaoqingma = null;
    target.tvMyYaoqingma = null;
    target.llFuzhi = null;
    target.llTop1 = null;
    target.tvMyYaoqingma2 = null;
    target.llTop2 = null;
    target.tvTitleBase = null;
  }
}
