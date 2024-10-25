package dev.lpa;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

class Player implements Serializable {
  
  private final static long serialVersionUID = 1L;
  private String name;
  private int topScore;
  private long bigScore;
  private List<String> collectedWeapons = new ArrayList<>();
  
  public Player(String name, long topScore, List<String> collectedWeapons) {
    this.name = name;
    this.bigScore = topScore;
    this.collectedWeapons = collectedWeapons;
  }
  
  @Override
  public String toString() {
    return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
             .add("name='" + name + "'")
             .add("bigScore=" + bigScore)
             .add("collectedWeapons=" + collectedWeapons)
             .toString();
  }
}

public class Main {
  
  public static void main(String[] args) {
    
    Path dataFile = Path.of("data.dat");
    writeData(dataFile);
    readData(dataFile);
    
    Player tim = new Player("Tim", 100_000_010,
      List.of("knife", "machete", "pistol"));
    System.out.println("Before write: " + tim);
    
    Path timPath = Path.of("playerTim.dat");
    writeObject(timPath, tim);
    Player reconstitutedTim = readObject(timPath);
    System.out.println("After read: " + reconstitutedTim);
  }
  
  private static void writeData(Path dataFile) {
    
    try (DataOutputStream dataStream =
           new DataOutputStream(
             new BufferedOutputStream(
               new FileOutputStream(dataFile.toFile())))
    ) {
      int myInt = 17;
      long myLong = 100_000_000_000_000L;
      boolean myBoolean = true;
      char myChar = 'Z';
      float myFloat = 77.7f;
      double myDouble = 98.6;
      String myString = "Hello World";
      
      long position = 0;
      dataStream.writeInt(myInt);
      System.out.println("writeInt writes " + (dataStream.size() - position));
      position = dataStream.size();
      
      dataStream.writeLong(myLong);
      System.out.println("writeLong writes " + (dataStream.size() - position));
      position = dataStream.size();
      
      dataStream.writeBoolean(myBoolean);
      System.out.println("writeBoolean writes " + (dataStream.size() - position));
      position = dataStream.size();
      
      dataStream.writeChar(myChar);
      System.out.println("writeChar writes " + (dataStream.size() - position));
      position = dataStream.size();
      
      dataStream.writeFloat(myFloat);
      System.out.println("writeFloat writes " + (dataStream.size() - position));
      position = dataStream.size();
      
      dataStream.writeDouble(myDouble);
      System.out.println("writeDouble writes " + (dataStream.size() - position));
      position = dataStream.size();
      
      dataStream.writeUTF(myString);
      System.out.println("writeUTF writes " + (dataStream.size() - position));
      position = dataStream.size();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void readData(Path dataFile) {
    
    try (DataInputStream dataStream = new DataInputStream(
      Files.newInputStream(dataFile))) {  // special NIO2 Input Stream
      System.out.println("myInt = " + dataStream.readInt());
      System.out.println("myLong = " + dataStream.readLong());
      System.out.println("myBoolean = " + dataStream.readBoolean());
      System.out.println("myChar = " + dataStream.readChar());
      System.out.println("myFloat = " + dataStream.readFloat());
      System.out.println("myDouble = " + dataStream.readDouble());
      System.out.println("myString = " + dataStream.readUTF());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void writeObject(Path dataFile, Player player) {
    try (ObjectOutputStream objStream = new ObjectOutputStream(Files.newOutputStream(dataFile))) {
      objStream.writeObject(player);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static Player readObject(Path dataFile) {
    try (ObjectInputStream objStream = new ObjectInputStream(Files.newInputStream(dataFile))) {
      Object playerObj = objStream.readObject();
      return (playerObj instanceof Player player) ? player : null;
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
