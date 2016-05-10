package ispb.frontend.rest.resource;


import ispb.base.frontend.rest.RestContext;
import ispb.base.frontend.rest.RestEntity;
import ispb.base.frontend.rest.RestResource;
import ispb.base.frontend.rest.RestResponse;
import ispb.base.frontend.utils.AccessLevel;
import org.tinyradius.dictionary.WritableRadiusDictionary;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RadiusDictionaryResource extends RestResource {

    private static class RadiusDictionaryEntity extends RestEntity {

        private final String attributeName;

        public RadiusDictionaryEntity(long id, String attributeName){
            setId(id);
            this.attributeName = attributeName;
        }

        public boolean verify(){
            return false;
        }
    }

    private static class RadiusDictionaryRestResponse extends RestResponse {

        private final List<RadiusDictionaryEntity> radiusAttributeList = new LinkedList<>();

        public RadiusDictionaryRestResponse(Set<String> attributeSet){
            long i = 0;
            for (String attributeName: attributeSet)
                radiusAttributeList.add(new RadiusDictionaryEntity(++i, attributeName));
        }
    }


    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType(){
        return RadiusDictionaryEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        WritableRadiusDictionary radiusDictionary = getRadiusDictionary(restContext);
        return new RadiusDictionaryRestResponse(radiusDictionary.getAttributeNamesSet());
    }

    private WritableRadiusDictionary getRadiusDictionary(RestContext restContext){
        return restContext.getApplication().getByType(WritableRadiusDictionary.class);
    }
}
