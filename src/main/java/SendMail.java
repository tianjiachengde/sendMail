import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
    //邮件标题
    private static final String subject = "Happy birthday!";
    //发件人
    private static final String mailFrom = "";
    private static final String password = "";
    private static final String host = "smtp.qq.com";
    //文件路径
    private static final String File_Path = "employee_records.text";

    public static void main(String[] args) {
        ArrayList<String> strings = readFile();
        judgeDateHandler(strings);
    }

    private static ArrayList<String> readFile() {
        File file = new File(File_Path);
        ArrayList<String> list = new ArrayList<String>();
        try {
            InputStreamReader input = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(input);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                list.add(str);
            }
            bufferedReader.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 判断符合发送邮件的信息并发送邮件
     *
     * @param strings 信息数组
     */
    private static void judgeDateHandler(ArrayList<String> strings) {
        int len = strings.size();
        int i = 1;
        while (i < len) {
            String str = strings.get(i);
            String[] arr = str.split(",");
            String[] dateArr = arr[2].split("/");
            if ((dateArr[1] + dateArr[2]).equals(curDate())) {
                //邮件内容
                String content = "Happy birthday, dear " + arr[0] + "!";
                try {
                    sendingMail(arr[3], content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
    }

    /**
     * 获取当前日期
     *
     * @return string
     */
    private static String curDate() {
        Calendar cal = Calendar.getInstance();
        int m = cal.get(Calendar.MONTH) + 1;
        String date = "";
        if (m < 10) {
            date += "0" + m;
        } else {
            date += String.valueOf(m);
        }
        int d = cal.get(Calendar.DATE);
        if (d < 10) {
            date += "0" + d;
        } else {
            date += String.valueOf(d);
        }
        return date;
    }

    /**
     * 发送邮件
     *
     * @param mailto  收件人
     * @param content 邮件内容
     * @throws Exception exception
     */
    private static void sendingMail(String mailto, String content) throws Exception {
        Properties prop = new Properties();
        prop.setProperty("mail.host", host);
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(prop);
        session.setDebug(true);
        Transport ts = session.getTransport();
        ts.connect(host, mailFrom, password);
        Message message = createSimpleMail(session, mailto, content);
        ts.sendMessage(message, message.getAllRecipients());
        System.out.print("sendSuccess");
        ts.close();
    }

    /**
     * 创建邮件
     *
     * @param session session
     * @param mailto  收件人
     * @param content 邮件内容
     * @return message
     * @throws Exception exception
     */
    private static MimeMessage createSimpleMail(Session session, String mailto, String content)
            throws Exception {
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(mailFrom));
        //指明邮件的收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailto));
        //邮件的标题
        message.setSubject(subject);
        //邮件的文本内容
        message.setContent(content, "text/html;charset=UTF-8");
        return message;
    }
}
