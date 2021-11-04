import javax.print.Doc;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui {
  public JFrame frame;
  private JPanel panel1;
  private JButton doctorsButton;
  private JButton patientsButton;
  private JButton appointmentsButton;
  private JButton sendAnEmailReminderButton;
  private JButton concludeAppointmentButton;

  public Gui() {

    doctorsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {

        frame.dispose();
        DoctorsGui doctorsGui = new DoctorsGui();

      }
    });

    appointmentsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        frame.dispose();
        AppointmentsGui appointmentsGui = new AppointmentsGui();
      }
    });



    frame = new JFrame("Hospital Management");
    frame.setContentPane(panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    }

  public static void main(String[] args) {
    Gui gui = new Gui();
  }


}

