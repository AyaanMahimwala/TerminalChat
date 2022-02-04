import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientReady {
	private BufferedReader reader;
	private PrintWriter writer;
	private String user;
	

	public void run() throws Exception {
		getUser();
		setUpNetworking();
		sendMessage();
	}
	
	private void getUser() {
		System.out.println("Identify yourself: ");
		user = new Scanner(System.in).nextLine();
	}


	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("54.215.234.184", 5001); //put address and port of EC2 server 
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}

	
	private void sendMessage() {
		Scanner scanner = new Scanner(System.in);
		while(true) {
			String message = scanner.nextLine();
			writer.println(user + ": " + message);
			writer.flush();
		}
	}

	public static void main(String[] args) {
		try {
			new ClientReady().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					
						System.out.println(message);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
