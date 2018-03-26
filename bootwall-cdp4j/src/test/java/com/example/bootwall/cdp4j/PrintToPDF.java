package com.example.bootwall.cdp4j;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import io.webfolder.cdp.AdaptiveProcessManager;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class PrintToPDF {

  // Requires Headless Chrome
  // https://chromium.googlesource.com/chromium/src/+/lkgr/headless/README.md
  public static void main(String[] args) throws IOException {
    Launcher launcher = new Launcher();
    launcher.setProcessManager(new AdaptiveProcessManager());

    Path file = Files.createTempFile("cdp4j", ".pdf");

    try (SessionFactory factory = launcher.launch(Arrays.asList("--disable-gpu", "--headless"))) {

      String context = factory.createBrowserContext();
      try (Session session = factory.create(context)) {

        session.navigate("https://webfolder.io/cdp4j.html");
        session.waitDocumentReady();

        byte[] content = session.getCommand().getPage().printToPDF();

        Files.write(file, content);
      }

      factory.disposeBrowserContext(context);
    }

    if (Desktop.isDesktopSupported()) {
      Desktop.getDesktop().open(file.toFile());
    }

    launcher.getProcessManager().kill();
  }
}
