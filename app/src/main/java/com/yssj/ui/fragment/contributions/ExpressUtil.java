package com.yssj.ui.fragment.contributions;
import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

public class ExpressUtil {


	static final String homeUrl = "https://m.kuaidi100.com/app/?coname=yssj";
	static final String initUrl = "https://m.kuaidi100.com/app/query/?coname=yssj&nu=yt8083668419294&com=yuantong";
	static final String queryUrl = "https://m.kuaidi100.com/query";
	static final String ua = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko)";
	static final String accept = "application/json, text/javascript, */*; q=0.01";


	public static String queryExpress(HashMap<String, Object> data2) {
//		HttpUtil.createGet(homeUrl).header(Header.USER_AGENT,ua).execute().getStatus();
		HttpUtil.createGet(initUrl).header(Header.USER_AGENT,ua).execute().getStatus();
		return HttpRequest.post(queryUrl).header(Header.USER_AGENT,ua).header(Header.ACCEPT,accept).form(data2).execute().body();
	}


	public static void main(String type,String postid) {

		final HashMap<String, Object> data2 = new HashMap<>();
		data2.put("postid",postid);
		data2.put("id",1);
		data2.put("valicode",null);
		data2.put("temp",getRandomNickname(10));
		data2.put("type",type);
		data2.put("phone",null);
		data2.put("token",null);
		data2.put("platform","MWWW");
		data2.put("coname","yssj");

		new Thread(new Runnable(){

			@Override
			public void run(){
				//处理具体的逻辑
				String data = queryExpress(data2);
				System.out.println("测试用" + data);

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(data);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogistticsBean logistticsBean = JSON.parseObject(jsonObject.toString(), LogistticsBean.class);
				EventBus.getDefault().post(logistticsBean);
			}
		}).start();


	}

	public static String getRandomNickname(int length) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			val += String.valueOf(random.nextInt(10));
		}
		return val;
	}

}

