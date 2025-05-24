# MedReminderApp

## Description

MedReminderApp est une application Android conçue pour aider les utilisateurs à gérer rigoureusement leur prise médicamenteuse grâce à des rappels programmés. Cette application permet de créer, consulter et modifier des rappels pour ne jamais oublier de prendre ses médicaments.

---

## Fonctionnalités principales

- Création et gestion des médicaments et de leurs rappels
- Notifications programmées pour chaque prise
- Interface simple et intuitive avec une navigation fluide
- Gestion des utilisateurs avec authentification (login / inscription)
- Visualisation des rappels du jour sur la page d'accueil

---

## Analyse des besoins

### Fonctionnels

- Permettre à l'utilisateur de s'inscrire et se connecter
- Ajouter, modifier et supprimer des médicaments
- Programmer des rappels à heures fixes
- Afficher la liste des rappels du jour
- Recevoir des notifications push lors des prises

### Non fonctionnels

- Interface utilisateur réactive et accessible
- Fonctionnement hors ligne après synchronisation
- Sécurisation des données utilisateur

---

## Technologies utilisées

- Langage : Java
- Architecture : MVVM
- Base de données locale : Room (SQLite)
- Notifications : AlarmManager / NotificationManager
- Navigation : Jetpack Navigation Component
- UI : XML layouts

---

## Installation

1. Cloner le dépôt :

```bash
git clone https://github.com/ton-utilisateur/MedReminderApp.git
