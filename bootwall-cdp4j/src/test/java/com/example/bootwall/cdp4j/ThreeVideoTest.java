package com.example.bootwall.cdp4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreeVideoTest {

  public static void main(String[] args) throws Exception {
  }

  protected static void one2two(String path) throws InterruptedException, ExecutionException {
    ExecutorService threadPool = Executors.newFixedThreadPool(4);
    CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);
    String[] files = new File(path).list();
    if (files.length == 0) {
      return;
    }
    CountDownLatch latch = new CountDownLatch(files.length);
    for (int i = 0; i < files.length; i++) {
      pool.submit(new One2Two(path, files[i], latch));
    }
    latch.await();
    for (int i = 0; i < files.length; i++) {
      String result = pool.take().get();
      System.out.println(result + " is OK!");
    }
    threadPool.shutdown();
  }
  
  protected static void one2three(String path) throws InterruptedException, ExecutionException {
    ExecutorService threadPool = Executors.newFixedThreadPool(4);
    CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);
    String[] files = new File(path).list();
    if (files.length == 0) {
      return;
    }
    CountDownLatch latch = new CountDownLatch(files.length);
    for (int i = 0; i < files.length; i++) {
      pool.submit(new One2Three(path, files[i], latch));
    }
    latch.await();
    for (int i = 0; i < files.length; i++) {
      String result = pool.take().get();
      System.out.println(result + " is OK!");
    }
    threadPool.shutdown();
  }

  public static class One2Two implements Callable<String> {

    private String file;
    private String path;
    private CountDownLatch latch;

    public One2Two(String path, String file, CountDownLatch latch) {
      this.file = file;
      this.path = path;
      this.latch = latch;
    }

    @Override
    public String call() throws Exception {
      try {
        one(path, file);
        return file;
      } finally {
        latch.countDown();
      }
    }

  }
  
  public static class One2Three implements Callable<String> {

    private String file;
    private String path;
    private CountDownLatch latch;

    public One2Three(String path, String file, CountDownLatch latch) {
      this.file = file;
      this.path = path;
      this.latch = latch;
    }

    @Override
    public String call() throws Exception {
      try {
        one2three(path, file);
        return file;
      } finally {
        latch.countDown();
      }
    }

  }
  
  public static void one(String path, String v1) throws Exception {
    String tmp = v1.replaceFirst("\\.mp4$", "-1.mp4");
    sidebyside(path, //
        v1, //
        v1, //
        tmp);
  }
  
  public static void two2one(String path, String v1, String v2) throws Exception {
    String tmp = v1.replaceFirst("\\.mp4$", "-2.mp4");
    sidebyside(path, //
        v1, //
        v2, //
        tmp);
  }

  public static void one2three(String path, String v) throws Exception {
    String tmp = v.replaceFirst("\\.mp4$", "-2.mp4");
    sidebyside(path, //
        v, //
        v, //
        tmp);
    sidebyside(path, //
        tmp, //
        v, //
        v.replaceFirst("\\.mp4$", "-3.mp4"));
    File file = new File(path + "/" + tmp);
    if (file.exists()) {
      file.delete();
    }
  }

  public static void sidebyside(String path, //
      String v1, //
      String v2, //
      String dest) throws Exception {

    List<String> commands = new ArrayList<>();
    commands.add("/usr/local/bin/ffmpeg");//
    commands.add("-i");//
    commands.add(v1);// "4.mp4");//
    commands.add("-i");//
    commands.add(v2);// "5.mp4");//
    commands.add("-filter_complex");//
    commands.add("[0:v][1:v]hstack,format=yuv420p[v];[0:a][1:a]amerge[a]");//
    commands.add("-map");//
    commands.add("[v]");//
    commands.add("-map");//
    commands.add("[a]");//
    commands.add("-c:v");//
    commands.add("libx264");//
    commands.add("-crf");//
    commands.add("18");//
    commands.add("-ac");//
    commands.add("2");//
    commands.add(dest);//
    run(path, commands.toArray(new String[] {}));
  }

  public static void run(String path, String... commands)
      throws IOException, InterruptedException, UnsupportedEncodingException {
    ProcessBuilder pb = new ProcessBuilder(commands);
    pb.directory(new File(path));
    pb.redirectErrorStream(true);
    ProcessBuilder inheritBuilder = pb.inheritIO();
    inheritBuilder.redirectOutput(Redirect.PIPE);
    Process p = inheritBuilder.start();
    p.waitFor(); // wait for process to finish then continue.
    InputStream in = p.getInputStream();
    BufferedReader bri = new BufferedReader(//
        new InputStreamReader(in, "UTF-8"));
    String line = null;
    while ((line = bri.readLine()) != null) {
      System.out.println(line);
    }
    try {
      in.close();
    } catch (Exception e2) {
      e2.printStackTrace();
    }
    try {
      p.destroy();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    try {
      bri.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
