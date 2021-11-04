import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;

public class AppointmentsGui {
  private JFrame frame;
  private JPanel panel1;
  private JButton addAppointmentButton;
  private JButton removeAppointmentButton;
  private JButton updateAppointmentButton;
  private JButton concludeAppointmentButton;

  public AppointmentsGui() {

    frame = new JFrame("Hospital Management");
    frame.setContentPane(panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);


    addAppointmentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        frame.dispose();
        try {
          AddAppointment addAppointment = new AddAppointment();
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    });
    removeAppointmentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        frame.dispose();
        //RemoveAppointment removeAppointment = new RemoveAppointment();
      }
    });
    updateAppointmentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        frame.dispose();
        //UpdateAppointment updateAppointment = new UpdatteAppointment();
      }
    });
  }
}
