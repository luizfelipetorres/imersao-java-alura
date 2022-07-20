import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import json.JsonParser;

public class App {
    private static final Pattern REGEX_ITEMS = Pattern.compile(".*\\[(.+)\\].*");
    private static final Pattern REGEX_ATRIBUTOS_JSON = Pattern.compile("\"(.+?)\":\"(.*?)\"");

    public static void main(String[] args) throws Exception {

        // Fazer conexão HTTP
        String url = "https://mocki.io/v1/9a7c1ca9-29b4-4eb3-8306-1adb9d159060";
        String auth = "";
        URI endereco = URI.create(url + auth);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(endereco)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();

        System.out.println(body);

        // Extrair informações relevantes
        JsonParser parser = new JsonParser();

        List<Map<String, String>> listaDeFilmes = parser.parse(body);
        System.out.println(listaDeFilmes.size());
        System.out.println(listaDeFilmes.get(0));

        // Manipular e exibir dados
        int index = 1;
        for (Map<String, String> filme : listaDeFilmes) {
            System.out.println("Posição:    " + index++);
            System.out.println("Filme:      \u001b[37m\u001b[41m" + filme.get("title") + "\u001b[m");
            System.out.println("Poster:     " + filme.get("image"));
            float nota = Float.parseFloat(filme.get("imDbRating"));
            System.out.println("Nota:       " + filme.get("imDbRating"));

            // Imprimir estrelas
            for (int i = 0; i < nota; i++) {
                System.out.print("\u2B50");
            }
            System.out.printf("\n\n");
        }

        System.out.print("Digite a posição de um filme para classificá-lo: ");
        Scanner scan = new Scanner(System.in);
        int escolha = scan.nextInt();

        boolean validador = false;

        do {
            if (escolha < 1 || escolha > 250) {
                System.out.println("Opção inválida! Escolha entre 1 e 250");
            } else {
                validador = true;
                escolha--;
            }
        } while (validador == false);

        var filme = listaDeFilmes.get(escolha);
        System.out.println("Filme escolhido:    " + filme.get("title"));
        System.out.println("Nota atual:         " + filme.get("imDbRating"));
        System.out.println("\nQual nota você da para esse filme? ");
        float minhaNota = scan.nextFloat();
        filme.put("imDbRating", String.valueOf(minhaNota));
        System.out.println(filme);
    }
}
