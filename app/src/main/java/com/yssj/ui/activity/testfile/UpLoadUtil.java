package com.yssj.ui.activity.testfile;

import android.content.Context;
import android.util.Log;

import com.yssj.YUrl;
import com.yssj.network.YConn;
import com.yssj.utils.AuthKeyTools;
import com.yssj.utils.YCache;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.yssj.network.YConn.Base64Encode;

public class UpLoadUtil {


    /**
     * 上传图片
     *
     * @param imagePath 图片路径
     * @return 新图片的路径
     * @throws IOException
     * @throws JSONException
     */
    @NotNull
    public static void uploadImage(Context context, int material_type,String imagePath, final UpLoadImgListener upLoadImgListener) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        File file = new File(imagePath);
        String imgName = file.getName();
        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        Request.Builder builder = new Request.Builder();
        builder.addHeader("token", YCache.getCacheToken(context));

        String url = YUrl.CLOUD_API_WAR_SUPPLYMATERIAL_ADDMULTIPLE + "?";
        url += "version" + "=" + "V1.32";
        url += "&" + "channel" + "=" + "18";
        url += "&" + "app_id" + "=" + YUrl.APP_ID;
        url += "&" + "appVersion" + "=" + "V3.8.6";
        url += "&" + "device" + "=" + "1";
        url += "&" + "material_type" + "=" + material_type;
        url += "&" + "token" + "=" + YCache.getCacheToken(context);

        String body = url;
        String authKey = null;
        try {
            authKey = AuthKeyTools.builderURL(body,"yunshangshiji");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String I10o = Base64Encode(authKey);

        url += "&" + "authKey" + "=" + authKey;
        url += "&" + "I10o" + "=" + I10o;


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file_data",imgName,image)
                .build();
        Request request = builder
                .url(url)
                .post(requestBody)
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                upLoadImgListener.upLoadFail();
                android.util.Log.e("上传图片", "失败" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    UpLoadImageResult result = JsonUtils.parseObject(jsonObject.toString(), UpLoadImageResult.class);


                    if (!result.getStatus().equals("1")) {
                        upLoadImgListener.upLoadFail();
                        android.util.Log.e("上传图片", "失败" + result.getMessage());

                    } else {
                        upLoadImgListener.upLoadSuccess(result.getFileId());
                        android.util.Log.e("上传图片", "成功" + jsonObject.toString());

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface UpLoadImgListener {
        void upLoadSuccess(int imgId);

        void upLoadFail();
    }


}
