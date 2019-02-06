package tests;

import io.github.cdimascio.dotenv.DotEnvException;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JavaTests {
    @Test(expected = DotEnvException.class)
    public void throwIfMalconfigured() {
        Dotenv.configure().directory("/libs").load();
    }

    @Test(expected = DotEnvException.class)
    public void load() {
        Dotenv dotenv = Dotenv.configure().directory("/libs").load();
        assertEquals("ivr_ident", dotenv.get("DB_USER"));
    }

    @Test
    public void configurWithIgnoreMalformed() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .load();
        assertEquals("ivr_ident", dotenv.get("DB_USER"));
    }

    @Test
    public void configurWithIgnoreMissingAndMalformed() {
        Dotenv dotenv = Dotenv.configure()
                .directory("/libs")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
        assertNotNull(dotenv.get("PATH"));
    }

    @Test
    public void checkIfDotEnvFileExists() {
        Dotenv dotenv = Dotenv
                .configure()
                .filename(".env")
                .load();
    }
}