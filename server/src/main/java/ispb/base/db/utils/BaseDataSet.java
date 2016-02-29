package ispb.base.db.utils;

import ispb.base.utils.GsonGetter;

public class BaseDataSet {

    @Override
    public String toString() {
        return GsonGetter.get().toJson(this);
    }
}
