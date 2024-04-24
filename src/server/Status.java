package server;

import java.util.HashMap;
import java.util.Map;

public enum Status {
	
	busy("busy", "8bc1b2f84252c3df4edd53e4aad097a7") , 
	available("available", "e4894ca167b08880bfc35862f18575eb"), 
	meeting("meeting", "d48788168076b999d36c4f3ccb75ba2f"), 
	onbreak("onbreak", "1ebbb707993761895265dd4536c04364"), 
	phone("phone", "f7a42fe7211f98ac7a60a285ac3a9e87"), 
	home("home", "106a6c241b8797f52e1e77317b96a201");
	
	
	private static Map<String, Status> networkToTextMapping;

	private final String text;
	private final String networkKey;
	
	Status(String key, String networkKey) {
		this.text = key;
		this.networkKey = networkKey;
	}

	public String getKey() {
		return text;
	}

	public String getNetworkKey() {
		return networkKey;
	}
	
	public byte[] getNetworkKeyBytes() {
		return networkKey.getBytes();
	}
	
	public static Status getStatus(String networkCode) {
		if (networkToTextMapping == null) {
			initMapping();
		}
		return networkToTextMapping.get(networkCode);
	}
	
	private static void initMapping() {
		networkToTextMapping = new HashMap<>();
		for (Status s : values()) {
			networkToTextMapping.put(s.networkKey, s);
		}
	}
	
	

}
