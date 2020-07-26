package AudioServerApi;

import java.util.List;

import AudioServerApi.models.Channel;
import AudioServerApi.models.HostClient;
import AudioServerApi.models.PlayerClient;
import com.microsoft.signalr.Action;
import com.microsoft.signalr.Action1;
import com.microsoft.signalr.Action2;
import com.microsoft.signalr.Action3;
import com.microsoft.signalr.Action5;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.Subscription;
import io.reactivex.functions.Consumer;

public interface IAudioServerApi {
	
	/**
	 * Create a connection to the audiohub socket.
	 * 
	 * @param url The url with port to connect to. example: "http:localhost:5000/audiohub"
	 * @param authCode The authorization code
	 * @param name The <u>unique</u> name of the host
	 */
	void createConnection(String url, String authCode, String name);

	/**
	 * Stop the connection if connected.
	 */
	void stopConnection();
	
	/**
	 * An event that gets triggered when a client connects to the audiohub.
	 * 
	 * @param callback The callback containing the player client
	 */
	Subscription onClientConnect(Action1<PlayerClient> callback);
	
	/**
	 * An event that gets triggered when a client disconnects from the audiohub.
	 * 
	 * @param callback The callback containing the player client
	 */
	Subscription onClientDisconnect(Action1<PlayerClient> callback);
	
	/**
	 * An event that gets triggered when a host connects to the audiohub.
	 * 
	 * @param callback The callback containing the host client
	 */
	Subscription onHostConnect(Action1<HostClient> callback);
	
	/**
	 * An event that gets triggered when a host disconnects from the audiohub.
	 * 
	 * @param callback The callback containing the host client
	 */
	Subscription onHostDisconnect(Action1<HostClient> callback);
	
	
	/**
	 * Create a new channel. If a channel with the name already exists, this request is ignored.
	 * 
	 * @param name The unique name of the channel
	 * @param description The description of the channel. example: "Used for playing all regional music"
	 * @param singleAudio When true, only 1 audio can be played at all times on this channel. 
	 * 					  An existing audio will be overwritten when playing a new audio.
	 */
	void createChannel(String name, String description, boolean singleAudio);
	
	/**
	 * An event that gets triggered when a new channel is succesfully created.
	 * 
	 * @param callback The callback containing the channel
	 */
	Subscription onCreateChannel(Action1<Channel> callback);
	
	/**
	 * Remove a channel. WHen the channel is not found, this request is ignored.
	 * 
	 * @param name The unique name of the channel
	 */
	void removeChannel(String name);
	
	/**
	 * An event that gets triggered when a channel is removed.
	 * 
	 * @param callback The callback containing the channel
	 */
	Subscription onRemoveChannel(Action1<Channel> callback);
	
	/**
	 * Play an audio for some players on a specific channel.
	 * 
	 * @param uuids A list containing the players uuids
	 * @param channel The channel to play the audio on
	 * @param name The unique name of the audio. This name is used for stopping the audio
	 * @param fileLocation The file location url. example: "https://www.dropbox.com/s/8zmokvwmcr18dy"
	 * @param startTime The time to start the audio on. When -1 is given, it will be synchronized with all clients.
	 * @param looping When true, it will loop the audio until stopped
	 */
	void playAudio(List<String> uuids, String channel, String name, String fileLocation, double startTime, boolean looping);
	
	/**
	 * Stop an audio for some players on a specific channel
	 * 
	 * @param uuids A list containing the players uuids
	 * @param channel The channel to play the audio on
	 * @param audio The name of the audio
	 */
	void stopAudio(List<String> uuids, String channel, String audio);
	
	/**
	 * Stop all audio in a specific channel.
	 * 
	 * @param uuids A list containing the players uuids
	 * @param channels A list containing all the channel names
	 */
	void stopChannels(List<String> uuids, List<String> channels);
	
	/**
	 * Stop all audio.
	 * 
	 * @param uuids A list containing the players uuids
	 */
	void stopAllAudio(List<String> uuids);

	/**
	 * Set the background image for the audio page
	 *
	 * @param uuids A list containing the players uuids
	 * @param imageUrl The full URL of the image
	 */
	void setImageUrl(List<String> uuids, String imageUrl);

	/**
	 * Ping the audiohub!
	 *
	 * @param callback The callback containing the response message
	 */
	void ping(Consumer<String> callback);

	/**
	 * Get the channels from the hub.
	 *
	 * @param callback The callback containing the channels
	 */
	void getRemoteChannels(Consumer<Channel[]> callback);

	/**
	 * Get the connected player clients from the hub.
	 *
	 * @param callback The callback containing the connected player clients
	 */
	void getRemotePlayerClients(Consumer<PlayerClient[]> callback);

	/**
	 * Get the connected host clients from the hub.
	 *
	 * @param callback The callback containing the connected host clients
	 */
	void getRemoteHostClients(Consumer<HostClient[]> callback);

	/**
	 * Get the channels.
	 * This list is automatically synced in real time with the hub.
	 */
	List<Channel> getChannels();

	/**
	 * Get the connected player clients.
	 * This list is automatically synced in real time with the hub.
	 */
	List<PlayerClient> getPlayerClients();

	/**
	 * Get the connected host clients.
	 * This list is automatically synced in real time with the hub.
	 */
	List<HostClient> getHostClients();
	
	/**
	 * Get the connection object.
	 */
	HubConnection getConnection();
}
