import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UpdateDoctor {
  private JPanel panel1;
  private JButton submitButton;
  private JTextField idTextField;
  private JTextField nameTextField;
  private JTextField emailTextField;
  private JTextField specialtyTextField;
  private JFrame frame;

  public UpdateDoctor() {

    frame = new JFrame("Hospital Management");
    frame.setContentPane(panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    submitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        int id = Integer.parseInt(idTextField.getText());
        String first_name = nameTextField.getText();
        String email_address = emailTextField.getText();
        String specialty = specialtyTextField.getText();


        Connection conn = null;
        try {
          conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hospital", "root", "password");
          Doctors.updateInfo(conn, id, first_name,email_address,specialty);
        } catch (SQLException e) {
          e.printStackTrace();
        }

        JOptionPane.showMessageDialog(frame, "Doctor updated!");
        frame.dispose();
      }
    });
  }
}
