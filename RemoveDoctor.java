import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RemoveDoctor {
  private JPanel panel1;
  private JTextField doctorID;
  private JButton submitButton;
  private JFrame frame;

  public RemoveDoctor() {

    frame = new JFrame("Hospital Management");
    frame.setContentPane(panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
    submitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        int id = Integer.parseInt(doctorID.getText());

        Connection conn = null;
        try {
          conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hospital", "root", "password");
          Doctors.removeDoctor(conn, id);
        } catch (SQLException e) {
          e.printStackTrace();
        }

        JOptionPane.showMessageDialog(frame, "Doctor removed if ID exists!");
        frame.dispose();
      }
    });
  }
}
