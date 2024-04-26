package uk.ac.tees.mad.d3840711

data class SheModel(      val name: String,
                          val email : String,
                          val emergencyPhone : String,
                          val emergencyMessage: String,
                          val image_url: String,
                          val lat : Double,
                          val long : Double) {
    constructor() : this("", "", "", "", "" ,0.0,0.0)
}