package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DHCPServer extends Thread {
	DatagramPacket inboundPacket;
	DatagramSocket server;

	public DHCPServer(DatagramSocket server, DatagramPacket packet) {
	}
	
	public void run() {
		
	}
}
