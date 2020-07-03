package AudioServerApi;

import java.util.List;

import com.microsoft.signalr.Action;
import com.microsoft.signalr.Action1;
import com.microsoft.signalr.Action2;
import com.microsoft.signalr.Action3;
import com.microsoft.signalr.Action5;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.Subscription;

public interface IAudioServerApi {
	
	/**
	 * Create a connection to the audiohub socket.
	 * 
	 * @param url The url with port to connect to. example: "http:localhost:5000/audiohub"
	 * @param authCode The authorization code
	 * @param name The <u>unique</u> name of the host
	 */
	public void CreateConnection(String url, String authCode, String name);
	
	/**
	 * An event that gets triggered when a client connects to the audiohub.
	 * 
	 * @param callback The callback containing the unique connection ID and uuid
	 */
	public Subscription OnClientConnect(Action2<String, String> callback);
	
	/**
	 * An event that gets triggered when a client disconnects from the audiohub.
	 * 
	 * @param callback The callback containing the unique connection ID and uuid
	 */
	public Subscription OnClientDisconnect(Action2<String, String> callback);
	
	/**
	 * An event that gets triggered when a host connects to the audiohub.
	 * 
	 * @param callback The callback containing the unique host name
	 */
	public Subscription OnHostConnect(Action1<String> callback);
	
	/**
	 * An event that gets triggered when a host disconnects from the audiohub.
	 * 
	 * @param callback The callback containing the unique host name
	 */
	public Subscription OnHostDisconnect(Action1<String> callback);
	
	
	/**
	 * Create a new channel. If a channel with the name already exists, this request is ignored.
	 * 
	 * @param name The unique name of the channel
	 * @param description The description of the channel. example: "Used for playing all regional music"
	 * @param singleAudio When true, only 1 audio can be played at all times on this channel. 
	 * 					  An existing audio will be overwritten when playing a new audio.
	 */
	public void CreateChannel(String name, String description, boolean singleAudio);
	
	/**
	 * An event that gets triggered when a new channel is succesfully created.
	 * 
	 * @param callback The callback containing the name, description and singleAudio
	 */
	public Subscription OnCreateChannel(Action3<String, String, Boolean> callback);
	
	/**
	 * Remove a channel. WHen the channel is not found, this request is ignored.
	 * 
	 * @param name The unique name of the channel
	 */
	public void RemoveChannel(String name);
	
	/**
	 * An event that gets triggered when a channel is removed.
	 * 
	 * @param callback The callback containing the channel name
	 */
	public Subscription OnRemoveChannel(Action1<String> callback);
	
	
	/**
	 * Play an audio for some players on a specific channel.
	 * 
	 * @param uuids A list containing the players uuids
	 * @param channel The channel to play the audio on
	 * @param name The unique name of the audio. This name is used for stopping the audio
	 * @param fileLocation The file location url. example: "https://www.dropbox.com/s/8zmokvwmcr18dy"
	 * @param startTime The time to start the audio on. When -1 is given, it will be synchronized with all clients.=
	 * @param looping When true, it will loop the audio until stopped
	 */
	public void PlayAudio(List<String> uuids, String channel, String name, String fileLocation, double startTime, boolean looping);
	
	/**
	 * Stop an audio for some players on a specific channel
	 * 
	 * @param uuids A list containing the players uuids
	 * @param channel The channel to play the audio on
	 * @param audio The name of the audio
	 */
	public void StopAudio(List<String> uuids, String channel, String audio);
	
	/**
	 * Stop all audio in a specific channel.
	 * 
	 * @param uuids A list containing the players uuids
	 * @param channels A list containing all the channel names
	 */
	public void StopChannel(List<String> uuids, List<String> channels);
	
	/**
	 * Stop all audio.
	 * 
	 * @param uuids A list containing the players uuids
	 */
	public void StopAllAudio(List<String> uuids);
	
	
	/**
	 * Ping the audiohub!
	 * <p>
	 * When successful it will trigger the {@link IAudioServerApi#OnPong(Action)} event directly after.
	 */
	public void Ping();
	
	/**
	 * An event that gets triggered when the audio hub is pinged.
	 * 
	 * @param callback Pong!
	 */
	public Subscription OnPong(Action callback);
	
	/**
	 * Get the connection object.
	 */
	public HubConnection getConnection();
	
	
	// ######################################### //
	// 			!!! Client methods !!!           //
	// ######################################### //
	
	
	public Subscription OnPlayAudio(Action5<String, String, String, Double, Boolean> callback);
	
	public Subscription OnStopAudio(Action2<String, String> callback);
	
	public Subscription OnStopChannels(Action1<String> callback);
	
	public Subscription OnStopAll(Action callback);
}
