import java.text.SimpleDateFormat;
import java.util.Date;
public class test {
  public static void main(String[] args)throws Exception {
    String s = "2021-01-09";
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    Date date = df.parse(s);

    System.out.print("date is: ");
    System.out.println(date);
  }


}