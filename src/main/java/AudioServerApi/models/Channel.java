package AudioServerApi.models;

import java.util.Objects;

public class Channel {
    public final String name;
    public final String description;
    public final Boolean singleAudio;

    public Channel(String name, String description, Boolean singleAudio) {
        this.name = name;
        this.description = description;
        this.singleAudio = singleAudio;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "Name='" + name + '\'' +
                ", Description='" + description + '\'' +
                ", SingleAudio=" + singleAudio +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(name, channel.name);
    }
}
