package com.example.safemedicare;

public class Medication {
    String MedicationName;
    int Med_numberOfTime, Med_DoseAmount;

    public Medication() {
    }

    public Medication(String medicationName, int med_numberOfTime, int med_DoseAmount) {
        MedicationName = medicationName;
        Med_numberOfTime = med_numberOfTime;
        Med_DoseAmount = med_DoseAmount;
    }

    public String getMedicationName() {
        return MedicationName;
    }

    public void setMedicationName(String medicationName) {
        MedicationName = medicationName;
    }

    public int getMed_numberOfTime() {
        return Med_numberOfTime;
    }

    public void setMed_numberOfTime(int med_numberOfTime) {
        Med_numberOfTime = med_numberOfTime;
    }

    public int getMed_DoseAmount() {
        return Med_DoseAmount;
    }

    public void setMed_DoseAmount(int med_DoseAmount) {
        Med_DoseAmount = med_DoseAmount;
    }


}
