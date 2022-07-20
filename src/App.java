import java.io.IOException;
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
    private static final String URL = "https://mocki.io/v1/9a7c1ca9-29b4-4eb3-8306-1adb9d159060";
    private static final Scanner scan = new Scanner(System.in);

    private static String getBody() throws IOException, InterruptedException {
        URI endereco = URI.create(URL);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(endereco)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();

        return body;
    }

    private static List<Map<String, String>> parse(String body) {
        JsonParser parser = new JsonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body);
        return listaDeFilmes;
    }

    private static void printLista(List<Map<String, String>> lista) {
        int index = 0;
        for (Map<String, String> filme : lista) {
            printFilme(filme, index++);
        }
    }

    private static void printFilme(Map<String, String> filme, int posicao) {
        final String STAR   = "\u2B50";
        final String COLOR  = "\u001b[37m\u001b[41m";
        final String BREAK  = "\u001b[m";

        System.out.println("Posição:    " + ++posicao);
        System.out.println("Filme:      " + COLOR + filme.get("title") + BREAK);
        System.out.println("Poster:     " + filme.get("image"));
        float nota = Float.parseFloat(filme.get("imDbRating"));
        System.out.println("Nota:       " + filme.get("imDbRating"));

        // Imprimir estrelas
        for (int i = 0; i < nota; i++) {
            System.out.print(STAR);
        }
        System.out.printf("\n\n");
    }

    private static boolean isValid(int escolha, int min, int max) {
        if (escolha < min || escolha > max) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(String[] args) throws Exception {
        
        boolean validador;
        int escolha;

        // Fazer conexão HTTP e retornar o body em string
        String body = getBody();

        // Parsear em uma lista de Map<String, String>
        var listaDeFilmes = parse(body);

        // Imprimir lista
        printLista(listaDeFilmes);

        // Capturar posição do filme
        do {
            System.out.print("Digite a posição de um filme para classificá-lo: ");
            escolha = scan.nextInt();
            validador = isValid(escolha, 1, 250);

            if (!validador) {
                System.out.println("Digite um numero entre 1 e 250");
            }else{
                escolha--;
            }
        } while (!validador);

        // Separar e imprimir fime escolhido
        var filme = listaDeFilmes.get(escolha);
        printFilme(filme, escolha);

        //Capturar nota, atribuir e imprimir
        System.out.println("\nQual nota você da para esse filme? ");
        float minhaNota = scan.nextFloat();
        filme.put("imDbRating", String.valueOf(minhaNota));
        printFilme(filme, escolha);
    }
}