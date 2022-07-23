package com.kerollosragaie.workoutboost.models

class Exercise(
    private var id:Int,
    private var name:String,
    private var image:Int,
    private var practisingTime:Int=4,
    private var isCompleted:Boolean = false,
    private var isSelected:Boolean = false
){
    fun getId():Int{
        return this.id
    }

    fun setId(id:Int){
        this.id = id
    }

    fun getName():String{
        return this.name
    }

    fun setName(name:String){
        this.name = name
    }

    fun getImage():Int{
        return this.image
    }

    fun setImage(image:Int){
        this.image = image
    }

    fun getPractisingTime():Int{
        return this.practisingTime
    }

    fun setPractisingTime(practisingTime:Int){
        this.practisingTime = practisingTime
    }

    fun getIsCompleted():Boolean{
        return this.isCompleted
    }

    fun setIsCompleted(isCompleted:Boolean){
        this.isCompleted = isCompleted
    }

    fun getIsSelected():Boolean{
        return this.isSelected
    }

    fun setIsSelected(isSelected: Boolean){
        this.isSelected = isSelected
    }
}