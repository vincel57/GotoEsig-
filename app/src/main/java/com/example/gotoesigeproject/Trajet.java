package com.example.gotoesigeproject;

import java.time.LocalDateTime;

public class Trajet {
    private String pointDepart;
    private String date;
    private String heure;
    private String retardTolere;
    private String places;
    private String contribution;
    private String duree;
    private String distance;
    private String email; // Nouvel attribut pour l'ID utilisateur



    // Constructeur sans arguments (requis par Firestore)
    public Trajet() {
    }

    // Constructeur avec arguments
    public Trajet(String pointDepart, String date, String heure, String retardTolere, String places, String contribution, String duree, String distance, String email) {
        this.pointDepart = pointDepart;
        this.date = date;
        this.heure = heure;
        this.retardTolere = retardTolere;
        this.places = places;
        this.contribution = contribution;
        this.duree = duree;
        this.distance = distance;
        this.email = email;
    }



    // Getters et Setters pour chaque champ
    public String getPointDepart() {
        return pointDepart;
    }

    public void setPointDepart(String pointDepart) {
        this.pointDepart = pointDepart;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getRetardTolere() {
        return retardTolere;
    }

    public void setRetardTolere(String retardTolere) {
        this.retardTolere = retardTolere;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getContribution() {
        return contribution;
    }

    public void setContribution(String contribution) {
        this.contribution = contribution;
    }
    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
