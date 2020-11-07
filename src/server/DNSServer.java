package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
    Student 1  : Ali Mohammadian (201807939)
    Student 2  : Mohamed Daniel Bin Mohamed Izham (201802738)
    Course     : CMPS 405 - Operating Systems
    Assessment : Project 2
    Instructor : Heba M. Dawoud
*/

public class DNSServer extends Thread {

	DatagramPacket inboundPacket, outboundPacket;
	DatagramSocket server;
	String DNSlist[][] = { { "cdns2.qatar.net.qa", "212.77.192.60" }, { "www.yahoo.com", "87.248.122.122" },
			{ "java.sun.com", "192.9.162.55" }, { "www.google.com", "173.194.36.52" },
			{ "www.google.com", "173.194.36.48" }, { "www.google.com", "173.194.36.50" },
			{ "www.google.com", "173.194.36.51" }, { "www.google.com", "173.194.36.49" },
			{ "www.qu.edu.qa", "86.36.68.18" }, { "upm.edu.my", "119.40.119.1" }, { "uum.edu.my", "103.5.180.122" },
			{ "yu.edu.jo", "87.236.233.10" }, { "www.sun.com", "137.254.16.113" }, { "www.oracle.com", "2.23.241.55" },
			{ "www.gm.com", "2.22.9.175" }, { "www.motorola.com", "23.14.215.224" }, { "www.nokia.com", "2.22.75.80" },
			{ "www.intel.com", "212.77.199.203" }, { "www.intel.com", "212.77.199.210" },
			{ "www.apple.com", "2.22.77.15" }, { "www.honda.com", "164.109.25.248" },
			{ "www.gmail.com", "173.194.36.54" }, { "www.gmail.com", "173.194.36.53" },
			{ "www.hotmail.com", "94.245.116.13" }, { "www.hotmail.com", "94.245.116.11" },
			{ "www.toyota.com", "212.77.199.224" }, { "www.toyota.com", "212.77.199.203" },
			{ "www.gmc.com", "2.22.247.241" }, { "www.mit.edu", "18.9.22.169" }, { "www.cmu.edu", "128.2.10.162" } };

	public DNSServer(DatagramSocket server, DatagramPacket packet) {
		this.inboundPacket = packet;
		this.server = server;
	}

	private boolean hasUsedDHCP() { // How to access DHCP info of the client ???
		InetAddress clientAddress = inboundPacket.getAddress(); // returns the current computer's IP address
		System.out.println(clientAddress);
		return true;
	}

	public void run() {
		if (hasUsedDHCP()) {
			byte[] data = inboundPacket.getData();
			String domain_name = (new String(data, 0, data.length)).trim();
			System.out.println(domain_name);
			boolean found = false;
			String response = new String("");
			for (int i = 0; i < DNSlist.length; i++) {
				if (DNSlist[i][0].equals(domain_name)) {
					response = response + "Name: " + DNSlist[i][0] + "\tIP: " + DNSlist[i][1] + "\n";
					found = true;
				}
			}
			if (!found)
				response = response + "Cannot resolve Name to IP ... ";
			response = response + "\n";

			byte[] r = response.getBytes();
			outboundPacket = new DatagramPacket(r, r.length, inboundPacket.getAddress(), inboundPacket.getPort());
			System.out.println(response);
			try {
				server.send(outboundPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
