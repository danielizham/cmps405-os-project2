package server;

import java.net.*;

import util.Ports;

import java.io.*;

/*
    Student 1  : Ali Mohammadian (201807939)
    Student 2  : Mohamed Daniel Bin Mohamed Izham (201802738)
    Course     : CMPS 405 - Operating Systems
    Assessment : Project 2
    Instructor : Heba M. Dawoud
*/

public class Server extends Thread {
	DatagramSocket server;
	int port;

	public Server(int port) {
		this.port = port;
	}

	public void run() {
		try {
			server = new DatagramSocket(port);
		} catch (SocketException se) {
			System.exit(1);
		}
		while (true) {
			try {
				byte[] data = new byte[100];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				server.receive(packet);
				System.out.println("received");

				switch (port) {
				case Ports.DHCP_SERVER_PORT:
//					new DHCPServer(server, packet).start();
					break;
				case Ports.DNS_PORT:
					new DNSServer(server, packet).start();
					break;
				default:
					System.exit(0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		new Server(Ports.DHCP_SERVER_PORT).start();
		new Server(Ports.DNS_PORT).start();
	}

}