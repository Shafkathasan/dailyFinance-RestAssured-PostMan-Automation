package test;

import controller.ItemController;
import controller.UserController;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import setup.ItemModel;
import setup.Setup;
import setup.UserModel;
import utils.Utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;

public class ItemTestRunner extends Setup {

        @Test(priority = 1, description = "User Login")
        public void userLogin() throws ConfigurationException {
            ItemController itemController = new ItemController(prop);
            UserModel userModel = new UserModel();
            userModel.setEmail(prop.getProperty("userEmail"));
            userModel.setPassword(prop.getProperty("userPass"));
            Response res = itemController.userLogin(userModel);
            //System.out.println(res.asString());
            JsonPath jsonObj = res.jsonPath();
            String userToken = jsonObj.get("token");
            System.out.println(userToken);
            Utils.setEnvVar("userToken", userToken);

            Assert.assertEquals(res.getStatusCode(), 200);
            // Assert "role" field contains "user"
            String role = res.jsonPath().getString("role");
            assertThat(role, containsString("user"));
        }

    @Test(priority = 2, description = "Get Item List")
    public void getItemList(){
        ItemController itemController = new ItemController(prop);
        Response res = itemController.getItemList();
        System.out.println(res.asString());
        Assert.assertEquals(res.getStatusCode(), 200);

    }

    @Test(priority = 3, description = "Add Item")
    public void addItem() throws ConfigurationException {
        ItemController itemController = new ItemController(prop);

        ItemModel itemModel = new ItemModel();

        itemModel.setItemName("Any Item");
        itemModel.setQuantity("5");
        itemModel.setAmount("50");
        itemModel.setPurchaseDate("2025-05-05");
        itemModel.setMonth("May");
        itemModel.setRemarks("Automated Item");

        Response res = itemController.addItem(itemModel);
        Assert.assertEquals(res.getStatusCode(), 201);

        System.out.println(res.asString());
        JsonPath jsonPath = res.jsonPath();

        String itemId = jsonPath.get("_id");
        Utils.setEnvVar("itemId", itemId);
        String itemName = jsonPath.get("itemName");
        Utils.setEnvVar("ItemName", itemName);

    }

    @Test(priority = 4, description = "Search Item")
    public void searchItem() {
        ItemController itemController = new ItemController(prop);
        Response res = itemController.searchItem(prop.getProperty("itemId"));
        System.out.println(res.asString());

        // Assert status code is 200
        assertEquals(res.getStatusCode(), 200);

    }

    @Test(priority = 5, description = "Edited Item")
    public void editItem(){
        ItemController itemController = new ItemController(prop);
        ItemModel itemModel = new ItemModel();

        itemModel.setItemName("Edit item");
        itemModel.setQuantity("1");
        itemModel.setAmount("10");
        itemModel.setPurchaseDate("2025-06-06");
        itemModel.setMonth("June");
        itemModel.setRemarks("Edited Item");
        Response res = itemController.editItem(prop.getProperty("itemId"), itemModel);
        System.out.println(res.asString());
        Assert.assertEquals(res.getStatusCode(), 200);

    }

    @Test(priority = 6, description = "Delete Item")
    public void deleteItem(){
        ItemController itemController = new ItemController(prop);
        Response res = itemController.deleteItem(prop.getProperty("itemId"));

        System.out.println(res.asString());
        // Assert status code is 200
        assertEquals(res.getStatusCode(), 200);

        // Assert message contains expected success text
        String message = res.jsonPath().getString("message");
        assertThat(message, containsString("Cost deleted successfully"));
    }

    @Test(priority = 7, description = "User Login with Invalid Credentials")
    public void testUserLoginInvalidCredentials() throws ConfigurationException {
        ItemController itemController = new ItemController(prop);
        UserModel userModel = new UserModel();
        userModel.setEmail(prop.getProperty("userEmail"));
        userModel.setPassword("wrong_password");
        Response res = itemController.userLogin(userModel);

        Assert.assertEquals(res.getStatusCode(), 401);
        //assertThat(res.jsonPath().getString("message"), containsString("Invalid credentials"));
    }

    @Test(priority = 8, description = "Add Item with Invalid Data")
    public void testAddItemWithInvalidData() {
        ItemController itemController = new ItemController(prop);
        ItemModel itemModel = new ItemModel();
        itemModel.setAmount(String.valueOf(-100)); // Negative amount
        Response res = itemController.addItem(itemModel);

        Assert.assertEquals(res.getStatusCode(), 500);
        //assertThat(res.jsonPath().getString("message"), containsString("validation failed"));
    }

    @Test(priority = 9, description = "Get Item List Without Authentication")
    public void testGetItemListUnauthorized() {
        // Save original token
        String originalToken = prop.getProperty("userToken");

        try {
            prop.setProperty("userToken", "invalid_token");
            ItemController itemController = new ItemController(prop);
            Response res = itemController.getItemList();

            Assert.assertEquals(res.getStatusCode(), 401);
        } finally {
            prop.setProperty("userToken", originalToken);
        }
    }

    @Test(priority = 10, description = "Update Non-Existent Item")
    public void testUpdateNonExistentItem() {
        ItemController itemController = new ItemController(prop);
        ItemModel itemModel = new ItemModel();
        itemModel.setItemName("Invalid Item");
        Response res = itemController.editItem("invalid_item_id_123", itemModel);

        Assert.assertEquals(res.getStatusCode(), 500);
        //assertThat(res.jsonPath().getString("message"), containsString("not found"));
    }

    @Test(priority = 11, description = "Delete Item Without Authorization")
    public void testDeleteItemUnauthorized() {
        // Save original token
        String originalToken = prop.getProperty("userToken");

        try {
            prop.setProperty("userToken", "invalid_token");
            ItemController itemController = new ItemController(prop);
            Response res = itemController.deleteItem(prop.getProperty("itemId"));

            Assert.assertEquals(res.getStatusCode(), 401);
        } finally {
            prop.setProperty("userToken", originalToken);
        }
    }

}
