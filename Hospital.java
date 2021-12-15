import javax.print.Doc;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Hospital {

  static int maxEntries = 10;

  public static void main(String[] args) throws SQLException, ParseException {
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hospital", "root", HospitalConstants.password);
    _buildTables(conn);



    Patients patient = new Patients("Smith Jack", "cba@cba.moc" , 0);
    Patients patient2 = new Patients("Smith Jack2", "cba2@cba.moc" , 0);

    Doctors doctor = new Doctors("Jack Smith", "abc@abc.com", "internal medicine");
    Doctors doctor2 = new Doctors("Jack Smith2", "abc@abc.com2", "internal medicine2");


    patient.addPatient(conn);
    patient2.addPatient(conn);
    doctor.addDoctor(conn);
    doctor2.addDoctor(conn);

    java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());

    Appointments ap = new Appointments(currentDate, 2,1,1);

    ap.addAppointment(conn);

    ArrayList<Availability> av = Appointments.findAllAvailabilities(conn);

    System.out.println(av);



    //Starts the GUI by showing the Patients
    showTable(conn, "Patients", "");

  }

  static void drop_table(Connection conn, String table) throws SQLException {
    String query = new String("drop table if exists " + table);
    Statement st = conn.createStatement();
    st.execute(query);
  }

  static void _buildTables(Connection conn) throws SQLException {
    Statement st = conn.createStatement();

    st.execute("SET FOREIGN_KEY_CHECKS = 0;");
    drop_table(conn, "Patients");
    drop_table(conn, "Doctors");
    drop_table(conn, "Appointments");
    drop_table(conn, "Prescriptions");
    drop_table(conn, "Bills");
    st.execute("SET FOREIGN_KEY_CHECKS = 1;");


    String patients = new String("create table Patients\n" +
        "(\n" +
        "\tid int not null,\n" +
        "\tfull_name varchar(64) null,\n" +
        "\temail_address varchar(64) null,\n" +
        "\tnumber_of_completed_appointments int default 0 null,\n" +
        "\tconstraint Patients_pk\n" +
        "\t\tprimary key (id)\n" +
        ");");

    String doctors = new String("create table Doctors\n" +
        "(\n" +
        "\tid int not null,\n" +
        "\tfull_name varchar(64) null,\n" +
        "\temail_address varchar(64) null,\n" +
        "\tspecialty varchar(64) null,\n" +
        "\tconstraint Doctors_pk\n" +
        "\t\tprimary key (id)\n" +
        ");");

    String appointments = new String("create table Appointments\n" +
        "(\n" +
        "\tid int not null,\n" +
        "\tdate date null,\n" +
        "\t`order` int null,\n" +
        "\tpatient_id int null,\n" +
        "\tdoctor_id int null,\n" +
        "\tconstraint Appointments_pk\n" +
        "\t\tprimary key (id),\n" +
        "\tconstraint Appointments_Doctors_id_fk\n" +
        "\t\tforeign key (doctor_id) references Doctors (id),\n" +
        "\tconstraint Appointments_Patients_id_fk\n" +
        "\t\tforeign key (patient_id) references Patients (id)\n" +
        ");");

    String prescriptions = new String("create table Prescriptions\n" +
        "(\n" +
        "\tid int not null,\n" +
        "\tname varchar(64) null,\n" +
        "\tprice int null,\n" +
        "\tconstraint Prescriptions_pk\n" +
        "\t\tprimary key (id)\n" +
        ");");
    String bills = new String("create table Bills\n" +
        "(\n" +
        "\tpatient_id int null,\n" +
        "\tprescription_id int null,\n" +
        "\tpaid bool default false null,\n" +
        "\tconstraint fk\n" +
        "\t\tforeign key (patient_id) references Patients (id),\n" +
        "\tconstraint fk2\n" +
        "\t\tforeign key (prescription_id) references Prescriptions (id)\n" +
        ");");

    st.executeUpdate(patients);
    st.executeUpdate(doctors);
    st.executeUpdate(appointments);
    st.executeUpdate(prescriptions);
    st.executeUpdate(bills);
  } /* _initTables() */

  static void showTable(Connection conn, String tableName, String query) throws SQLException {
    // handle "" default query parameter
    if (query.equals("")) {
      query = "select * from " + tableName;
    }

    //create frame
    JFrame frame = new JFrame("Ultimate Hospital Database");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(1000,300);

    //run sql to select a whole table
    Statement statement = conn.createStatement();
    ResultSet resultSet = statement.executeQuery(query);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

    //build out the table that will be inserted into the GUI
    String[] columns = new String[resultSetMetaData.getColumnCount()];

    for (int i = 0; i < columns.length; i++) {
      columns[i] = resultSetMetaData.getColumnName(i+1);
    }

    Object[][] data = new Object[maxEntries][columns.length];

    int numEntries = 0;

    while (resultSet.next()) {
      for (int i = 0; i < columns.length; i++) {
        data[numEntries][i] = resultSet.getString(columns[i]);
      }
      numEntries++;
    }

    //put the table into GUI
    JTable table = new JTable(data, columns);
    JScrollPane scrollPane = new JScrollPane(table);
    table.setFillsViewportHeight(true);

    JLabel heading = new JLabel(tableName + "(" + numEntries + " entries)");

    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(heading, BorderLayout.PAGE_START);
    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

    //add buttons to show all tables
    JButton showPatients = new JButton("Show Patients");
    JButton showDoctors = new JButton("Show Doctors");
    JButton showAppointments = new JButton("Show Appointments");
    JButton showPrescriptions = new JButton("Show Prescriptions");
    JButton showBills = new JButton("Show Bills");

    //implementation of showPatients button
    showPatients.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          frame.dispose();
          showTable(conn, "Patients", "");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    //implementation of showDoctors button
    showDoctors.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          frame.dispose();
          showTable(conn, "Doctors", "");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    //implementation of showAppointments button
    showAppointments.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          frame.dispose();
          showTable(conn, "Appointments", "");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    //implementation of showPrescriptions button
    showPrescriptions.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          frame.dispose();
          showTable(conn, "Prescriptions", "");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    //implementation of showBills button
    showBills.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          frame.dispose();
          showTable(conn, "Bills", "");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    //Panels that buttons and text fields will be place on
    JPanel mainPanel = new JPanel();
    JPanel showPanel = new JPanel();
    JPanel modPanel = new JPanel();
    JPanel fieldsPanel = new JPanel();

    //add buttons that show tables to showPanel
    showPanel.add(showPatients);
    showPanel.add(showDoctors);
    showPanel.add(showAppointments);
    showPanel.add(showPrescriptions);
    showPanel.add(showBills);

    //Buttons to insert or delete from the current table
    JButton insertRow = new JButton("Insert into \"" + tableName + "\"");
    JButton deleteRow = new JButton("Delete from \"" + tableName + "\"");
    JButton searchRow = new JButton("Search \"" + tableName + "\"");

    JTextField[] textBoxes = new JTextField[columns.length];

    //create text fields for each column
    for (int i = 0; i < columns.length; i++) {
      fieldsPanel.add(new JLabel(columns[i] + ":"));
      textBoxes[i] = new JTextField(5);
      fieldsPanel.add(textBoxes[i]);
    }

    //add insert and delete buttons to modPanel
    modPanel.add(insertRow);
    modPanel.add(deleteRow);
    modPanel.add(searchRow);

    //implementation for insertRow button
    insertRow.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          switch (tableName) {
            case "Patients":
              Patients newPatient = new Patients(textBoxes[1].getText(), textBoxes[2].getText(), Integer.parseInt(textBoxes[3].getText()));
              newPatient.addPatient(conn);
              break;
            case "Doctors":
              Doctors newDoctor = new Doctors(textBoxes[1].getText(), textBoxes[2].getText(), textBoxes[3].getText());
              newDoctor.addDoctor(conn);
              break;
            case "Appointments":
              Appointments newAppointment = new Appointments(Date.valueOf(textBoxes[1].getText()), Integer.parseInt(textBoxes[2].getText()), Integer.parseInt(textBoxes[3].getText()), Integer.parseInt(textBoxes[4].getText()));
              newAppointment.addAppointment(conn);
              break;
            case "Prescriptions":
              //TODO Uncomment or adjust when prescriptions class is completed
              //Prescriptions newPrescription = new Prescriptions(textBoxes[1].getText(), Integer.parseInt(textBoxes[2].getText()));
              //newPrescription.addPrescription(conn);
              break;
            case "Bills":
              Bills newBill;
              if (textBoxes[2].getText().equals("T") || textBoxes[2].getText().equals("t") || textBoxes[2].getText().equals("True") || textBoxes[2].getText().equals("true")) {
                newBill = new Bills(Integer.parseInt(textBoxes[0].getText()), Integer.parseInt(textBoxes[1].getText()), true);
              } else {
                newBill = new Bills(Integer.parseInt(textBoxes[0].getText()), Integer.parseInt(textBoxes[1].getText()), false);
              }
              newBill.addBill(conn);
              break;
          }
          frame.dispose();
          showTable(conn, tableName, "");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    //implementation for deleteRow button
    deleteRow.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          switch (tableName) {
            case "Patients":
              Patients.removePatient(conn, Integer.parseInt(textBoxes[0].getText()));
              break;
            case "Doctors":
              Doctors.removeDoctor(conn, Integer.parseInt(textBoxes[0].getText()));
              break;
            case "Appointments":
              Appointments.removeAppointment(conn, Integer.parseInt(textBoxes[0].getText()));
              break;
            case "Prescriptions":
              //TODO Uncomment or adjust when prescriptions class is completed
              //Prescriptions.removePrescriptions(conn, Integer.parseInt(textBoxes[0].getText()));
              break;
            case "Bills":
              Bills.removeBill(conn, Integer.parseInt(textBoxes[0].getText()), Integer.parseInt(textBoxes[1].getText()));
              break;
          }
          frame.dispose();
          showTable(conn, tableName, "");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    //implementation for searchRow button
    searchRow.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          switch (tableName) {
            case "Patients":
              String catStr = "";
              int c = 0;
              if (!textBoxes[0].getText().equals("")) {
                boolean failed = false;
                try {
                  Integer.parseInt(textBoxes[0].getText());
                } catch (NumberFormatException ex) {
                  failed = true;
                  ex.printStackTrace();
                }
                if (!failed) {
                  catStr = "id = " + textBoxes[0].getText();
                  c++;
                }
              }
              if (!textBoxes[1].getText().equals("")) {
                if (c == 0) {
                  catStr = "full_name = \"" + textBoxes[1].getText() + "\"";
                }
                else {
                  catStr += " and full_name = \"" + textBoxes[1].getText() + "\"";
                }
                c++;
              }
              if (!textBoxes[2].getText().equals("")) {
                if (c == 0) {
                  catStr = "email_address = \"" + textBoxes[2].getText() + "\"";
                }
                else {
                  catStr += " and email_address = \"" + textBoxes[2].getText() + "\"";
                }
                c++;
              }
              if (!textBoxes[3].getText().equals("")) {
                boolean failed = false;
                try {
                  Integer.parseInt(textBoxes[3].getText());
                } catch (NumberFormatException ex) {
                  failed = true;
                  ex.printStackTrace();
                }
                if (!failed) {
                  if (c == 0) {
                    catStr = "number_of_completed_appointments = " + textBoxes[3].getText();
                  } else {
                    catStr += " and number_of_completed_appointments = " + textBoxes[3].getText();
                  }
                  c++;
                }
              }
              if (!catStr.equals("")) {
                frame.dispose();
                showTable(conn, tableName, "select * from " + tableName + " where " + catStr);
              }
              break;
            case "Doctors":
              String catStr2 = "";
              int c2 = 0;
              if (!textBoxes[0].getText().equals("")) {
                boolean failed = false;
                try {
                  Integer.parseInt(textBoxes[0].getText());
                } catch (NumberFormatException ex) {
                  failed = true;
                  ex.printStackTrace();
                }
                if (!failed) {
                  catStr2 = "id = " + textBoxes[0].getText();
                  c2++;
                }
              }
              if (!textBoxes[1].getText().equals("")) {
                if (c2 == 0) {
                  catStr2 = "full_name = \"" + textBoxes[1].getText() + "\"";
                }
                else {
                  catStr2 += " and full_name = \"" + textBoxes[1].getText() + "\"";
                }
                c2++;
              }
              if (!textBoxes[2].getText().equals("")) {
                if (c2 == 0) {
                  catStr2 = "email_address = \"" + textBoxes[2].getText() + "\"";
                }
                else {
                  catStr2 += " and email_address = \"" + textBoxes[2].getText() + "\"";
                }
                c2++;
              }
              if (!textBoxes[3].getText().equals("")) {
                if (c2 == 0) {
                  catStr2 = "specialty = \"" + textBoxes[3].getText() + "\"";
                } else {
                  catStr2 += " and specialty = \"" + textBoxes[3].getText() + "\"";
                }
                c2++;
              }
              if (!catStr2.equals("")) {
                frame.dispose();
                showTable(conn, tableName, "select * from " + tableName + " where " + catStr2);
              }
              break;
            case "Appointments":
              String catStr3 = "";
              int c3 = 0;
              if (!textBoxes[0].getText().equals("")) {
                boolean failed = false;
                try {
                  Integer.parseInt(textBoxes[0].getText());
                } catch (NumberFormatException ex) {
                  failed = true;
                  ex.printStackTrace();
                }
                if (!failed) {
                  catStr3 = "id = " + textBoxes[0].getText();
                  c3++;
                }
              }
              if (!textBoxes[1].getText().equals("")) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if (c3 == 0) {
                  catStr3 = "date = \"" + textBoxes[1].getText() + "\"";
                }
                else {
                  catStr3 += " and date = \"" + textBoxes[1].getText() + "\"";
                }
                c3++;
              }
              if (!textBoxes[2].getText().equals("")) {
                boolean failed = false;
                try {
                  Integer.parseInt(textBoxes[2].getText());
                } catch (NumberFormatException ex) {
                  failed = true;
                  ex.printStackTrace();
                }
                if (!failed) {
                  if (c3 == 0) {
                    catStr3 = "Appointments.order = " + textBoxes[2].getText();
                  } else {
                    catStr3 += " and Appointments.order = " + textBoxes[2].getText();
                  }
                  c3++;
                }
              }
              if (!textBoxes[3].getText().equals("")) {
                boolean failed = false;
                try {
                  Integer.parseInt(textBoxes[3].getText());
                } catch (NumberFormatException ex) {
                  failed = true;
                  ex.printStackTrace();
                }
                if (!failed) {
                  if (c3 == 0) {
                    catStr3 = "patient_id = " + textBoxes[3].getText();
                  } else {
                    catStr3 += " and patient_id = " + textBoxes[3].getText();
                  }
                  c3++;
                }
              }
              if (!textBoxes[4].getText().equals("")) {
                boolean failed = false;
                try {
                  Integer.parseInt(textBoxes[4].getText());
                } catch (NumberFormatException ex) {
                  failed = true;
                  ex.printStackTrace();
                }
                if (!failed) {
                  if (c3 == 0) {
                    catStr3 = "doctor_id = " + textBoxes[4].getText();
                  } else {
                    catStr3 += " and doctor_id = " + textBoxes[4].getText();
                  }
                  c3++;
                }
              }
              if (!catStr3.equals("")) {
                System.out.println(catStr3 + " ~~ " + c3);
                frame.dispose();
                showTable(conn, tableName, "select * from " + tableName + " where " + catStr3);
              }
              break;
            case "Prescriptions":
              String catStr4 = "";
              int c4 = 0;
              if (!textBoxes[0].getText().equals("")) {
                boolean failed = false;
                try {
                  Integer.parseInt(textBoxes[0].getText());
                } catch (NumberFormatException ex) {
                  failed = true;
                  ex.printStackTrace();
                }
                if (!failed) {
                  catStr4 = "id = " + textBoxes[0].getText();
                  c4++;
                }
              }
              if (!textBoxes[1].getText().equals("")) {
                if (c4 == 0) {
                  catStr4 = "name = \"" + textBoxes[1].getText() + "\"";
                } else {
                  catStr4 += " and name = \"" + textBoxes[1].getText() + "\"";
                }
                c4++;
              }
              if (!textBoxes[2].getText().equals("")) {
                if (c4 == 0) {
                  catStr4 = "price = \"" + textBoxes[2].getText() + "\"";
                } else {
                  catStr4 += " and price = \"" + textBoxes[2].getText() + "\"";
                }
                c4++;
              }
              if (!catStr4.equals("")) {
                frame.dispose();
                showTable(conn, tableName, "select * from " + tableName + " where " + catStr4);
              }
              break;
            case "Bills":
              String catStr5 = "";
              int c5 = 0;
              if (!textBoxes[0].getText().equals("")) {
                boolean failed = false;
                try {
                  Integer.parseInt(textBoxes[0].getText());
                } catch (NumberFormatException ex) {
                  failed = true;
                  ex.printStackTrace();
                }
                if (!failed) {
                  catStr5 = "patient_id = " + textBoxes[0].getText();
                  c5++;
                }
              }
              if (!textBoxes[1].getText().equals("")) {
                boolean failed = false;
                try {
                  Integer.parseInt(textBoxes[1].getText());
                } catch (NumberFormatException ex) {
                  failed = true;
                  ex.printStackTrace();
                }
                if (!failed) {
                  if (c5 == 0) {
                    catStr5 = "prescription_id = " + textBoxes[1].getText();
                  } else {
                    catStr5 += " and prescription_id = " + textBoxes[1].getText();
                  }
                  c5++;
                }
              }
              if (!textBoxes[2].getText().equals("")) {
                if (c5 == 0) {
                  catStr5 = "paid = " + Boolean.parseBoolean(textBoxes[2].getText());
                } else {
                  catStr5 += " and paid = " + Boolean.parseBoolean(textBoxes[2].getText());
                }
                c5++;
              }
              if (!catStr5.equals("")) {
                frame.dispose();
                showTable(conn, tableName, "select * from " + tableName + " where " + catStr5);
              }
              break;
          }
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    //put the button panels onto the main panel
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(showPanel, BorderLayout.PAGE_START);
    mainPanel.add(modPanel, BorderLayout.PAGE_END);
    mainPanel.add(fieldsPanel, BorderLayout.CENTER);

    //put the main panel onto the frame
    frame.getContentPane().add(mainPanel, BorderLayout.PAGE_END);

    //show GUI
    frame.setVisible(true);
  }


  public static int getNextId(String table) throws SQLException {
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hospital", "root", HospitalConstants.password);
    Statement st = conn.createStatement();

    String query = "SELECT MAX(id) FROM " + table + ";";

    ResultSet rs = st.executeQuery(query);

    int max = 0;

    if (rs.next()) {
      String s = rs.getString(1);

      if (s != null) {
        max = Integer.parseInt(s);
      }

    }
    return max + 1;
  }

}



