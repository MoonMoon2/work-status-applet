package common;

import java.util.HashMap;
import java.util.Map;

public enum Status {
	
	busy("busy", 1) , 
	available("available", 0), 
	meeting("meeting", 3), 
	onbreak("onbreak", 4), 
	phone("phone", 5), 
	home("home", 6);
	
	
	private static Map<Byte, Status> networkToTextMapping;

	private final String text;
	private final byte networkKey;
	
	Status(String key, int networkKey) {
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
	
	public static Status getStatus(int networkCode) {
		if (networkToTextMapping == null) {
			initMapping();
		}
		return networkToTextMapping.get((byte) networkCode);
	}
	
	private static void initMapping() {
		networkToTextMapping = new HashMap<>();
		for (Status s : values()) {
			networkToTextMapping.put(s.networkKey, s);
		}
	}
	
	

}
