import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendEMail {

  static void sendEmail(String subject, String body, String recipient) {

    final String username = "ultimateHospitalDatabase@gmail.com";
    final String password = "$ultimateHospitalDatabase^";

    Properties props = new Properties();
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });

    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("ultimateHospitalDatabase@gmail.com"));
      message.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse(recipient));
      message.setSubject(subject);
      message.setText(body);

      Transport.send(message);

      System.out.println("Console: Email Sent!");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}