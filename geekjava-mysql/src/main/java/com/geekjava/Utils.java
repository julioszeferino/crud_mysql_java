package com.geekjava;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

public class Utils {

    private String DB_HOST;
    private String DB_PORT;
    private String DB_NAME;
    private String DB_USER;
    private String DB_PASS;


    // criar um construtor com as variaveis do BD
    public Utils() {

        Dotenv dotenv = Dotenv.configure().load();
        for (DotenvEntry e : dotenv.entries()) {
            System.out.println(e);
        }
        this.DB_HOST = dotenv.get("DB_HOST");
        this.DB_PORT = dotenv.get("DB_PORT");
        this.DB_NAME = dotenv.get("DB_NAME");
        this.DB_USER = dotenv.get("DB_USER");
        this.DB_PASS = dotenv.get("DB_PASS");
    }

    // criar um metodo para retornar a string de conexao
    public String criaStringConexao() {
        return "jdbc:mysql://" + this.DB_HOST + ":" + this.DB_PORT + "/" + this.DB_NAME + "?useSSL=false";
    }

    public String getUsuario() {
        return this.DB_USER;
    }

    public String getSenha() {
        return this.DB_PASS;
    }
}
