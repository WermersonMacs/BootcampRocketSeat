package com.rocketseat.redirecturlshortener;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe principal que implementa um redirecionador de URL encurtada usando AWS Lambda.
 * Ela lida com as solicitações recebidas, verifica os dados no Amazon S3 e redireciona ou retorna um erro, dependendo da validade da URL.
 */
public class Main implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    // Instancia um cliente S3 para interagir com o bucket.
    private final S3Client s3Client = S3Client.builder().build();

    // Instancia um ObjectMapper para deserializar JSON em objetos Java.
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Método principal chamado pela AWS Lambda quando uma requisição é recebida.
     *
     * @param input   Mapa contendo os dados da solicitação.
     * @param context Contexto da execução da Lambda.
     * @return Mapa contendo o status e os cabeçalhos da resposta HTTP.
     */
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        // Obtém o caminho bruto da solicitação, que contém o código da URL encurtada.
        String pathParameters = (String) input.get("rawPath");
        String shortUrlCode = pathParameters.replace("/", ""); // Remove barras do caminho.

        // Valida se o código da URL encurtada é válido.
        if (shortUrlCode == null || shortUrlCode.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: 'shortUrlCode' is required.");
        }

        // Monta a chave do arquivo no S3 correspondente ao código da URL.
        String key = shortUrlCode + ".json";
        System.out.println("Chave solicitada: " + key);

        // Cria a solicitação para obter o objeto do S3.
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("meu-bucket-shortener") // Nome do bucket onde as URLs estão armazenadas.
                .key(key)                      // Chave do objeto no bucket.
                .build();

        InputStream s3ObjectStream;

        // Tenta buscar o objeto do S3.
        try {
            s3ObjectStream = s3Client.getObject(getObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching data from S3" + e.getMessage(), e);
        }

        UrlData urlData;

        // Tenta deserializar os dados do objeto S3 para o objeto UrlData.
        try {
            urlData = objectMapper.readValue(s3ObjectStream, UrlData.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing URL data: " + e.getMessage(), e);
        }

        // Obtém o tempo atual em segundos (para comparar com a validade da URL).
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;

        // Cria um mapa para armazenar a resposta da Lambda.
        Map<String, Object> response = new HashMap<>();

        // Verifica se a URL já expirou.
        if (urlData.getExpirationTime() < currentTimeInSeconds) {
            // Retorna um erro HTTP 410 (Gone) indicando que a URL expirou.
            response.put("statusCode", 410);
            response.put("body", "This URL has expired");
            return response;
        }

        // Se a URL ainda é válida, retorna um redirecionamento HTTP 302.
        response.put("statusCode", 302);

        // Adiciona o cabeçalho "Location" com a URL original para redirecionamento.
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", urlData.getOriginalUrl());
        response.put("headers", headers);

        return response; // Retorna a resposta final.
    }
}
