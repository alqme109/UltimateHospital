import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class Patients {
  static int static_id;

  static {
    try {
      static_id = Hospital.getNextId("Patients");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  int id;
  String full_name;
  String email_address;
  int number_of_completed_appointments;

  public Patients(String full_name, String email_address, int number_of_completed_appointments) {
    this.id = static_id++;
    this.full_name = full_name;
    this.email_address = email_address;
    this.number_of_completed_appointments = number_of_completed_appointments;
  }

  void addPatient(Connection conn) throws SQLException {
    Statement st = conn.createStatement();
    String query = "INSERT INTO Patients VALUES("+
        id+","+
        "\""+full_name+"\""+","+
        "\""+email_address+"\""+","+
        number_of_completed_appointments+");";

    st.executeUpdate(query);

  }

  static void removePatient(Connection conn, int id) throws SQLException {
    Statement st = conn.createStatement();
    st.execute("SET FOREIGN_KEY_CHECKS = 0;");
    String query = "DELETE FROM Patients " +
        "WHERE id="+id+";";
    st.execute(query);
  }

  static void updateInfo(Connection conn, int id, String newName, String newEmail, int newCompletedAppts) throws SQLException {
    Statement st = conn.createStatement();
    String query = "UPDATE Patients " +
        "SET " +
        "full_name=" + "\"" + newName + "\"" + "," +
        "email_address=" + "\"" + newEmail + "\"" + "," +
        "number_of_completed_appointments=" + newCompletedAppts + " " +
        "WHERE id=" + id + ";";
    st.execute(query);
  }

}



