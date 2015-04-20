package be.kuleuven.cs.swop.domain.company.user;


@SuppressWarnings("serial")
public class Developer extends User {

    public Developer(String name) {
        super(name);
    }

    // FIXME, use better values when you figure out how to use them.
    // Also, change them to getters, in case we might want them to be
    // different for different developers.
    private static final int WORKDAY_START      = 7;
    private static final int BREAK_TIME         = 1;
    private static final int BREAK_PERIOD_START = 11;
    private static final int BREAK_PERIOD_END   = 14;
    private static final int WORKDAY_END        = 17;
}
