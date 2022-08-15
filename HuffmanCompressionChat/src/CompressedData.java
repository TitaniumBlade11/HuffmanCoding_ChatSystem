import java.io.Serializable;

public class CompressedData implements Serializable{
    String data;
    CompressedData(String data) {
      this.data = data;
    }
}
