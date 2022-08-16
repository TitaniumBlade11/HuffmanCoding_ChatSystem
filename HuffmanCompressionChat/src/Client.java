import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  public static void main(String[] args) throws Exception {
    Socket localClient = new Socket("localhost", 10000);
    System.out.println("Connecting to server....");

    OutputStream outputStream = localClient.getOutputStream();
    InputStream inputStream = localClient.getInputStream();

    BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(inputStream));
    Scanner consoleInput = new Scanner(System.in);

    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

    String textTyped = null;
    String textFromOtherSide = null;
    HuffmanCoding huffmanCoding = new HuffmanCoding();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

    while (true) {
      textTyped = consoleInput.nextLine();
      CompressedPackage compressedPackage = huffmanCoding.encode(textTyped);
      objectOutputStream.writeObject(compressedPackage);

      if ("BYE".equals(textTyped))
        break;

      CompressedPackage incomingCompressedPackage = (CompressedPackage) objectInputStream.readObject();
      textFromOtherSide = huffmanCoding.decode(incomingCompressedPackage.decodeTreeNodeRoot,
              incomingCompressedPackage.compressedString);

      System.out.println("Text from other side: " + textFromOtherSide);

      if (textFromOtherSide.contains("BYE"))
        break;
    }

      localClient.close();
  }
}
