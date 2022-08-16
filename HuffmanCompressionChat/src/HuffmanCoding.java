import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/***
 * The class {@code HuffmanCoding} represents the Huffman coding Algorithm, which is used to
 * compress the string.
 */
public class HuffmanCoding {

  /***
   * Compress the given original message and create a package {@code CompressedPackage} which contains
   * {@code TreeNode} root of the tree and the compressed message.
   * Time complexity O(n log n)
   * Space Complexity O(1)
   *
   * @param originalString the original message
   * @return the compressed package
   */
  public CompressedPackage encode(String originalString) {
    // map used to store the frequencies of the character in original string.
    Map<Character, Integer> frequencyMap = new HashMap<>();
    // updating the frequency.
    for(Character c : originalString.toCharArray()) {
      frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
    }

    // heap used to find the most frequent character in string time O(log n).
    PriorityQueue<TreeNode> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a.frequency));
    for(Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
      heap.offer(new TreeNode(entry.getValue(), entry.getKey()));
    }

    TreeNode rootNode = new TreeNode();
    if(heap.size() <= 1) {
      rootNode = heap.poll();
    }
    // Iterates until the tree is formed.
    // time O(n log n)
    while(heap.size() > 1) {
      TreeNode leftNode = heap.poll();
      TreeNode rightNode = heap.poll();
      rootNode = new TreeNode((leftNode.frequency + rightNode.frequency), null);
      rootNode.left = leftNode;
      rootNode.right = rightNode;
      heap.offer(rootNode);
    }

    // map used to store the encoded string for character.
    Map<Character, String> decodingMap = new HashMap<>();
    convertEncodingTreeToHashMap(rootNode, "", decodingMap);
    StringBuilder compressedString = new StringBuilder();
    // building the compressed String here from the encoded values.
    for(Character ch : originalString.toCharArray()) {
      compressedString.append(decodingMap.get(ch));
    }
    return new CompressedPackage(rootNode, compressedString.toString());
  }

  /***
   * Decodes the given compressed string with the help of {@code TreeNode} root and
   * gets the original message.
   * Time complexity O(N log M) - N is the encoded string length and M is number of unique characters in the tree.
   * @param rootNode the root of the decode tree
   * @param encodedString the encoded string
   * @return the original message
   */
  public String decode(TreeNode rootNode, String encodedString) {
    StringBuilder originalString = new StringBuilder();
    int i=0, j=0;
    for(; j <= encodedString.length(); j++) {
      String partOfString = encodedString.substring(i, j);
      // getting the character based on the part of the string if it exists in the tree.
      Character charPart = decodeTheCharacterFromTheTree(rootNode, 0, partOfString);
      if(charPart != null) {
        originalString.append(charPart);
        i = j;
      }
    }
    return originalString.toString();
  }

  /***
   * Helper method used to map character to the given decode string.
   * time complexity O(2 ^ n) here n is constant (character are only 256).
   * @param rootNode the root node of the tree
   * @param accumulate accumulation value.
   * @param decodingMap decoding mapping
   */
  private void convertEncodingTreeToHashMap(TreeNode rootNode, String accumulate, Map<Character, String> decodingMap) {
      if(rootNode == null) {
        return;
      }
      if(rootNode.right == null && rootNode.left == null) {
         decodingMap.put(rootNode.character, accumulate);
         return;
      }
      if(rootNode.left != null) {
        convertEncodingTreeToHashMap(rootNode.left, accumulate + '0', decodingMap);
      }
      if(rootNode.right != null) {
        convertEncodingTreeToHashMap(rootNode.right, accumulate + '1', decodingMap);
      }
  }

  /***
   * Decodes and find the character from the given part of the compressed string.
   * time complexity O(log n) - each time half of the tree is being skipped.
   * @param rootNode the root node
   * @param i the current index
   * @param partOfString part of the string
   * @return decoded character
   */
  private Character decodeTheCharacterFromTheTree(TreeNode rootNode, int i, String partOfString) {
    if(rootNode == null) {
      return null;
    }
    if(i == partOfString.length() && rootNode.character != null) {
        return rootNode.character;
    } else if(i == partOfString.length()){
      return null;
    }

    Character left = null;
    Character right = null;
    if(i < partOfString.length() && partOfString.charAt(i) == '0') {
      left = decodeTheCharacterFromTheTree(rootNode.left, i+1, partOfString);
    }
    if (i < partOfString.length() && partOfString.charAt(i) == '1') {
      right = decodeTheCharacterFromTheTree(rootNode.right, i+1, partOfString);
    }
    return left == null ? right : left;
  }
}
