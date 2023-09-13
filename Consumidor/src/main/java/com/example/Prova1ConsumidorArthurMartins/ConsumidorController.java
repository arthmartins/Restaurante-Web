package com.example.Prova1ConsumidorArthurMartins;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ConsumidorController {

    private static final String SESSION_PEDIDO = "sessionPedido";

    @Autowired
    RestauranteRepository restauranteRepository;
    @Autowired
    ItemCardapioRepository itemcardapioRepository;

    @GetMapping("/index")
    public String mostrarIndex(){return "index";}

    @GetMapping("restaurantes")
    public String mostrarRestaurantes(Model model) {
        model.addAttribute("restaurantes", restauranteRepository.findAll());
        return "restaurantes";
    }
    @GetMapping("/cardapio/{id}")
    public String mostrarCardapio(@PathVariable("id") int id, Model model) {
        List cardapio = itemcardapioRepository.findByRestauranteId(id);

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + id));

        model.addAttribute("items", cardapio);
        model.addAttribute("restaurante",restaurante);
        return "cardapio";
    }

    @GetMapping("/pedido")
    public String mostrarPedido(Model model, HttpServletRequest request){

        List<Consumidor> pedido =
                (List<Consumidor>)request.getSession().getAttribute(SESSION_PEDIDO);

        List<ItemCardapio> items = (List<ItemCardapio>)itemcardapioRepository.findAll();
        int flag = 0;

        if(!CollectionUtils.isEmpty(pedido)) {
            for (Consumidor con: new ArrayList<Consumidor>(pedido)) {
                flag = 0;
                for(ItemCardapio item: items) {

                    if (con.getItemCardapio().getId() == item.getId()) {

                        if(item.getNome() != con.getItemCardapio().getNome() ||
                        item.getDescricao() != con.getItemCardapio().getDescricao()||
                        item.getPreco() != con.getItemCardapio().getPreco())
                        {
                            con.setItemCardapio(item);
                        }
                        flag = 1;
                        break;

                    }
                }
                if(flag ==0)
                {
                    pedido.remove(con);
                }
                else{
                    Consumidor.setPrecototal(Consumidor.getPrecototal() +
                            con.getItemCardapio().getPreco() * con.getQuantidade());
                }
            }

            model.addAttribute("precoTotal", Consumidor.getPrecototal());
            request.getSession().setAttribute(SESSION_PEDIDO, pedido);

        }
        model.addAttribute("sessionPedido",
                !CollectionUtils.isEmpty(pedido) ? pedido : new ArrayList<>());
        return "pedido";
    }

    @GetMapping("/pedir/{id}")
    public String adicionarPedido(@PathVariable("id") int id,
                                   HttpServletRequest request){

        ItemCardapio item = itemcardapioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do item é inválido: " + id));


        List<Consumidor> pedido = (List<Consumidor>)
                request.getSession().getAttribute(SESSION_PEDIDO);

        boolean flag = false;

        if (CollectionUtils.isEmpty(pedido)) {
            pedido = new ArrayList<>();

        }

        for(Consumidor con: pedido)
        {
            if(con.getItemCardapio().getId()==item.getId())
            {
                con.setQuantidade(con.getQuantidade()+1);
                flag=true;
                break;
            }
        }
        if(!flag)
        {
            Consumidor consumidor = new Consumidor();
            consumidor.setItemCardapio(item);
            pedido.add(consumidor);
        }

        request.getSession().setAttribute(SESSION_PEDIDO, pedido);

        return "redirect:/pedido";

    }
    @GetMapping("/pedido/aumentar/{id}")
    public String aumentarItem(@PathVariable("id") int id,
                                  HttpServletRequest request){
        ItemCardapio itemCardapio = itemcardapioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do contato é inválido: " + id));

        List<Consumidor> pedido = (List<Consumidor>)
                request.getSession().getAttribute(SESSION_PEDIDO);

        for(Consumidor con: pedido)
        {
            if(con.getItemCardapio().getId()==itemCardapio.getId())
            {

                con.setQuantidade(con.getQuantidade()+1);
                break;
            }
        }
        request.getSession().setAttribute(SESSION_PEDIDO, pedido);
        return "redirect:/pedido";

    }

    @GetMapping("/pedido/diminuir/{id}")
    public String diminuirItem(@PathVariable("id") int id,
                               HttpServletRequest request){
        ItemCardapio itemCardapio = itemcardapioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do contato é inválido: " + id));

        List<Consumidor> pedido = (List<Consumidor>)
                request.getSession().getAttribute(SESSION_PEDIDO);

        for(Consumidor con: pedido)
        {
            if(con.getItemCardapio().getId()==itemCardapio.getId())
            {

                con.setQuantidade(con.getQuantidade()-1);
                if(con.getQuantidade()==0)
                {
                    pedido.remove(con);
                    request.getSession().setAttribute(SESSION_PEDIDO, pedido);
                }

                break;
            }
        }
        request.getSession().setAttribute(SESSION_PEDIDO, pedido);
        return "redirect:/pedido";

    }
}
