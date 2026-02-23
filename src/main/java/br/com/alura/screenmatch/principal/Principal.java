package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0){
            var menu = """
                
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar Séries buscadas
                4 - Buscar séries por título
                5 - Buscar séries por ator
                6 - Mostrar top 5 séries
                7 - Buscar série por categoria
                8 - filtrar busca
                9 - buscar episódio por trecho
               10 - top 5 episódios
               11 - buscar por data
               
                0 - Sair
                """;

            System.out.println(menu);

            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1 -> buscarSerieWeb();
                case 2 -> buscarEpisodioPorSerie();
                case 3 -> listarSeriesBuscadas();
                case 4 -> buscarSeriePorTitulo();
                case 5 -> buscarSeriePorAtor();
                case 6 -> buscarTop5Series();
                case 7 -> buscarPorCategoria();
                case 8 -> buscarPorTemporarada();
                case 9 -> buscarEpisodioPorTrecho();
                case 10 -> topEpisodiosPorSerie();
                case 11 -> buscarEpisodioPorData();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        }
    }



    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        dadosSeries.add(dados);
        repositorio.save(serie);
        buscarEpisodioPorSerie();
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome");
        var nomeSerie= leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);


        if (serie.isPresent()){

            var serieEncontrada = serie.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada");
        }


    }

    private void listarSeriesBuscadas() {
        series =  repositorio.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenero))
              .forEach(System.out::println);

    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome");
        var nomeSerie= leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()){
            System.out.println("Dados da série: " + serieBusca.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator para buscar: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Digite o valor mínimo da avaliação para filtrar");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(s -> System.out.println("("+ s.getTitulo() + ") avaliação: " + s.getAvaliacao()));

    }

    private void buscarTop5Series() {
        List<Serie> seriesTop5 = repositorio.findTop5ByOrderByAvaliacaoDesc();
        System.out.println("Top 5 Séries:");
        seriesTop5.forEach(s -> System.out.println("("+ s.getTitulo() + ") avaliação: " + s.getAvaliacao()));
    }

    private void buscarPorCategoria() {
        System.out.println("Digite a categoria para filtrar a busca pela série:");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries por categoria " + nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarPorTemporarada() {
        System.out.println("Digite o número máximo de temporadas da série");
        var totalTemporada = leitura.nextInt();
        System.out.println("Digite o valor mínimo da avaliação para filtrar");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesPorTemporada = repositorio.seriesPorTemporadaEAvaliacao(totalTemporada, avaliacao);

        seriesPorTemporada.forEach(s -> System.out.println("("+ s.getTitulo() + ") Total de Temporadas: " + s.getTotalTemporadas() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o trecho do episódio que deseja buscar: ");
        var trecho = leitura.nextLine();
        List<Episodio> episódioPorTrecho = repositorio.episodioPorTrecho(trecho);
        episódioPorTrecho.forEach(e -> System.out.printf("Série: %s Temporada %s Episódio %s - %s\n",
                e.getSerie().getTitulo(), e.getTemporada(),
                e.getNumeroEpisodio(), e.getTitulo()));
    }
    private void  topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            serieBusca.get();
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodioPorSerie(serie);
            topEpisodios.forEach(e -> System.out.printf("Série: %s Temporada %s Episódio %s - %s Avaliação: %s\n",
                    e.getSerie().getTitulo(), e.getTemporada(),
                    e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodioPorData(){
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento:");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();
            List<Episodio> episodiosData = repositorio.episodioPorSerieAno(serie, anoLancamento);
            episodiosData.forEach(e -> System.out.printf("Série: %s Temporada %s Episódio %s Data de Lançamento: %s - %s\n",
                    e.getSerie().getTitulo(), e.getTemporada(),
                    e.getNumeroEpisodio(), e.getDataLancamento(),e.getTitulo() ));

        }
    }

}