package audio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceLoader {
	public static InputStream load(
			Class<?> clazz, String filePath ) {
			// try the resource first
			InputStream in = null;
			
			if( !( filePath == null || filePath.isEmpty() ) ) {
				// try the file path
				try {
					in = new FileInputStream( filePath );
				} catch( FileNotFoundException e ) {
					e.printStackTrace();
				}
			}
		return in;
	}
}
