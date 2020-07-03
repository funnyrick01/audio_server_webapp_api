package LiveMapApi;

public class PlayerLocation {
	
	public final String UUID;
	public final double X;
	public final double Z;
	
	public PlayerLocation(String uuid, double x, double z) {
		this.UUID = uuid;
		this.X = x;
		this.Z = z;
	}
}
