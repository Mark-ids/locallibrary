package com.qathafiii.locallibrary.screens.models

import java.io.Serializable

class Category(id:Int, name: String, description:String):Serializable{
    var categoryId = id
    var categoryName = name
    var categoryDescription = description

}