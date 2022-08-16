import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCoding {

  public CompressedPackage encode(String originalString) {
    if(originalString == null) {
      return null;
    }
    Map<Character, Integer> frequencyMap = new HashMap<>();
    for(Character c : originalString.toCharArray()) {
      frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
    }
    PriorityQueue<TreeNode> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a.frequency));
    for(Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
      heap.offer(new TreeNode(entry.getValue(), entry.getKey()));
    }

    TreeNode rootNode = new TreeNode();
    if(heap.size() <= 1) {
      rootNode = heap.poll();
    }
    while(heap.size() > 1) {
      TreeNode leftNode = heap.poll();
      TreeNode rightNode = heap.poll();
      rootNode = new TreeNode((leftNode.frequency + rightNode.frequency), null);
      rootNode.left = leftNode;
      rootNode.right = rightNode;
      heap.offer(rootNode);
    }

    Map<Character, String> decodingMap = new HashMap<>();
    convertEncodingTreeToHashMap(rootNode, "", decodingMap);
    StringBuilder compressedString = new StringBuilder();
    for(Character ch : originalString.toCharArray()) {
      compressedString.append(decodingMap.get(ch));
    }
    return new CompressedPackage(rootNode, compressedString.toString());
  }

  public String decode(TreeNode rootNode, String encodedString) {
    StringBuilder originalString = new StringBuilder();
    int i=0, j=0;
    for(; j <= encodedString.length(); j++) {
      String partOfString = encodedString.substring(i, j);
      Character charPart = decodeTheCharacterFromTheTree(rootNode, 0, partOfString);
      if(charPart != null) {
        originalString.append(charPart);
        i = j;
      }
    }
    return originalString.toString();
  }

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