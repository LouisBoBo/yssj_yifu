//package com.yssj.ui.activity;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.yssj.activity.R;
//import com.yssj.app.SAsyncTask;
//import com.yssj.entity.ReturnInfo;
//import com.yssj.entity.ShopCart;
//import com.yssj.entity.StockType;
//import com.yssj.model.ComModel;
//import com.yssj.ui.activity.shopdetails.ShopCartDialog;
//import com.yssj.ui.activity.shopdetails.ShopDetailsActivity;
//import com.yssj.ui.activity.shopdetails.SubmitMultiShopSpecialActivty;
//import com.yssj.ui.adpter.ShopCartAdpter;
//import com.yssj.ui.adpter.ShopCartAdpter.ShopCartAdpterInterface;
//import com.yssj.ui.base.BasePager;
//import com.yssj.utils.LogYiFu;
//import com.yssj.utils.YCache;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class ShopCartSpecialPager extends BasePager implements OnClickListener,
//		ShopCartAdpterInterface, Serializable {
//
//	private LinearLayout lay_cartpay;
//	private TextView tv_allchoose, tv_price, tv_no_freight, tv_pay;
//	private List<ShopCart> mListCommon;
//	private int height;
//	public List<ShopCart> listShopCarts;
//	private ArrayList<Integer> sp;
//	private ArrayList<Integer> spImg;
//
//	private ShopCartAdpter adpter;
//	private PullToRefreshListView listView;
//	private int statusBarHeight;
//
//	private TextView tv_edit_cart;
//
//	private double cPrice;// ????????????????????????
//
//	private boolean refeshFlag = true, allFlag = false;
//
//	private LinearLayout layout_nodata_shopcar;
//	private Button btn_to_shop;
//
//	private LinearLayout llBottom;
//	private Boolean mFlag = false;
//
//	public ShopCartSpecialPager(Context context) {
//		super(context);
//	}
//
//	@Override
//	public View initView() {
//		sp = new ArrayList<Integer>();
//		spImg = new ArrayList<Integer>();
//		view = View.inflate(context, R.layout.activity_shopcart_common, null);
//		listView = (PullToRefreshListView) view
//				.findViewById(R.id.listview_shopcart);
//		// flush();
//		lay_cartpay = (LinearLayout) view.findViewById(R.id.lay_cartpay);
//		// lay_cartremove = (LinearLayout) v.findViewById(R.id.lay_cartremove);
//		tv_price = (TextView) view.findViewById(R.id.tv_price);
//		tv_no_freight = (TextView) view.findViewById(R.id.tv_no_freight);
//		tv_pay = (TextView) view.findViewById(R.id.tv_pay);
//		tv_pay.setOnClickListener(this);
//		// tv_remove = (TextView) v.findViewById(R.id.tv_remove);
//		// tv_remove.setOnClickListener(this);
//		// tv_delete_all = (TextView) v.findViewById(R.id.tv_delete_all);
//		// tv_delete_all.setOnClickListener(this);
//		tv_allchoose = (TextView) view.findViewById(R.id.tv_allchoose);
//		tv_allchoose.setCompoundDrawablesWithIntrinsicBounds(context
//				.getResources().getDrawable(R.drawable.tvchooseno_normal),
//				null, null, null);
//		tv_allchoose.setOnClickListener(this);
//
//		// int resource = R.layout.listview_shop_cart;
//		listShopCarts = new ArrayList<ShopCart>();
//		mListCommon = new ArrayList<ShopCart>();
//		// queryListShopCart(1);
//		// adpter = new ShopCartAdpter(context.getApplicationContext(),
//		// resource,
//		// listShopCarts, sp, spImg);
//		// adpter.setCartOncallback(this);
//		//
//		// listView.setAdapter(adpter);
//		// listView.setRefreshing();
//		layout_nodata_shopcar = (LinearLayout) view
//				.findViewById(R.id.layout_nodata_shopcar);
//		llBottom = (LinearLayout) view.findViewById(R.id.lay_bottom);
//		// if (listShopCarts!=null&&listShopCarts.size() > 0) {
//		// llBottom.setVisibility(View.VISIBLE);
//		// }else{
//		// layout_nodata_shopcar.setVisibility(View.VISIBLE);
//		// listView.setVisibility(View.GONE);
//		// }
//
//		btn_to_shop = (Button) view.findViewById(R.id.btn_to_shop);
//		btn_to_shop.setOnClickListener(this);
//		view.findViewById(R.id.img_back).setOnClickListener(this);
//
//		return view;
//	}
//
//	/**
//	 * ????????????
//	 */
//	private void editorAllGoods(TextView tv_edit_cart) {
//		String strTitle = tv_edit_cart.getText().toString();
//		if (strTitle.equals("????????????")) {
//			tv_edit_cart.setText("??????");
//			lay_cartpay.setVisibility(View.GONE);
//			// lay_cartremove.setVisibility(View.VISIBLE);
//			refeshFlag = false;
//			for (int i = 0; i < sp.size(); i++) {
//				sp.set(i, 3);
//			}
//
//		} else {
//			tv_edit_cart.setText("????????????");
//			lay_cartpay.setVisibility(View.VISIBLE);
//			// lay_cartremove.setVisibility(View.GONE);
//			refeshFlag = true;
//			for (int i = 0; i < sp.size(); i++) {
//				sp.set(i, 1);
//			}
//		}
//		adpter.notifyDataSetChanged();
//
//	}
//
//	private int curPage = 1;
//	private int pageSize = 10;
//	private AlertDialog dialog;
//
//	/****
//	 * ???????????????????????????
//	 */
//	public void queryListShopCart(final int type) {
//		new SAsyncTask<String, Void, List<ShopCart>>(
//				(FragmentActivity) context, R.string.wait) {
//			@Override
//			protected List<ShopCart> doInBackground(FragmentActivity context,
//					String... params) throws Exception {
//				List<ShopCart> list = ComModel.queryShopCartsSpecial(context,
//						YCache.getCacheToken(context), params[0], params[1],1+"");
//				return list;
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context,
//					List<ShopCart> list, Exception e) {
//
//				if (e != null) {// ????????????
//					LogYiFu.e("?????? -----", "??????");
//					layout_nodata_shopcar.setVisibility(View.VISIBLE);
//					listView.setVisibility(View.GONE);
//					llBottom.setVisibility(View.GONE);
//					listView.onRefreshComplete();
//				} else {// ???????????????????????????????????????
//
//					if (list != null) {
//						LogYiFu.i("TAG", "list= " + list.toString());
//						mListCommon.clear();
//						mListCommon.addAll(list);
//						mFlag = true;
////						System.out.println("type:" + type);
//						if (type == 1) { // ????????????
//							if (list != null && list.size() == 0) {
//								LogYiFu.e("TAG", "?????????????????????");
//								layout_nodata_shopcar
//										.setVisibility(View.VISIBLE);
//								listView.setVisibility(View.GONE);
//								llBottom.setVisibility(View.GONE);
//							} else {
//								layout_nodata_shopcar.setVisibility(View.GONE);
//								listView.setVisibility(View.VISIBLE);
//								llBottom.setVisibility(View.VISIBLE);
//							}
//							listShopCarts.clear();
//							listShopCarts.addAll(list);
//							sp.clear();
//							spImg.clear();
//						} else if (type == 2 || type == 0) { // ????????????
//							listShopCarts.addAll(list);
//						}
//						for (int i = 0; i < list.size(); i++) {
//							sp.add(1);
//						}
//						for (int i = 0; i < list.size(); i++) {
//							spImg.add(1);
//						}
//
//					}
//				}
//				if (type == 1 || type == 2) {
//					listView.onRefreshComplete();
//				}
//				// setPrice();
//				tv_allchoose.setCompoundDrawablesWithIntrinsicBounds(
//						R.drawable.tvchooseno_normal, 0, 0, 0);
//				tv_price.setText("?????? " + 0.0 + "???");
//				tv_pay.setText("?????????(" + 0 + ")");
//				tv_no_freight.setText("??????????????" + 0.00);
//				allFlag = false;
//				adpter.notifyDataSetChanged();
//			};
//
//			@Override
//			protected boolean isHandleException() {
//				return true;
//			};
//		}.execute(String.valueOf(curPage), String.valueOf(pageSize));
//	}
//
//	/****
//	 * ???????????????????????????
//	 */
//	private void deleteShopCart(final int position) {
//		new SAsyncTask<String, Void, ReturnInfo>((FragmentActivity) context,
//				null, R.string.wait) {
//			@Override
//			protected ReturnInfo doInBackground(FragmentActivity context,
//					String... params) throws Exception {
//				ReturnInfo r = ComModel.deleteShopCartSpecial(context,
//						YCache.getCacheToken(context), params[0]);
//				return r;
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context,
//					ReturnInfo r, Exception e) {
//
//				if (e != null) {
//					LogYiFu.e("?????? -----", "??????");
//				} else {
//					adpter.deleteSucess(listShopCarts.get(position));
//					listShopCarts.remove(position);
//					sp.remove(position);
//					spImg.remove(position);
//					adpter.notifyDataSetChanged();
//					setPrice();
//					Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
//					if (listShopCarts.size() == 0) {
//						layout_nodata_shopcar.setVisibility(View.VISIBLE);
//						listView.setVisibility(View.GONE);
//						llBottom.setVisibility(View.GONE);
//					}
//				}
//			};
//
//			@Override
//			protected boolean isHandleException() {
//				return true;
//			};
//		}.execute(listShopCarts.get(position).getId() + "",
//				listShopCarts.get(position).getP_code());
//
//	}
//
//	/*
//	 * @Override public void onPause() { // TODO Auto-generated method stub
//	 * super.onPause(); MobclickAgent.onPageEnd("ShopCartActivity");
//	 * MobclickAgent.onPause(context); }
//	 */
//	// TODO:
//	public void flush() {
//		// MobclickAgent.onPageStart("ShopCartActivity");
//		// MobclickAgent.onResume(context);
//
//		// UserInfo userInfo = YCache.getCacheUserSafe(ShopCartActivity.this);
//		curPage = 1;
//		// queryListShopCart(1);
//		listView.setMode(Mode.BOTH);
//		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
//
//			@Override
//			public void onPullDownToRefresh(
//					PullToRefreshBase<ListView> refreshView) {
//				if (refeshFlag) {
//					curPage = 1;
//					// MyLogYiFu.e("????????????curpage == ", ""+ curPage);
//					queryListShopCart(1);
//				} else {
//					Toast.makeText(context, "???????????????????????????", Toast.LENGTH_SHORT)
//							.show();
//				}
//				listView.onRefreshComplete();
//				setAllSelect(false);
//				allFlag = false;
//
//			}
//
//			@Override
//			public void onPullUpToRefresh(
//					PullToRefreshBase<ListView> refreshView) {
//				if (refeshFlag) {
//					curPage++;
//					// MyLogYiFu.e("????????????curpage == ", ""+ curPage);
//					queryListShopCart(2);
//				} else {
//					Toast.makeText(context, "???????????????????????????", Toast.LENGTH_SHORT)
//							.show();
//				}
//				listView.onRefreshComplete();
//				setAllSelect(false);
//				allFlag = false;
//			}
//
//		});
//
//		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
//
//			@Override
//			public void onLastItemVisible() {
//
//				LogYiFu.e("End of List! == ", "End of List!");
//			}
//		});
//
//		// listView.setOnItemClickListener(this);
//	}
//
//	/****
//	 * ???????????????????????????
//	 */
//	private void deleteShopCartList() {
//		if (listShopCarts != null && listShopCarts.size() > 0) {
//			new SAsyncTask<String, Void, ReturnInfo>(
//					(FragmentActivity) context, null, R.string.wait) {
//				@Override
//				protected ReturnInfo doInBackground(FragmentActivity context,
//						String... params) throws Exception {
//					String ids = "";
//					StringBuffer buffer = new StringBuffer();
//					int length = spImg.size() - 1;
//					for (int i = 0; i < length + 1; i++) {
//						int p = spImg.get(i);
//
//						if (p == 2) {
//							String id = listShopCarts.get(i).getId() + "";
//							buffer.append(id + ",");
//						}
//						ids = buffer.toString();
//						ids = ids.substring(0, ids.length() - 1);
//
//					}
//					ReturnInfo r = ComModel.deleteShopCartList(context,
//							YCache.getCacheToken(context), ids);
//					return r;
//				}
//
//				@Override
//				protected void onPostExecute(FragmentActivity context,
//						ReturnInfo r, Exception e) {
//
//					if (e != null) {
//						LogYiFu.e("?????? -----", "??????");
//					} else {
//						for (int i = spImg.size() - 1; i >= 0; i--) {
//							if (spImg.get(i) == 2) {
//								listShopCarts.remove(i);
//								spImg.remove(i);
//								sp.remove(i);
//
//							}
//						}
//
//						adpter.notifyDataSetChanged();
//						// setPrice();
//						Toast.makeText(context, "????????????", Toast.LENGTH_SHORT)
//								.show();
//					}
//				};
//
//				@Override
//				protected boolean isHandleException() {
//					return true;
//				};
//			}.execute();
//		}
//
//	}
//
//	/***
//	 * ?????????????????????
//	 */
//	private void updateShopCart(final int position, final int shop_number) {
//
//		final ShopCart cart = listShopCarts.get(position);
//		new SAsyncTask<String, Void, ReturnInfo>((FragmentActivity) context,
//				null, R.string.wait) {
//			@Override
//			protected ReturnInfo doInBackground(FragmentActivity context,
//					String... params) throws Exception {
//				ReturnInfo r = ComModel.updateShopCartSpecial(context,
//						YCache.getCacheToken(context), "" + shop_number,
//						cart.getId() + "");
//				return r;
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context,
//					ReturnInfo r, Exception e) {
//
//				if (e != null) {
//					LogYiFu.e("?????? -----", "??????");
//				} else {
//					listShopCarts.get(position).setShop_num(shop_number);
//					setPrice();
//					adpter.notifyDataSetChanged();
//				}
//			};
//
//			@Override
//			protected boolean isHandleException() {
//				return true;
//			};
//		}.execute();
//
//	}
//
//	/***
//	 * ??????????????????
//	 */
//	private void queryShopQueryAttr(final int position) {
//		if (listShopCarts != null && listShopCarts.size() > 0) {
//			ShopCart cart = listShopCarts.get(position);
//			new SAsyncTask<String, Void, List<StockType>>(
//					(FragmentActivity) context, null, R.string.wait) {
//				@Override
//				protected List<StockType> doInBackground(
//						FragmentActivity context, String... params)
//						throws Exception {
//
//					List<StockType> list = ComModel.queryShop_Stokect(context,
//							params[0]);
//					return list;
//				}
//
//				@Override
//				protected void onPostExecute(FragmentActivity context,
//						List<StockType> list, Exception e) {
//
//					if (e != null) {// ????????????
//						Toast.makeText(context, "????????????????????????", Toast.LENGTH_LONG)
//								.show();
//
//					} else {// ???????????????????????????????????????
//						if (list != null && list.size() > 0) {
//							showPopWindow(position, list);
//						}
//					}
//
//				};
//
//				@Override
//				protected boolean isHandleException() {
//					return true;
//				};
//			}.execute(cart.getShop_code());
//
//		}
//
//	}
//
//	/****
//	 * ?????????????????????
//	 * 
//	 * @param i
//	 */
//	private void showPopWindow(final int position, List<StockType> list) {
//		if (listShopCarts != null && listShopCarts.size() > 0) {
//			final ShopCartDialog dlg = new ShopCartDialog(context,
//					R.style.DialogStyle, height, position, listShopCarts, list);
//			Window window = dlg.getWindow();
//			window.setGravity(Gravity.BOTTOM);
//			window.setWindowAnimations(R.style.dlg_down_to_top);
//			dlg.show();
//
//			dlg.cartDialogOncallBack = new ShopCartDialog.ShopCartDialogOncallBack() {
//
//				@Override
//				public void updateOncallBack(double shop_se_price,
//						String color, String size, int shop_num,
//						String def_pic, String stock_type_id, View v) {
//					dlg.dismiss();
//					updateShopCartType(shop_se_price + "", color, size,
//							shop_num, def_pic, stock_type_id, position, v);
//
//				}
//			};
//
//		}
//
//	}
//
//	/**
//	 * ??????????????????????????????,??????????????????
//	 */
//	private void updateShopCartType(final String shop_se_price,
//			final String color, final String size, final int shop_num,
//			String def_pic, String stock_type_id, int p, final View v) {
//
//		final ShopCart cart = listShopCarts.get(p);
//		if (cart == null)
//			return;
//		new SAsyncTask<String, Void, ReturnInfo>((FragmentActivity) context,
//				null, R.string.wait) {
//			@Override
//			protected ReturnInfo doInBackground(FragmentActivity context,
//					String... params) throws Exception {
//				ReturnInfo r = ComModel.updateShopCartType(context,
//						YCache.getCacheToken(context), params[0], params[1],
//						params[2], params[3], params[4], params[5], params[6]);
//				return r;
//			}
//
//			@Override
//			protected void onPostExecute(FragmentActivity context,
//					ReturnInfo r, Exception e) {
//
//				if (e != null) {
//					LogYiFu.e("?????? -----", "??????" + e);
//
//				} else {// ????????????
//					cart.setShop_num(shop_num);
//					cart.setShop_se_price(Double.parseDouble(shop_se_price));
//					cart.setPrice(cart.getPrice());
//					cart.setColor(color);
//					cart.setSize(size);
//					adpter.notifyDataSetChanged();
//					setPrice();
//
//				}
//			};
//
//			@Override
//			protected boolean isHandleException() {
//				return true;
//			};
//		}.execute(cart.getId() + "", shop_se_price, color, size, shop_num + "",
//				def_pic, stock_type_id);
//	}
//
//	@Override
//	public void onClick(View v) {
//		Intent intent = null;
//		switch (v.getId()) {
//		case R.id.tv_edit_cart:// ????????????
//			editorAllGoods(tv_edit_cart);
//			break;
//		case R.id.btn_to_shop:// ?????????????????????????????????
//			/*
//			 * if(MainMenuActivity.instances!=null){
//			 * MainMenuActivity.instances.finish(); } intent=new Intent(this,
//			 * MainMenuActivity.class);
//			 * 
//			 * startActivity(intent);
//			 */
//
//			intent = new Intent();
//			((Activity) context).setResult(10001);
//			((Activity) context).finish();
//			break;
//		case R.id.tv_allchoose:// ??????
//			allFlag = !allFlag;
//			setAllSelect(allFlag);
//			break;
//		case R.id.tv_pay:// ??????
//			getOrderNo();
//			break;
//		// case R.id.tv_remove:// ???????????????
//		// addShopLike(tv_remove);
//		// break;
//		// case R.id.tv_delete_all:// ????????????
//		// deleteShopCartList();
//		// break;
//		default:
//			break;
//		}
//
//	}
//
//	// ????????????
//	private void setAllSelect(boolean isSelectedAll) {
//		if (isSelectedAll) { // ?????? ????????????
//			cPrice = 0;
//			int goodsNum = 0;
//			int saveMoney = 0;
//			for (int i = 0; i < spImg.size(); i++) {
//				spImg.set(i, 2);
//				tv_allchoose.setCompoundDrawablesWithIntrinsicBounds(
//						context.getResources().getDrawable(
//								R.drawable.tvchooseno_selected), null, null,
//						null);
//				ShopCart shopCart = listShopCarts.get(i);
//				cPrice = cPrice + shopCart.getPrice() * shopCart.getShop_num();
//				goodsNum += shopCart.getShop_num();
//				// saveMoney += shopCart.getShop_num()
//				// * (shopCart.getShop_price() - shopCart
//				// .getShop_se_price());
//			}
//			String price = new java.text.DecimalFormat("#0.00").format(cPrice);
//			String savemoneyStr = new java.text.DecimalFormat("#0.00")
//					.format(saveMoney);
//			tv_price.setText("?????? " + price + "???");
//			tv_pay.setText("?????????(" + goodsNum + ")");
//			tv_no_freight.setText("??????????????0.00");
//
//		} else {
//			for (int i = 0; i < spImg.size(); i++) {
//				spImg.set(i, 1);
//
//				tv_allchoose
//						.setCompoundDrawablesWithIntrinsicBounds(
//								context.getResources().getDrawable(
//										R.drawable.tvchooseno_normal), null,
//								null, null);
//			}
//			cPrice = 0;
//			tv_price.setText("?????? " + cPrice + "???");
//			tv_pay.setText("?????????(0)");
//			tv_no_freight.setText("????????????0.00???");
//		}
//		adpter.notifyDataSetChanged();
//	}
//
//	private void getOrderNo() {
//		List<ShopCart> listGoods = new ArrayList<ShopCart>();
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < spImg.size(); i++) {
//			int f = spImg.get(i);
//			if (f == 2) {
//				ShopCart shopCart = listShopCarts.get(i);
//				String id = shopCart.getId() + ",";
//				sb.append(id);
//				shopCart.setShop_list(null);
//				if (shopCart != null) {
//					listGoods.add(shopCart);
//				}
//
//			}
//		}
//		String mCardIds = sb.toString().substring(0, sb.toString().length());
//		// ???????????????0 ?????????
//		if (listGoods.size() == 0) {
//			Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		Intent intent = new Intent(context, SubmitMultiShopSpecialActivty.class);
//		// TODO:
//		// intent.putExtra(name, value);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("listGoods", (Serializable) listGoods);
//		intent.putExtra("mCardIds", mCardIds);
//		intent.putExtras(bundle);
//		context.startActivity(intent);
//	}
//
//	@Override
//	public void selectOnCallBack(int position) {
//		int p = sp.get(position);
//		if (p == 1) {
//			sp.set(position, 2);
//		} else if (p == 2) {
//			sp.set(position, 1);
//		}
//		setPrice();
//		// adpter.notifyDataSetChanged();
//
//	}
//
//	@Override
//	public void addOnCallBack(int position, int shop_number) {
//		updateShopCart(position, shop_number);
//
//	}
//
//	@Override
//	public void reduceOnCallBack(int position, int shop_number) {
//		updateShopCart(position, shop_number);
//
//	}
//
//	@Override
//	public void updatetOnCallBack(int position) {
//		queryShopQueryAttr(position);
//
//	}
//
//	@Override
//	public void deleteOnCallBack(final int position) {
//		// if (spImg.get(position) == 2) {
//		AlertDialog.Builder builder = new Builder(context);
//		// ???????????????????????????
//		View view = View.inflate(context, R.layout.payback_esc_apply_dialog,
//				null);
//		TextView tv_des = (TextView) view.findViewById(R.id.tv_des);
//
//		tv_des.setText("??????????????????");
//
//		Button ok = (Button) view.findViewById(R.id.ok);
//		ok.setBackgroundResource(R.drawable.payback_esc_apply_esc);
//		Button cancel = (Button) view.findViewById(R.id.cancel);
//
//		cancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// ???????????????????????????
//				dialog.dismiss();
//			}
//		});
//		ok.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				deleteShopCart(position);
//				dialog.dismiss();
//			}
//		});
//		dialog = builder.create();
//		dialog.setView(view, 0, 0, 0, 0);
//		dialog.show();
//
//		// } else {
//		// Toast.makeText(context, "?????????????????????????????????", Toast.LENGTH_SHORT)
//		// .show();
//		// }
//
//	}
//
//	// ?????????????????????
//	@Override
//	public void selectImgOnCallBack(int position) {
//		int p = spImg.get(position);
//		if (p == 1) {
//			spImg.set(position, 2);
//		} else {
//			spImg.set(position, 1);
//		}
//		setPrice();
//		// ??????????????????
//		setSelectAllStatus();
//	}
//
//	// ??????????????????????????? ??????????????????
//	private void setSelectAllStatus() {
//		if (spImg.size() == 0) {
//			tv_allchoose.setCompoundDrawablesWithIntrinsicBounds(
//					R.drawable.tvchooseno_normal, 0, 0, 0);
//		}
//		for (int i = 0; i < spImg.size(); i++) {
//			int p = spImg.get(i);
//			if (p == 1) {
//				tv_allchoose.setCompoundDrawablesWithIntrinsicBounds(
//						R.drawable.tvchooseno_normal, 0, 0, 0);
//				return;
//			}
//		}
//		tv_allchoose.setCompoundDrawablesWithIntrinsicBounds(
//				R.drawable.tvchooseno_selected, 0, 0, 0);
//	}
//
//	// ???????????????
//	private void setPrice() {
//		if (spImg != null && listShopCarts != null) {
//			cPrice = 0;
//			int chooseNo = 0;
//			double saveMoney = 0;
//			for (int i = 0; i < spImg.size(); i++) {
//				int f = spImg.get(i);
//				if (f == 2) {
//					ShopCart shopCart = listShopCarts.get(i);
//					if (shopCart != null) {
//						int number = shopCart.getShop_num();
//						// double se_price = shopCart.getShop_se_price();
//						double se_price = shopCart.getPrice();
//						cPrice = cPrice + number * se_price;
//						chooseNo = chooseNo + number;
//						// saveMoney += shopCart.getShop_num()
//						// * (shopCart.getShop_price() - se_price);
//					}
//
//				}
//			}
//			tv_pay.setText("?????????(" + chooseNo + ")");
//			// new java.text.DecimalFormat("#0.00").format(saveMoney)
//			tv_price.setText("?????? "
//					+ new java.text.DecimalFormat("#0.00").format(cPrice) + "???");
//			// tv_no_freight.setText("??????????????????"
//			// + new java.text.DecimalFormat("#0.00").format(saveMoney));//
//			// ???????????????
//			tv_no_freight.setText("?????????????????0.00");// ???????????????
//
//		}
//
//	}
//
//	/***
//	 * ??????????????????
//	 * 
//	 * @param v
//	 */
//	private void addShopLike(View v) {
//		if (listShopCarts != null && listShopCarts.size() > 0) {
//			StringBuffer bfAdd = new StringBuffer();
//			for (int i = 0; i < spImg.size(); i++) {
//				int f = spImg.get(i);
//				if (f == 2) {
//					ShopCart cart = listShopCarts.get(i);
//					String like_id = cart.getLike_id();
//					if (like_id.equals("-1")) {
//						String shop_code = cart.getShop_code();
//						bfAdd.append(shop_code + ",");
//					}
//				}
//			}
//
//			String shop_code = bfAdd.toString();
//			if (!TextUtils.isEmpty(shop_code)) {
//				shop_code = shop_code.substring(0, shop_code.length() - 1);// ????????????????????????
//				new SAsyncTask<String, Void, ReturnInfo>(
//						(FragmentActivity) context, v, R.string.wait) {
//
//					@Override
//					protected ReturnInfo doInBackground(
//							FragmentActivity context, String... params)
//							throws Exception {
//
//						return ComModel.addLikeShop(context,
//								YCache.getCacheToken(context), params[0]);
//					}
//
//					@Override
//					protected boolean isHandleException() {
//						return true;
//					}
//
//					@Override
//					protected void onPostExecute(FragmentActivity context,
//							ReturnInfo result, Exception e) {
//
//						if (null == e) {
//							if (null != result) {
//								Toast.makeText(context, "????????????",
//										Toast.LENGTH_SHORT).show();
//							} else {
//								Toast.makeText(context, "????????????",
//										Toast.LENGTH_SHORT).show();
//							}
//						}
//						super.onPostExecute(context, result, e);
//					}
//
//				}.execute(shop_code);
//			}
//
//		}
//
//	}
//
//	@Override
//	public void onItemClickLintener(int position) {
//		Intent intent = new Intent(context, ShopDetailsActivity.class);
//		intent.putExtra("height", statusBarHeight);
//		String p_code = listShopCarts.get(position).getP_code();
//		if (TextUtils.isEmpty(p_code)) {
//			intent.putExtra("code", listShopCarts.get(position).getShop_code());
//		} else {
//			intent.putExtra("code", p_code);
//			intent.putExtra("isMeal", true);
//		}
//
//		// startActivity(intent);
//		((Activity) context).startActivityForResult(intent, 100);
//		((Activity) context).overridePendingTransition(R.anim.slide_left_in,
//				R.anim.slide_left_out);
//		;
//	}
//
//	@Override
//	public void initData() {
//		int resource = R.layout.listview_shop_cart_special;
//		if (mFlag) {
//			adpter = new ShopCartAdpter(context, resource, mListCommon, sp,
//					spImg, 2,0,"0");
//		} else {
//			queryListShopCart(1);
//
//			adpter = new ShopCartAdpter(context, resource, listShopCarts, sp,
//					spImg, 2,0,"0");
//			adpter.setCartOncallback(this);
//		}
//		listView.setAdapter(adpter);
//		flush();
//
//	}
//
//	@Override
//	public void newJoinOnCallBack(int position) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
