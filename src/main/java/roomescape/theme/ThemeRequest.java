package roomescape.theme;

public class ThemeRequest {
	private String name;

	private String description;

	private String thumbnail;

	public ThemeRequest(String name, String description, String thumbnail) {
		this.name = name;
		this.description = description;
		this.thumbnail = thumbnail;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getThumbnail() {
		return thumbnail;
	}
}
