package LiveMapApi.Consoles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import LiveMapApi.LiveMapApi;
import LiveMapApi.PlayerLocation;

public class HostConsole {

	private static String url = "http://localhost:5000/mapHub";
	private static String authCode = "2806d01cdd3c4bfc80b751e9cec02110293a30354ebb48678147b800cc95b9c4";
	private static String name = "HostConsole" + Math.random();

	public static void main(String[] args) throws IOException {
		BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));

		LiveMapApi liveMap = LiveMapApi.getInstance();

		liveMap.CreateConnection(url, authCode, name);

		liveMap.OnGetPlayerLocations((locations) -> {
			System.out.println("OnGetPlayerLocations: '" + locations + "'");
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
						System.out.println("Available commands: addplayer, getplayerlocations, removeplayer");
						break;
					case "addplayer":
					case "updateplayer":
						if (input.length < 4) {
							System.out.println("Required 3 paramaters. uuid, x and z");
							break;
						}
						List<PlayerLocation> list = new ArrayList<>();
						list.add(new PlayerLocation(input[1], Double.parseDouble(input[2]), Double.parseDouble(input[3])));
						
						liveMap.AddOrUpdatePlayers(list);
						System.out.println("Adding player");
						break;
						
					case "getplayerlocations":
						liveMap.GetPlayerLocations();
						break;
						
					case "removeplayer":
						liveMap.RemovePlayers(Arrays.asList(input[1]));
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
