package client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

import model.DHCPPacket;
import util.Ports;

public class Client {

	private DatagramSocket client;

	private Client() throws SocketException {
		client = new DatagramSocket();
		requestDHCP();
		requestDNS();
	}

	private void requestDHCP() {
		final String DHCP_DISCOVER = "DHCP DISCOVER";
		final String DHCP_REQUEST = "DHCP REQUEST";
		final String DHCP_ACK = "DHCP ACK";

		try {
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

			System.out.println("The DHCP Packet was received! \n");
			System.out.printf("%-25s %s\n", "IP Address: ", dhcpPacket.getIp());
			System.out.printf("%-25s %s\n", "Subnet Mask: ", dhcpPacket.getMask());
			System.out.printf("%-25s %s\n", "Default Gateway: ", dhcpPacket.getGateway());
			System.out.printf("%-25s %s\n", "DNS IP Address: ", dhcpPacket.getDnsIP()[0]);
			System.out.printf("%-25s %s\n\n", "DNS IP Address: ", dhcpPacket.getDnsIP()[1]);

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
				System.out.println("DHCP Acknowledgment received!");
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void requestDNS() {
		Scanner kb = null;
		try {
			kb = new Scanner(System.in);
			System.out.print("\nEnter a domain name to be translated: ");
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
		} finally {
			if (kb != null)
				kb.close();
		}
	}

	public static void main(String[] args) {
		try {
			new Client();
		} catch (SocketException e) {
			System.exit(1);
		}
	}

}
