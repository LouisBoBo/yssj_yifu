// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.infos;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class InviteCodeActivity$$ViewBinder<T extends com.yssj.ui.activity.infos.InviteCodeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231306, "field 'edYaoqingma'");
    target.edYaoqingma = finder.castView(view, 2131231306, "field 'edYaoqingma'");
    view = finder.findRequiredView(source, 2131234169, "field 'tvMyYaoqingma'");
    target.tvMyYaoqingma = finder.castView(view, 2131234169, "field 'tvMyYaoqingma'");
    view = finder.findRequiredView(source, 2131232414, "field 'llFuzhi' and method 'onViewClicked'");
    target.llFuzhi = finder.castView(view, 2131232414, "field 'llFuzhi'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131232494, "field 'llTop1'");
    target.llTop1 = finder.castView(view, 2131232494, "field 'llTop1'");
    view = finder.findRequiredView(source, 2131234170, "field 'tvMyYaoqingma2'");
    target.tvMyYaoqingma2 = finder.castView(view, 2131234170, "field 'tvMyYaoqingma2'");
    view = finder.findRequiredView(source, 2131232495, "field 'llTop2' and method 'onViewClicked'");
    target.llTop2 = finder.castView(view, 2131232495, "field 'llTop2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131233718, "field 'tvTitleBase'");
    target.tvTitleBase = finder.castView(view, 2131233718, "field 'tvTitleBase'");
    view = finder.findRequiredView(source, 2131231706, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131234614, "method 'onViewClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onViewClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131234459, "method 'onViewClicked'");
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
