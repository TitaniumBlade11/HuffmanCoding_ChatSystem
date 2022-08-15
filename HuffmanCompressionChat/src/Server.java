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
    try {
      while (true) {
        ObjectInputStream objectInputStream = new ObjectInputStream(localClient.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(localClient.getOutputStream());
        CompressedData receivedObj = (CompressedData) objectInputStream.readObject();
        textIncoming = receivedObj.data;
        System.out.println(textIncoming);
        ourText = consoleInput.nextLine();
        objectOutputStream.writeObject(new CompressedData(ourText));
      }
    } catch (Exception e) {
      System.out.println("All clients disconnected, stopping server.");
      localClient.close();
      socket.close();
    }

  }

}
