package com.electronichealth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


@Entity
public class MedicalRecord {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String disease;
        private String treatment;
        private String date;
        private String treatmentHistory;
        private String medications;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "patient_id")
        @JsonIgnore
        private Patient patient;

        public MedicalRecord() {}

        public MedicalRecord(String disease, String treatment, String date, String treatmentHistory, String medications) {
                this.disease = disease;
                this.treatment = treatment;
                this.date = date;
                this.treatmentHistory = treatmentHistory;
                this.medications = medications;
        }



        public Patient getPatient() {
                return patient;
        }

        public Long getId() {
                return id;
        }

        public void setPatient(Patient patient) {
                this.patient = patient;
        }

        public String getDisease() {
                return disease;
        }

        public void setDisease(String disease) {
                this.disease = disease;
        }

        public String getTreatment() {
                return treatment;
        }

        public void setTreatment(String treatment) {
                this.treatment = treatment;
        }

        public String getDate() {
                return date;
        }

        public void setDate(String date) {
                this.date = date;
        }

        public String getTreatmentHistory() {
                return treatmentHistory;
        }

        public void setTreatmentHistory(String treatmentHistory) {
                this.treatmentHistory = treatmentHistory;
        }

        public String getMedications() {
                return medications;
        }

        public void setMedications(String medications) {
                this.medications = medications;
        }

        public void setId(Long id) {
                this.id = id;
        }
}
