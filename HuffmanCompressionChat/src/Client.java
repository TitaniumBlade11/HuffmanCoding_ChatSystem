
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

// In our implementation, client always starts the conversation when it connects to a server, the
// communication is a 2 way chat system where the chance for sending chat is done in a sea-saw
// fashion i.e client sends msg then server then client this is to demonstrate the functionality of
// Huffman compression more accurately for 1 block of text message(block is variable length that
// ends with a new line character) at a time as the aim of this project is to create a POC for a
// duplex chat system using Huffman compression-decompression algorithm for space efficiency.

/**
 * This class represents the set of functionalities to establish communication with the chat server.
 */
public class Client {
  public static void main(String[] args) {
    try {
      // Bind the socket to a local port.
      Socket localClient = new Socket("localhost", 10000);
      System.out.println("Connecting to server....");

      // Get the I/O streams connected to the local socket.
      OutputStream outputStream = localClient.getOutputStream();
      InputStream inputStream = localClient.getInputStream();

      // Input pipe from console.
      Scanner consoleInput = new Scanner(System.in);

      String textTyped = null;
      String textFromOtherSide = null;

      // Creating an object of HuffmanCoding to leverage its utilities to encode and decode the
      // strings.
      HuffmanCoding huffmanCoding = new HuffmanCoding();

      // To push the serialized object in the output stream pipe of the local socket to remote end.
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

      // To receive serialized object from the other end using input stream of the local socket.
      ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

      while (true) {
        // Input from local user.
        textTyped = consoleInput.nextLine();
        // Pack the encoded string and its tree in a CompressedPackage object.
        CompressedPackage compressedPackage = huffmanCoding.encode(textTyped);

        // Push the compressed object through the object output stream pipeline to remote server.
        objectOutputStream.writeObject(compressedPackage);

        // If the local client types "BYE" in console, break out of the loop.
        if ("BYE".equals(textTyped))
          break;

        // Read and deserialize the incoming CompressedPackage object from the remote end.
        CompressedPackage incomingCompressedPackage = (CompressedPackage) objectInputStream
                .readObject();

        // Get the incoming text after decoding the compressed encoded string.
        textFromOtherSide = huffmanCoding.decode(incomingCompressedPackage.decodeTreeNodeRoot,
                incomingCompressedPackage.compressedString);

        System.out.println("Text from other side: " + textFromOtherSide);

        // If remote party send "BYE" message, break out of the loop.
        if (textFromOtherSide.contains("BYE"))
          break;
      }

      // Safely release the resources occupied by the local client socket.
      localClient.close();
    } catch (Exception e) {
      System.out.println("Could not establish successful chat communication with the server.");
    }
  }
}
