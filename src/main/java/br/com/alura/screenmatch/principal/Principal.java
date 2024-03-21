package br.com.alura.screenmatch.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.alura.screenmatch.model.DadosCEP;
import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import br.com.alura.screenmatch.utils.Constantes;

public class Principal {
	
	private Scanner leitura 			= new Scanner(System.in);
	private ConsumoAPI consumo 			= new ConsumoAPI();
	private ConverteDados conversor 	= new ConverteDados();
	
	public void buscarSeriePor(String tipo) {
		try {
			if(tipo!=null && !tipo.equals("")) {
				String auxBusca = "";
				String aux = "";
				if(tipo.trim().equalsIgnoreCase("nome")) {
					aux = "nome";
				}else if(tipo.trim().equalsIgnoreCase("id")) {
					aux = "ID";
				}
				System.out.println("Digite o " + aux + " da série para burca:");
				auxBusca = leitura.nextLine();			
				var json = consumo.obterDados(Constantes.HTTPS + Constantes.API_FILMES_SERIES + auxBusca.replace(" ", "+") + 
											  Constantes.API_KEY + Constantes.OMDBAPI_KEY);
				if(json.contains("Movie not found!")) {
					System.out.println("Filme/Série não encontrada");
				}else {
					DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
					System.out.println(dados);
					json = consumo.obterDados(Constantes.HTTPS + Constantes.API_FILMES_SERIES + auxBusca.replace(" ", "+") + 
											  Constantes.SEASON + "1" + 
											  Constantes.EPISODE + "2" + 
											  Constantes.API_KEY + Constantes.OMDBAPI_KEY);
					DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
					System.out.println(dadosEpisodio);				
					List<DadosTemporada> listDadosTemporada = new ArrayList<>(); 
					for (int i = 1; i < dados.totalTemporadas(); i++) {
						json = consumo.obterDados(Constantes.HTTPS + Constantes.API_FILMES_SERIES + auxBusca.replace(" ", "+") + 
													 Constantes.SEASON  + i + 
													 Constantes.API_KEY + Constantes.OMDBAPI_KEY);
						DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
						listDadosTemporada.add(dadosTemporada);
					}
					listDadosTemporada.forEach(System.out::println);
					
					//USANDO LAMBDA
					listDadosTemporada.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}	
	}
	
	public void buscarCEP(){
		try {
			System.out.println("Digite o CEP desejado:");
			String auxBusca = leitura.nextLine();
			var json = consumo.obterDados(Constantes.HTTPS + Constantes.API_CEP + auxBusca + Constantes.API_CEP_COMPLEMENTO_FINAL);
			DadosCEP dadosCEP = conversor.obterDados(json, DadosCEP.class);
			System.out.println(dadosCEP);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}		
	}
}
