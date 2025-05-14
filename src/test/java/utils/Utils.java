package utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.Random;

public class Utils {
    public static void setEnvVar (String key, String value) throws ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration("./src/test/resources/config.properties");
        config.setProperty(key,value);
        config.save();
    }

    public static int generateRandomNumber (int min, int max){
        double random = Math.random()*(max-min)+min;
        return (int) random;
    }

//    public static String generateGender() {
//        Random random = new Random();
//        // Generate a random boolean value (true or false)
//        boolean isMale = random.nextBoolean();
//        return isMale ? "Male" : "Female";
//    }

//    public static void main(String[] args) {
//        System.out.println(generateGender());
//    }
}
