package ispb.base.radius.dictionary;


import java.io.InputStream;

public interface RadiusDictionaryReader {
    void readDictionary(InputStream in, RadiusDictionary dictionary);
    void readDictionary(String inFile, RadiusDictionary dictionary);
    void readDefaultDictionary(RadiusDictionary dictionary);
}
