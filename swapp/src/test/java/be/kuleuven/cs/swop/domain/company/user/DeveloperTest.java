package be.kuleuven.cs.swop.domain.company.user;


import org.junit.Test;


public class DeveloperTest {

    @Test
    public void ConstructorValidTest() {
        new Developer("Valid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConstructorInvalidTest() {
        new Developer(null);
    }

}
