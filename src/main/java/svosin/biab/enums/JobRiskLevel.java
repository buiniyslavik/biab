package svosin.biab.enums;

public enum JobRiskLevel {
    JOBRISK_HIGH(0.0),
    JOBRISK_MEDIUM(0.16),
    JOBRISK_LOW(0.55);

    public final Double rate;

    JobRiskLevel(Double rate) {
        this.rate = rate;
    }
}
