package ispb.base.radius.dictionary;


import ispb.base.radius.attribute.*;
import ispb.base.radius.exception.RadiusAttrAlreadyExist;
import ispb.base.radius.exception.RadiusAttrNotExist;
import ispb.base.radius.exception.RadiusVendorAlreadyExist;
import ispb.base.radius.exception.RadiusVendorNotExist;
import ispb.base.service.LogService;

import java.io.*;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class RadiusDictionaryReaderImpl implements RadiusDictionaryReader {

    private static final Map<String, Class<? extends RadiusAttribute>> types;
    private final LogService logService;

    static {
        types = new  ConcurrentHashMap<>();
        types.put("octets", RadiusOctetAttr.class);
        types.put("integer", RadiusIntegerAttr.class);
        types.put("string", RadiusStringAttr.class);
        types.put("ipaddr", RadiusIp4Attr.class);
        types.put("date", RadiusIntegerAttr.class);
    }

    public RadiusDictionaryReaderImpl(LogService logService){
        this.logService = logService;
    }


    public void readDictionary(InputStream source, RadiusDictionary dictionary){
        BufferedReader in = new BufferedReader(new InputStreamReader(source));

        try {
            String line;
            int lineNum = -1;
            while ((line = in.readLine()) != null) {

                lineNum++;
                line = line.trim();
                if (line.startsWith("#") || line.length() == 0)
                    continue;

                StringTokenizer tok = new StringTokenizer(line);
                if (!tok.hasMoreTokens())
                    continue;

                String lineType = tok.nextToken().trim();
                if (lineType.equalsIgnoreCase("ATTRIBUTE"))
                    parseAttributeLine(dictionary, tok, lineNum);
                else if (lineType.equalsIgnoreCase("VALUE"))
                    parseValueLine(dictionary, tok, lineNum);
                else if (lineType.equalsIgnoreCase("$INCLUDE"))
                    includeDictionaryFile(dictionary, tok, lineNum);
                else if (lineType.equalsIgnoreCase("VENDORATTR"))
                    parseVendorAttributeLine(dictionary, tok, lineNum);
                else if (lineType.equals("VENDOR"))
                    parseVendorLine(dictionary, tok, lineNum);
                else
                    logService.warn("Radius dictionary: unknown line type: " + lineType + " line: " + lineNum);
            }
        }
        catch (IOException e){
            logService.warn("Error while pars radius dictionary", e);
        }
    }

    public void readDictionary(String fileName, RadiusDictionary dictionary){
        File file = new File(fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            readDictionary(fis, dictionary);
        }
        catch (FileNotFoundException e){
            logService.warn("Radius dictionary: file '" + fileName + "' not found");
        }
    }

    public void readDefaultDictionary(RadiusDictionary dictionary){
        InputStream stream = this.getClass().getResourceAsStream("default_dictionary");
        readDictionary(stream, dictionary);
    }

    private void parseAttributeLine(RadiusDictionary dictionary, StringTokenizer tok, int lineNum){
        if (tok.countTokens() != 3) {
            logService.warn("Radius dictionary: syntax error on line " + lineNum);
            return;
        }

        String name = tok.nextToken().trim();
        int code = Integer.parseInt(tok.nextToken());
        String typeStr = tok.nextToken().trim();

        if(!types.containsKey(typeStr)){
            logService.warn("Radius dictionary: unknown attribute type " + typeStr + " line: " + lineNum);
            return;
        }

        try {
            dictionary.addAttribute(name, code, types.get(typeStr));
        }catch (RadiusAttrAlreadyExist e){
            logService.warn("Radius dictionary: attribute already exist line: " + lineNum);
        }
    }

    private void parseValueLine(RadiusDictionary dictionary, StringTokenizer tok, int lineNum){
        if (tok.countTokens() != 3) {
            logService.warn("Radius dictionary: syntax error on line " + lineNum);
            return;
        }

        String attrName = tok.nextToken().trim();
        String alias = tok.nextToken().trim();
        String value = tok.nextToken().trim();

        try {
            dictionary.addValue(attrName, alias, value);
        }
        catch (RadiusAttrNotExist e){
            logService.warn("Radius dictionary: attribute not exist " + attrName + " line: " + lineNum);
        }
    }

    private void parseVendorAttributeLine(RadiusDictionary dictionary, StringTokenizer tok, int lineNum){
        if (tok.countTokens() != 4) {
            logService.warn("Radius dictionary: syntax error on line " + lineNum);
            return;
        }

        String vendor = tok.nextToken().trim();
        Integer vendorId;
        String attrName = tok.nextToken().trim();
        int code = Integer.parseInt(tok.nextToken().trim());
        String typeStr = tok.nextToken().trim();

        if(!types.containsKey(typeStr)){
            logService.warn("Radius dictionary: unknown attribute type " + typeStr + " line: " + lineNum);
            return;
        }

        try {
            vendorId = Integer.parseInt(vendor);
        }
        catch (NumberFormatException e){
            vendorId = null;
        }

        if (vendorId != null)
            try {
                dictionary.addVendorAttribute(attrName, vendorId, code, types.get(typeStr));
            }
            catch (RadiusAttrAlreadyExist e){
                logService.warn("Radius dictionary: attribute already exist line: " + lineNum);
            }
        else
            try {
                dictionary.addVendorAttribute(attrName, vendor, code, types.get(typeStr));
            }
            catch (RadiusAttrAlreadyExist e){
                logService.warn("Radius dictionary: attribute already exist line: " + lineNum);
            }
            catch (RadiusVendorNotExist e){
                logService.warn("Radius dictionary: vendor not exist line: " + lineNum);
            }
    }

    private void parseVendorLine(RadiusDictionary dictionary, StringTokenizer tok, int lineNum){
        if (tok.countTokens() != 2) {
            logService.warn("Radius dictionary: syntax error on line " + lineNum);
            return;
        }

        int vendorId = Integer.parseInt(tok.nextToken().trim());
        String vendorName = tok.nextToken().trim();

        try {
            dictionary.addVendor(vendorName, vendorId);
        }
        catch (RadiusVendorAlreadyExist e){
            logService.warn("Radius dictionary: vendor already exist line: " + lineNum);
        }
    }

    private void includeDictionaryFile(RadiusDictionary dictionary, StringTokenizer tok, int lineNum){
        if (tok.countTokens() != 1){
            logService.warn("Radius dictionary: syntax error on line " + lineNum);
        }

        String includeFile = tok.nextToken();
        readDictionary(includeFile, dictionary);
    }
}
