package com.example.safemedicare;

import java.util.ArrayList;

public class MedicationLog {

    ArrayList<Medication> MedicationList = new ArrayList<Medication>();

    public MedicationLog(ArrayList<Medication> medicationList) {
        MedicationList = medicationList;
    }

    public MedicationLog() {

    }

    public void AddMedication(Medication medication) {
        MedicationList.add(medication);

    }

    public void DeleteMedication(Medication medication) {
        MedicationList.remove(medication);
    }

    //////////////////////////////////// not important ?  //////////////////////////////////
    public Medication DisplayMedicationDetails() {
        if (MedicationList.size() != 0)
            for (int i = 0; i < MedicationList.size(); i++) {
                return MedicationList.get(i);
            }
        return null;
    }

    public void ShowAllMedication() {
        if (MedicationList.size() != 0)
            for (int i = 0; i < MedicationList.size(); i++) {
                System.out.println(MedicationList.get(i));
            }
    }
//////////////////////////////////// not important ?  //////////////////////////////////

    public ArrayList<Medication> getMedicationList() {
        return MedicationList;
    }

    public void setMedicationList(ArrayList<Medication> medicationList) {
        MedicationList = medicationList;
    }
}
