package com.example.fitnessapplication.model


class Exercise {
    var key: String? = null
    var name: String? = null
    var description: String? = null
    var countOfRepeat: Int? = null
    var countOfApproaches: Int? = null
    var bodyPart: String? = null
    var day: String? = null

    constructor() {}

    constructor(key: String,
                name: String,
                description: String,
                countOfRepeat: Int,
                countOfApproaches:Int,
                bodyPart: String,
                day: String) {
        this.key = key
        this.name = name
        this.description = description
        this.countOfRepeat = countOfRepeat
        this.countOfApproaches = countOfApproaches
        this.bodyPart = bodyPart
        this.day = day
    }

    constructor(
                name: String,
                description: String,
                countOfRepeat: Int,
                countOfApproaches: Int,
                bodyPart: String,
                day: String) {
        this.name = name
        this.description = description
        this.countOfRepeat = countOfRepeat
        this.countOfApproaches = countOfApproaches
        this.bodyPart = bodyPart
        this.day = day
    }


    fun toMap(): Map<String, Any> {

        val map = HashMap<String, Any>()
        name?.let { map.put("name", it) }
        description?.let { map.put("description", it) }
        countOfRepeat?.let { map.put("countOfRepeat", it) }
        countOfApproaches?.let { map.put("countOfApproaches", it) }
        bodyPart?.let { map.put("bodyPart", it) }
        day?.let { map.put("day", it)}

        return map
    }
}