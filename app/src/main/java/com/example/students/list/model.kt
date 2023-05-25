package com.example.students.list

class model {
    var name: String? = null
    var nap: String? = null
    val balinf: String? = null
    val balfiz: String? = null
    val balrus: String? = null
    val balprofmat: String? = null
    val balhim: String? = null
    val balbio: String? = null
    val balmat: String? = null
    val balobs: String? = null
    val balist: String? = null
    val balin: String? = null
    var forma:String? = null
    var coord1:String? = null
    var coord2:String? = null

    constructor() {}
    constructor(name: String?, nap: String?,forma: String?,coord1: String?,coord2: String?) {
        this.name = name
        this.nap = nap
        this.forma = forma
        this.coord1 = coord1
        this.coord2 = coord2
    }
}
