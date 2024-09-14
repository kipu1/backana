package com.example.Nutriologa.Analia.Roman.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


public class PagoRequest {
    private String numeroTarjeta;
    private String fechaExpiracion;
    private String cvv;
    private double monto;

    public PagoRequest(String numeroTarjeta, String fechaExpiracion, String cvv, double monto) {
        this.numeroTarjeta = numeroTarjeta;
        this.fechaExpiracion = fechaExpiracion;
        this.cvv = cvv;
        this.monto = monto;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
