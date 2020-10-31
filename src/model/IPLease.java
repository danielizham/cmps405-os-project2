package model;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class IPLease {

	private String ip;
	private int port;
	private LocalDateTime date;


	public IPLease(String ip, int port, LocalDateTime date) {
		super();
		this.ip = ip;
		this.port = port;
		this.date = date;
	}


	
	
	public boolean isExpired(LocalDateTime now) {
		return date.isBefore(now);
	}
	
	public InetSocketAddress getAddress() {
		return new InetSocketAddress(getIp(), getPort());
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


	public void setPort(int port) {
		this.port = port;
	}


	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "IPLease [ip=" + ip + ", date=" + date + "]";
	}
	
	
	
}
