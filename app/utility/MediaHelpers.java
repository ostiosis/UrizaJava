package utility;

import java.io.InputStream;

/**
 * 
 * @author Philip Lipman
 *
 */
public class MediaHelpers
{
	public static String guessContentTypeFromStream(InputStream is) throws java.io.IOException
    {
		is.mark(10);
		int c1 = is.read();
		int c2 = is.read();
		int c3 = is.read();
		int c4 = is.read();
		/**
		int c5 = is.read();
		int c6 = is.read();
		int c7 = is.read();
		int c8 = is.read();
		/**/
		is.reset();
		if (c1 == 0xFF && c2 == 0xD8 && c3 == 0xFF && c4 == 0xE1)
		{
			return "image/jpeg";
		}
		
		return java.net.URLConnection.guessContentTypeFromStream(is);
    }
}
