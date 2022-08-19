import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// In our implementation, client always starts the conversation when it connects to a server, the
// communication is a 2 way chat system where the chance for sending chat is done in a sea-saw
// fashion i.e client sends msg then server then client this is to demonstrate the functionality of
// Huffman compression more accurately for 1 block of text message(block is variable length that
// ends with a new line character) at a time as the aim of this project is to create a POC for a
// duplex chat system using Huffman compression-decompression algorithm for space efficiency.

/**
 * This class represents the set of functionalities for a chat server to establish connection and
 * further communicate with the clients.
 */
public class Server {
  public static void main(String[] args) {
    ServerSocket socket = null;
    Socket localClient = null;

    // Storing metrics of input data compression rate as Map:
    // String input -> {BeforeCompressionSize, AfterCompressionSize, CompressionRatio}
    Map<String, List<Double>> metricsMap = new HashMap<>();

    try {
      // Create a server socket that listens to connection requests from remote client.
      socket = new ServerSocket(10000);

      // Create a local client socket for establishing communication with remote client to enable 2
      // way chat.
      localClient = socket.accept();
      System.out.println("Got connected to the other side.");

      // Get the I/O streams connected to the local socket.
      OutputStream outputStream = localClient.getOutputStream();
      InputStream inputStream = localClient.getInputStream();

      // Input pipe from console.
      Scanner consoleInput = new Scanner(System.in);
      DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

      String textIncoming = null;
      String ourText = null;

      // Creating an object of HuffmanCoding to leverage its utilities to encode and decode the
      // strings.
      HuffmanCoding huffmanCoding = new HuffmanCoding();

      // To push the serialized object in the output stream pipe of the local socket to remote end.
      ObjectInputStream objectInputStream = new ObjectInputStream(localClient.getInputStream());

      // To receive serialized object from the other end using input stream of the local socket.
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(localClient.getOutputStream());

      while (true) {
        // Read and deserialize the incoming CompressedPackage object from the remote end.
        CompressedPackage compressedPackage = (CompressedPackage) objectInputStream.readObject();

        // Get the incoming text after decoding the compressed encoded string.
        textIncoming = huffmanCoding.decode(compressedPackage.decodeTreeNodeRoot, compressedPackage
                .compressedString);

        System.out.println(textIncoming);

        // Input from local user.
        ourText = consoleInput.nextLine();

        // Pack the encoded string and its tree in a CompressedPackage object.
        CompressedPackage sendCompressedPackage = huffmanCoding.encode(ourText);

        // Storing metrics into map for string->(beforeCompressionSize, afterCompressionSize, compressionRatio)
        Double beforeCompressionSize = (double) ourText.length()*16;// Java uses unicode character encoding for each character.
        Double afterCompressionSize = (double) compressedPackage.compressedString.length();

        List<Double> metricsList = new ArrayList<>();
        metricsList.add(beforeCompressionSize);
        metricsList.add(afterCompressionSize);
        metricsList.add(afterCompressionSize/ beforeCompressionSize);

        metricsMap.put(ourText, metricsList);

        // Push the compressed object through the object output stream pipeline to remote client.
        objectOutputStream.writeObject(sendCompressedPackage);
      }
    } catch (IOException ioe) {
      System.out.println("Could not establish successful connection pipeline to remote client.");
    } catch (Exception e) {
      System.out.println("All clients disconnected, stopping server.");
      try {
        localClient.close();
        socket.close();
      } catch(IOException ioe) {
        System.out.println("Could not close the socket.");
      }
    }

    // Printing out the metrics of compression
    System.out.println("--------------METRICS---------------");
    System.out.println("Input String -> Compression Ratio = (Compressed size / Originalsize)");
    boolean start = true;
    for (String key : metricsMap.keySet()) {
      if(start) {
        start = false;
        continue;
    }
      System.out.println(key + " ------> " + metricsMap.get(key).get(2));
    }
  }
}
