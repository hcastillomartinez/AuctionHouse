package Agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestBank implements Runnable {

    private BufferedReader in, stdIn;
    private PrintWriter out;
    private Socket client;

    public TestBank(Socket client) {
        try {
            this.client = client;
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);

        ServerSocket server = new ServerSocket(portNumber);

        while (true) {
            Socket client = server.accept();
            TestBank bank = new TestBank(client);
            (new Thread(bank)).start();
        }
    }

    /**
     * Function to close the client from the server.
     */
    private void closeClient() {
        try {
            out.println("Server " + client.getLocalAddress() + " has closed.");
            in.close();
            out.close();
            stdIn.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Override
    public void run() {
        String output, input = null;

        try {
            do {
                output = stdIn.readLine();
                out.println(output);
                System.out.println("Server output: " + output);

                if (output.equalsIgnoreCase("bye")) {
                    input = null;
                } else {
                    input = in.readLine();
                    System.out.println("Client input: " + input);
                }
            } while (input != null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            closeClient();
        }
    }
}
