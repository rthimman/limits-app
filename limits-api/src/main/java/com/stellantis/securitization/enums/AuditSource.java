package com.stellantis.securitization.enums;

public enum AuditSource {
    SIMULATION_POPIN,
    EXCEL_IMPORT,
    INLINE_EDIT;

    @Override
    public String toString() {
        return name();
    }

}
