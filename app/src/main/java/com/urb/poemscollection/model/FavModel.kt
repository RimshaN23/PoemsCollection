package com.urb.poemscollection.model

class FavModel {

    var id: Int? =null
    var name: String? =null
    var thumbnail: String? =null
    var videoUri: String? =null


    constructor(id:Int, name : String , thumbnail : String , videoUri : String ){
        this.id= id
        this.name= name
        this.thumbnail= thumbnail
        this.videoUri= videoUri
    }
}