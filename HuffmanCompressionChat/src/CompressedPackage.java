import java.io.Serializable;

/***
 * The class {@code CompressedPackage} represents a collection of data, which has {@code TreeNode} used to decode
 * the values and compressed string.
 */
public class CompressedPackage implements Serializable {

    public TreeNode decodeTreeNodeRoot;
    public String compressedString;

  /***
   * Constructs the {@code CompressedPackage} object.
   *
   * @param decodeTreeNodeRoot the root of the tree node
   * @param compressedString the compressed string
   */
  public CompressedPackage(TreeNode decodeTreeNodeRoot, String compressedString) {
      this.decodeTreeNodeRoot = decodeTreeNodeRoot;
      this.compressedString = compressedString;
    }
}
