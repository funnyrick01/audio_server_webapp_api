package AudioServerApi.models;

public class HostClient {
    public final String ConnectionId;
    public final String Name;

    public HostClient(String connectionId, String name) {
        ConnectionId = connectionId;
        Name = name;
    }

    @Override
    public String toString() {
        return "HostClient{" +
                "ConnectionId='" + ConnectionId + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}
