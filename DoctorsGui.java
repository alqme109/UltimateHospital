import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoctorsGui {
  public JPanel panel1;
  private JButton addDoctorButton;
  private JButton removeDoctorButton;
  private JButton updateDoctorButton;
  private JFrame frame;

  public DoctorsGui() {
    frame = new JFrame("Hospital Management");
    frame.setContentPane(panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    addDoctorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        frame.dispose();
        AddDoctor addDoctor = new AddDoctor();
      }
    });


    removeDoctorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        frame.dispose();
        RemoveDoctor removeDoctor = new RemoveDoctor();
      }
    });

    updateDoctorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        frame.dispose();
        UpdateDoctor updateDoctor = new UpdateDoctor();
      }
    });
  }
}
