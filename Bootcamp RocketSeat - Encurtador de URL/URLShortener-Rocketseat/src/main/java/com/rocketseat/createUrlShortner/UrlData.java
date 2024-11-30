package com.rocketseat.createUrlShortner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A classe UrlData é usada para armazenar as informações de uma URL encurtada,
 * incluindo a URL original e o tempo de expiração da URL encurtada.
 */
@AllArgsConstructor // Gera um construtor com todos os parâmetros.
@Setter // Gera métodos setter para todas as propriedades.
@Getter // Gera métodos getter para todas as propriedades.
public class UrlData {
    // A URL original que será encurtada.
    private String originalUrl;

    // O tempo de expiração da URL encurtada em segundos (timestamp).
    private long expirationTime;
}
