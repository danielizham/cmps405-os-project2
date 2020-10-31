package client;

import java.net.*;
import java.util.Formatter;
import java.util.Scanner;

import model.DHCPPacket;

import java.io.*;


public class Client {
	Socket server;
	int port;
	Formatter toNet = null;
	Scanner fromNet = null;
	Scanner fromUser = new Scanner(System.in);
	final String DHCP_DISCOVER = "DHCP DISCOVER";
	final String DHCP_REQUEST = "DHCP REQUEST";
	final String DHCP_ACK = "DHCP ACK";

	

	public Client() {
		try {
			
			// connect to the server 
			server = new Socket("localhost", 4004);
			fromNet = new Scanner(server.getInputStream());
			toNet = new Formatter(server.getOutputStream());
	        ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
			
	        // send DHCP DISCOVER 
			toNet.format("%s\n", DHCP_DISCOVER);
			toNet.flush();
			
			// receive the DHCP packet
			DHCPPacket rPacket = (DHCPPacket) objectInputStream.readObject();
			
			System.out.println("The DHCP Packet was recieved! \n");
			System.out.printf("%-25s %s\n", "IP Address: ", rPacket.getIp());
			System.out.printf("%-25s %s\n", "Subnet Mask: ", rPacket.getMask());
			System.out.printf("%-25s %s\n", "Default Gateway: ", rPacket.getGateway());
			System.out.printf("%-25s %s\n", "DNS IP Address: ", rPacket.getDnsIP()[0]);
			System.out.printf("%-25s %s\n", "DNS IP Address: ", rPacket.getDnsIP()[1]);
			
			// send DHCP REQUEST and the chosen IP
			toNet.format("%s\n", DHCP_REQUEST);
			toNet.flush();
			toNet.format("%s\n", rPacket.getIp());
			toNet.flush();
			
			
			// wait for DHCP acknowledgment
			String response = fromNet.nextLine();

			if (response.equals(DHCP_ACK)) {
				System.out.println("DHCP Acknowledgment recieved!");
			}
			
			
		} catch (IOException ioe) {
			System.out.println(ioe);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client();
	}
}
