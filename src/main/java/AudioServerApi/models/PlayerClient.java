package AudioServerApi.models;

import java.util.Objects;

public class PlayerClient {
    public final String connectionId;
    public final String uuid;
    public final String username;

    public PlayerClient(String connectionId, String uuid, String username) {
        this.connectionId = connectionId;
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public String toString() {
        return "PlayerClient{" +
                "ConnectionId='" + connectionId + '\'' +
                ", UUID='" + uuid + '\'' +
                ", Username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerClient that = (PlayerClient) o;
        return Objects.equals(connectionId, that.connectionId);
    }
}
