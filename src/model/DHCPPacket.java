package model;

import java.io.Serializable;
import java.util.Arrays;

public class DHCPPacket implements Serializable {

	private String ip, gateway, mask;
	private String dnsIP[];
	
	
	public DHCPPacket(String ip, String gateway, String mask, String[] dnsIP) {
		super();
		this.ip = ip;
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
