package client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import model.DHCPPacket;
import util.Ports;

/*
    Student 1  : Ali Mohammadian (201807939)
    Student 2  : Mohamed Daniel Bin Mohamed Izham (201802738)
    Course     : CMPS 405 - Operating Systems
    Assessment : Project 2
    Instructor : Heba M. Dawoud
*/

public class Client {

	private boolean willRequestDHCP = true;
	private DatagramSocket client;
	private ClientPacketReceiver clientPacketReceiver;
	private String myIP = "";
	private int myPort;
	boolean isRequestingDNS = false;

	private Client() throws IOException, InterruptedException {
		client = new DatagramSocket();
		System.out.println("Client\t: I have just connected to a network!");
		
		if (willRequestDHCP) {
			boolean success = false;
			while (!success) 
				success = requestDHCP();
		}
		
		clientPacketReceiver = new ClientPacketReceiver(client, this);
		clientPacketReceiver.start();
		
		requestDNS();
		
		if (clientPacketReceiver != null)
			clientPacketReceiver.join();
		
		if (!myIP.equals(""))
			System.out.printf("Client\t: Client with IP address %s at port %d has terminated.%n", myIP, myPort);
		else
			System.out.println("Client\t: Client without an IP address has terminated.");
	}

	boolean requestDHCP() {
		final String DHCP_DISCOVER = "DHCP DISCOVER";
		final String DHCP_REQUEST = "DHCP REQUEST";
		final String DHCP_ACK = "DHCP ACK";

		try {
			client.setSoTimeout(1000); // wait for DHCP replies for up to 1 second
			
			System.out.println("Client\t: Making a DHCP request...");
			// send DHCP DISCOVER
			String request = DHCP_DISCOVER;
			byte[] sdata = request.getBytes();
			DatagramPacket spacket = new DatagramPacket(sdata, sdata.length, InetAddress.getLocalHost(),
					Ports.DHCP_SERVER_PORT);
			client.send(spacket);

			// receive the DHCP packet
			byte[] rdata = new byte[1000];
			DatagramPacket rpacket = new DatagramPacket(rdata, rdata.length);
			client.receive(rpacket);

			ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(rpacket.getData()));
			DHCPPacket dhcpPacket = (DHCPPacket) iStream.readObject();

			iStream.close();

			System.out.println("Client\t: The DHCP Packet was received!");
			System.out.printf("\t  %-25s %s\n", "IP Address: ", dhcpPacket.getIp());
			System.out.printf("\t  %-25s %s\n", "Subnet Mask: ", dhcpPacket.getMask());
			System.out.printf("\t  %-25s %s\n", "Default Gateway: ", dhcpPacket.getGateway());
			System.out.printf("\t  %-25s %s\n", "DNS IP Address: ", dhcpPacket.getDnsIP()[0]);
			System.out.printf("\t  %-25s %s\n", "DNS IP Address: ", dhcpPacket.getDnsIP()[1]);

			myIP = dhcpPacket.getIp();
			myPort = dhcpPacket.getPort();

			// send DHCP REQUEST and the chosen IP
			request = DHCP_REQUEST;
			sdata = request.getBytes();
			spacket = new DatagramPacket(sdata, sdata.length, InetAddress.getLocalHost(), Ports.DHCP_SERVER_PORT);
			client.send(spacket);

			request = dhcpPacket.getIp();
			sdata = request.getBytes();
			spacket = new DatagramPacket(sdata, sdata.length, InetAddress.getLocalHost(), Ports.DHCP_SERVER_PORT);
			client.send(spacket);

			// wait for DHCP acknowledgement
			rdata = new byte[1000];
			rpacket = new DatagramPacket(rdata, rdata.length);
			client.receive(rpacket);
			String response = new String(rpacket.getData(), 0, rpacket.getLength());

			if (response.equals(DHCP_ACK)) {
				System.out.println("Client\t: DHCP Acknowledgment received!");
			}
			
			client.setSoTimeout(0); // disable socket timeout
			return true;

		} catch (IOException | ClassNotFoundException e) {
			return false; // if there is a problem with DHCP request, try again
		}
	}

	private void requestDNS() {
		isRequestingDNS = true;
		Scanner kb = null;
		try {
			kb = new Scanner(System.in);
			System.out.print("Client\t: Enter a domain name to be translated: ");
			String message = kb.nextLine();
			isRequestingDNS = false;

			byte[] data = message.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), Ports.DNS_PORT);
			client.send(packet);
		} catch (IOException ioe) {
			System.exit(1);
		} finally {
			if (kb != null)
				kb.close();
		}
	}

	public static void main(String[] args) {
		try {
			new Client();
		} catch (IOException | InterruptedException e) {
			System.exit(1);
		}
	}

}
