import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.sun.tools.javac.util.Constants.format;

public class Appointments {
  int static_id = Hospital.getNextId("Appointments");
  int id;
  Date date;
  int order_of_appointment;
  int patient_id;
  int doctor_id;

  public Appointments(Date date, int order_of_appointment, int patient_id, int doctor_id) throws SQLException {
    this.id = static_id++;
    this.date = date;
    this.order_of_appointment = order_of_appointment;
    this.patient_id = patient_id;
    this.doctor_id = doctor_id;
  }

  void addAppointment(Connection conn) throws SQLException {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Statement st = conn.createStatement();
    String query = "INSERT INTO Appointments VALUES(" +
        id + "," +
        "\"" + df.format(date) + "\"" + "," +
        order_of_appointment + "," +
        patient_id + "," +
        doctor_id + ");";

    System.out.println(query);
    st.executeUpdate(query);

  }

  static void removeAppointment(Connection conn, int id) throws SQLException {
    Statement st = conn.createStatement();
    st.execute("SET FOREIGN_KEY_CHECKS = 0;");
    String query = "DELETE FROM Appointments " +
        "WHERE id=" + id + ";";
    st.execute(query);
  }

  static void updateInfo(Connection conn, int id, Date newDate, int newOrder, int newPatientid, int newDoctorid) throws SQLException {
    Statement st = conn.createStatement();
    String query = "UPDATE Appointments " +
        "SET " +
        "date=" + "\"" + format(newDate) + "\"" + "," +
        "order_of_appointment=" + newOrder + "," +
        "patient_id=" + newPatientid + "," +
        "doctor_id=" + newDoctorid + " " +
        "WHERE id=" + id + ";";
    st.execute(query);
  }

  static ArrayList<Availability> findAllAvailabilities(Connection conn) throws SQLException, ParseException {
    ArrayList<Availability> busy = new ArrayList<>();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    Statement st = conn.createStatement();
    String query = "SELECT * FROM Appointments";

    ResultSet rs = st.executeQuery(query);

    while (rs.next()) {
      java.util.Date d =  df.parse(rs.getString(2));

      java.sql.Date busyDate = new Date(d.getTime());
      int order_of_appointment = Integer.parseInt(rs.getString(3));
      int doctorid = Integer.parseInt(rs.getString(4));
      String specialty = Doctors.getDoctorSpecialty(conn, doctorid);

      Availability availability = new Availability(doctorid, specialty, busyDate, order_of_appointment);
      busy.add(availability);
    }

    ArrayList<Integer> doctors = new ArrayList<>();

    Statement st2 = conn.createStatement();
    String query2 = "SELECT id FROM Doctors";
    ResultSet rs2 = st2.executeQuery(query2);

    while (rs2.next()) {
      doctors.add(Integer.parseInt(rs2.getString(1)));
    }


    ArrayList<Availability> allTimes = new ArrayList<>();
    java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    for (int i = 0; i < doctors.size(); i++) { //for every doctor
      int did = doctors.get(i);
      for (int j = 0; j < 3; j++) {  //for everyday including today up to 2 days later
        if (j != 0) {
          currentDate = addToDateTime(currentDate, Increment.DAILY);
        }
        for (int k = 1; k <= 10; k++) {
          Availability av = new Availability(did, Doctors.getDoctorSpecialty(conn, did),currentDate, k);
          allTimes.add(av);
        }
      }
    }

    System.out.println(busy);
    System.out.println();
    System.out.println();
    System.out.println();
    allTimes.removeAll(busy);

    return allTimes;

  } /* findAllAvailabilities */

  public static java.sql.Date addToDateTime(java.sql.Date date, Increment increment) throws ParseException {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.setTime(date);

    switch (increment) {
      case HOURLY: calendar.add(Calendar.HOUR, 1); break;
      case DAILY: calendar.add(Calendar.DATE, 1); break;
      case WEEKLY: calendar.add(Calendar.WEEK_OF_YEAR, 1); break;
      case DO_NOT_POLL: break;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    java.util.Date d = sdf.parse(sdf.format(calendar.getTime()));
    return new java.sql.Date(d.getTime());
  }

  public enum Increment {
    HOURLY, DAILY, WEEKLY, DO_NOT_POLL;
  }

  public static void main(String[] args) {

  }


}
