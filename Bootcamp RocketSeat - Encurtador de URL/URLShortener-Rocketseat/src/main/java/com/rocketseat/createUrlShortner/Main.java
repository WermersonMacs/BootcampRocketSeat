package com.rocketseat.createUrlShortner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe principal que implementa o processo de criação de um encurtador de URL.
 * Ela recebe uma URL original e o tempo de expiração da URL, gera um código curto para a URL e armazena
 * as informações no Amazon S3.
 */
public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {

    // Instancia o ObjectMapper para manipulação de JSON.
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Instancia o cliente S3 para interagir com o Amazon S3.
    private final S3Client s3Client = S3Client.builder().build();

    /**
     * Método principal chamado pela AWS Lambda quando uma requisição é recebida.
     *
     * @param input   Mapa contendo os dados da solicitação.
     * @param context Contexto da execução da Lambda.
     * @return Mapa com o código da URL encurtada.
     */
    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {
        // Obtém o corpo da solicitação que contém os dados da URL original e tempo de expiração.
        String body = (String) input.get("body");

        Map<String, String> bodyMap;
        try {
            // Converte o corpo da solicitação de JSON para um Map para facilitar o acesso aos dados.
            bodyMap = objectMapper.readValue(body, Map.class);
        } catch (Exception exception) {
            // Lança uma exceção se o JSON não for parseado corretamente.
            throw new RuntimeException("Error parsing JSON body: " + exception.getMessage(), exception);
        }

        // Extrai a URL original e o tempo de expiração (em milissegundos) do corpo da solicitação.
        String originalUrl = bodyMap.get("originalUrl");
        String expirationTime = bodyMap.get("expirationTime");
        long expirationTimeInSeconds = Long.parseLong(expirationTime);

        // Gera um código curto único para a URL usando UUID. O código será a primeira parte do UUID gerado.
        String shortUrlCode = UUID.randomUUID().toString().substring(0, 8);

        // Cria um objeto UrlData com os dados recebidos.
        UrlData urlData = new UrlData(originalUrl, expirationTimeInSeconds);

        try {
            // Converte o objeto UrlData para uma string JSON.
            String urlDataJson = objectMapper.writeValueAsString(urlData);

            // Prepara a solicitação para armazenar os dados da URL encurtada no S3.
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket("meu-bucket-shortener") // Nome do bucket no S3 onde os dados serão armazenados.
                    .key(shortUrlCode + ".json")    // A chave para o arquivo (código curto da URL + .json).
                    .build();

            // Envia os dados para o S3.
            s3Client.putObject(request, RequestBody.fromString(urlDataJson));
        } catch (Exception exception) {
            // Lança uma exceção caso ocorra um erro ao salvar os dados no S3.
            throw new RuntimeException("Error saving data to S3: " + exception.getMessage(), exception);
        }

        // Cria a resposta com o código curto gerado para a URL.
        Map<String, String> response = new HashMap<>();
        response.put("code", shortUrlCode); // Adiciona o código da URL encurtada à resposta.

        return response; // Retorna a resposta contendo o código da URL encurtada.
    }
}
