package cn.yzf.pass.test;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
@RestController
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Autowired
    private RestHighLevelClient restClient;

    @GetMapping("/get")
    public String get(String param){
        GetRequest getRequest = new GetRequest(
                "posts",
                "doc",
                "1");

        GetResponse response = null;
        try {
            response = restClient.get(getRequest, RequestOptions.DEFAULT);
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
