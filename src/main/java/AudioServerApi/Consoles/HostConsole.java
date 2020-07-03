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

		audioServer.CreateConnection(url, authCode, name);

		audioServer.OnClientConnect((connectionId, uuid) -> {
			System.out.println("client connected! connection ID: '" + connectionId + "', uuid: '" + uuid + "'");
		});

		audioServer.OnClientDisconnect((connectionId, uuid) -> {
			System.out.println("client disconnected! connection ID: '" + connectionId + "', uuid: '" + uuid + "'");
		});

		audioServer.OnHostConnect((name) -> {
			System.out.println("host connected! name: '" + name + "'");
		});

		audioServer.OnHostDisconnect((name) -> {
			System.out.println("host disconnected! name: '" + name + "'");
		});

		audioServer.OnCreateChannel((name, description, singleAudio) -> {
			System.out.println("channel created! name: '" + name + "', description: '" + description + "', singleAudio: '" + singleAudio + "'");
		});

		audioServer.OnRemoveChannel((name) -> {
			System.out.println("channel removed! name: '" + name + "'");
		});

		audioServer.OnPong(() -> {
			System.out.println("pong!");
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
						
						audioServer.CreateChannel(input[1], input[2], Boolean.parseBoolean(input[3]));
						System.out.println("Creating channel...");
						break;
						
					case "removechannel":
						if (input.length < 2) {
							System.out.println("Required 1 paramater. name");
							break;
						}
	
						audioServer.RemoveChannel(input[1]);
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
						
						audioServer.PlayAudio(Arrays.asList(input[1]), input[2], input[3], input[4], startTime, looping);
						System.out.println("playing audio...");
						break;
						
					case "stop":
					case "stopaudio":
						if (input.length < 4) {
							System.out.println("Required at least 3 paramaters. uuid, channel, audioname");
							break;
						}
						
						audioServer.StopAudio(Arrays.asList(input[1]), input[2], input[3]);
						System.out.println("stopping audio...");
						break;
						
					case "stopchannel":
						if (input.length < 3) {
							System.out.println("Required at least 2 paramaters. uuid, channel");
							break;
						}
	
						audioServer.StopChannel(Arrays.asList(input[1]), Arrays.asList(input[2]));
						System.out.println("stopping all audio in channel...");
						break;
	
					case "stopall":
					case "stopallaudio":
						if (input.length < 2) {
							System.out.println("Required at least 1 paramater. uuid");
							break;
						}
	
						audioServer.StopAllAudio(Arrays.asList(input[1]));
						System.out.println("stopping all audio...");
						break;
						
					case "ping":
						audioServer.Ping();
						System.out.println("pinging...");
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