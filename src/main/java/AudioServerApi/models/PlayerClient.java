package AudioServerApi.models;

public class PlayerClient {
    public final String ConnectionId;
    public final String UUID;
    public final String Username;

    public PlayerClient(String connectionId, String uuid, String username) {
        ConnectionId = connectionId;
        UUID = uuid;
        Username = username;
    }

    @Override
    public String toString() {
        return "PlayerClient{" +
                "ConnectionId='" + ConnectionId + '\'' +
                ", UUID='" + UUID + '\'' +
                ", Username='" + Username + '\'' +
                '}';
    }
}
