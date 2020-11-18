package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import model.IPLease;

/*
    Student 1  : Ali Mohammadian (201807939)
    Student 2  : Mohamed Daniel Bin Mohamed Izham (201802738)
    Course     : CMPS 405 - Operating Systems
    Assessment : Project 2
    Instructor : Heba M. Dawoud
*/

public class DNSServer extends Thread {

	private DatagramPacket inboundPacket, outboundPacket;
	private DatagramSocket server;
	private String DNSlist[][] = { { "cdns2.qatar.net.qa", "212.77.192.60" }, { "www.yahoo.com", "87.248.122.122" },
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

	DNSServer(DatagramSocket server, DatagramPacket packet) {
		this.inboundPacket = packet;
		this.server = server;
	}

	private boolean hasUsedDHCP() {
		for (IPLease clientWithDHCP : DHCPServer.leases) {
			if (clientWithDHCP.getPort() == inboundPacket.getPort())
				return true; // the client that sent this packet has used DHCP
		}
		return false; // the client has not used DHCP
	}

	public void run() {
		String response = new String("");

		if (hasUsedDHCP()) {
			byte[] data = inboundPacket.getData();
			String domain_name = (new String(data, 0, data.length)).trim();

			boolean found = false;
			response = "DNS\t: " + domain_name + " corresponds to IP address(es) ";
			for (int i = 0; i < DNSlist.length; i++) {
				if (DNSlist[i][0].equals(domain_name)) {
					response = response + DNSlist[i][1] + ", ";
					found = true;
				}
			}
			response = response.substring(0, response.length() - 2) + "\n"; // remove the last comma & space

			if (!found)
				response = "DNS\t: Cannot resolve Name to IP ... \n";

		} else {
			response = "DNS\t: ERROR! Your device has not been assigned an IP address by the DHCP.\n"
					+ "\t  Please make a DHCP request before using the DNS service. Sorry for the inconvenience.\n";
		}

		try {
			byte[] r = response.getBytes();
			outboundPacket = new DatagramPacket(r, r.length, inboundPacket.getAddress(), inboundPacket.getPort());
			server.send(outboundPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
