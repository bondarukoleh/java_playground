package api;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    private RestTemplate restTemplate = new RestTemplate();

    public <T> T get(String url, Class<T> type){
        this.log(url);
        return restTemplate.getForObject(url, type);
    }

    private void log(String url){
        System.out.println("Trying to get '" + url + "'");
    }
}
