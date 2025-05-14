import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.jupiter.api.Test;
import utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class myRestAssured {
    Properties prop;
    public myRestAssured() throws IOException{
        prop = new Properties();
        FileInputStream fs = new FileInputStream("./src/test/resources/config.properties");
        prop.load(fs);
    }
    @Test
    public void doLogin () throws ConfigurationException {
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =given().contentType("application/json")
                .body("{\n" +
                        "  \"email\": \"admin@test.com\",\n" +
                        "  \"password\": \"admin123\"\n" +
                        "}")
                .when().post("/auth/login");
        //System.out.println(res.asString());
        JsonPath jsonObj = res.jsonPath();
        String token = jsonObj.get("token");
        System.out.println(token);
        Utils.setEnvVar("token",token);
    }

    @Test
    public void userList () throws ConfigurationException {
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =given().contentType("application/json").
                header("Authorization" ,"Bearer " +prop.getProperty("token"))
                .when().get("/user/users");
        //System.out.println(prop);

        System.out.println(res.asString());
    }

    @Test
    public void scarchUser() throws ConfigurationException {
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =given().contentType("application/json").
                header("Authorization" ,"Bearer " +prop.getProperty("token"))
                        .when().get("/user/727245d6-7c91-4580-896c-5159daffb195");
        //System.out.println(prop);

        System.out.println(res.asString());
//        JsonPath jsonObj = res.jsonPath();
//        String token = jsonObj.get("user.id").toString();
//        System.out.println(token);
        //utils.Utils.setEnvVar("userId",);
    }

    public void deleteUser(){
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res = given().contentType("application/json").
                header("Authorization", "Bearer "+prop.getProperty("token"))
                .when().delete("/user/12");
        System.out.println(res.asString());
    }
}
