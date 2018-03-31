package com.example.bootwall.tpl.runner;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.mp4parser.Container;
import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.Track;
import org.mp4parser.muxer.builder.DefaultMp4Builder;
import org.mp4parser.muxer.container.mp4.MovieCreator;
import org.mp4parser.muxer.tracks.AppendTrack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class VedioConcatRunner implements CommandLineRunner {

  @Value("${mp4.targetPath}")
  private String targetPath;
  
  @Override
  public void run(String... args) throws Exception {
    log.debug(MessageFormat.format("视频集：{0}", Arrays.asList(args)));
    log.debug(MessageFormat.format("目标地址：{0}", targetPath));
    videoMerge(args, targetPath);
    System.exit(0);
  }

  public void videoMerge(String[] srcVideoPath, String dstVideoPath) throws IOException {
    List<Movie> inMovies = new ArrayList<Movie>();
    for (String videoUri : srcVideoPath) {
      log.info(MessageFormat.format("地址：{0}", videoUri));
      inMovies.add(MovieCreator.build(videoUri));
    }
    List<Track> videoTracks = new LinkedList<Track>();
    List<Track> audioTracks = new LinkedList<Track>();

    for (Movie m : inMovies) {
      for (Track t : m.getTracks()) {
        if (t.getHandler().equals("soun")) {
          audioTracks.add(t);
        }
        if (t.getHandler().equals("vide")) {
          videoTracks.add(t);
        }
      }
    }

    Movie result = new Movie();

    if (audioTracks.size() > 0) {
      result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
    }
    if (videoTracks.size() > 0) {
      result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
    }

    Container out = new DefaultMp4Builder().build(result);

    RandomAccessFile accFile = new RandomAccessFile(String.format(dstVideoPath), "rw");
    FileChannel fc = accFile.getChannel();
    out.writeContainer(fc);
    fc.close();
    accFile.close();
  }

}
