import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	private ArrayList<PrintWriter> clientOutputStreams;

	public static void main(String[] args) {
		try {
			new ChatServer().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		clientOutputStreams = new ArrayList<PrintWriter>();
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(5001);
		while (true) {
			Socket clientSocket = serverSock.accept();
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			clientOutputStreams.add(writer);

			Thread t = new Thread(new ClientHandler(clientSocket, writer));
			t.start();
			System.out.println("got a connection");
		}

	}

	private void notifyClients(String message, PrintWriter w) {


		for (PrintWriter writer : clientOutputStreams) {
			if(writer != w) {
				writer.println(">" + message);
				writer.flush();
			}
		}
	}

	class ClientHandler implements Runnable {
		private BufferedReader reader;
		private PrintWriter writer;
		public ClientHandler(Socket clientSocket, PrintWriter writer) throws IOException {
			Socket sock = clientSocket;
			this.writer = writer;
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					notifyClients(message, writer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
