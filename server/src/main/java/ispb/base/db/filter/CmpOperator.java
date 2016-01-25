package ispb.base.db.filter;


public enum CmpOperator {

    EQ {
        public String toHql(){ return "="; }
        public boolean hasArgument() { return true; }
    },
    NOT_EQ {
        public String toHql(){ return "!="; }
        public boolean hasArgument() { return true; }
    },
    GT {
        public String toHql(){ return ">"; }
        public boolean hasArgument() { return true; }
    },
    GT_EQ {
        public String toHql(){ return ">="; }
        public boolean hasArgument() { return true; }
    },
    LT {
        public String toHql(){ return "<"; }
        public boolean hasArgument() { return true; }
    },
    LT_EQ {
        public String toHql(){ return "<"; }
        public boolean hasArgument() { return true; }
    },
    LIKE {
        public String toHql(){ return "like"; }
        public boolean hasArgument() { return true; }
    },
    IS_NULL {
        public String toHql(){ return "is null"; }
        public boolean hasArgument() { return false; }
    }
    ;

    public abstract String toHql();
    public abstract boolean hasArgument();
}
