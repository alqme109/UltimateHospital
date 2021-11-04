import com.mysql.cj.protocol.Resultset;

import java.sql.*;

public class Doctors {
  static int static_id;

  static {
    try {
      static_id = Hospital.getNextId("Doctors");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  int id;
  String full_name;
  String email_address;
  String specialty;

  public Doctors(String full_name, String email_address, String specialty) { // new doctor
    this.id = static_id++;
    this.full_name = full_name;
    this.email_address = email_address;
    this.specialty = specialty;
  }

  public Doctors(int id, String full_name, String email_address, String specialty) { // existing doctor
    this.id = id;
    this.full_name = full_name;
    this.email_address = email_address;
    this.specialty = specialty;
  }


  void addDoctor(Connection conn) throws SQLException {
    Statement st = conn.createStatement();
    String query = "INSERT INTO Doctors VALUES("+
        id+","+
        "\""+full_name+"\""+","+
        "\""+email_address+"\""+","+
        "\""+specialty+"\"" +");";

    st.executeUpdate(query);

  }

  static void removeDoctor(Connection conn, int id) throws SQLException {
    Statement st = conn.createStatement();
    st.execute("SET FOREIGN_KEY_CHECKS = 0;");
    String query = "DELETE FROM Doctors " +
        "WHERE id="+id+";";
    st.execute(query);
  }

  static void updateInfo(Connection conn, int id, String newName, String newEmail, String newSpecialty) throws SQLException {
    Statement st = conn.createStatement();
    String query = "UPDATE Doctors " +
        "SET " +
        "full_name=" + "\"" + newName + "\"" + "," +
        "email_address=" + "\"" + newEmail + "\"" + "," +
        "specialty=" + "\"" + newSpecialty + "\"" + " " +
        "WHERE id=" + id + ";";
    st.execute(query);
  }

  static String getDoctorSpecialty(Connection conn, int id) throws SQLException {
    Statement st = conn.createStatement();
    String query = "SELECT specialty FROM Doctors WHERE id=" + id + ";";
    ResultSet rs = st.executeQuery(query);

    if (rs.next()) {
      return rs.getString(1);
    }
    return null;
  }

  static String getDoctorName(Connection conn, int id) throws SQLException {
    Statement st = conn.createStatement();
    String query = "SELECT full_name FROM Doctors WHERE id=" + id + ";";
    ResultSet rs = st.executeQuery(query);

    if (rs.next()) {
      return rs.getString(1);
    }
    return null;
  }

}
