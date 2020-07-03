package AudioServerApi.Consoles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import AudioServerApi.AudioServerApi;
import AudioServerApi.IAudioServerApi;

public class HostConsole {


	private static String url = "http://localhost:5000/audioHub";
	private static String authCode = "2806d01cdd3c4bfc80b751e9cec02110293a30354ebb48678147b800cc95b9c4";
	private static String name = "HostConsole" + Math.random();

	public static void main(String[] args) throws IOException {
		BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));

		IAudioServerApi audioServer = AudioServerApi.getInstance();

		audioServer.createConnection(url, authCode, name);

		audioServer.onClientConnect((playerClient) -> {
			System.out.println("client connected! " + playerClient);
		});

		audioServer.onClientDisconnect((playerClient) -> {
			System.out.println("client disconnected! " + playerClient);
		});

		audioServer.onHostConnect((name) -> {
			System.out.println("host connected! name: '" + name + "'");
		});

		audioServer.onHostDisconnect((name) -> {
			System.out.println("host disconnected! name: '" + name + "'");
		});

		audioServer.onCreateChannel((channel) -> {
			System.out.println("channel created! " + channel);
		});

		audioServer.onRemoveChannel((name) -> {
			System.out.println("channel removed! name: '" + name + "'");
		});

		System.out.println("Connected as a host! name: " + name);

		while (true) {

			try {
				String[] input = obj.readLine().split(" ");
				
				if (input.length <= 0)
					return;
			
				switch (input[0].toLowerCase()) {
					case "?":
					case "help":
						System.out.println("Available commands: createchannel, removechannel, playaudio, stopaudio, stopchannel, ping");
						break;
					case "createchannel":
						if (input.length < 4) {
							System.out.println("Required 3 paramaters. name, description and singleAudio");
							break;
						}
						
						audioServer.createChannel(input[1], input[2], Boolean.parseBoolean(input[3]));
						System.out.println("Creating channel...");
						break;
						
					case "removechannel":
						if (input.length < 2) {
							System.out.println("Required 1 paramater. name");
							break;
						}
	
						audioServer.removeChannel(input[1]);
						System.out.println("Removing channel...");
						break;
	
					case "play":
					case "playaudio":
						if (input.length < 5) {
							System.out.println("Required at least 4 paramaters. uuid, channel, audioname, fileLocation, [startTime], [looping]");
							break;
						}
						
						double startTime = input.length >= 6 ? Double.parseDouble(input[5]) : 0;
						boolean looping = input.length >= 7 ? Boolean.parseBoolean(input[6]) : false;
						
						audioServer.playAudio(Arrays.asList(input[1]), input[2], input[3], input[4], startTime, looping);
						System.out.println("playing audio...");
						break;
						
					case "stop":
					case "stopaudio":
						if (input.length < 4) {
							System.out.println("Required at least 3 paramaters. uuid, channel, audioname");
							break;
						}
						
						audioServer.stopAudio(Arrays.asList(input[1]), input[2], input[3]);
						System.out.println("stopping audio...");
						break;
						
					case "stopchannel":
						if (input.length < 3) {
							System.out.println("Required at least 2 paramaters. uuid, channel");
							break;
						}
	
						audioServer.stopChannel(Arrays.asList(input[1]), Arrays.asList(input[2]));
						System.out.println("stopping all audio in channel...");
						break;
	
					case "stopall":
					case "stopallaudio":
						if (input.length < 2) {
							System.out.println("Required at least 1 paramater. uuid");
							break;
						}
	
						audioServer.stopAllAudio(Arrays.asList(input[1]));
						System.out.println("stopping all audio...");
						break;

					case "getchannels":
						System.out.println("getting channels...");
						audioServer.getChannels(channels -> System.out.println(channels));
						break;

					case "getplayers":
					case "getplayerclients":
						System.out.println("getting player clients...");
						audioServer.getPlayerClients(playerClients -> System.out.println(playerClients));
						break;

					case "gethosts":
					case "gethostclients":
						System.out.println("getting host clients...");
						audioServer.getHostClients(hostClients -> System.out.println(hostClients));
						break;

					case "ping":
						System.out.println("pinging...");
						audioServer.ping(callback -> System.out.println(callback));
						break;
	
					default:
						System.out.println("Unknown command!");
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
