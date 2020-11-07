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

		if (port == Ports.DHCP_SERVER_PORT)
			new DHCPServer().start();

	}

	public void run() {
		try {
			server = new DatagramSocket(port);
		} catch (SocketException se) {
			System.exit(1);
		}

		while (true) {
			try {
				byte[] payload = new byte[100];
				DatagramPacket rPacket = new DatagramPacket(payload, payload.length);
				server.receive(rPacket);

				switch (port) {
				case Ports.DHCP_SERVER_PORT:

					String request = new String(rPacket.getData(), 0, rPacket.getLength());

					switch (request) {

					case DHCPServer.DHCP_DISCOVER:
						DHCPServer.handleDHCPDiscover(server, rPacket);
						break;
					case DHCPServer.DHCP_REQUEST:
						DHCPServer.handleDHCPRequest(server);
						break;
					default:
						break;
					}

					break;
				case Ports.DNS_PORT:
					System.out.println("DNS request recieved");
					new DNSServer(server, rPacket).start();
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