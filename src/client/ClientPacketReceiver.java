package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
		while (true) {
//		for (int i = 0; i < 4; i++) {
			try {
				rdata = new byte[1000];
				rpacket = new DatagramPacket(rdata, rdata.length);
				client.receive(rpacket);
				String response = new String(rpacket.getData(), 0, rpacket.getLength());
				
				if (response.toLowerCase().contains("lease")) {
					sdata = ipLeaseAck.getBytes();
					spacket = new DatagramPacket(sdata, sdata.length, rpacket.getAddress(),
							rpacket.getPort());
					client.send(spacket);
				}
				
				System.out.printf("%s", response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
