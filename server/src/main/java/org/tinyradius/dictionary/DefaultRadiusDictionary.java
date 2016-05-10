/**
 * $Id: DefaultRadiusDictionary.java,v 1.1 2005/09/04 22:11:00 wuttke Exp $
 * Created on 28.08.2005
 * @author mw
 * @version $Revision: 1.1 $
 */
package org.tinyradius.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The default dictionary is a singleton object containing
 * a dictionary in the memory that is filled on application
 * startup using the default dictionary file from the
 * classpath resource
 * <code>org.tinyradius.dictionary.default_dictionary</code>.
 */
public class DefaultRadiusDictionary
extends MemoryDictionary{

	/**
	 * Returns the singleton instance of this object.
	 * @return DefaultRadiusDictionary instance
	 */
	public static WritableRadiusDictionary getDefaultDictionary() {
		if (instance == null){
			try {
				instance = new DefaultRadiusDictionary();
				InputStream source = DefaultRadiusDictionary.class.getClassLoader().getResourceAsStream(DICTIONARY_RESOURCE);
				DictionaryParser.parseDictionary(source, instance);
			} catch (IOException e) {
				throw new RuntimeException("default dictionary unavailable", e);
			}
		}
		return instance;
	}

	public static void addFromFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists())
			throw new IOException("inclueded file '" + fileName + "' not found");

		FileInputStream fis = new FileInputStream(file);
		DictionaryParser.parseDictionary(fis, getDefaultDictionary());
	}
	
	/**
	 * Make constructor private so that a DefaultRadiusDictionary
	 * cannot be constructed by other classes. 
	 */
	private DefaultRadiusDictionary() {
	}
	
	private static final String DICTIONARY_RESOURCE = "org/tinyradius/dictionary/default_dictionary";
	private static DefaultRadiusDictionary instance = null;
}