package br.com.alura.screenmatch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.screenmatch.model.DadosCEP;
import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import br.com.alura.screenmatch.utils.Constantes;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoAPI consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados(Constantes.HTTPS + Constantes.API_FILMES_SERIES + "gilmore+girls" + 
										 Constantes.API_KEY + Constantes.OMDBAPI_KEY);
		//System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
		json = consumoAPI.obterDados(Constantes.HTTPS + Constantes.API_FILMES_SERIES + "gilmore+girls" + 
									 Constantes.SEASON + "1" + 
									 Constantes.EPISODE + "2" + 
									 Constantes.API_KEY + Constantes.OMDBAPI_KEY);
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);
		
		List<DadosTemporada> listDadosTemporada = new ArrayList<>(); 
		for (int i = 1; i < dados.totalTemporadas(); i++) {
			json = consumoAPI.obterDados(Constantes.HTTPS + Constantes.API_FILMES_SERIES + "gilmore+girls" + 
										 Constantes.SEASON  + i + 
										 Constantes.API_KEY + Constantes.OMDBAPI_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			listDadosTemporada.add(dadosTemporada);
		}
		listDadosTemporada.forEach(System.out::println);
		
		//Buscar CEP
		List<String> listaCEP = new ArrayList<>();
		listaCEP.add("26255155");
		listaCEP.add("26013310");
		for (String itemCEP : listaCEP) {
			json = consumoAPI.obterDados(Constantes.HTTPS + Constantes.API_CEP + itemCEP + Constantes.API_CEP_COMPLEMENTO_FINAL);
			DadosCEP dadosCEP = conversor.obterDados(json, DadosCEP.class);
			System.out.println(dadosCEP);
		}
	}
}