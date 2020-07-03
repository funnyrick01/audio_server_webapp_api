package AudioServerApi;

import java.util.List;

import AudioServerApi.models.Channel;
import AudioServerApi.models.HostClient;
import AudioServerApi.models.PlayerClient;
import LiveMapApi.PlayerLocation;
import com.microsoft.signalr.Action;
import com.microsoft.signalr.Action1;
import com.microsoft.signalr.Action2;
import com.microsoft.signalr.Action3;
import com.microsoft.signalr.Action5;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.Subscription;
import io.reactivex.functions.Consumer;

public class AudioServerApi implements IAudioServerApi {
	
	private static IAudioServerApi instance = new AudioServerApi();
	
	public static IAudioServerApi getInstance() {
		return instance;
	}
	
	private HubConnection connection;
	
	public void createConnection(String url, String authCode, String name) {
		connection = HubConnectionBuilder.create(url + "?authCode=" + authCode + "&name=" + name).build();
		
		connection.start().blockingAwait();
	}
	
	public void CreateClientConnection(String url, String uuid) {
		connection = HubConnectionBuilder.create(url + "?uuid=" + uuid).build();
		
		connection.start().blockingAwait();
	}

	public void stopConnection() {
		connection.stop().blockingAwait();
	}
	
	public Subscription onClientConnect(Action1<PlayerClient> action) {
		return connection.on("ClientConnect", action, PlayerClient.class);
	}
	
	public Subscription onClientDisconnect(Action1<PlayerClient> action) {
		return connection.on("ClientDisconnect", action, PlayerClient.class);
	}
	
	public Subscription onHostConnect(Action1<HostClient> action) {
		return connection.on("HostConnect", action, HostClient.class);
	}
	
	public Subscription onHostDisconnect(Action1<HostClient> action) {
		return connection.on("HostDisconnect", action, HostClient.class);
	}
	
	
	public void createChannel(String name, String description, boolean singleAudio) {
		connection.send("CreateChannel", name, description, singleAudio);
	}
	
	public Subscription onCreateChannel(Action1<Channel> action) {
		return connection.on("CreateChannel", action, Channel.class);
	}
	
	public void removeChannel(String name) {
		connection.send("RemoveChannel", name);
	}
	
	public Subscription onRemoveChannel(Action1<Channel> action) {
		return connection.on("RemoveChannel", action, Channel.class);
	}
	
	public void playAudio(List<String> uuids, String channel, String name, String fileLocation, double startTime, boolean looping) {
		connection.send("PlayAudio", uuids, channel, name, fileLocation, startTime, looping);
	}
	
	public void stopAudio(List<String> uuids, String channel, String name) {
		connection.send("StopAudio", uuids, channel, name);
	}

	public void stopChannel(List<String> uuids, List<String> channels) {
		connection.send("StopChannel", uuids, channels);
	}

	public void stopAllAudio(List<String> uuids) {
		connection.send("StopAllAudio", uuids);
	}

	public void getChannels(Consumer<List<Channel>> callback) {
		connection.invoke((Class<List<Channel>>)(Class<?>)List.class, "GetChannels").subscribe(callback);
	}

	public void getPlayerClients(Consumer<List<PlayerClient>> callback) {
		connection.invoke((Class<List<PlayerClient>>)(Class<?>)List.class, "GetPlayerClients").subscribe(callback);
	}

	public void getHostClients(Consumer<List<HostClient>> callback) {
		connection.invoke((Class<List<HostClient>>)(Class<?>)List.class, "GetHosts").subscribe(callback);
	}


	public void ping(Consumer<String> callback) {
		connection.invoke(String.class, "Ping").subscribe(callback);
	}

	public HubConnection getConnection() {
		return connection;
	}

	public Subscription OnPlayAudio(Action5<String, String, String, Double, Boolean> action) {
		return connection.on("PlayAudio", action, String.class, String.class, String.class, Double.class, Boolean.class);
	}

	public Subscription OnStopAudio(Action2<String, String> action) {
		return connection.on("StopAudio", action, String.class, String.class);
	}

	public Subscription OnStopChannels(Action1<String> action) {
		return connection.on("StopChannels", action, String.class);
	}

	public Subscription OnStopAll(Action action) {
		return connection.on("StopAll", action);
	}
}
