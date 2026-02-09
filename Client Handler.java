import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Handler Error: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        String message;

        try {
            while ((message = reader.readLine()) != null) {
                System.out.println("Received: " + message);
                broadcastMessage(message);
            }
        } catch (Exception e) {
            System.out.println("Client disconnected");
        } finally {
            closeConnection();
        }
    }

    private void broadcastMessage(String message) {
        for (ClientHandler client : ChatServer.clients) {
            client.writer.println(message);
        }
    }

    private void closeConnection() {
        try {
            ChatServer.clients.remove(this);
            socket.close();
        } catch (Exception e) {
            System.out.println("Error closing connection");
        }
    }
}
