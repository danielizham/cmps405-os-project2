package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

import model.DHCPPacket;
import model.IPLease;

public class DHCPListener extends Thread {
	Socket client;
	Scanner fromNet = null;
	Formatter toNet = null;
	
	private final String GATEWAY_IP = "192.168.0.1";
	private final String[] DNS_IPS = {"192.168.0.2","192.168.0.3"};
	private final String MASK = "255.255.0.0";
	private final String DHCP_DISCOVER = "DHCP DISCOVER";
	private final String DHCP_REQUEST = "DHCP REQUEST";
	private final String DHCP_ACK = "DHCP ACK";

	public DHCPListener(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		
		try {

			fromNet = new Scanner(client.getInputStream());
			toNet = new Formatter(client.getOutputStream());
	        ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());

	        
	        // The loop will keep listening for any requests by the client(s)
	        while(true) {
	        	
	        	// only if there is a request 
	        	if (fromNet.hasNextLine()) {
				
	        		
				String request = fromNet.nextLine();
				
					
					// if the type is  DHCP DISCOVER
					if (request.equals(DHCP_DISCOVER)) {
						
						System.out.println(DHCP_DISCOVER + " request recieved");
						Random r = new Random();
						r.setSeed(r.nextInt(10000));

						// create a packet object with a random IP from the pool
						DHCPPacket sPacket = new DHCPPacket(DHCPServer.ipPool.get(r.nextInt(1000) % DHCPServer.ipPool.size()), GATEWAY_IP, MASK, DNS_IPS);
						
						// send the packet using objectOutputStream
						objectOutputStream.writeObject(sPacket);
					}
					
					// if the type is DHCP REQUEST
					if (request.equals(DHCP_REQUEST)) {
						System.out.println(DHCP_REQUEST + " request recieved");
						
						// get the IP
						String leasedIP = fromNet.nextLine();
						
						// add a new lease and remove IP from the pool
						addLease(leasedIP);
						
						
						// send DHCP Acknowledgement
						toNet.format("%s\n", DHCP_ACK);
						toNet.flush();
						
					}
					
	        		
				}
				
	        	
	        }
	        

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}
	
	public void addLease(String leasedIP) {
		
			
			LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);
						
			IPLease lease = new IPLease(leasedIP, client.getPort() , expiryDate);
			
			DHCPServer.leases.add(lease);
			
			DHCPServer.ipPool.remove(leasedIP);

		
	}
	
}
