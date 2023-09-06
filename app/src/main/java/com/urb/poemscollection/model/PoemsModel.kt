package com.urb.poemscollection.model

 class PoemsModel{

    var id: String? =null
    var name: String? =null
    var thumbnail: String? =null
    var videoUri: String? =null

     constructor(){

     }

    constructor(id:String, name : String , thumbnail : String , videoUri : String ){
        this.id= id
        this.name= name
        this.thumbnail= thumbnail
        this.videoUri= videoUri
    }

}





