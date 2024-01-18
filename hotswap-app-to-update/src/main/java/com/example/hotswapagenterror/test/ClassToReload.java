package com.example.hotswapagenterror.test;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class ClassToReload {


  public String getTest() {
    randomString();
    return new StringBuilder("").append("asf;iugsdcipuasd").append("adscasdcasdc").toString();
  }

  public void randomString() {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random();

    String generatedString = random.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

    System.out.println(generatedString);
  }
}
