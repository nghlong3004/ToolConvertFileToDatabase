package tool;

public class Word {
  public String key;
  public String pronounce;
  public String specialized_id;
  public String synonym;
  public String antonym;
  public String meaning;
  public String description;
  public String partOfSpeech;
  public String language;
  
  public Word() {

  }

  public Word(String word, String pronounce, String specialized_id, String synonym,
      String antonym, String meaning, String description) {
    super();
    this.key = word;
    this.pronounce = pronounce;
    this.specialized_id = specialized_id;
    this.synonym = synonym;
    this.antonym = antonym;
    this.meaning = meaning;
    this.description = description;
  }


}
