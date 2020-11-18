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
	private Client clientInstance;
	private final String ipLeaseAck = String.format("Client\t: IP lease renewal acknowledged.");

	ClientPacketReceiver(DatagramSocket client, Client clientInstance) {
		this.client = client;
		this.clientInstance = clientInstance;
	}

	public void run() {
		try {
//		while (true) {
			for (int i = 0; i < 20; i++) {
				rdata = new byte[1000];
				rpacket = new DatagramPacket(rdata, rdata.length);
				client.receive(rpacket);
				String response = new String(rpacket.getData(), 0, rpacket.getLength());

				if (response.toLowerCase().contains("error")) {
					System.out.print(response);
					return;
				}

				if (response.toLowerCase().contains("lease")) {
					if (clientInstance.isRequestingDNS)
						sdata = "Client\t: Server, please suspend your output".getBytes();
					else
						sdata = ipLeaseAck.getBytes();
					spacket = new DatagramPacket(sdata, sdata.length, rpacket.getAddress(), rpacket.getPort());
					client.send(spacket);
				} else if (response.toLowerCase().contains("request dhcp again")) {
					clientInstance.requestDHCP();
				}

				if (!clientInstance.isRequestingDNS)
					System.out.printf("%s", response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
