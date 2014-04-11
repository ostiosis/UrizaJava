package utility;

/**
 * 
 * @author Philip Lipman
 *
 */
public enum MediaObjectThumbnailType
{
	SMALL("small", 25, 25),
	MEDIUM("medium", 50, 50),
	LARGE("large", 100, 100);
	
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
