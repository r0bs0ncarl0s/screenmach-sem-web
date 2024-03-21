package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosCEP (@JsonAlias("cep") String cep,
						@JsonAlias("logradouro") String logradouro,
						@JsonAlias("complemento") String complemento,
						@JsonAlias("bairro") String bairro,
						@JsonAlias("localidade") String localidade,
						@JsonAlias("uf") String uf,
						@JsonAlias("ibge") String ibge,
						@JsonAlias("gia") String gia,
						@JsonAlias("ddd") Integer ddd,
						@JsonAlias("siafi") String siafi) {
}
