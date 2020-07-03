package AudioServerApi;

import java.util.List;

import com.microsoft.signalr.Action;
import com.microsoft.signalr.Action1;
import com.microsoft.signalr.Action2;
import com.microsoft.signalr.Action3;
import com.microsoft.signalr.Action5;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.Subscription;

public class AudioServerApi implements IAudioServerApi {
	
	private static IAudioServerApi instance = new AudioServerApi();
	
	public static IAudioServerApi getInstance() {
		return instance;
	}
	
	
	private HubConnection connection;
	
	public void CreateConnection(String url, String authCode, String name) {
		connection = HubConnectionBuilder.create(url + "?authCode=" + authCode + "&name=" + name).build();
		
		connection.start().blockingAwait();
	}
	
	public void CreateClientConnection(String url, String uuid) {
		connection = HubConnectionBuilder.create(url + "?uuid=" + uuid).build();
		
		connection.start().blockingAwait();
	}
	
	public Subscription OnClientConnect(Action2<String, String> action) {
		return connection.on("ClientConnect", action, String.class, String.class);
	}
	
	public Subscription OnClientDisconnect(Action2<String, String> action) {
		return connection.on("ClientDisconnect", action, String.class, String.class);
	}
	
	public Subscription OnHostConnect(Action1<String> action) {
		return connection.on("HostConnect", action, String.class);
	}
	
	public Subscription OnHostDisconnect(Action1<String> action) {
		return connection.on("HostDisconnect", action, String.class);
	}
	
	
	public void CreateChannel(String name, String description, boolean singleAudio) {
		connection.send("CreateChannel", name, description, singleAudio);
	}
	
	public Subscription OnCreateChannel(Action3<String, String, Boolean> action) {
		return connection.on("CreateChannel", action, String.class, String.class, Boolean.class);
	}
	
	public void RemoveChannel(String name) {
		connection.send("RemoveChannel", name);
	}
	
	public Subscription OnRemoveChannel(Action1<String> action) {
		return connection.on("RemoveChannel", action, String.class);
	}
	
	public void PlayAudio(List<String> uuids, String channel, String name, String fileLocation, double startTime, boolean looping) {
		connection.send("PlayAudio", uuids, channel, name, fileLocation, startTime, looping);
	}
	
	public void StopAudio(List<String> uuids, String channel, String name) {
		connection.send("StopAudio", uuids, channel, name);
	}

	public void StopChannel(List<String> uuids, List<String> channels) {
		connection.send("StopChannel", uuids, channels);
	}

	public void StopAllAudio(List<String> uuids) {
		connection.send("StopAllAudio", uuids);
	}
	
	
	public void Ping() {
		connection.send("Ping");
	}
	
	public Subscription OnPong(Action action) {
		return connection.on("Pong", action);
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
