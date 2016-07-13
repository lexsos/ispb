package ispb.base.radius.dictionary;


import ispb.base.radius.attribute.RadiusAttribute;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("WeakerAccess")
public class AttributeType {

    protected final Class<? extends RadiusAttribute> clazz;
    protected final String attributeName;
    protected Map<String, String> valueToAlias;
    protected Map<String, String> aliasToValue;
    protected final int type;

    public AttributeType(String attributeName, int type, Class<? extends RadiusAttribute> clazz){
        this.attributeName = attributeName;
        this.type = type;
        this.clazz = clazz;
    }

    public int getType(){
        return type;
    }

    public String getAttributeName(){
        return attributeName;
    }

    public void addValue(String alias, String value){
        if (valueToAlias == null)
            valueToAlias = new ConcurrentHashMap<>();
        if (aliasToValue == null)
            aliasToValue = new ConcurrentHashMap<>();

        valueToAlias.put(value, alias);
        aliasToValue.put(alias, value);
    }

    public String getValue(String alias){
        if (aliasToValue != null && aliasToValue.containsKey(alias))
            return aliasToValue.get(alias);
        return null;
    }

    public String getAlias(String value){
        if (valueToAlias != null && valueToAlias.containsKey(value))
            return valueToAlias.get(value);
        return null;
    }

    public Set<String> getAliasSet(){
        if (aliasToValue != null)
            return aliasToValue.keySet();
        return null;
    }

    public RadiusAttribute newInstance(){
        try {
            RadiusAttribute attribute = clazz.newInstance();
            attribute.setType(type);
            attribute.setAttributeType(this);
            return attribute;
        }catch (Throwable throwable){
            return null;
        }
    }
}
