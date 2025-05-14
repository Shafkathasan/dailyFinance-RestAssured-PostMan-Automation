package test;

import com.github.javafaker.Faker;
import controller.UserController;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;
import setup.Setup;
import setup.UserModel;
import utils.Utils;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class UserTestRunner extends Setup {
//    @Test(priority = 1, description = "User Login")
    public void userLogin() throws ConfigurationException {
        UserController userController = new UserController(prop);
        UserModel userModel = new UserModel();
        userModel.setEmail("admin@test.com");
        userModel.setPassword("admin123");
        Response res = userController.userLogin(userModel);
        //System.out.println(res.asString());
        JsonPath jsonObj = res.jsonPath();
        String token = jsonObj.get("token");
        System.out.println(token);
        Utils.setEnvVar("token",token);
    }

//    @Test(priority = 2, description = "Create User")
    public void createUser() throws ConfigurationException {
        UserController userController = new UserController(prop);
        UserModel userModel = new UserModel();
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = "safuser"+Utils.generateRandomNumber(1000,9999)+"@test.com";
        String password = "1234";
        String phoneNumber = "0120"+Utils.generateRandomNumber(1000000,9999999);
        String address = faker.address().streetAddress();
        String gender = "Male";
        boolean termsAccepted = true;

        userModel.setFirstName(firstName);
        userModel.setLastName(lastName);
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setPhoneNumber(phoneNumber);
        userModel.setAddress(address);
        userModel.setGender(gender);
        userModel.setTermsAccepted(String.valueOf(termsAccepted));

        Response res = userController.createUser(userModel);
        //System.out.println(res.asString());
        JsonPath jsonObj = res.jsonPath();
        String id = jsonObj.get("_id");
        System.out.println(id);
        Utils.setEnvVar("userId",id);
    }

    @Test(priority = 3, description = "User List")
    public void  userList(){
        UserController userController = new UserController(prop);
        Response res = userController.userList();
        System.out.println(res.asString());

        // Assert status code is 200
        assertEquals(res.getStatusCode(), 200);

        // Parse the response as a List (assuming JSON array is returned at root)
        List<?> users = res.jsonPath().getList("$");

        // Assert that it's an array and has more than 10 elements
        assertThat("Response should be a JSON array", users, is(notNullValue()));
        assertThat("Response array should have more than 10 elements", users.size(), greaterThan(10));
    }

//    @Test(priority = 4, description = "Search User by Id")
    public void searchUser(){
        UserController userController = new UserController(prop);
        Response res = userController.searchUser(prop.getProperty("userId"));
        System.out.println(res.asString());

        // Assert status code is 200
        assertEquals(res.getStatusCode(), 200);

        // Assert "role" field contains "user"
        String role = res.jsonPath().getString("role");
        assertThat(role, containsString("user"));
    }

//    @Test(priority = 5, description = "Delete User by Id")
    public void deleteUser(){
        UserController userController = new UserController(prop);
        Response res = userController.deleteUser(prop.getProperty("userId"));
        System.out.println(res.asString());

        // Assert status code is 200
        assertEquals(res.getStatusCode(), 200);

        // Assert message contains expected success text
        String message = res.jsonPath().getString("message");
        assertThat(message, containsString("User deleted successfully"));
    }


}
