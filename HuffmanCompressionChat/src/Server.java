import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {
  public static void main(String[] args) throws Exception {
    ServerSocket socket = new ServerSocket(10000);
    Socket localClient = socket.accept();
    System.out.println("Got connected to the other side.");

    OutputStream outputStream = localClient.getOutputStream();
    InputStream inputStream = localClient.getInputStream();

    Scanner consoleInput = new Scanner(System.in);
    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

    String textIncoming = null;
    String ourText = null;
    HuffmanCoding huffmanCoding = new HuffmanCoding();
    ObjectInputStream objectInputStream = new ObjectInputStream(localClient.getInputStream());
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(localClient.getOutputStream());

    try {
      while (true) {
        CompressedPackage compressedPackage = (CompressedPackage) objectInputStream.readObject();
        textIncoming = huffmanCoding.decode(compressedPackage.decodeTreeNodeRoot, compressedPackage.compressedString);

        System.out.println(textIncoming);

        ourText = consoleInput.nextLine();
        CompressedPackage sendCompressedPackage = huffmanCoding.encode(ourText);
        objectOutputStream.writeObject(sendCompressedPackage);
      }
    } catch (Exception e) {
      System.out.println("All clients disconnected, stopping server.");
      localClient.close();
      socket.close();
    }

  }

}
