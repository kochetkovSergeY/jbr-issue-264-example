package com.example.reloader.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class Reloader {


  @PostConstruct
  public void init() throws IOException, InterruptedException, IllegalConnectorArgumentsException {
    int attemptCount = 1;

    for (int i = 0;
         i < 10;
         i++) {

//      int classBytesNum = getRandomNumberUsingInts(0, 7);
      for (int classBytesNum = 0;
           classBytesNum < 7;
           classBytesNum++) {
        byte[] byteCode = getClass().getResourceAsStream(
                "/ClassToReload" + classBytesNum + ".class")
            .readAllBytes();
        Map<String, byte[]> map = new HashMap<>();
        map.put("com.example.hotswapagenterror.test.ClassToReload", byteCode);
        HotSwapper hotSwapper = new HotSwapper(Integer.toString(8787), "localhost");
        hotSwapper.reload(map);
        hotSwapper.dispose();
        Thread.sleep(500);
        System.out.println("Reload complete for attempt=" + attemptCount);
        attemptCount++;
      }

    }
  }

  public int getRandomNumberUsingInts(int min, int max) {
    Random random = new Random();
    return random.ints(min, max)
        .findFirst()
        .getAsInt();
  }
}
