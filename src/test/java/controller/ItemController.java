package controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import setup.ItemModel;
import setup.UserModel;

import java.util.Properties;

import static io.restassured.RestAssured.given;

public class ItemController {
    Properties prop;

    public ItemController(Properties prop){
        this.prop = prop;
    }

    public Response userLogin(UserModel userModel){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        return given().contentType("application/json")
                .body(userModel)
                .when().post("/auth/login");
    }

    public Response getItemList(){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        return given().contentType("application/json").
                header("Authorization" ,"Bearer " +prop.getProperty("userToken"))
                .when().get("/costs");
    }

    public Response addItem(ItemModel itemModel) {
        RestAssured.baseURI = prop.getProperty("baseUrl");
        return given().contentType("application/json").body(itemModel)
                .header("Authorization", "Bearer " + prop.getProperty("userToken"))
                .when().post("/costs");

    }

    public Response searchItem(String itemId) {
        RestAssured.baseURI = prop.getProperty("baseUrl");
        return given().contentType("application/json")
                .header("Authorization", "Bearer " + prop.getProperty("userToken"))
                .when().get("/costs/"+ itemId);

    }

    public Response editItem(String itemId, ItemModel itemModel){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        return given().contentType("application/json").body(itemModel)
                .header("Authorization","Bearer "+ prop.getProperty("userToken"))
                .when().put("/costs/"+ itemId);

    }

    public Response deleteItem(String itemId){
        RestAssured.baseURI = prop.getProperty("baseUrl");
        return given().contentType("application/json")
                .header("Authorization", "Bearer " + prop.getProperty("userToken"))
                .when().delete("/costs/"+itemId);
    }

}
