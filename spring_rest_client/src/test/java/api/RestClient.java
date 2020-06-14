package api;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    private RestTemplate restTemplate = new RestTemplate();

//    public <T> T get(String url, Class<T> type){
//        this.log(url);
//        return restTemplate.getForObject(url, type);
//    }

    public <T> T get(String url, Class<T> type){
        this.log(url);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "token ");
//        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.getForObject(url, type);
//        return responseEntity.getBody();
    }

    private void log(String url){
        System.out.println("Trying to get '" + url + "'");
    }
}
