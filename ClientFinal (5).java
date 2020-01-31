import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class ClientFinal extends Thread {

    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private DataInputStream server = null;
    public static int i;
    public static long currentTime;
    public static long endTime;
    public static int intial;
    private static int num;
    private static String command;

    public ClientFinal( int i) {
        this.i = i;
    }

    public static void displayMenu()
    {
        System.out.println("==================================================\n");
        System.out.println("\tSelect a server statistic to display (Input 1-7):\n");
        System.out.println("\t\t1. Server current Date and Time\n");
        System.out.println("\t\t2. Server uptime\n");
        System.out.println("\t\t3. Memory use\n");
        System.out.println("\t\t4. Server NetStats\n");
        System.out.println("\t\t5. Current users on the server\n");
        System.out.println("\t\t6. Running processes on the server\n");
        System.out.println("\t\t7. Quit\n");
        System.out.println("==================================================\n\n");

    }

    public void run() {
        try {

            socket = new Socket("192.168.101.105", 5000);
            OutputStream out = socket.getOutputStream();
            currentTime = System.currentTimeMillis();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(command);
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String serverData = null;
            while ((serverData = reader.readLine()) != null) {
                System.out.println(serverData);
                endTime = System.currentTimeMillis();  
                if (serverData.equals("end")) {
                    System.out.println("ELAPSE TIME: " + (endTime - currentTime));
                    try {
                        in.close();
                        out.close();
                        writer.close();
                        reader.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\tEnding program\n");
                    System.out.println("\tDisconnecting from the server...\n");
                    break;
                }
            }
        } catch (UnknownHostException u) {
            System.out.println(u.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String args[])
    {
        num = 1;
        Scanner sc = new Scanner (System.in);
        String IP = "";
        while (!IP.equals("192.168.101.105"))
        {
            System.out.println("Enter IP: ");
            IP= sc.nextLine();
            if (IP.equals(""))
            {
                System.exit(0);
            }
        }

        System.out.println("How many Client?");
        num = sc.nextInt();
        displayMenu();
        sc.nextLine();

        intial = 1;
        command = String.valueOf(intial);
        //command = sc.nextLine();
        while (!command.equals("7"))
        {
            Thread[] allClient = new Thread[num];
            for (int i = 0; i< num; i++)
            {               
                allClient[i] = new Thread (new ClientFinal(i));
            }
            for (int j = 0; j < num; j++)
            {
                System.out.println("Connected");
                allClient[j].start();
            }
            for (int k = 0; k < num; k++)
            {
                try
                {
                    allClient[k].join();
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }

            if(intial < 6) {
                intial++;
                command = String.valueOf(intial);
            } else {
                displayMenu();
                command = sc.nextLine();
            }

        }

        try {
            System.exit(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
}
