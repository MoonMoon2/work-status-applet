package server;

import java.net.InetAddress;
import java.util.Set;
import java.util.HashSet;

import common.MD5Hasher;
import common.Status;

public class User {
	
	private final String username;
	private String displayName;
	private byte[] password;
	private Status status;
	
	private Set<Team> teams;
	
	private InetAddress hostAddress;
	
	
	public User(String username, String displayName, String password) {
		super();
		this.username = username;
		this.displayName = displayName;
		this.password = MD5Hasher.getHashBytes(password);
		this.teams = new HashSet<>();
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public byte[] getPassword() {
		return password;
	}
	
	
	public String getPasswordStr() {
		return MD5Hasher.bytesToHex(password);
	}


	public void setPassword(byte[] password) {
		this.password = password;
	}
	
	public void setPassword(String password) {
		this.password = MD5Hasher.getHashBytes(password);
	}


	public String getUsername() {
		return username;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}
	

	public Set<Team> getTeams() {
		return teams;
	}
	
	
	public boolean isOnTeam(Team team) {
		return teams.contains(team);
	}
	

	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}
	
	
	public boolean addTeam(Team team) {
		return teams.add(team);
	}
	
	
	public boolean removeTeam(Team team) {
		return teams.remove(team);
	}
	

	public InetAddress getHostAddress() {
		return hostAddress;
	}
	

	public void setHostAddress(InetAddress hostAddress) {
		this.hostAddress = hostAddress;
	}
	
	

}
