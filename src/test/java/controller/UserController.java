package controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import setup.UserModel;

import java.util.Properties;

import static io.restassured.RestAssured.given;

public class UserController {

    Properties prop;

    public UserController(Properties prop){
        this.prop = prop;
    }

    public Response userLogin(UserModel userModel){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        return given().contentType("application/json")
                .body(userModel)
                .when().post("/auth/login");
    }

    public Response createUser(UserModel userModel){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        return given().contentType("application/json").body(userModel)
                .when().post("/auth/register");
    }

    public Response userList(){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        return given().contentType("application/json").
                header("Authorization" ,"Bearer " +prop.getProperty("token"))
                .when().get("/user/users");
    }

    public Response searchUser(String userId){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        return given().contentType("application/json").
                header("Authorization" ,"Bearer " +prop.getProperty("token"))
                .when().get("/user/"+userId);
    }

    public Response deleteUser(String userId){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        return given().contentType("application/json").
                header("Authorization" ,"Bearer " +prop.getProperty("token"))
                .when().delete("/user/"+userId);
    }
}
