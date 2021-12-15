import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AddDoctor {
  private JPanel panel1;
  private JTextField fullNameTextField;
  private JTextField emailAddressTextField;
  private JTextField specialtyTextField;
  private JButton submitButton;
  private JFrame frame;

  public AddDoctor() {

    frame = new JFrame("Hospital Management");
    frame.setContentPane(panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);


    submitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        String first_name = fullNameTextField.getText();
        String email_address = emailAddressTextField.getText();
        String specialty = specialtyTextField.getText();

        Doctors newDoctor = new Doctors(first_name, email_address, specialty);

        Connection conn = null;
        try {
          conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hospital", "root", HospitalConstants.password);
          newDoctor.addDoctor(conn);
        } catch (SQLException e) {
          e.printStackTrace();
        }

        JOptionPane.showMessageDialog(frame, "Doctor added!");
        frame.dispose();
      }
    });
  }
}
