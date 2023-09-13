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
public class ItemCardapioController{

    private static final String SESSION_PEDIDO = "sessionPedido";

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    ItemCardapioRepository itemcardapioRepository;

    @GetMapping("/index")
    public String mostrarIndex(){return "index";}

    @GetMapping("/cardapio/{id}")
    public String mostrarCardapio(@PathVariable("id") int id,Model model) {
        List cardapio = itemcardapioRepository.findByRestauranteId(id);
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + id));


        model.addAttribute("items", cardapio);
        model.addAttribute("restaurante",restaurante);
        return "cardapio";
    }

    @GetMapping("/novo-itemcardapio/{id}")
    public String mostrarFormNovoItemCardapio(ItemCardapio itemCardapio,@PathVariable("id") int id,Model model){
        model.addAttribute("idRest",id);
        return "/novo-itemcardapio";
    }

    @PostMapping("/adicionar-itemcardapio/{id}")
    public String adicionarItemCardapio(@PathVariable("id") int id,@Valid ItemCardapio itemCardapio, BindingResult result) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do produto é inválido:" + id));
        itemCardapio.setRestaurante(restaurante);
        if (result.hasErrors()) {
            return "/novo-itemcardapio";
        }
        itemcardapioRepository.save(itemCardapio);
        return "redirect:/cardapio/{id}";
    }

    @GetMapping("/editar-item/{id}")
    public String mostrarFormAtualizadaItemCardapio(@PathVariable("id") int id,
                                                     Model model) {
        ItemCardapio itemCardapio = itemcardapioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "O id do item é inválido:" + id));

        model.addAttribute("item", itemCardapio);

        return "atualizar-itemcardapio";
    }

    @PostMapping("/{idRest}/atualizaritem/{id}")
    public String atualizarItemCardapio(@PathVariable("id") int id,
                                        @PathVariable("idRest") int idRest, @Valid ItemCardapio itemCardapio,
                                        BindingResult result, Model model) {
        if (result.hasErrors()) {
            itemCardapio.setId(id);
            return "atualizar-itemcardapio";
        }

        Restaurante restaurante = restauranteRepository.findById(idRest)
                .orElseThrow(() -> new IllegalArgumentException("O id do produto é inválido:" + id));
        itemCardapio.setRestaurante(restaurante);
        itemcardapioRepository.save(itemCardapio);
        return "redirect:/cardapio/{idRest}";
    }

    @GetMapping("cardapio/{idRest}/remover/{id}")
    public String removerItemCardapio(@PathVariable("id") int id, @PathVariable("idRest") int idRest,
                                     HttpServletRequest request){
        ItemCardapio itemCardapio = itemcardapioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "O id do item é inválido:" + id));
        itemcardapioRepository.delete(itemCardapio);

        return "redirect:/cardapio/{idRest}";
    }

}


