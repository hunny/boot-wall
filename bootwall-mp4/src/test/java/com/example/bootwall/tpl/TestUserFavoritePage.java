package com.example.bootwall.tpl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestUserFavoritePage {

  public static void main(String[] args) throws Exception {

    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MILLISECONDS)
        .build();
    Request request = new Request.Builder()
        .url(
            "https://aweme.snssdk.com/aweme/v1/aweme/post/?user_id=67202249035&max_cursor=0&count=20&retry_type=no_retry&iid=31502162491&device_id=50142706973&ac=wifi&channel=360&aid=1128&app_name=aweme&version_code=179&version_name=1.7.9&device_platform=android&ssmix=a&device_type=Nexus+6P&device_brand=google&language=zh&os_api=27&os_version=8.1.0&openudid=9ee790b17a153ec6&manifest_version_code=179&resolution=1440*2392&dpi=560&update_version_code=1792&_rticket=1527937089629&ts=1527937090&as=a145975132844bd8521312&cp=744db35c25281c89e1okxg&mas=001b5f0f7ddfdf5103c6f1fd3d51fd57304c8ccc8c4cac1c26462c")
        .get() //
        .addHeader("Accept-Encoding", "utf-8") //
        .addHeader("Host", "api.amemv.com") //
        .addHeader("content-type", "application/json") //
        .addHeader("user-agent", "okhttp/3.8.1") //
        .header("Cookie", "odin_tt=43b2b729a66dde31b2d5d42efe91ec6bb8bee27ca6efa4f8b09c3845e7dc5e20942de0a4be1a41b26bbcf69f2207d26f; uid_tt=0e5c9ddc912c2fe970165afa32cbd2fb; sid_tt=aa90cca3d7343026bf57458d8d088f95; sessionid=aa90cca3d7343026bf57458d8d088f95; install_id=31502162491; ttreq=1$80fff2566b9d66734729b80bbbdb1c60a0d4c1a8; sid_guard=aa90cca3d7343026bf57458d8d088f95%7C1527165626%7C2592000%7CSat%2C+23-Jun-2018+12%3A40%3A26+GMT; qh[360]=1") //
        .build();

    Response response = client.newCall(request).execute();
    String str = new String(response.body().bytes());
    System.out.println(str);

  }

}
