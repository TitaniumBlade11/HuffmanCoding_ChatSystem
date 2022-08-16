public class Temp {
  public static void main(String[] args) {
    HuffmanCoding huffmanCoding = new HuffmanCoding();
    String input = "%%%%%%%%%%%%";
    CompressedPackage compressedPackage = huffmanCoding.encode(input);
    System.out.println("Output: " + huffmanCoding.decode(compressedPackage.decodeTreeNodeRoot, compressedPackage.compressedString));
  }
}
