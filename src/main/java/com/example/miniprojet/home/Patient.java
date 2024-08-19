package com.example.miniprojet.home;

public class Patient {
        private int cin;
        private String nom;
        private String prenom;
        private String sexe;
        private String tel;
        public Patient(int cin, String nom, String prenom, String sexe, String tel) {
            this.cin = cin;
            this.nom = nom;
            this.prenom = prenom;
            this.sexe = sexe;
            this.tel = tel;
        }
        public int getCin() {
            return cin;
        }
        public String getNom() {
            return nom;
        }
        public String getPrenom() {
            return prenom;
        }
        public String getSexe() {
            return sexe;
        }
        public String getTel() {
            return tel;
        }
        @Override
    public String toString() {
            return "Patient{"+
                    "cin"+cin+
                    "nom"+nom+
                    "prenom"+prenom+
                    "sexe"+sexe+
                    "tel"+tel+"}";
        }


}
