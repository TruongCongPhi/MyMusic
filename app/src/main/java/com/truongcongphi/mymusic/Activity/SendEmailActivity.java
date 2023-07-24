package com.truongcongphi.mymusic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.truongcongphi.mymusic.Class.LoadingDialog;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.User;
import com.truongcongphi.mymusic.R;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendEmailActivity extends AppCompatActivity {
    EditText edtTieuDe, edtNoiDung;
    Button btnSend;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        sessionManager = new SessionManager(this);
        edtNoiDung= findViewById(R.id.edt_noidung);
        edtTieuDe =findViewById(R.id.edt_tieude);
        btnSend = findViewById(R.id.btn_send);
        loadingDialog = new LoadingDialog(this);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    final String fromEmail = "tcp02012003@gmail.com"; // Thay thế bằng địa chỉ email của bạn
//                    final String password = "hqgzicjtjvlkcjxs"; // Thay thế bằng mật khẩu email của bạn
//                    final String toEmail = "congphi02012003@gmail.com"; // Thay thế bằng địa chỉ email của người nhận
//                    final String subject = edtTieuDe.getText().toString();
//                    final String body = edtNoiDung.getText().toString();
//                    String host ="smtp.gmail.com";
//
//                    // Cấu hình các thuộc tính SMTP
//                    Properties props = System.getProperties();
//                    props.put("mail.smtp.host", host); // Thay thế bằng địa chỉ máy chủ SMTP, ví dụ: smtp.gmail.com
//                    props.put("mail.smtp.port", "465"); // Thay thế bằng cổng SMTP, ví dụ: 587
//                    props.put("mail.smtp.auth", "true");
//                    props.put("mail.smtp.starttls.enable", "true");
//
//                    // Tạo phiên làm việc với xác thực
//                    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//                        protected PasswordAuthentication getPasswordAuthentication() {
//                            return new PasswordAuthentication(fromEmail, password);
//                        }
//                    });
//
//                    // Tạo thông điệp email
//                    MimeMessage message = new MimeMessage(session);
//                    message.setFrom(new InternetAddress(fromEmail));
//                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
//                    message.setSubject(subject);
//                    message.setText(body);
//
//                    // Gửi email
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Transport.send(message);
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // Email gửi thành công
//                                        // Bạn có thể hiển thị Toast hoặc xử lý thành công tùy ý
//                                        Toast.makeText(SendEmailActivity.this,"Đã gửi thành công",Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            } catch (final MessagingException e) {
//                                e.printStackTrace();
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // Xử lý lỗi ở đây (ví dụ: hiển thị thông báo lỗi)
//                                        Toast.makeText(SendEmailActivity.this,"Không thể gửi",Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//                            }
//                        }
//                    }).start();
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
                loadingDialog.showDialog();
                try {
                    String SenderEmail = "tcp02012003@gmail.com";
                    String ReceiverEmail = "congphi02012003@gmail.com";
                    String PasswordSenderEmail = "hqgzicjtjvlkcjxs";
                    String subject = edtTieuDe.getText().toString();
                    String content = edtNoiDung.getText().toString();

                    String Host ="smtp.gmail.com";

                    Properties properties = System.getProperties();

                    properties.put("mail.smtp.host",  Host);
                    properties.put("mail.smtp.port", "465");
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.auth", "true");

                    javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(SenderEmail, PasswordSenderEmail);
                        }
                    });

                    MimeMessage mimeMessage = new MimeMessage(session);
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(ReceiverEmail));

                    mimeMessage.setSubject(subject);
                    mimeMessage.setText(content);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Transport.send(mimeMessage);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Email gửi thành công
                                        // Bạn có thể hiển thị Toast hoặc xử lý thành công tùy ý
                                        Toast.makeText(SendEmailActivity.this,"Đã gửi thành công",Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismissDialog();
                                    }
                                });
                            } catch (MessagingException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Xử lý lỗi ở đây (ví dụ: hiển thị thông báo lỗi)
                                        Toast.makeText(SendEmailActivity.this,"Không thể gửi",Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismissDialog();
                                    }
                                });

                            }
                        }

                    });
                    thread.start();

                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}


