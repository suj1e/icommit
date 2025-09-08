package org.flooc.plugin.icommit.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class PropertiesUtils {

  private PropertiesUtils() {
  }

  private static final Map<String, Properties> PROPERTIES_CACHE = new ConcurrentHashMap<>();

  public static Properties readIfPossible(String path) throws IOException {
    if (PROPERTIES_CACHE.containsKey(path)) {
      return PROPERTIES_CACHE.get(path);
    }
    try {
      // 普通读取
      try (InputStream inputStream = PropertiesUtils.class.getClassLoader()
          .getResourceAsStream(path)) {
        String content = new String(inputStream.readAllBytes());
        Properties properties = convertStringToProperties(content);
        PROPERTIES_CACHE.put(path, properties);
        return properties;
      }
    } catch (Exception e) {
      // jar方式读取
      CodeSource codeSource = PropertiesUtils.class.getProtectionDomain().getCodeSource();
      if (codeSource == null) {
        return null;
      }
      String jarPath = codeSource.getLocation().getPath();
      try (JarFile jarFile = new JarFile(jarPath)) {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
          JarEntry jarEntry = entries.nextElement();
          if (jarEntry.getName().endsWith(path)) {
            try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
              String content = new String(inputStream.readAllBytes());
              Properties properties = convertStringToProperties(content);
              PROPERTIES_CACHE.put(path, properties);
              return properties;
            }
          }
        }
      }
    }
    return null;
  }

  private static Properties convertStringToProperties(String content) throws IOException {
    Properties properties = new Properties();

    // 将字符串转为字节输入流（指定UTF-8编码，避免平台默认编码问题）
    try (InputStream inputStream = new ByteArrayInputStream(
        content.getBytes(StandardCharsets.UTF_8))) {

      // 加载输入流到Properties对象
      properties.load(inputStream);
    }

    return properties;
  }

}