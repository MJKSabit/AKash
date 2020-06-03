package github.mjksabit.akash.app.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class ResponseListener implements Runnable {
    private BufferedReader in = null;
    private ServerConnect source = null;
    private boolean continueListening;

    public ResponseListener(ServerConnect source, BufferedReader in) {
        this.source = source;
        this.in = in;
    }

    public void start() {
        continueListening = true;
        new Thread(this).start();
    }

    public void stop() {
        continueListening = false;
    }

    @Override
    public void run() {
        while (continueListening) {
            try {
                String response = in.readLine();
                source.handleResponse(response);
            } catch (SocketTimeoutException e) {
                continueListening = false;
                System.out.println("Server timeout...");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
