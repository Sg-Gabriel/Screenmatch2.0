package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDto;
import br.com.alura.screenmatch.dto.SerieDto;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDto> obterSeries() {
      return service.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDto> obterTop5Series(){
        return service.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDto> obterLancamentos(){
        return service.obterLancamentos();
    }
    @GetMapping("/{id}")
    public SerieDto obterId(@PathVariable Long id){
        return service.obterId(id);
    }
    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDto> obterTodasTemporadas(@PathVariable Long id){
        return service.obterTodasTemporadas(id);
    }
    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDto> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Long numero){
        return service.obterTemporadasPorNumero(id, numero);
    }
    @GetMapping("/categoria/{genero}")
    public List<SerieDto> obterCategoria(@PathVariable String genero){
        return service.obterCategoria(genero);
    }
    @GetMapping("/{id}/temporadas/top")
    public List<EpisodioDto> obterTop5Episodios(@PathVariable Long id){
        return service.obterTop5Episodios(id);
    }


}
