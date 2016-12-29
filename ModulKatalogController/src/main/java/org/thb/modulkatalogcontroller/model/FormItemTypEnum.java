package org.thb.modulkatalogcontroller.model;

public enum FormItemTypEnum
{
	FILE("Datei"),STRING("String"), NUMBER("Zahl");
	
	private String label;
	
    private FormItemTypEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
