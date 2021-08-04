import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class App {
    private HttpURLConnection APIconnection;

    public static void main(String[] args) {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        App app = new App();
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JLabel passwordLabel = new JLabel("Sudo Password");
        passwordLabel.setBounds(10, 20, 120, 25);
        panel.add(passwordLabel);

        JTextField passwordText = new JTextField();
        passwordText.setBounds(125, 20, 165, 25);
        panel.add(passwordText);

        JLabel authKey = new JLabel("Auth Key");
        authKey.setBounds(10, 50, 120, 25);
        panel.add(authKey);

        JTextField authKeyText = new JTextField();
        authKeyText.setBounds(125, 50, 165, 25);
        panel.add(authKeyText);

        JLabel networkInterface = new JLabel("Network Interface Name (e.g. eth0)");
        networkInterface.setBounds(10, 80, 250, 25);
        panel.add(networkInterface);

        JTextField networkInterfaceText = new JTextField();
        networkInterfaceText.setBounds(265, 80, 165, 25);
        panel.add(networkInterfaceText);

        JButton buttonStart = new JButton("Start Capture");
        buttonStart.setBounds(200, 200, 300, 25);
        buttonStart.addActionListener(actionEvent -> {
            try {
                if (app.checkToken(authKeyText.getText()) && !(passwordText.getText().equals(""))) {
                    try {
                        String[] cmd = {"bash", "-c", "echo " + passwordText.getText() + "| sudo -S screen -d -m ettercap -T -S -i "+ networkInterfaceText.getText() +" -M arp:remote"};
                        Process pb = Runtime.getRuntime().exec(cmd);

                        String line;
                        BufferedReader input = new BufferedReader(new InputStreamReader(pb.getInputStream()));
                        while ((line = input.readLine()) != null) {
                            System.out.println(line);
                        }
                        input.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        String[] cmd = {"bash", "-c", "echo " + passwordText.getText() + "| sudo -S screen -d -m "+ networkInterfaceText.getText() +" -i enp10s0"};
                        Process pb = Runtime.getRuntime().exec(cmd);

                        String line;
                        BufferedReader input = new BufferedReader(new InputStreamReader(pb.getInputStream()));
                        while ((line = input.readLine()) != null) {
                            System.out.println(line);
                        }
                        input.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Incorrect Auth Key or Password");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        panel.add(buttonStart);

        JButton buttonStop = new JButton("Stop Capture");
        buttonStop.setBounds(200, 230, 300, 25);
        buttonStop.addActionListener(actionEvent -> {
            try {
                String[] cmd = {"bash", "-c", "echo " + passwordText.getText() + "| sudo -S pkill screen"};
                Process pb = Runtime.getRuntime().exec(cmd);

                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(pb.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        panel.add(buttonStop);

        frame.setVisible(true);
    }

    private boolean checkToken(String token1) throws IOException {
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        URL url = new URL("https://the-token-master.glitch.me/getDreams");
        APIconnection = (HttpURLConnection) url.openConnection();

        APIconnection.setRequestMethod("GET");
        APIconnection.setConnectTimeout(5000);
        APIconnection.setReadTimeout(5000);

        int status = APIconnection.getResponseCode();
        System.out.println(status);

        if (status > 299) {
            reader = new BufferedReader(new InputStreamReader(APIconnection.getErrorStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
        } else {
            reader = new BufferedReader(new InputStreamReader(APIconnection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
        }

        JsonParser jp = new JsonParser();
        JsonArray dreams = (JsonArray) jp.parse(responseContent.toString());
        System.out.println(token1);
        for (int i = 0; i < dreams.size(); i++) {
            JsonObject dream = (JsonObject) dreams.get(i);
            String foo = dream.get("dream").toString();
            String token = foo.substring(1, foo.length() - 1);
            System.out.println(token);
            if (token.equals(token1)) {
                System.out.println("correct token");
                return true;
            }

        }
        APIconnection.disconnect();
        System.out.println("incorrect token");
        return false;


    }
}
