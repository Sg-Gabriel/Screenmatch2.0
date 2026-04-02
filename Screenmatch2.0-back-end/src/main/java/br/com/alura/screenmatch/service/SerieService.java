package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDto;
import br.com.alura.screenmatch.dto.SerieDto;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repositorio;

    public List<SerieDto> obterTodasAsSeries (){
        return converteDados(repositorio.findAll());

    }

    public List<SerieDto> obterTop5Series() {
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());

    }

    private List<SerieDto> converteDados(List<Serie> series){
        return series.stream()
                .map(s -> new SerieDto(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getGenero(), s.getAvaliacao(),  s.getAtores(), s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDto> obterLancamentos() {
        return converteDados(repositorio.encontrarEpisodiosMaisRecentes());
    }

    public SerieDto obterId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDto(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getGenero(), s.getAvaliacao(),  s.getAtores(), s.getPoster(), s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDto> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDto(e.getTitulo(), e.getNumeroEpisodio(), e.getTemporada()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDto> obterTemporadasPorNumero(Long id, Long numero) {
        return  repositorio.obterEpisodioPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDto(e.getTitulo(), e.getNumeroEpisodio(), e.getTemporada()))
                .collect(Collectors.toList());
    }

    public List<SerieDto> obterCategoria(String genero) {
        Categoria categoria = Categoria.fromPortugues(genero);
        return converteDados(repositorio.findByGenero(categoria));
    }

    public List<EpisodioDto> obterTop5Episodios(Long id) {
        return repositorio.buscarTopEpisodiosPorSerie(id)
                .stream()
                .map(e -> new EpisodioDto(e.getTitulo(), e.getNumeroEpisodio(), e.getTemporada()))
                .collect(Collectors.toList());
    }
}
