package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias({"Title","Titulo_Filme","Titulo_Completo"}) String titulo, 
						 @JsonAlias("totalSeasons") Integer totalTemporadas, 
						 @JsonAlias("imdbRating") String avaliacao,
						 @JsonProperty("imdbVotes") String votos) {

}
