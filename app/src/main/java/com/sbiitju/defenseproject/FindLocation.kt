package com.sbiitju.defenseproject


internal object FindLocation {
    fun distance(
        lat1: Double,
        lat2: Double, lon1: Double,
        lon2: Double
    ): Double { // The math module contains a function // named toRadians which converts from // degrees to radians.
        var lat1 = lat1
        var lat2 = lat2
        var lon1 = lon1
        var lon2 = lon2
        lon1 = Math.toRadians(lon1)
        lon2 = Math.toRadians(lon2)
        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)
        // Haversine formula
        val dlon = lon2 - lon1
        val dlat = lat2 - lat1
        val a = (Math.pow(Math.sin(dlat / 2), 2.0)
                + (Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2.0)))
        val c = 2 * Math.asin(Math.sqrt(a))
        // Radius of earth in kilometers. Use 3956 // for miles
        val r = 6371.0
        // calculate the result
        return c * r
    }

    // driver code
    @JvmStatic
    fun main(args: Array<String>) {
        val lat1 = 53.32055555555556
        val lat2 = 53.31861111111111
        val lon1 = -1.7297222222222221
        val lon2 = -1.6997222222222223
        println(
            distance(
                lat1, lat2,
                lon1, lon2
            ).toString() + " K.M"
        )
    }
}