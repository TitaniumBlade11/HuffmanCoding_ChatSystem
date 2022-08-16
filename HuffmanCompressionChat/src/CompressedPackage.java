import java.io.Serializable;

public class CompressedPackage implements Serializable {

    public TreeNode decodeTreeNodeRoot;
    public String compressedString;

    public CompressedPackage(TreeNode decodeTreeNodeRoot, String compressedString) {
      this.decodeTreeNodeRoot = decodeTreeNodeRoot;
      this.compressedString = compressedString;
    }
}
