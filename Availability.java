import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Availability {
  int doctorid;
  String specialty;
  Date date;
  int order_of_appointment;

  public Availability(int doctorid, String specialty, Date date, int order_of_appointment) {
    this.doctorid = doctorid;
    this.specialty = specialty;
    this.date = date;
    this.order_of_appointment = order_of_appointment;
  }

  @Override
  public String toString() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hospital", "root", "password");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    try {
      return Doctors.getDoctorName(conn, doctorid) + ", " + specialty + ", " + date + ",   " + order_of_appointment;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;


        /*
        "doctorid=" + doctorid +
        ", specialty='" + specialty + '\'' +
        ", date=" + date +
        ", order_of_appointment=" + order_of_appointment +
        '}' + '\n';

         */
  }

  @Override
  public boolean equals(Object obj) {
    boolean returnVal = false;

    Availability busyslote = (Availability) obj;

    if (this.doctorid == busyslote.doctorid) {
      if (this.order_of_appointment == busyslote.order_of_appointment) {
        if (this.specialty.equalsIgnoreCase(busyslote.specialty)) {
          if (this.date.equals(busyslote.date)) {
            returnVal = true;
          } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (sdf.format(this.date).equals(sdf.format(busyslote.date))) {
              returnVal = true;
            }
            else {
              returnVal = false;
            }
          }
        } else {
          returnVal = false;
        }
      } else {
        returnVal = false;
      }
    } else {
      returnVal = false;
    }

    return returnVal;
  }

  @Override
  public int hashCode() {
    return Objects.hash(doctorid, specialty, date, order_of_appointment);
  }
}
