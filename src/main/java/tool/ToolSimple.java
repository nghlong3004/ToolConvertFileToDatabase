package tool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ToolSimple {

  private Map<String, String> dictionary = new HashMap<String, String>();

  public void writeFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("DictionaryEV.txt"), 1 << 16)) {
      for (Map.Entry<String, String> entry : dictionary.entrySet()) {
        StringBuilder sb = new StringBuilder();
        sb.append(entry.getKey()).append(entry.getValue()).append('\n');
        writer.write(sb.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Write File is succesfully!!");
  }

  public void readFile() {

    try (Stream<String> lines = Files.lines(Paths.get("A.txt"))) {
      lines.forEach(line -> {
        int splitIndex = line.indexOf('[');
        String value = line.substring(line.indexOf('<')).trim();
        if (splitIndex != -1 && value.length() > 125) {
          if (value.indexOf('[') == 126) {
            for (int i = 'a'; i <= 'z'; ++i) {
              String regux = "<td><img src=\"" + (char) (i) + ".png\"></td>";
              value = value.replace(regux, "");
            }
            value = value.replace("colspan=\"2\"", "");
            value = value.replace("colspan=\"3\"", "");
            value = value.replace("#7E0000", "#84cc16");
            String key = line.substring(0, line.indexOf('<')).trim();
            key = key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase();
            dictionary.put(key, value);
          }
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Read File is succesfully!!");

  }

  public Connection getConnectionDatabase() {
    Connection c = null;
    try {
      Class.forName("org.postgresql.Driver");
      c = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5432/dictionary?currentSchema=dictionary", "postgres",
          "123456");
    } catch (Exception e) {
      System.err.println("Not connection database " + c);
    }
    return c;
  }

  public void databaseExecuteUpdate(Word word) {
    try (Connection c = getConnectionDatabase()) {

      if (c != null) {
        String sql =
            "INSERT INTO dictionary(word, meaning, pronounce, language) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
          pstmt.setString(1, word.key);
          pstmt.setString(2, word.meaning);
          pstmt.setString(3, word.pronounce);
          pstmt.setString(4, word.language);
          pstmt.executeUpdate();
        } catch (Exception e) {
          System.err.println("Not execute update query " + e);
        }
      }
    } catch (SQLException e) {
      System.err.println("Connection Database is Error! " + e);
    }
  }

  public void closeConnectionDatabase(Connection c) {
    try {
      if (c != null) {
        c.close();
      }
    } catch (Exception e) {
      System.err.println("Not close connection database");
    }
  }


  public static void main(String[] args) {
    ToolSimple tool = new ToolSimple();
    tool.readFile();
    List<Word> words = new ArrayList<Word>();
    for (Map.Entry<String, String> entry : tool.dictionary.entrySet()) {
      Word word = new Word();
      word.key = entry.getKey();
      word.meaning = entry.getValue().replace("[", "").replace("]", "");
      word.pronounce = entry.getValue().substring(entry.getValue().indexOf('[') + 1,
          entry.getValue().indexOf(']'));
      word.language = "en";
      words.add(word);
    }
    words.forEach(wordElement -> {
      tool.databaseExecuteUpdate(wordElement);
    });
  }

}
