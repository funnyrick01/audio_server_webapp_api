package AudioServerApi.models;

import java.util.Objects;

public class HostClient {
    public final String connectionId;
    public final String name;

    public HostClient(String connectionId, String name) {
        this.connectionId = connectionId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "HostClient{" +
                "ConnectionId='" + connectionId + '\'' +
                ", Name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HostClient that = (HostClient) o;
        return Objects.equals(connectionId, that.connectionId);
    }
}
