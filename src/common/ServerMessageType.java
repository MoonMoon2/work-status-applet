package common;

import java.util.HashMap;
import java.util.Map;

public enum ServerMessageType {
	
	teamStatusVector("teamStatus", 2), 
	teamsList("listTeams", 1), 
	acceptMessage("acceptMessage", 0),
	denyMessage("denyMessage", 15),
	userList("userList", 3);
	
	
	private static Map<Byte, ServerMessageType> networkToTextMapping;

	private final String text;
	private final byte networkKey;
	
	ServerMessageType(String key, int networkKey) {
		this.text = key;
		this.networkKey = (byte) networkKey;
	}

	public String getKey() {
		return text;
	}

	public int getNetworkKey() {
		return networkKey;
	}
	
	public byte getNetworkKeyByte() {
		return networkKey;
	}
	
	public static ServerMessageType getMessageType(byte networkCode) {
		if (networkToTextMapping == null) {
			initMapping();
		}
		return networkToTextMapping.get(networkCode);
	}
	
	private static void initMapping() {
		networkToTextMapping = new HashMap<>();
		for (ServerMessageType s : values()) {
			networkToTextMapping.put(s.networkKey, s);
		}
	}
	
	

}
