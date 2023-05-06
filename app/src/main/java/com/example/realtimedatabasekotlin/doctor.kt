package com.example.realtimedatabasekotlin

data class doctor(
    var id:Int?=null,
    var name: String?=null,

)
data class productClass(
    var id:String?="",
    var image:String?="",
    var nameOfProduct:String?="",
    var priceOfProduct:String?="",
    var districtOfProduct:String?="",
    var dateOfProduct:String?="",
)

data class swapClass(

var id:String?="",
var swapId:String?="",
var image:String?="",
var nameOfProduct:String?="",
var priceOfProduct:String?="",
var districtOfProduct:String?="",
var dateOfProduct:String?="",
)
