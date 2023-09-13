package com.example.Prova1ConsumidorArthurMartins;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;

public class Consumidor extends ItemCardapio{

    @Value("0")
    private int quantidade;

    @Value("0")
    private static double precototal;

    @NotBlank
    private ItemCardapio itemCardapio;

    public Consumidor(){quantidade = 1;}
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public double getPrecoTotal() {
        return precototal;
    }

    public void setPrecoTotal(double precototal) {
        this.precototal = precototal;
    }

    public ItemCardapio getItemCardapio() {
        return itemCardapio;
    }

    public void setItemCardapio(ItemCardapio itemCardapio) {
        this.itemCardapio = itemCardapio;
    }

    public static double getPrecototal()
    {
        return precototal;
    }
    public static void setPrecototal(double preco)
    {
        precototal = preco;
    }



}
