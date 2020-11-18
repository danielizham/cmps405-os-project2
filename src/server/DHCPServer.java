package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import model.DHCPPacket;
import model.IPLease;
import util.Ports;

/*
    Student 1  : Ali Mohammadian (201807939)
    Student 2  : Mohamed Daniel Bin Mohamed Izham (201802738)
    Course     : CMPS 405 - Operating Systems
    Assessment : Project 2
    Instructor : Heba M. Dawoud
*/

public class DHCPServer extends Thread {

	private DatagramSocket IPLeaseServer;
	private DatagramPacket rpacket;
	private DatagramPacket spacket;
	private byte[] rdata;
	private byte[] sdata;

	// For testing purposes. REMEMBER to change back to 1 day before submission
	private enum TimeUnit {
		DAY, MINUTE, SECOND
	}

	// DEFAULT: 1 day
	private static TimeUnit timeUnit = TimeUnit.SECOND;
	private static long numOfTimeUnits = 3;

	private static CopyOnWriteArrayList<String> ipPool = new CopyOnWriteArrayList<String>( // thread-safe/synchronised
			Arrays.asList("192.168.0.4", "192.168.0.5", "192.168.0.6", "192.168.0.7", "192.168.0.8", "192.168.0.9",
					"192.168.0.10", "192.168.0.11", "192.168.0.12", "192.168.0.13", "192.168.0.14", "192.168.0.15",
					"192.168.0.16", "192.168.0.17", "192.168.0.18", "192.168.0.19", "192.168.0.20", "192.168.0.21",
					"192.168.0.22", "192.168.0.23", "192.168.0.24", "192.168.0.25", "192.168.0.26", "192.168.0.27",
					"192.168.0.28", "192.168.0.29", "192.168.0.30", "192.168.0.31", "192.168.0.32", "192.168.0.33",
					"192.168.0.34", "192.168.0.35", "192.168.0.36", "192.168.0.37", "192.168.0.38", "192.168.0.39",
					"192.168.0.40", "192.168.0.41", "192.168.0.42", "192.168.0.43", "192.168.0.44", "192.168.0.45",
					"192.168.0.46", "192.168.0.47", "192.168.0.48", "192.168.0.49", "192.168.0.50", "192.168.0.51",
					"192.168.0.52", "192.168.0.53", "192.168.0.54", "192.168.0.55", "192.168.0.56", "192.168.0.57",
					"192.168.0.58", "192.168.0.59", "192.168.0.60", "192.168.0.61", "192.168.0.62", "192.168.0.63",
					"192.168.0.64", "192.168.0.65", "192.168.0.66", "192.168.0.67", "192.168.0.68", "192.168.0.69",
					"192.168.0.70", "192.168.0.71", "192.168.0.72", "192.168.0.73", "192.168.0.74", "192.168.0.75",
					"192.168.0.76", "192.168.0.77", "192.168.0.78", "192.168.0.79", "192.168.0.80", "192.168.0.81",
					"192.168.0.82", "192.168.0.83", "192.168.0.84", "192.168.0.85", "192.168.0.86", "192.168.0.87",
					"192.168.0.88", "192.168.0.89", "192.168.0.90", "192.168.0.91", "192.168.0.92", "192.168.0.93",
					"192.168.0.94", "192.168.0.95", "192.168.0.96", "192.168.0.97", "192.168.0.98", "192.168.0.99",
					"192.168.0.100", "192.168.0.101", "192.168.0.102", "192.168.0.103", "192.168.0.104",
					"192.168.0.105", "192.168.0.106", "192.168.0.107", "192.168.0.108", "192.168.0.109",
					"192.168.0.110", "192.168.0.111", "192.168.0.112", "192.168.0.113", "192.168.0.114",
					"192.168.0.115", "192.168.0.116", "192.168.0.117", "192.168.0.118", "192.168.0.119",
					"192.168.0.120", "192.168.0.121", "192.168.0.122", "192.168.0.123", "192.168.0.124",
					"192.168.0.125", "192.168.0.126", "192.168.0.127", "192.168.0.128", "192.168.0.129",
					"192.168.0.130", "192.168.0.131", "192.168.0.132", "192.168.0.133", "192.168.0.134",
					"192.168.0.135", "192.168.0.136", "192.168.0.137", "192.168.0.138", "192.168.0.139",
					"192.168.0.140", "192.168.0.141", "192.168.0.142", "192.168.0.143", "192.168.0.144",
					"192.168.0.145", "192.168.0.146", "192.168.0.147", "192.168.0.148", "192.168.0.149",
					"192.168.0.150", "192.168.0.151", "192.168.0.152", "192.168.0.153", "192.168.0.154",
					"192.168.0.155", "192.168.0.156", "192.168.0.157", "192.168.0.158", "192.168.0.159",
					"192.168.0.160", "192.168.0.161", "192.168.0.162", "192.168.0.163", "192.168.0.164",
					"192.168.0.165", "192.168.0.166", "192.168.0.167", "192.168.0.168", "192.168.0.169",
					"192.168.0.170", "192.168.0.171", "192.168.0.172", "192.168.0.173", "192.168.0.174",
					"192.168.0.175", "192.168.0.176", "192.168.0.177", "192.168.0.178", "192.168.0.179",
					"192.168.0.180", "192.168.0.181", "192.168.0.182", "192.168.0.183", "192.168.0.184",
					"192.168.0.185", "192.168.0.186", "192.168.0.187", "192.168.0.188", "192.168.0.189",
					"192.168.0.190", "192.168.0.191", "192.168.0.192", "192.168.0.193", "192.168.0.194",
					"192.168.0.195", "192.168.0.196", "192.168.0.197", "192.168.0.198", "192.168.0.199",
					"192.168.0.200", "192.168.0.201", "192.168.0.202", "192.168.0.203", "192.168.0.204",
					"192.168.0.205", "192.168.0.206", "192.168.0.207", "192.168.0.208", "192.168.0.209",
					"192.168.0.210", "192.168.0.211", "192.168.0.212", "192.168.0.213", "192.168.0.214",
					"192.168.0.215", "192.168.0.216", "192.168.0.217", "192.168.0.218", "192.168.0.219",
					"192.168.0.220", "192.168.0.221", "192.168.0.222", "192.168.0.223", "192.168.0.224",
					"192.168.0.225", "192.168.0.226", "192.168.0.227", "192.168.0.228", "192.168.0.229",
					"192.168.0.230", "192.168.0.231", "192.168.0.232", "192.168.0.233", "192.168.0.234",
					"192.168.0.235", "192.168.0.236", "192.168.0.237", "192.168.0.238", "192.168.0.239",
					"192.168.0.240", "192.168.0.241", "192.168.0.242", "192.168.0.243", "192.168.0.244",
					"192.168.0.245", "192.168.0.246", "192.168.0.247", "192.168.0.248", "192.168.0.249",
					"192.168.0.250", "192.168.0.251", "192.168.0.252", "192.168.0.253", "192.168.0.254"));
	static CopyOnWriteArrayList<IPLease> leases = new CopyOnWriteArrayList<IPLease>(); // thread-safe/synchronised

	private static final String GATEWAY_IP = "192.168.0.1";
	private static final String[] DNS_IPS = { "192.168.0.2", "192.168.0.3" };
	private static final String MASK = "255.255.0.0";
	static final String DHCP_DISCOVER = "DHCP DISCOVER";
	static final String DHCP_REQUEST = "DHCP REQUEST";
	static final String DHCP_ACK = "DHCP ACK";

	DHCPServer() {
	}

	public void run() {
		try {
			IPLeaseServer = new DatagramSocket(Ports.DHCP_IPLEASE_PORT);
			IPLeaseServer.setSoTimeout(1000);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		// This loop will always check the date and time to find expired leased IPs
		while (true) {

			LocalDateTime now = LocalDateTime.now();

			for (IPLease ipLease : leases) {

				if (ipLease != null) {
					// if the lease is expired
					if (ipLease.isExpired(now)) {
						String ip = ipLease.getIp();
						int port = ipLease.getPort();

						if (hasDuplicatesAndNotFirstElem(ipLease, leases)) {
							// among the duplicates, only the first one will be renewed
							// the rest will need to make another DHCP request
							try {
								String leaseFailureMessage = String
										.format("DHCP\t: Client, your IP has been taken. Please request DHCP again%n");
								sdata = leaseFailureMessage.getBytes();
								spacket = new DatagramPacket(sdata, sdata.length, InetAddress.getLocalHost(), port);
								IPLeaseServer.send(spacket);
								leases.remove(ipLease); // remove this duplicate while keeping the first instance of this IP
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							// send an IP address renewal message and see if
							// the client replies using the try-catch block
							try {
								String renewalInfo = String.format(
										"DHCP\t: Successfully renewed lease for IP address %s at port number %d%n", ip,
										port);
								sdata = renewalInfo.getBytes();
								spacket = new DatagramPacket(sdata, sdata.length, InetAddress.getLocalHost(), port);
								IPLeaseServer.send(spacket);

								try {
									rdata = new byte[1000];
									rpacket = new DatagramPacket(rdata, rdata.length);
									IPLeaseServer.receive(rpacket);

									String response = new String(rpacket.getData(), 0, rpacket.getLength());
									if (!response.toLowerCase().contains("suspend your output"))
										System.out.print(renewalInfo);

									switch (timeUnit) {
									case DAY:
										ipLease.setDate(now.plusDays(numOfTimeUnits));
										break;
									case MINUTE:
										ipLease.setDate(now.plusMinutes(numOfTimeUnits));
										break;
									case SECOND:
										ipLease.setDate(now.plusSeconds(numOfTimeUnits));
										break;
									}
								} catch (SocketTimeoutException ste) {
									leases.remove(ipLease);
									ipPool.add(ipLease.getIp());
									System.out.printf("DHCP\t: IP address %s failed to be renewed%n", ip);
								}

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	private boolean hasDuplicatesAndNotFirstElem(IPLease ipLease, CopyOnWriteArrayList<IPLease> currentLeases) {
		// Assume first there is no duplicate, hence it is also the first element.
		boolean isDuplicate = false;
		boolean isFirst = true;
		for (IPLease lease : currentLeases) {
			if (lease.getIp().equals(ipLease.getIp()) && lease.getPort() != ipLease.getPort()) {
				isDuplicate = true;
				if (currentLeases.indexOf(lease) < currentLeases.indexOf(ipLease)) {
					isFirst = false;
					break;
				}
			}
		}
		return (isDuplicate && !isFirst);
	}

	private static String getRandomAvailableIP() {

		Random r = new Random(System.currentTimeMillis());

		return ipPool.get(r.nextInt(ipPool.size()));
	}

	static void handleDHCPDiscover(DatagramSocket server, DatagramPacket rPacket) {

		ByteArrayOutputStream byteStream = null;
		ObjectOutput objectOutput = null;
		try {

			System.out.println("DHCP\t: " + DHCPServer.DHCP_DISCOVER + " request received");

			// create a packet object with a random IP from the pool
			DHCPPacket dhcpPacket = new DHCPPacket(DHCPServer.getRandomAvailableIP(), rPacket.getPort(),
					DHCPServer.GATEWAY_IP, DHCPServer.MASK, DHCPServer.DNS_IPS);

			// send the packet using ObjectOutputStream and ByteArrayOutputStream
			byteStream = new ByteArrayOutputStream();
			objectOutput = new ObjectOutputStream(byteStream);
			objectOutput.writeObject(dhcpPacket);

			byte[] data = byteStream.toByteArray();

			DatagramPacket spacket = new DatagramPacket(data, data.length, rPacket.getAddress(), rPacket.getPort());
			server.send(spacket);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectOutput != null)
					objectOutput.close();
				if (byteStream != null)
					byteStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	static void handleDHCPRequest(DatagramSocket server) {

		System.out.println("DHCP\t: " + DHCPServer.DHCP_REQUEST + " request received");

		try {

			// get the IP
			byte[] payload = new byte[100];
			DatagramPacket rPacket = new DatagramPacket(payload, payload.length);
			server.receive(rPacket);
			String leasedIP = new String(rPacket.getData(), 0, rPacket.getLength());

			// add a new lease and remove IP from the pool
			addLease(leasedIP, rPacket);

			// send DHCP Acknowledgement
			byte[] data = DHCPServer.DHCP_ACK.getBytes();
			DatagramPacket spacket = new DatagramPacket(data, data.length, rPacket.getAddress(), rPacket.getPort());
			server.send(spacket);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void addLease(String leasedIP, DatagramPacket packet) {

		LocalDateTime expiryDate = null;
		switch (timeUnit) {
		case DAY:
			expiryDate = LocalDateTime.now().plusDays(numOfTimeUnits);
			break;
		case MINUTE:
			expiryDate = LocalDateTime.now().plusMinutes(numOfTimeUnits);
			break;
		case SECOND:
			expiryDate = LocalDateTime.now().plusSeconds(numOfTimeUnits);
		}

		IPLease lease = new IPLease(leasedIP, packet.getPort(), expiryDate);

		DHCPServer.leases.add(lease);

		DHCPServer.ipPool.remove(leasedIP);

	}

}
