package AudioServerApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import AudioServerApi.models.Channel;
import AudioServerApi.models.HostClient;
import AudioServerApi.models.PlayerClient;
import com.microsoft.signalr.Action1;
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

	private final List<Channel> channels = new ArrayList<>();
	private final List<PlayerClient> playerClients = new ArrayList<>();
	private final List<HostClient> hostClients = new ArrayList<>();

	public void createConnection(String url, String authCode, String name) {
		connection = HubConnectionBuilder.create(url + "?authCode=" + authCode + "&name=" + name).build();

		connection.start().doOnError(Throwable::printStackTrace).doOnComplete(() -> System.out.println("Connection successfully made to: " + url + " as " + name)).blockingAwait();

		getRemoteChannels(channels -> Collections.addAll(this.channels, channels));
		getRemotePlayerClients(playerClients -> Collections.addAll(this.playerClients, playerClients));
		getRemoteHostClients(hostClients -> Collections.addAll(this.hostClients, hostClients));
	}

	public void stopConnection() {
		connection.stop().blockingAwait();

		channels.clear();
		playerClients.clear();
		hostClients.clear();
	}
	
	public Subscription onClientConnect(Action1<PlayerClient> action) {
		return connection.on("ClientConnect", playerClient -> {
			this.playerClients.add(playerClient);
			action.invoke(playerClient);
		}, PlayerClient.class);
	}
	
	public Subscription onClientDisconnect(Action1<PlayerClient> action) {
		return connection.on("ClientDisconnect", playerClient -> {
			int index = this.playerClients.indexOf(playerClient);
			if (index >= 0)
				this.playerClients.remove(index);
			action.invoke(playerClient);
		}, PlayerClient.class);
	}
	
	public Subscription onHostConnect(Action1<HostClient> action) {
		return connection.on("HostConnect", hostClient -> {
			this.hostClients.add(hostClient);
			action.invoke(hostClient);
		}, HostClient.class);
	}
	
	public Subscription onHostDisconnect(Action1<HostClient> action) {
		return connection.on("HostDisconnect", hostClients -> {
			int index = this.hostClients.indexOf(hostClients);
			if (index >= 0)
				this.hostClients.remove(index);
			action.invoke(hostClients);
		}, HostClient.class);
	}
	
	
	public void createChannel(String name, String description, boolean singleAudio) {
		connection.send("CreateChannel", name, description, singleAudio);
	}
	
	public Subscription onCreateChannel(Action1<Channel> action) {
		return connection.on("CreateChannel", channel -> {
			this.channels.add(channel);
			action.invoke(channel);
		}, Channel.class);
	}
	
	public void removeChannel(String name) {
		connection.send("RemoveChannel", name);
	}
	
	public Subscription onRemoveChannel(Action1<Channel> action) {
		return connection.on("RemoveChannel", channel -> {
			int index = this.channels.indexOf(channel);
			if (index >= 0)
				this.channels.remove(index);
			action.invoke(channel);
		}, Channel.class);
	}
	
	public void playAudio(List<String> uuids, String channel, String name, String fileLocation, double startTime, boolean looping) {
		connection.send("PlayAudio", uuids, channel, name, fileLocation, startTime, looping);
	}
	
	public void stopAudio(List<String> uuids, String channel, String name) {
		connection.send("StopAudio", uuids, channel, name);
	}

	public void stopChannels(List<String> uuids, List<String> channels) {
		connection.send("StopChannel", uuids, channels);
	}

	public void stopAllAudio(List<String> uuids) {
		connection.send("StopAllAudio", uuids);
	}

	public void ping(Consumer<String> callback) {
		connection.invoke(String.class, "Ping").subscribe(callback);
	}

	public void getRemoteChannels(Consumer<Channel[]> callback) {
		connection.invoke(Channel[].class, "GetChannels").subscribe(callback);
	}

	public void getRemotePlayerClients(Consumer<PlayerClient[]> callback) {
		connection.invoke(PlayerClient[].class, "GetPlayerClients").subscribe(callback);
	}

	public void getRemoteHostClients(Consumer<HostClient[]> callback) {
		connection.invoke(HostClient[].class, "GetHosts").subscribe(callback);
	}

	public List<Channel> getChannels() {
		return this.channels;
	}

	public List<PlayerClient> getPlayerClients() {
		return this.playerClients;
	}

	public List<HostClient> getHostClients() {
		return this.hostClients;
	}

	public HubConnection getConnection() {
		return connection;
	}
}
