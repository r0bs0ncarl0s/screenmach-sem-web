package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(@JsonAlias("Title") String tipo, 
							@JsonAlias("Episode") Integer numero, 
							@JsonAlias("imdbRating") String avaliacao, 
							@JsonAlias("Released") String dataLancamento,
							@JsonAlias("Poster") String urlPoster,
							@JsonAlias("Runtime") String duracao,
							@JsonAlias("Actors") String atores) {

}
