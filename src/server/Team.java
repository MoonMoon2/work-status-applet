package server;

import java.security.InvalidKeyException;
import java.util.List;
import java.util.Map;

public class Team {
	
	
	private Map<String, Status> clientStatuses;
	private List<String> clients;
	
	public Team(Map<String, Status> clientStatuses, List<String> clients) {
		this.clientStatuses = clientStatuses;
		this.clients = clients;
	}

	public Map<String, Status> getClientStatuses() {
		return clientStatuses;
	}

	public void setClientStatuses(Map<String, Status> clientStatuses) {
		this.clientStatuses = clientStatuses;
	}

	public List<String> getClients() {
		return clients;
	}

	public void setClients(List<String> clients) {
		this.clients = clients;
	}
	
	public boolean updateClientStatus(String clientId, Status newStatus) throws InvalidKeyException {
		if (clientStatuses.containsKey(clientId)) {
			clientStatuses.put(clientId, newStatus);
			return true;
		} else {
			throw new InvalidKeyException("Client not found in team");
		}
	}

}
