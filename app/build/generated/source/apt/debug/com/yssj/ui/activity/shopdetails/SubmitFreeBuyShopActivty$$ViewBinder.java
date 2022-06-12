// Generated code from Butter Knife. Do not modify!
package com.yssj.ui.activity.shopdetails;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SubmitFreeBuyShopActivty$$ViewBinder<T extends com.yssj.ui.activity.shopdetails.SubmitFreeBuyShopActivty> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131234028, "field 'tvFreeBuyYouhui'");
    target.tvFreeBuyYouhui = finder.castView(view, 2131234028, "field 'tvFreeBuyYouhui'");
    view = finder.findRequiredView(source, 2131234660, "field 'tvYouohuiShowText'");
    target.tvYouohuiShowText = finder.castView(view, 2131234660, "field 'tvYouohuiShowText'");
  }

  @Override public void unbind(T target) {
    target.tvFreeBuyYouhui = null;
    target.tvYouohuiShowText = null;
  }
}
