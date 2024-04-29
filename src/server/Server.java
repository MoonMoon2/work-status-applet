package server;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

import common.ClientMessageType;
import common.ServerMessageType;
import common.Status;
import utils.Hasher;
import utils.LookupSet;

public class Server {
	
	
	private static LookupSet<Integer, Team> teams;
	private static LookupSet<InetAddress, User> clients;
	
	
	public static final int HEADER_LENGTH = 1 + Long.BYTES + 1;
	
	
	
	{
		teams = new LookupSet<Integer, Team>(LookupSet.DEFAULT_INTEGER_HASHER);
		clients = new LookupSet<InetAddress, User>(new Hasher<InetAddress>() {

			@Override
			public int hashKey(InetAddress key, int N) {
				return LookupSet.DEFAULT_STRING_HASHER.hashKey(key.getHostName(), N);
			}
			
		});
		
	}
	
	
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Required arguments: port");
			return;
		}
		
		int serverPort = Integer.parseInt(args[0]);
		
		try (DatagramSocket socket = new DatagramSocket(serverPort)) {
			while (true) {
				DatagramPacket request = new DatagramPacket(new byte[2048], 2048);
				
				socket.receive(request);
				
				DatagramPacket response = null;
				
				try {
				
				ClientMessageType type = interpretType(request);

				// need to deal with registClient and authentication
				
				if (type == ClientMessageType.registerClient) {
					
					response = handleRegisterMessage(request);
					
				} else {
					switch (type) {
					case accept:
						response = handleAcceptMessage(request);
						break;
					case joinTeam:
						response = handleJoinMessage(request);
						break;
					case registerClient:
						System.err.println("Something is very wrong if this prints. Something is both true and false. Logic no longer exists. Pray to your gods while you still can.");
						break;
					case requestStatus:
						response = handleRequestStatusMessage(request);
						break;
					case statusUpdate:
						response = handleUpdateStatusMessage(request);
						break;
					case teamList:
						response = handleTeamsMessage(request);
						break;
					}
				}
				} catch (Exception e) {
					
					e.printStackTrace();
					
				}
				
				if (response == null) {
					byte[] message = "Bad Request".getBytes();
					response = new DatagramPacket(message, message.length, request.getAddress(), request.getPort());
				}
				
				socket.send(response);
				
			}
		}
		
	}
	
	
	private static DatagramPacket handleTeamsMessage(DatagramPacket request) {	
		
		StringBuilder stringList = new StringBuilder();
		
		for (Team team : teams.values()) {
			stringList.append(team.getTeamId() + "," + team.getTeamName());
			stringList.append(";");
		}
		
		byte[] responseMessage = stringList.toString().getBytes();
		
		return createStandardPacket(ServerMessageType.teamsList, timestampBytes(), responseMessage, request.getAddress(), request.getPort());
		
	}


	private static DatagramPacket handleUpdateStatusMessage(DatagramPacket request) {
		
		User user = validUser(request);
		
		if (user != null) {
			try {
				byte[] message = getMessageFromRequest(request);
				int statusNumber = message[0];
				user.setStatus(Status.getStatus(statusNumber));
				byte[] rtnMessage = new byte[] {user.getStatus().getNetworkKeyByte()};
				return createStandardPacket(ServerMessageType.acceptMessage, timestampBytes(), rtnMessage, request.getAddress(), request.getPort());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// in the case that this is not a valid user, or that this is a poorly formatted request, send a deny message
		return createStandardPacket(ServerMessageType.denyMessage, timestampBytes(), "".getBytes(), request.getAddress(), request.getPort());
		

	}


	private static DatagramPacket handleRequestStatusMessage(DatagramPacket request) {
		
		
		/*
		 * Needs to send a status update depending on the team ID in the message
		 * 
		 * Check valid User
		 * Parse team ID
		 * Prepare Status Vector
		 * 
		 */
		
		User user = validUser(request);
		
		if (user != null) {
			Team team = validTeam(request);
			
			if (team != null) {
				
				
				
			}
		}
		
		
		return null;
	}


	private static DatagramPacket handleRegisterMessage(DatagramPacket request) {
		// TODO Auto-generated method stub
		return null;
	}


	private static DatagramPacket handleJoinMessage(DatagramPacket request) {
		// TODO Auto-generated method stub
		return null;
	}


	private static DatagramPacket handleAcceptMessage(DatagramPacket request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private static DatagramPacket createStandardPacket(ServerMessageType messageType, byte[] timestamp, byte[] message, InetAddress address, int port) {
		int respLength = HEADER_LENGTH + message.length;
		byte[] finalResponse = new byte[respLength];
		
		finalResponse[0] = messageType.getNetworkKeyByte();
		
		for (int i = 0; i < timestamp.length; i++) {
			finalResponse[i+1] = timestamp[i];
		}
		
		byte spacer = 123;
		
		finalResponse[HEADER_LENGTH - 2] = spacer;			// -1 to get to 0-indexed system, -1 to get to index we want
		
		for (int i = 0; i < finalResponse.length; i++) {
			finalResponse[i + HEADER_LENGTH] = message[i];
		}
		
		return new DatagramPacket(finalResponse, finalResponse.length, address, port);
	}


	private static ClientMessageType interpretType(DatagramPacket request)  {
		byte[] requestData = request.getData();
		
		byte code = requestData[0];
		
		ClientMessageType type = ClientMessageType.getMessageType(code);
		
		return type;
		
	}
	
	private static byte[] timestampBytes() {
		long timestampVal = System.nanoTime();
		ByteBuffer timestampBuff = ByteBuffer.allocate(Long.BYTES);
		timestampBuff.putLong(timestampVal);
		return timestampBuff.array();
	}
	
	private static byte[] getMessageFromRequest(DatagramPacket request) {
		return Arrays.copyOfRange(request.getData(), HEADER_LENGTH, request.getLength());
	}
	
	private static User validUser(DatagramPacket request) {
		
		return clients.get(request.getAddress());
	}
	
	private static Team validTeam(DatagramPacket request) {
		
		byte[] message = getMessageFromRequest(request);
		
		return teams.get((int) message[0]);
	}
	
}
