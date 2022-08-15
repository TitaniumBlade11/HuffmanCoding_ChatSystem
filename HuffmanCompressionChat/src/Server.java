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

//    BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(inputStream));
    Scanner consoleInput = new Scanner(System.in);

    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

    String textIncoming = null;
    String ourText = null;
    while (true) {
//      textIncoming = bufferedInput.readLine();
//      System.out.println("Text from other side: " + textIncoming);
//      if("BYE".equals(textIncoming))
//        break;
      ObjectInputStream objectInputStream = new ObjectInputStream(localClient.getInputStream());
      CompressedData receivedObj = (CompressedData) objectInputStream.readObject();
      System.out.println(receivedObj.data);
//      ourText = consoleInput.nextLine();
//      dataOutputStream.writeBytes(ourText + "\n");
//
//      if("BYE".equals(ourText))
//        break;
    }

//    localClient.close();
//    socket.close();

  }

}
