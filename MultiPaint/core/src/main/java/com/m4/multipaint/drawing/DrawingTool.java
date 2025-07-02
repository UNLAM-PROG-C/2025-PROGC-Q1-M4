package com.m4.multipaint.drawing;

public enum DrawingTool
{

    BRUSH("Lapiz", 1),
    LINE("Linea", 2),
    RECTANGLE("Cuadrado", 3),
    CIRCLE("Circulo", 4);

    public final String label;
    public final int value;

    DrawingTool(String label, int value)
    {
        this.label = label;
        this.value = value;
    }
}
