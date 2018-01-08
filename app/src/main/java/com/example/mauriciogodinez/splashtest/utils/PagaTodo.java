package com.example.mauriciogodinez.splashtest.utils;

/*
 * Created by mauriciogodinez on 19/05/17.
 */

public class PagaTodo {
    /** agente pagatodo */
    private String agente;
    /** error pagatodo */
    private String error;
    /** usuario pagatodo */
    private int id_user;
    /** token pagatodo */
    private String token;

    /**
     *
     * @param agente dato entregado en la respuesta de JSON
     * @param error dato entregado si hay error en la consulta
     * @param id_user dato entregado con el numero de usuario
     * @param token entregado en la consulta
     */
    public PagaTodo(String agente, String error, int id_user, String token) {
        this.agente = agente;
        this.error = error;
        this.id_user = id_user;
        this.token = token;
    }

    public String getAgente() {
        return agente;
    }

    public String getError() {
        return error;
    }

    public int getId_user() {
        return id_user;
    }

    public String getToken() {
        return token;
    }
}
