package server;

import java.net.*;
import java.io.*;


public class Server extends Thread { 
	ServerSocket server;
	Socket client;
	int port;

	public Server(int port) {
		this.port = port;      
	}

	public void run() {
		
		new DHCPServer().start();
		new DHCPListener().start();
		
//		try {
//			server = new ServerSocket(port);
//			while (true) {
//				client = server.accept();
//
//				switch (port) {
//				case 4004:
//					new DHCPServer(client).start();
//					new DHCPListener().start();
//					break;
//				default:
//					System.exit(0);
//				}
//			}
//		} catch (IOException e) {
//			System.out.println(e);
//		}
	}

	public static void main(String[] args) {
		new Server(4004).start();
	}

}
