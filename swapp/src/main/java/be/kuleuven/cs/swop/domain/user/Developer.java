package be.kuleuven.cs.swop.domain.user;


public class Developer extends User {

    public Developer(String name) {
        super(name);
    }

    // FIXME, use better values when you figure out how to use them.
    private static final int WORKDAY_START      = 7;
    private static final int BREAK_TIME         = 1;
    private static final int BREAK_PERIOD_START = 11;
    private static final int BREAK_PERIOD_END   = 14;
    private static final int WORKDAY_END        = 17;
}
