package cn.yzf.pass.test;

import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
@RestController
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Autowired
    private RestClient restClient;

    @GetMapping("/get")
    public String get(@RequestParam String q){

        Request request = new Request(
                "GET",
                q);
        try {
            Response response = restClient.performRequest(request);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
        return "success";
    }

//    @Autowired
//    private RestHighLevelClientClient restClient;
//
//    @GetMapping("/get")
//    public String get(@RequestParam String q){
//
//        ClusterHealthRequest request = new ClusterHealthRequest();
//        try {
//            ClusterHealthResponse resp = restClient.cluster().health(request, RequestOptions.DEFAULT);
//            System.out.println(resp);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return e.toString();
//        }
//        return "success";
//    }
}
