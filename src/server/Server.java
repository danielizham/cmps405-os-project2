package server;

import java.net.*;
import java.io.*;

// Name       : Mohamed Daniel Bin Mohamed Izham
// QUID       : 201802738
// Course     : CMPS 405 - Operating Systems
// Assessment : Homework 2
// Instructor : Mohammad Saleh Mustafa Saleh

public class Server extends Thread { // multiple services each is a thread on different ports
	ServerSocket server;
	Socket client;
	int port;

	public Server(int port) {
		this.port = port;      
	}

	public void run() {
		try {
			server = new ServerSocket(port);
			while (true) {
				client = server.accept();

				switch (port) {
				case 4000:
					new ServiceServer0(client).start();
					break;
				case 4001:
					new ServiceServer1(client).start();
					break;
				default:
					System.exit(0);
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		new Server(4000).start();
		new Server(4001).start();
	}

}
