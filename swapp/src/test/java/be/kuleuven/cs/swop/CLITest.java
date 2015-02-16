package be.kuleuven.cs.swop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class CLITest {

	private static String[] validArgs = {"hi", "this", "is", "a", "valid", "cli"};
	private static String[] emptyArgs = {};
	private static String[] invalidArgs;
	private static CLI validCLI;

	@BeforeClass
	public static void setUp() {
		invalidArgs = null;
		validCLI = new CLI(emptyArgs);
	}

	@BeforeClass
	public static void tearDown() {
	}

	@Test
	public void testCanHaveAsArgumentsValid() {
		assertTrue(CLI.canHaveAsArgs(validArgs));
		assertTrue(CLI.canHaveAsArgs(emptyArgs));
	}

	public void testCanHaveAsArgumentsInvalid() {
		assertFalse(CLI.canHaveAsArgs(invalidArgs));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructCLIIllegalArguments() {
		new CLI(null);
	}

}
