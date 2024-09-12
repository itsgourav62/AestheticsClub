//create a demo test

import org.junit.Before;
// import org.junit.Test;

import com.aestheticsclub.admindao.Admin;
import com.aestheticsclub.admindao.AdminInterface;
import com.aestheticsclub.userdao.User;
import com.aestheticsclub.userdao.UserInterface;

import junit.framework.TestCase;

public class Test extends TestCase {
    private static UserInterface user;
    private static AdminInterface admin;

    @Override
    @Before
    public void setUp() {
        user = new User();
        admin = new Admin();
    }

    @org.junit.Test
    public static void testUserlogin() {
        int status = user.login("a", "a");
        // Check if status is greater than 0
        assertTrue("User login status should be greater than 0", status > 0);
    }

    @org.junit.Test
    public static void testAdminlogin() {
        Boolean status = admin.login("a", "a");
        //Check if the status is true
        assertTrue("Admin login status should be true", status);
    }
}