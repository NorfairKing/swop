package be.kuleuven.cs.swop.domain.company.user;


import org.junit.Test;


public class ManagerTest {

    @Test
    public void ConstructorValidTest() {
        new Manager("Valid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConstructorInvalidTest() {
        new Manager(null);
    }

}
