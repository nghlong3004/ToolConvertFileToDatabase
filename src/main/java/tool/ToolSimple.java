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

  public void readFileEV() {

    try (Stream<String> lines = Files.lines(Paths.get("A.txt"))) {
      lines.forEach(line -> {
        int splitIndex = line.indexOf('[');
        String value = line.substring(line.indexOf('<')).trim();
        if (splitIndex != -1 && value.length() > 125) {
          if (value.indexOf('[') == 126) {
            String key = line.substring(0, line.indexOf('<')).trim();
            key = key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase();
            dictionary.put(key, value);
          }
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Read File Eng - Viet is succesfully!!");

  }

  public void readFileVE() {

    try (Stream<String> lines = Files.lines(Paths.get("DictionaryVE.txt"))) {
      lines.forEach(line -> {
        String value = line.substring(line.indexOf('<')).trim();
        String key = line.substring(0, line.indexOf('<')).trim();
        key = key.toLowerCase();
        dictionary.put(key, value);

      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Read File Viet - Eng is succesfully!!");

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
            "INSERT INTO dictionary(word, meaning, pronounce, language, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
          pstmt.setString(1, word.key);
          pstmt.setString(2, word.meaning);
          pstmt.setString(3, word.pronounce);
          pstmt.setString(4, word.language);
          pstmt.setString(5, word.description);
          pstmt.executeUpdate();
        } catch (Exception e) {
          System.err.println("Not execute update query " + e);
        }
      }
    } catch (SQLException e) {
      System.err.println("Connection Database is Error! " + e);
    }
    System.out.println("OK!");
  }

  public void databaseExecuteBatchInsertWithTransaction(List<Word> words) {
    try (Connection c = getConnectionDatabase()) {
      if (c != null) {
        c.setAutoCommit(false);
        String sql =
            "INSERT INTO dictionary(word, meaning, pronounce, language, description) VALUES (?, ?, ?, ?, ?) ON CONFLICT (word) DO NOTHING";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
          for (Word word : words) {
            
              pstmt.setString(1, word.key);
              pstmt.setString(2, word.meaning);
              pstmt.setString(3, word.pronounce);
              pstmt.setString(4, word.language);
              pstmt.setString(5, word.description);
              pstmt.addBatch();
           
          }
          System.out.println("Start");
          pstmt.executeBatch();
          c.commit();
        } catch (Exception e) {
          c.rollback();
          System.err.println("Transaction failed: " + e);
        } finally {
          c.setAutoCommit(true);
        }
      }
    } catch (SQLException e) {
      System.err.println("Connection error: " + e);
    }
    System.out.println("Batch insert with transaction completed!");
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

  private List<Word> targetWordsEV() {
    List<Word> words = new ArrayList<Word>();
    System.out.println(dictionary.size());
    for (Map.Entry<String, String> entry : dictionary.entrySet()) {
      String value = entry.getValue();
      for (int i = 'a'; i <= 'z'; ++i) {
        String regux = "<td><img src=\"" + (char) (i) + ".png\"></td>";
        value = value.replace(regux, "");
      }
      value = value.replace("colspan=\"2\"", "");
      value = value.replace("colspan=\"3\"", "");
      value = value.replace("#7E0000", "#16a34a");
      value = value.replace("#0000FF", "#ef4444");
      value = value.replace("#FF0000", "#57534e");
      value = value.replace("#7E7E7E", "#737373");
      Word word = new Word();
      word.key =
          entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1).toLowerCase();
      word.description = value.replace("[", "").replace("]", "").replace("\'", "");
      word.pronounce = value.substring(value.indexOf('[') + 1, value.indexOf(']'));
      word.language = "en";
      word.meaning = word.description.substring(word.description.indexOf(';') + 1);
      if (word.meaning.charAt(0) != '<') {
        word.meaning = word.meaning.substring(word.meaning.indexOf(';'));
      }
      word.meaning = word.meaning.substring(9);
      word.meaning = word.meaning.replace(">", "");
      word.meaning = word.meaning.substring(0, word.meaning.indexOf('<')).trim();
      word.meaning = word.meaning.replace(">", "");
      if (!word.meaning.isEmpty() && !word.description.contains("Như")
          && !word.description.contains("như"))
        words.add(word);
      else if (!word.description.contains("Như") && !word.description.contains("như")) {
        StringBuffer sbr = new StringBuffer(word.description);
        sbr.reverse();
        String a = sbr.substring(0, sbr.indexOf(";"));
        sbr = new StringBuffer(a);
        sbr.reverse();
        a = sbr.toString().substring(8);
        a = a.substring(0, a.indexOf('<'));
        a = a.replace("<", "");
        a = a.replace(">", "");
        a = a.trim();
        if (!a.isEmpty()) {
          word.meaning = a;
          words.add(word);
        }
      }
    }
    return words;
  }

  private List<Word> targetWordsVE() {
    List<Word> words = new ArrayList<Word>();
    for (Map.Entry<String, String> entry : dictionary.entrySet()) {
      String value = entry.getValue();
      if (value.contains("<B><I>") && value.contains("<B> ")) {
        String pronounce = value.substring(1);
        Word word = new Word();
        word.key = entry.getKey().substring(0, 1).toUpperCase()
            + entry.getKey().substring(1).toLowerCase();
        word.description = value.replace(
            "(C) 2007 <A href='http://www.tudientiengviet.net'>www.TừĐiểnTiếngViệt.net</A>", "");
        pronounce = pronounce.substring(pronounce.indexOf("<B><I>"));
        word.pronounce = pronounce.substring(pronounce.indexOf(' '));
        word.meaning = word.pronounce.substring(word.pronounce.indexOf("<B>") + 3);
        word.meaning = word.meaning.substring(0, word.meaning.indexOf('<')).trim();
        word.pronounce = word.pronounce.substring(0, word.pronounce.indexOf('<'));
        word.language = "vi";
        if (!word.meaning.isEmpty()) {
          words.add(word);
        }
      }
    }
    return words;
  }

  public static void main(String[] args) {
    ToolSimple tool = new ToolSimple();
    tool.readFileVE();
    List<Word> words = tool.targetWordsVE();
//    // eng -> viet
//    words.forEach(word -> {
//      if (word.meaning.contains("(") && word.meaning.length() == 1) {
//        String value = word.description;
//        value = value.substring(value.indexOf(';') + 10);
//        value = value.substring(value.indexOf(')') + 1);
//        while (value.charAt(0) == '(' && value.charAt(1) == '<') {
//          value = value.substring(value.indexOf(')') + 2);
//        }
//        value = value.substring(0, value.indexOf('<'));
//        word.meaning = value.trim();
//      }
//      if ((word.meaning.length() == 1 && word.meaning.contains("(")) || word.meaning.isEmpty()) {
//
//      } else {
//        if (word.meaning.length() == 1)
//          word.meaning = word.meaning.substring(0, 1).toUpperCase();
//        else
//          word.meaning =
//              word.meaning.substring(0, 1).toUpperCase() + word.meaning.substring(1).toLowerCase();
//      }
//    });
    // Viet -> eng
    System.out.println(words.size());
    words.forEach(word -> {
      word.partOfSpeech = word.pronounce;
      word.pronounce = "";
     // System.out.println(word.description);
    });
    
    tool.databaseExecuteBatchInsertWithTransaction(words);
    // words = tool.targetWordsEV();
  }

}
