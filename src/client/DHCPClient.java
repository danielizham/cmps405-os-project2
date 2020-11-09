package client;

import java.net.*;
import model.DHCPPacket;
import util.Ports;

import java.io.*;


public class DHCPClient {
	private static final String DHCP_DISCOVER = "DHCP DISCOVER";
	private static final String DHCP_REQUEST = "DHCP REQUEST";
	private static final String DHCP_ACK = "DHCP ACK";

	public DHCPClient() {
		DatagramSocket client;
		
		try {
			
			client = new DatagramSocket();
			
	        // send DHCP DISCOVER 
			String request = DHCP_DISCOVER;
			byte[] sdata = request.getBytes();
			DatagramPacket spacket = new DatagramPacket(sdata,sdata.length,InetAddress.getLocalHost(),Ports.DHCP_SERVER_PORT);
			client.send(spacket);
			
			
			// receive the DHCP packet
			byte[] rdata = new byte[1000];
			DatagramPacket rpacket = new DatagramPacket(rdata,rdata.length);
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
			spacket = new DatagramPacket(sdata,sdata.length,InetAddress.getLocalHost(),Ports.DHCP_SERVER_PORT);
			client.send(spacket);
			
			request = dhcpPacket.getIp();
			sdata = request.getBytes();
			spacket = new DatagramPacket(sdata,sdata.length,InetAddress.getLocalHost(),Ports.DHCP_SERVER_PORT);
			client.send(spacket);
			
			
			// wait for DHCP acknowledgment
			rdata = new byte[1000];
			rpacket = new DatagramPacket(rdata,rdata.length);
			client.receive(rpacket);
			String response = new String(rpacket.getData(),0,rpacket.getLength());
			

			if (response.equals(DHCP_ACK)) {
				System.out.println("DHCP Acknowledgment received!");
			}
			
			
		}catch(IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		new DHCPClient();
	}
}
