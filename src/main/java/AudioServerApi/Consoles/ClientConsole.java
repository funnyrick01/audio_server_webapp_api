package AudioServerApi.Consoles;

import java.io.IOException;

import AudioServerApi.AudioServerApi;

public class ClientConsole {
	
	private static String url = "http://localhost:5000/audioHub";
	private static String uuid = "069a79f4-44e9-4726-a5be-fca90e38aaf5";

	public static void main(String[] args) throws IOException {
		AudioServerApi audioServer = (AudioServerApi) AudioServerApi.getInstance();

		audioServer.CreateClientConnection(url, uuid);
		
		audioServer.onCreateChannel((channel) -> {
			System.out.println("[OnCreateChannel] " + channel);
		});
		
		audioServer.onRemoveChannel((channel) -> {
			System.out.println("[OnRemoveChannel] " + channel);
		});
		
		audioServer.OnPlayAudio((channel, name, fileLocation, startTime, looping) -> {
			System.out.println("[OnPlayAudio] " + String.join(", ", channel, name, fileLocation, startTime.toString(), looping.toString()));
		});
		
		audioServer.OnStopAudio((channel, name)-> {
			System.out.println("[OnStopAudio] " + String.join(", ", channel, name));
		});
		
		audioServer.OnStopChannels((channel) -> {
			System.out.println("[OnStopChannels] " + String.join(", ", channel));
		});
		
		audioServer.OnStopAll(() -> {
			System.out.println("[OnStopChannel] no paramaters...");
		});
		

		System.out.println("Connected as a client! uuid: " + uuid);
	}
}
