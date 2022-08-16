import java.io.Serializable;

public class TreeNode implements Serializable {
    public int frequency;
    public Character character;
    public TreeNode left;

    public Character edgeWeight;
    public TreeNode right;
    public TreeNode(int frequency, Character character, TreeNode left, TreeNode right, Character edgeWeight) {
      this.frequency = frequency;
      this.character = character;
      this.left = left;
      this.right = right;
      this.edgeWeight = edgeWeight;
    }

    public TreeNode() {
      this.frequency = 0;
      this.character = null;
      this.left = null;
      this.right = null;
      this.edgeWeight = null;
    }

    public TreeNode(int frequency, Character character) {
      this.frequency = frequency;
      this.character = character;
      this.left = null;
      this.right = null;
      this.edgeWeight = null;
    }
}
