package ispb.base.radius;


import ispb.base.radius.attribute.RadiusAttributeContainer;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class RadiusAttributeList implements Iterable<RadiusAttributeContainer> {

    private final Map<String, List<String>> attributes = new HashMap<>();

    private class RadiusAttributeListIterator implements Iterator<RadiusAttributeContainer> {

        private final Iterator<String> names;
        private Iterator<String> values;
        private String currentName;

        private class Attribute implements RadiusAttributeContainer {

            private final String name;
            private final String value;

            public Attribute(String name, String value){
                this.name = name;
                this.value = value;
            }

            public String getAttributeName(){
                return name;
            }

            public String getAttributeValue(){
                return value;
            }
        }

        public RadiusAttributeListIterator(){
            names = attributes.keySet().iterator();
        }

        public boolean hasNext(){
            if (values != null && values.hasNext())
                return true;

            while (names.hasNext()){
                currentName = names.next();
                values = attributes.get(currentName).iterator();
                if (values.hasNext())
                    return true;
            }

            return false;
        }

        public RadiusAttributeContainer next(){
            if (hasNext())
                return new Attribute(currentName, values.next());
            throw new NoSuchElementException();
        }
    }

    public void addAttribute(String attributeName, String attributeValue){
        if (!attributes.containsKey(attributeName))
            attributes.put(attributeName, new LinkedList<>());
        attributes.get(attributeName).add(attributeValue);
    }

    public void addAttribute(RadiusAttributeContainer attribute){
        addAttribute(attribute.getAttributeName(), attribute.getAttributeValue());
    }

    @SuppressWarnings("Convert2streamapi")
    public void addAttributeList(List<? extends RadiusAttributeContainer> attributeList){
        for (RadiusAttributeContainer attribute: attributeList)
            addAttribute(attribute);
    }

    public boolean containsAttribute(String attributeName){
        return attributes.containsKey(attributeName);
    }

    public Iterator<RadiusAttributeContainer> iterator(){
        return new RadiusAttributeListIterator();
    }
}
