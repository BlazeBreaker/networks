import java.io.*;
import java.net.ServerSocket;
import java.time.format.DateTimeFormatter;
import java.net.*;
import java.time.LocalDateTime;

public class ServerFinal{

    public static String DisplayTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formatDateTime = now.format(dtf);
        return formatDateTime;
    }

    public static String process(String input) {
        String fs = "";
        String s;
        try {
            Process process = Runtime.getRuntime().exec(input);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((s = br.readLine()) != null)
                fs = fs.concat(s + "\n");
            process.waitFor();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return fs;
    }
    
    public static void main(String[] args) throws Exception{

        ServerSocket newServer = new ServerSocket(5000);
        System.out.println("Server Started.");
        
        while (true)
        {
            Socket s = null;
            try
            {
                s = newServer.accept();
                System.out.println("A new client is connected : " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");
                Thread t = new Thread(new ClientHandler(s, dis, dos));
                t.start();
            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}
    class ClientHandler extends Thread {
        public InputStream dis = null;
        public OutputStream dos = null;
        public Socket s = null;

        public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
            this.s = s;
            this.dis = dis;
            this.dos = dos;
        }

        public void run() {
            String received;
            String toreturn;
            String input = " ";

            try {

                dis = s.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(dis));
                dos = s.getOutputStream();
                PrintWriter writer = new PrintWriter(dos, true);

                switch (received = reader.readLine()) {
                    case "1":
                        writer.println("Server returns: \n" + ServerFinal.DisplayTime() + "\nend");
                        break;

                    case "2":
                        input = "uptime -p";
                        writer.println("Server returns: \n" + ServerFinal.process(input) + "end");
                        break;

                    case "3":
                        input = "free";
                        writer.println("Server returns: \n" + ServerFinal.process(input) + "end");
                        break;

                    case "4":
                        input = "netstat";
                        writer.println("Server returns: \n" + ServerFinal.process(input) + "end");
                        break;

                    case "5":
                        input = "who";
                        writer.println("Server returns: \n" + ServerFinal.process(input) + "end");
                        break;

                    case "6":
                        input = "ps -e";
                        writer.println("Server returns: \n" + ServerFinal.process(input) + "end");
                        break;
                        
                    case "7":
                        writer.println("Client " + this.s + " sends exit...");
                        this.s.close();
                        break;

                    default:
                        writer.println(("That was invalid input. The options are:\n" +
                                "==================================================\n" +
                                "Select a server statistic to display (Input 1-7):\n" +
                                "1.     Server current Date and Time\n" +
                                "2.     Server uptime\n" +
                                "3.     Memory use\n" +
                                "4.     Server NetStats\n" +
                                "5.     Current users on the server\n" +
                                "6.     Running processes on the server\n" +
                                "7.     Quit\n" +
                                "=================================================="));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                this.dis.close();
                this.dos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

 }
