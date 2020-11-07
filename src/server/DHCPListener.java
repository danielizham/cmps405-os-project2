package server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;

import model.DHCPPacket;
import model.IPLease;

public class DHCPListener extends Thread {
	
	private final String GATEWAY_IP = "192.168.0.1";
	private final String[] DNS_IPS = {"192.168.0.2","192.168.0.3"};
	private final String MASK = "255.255.0.0";
	private final String DHCP_DISCOVER = "DHCP DISCOVER";
	private final String DHCP_REQUEST = "DHCP REQUEST";
	private final String DHCP_ACK = "DHCP ACK";
	
	DatagramSocket server;


	public DHCPListener() {

	}
	
	
	
	@Override
	public void run() {
		
		
		try {
			server = new DatagramSocket(4004);
			
			while(true) {
				
				
				byte[] payload = new byte[100];
				DatagramPacket rPacket = new DatagramPacket(payload,payload.length);
				server.receive(rPacket);
				String request = new String(rPacket.getData(),0,rPacket.getLength());


				// if the type is  DHCP DISCOVER
				if (request.equals(DHCP_DISCOVER)) {
					
					System.out.println(DHCP_DISCOVER + " request recieved");

					// create a packet object with a random IP from the pool
					DHCPPacket dhcpPacket = new DHCPPacket(DHCPServer.getRandomAvailableIP(), GATEWAY_IP, MASK, DNS_IPS);
					
					
					// send the packet using ObjectOutputStream and ByteArrayOutputStream		
					ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
					ObjectOutput objectOutput = new ObjectOutputStream(byteStream); 
					objectOutput.writeObject(dhcpPacket);
					objectOutput.close();

					byte[] data = byteStream.toByteArray();
					
					DatagramPacket spacket = new DatagramPacket(data,data.length,rPacket.getAddress(),rPacket.getPort());
					server.send(spacket);
					
				}
				
				// if the type is DHCP REQUEST
				if (request.equals(DHCP_REQUEST)) {
					System.out.println(DHCP_REQUEST + " request recieved");
										
					// get the IP
					payload = new byte[100];
					rPacket = new DatagramPacket(payload,payload.length);
					server.receive(rPacket);
					String leasedIP = new String(rPacket.getData(),0,rPacket.getLength());
					
					// add a new lease and remove IP from the pool
					addLease(leasedIP, rPacket);
					
					
					// send DHCP Acknowledgement
					byte[] data = DHCP_ACK.getBytes();
					DatagramPacket spacket = new DatagramPacket(data,data.length,rPacket.getAddress(),rPacket.getPort());
					server.send(spacket);
					
				}
				
				

				
				
			}
		}catch(IOException ioe) {ioe.printStackTrace();}
		

	}
	
	public void addLease(String leasedIP, DatagramPacket packet) {
		
			
			LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);
						
			IPLease lease = new IPLease(leasedIP, packet.getPort() , expiryDate);
			
			DHCPServer.leases.add(lease);
			
			DHCPServer.ipPool.remove(leasedIP);

		
	}
	
}
