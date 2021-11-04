import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class AddAppointment {
  JFrame frame;
  private JPanel panel1;
  private JButton submitButton;
  private JComboBox dateAndTimeComboBox;
  private JTextField patient_idTextField;

  public AddAppointment() throws SQLException, ParseException {
    frame = new JFrame("Hospital Management");
    frame.setContentPane(panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hospital", "root", "password");


    ArrayList<Availability> availabilites = Appointments.findAllAvailabilities(conn);

    for (int i = 0; i < availabilites.size(); i++) {
      dateAndTimeComboBox.addItem(new Widget(i, availabilites.get(i).toString()));
    }


    submitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        int selectedIndex = dateAndTimeComboBox.getSelectedIndex();
        Availability chosen = availabilites.get(selectedIndex);
        int patientID = Integer.parseInt(patient_idTextField.getText());

        try {
          Appointments app = new Appointments(chosen.date, chosen.order_of_appointment, patientID, chosen.doctorid);
          app.addAppointment(conn);
        } catch (Exception e) {
          e.printStackTrace();
        }

        JOptionPane.showMessageDialog(frame, "Appointment Scheduled!");
        frame.dispose();

      }
    });
  }
}
