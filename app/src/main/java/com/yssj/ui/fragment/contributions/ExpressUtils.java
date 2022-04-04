package com.yssj.ui.fragment.contributions;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.cookie.GlobalCookieManager;

public class ExpressUtils {
    //	static final String homeUrl = "https://m.kuaidi100.com/app/?coname={conmae}";
    static final String initUrl = "https://m.kuaidi100.com/app/query/?coname={coname}&nu={nu}&com={com}";
    static final String queryUrl = "https://m.kuaidi100.com/query";
    static final String ua = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko)";
    static final String accept = "application/json, text/javascript, */*; q=0.01";
    static final ArrayList list = new ArrayList();

    static {
        for (char c = 'a'; c <= 'z'; c++) {
            list.add(c);
        }
    }

    private static String baseQueryExpress(HashMap<String, Object> data2) {
        String initUrl = ExpressUtil.initUrl.replace("{coname}", (CharSequence) data2.get("coname"))
                .replace("{nu}", (CharSequence) data2.get("postid"))
                .replace("{com}", (CharSequence) data2.get("type"));
        HttpResponse httpResponse = HttpRequest.get(initUrl).header(Header.USER_AGENT, ua).execute();
        List<HttpCookie> cookies = HttpCookie.parse(httpResponse.header(Header.SET_COOKIE));
        for (HttpCookie cookie : cookies) {
            GlobalCookieManager.getCookieManager().getCookieStore().add(URI.create(URI.create(initUrl).getHost()), cookie);
        }
        return HttpRequest.post(queryUrl).header(Header.USER_AGENT, ua).header(Header.ACCEPT, accept).form(data2).execute().body();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String queryExpress(HashMap<String, Object> data) {
        Integer s1 = new Random().ints(1000, 10000).iterator().nextInt();
        Integer s2 = new Random().ints(1000000, 10000000).iterator().nextInt();
        Integer s3 = new Random().ints(100000, 1000000).iterator().nextInt();
        data.put("temp", "0." + s1.toString() + s2.toString() + s3.toString());
        data.put("coname", r());

        return baseQueryExpress(data);
    }

    public static String r() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int num = (int) (Math.random() * 26);
            sb.append(list.get(num));
        }
        return sb.toString();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String postid,String type) {

        long period = 1000 * 60 * 3;
        final HashMap<String, Object> data2 = new HashMap<>();
        data2.put("postid", "YT8083668419294");
        data2.put("id", 1);
        data2.put("valicode", null);
        data2.put("type", "yuantong");
        data2.put("phone", null);
        data2.put("token", null);
        data2.put("platform", "MWWW");

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
}
