package be.kuleuven.cs.swop.domain.company.user;


@SuppressWarnings("serial")
public class Manager implements User {
    private final String name;

    public Manager(String name) {
        if (name == null) throw new IllegalArgumentException(ERROR_ILLEGAL_NAME);
        
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Manager other = (Manager) obj;
        if (!name.equals(other.name)) return false;
        return true;
    }

    private static final String ERROR_ILLEGAL_NAME = "Illegal name for Manager";

}
