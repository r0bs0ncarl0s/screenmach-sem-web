package br.com.alura.screenmatch.principal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.model.DadosCEP;
import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
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
					List<String> listTituloEpisodios = new ArrayList<>(); 
					for (int i = 1; i < dados.totalTemporadas(); i++) {
						json = consumo.obterDados(Constantes.HTTPS + Constantes.API_FILMES_SERIES + auxBusca.replace(" ", "+") + 
													 Constantes.SEASON  + i + 
													 Constantes.API_KEY + Constantes.OMDBAPI_KEY);
						DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
						listDadosTemporada.add(dadosTemporada);
						dadosTemporada.episodios().forEach(e -> listTituloEpisodios.add(e.titulo())); 
					}
					listDadosTemporada.forEach(System.out::println);
					
					
					//USANDO LAMBDA
					System.out.println("Imprimindo Título");
					listDadosTemporada.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
					
					
					
					System.out.println("Imprimindo Título em Ordem alfabética");
					listTituloEpisodios.stream().sorted().forEach(e -> System.out.println(e));
					
					
					List<DadosEpisodio> listaDadosEpisodios = listDadosTemporada.stream().flatMap(t -> t.episodios().stream()).collect(Collectors.toList());
					
					
					
					System.out.println("Imprimindo 5 melhores episódios");
					listaDadosEpisodios.stream().filter(e -> !e.avaliacao().equalsIgnoreCase("N/A")).sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed()).limit(5).forEach(System.out::println);
					
					
					
					
					System.out.println("Imprimindo 10 melhores episódios exibindo o que está sendo feito");
					listaDadosEpisodios.stream().filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
												.peek(e -> System.out.println("Filtrando :" + e))
												.sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
												.peek(e -> System.out.println("Ordenando :" + e))
												.limit(10)
												.peek(e -> System.out.println("Limitando os 10 primeiros :" + e))
												.forEach(System.out::println);
					
					
					List<Episodio> listaEpisodios = listDadosTemporada.stream()
																	  .flatMap(t -> t.episodios()
																			  		.stream()
																			  		.map(d -> new Episodio(t.numero(), d)))
																	  .collect(Collectors.toList());
					listaEpisodios.forEach(System.out::println);
					
					
					
					
					System.out.println("A partir de que ano você deseja ver os episódios?");
					var ano = leitura.nextInt();
					leitura.nextLine();
					
					LocalDate dataBusca = LocalDate.of(ano, 1, 1);
					
					DateTimeFormatter dtFormater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					listaEpisodios.stream().filter(e -> e.getDataLancamento()!=null && e.getDataLancamento().isAfter(dataBusca))
												   .forEach(e -> System.out.println("Temporada:" + e.getTemporada() + 
														   						    " Episódio:" + e.getTitulo() + 
														   						    " Data Lançamento:" + e.getDataLancamento().format(dtFormater) + 
														   						    " Avaliação:" + e.getAvaliacao()));
					
					
					
					
					System.out.println("Qual episódio você mais gostou?");
					var auxBuscaTitulo = leitura.nextLine();
					Optional<Episodio> optEpisodio = listaEpisodios.stream()
															  	   .filter(e -> e.getTitulo().toUpperCase().contains(auxBuscaTitulo.toUpperCase()))
															  	   .findFirst();
					if(optEpisodio.isPresent()) {
						System.out.println("Episódio encontrado!");
						System.out.println("Temporada:" + optEpisodio.get().getTemporada());
					}else {
						System.out.println("Episódio não encontrado!");
					}
					
					
					System.out.println("Média das avaliações das temporadas");
					Map<Integer, Double> avalTemporadas = listaEpisodios.stream()
																		.filter(e -> e.getAvaliacao() > 0D)
																	    .collect(Collectors.groupingBy(Episodio::getTemporada, 
																	    		 Collectors.averagingDouble(Episodio::getAvaliacao)));
					System.out.println(avalTemporadas);
					
					
					System.out.println("Estatísticas");
					DoubleSummaryStatistics estatistica = listaEpisodios.stream()
														                .filter(e -> e.getAvaliacao() > 0D)
														                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));	
					System.out.println(estatistica);
					System.out.println("Média: " + estatistica.getAverage());
					System.out.println("Qtd: " + estatistica.getCount());
					System.out.println("Min: " + estatistica.getMin());
					System.out.println("Max: " + estatistica.getMax());
					
					IntSummaryStatistics statsTemporadas = listaEpisodios.stream()
														                 .mapToInt(Episodio::getTemporada)
														                 .summaryStatistics();
					System.out.println(statsTemporadas);
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
	
	public void exemploStream() {
		List<String> listaNomes = Arrays.asList("Robson","Kênia","Maria Clara","Jacira","Diogo","Alan");
		listaNomes.stream().sorted().limit(3).forEach(e -> System.out.println(e));
	}
}
