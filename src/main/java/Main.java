import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Cat> noVotedCats = filterNoVotedCats(getAllCats());

        // Выведите результат на экран
        for (Cat cat: noVotedCats){
            System.out.println(cat);
        }
    }

    public static List<Cat> getAllCats() throws IOException {

        // Создайте метод в который добавьте и настройте класс CloseableHttpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        // Добавьте объект запроса
        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

        // Вызовите удаленный сервис
        CloseableHttpResponse response = httpClient.execute(request);

        // Преобразуйте json в список java объектов
        ObjectMapper objectMapper = new ObjectMapper();

        // result all cats as List
        return objectMapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
        });
    }
    public static List <Cat> filterNoVotedCats(List<Cat> cats){
        return cats.stream().filter(cat -> cat.getUpvotes()>0).collect(Collectors.toList());
    }
}
