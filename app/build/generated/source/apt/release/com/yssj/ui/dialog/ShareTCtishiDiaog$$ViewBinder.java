// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.dialog;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ShareTCtishiDiaog$$ViewBinder<T extends com.yssj.ui.dialog.ShareTCtishiDiaog> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131234608, "field 'xjNumTv'");
    target.xjNumTv = finder.castView(view, 2131234608, "field 'xjNumTv'");
    view = finder.findRequiredView(source, 2131233972, "field 'fMoneyTv'");
    target.fMoneyTv = finder.castView(view, 2131233972, "field 'fMoneyTv'");
    view = finder.findRequiredView(source, 2131233950, "field 'ednumTv'");
    target.ednumTv = finder.castView(view, 2131233950, "field 'ednumTv'");
    view = finder.findRequiredView(source, 2131233971, "field 'fExtraTv'");
    target.fExtraTv = finder.castView(view, 2131233971, "field 'fExtraTv'");
    view = finder.findRequiredView(source, 2131234135, "field 'moneyTv'");
    target.moneyTv = finder.castView(view, 2131234135, "field 'moneyTv'");
    view = finder.findRequiredView(source, 2131233969, "field 'extraTv'");
    target.extraTv = finder.castView(view, 2131233969, "field 'extraTv'");
    view = finder.findRequiredView(source, 2131231645, "field 'iconClose' and method 'onClick'");
    target.iconClose = finder.castView(view, 2131231645, "field 'iconClose'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.xjNumTv = null;
    target.fMoneyTv = null;
    target.ednumTv = null;
    target.fExtraTv = null;
    target.moneyTv = null;
    target.extraTv = null;
    target.iconClose = null;
  }
}
