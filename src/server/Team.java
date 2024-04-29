package server;

import java.util.List;
import java.util.Objects;

public class Team {
	
	private String teamName;
	private int teamId;
	private List<User> clients;
	
	public Team(int teamId, String teamName) {
		this.teamId = teamId;
		this.teamName = teamName;
	}
	
	/**
	 * Exists as a utility for checking team Ids
	 * @param teamId
	 */
	public Team(int teamId) {
		super();
		this.teamId = teamId;
	}


	public Team(String teamName, int teamId, List<User> clients) {
		super();
		this.teamName = teamName;
		this.teamId = teamId;
		this.clients = clients;
	}


	public Team(List<User> clients) {
		super();
		this.clients = clients;
	}
	
	
	public String getStatusVector() {
		StringBuilder str = new StringBuilder();
		
	}

	public List<User> getClients() {
		return clients;
	}

	public void setClients(List<User> clients) {
		this.clients = clients;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getTeamId() {
		return teamId;
	}

	public void getTeamId(int teamString) {
		this.teamId = teamString;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		return teamId == other.teamId;
	}
	
	

}
