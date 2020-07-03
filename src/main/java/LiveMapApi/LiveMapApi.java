package LiveMapApi;

import java.util.List;

import com.microsoft.signalr.Action1;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.Subscription;

public class LiveMapApi {
	
	private static LiveMapApi instance = new LiveMapApi();
	
	public static LiveMapApi getInstance() {
		return instance;
	}
	
	
	private HubConnection connection;
	
	public void CreateConnection(String url, String authCode, String name) {
		connection = HubConnectionBuilder.create(url + "?authCode=" + authCode + "&name=" + name).build();
		
		connection.start().blockingAwait();
	}

	
	public void AddOrUpdatePlayers(List<PlayerLocation> playerLocations) {
		connection.send("AddOrUpdatePlayers", playerLocations);
	}
	
	public void RemovePlayers(List<String> uuids) {
		connection.send("RemovePlayers", uuids);
	}
	
	public void GetPlayerLocations() {
		connection.send("GetPlayerLocations");
	}
	
	@SuppressWarnings("unchecked")
	public Subscription OnGetPlayerLocations(Action1<List<PlayerLocation>> action) {
		return connection.on("GetPlayerLocations", action, (Class<List<PlayerLocation>>)(Class<?>)List.class);
	}
}
