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
import br.com.alura.screenmatch.principal.Principal;
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
		
		Principal principal = new Principal(); 
		principal.buscarSeriePor("ID");
		
		//Buscar CEP
		ConsumoAPI consumoAPI = new ConsumoAPI(); 
		ConverteDados conversor = new ConverteDados();
		List<String> listaCEP = new ArrayList<>();
		listaCEP.add("26255155");
		listaCEP.add("26013310");
		for (String itemCEP : listaCEP) {
			var json = consumoAPI.obterDados(Constantes.HTTPS + Constantes.API_CEP + itemCEP + Constantes.API_CEP_COMPLEMENTO_FINAL);
			DadosCEP dadosCEP = conversor.obterDados(json, DadosCEP.class);
			System.out.println(dadosCEP);
		}
	}
}