import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

import static com.sun.tools.javac.util.Constants.format;

public class Bills {
  int patient_id;
  int prescription_id;
  boolean paid;

  public Bills(int patient_id, int prescription_id, boolean paid) {
    this.patient_id = patient_id;
    this.prescription_id = prescription_id;
    this.paid = paid;
  }

  void addBill(Connection conn) throws SQLException {
    Statement st = conn.createStatement();
    String query = "INSERT INTO Bills VALUES(" +
        patient_id + "," +
        prescription_id + "," +
        paid + ");";

    st.executeUpdate(query);
  }

  static void removeBill(Connection conn, int patient_id, int doctor_id) throws SQLException {
    Statement st = conn.createStatement();
    st.execute("SET FOREIGN_KEY_CHECKS = 0;");
    String query = "DELETE FROM Bills " +
        "WHERE patient_id=" + patient_id + " " +
        "AND doctor_id=" + doctor_id + ";";
    st.execute(query);
  }

  static void updateBill(Connection conn, int patient_id, int doctor_id, boolean paid) throws SQLException {
    Statement st = conn.createStatement();
    String query = "UPDATE Bills " +
        "SET " +
        "patient_id=" + patient_id + "," +
        "doctor_id=" + doctor_id + "," +
        "paid=" + paid + " " +

        "WHERE patient_id=" + patient_id + " " +
        "AND doctor_id=" + doctor_id + ";";
    st.execute(query);
  }

}
