package com.granzonamarciana.database;

import androidx.room.TypeConverter;
import com.granzonamarciana.entity.EstadoSolicitud;

public class EstadoSolicitudConverter {
    @TypeConverter
    public static EstadoSolicitud fromString(String value) {
        return value == null ? null : EstadoSolicitud.valueOf(value);
    }

    @TypeConverter
    public static String estadoToString(EstadoSolicitud estado) {
        return estado == null ? null : estado.name();
    }
}