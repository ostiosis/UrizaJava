package utility;

public enum MediaObjectThumbnailType
{
	SMALL("small", 100, 50),
	MEDIUM("medium", 200, 100),
	LARGE("large", 400, 200);
	
	private final String name;
	private final Integer width;
	private final Integer height;
	
	MediaObjectThumbnailType(String name, Integer width, Integer height)
	{
		this.name = name;
		this.width = width;
		this.height = height;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Integer getWidth()
	{
		return width;
	}
	
	public Integer getHeight()
	{
		return height;
	}
}
