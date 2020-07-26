package AudioServerApi.Consoles;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import AudioServerApi.AudioServerApi;
import AudioServerApi.IAudioServerApi;
import AudioServerApi.models.Channel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class HostConsole {

	final static Logger logger = LogManager.getLogger(HostConsole.class);

	private static String url;
	private static String authCode;
	private static String name;

	private static Properties config;

	public static void main(String[] args) throws IOException {
		//org.apache.log4j.BasicConfigurator.configure();

		try {
			LoadConfigFile();
		} catch (FileNotFoundException ex) {
			CreateConfigFile();
			LoadConfigFile();
		}

		url = config.getProperty("url");
		authCode = config.getProperty("authCode");
		name = config.getProperty("name");

		logger.info(url);

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

		audioServer.onRemoveChannel((channel) -> {
			System.out.println("channel removed! " + channel);
		});

		System.out.println("Connected as a host! name: " + name);

		BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
		while (true) {

			try {
				String[] input = obj.readLine().split(" ");
				
				if (input.length <= 0)
					return;
			
				switch (input[0].toLowerCase()) {
					case "?":
					case "help":
						System.out.println(
								"Available commands: " +
								"info, " +
								"createchannel, " +
								"removechannel, " +
								"playaudio, " +
								"stopaudio, " +
								"stopchannel, " +
								"setimage, " +
								"getchannels, " +
								"getplayers, " +
								"gethosts, " +
								"ping");
						break;
					case "info":
						System.out.println(audioServer.getChannels());
						System.out.println(audioServer.getPlayerClients());
						System.out.println(audioServer.getHostClients());
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
	
						audioServer.stopChannels(Arrays.asList(input[1]), Arrays.asList(input[2]));
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
						audioServer.getRemoteChannels(channels -> {
							for (Channel channel : channels) {
								System.out.println(channel);
							}
						});
						break;

					case "getplayers":
					case "getplayerclients":
						System.out.println("getting player clients...");
						audioServer.getRemotePlayerClients(playerClients -> System.out.println(playerClients));
						break;

					case "gethosts":
					case "gethostclients":
						System.out.println("getting host clients...");
						audioServer.getRemoteHostClients(hostClients -> System.out.println(hostClients));
						break;

					case "ping":
						System.out.println("pinging...");
						audioServer.ping(callback -> System.out.println(callback));
						break;

					case "setimage":
						if (input.length < 2) {
							System.out.println("Required at least 2 paramaters. uuid, url");
							break;
						}
						System.out.println("setting image...");
						audioServer.setImageUrl(Arrays.asList(input[1]), input[2]);
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

	private static void LoadConfigFile() throws IOException {
		File file = new File("connection.properties");
		FileInputStream is = new FileInputStream(file);

		config = new Properties();
		config.load(is);
	}

	private static void CreateConfigFile() throws IOException {
		File newFile = new File("connection.properties");
		File fileTemplate = new File("src/main/resources/connection.template.properties");
		if (newFile.createNewFile()) {
			logger.info("No config file found, created a new one...");

			FileWriter writer = new FileWriter(newFile);
			Scanner reader = new Scanner(fileTemplate);
			while(reader.hasNextLine()) {
				writer.write(reader.nextLine() + "\n");
			}
			writer.close();
			reader.close();
		}
	}
}
