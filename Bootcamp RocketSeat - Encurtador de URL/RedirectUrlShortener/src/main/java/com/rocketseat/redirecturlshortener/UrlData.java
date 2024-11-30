package com.rocketseat.redirecturlshortener;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe que representa os dados de uma URL encurtada, incluindo a URL original
 * e o tempo de expiração da URL.
 */
@AllArgsConstructor // Gera um construtor com todos os parâmetros
@NoArgsConstructor  // Gera um construtor sem parâmetros (necessário para o Jackson)
@Setter             // Gera os métodos setters para todos os campos
@Getter             // Gera os métodos getters para todos os campos
public class UrlData {

    // URL original para onde a URL encurtada irá redirecionar.
    private String originalUrl;

    // Tempo de expiração da URL em milissegundos desde a época Unix (1970-01-01T00:00:00Z).
    private long expirationTime;

    /**
     * Método getter para a URL original.
     * @return A URL original que será acessada ao clicar no link encurtado.
     */
    public String getOriginalUrl() {
        return originalUrl;
    }

    /**
     * Método setter para a URL original.
     * @param originalUrl A URL original que será associada à URL encurtada.
     */
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    /**
     * Método getter para o tempo de expiração.
     * @return O tempo de expiração em milissegundos desde a época Unix.
     */
    public long getExpirationTime() {
        return expirationTime;
    }

    /**
     * Método setter para o tempo de expiração.
     * @param expirationTime O tempo de expiração a ser associado à URL encurtada.
     */
    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
