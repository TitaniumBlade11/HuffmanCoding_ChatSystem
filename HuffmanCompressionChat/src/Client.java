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

    while (true) {
      textTyped = consoleInput.nextLine();
      CompressedData compressedData = new CompressedData(textTyped);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
      objectOutputStream.writeObject(compressedData);

      if ("BYE".equals(textTyped))
        break;

      ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
      textFromOtherSide = ((CompressedData) objectInputStream.readObject()).data;
      System.out.println("Text from other side: " + textFromOtherSide);

      if (textFromOtherSide.contains("BYE"))
        break;
    }

      localClient.close();
  }
}
