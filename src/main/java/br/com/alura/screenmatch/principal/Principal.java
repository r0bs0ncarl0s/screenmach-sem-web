package br.com.alura.screenmatch.principal;

import java.util.Scanner;

import br.com.alura.screenmatch.service.ConsumoAPI;

public class Principal {
	
	private Scanner leitura = new Scanner(System.in);
	private final String ENDERECO = "https://www.omdbapi.com/?t=";
	private final String API_KEY = "&apikey=6585022c";
	private ConsumoAPI consumo = new ConsumoAPI();  
	public void exibeMenu() {
		System.out.println("Digite o nome da série");
		var nomeSerie = leitura.nextLine();
		var json = consumo.obterDados(ENDERECO + nomeSerie.replace("", "+") + API_KEY);		
	}
}
