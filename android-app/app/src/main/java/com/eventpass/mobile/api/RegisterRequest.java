package com.eventpass.mobile.api;

public class RegisterRequest {
    public String nome;
    public String email;
    public String senha;
    public String tipo;
    public RegisterRequest(String nome, String email, String senha, String tipo) {
        this.nome = nome; this.email = email; this.senha = senha; this.tipo = tipo;
    }
}
