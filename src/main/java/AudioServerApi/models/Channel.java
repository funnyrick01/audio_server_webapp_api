package AudioServerApi.models;

public class Channel {
    public final String Name;
    public final String Description;
    public final Boolean SingleAudio;

    public Channel(String name, String description, Boolean singleAudio) {
        Name = name;
        Description = description;
        SingleAudio = singleAudio;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", SingleAudio=" + SingleAudio +
                '}';
    }
}
