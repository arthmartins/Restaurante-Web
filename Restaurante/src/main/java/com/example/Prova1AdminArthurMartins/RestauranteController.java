package com.example.Prova1AdminArthurMartins;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class RestauranteController {

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    ItemCardapioRepository itemcardapioRepository;
    @GetMapping("novo-restaurante")
    public String mostrarFormNovoRestaurante(Restaurante restaurante) {
        return "novo-restaurante";
    }

    @PostMapping("adicionar-restaurante")
    public String adicionarRestaurante(@Valid Restaurante restaurante, BindingResult result) {
        if (result.hasErrors()) {
            return "/novo-restaurante";
        }
        restauranteRepository.save(restaurante);
        return "redirect:/restaurantes";
    }

    @GetMapping("restaurantes")
    public String mostrarRestaurantes(Model model) {
        model.addAttribute("restaurantes", restauranteRepository.findAll());
        return "restaurantes";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormAtualizada(@PathVariable("id") int id, Model model) {
        Restaurante produto = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "O id do restaurante é inválido:" + id));


        model.addAttribute("restaurante", produto);
        return "atualizar-restaurante";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarRestaurante(@PathVariable("id") int id, @Valid Restaurante restaurante,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            restaurante.setId(id);
            return "atualizar-restaurante";
        }
        restauranteRepository.save(restaurante);
        return "redirect:/restaurantes";
    }
    @GetMapping("/remover/{id}")
    public String removerRestaurante(@PathVariable("id") int id,
                                 HttpServletRequest request){
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do produto é inválido:" + id));
        restauranteRepository.delete(restaurante);

        return "redirect:/restaurantes";
    }

}
