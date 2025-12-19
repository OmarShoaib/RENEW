package edu.aku.omarshoaib.renew.global;

public class KishGrid {

    // Kish grid generator
    public static void genKishGrid(int householdCount, int maxEligibleCount) {
        AppConstants.KISH_GRID = new int[householdCount][maxEligibleCount];

        for (int i = 0; i < maxEligibleCount; i++) {
            for (int j = 0; j < householdCount; j++) {
                int mod = (j + 1) % (i + 1);
                AppConstants.KISH_GRID[j][i] = mod > 0 ? mod : (i + 1);
            }
        }
    }
}
