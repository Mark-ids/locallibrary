package com.qathafiii.locallibrary.screens.models

import java.io.Serializable
import java.net.URL

class Book(id:Int, categoryId: Int, title:String, author:String, desc: String?, pdf:String?, bookImage:String?):Serializable {
    var id:Int
    var categoryId:Int
    var title:String
    var author:String
    var desc:String?
    var pdf:String?
    var image:String?
    init {
        this.id = id
        this.categoryId = categoryId
        this.title = title
        this.author = author
        this.desc = desc
        this.pdf = pdf
        this.image = bookImage
    }
}