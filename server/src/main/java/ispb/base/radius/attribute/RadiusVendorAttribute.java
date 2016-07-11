package ispb.base.radius.attribute;


public interface RadiusVendorAttribute extends RadiusAttribute {
    int getVendorId();
    int getVendorType();

    String getVendorName();
}
