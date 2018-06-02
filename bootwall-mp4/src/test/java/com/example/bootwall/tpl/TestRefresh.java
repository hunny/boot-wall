package com.example.bootwall.tpl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestRefresh {

  // 推荐刷新
  public static void main(String[] args) throws Exception {

    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MILLISECONDS)
        .build();
    Request request = new Request.Builder()
        .url(
            "https://api.amemv.com/aweme/v1/feed/?type=0&max_cursor=0&min_cursor=0&count=6&volume=0.26666666666666666&pull_type=1&need_relieve_aweme=0&ts=1527935461&app_type=normal&manifest_version_code=179&_rticket=1527935460470&ac=wifi&device_id=50142706973&iid=31502162491&os_version=8.1.0&channel=360&version_code=179&device_type=Nexus%206P&language=zh&resolution=1440*2392&openudid=9ee790b17a153ec6&update_version_code=1792&app_name=aweme&version_name=1.7.9&os_api=27&device_brand=google&ssmix=a&device_platform=android&dpi=560&aid=1128&as=a1c56741f5be9b11527528&cp=76efbc5e55291111e1hsex&mas=00e7af7de82f04b58c221c6aa12e5e370c1c4cacec4cac8c8c469c")
        .get() //
        .addHeader("accept-encoding", "utf-8") //
        .addHeader("content-type", "application/json") //
        .addHeader("user-agent", "okhttp/3.8.1") //
        .header("Cookie", "odin_tt=43b2b729a66dde31b2d5d42efe91ec6bb8bee27ca6efa4f8b09c3845e7dc5e20942de0a4be1a41b26bbcf69f2207d26f; uid_tt=0e5c9ddc912c2fe970165afa32cbd2fb; sid_tt=aa90cca3d7343026bf57458d8d088f95; sessionid=aa90cca3d7343026bf57458d8d088f95; install_id=31502162491; ttreq=1$80fff2566b9d66734729b80bbbdb1c60a0d4c1a8; sid_guard=aa90cca3d7343026bf57458d8d088f95%7C1527165626%7C2592000%7CSat%2C+23-Jun-2018+12%3A40%3A26+GMT; qh[360]=1") //
        .build();

    Response response = client.newCall(request).execute();
    String str = new String(response.body().bytes());
    System.out.println(str);
    Refresh.download(str);

  }

}
