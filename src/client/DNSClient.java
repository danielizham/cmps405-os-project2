package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import util.Ports;

/*
    Student 1  : Ali Mohammadian (201807939)
    Student 2  : Mohamed Daniel Bin Mohamed Izham (201802738)
    Course     : CMPS 405 - Operating Systems
    Assessment : Project 2
    Instructor : Heba M. Dawoud
*/

public class DNSClient {
	private DatagramSocket client;
	private Scanner kb;

	public DNSClient() {
		try {
			client = new DatagramSocket();
			kb = new Scanner(System.in);
			System.out.print("domain name: ");
			String message = kb.nextLine();

			byte[] data = message.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), Ports.DNS_PORT);
			client.send(packet);

			byte[] receivedData = new byte[1000];
			DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
			client.receive(receivedPacket);
			System.out.printf("%s\n", new String(receivedPacket.getData(), 0, receivedPacket.getLength()));

		} catch (IOException ioe) {
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		new DNSClient();
	}
}
