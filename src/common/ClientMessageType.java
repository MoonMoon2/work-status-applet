package common;

import java.util.HashMap;
import java.util.Map;

public enum ClientMessageType {
	
	statusUpdate("statusUpdate", 1), 
	requestStatus("requestStatus", 2), 
	joinTeam("joinTeam", 4), 
	teamList("teamList", 6), 
	registerClient("registerClient", 8),
	accept("accept", 15);
	
	
	private static Map<Byte, ClientMessageType> networkToTextMapping;

	private final String text;
	private final byte networkKey;
	
	ClientMessageType(String key, int networkKey) {
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
	
	public static ClientMessageType getMessageType(int networkCode) {
		if (networkToTextMapping == null) {
			initMapping();
		}
		return networkToTextMapping.get((byte) networkCode);
	}
	
	private static void initMapping() {
		networkToTextMapping = new HashMap<>();
		for (ClientMessageType s : values()) {
			networkToTextMapping.put(s.networkKey, s);
		}
	}
	
	

}
