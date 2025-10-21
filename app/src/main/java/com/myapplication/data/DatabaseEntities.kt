package com.myapplication.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.DrawableRes
import com.myapplication.R


@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val email: String,
    val password: String
)

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val precio: Int,
    @DrawableRes
    val imagen: Int,
    val descripcion: String,
    val categoria: String
)


val Productos = listOf(
    Producto(
        id = 1,
        nombre = "PC GAMER RDY Y70 TI B04",
        precio = 2_499_000,
        imagen = R.drawable.pc2,
        descripcion = "Intel® Core™ i9-14900KF, MSI PRO Z790-A MAX Wi-Fi",
        categoria = "PC Gamer"
    ),
    Producto(
        id = 3,
        nombre = "PC GAMER RDY Y70 Liquid Hybrid Max",
        precio = 2_000_000,
        imagen = R.drawable.pc3,
        descripcion = "Intel® Core™ Ultra 9, ASUS ROG STRIX Z890-E WIFI",
        categoria = "PC Gamer"
    ),
    Producto(
        id = 4,
        nombre = "TECLADO GAMER REDRAGON UCAL K673",
        precio = 62_350,
        imagen = R.drawable.teclado1,
        descripcion = "Keycaps PBT Premium",
        categoria = "Teclado"
    ),
    Producto(
        id = 5,
        nombre = "TECLADO REDRAGON K530W DRACONIC BLC",
        precio = 70_350,
        imagen= R.drawable.tecladob1,
        descripcion = "Keycaps PBT Premium",
        categoria = "Teclado"
    ),
    Producto(
        id = 6,
        nombre = "GAMING MOUSE Logitech G305",
        precio = 68_000,
        imagen = R.drawable.mouse1loghitech,
        descripcion = "Sensor HERO",
        categoria = "Mouse"
    ),
    Producto(
        id = 7,
        nombre = "SILLA GAMER COUGAR TERMINATOR",
        precio = 265_000,
        imagen = R.drawable.sillaterminator,
        descripcion = "Polipiel Hyper-Dura",
        categoria = "Silla"
    ),
    Producto(
        id = 8,
        nombre = "CONSOLA PS5",
        precio = 699_000,
        imagen = R.drawable.ps5,
        descripcion = "500 GB, lector Blu-ray 4K",
        categoria = "Consola"
    ),
    Producto(
        id = 9,
        nombre = "CONTROL GAME PAD XBOX",
        precio = 55_000,
        imagen = R.drawable.controlxbox,
        descripcion = "Inalámbrico Sky Cipher",
        categoria = "Control"
    )
)
