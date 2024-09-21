package com.opsc7311poe.gourmetguru_opscpoe

import java.time.Duration

data class RecipeData (
    var id: String?,
    var name: String?,
    var duration: Duration?,
    var ingredients: List<Ingredient>?,
    var method: String?,
    var isLocked: Boolean?

){
    // No-argument constructor (required by Firebase)
    constructor() : this(null, null, null, null, null, null)
}

data class Ingredient(
    var name: String?,
    var amount: String?
){
    // No-argument constructor (required by Firebase)
    constructor() : this(null, null)
}