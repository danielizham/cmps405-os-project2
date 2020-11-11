package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/*
    Student 1  : Ali Mohammadian (201807939)
    Student 2  : Mohamed Daniel Bin Mohamed Izham (201802738)
    Course     : CMPS 405 - Operating Systems
    Assessment : Project 2
    Instructor : Heba M. Dawoud
*/

public class ClientPacketReceiver extends Thread {

	private DatagramSocket client;
	private DatagramPacket rpacket;
	private DatagramPacket spacket;
	private byte[] rdata;
	private byte[] sdata;
	private final String ipLeaseAck = String.format("Client\t: IP lease renewal acknowledged.");

	ClientPacketReceiver(DatagramSocket client) {
		this.client = client;
	}

	public void run() {
//		while (true) {
		for (int i = 0; i < 4; i++) {
			try {
				rdata = new byte[1000];
				rpacket = new DatagramPacket(rdata, rdata.length);
				client.receive(rpacket);
				String response = new String(rpacket.getData(), 0, rpacket.getLength());

				if (response.toLowerCase().contains("lease")) {
					sdata = ipLeaseAck.getBytes();
					spacket = new DatagramPacket(sdata, sdata.length, rpacket.getAddress(), rpacket.getPort());
					client.send(spacket);
				}

				if (!Client.isRequestingDNS)
					System.out.printf("%s", response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
