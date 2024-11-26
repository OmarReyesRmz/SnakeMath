package com.example.snakemath

import android.graphics.Bitmap

class Manzanas {

    // Atributos de la clase
    var imagen: Int = 0   // Imagen asociada a la manzana
    var tipo: String = ""          // Tipo de la manzana (e.g., "número", "operación")
    var numero: Int = 0
    var x: Float = 0F         // Posición x de la manzana
    var y: Float = 0F      // Posición y de la manzana// Valor numérico asociado a la manzana
    var bandera1: Boolean = false  // Bandera 1 inicializada en falso
    var bandera2: Boolean = false  // Bandera 2 inicializada en falso

    // Constructor principal
    constructor(imagen: Int, tipo: String, numero: Int) {
        this.imagen = imagen
        this.tipo = tipo
        this.numero = numero
    }

    constructor(imagen: Int, tipo: String, numero: Int, x: Float, y: Float) {
        this.imagen = imagen
        this.tipo = tipo
        this.numero = numero
        this.x = x
        this.y = y
    }

    constructor(imagen: Int,tipo: String,numero: Int,x: Float,y: Float,bandera1: Boolean){
        this.imagen = imagen
        this.tipo = tipo
        this.numero = numero
        this.x = x
        this.y = y
        this.bandera1 = bandera1
    }

}
