package model;

import java.io.Serializable;
import java.util.Arrays;

/*
    Student 1  : Ali Mohammadian (201807939)
    Student 2  : Mohamed Daniel Bin Mohamed Izham (201802738)
    Course     : CMPS 405 - Operating Systems
    Assessment : Project 2
    Instructor : Heba M. Dawoud
*/

public class DHCPPacket implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ip, gateway, mask;
	private String dnsIP[];
	private int port;

	public DHCPPacket(String ip, int port, String gateway, String mask, String[] dnsIP) {
		super();
		this.ip = ip;
		this.port = port;
		this.gateway = gateway;
		this.mask = mask;
		this.dnsIP = dnsIP;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String[] getDnsIP() {
		return dnsIP;
	}

	public void setDnsIP(String[] dnsIP) {
		this.dnsIP = dnsIP;
	}

	@Override
	public String toString() {
		return "DHCPPacket [ip=" + ip + ", gateway=" + gateway + ", mask=" + mask + ", dnsIP=" + Arrays.toString(dnsIP)
				+ "]";
	}

}
